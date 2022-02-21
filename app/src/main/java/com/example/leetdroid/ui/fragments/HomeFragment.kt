package com.example.leetdroid.ui.fragments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.leetdroid.R
import com.example.leetdroid.adapter.ContestPagerAdapter
import com.example.leetdroid.api.ContestApi
import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.api.URL
import com.example.leetdroid.data.dao.ContestDao
import com.example.leetdroid.data.db.ContestsDatabase
import com.example.leetdroid.data.entitiy.Contest
import com.example.leetdroid.data.entitiy.DailyQuestion
import com.example.leetdroid.data.repository.ContestRepository
import com.example.leetdroid.data.viewModel.ContestViewModel
import com.example.leetdroid.data.viewModel.DailyQuestionViewModel
import com.example.leetdroid.databinding.FragmentHomeBinding
import com.example.leetdroid.model.DailyQuestionModel
import com.example.leetdroid.model.RandomQuestionModel
import com.example.leetdroid.notification.DailyQuestionAlarmReceiver
import com.example.leetdroid.utils.Constant
import com.example.leetdroid.utils.Converters
import com.example.leetdroid.utils.Converters.DailyQuestionConverter.fromDailyQuestion
import com.example.leetdroid.utils.Converters.DailyQuestionConverter.fromStringDailyQuestion
import com.example.leetdroid.utils.Converters.DailyQuestionDailyConverter.fromDailyQuestionDaily
import com.example.leetdroid.utils.Converters.DailyQuestionDailyConverter.fromStringDailyQuestionDaily
import com.example.leetdroid.utils.Converters.DailyQuestionTagsConverter.fromDailyQuestionTags
import com.example.leetdroid.utils.JsonUtils
import com.example.leetdroid.utils.SharedPreferences
import com.example.leetdroid.utils.extensions.copyToClipboard
import com.example.leetdroid.utils.extensions.showSnackBarWithAction
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.google.gson.JsonArray
import kotlinx.coroutines.launch
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var contestViewModel: ContestViewModel
    private lateinit var dailyViewModel: DailyQuestionViewModel
    private lateinit var generalErroView: View
    private lateinit var contestDB: ContestDao
    private lateinit var contestRepository: ContestRepository
    private lateinit var contestPagerAdapter: ContestPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val rootView = fragmentHomeBinding.root

        generalErroView = rootView.findViewById(R.id.general_error_view)

        createNotificationChannel()
        setAlarm(requireContext())

        contestDB = ContestsDatabase.getInstance(requireActivity().application).contestDao()
        contestRepository = ContestRepository(contestDB)

        fragmentHomeBinding.contestProgressBar.visibility = View.VISIBLE
        fragmentHomeBinding.questionProgressBar.visibility = View.VISIBLE
        contestViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ContestViewModel::class.java]

        dailyViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[DailyQuestionViewModel::class.java]

        sharedPreferences = SharedPreferences(requireContext())

        // check if contest timer has been ended or app is newly installed
        if (sharedPreferences.timerEnded || !sharedPreferences.contestsInserted) {
            getContestList()
        } else {
            displayContests()
        }

        // check if questions has been loaded, if yes then only load new question
        if (!sharedPreferences.dailyQuestionLoaded) {
            loadDailyQuestion()
            setupDailyQuestion()
            sharedPreferences.dailyQuestionLoaded = true
        } else {
            setupDailyQuestion()
        }

        fragmentHomeBinding.randomQuestionLayout.setOnClickListener {
            loadRandomQuestion()
        }

        fragmentHomeBinding.allQuestionsLayout.setOnClickListener {
            fragmentHomeBinding.root.findNavController().navigate(R.id.allQuestionsFragment)
        }

        if (!sharedPreferences.statusShown) {
            spotlightShowCase(
                fragmentHomeBinding.root,
                "Upcoming contest details",
                getString(R.string.upcoming_contest_details),
                2,
                fragmentHomeBinding.viewPager.id
            )
        }
        return rootView
    }

    // show the help screen with proper description
    private fun spotlightShowCase(
        contentView: View,
        head: String,
        content: String,
        nextTarget: Int,
        id: Int
    ) {
        GuideView.Builder(context)
            .setTitle(head)
            .setContentText(content)
            .setGravity(Gravity.center)
            .setTargetView(contentView.findViewById(id))
            .setContentTextSize(12)
            .setTitleTextSize(16)
            .setDismissType(DismissType.outside)
            .setGuideListener {
                when (nextTarget) {
                    2 -> spotlightShowCase(
                        contentView,
                        "Add Reminder",
                        getString(R.string.add_reminder_details),
                        3,
                        fragmentHomeBinding.viewPager.id
                    )
                    3 -> spotlightShowCase(
                        contentView,
                        "Problem Set",
                        getString(R.string.problem_set_details),
                        4,
                        fragmentHomeBinding.allQuestionsLayout.id
                    )
                    4 -> spotlightShowCase(
                        contentView,
                        "Random Questions",
                        getString(R.string.random_question_details),
                        5,
                        fragmentHomeBinding.randomQuestionLayout.id
                    )
                    5 -> spotlightShowCase(
                        contentView,
                        "Daily Challenge",
                        getString(R.string.daily_challenge_details),
                        6,
                        R.id.daily_question_relative_layout
                    )
                    6 -> sharedPreferences.statusShown = true
                }
            }
            .build()
            .show()
    }

    private fun setupDailyQuestion() {
        dailyViewModel.dailyQuestion.observe(viewLifecycleOwner) { it ->
            it?.let {
                displayQuestion(it)
            }
        }
    }

    // sets up the daily question
    private fun displayQuestion(dailyQuestion: DailyQuestion) {
        val dailyQuestionJson = fromStringDailyQuestion(dailyQuestion.question)!!
        val activeDailyJson =
            fromStringDailyQuestionDaily(dailyQuestion.activeDailyCodingChallengeQuestion)!!
        fragmentHomeBinding.dailyQuestionTitle.text = dailyQuestionJson.title
        // shows the date in words
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate: Date
        val formattedDate: String
        try {
            parsedDate = inputFormat.parse(activeDailyJson.date.toString())!!
            formattedDate = SimpleDateFormat("d MMM, yy", Locale.getDefault()).format(parsedDate)
            fragmentHomeBinding.dailyQuestionDate.text = formattedDate
        } catch (exception: ParseException) {
            Log.d(Constant.TAG(HomeFragment::class.java).toString(), "$exception occurred!")
            fragmentHomeBinding.dailyQuestionDate.text =
                SimpleDateFormat("d MMM, yy", Locale.getDefault()).format(
                    Date()
                )
        }
        val link = "${URL.leetcode}${activeDailyJson.link.toString()}"

        // open link
        fragmentHomeBinding.dailyQuestionShare.setOnClickListener {
            shareLink(link)
        }

        // share link
        fragmentHomeBinding.dailyQuestionLink.setOnClickListener {
            openLink(link)
        }

        // show difficulty
        setDifficulty(dailyQuestionJson.difficulty.toString())

        fragmentHomeBinding.dailyQuestionLayout.setOnClickListener {
            // open question
            openQuestion(
                dailyQuestionJson.titleSlug,
                dailyQuestionJson.hasSolution,
                dailyQuestionJson.frontendQuestionId
            )
        }
    }

    override fun onResume() {
        super.onResume()
        fragmentHomeBinding.viewPager.adapter
    }

    // setting the text and color of difficulty button
    private fun setDifficulty(difficulty: String) {
        fragmentHomeBinding.dailyQuestionDifficulty.text = difficulty
        when (difficulty) {
            "Easy" -> {
                fragmentHomeBinding.dailyQuestionDifficulty.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.easy_difficulty
                    )
                )
            }
            "Medium" -> {
                fragmentHomeBinding.dailyQuestionDifficulty.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.medium_difficulty
                    )
                )
            }
            "Hard" -> {
                fragmentHomeBinding.dailyQuestionDifficulty.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.hard_difficulty
                    )
                )

            }
        }
    }

    // opening the question in questionItemFragment
    private fun openQuestion(
        questionTitleSlug: String?,
        questionHasSolution: Boolean?,
        questionID: String?
    ) {

        val bundle = bundleOf(
            "questionTitleSlug" to questionTitleSlug,
            "questionHasSolution" to questionHasSolution,
            "questionID" to questionID
        )
        fragmentHomeBinding.root.findNavController()
            .navigate(R.id.action_homeFragment_to_questionFragment, bundle)
    }

    // gets the contest list
    private fun getContestList() {
        val apiInterface = ContestApi.create().getContent()

        apiInterface.enqueue(object : Callback<JsonArray> {
            override fun onResponse(Call: Call<JsonArray>?, response: Response<JsonArray>?) {
                if (response?.body() != null) {
                    val body = response.body()!!
                    Log.d(Constant.TAG(HomeFragment::class.java).toString(), body.toString())

                    saveContests(body)

//                    displayContests()
                }
            }

            override fun onFailure(call: Call<JsonArray>?, throwable: Throwable) {
                Log.d(Constant.TAG(HomeFragment::class.java).toString(), "requestFailed", throwable)
            }
        })
    }

    // saves the list in local database
    private fun saveContests(body: JsonArray) {
        val contestList =
            Converters.ContestConverter.fromStringContest(body.toString())!!

        val weeklyContest = Contest(
            contestList[0].name,
            contestList[0].url,
            contestList[0].duration,
            contestList[0].start_time,
            contestList[0].end_time,
            contestList[0].in_24_hours,
            contestList[0].status,
        )
        val biWeeklyContest = Contest(
            contestList[1].name,
            contestList[1].url,
            contestList[1].duration,
            contestList[1].start_time,
            contestList[1].end_time,
            contestList[1].in_24_hours,
            contestList[1].status,
        )
        val preferences = SharedPreferences(requireContext())
        preferences.contestsInserted = false
        lifecycleScope.launch {
            if (!preferences.contestsInserted && !preferences.timerEnded) {
                insertContests(biWeeklyContest, weeklyContest)
            } else {
                updateContests(biWeeklyContest, weeklyContest)
            }
        }
    }

    // updates the contests
    private fun updateContests(biWeeklyContest: Contest, weeklyContest: Contest) {
        SharedPreferences(requireContext()).timerEnded = false
        biWeeklyContest.id = 1
        contestViewModel.updateContest(biWeeklyContest)

        weeklyContest.id = 2
        contestViewModel.updateContest(weeklyContest)
        displayContests()
    }

    // inserts the contests in database
    private fun insertContests(biWeeklyContest: Contest, weeklyContest: Contest) {
        val preferences = SharedPreferences(requireContext())
        preferences.contestsInserted = true
        lifecycleScope.launch {
            if (contestRepository.insertContest(biWeeklyContest) != -1L
                &&
                contestRepository.insertContest(weeklyContest) != -1L
            ) {
                displayContests()
            }
        }
//        contestViewModel.addContest(biWeeklyContest)
//        contestViewModel.addContest(weeklyContest)
    }

    // displays the contests from database
    private fun displayContests() {
        val contestList = ArrayList<Contest>()
        lifecycleScope.launch {
            try {
                val biWeeklyContest = contestViewModel.getContest(1)
                val weeklyContest = contestViewModel.getContest(2)

                if (biWeeklyContest != null && weeklyContest != null) {
                    contestList.add(weeklyContest)
                    contestList.add(biWeeklyContest)

                    contestPagerAdapter =
                        ContestPagerAdapter(contestList, requireContext(), requireActivity())

                    fragmentHomeBinding.viewPager.adapter = contestPagerAdapter
                    TabLayoutMediator(
                        fragmentHomeBinding.pageIndicator,
                        fragmentHomeBinding.viewPager
                    ) { _, _ -> }.attach()
                    fragmentHomeBinding.contestProgressBar.visibility = View.GONE
                    fragmentHomeBinding.questionProgressBar.visibility = View.GONE
                } else {
                    generalErroView.visibility = View.VISIBLE
                    fragmentHomeBinding.homeLayout.visibility = View.GONE
                }
            } catch (exception: Exception) {
                generalErroView.visibility = View.VISIBLE
                fragmentHomeBinding.homeLayout.visibility = View.GONE
            }
        }
    }

    // load loadDailyQuestion from online
    private fun loadDailyQuestion() {

        val call: okhttp3.Call = createApiCall()
        call.enqueue(object : okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(Constant.TAG(HomeFragment::class.java).toString(), call.toString(), e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val questionData: DailyQuestionModel = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    DailyQuestionModel::class.java
                )
                Log.d(Constant.TAG(HomeFragment::class.java).toString(), questionData.toString())

                saveQuestion(questionData)
            }
        })
    }

    // update daily question
    private fun dailyUpdateQuestion() {
        val call: okhttp3.Call = createApiCall()
        call.enqueue(object : okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(Constant.TAG(HomeFragment::class.java).toString(), call.toString(), e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val questionData: DailyQuestionModel = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    DailyQuestionModel::class.java
                )
                Log.d(Constant.TAG(HomeFragment::class.java).toString(), questionData.toString())

                val todaysQuestion = DailyQuestion(
                    activeDailyCodingChallengeQuestion = fromDailyQuestionDaily(questionData.data?.activeDailyCodingChallengeQuestion!!).toString(),
                    question = fromDailyQuestion(questionData.data?.activeDailyCodingChallengeQuestion?.question!!).toString(),
                    topicTags = fromDailyQuestionTags(questionData.data?.activeDailyCodingChallengeQuestion?.question?.topicTags!!)
                        .toString()
                )
                updateQuestion(todaysQuestion)
            }
        })
    }

    // saving/updating the question in local database
    private fun saveQuestion(questionData: DailyQuestionModel) {
        val todaysQuestion = DailyQuestion(
            activeDailyCodingChallengeQuestion = fromDailyQuestionDaily(questionData.data?.activeDailyCodingChallengeQuestion!!).toString(),
            question = fromDailyQuestion(questionData.data?.activeDailyCodingChallengeQuestion?.question!!).toString(),
            topicTags = fromDailyQuestionTags(questionData.data?.activeDailyCodingChallengeQuestion?.question?.topicTags!!)
                .toString()
        )

        lifecycleScope.launch {
            val preferences = SharedPreferences(requireContext())
            if (!preferences.dailyQuestionAdded) {
                addQuestion(todaysQuestion)
            } else {
                updateQuestion(todaysQuestion)
            }
        }
    }

    // updates the question
    private fun updateQuestion(dailyQuestion: DailyQuestion) {
        dailyQuestion.id = 1
        dailyViewModel.updateQuestion(dailyQuestion)
    }

    // adding the question in local database
    private fun addQuestion(dailyQuestion: DailyQuestion) {
        val preferences = SharedPreferences(requireContext())
        preferences.dailyQuestionAdded = true
        dailyViewModel.addQuestion(dailyQuestion)
    }

    // creates an okHttpClient call for today's question
    private fun createApiCall(): okhttp3.Call {
        val okHttpClient = OkHttpClient()
        val postBody = Gson().toJson(LeetCodeRequests.Helper.getDailyQuestion)
        val requestBody: RequestBody =
            postBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val headers: Headers = Headers.Builder()
            .add("Content-Type", "application/json")
            .build()
        val request: Request = Request.Builder()
            .headers(headers)
            .post(requestBody)
            .url(URL.graphql)
            .build()
        return okHttpClient.newCall(request)
    }

    // share link
    private fun shareLink(link: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        share.putExtra(Intent.EXTRA_TEXT, link)
        activity?.startActivity(Intent.createChooser(share, "Share Problem Link!"))
    }

    // copy the link
    private fun copyLink(link: String) {
        requireContext().copyToClipboard(link)
        showSnackBarWithAction(
            requireActivity(),
            "Link Copied!",
            getString(R.string.question_share_link),
            null
        ) {
            shareLink(link)
        }
    }

    // open the link in browser
    private fun openLink(link: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(link)
            )
        )
    }

    private fun setAlarm(context: Context) {
        val cal = Calendar.getInstance()

        /**
         * leetcode daily challenges gets updated 00:00 UTC everyday
         * that is 5.30 am in IST
         * we are gonna send notification about it every day 5 am of device's time
         */
        cal.set(Calendar.HOUR_OF_DAY, 5)
        cal.set(Calendar.MINUTE, 0)

        // send notification every day for a new daily question
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, DailyQuestionAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)

        // TODO change time of compared calendar as checking and showing notification around only 5.00 AM is useless
        if (Calendar.getInstance().after(cal)) {
            cal.add(Calendar.DATE, 1)
        }
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.d(Constant.TAG(HomeFragment::class.java).toString(), "alarm has been sent")
        dailyUpdateQuestion()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "foxandroidReminderChannel"
            val description = "Channel For Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("foxandroid", name, importance)
            channel.description = description
            val notificationManager: NotificationManager = context?.getSystemService(
                NotificationManager::class.java
            )!!
            notificationManager.createNotificationChannel(channel)
        }
    }

    // get random question's title slug
    private fun loadRandomQuestion() {
        val call: okhttp3.Call = randomQuestionApiCall()
        call.enqueue(object : okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(Constant.TAG(HomeFragment::class.java).toString(), call.toString(), e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val randomQuestionData: RandomQuestionModel = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    RandomQuestionModel::class.java
                )
                Log.d(
                    Constant.TAG(HomeFragment::class.java).toString(),
                    randomQuestionData.toString()
                )

                openRandomQuestion(randomQuestionData.data?.randomQuestion?.titleSlug.toString())
            }
        })
    }

    private fun randomQuestionApiCall(): okhttp3.Call {
        val okHttpClient = OkHttpClient()
        val postBody = Gson().toJson(LeetCodeRequests.Helper.getRandomQuestion)
        val requestBody: RequestBody =
            postBody.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val headers: Headers = Headers.Builder()
            .add("Content-Type", "application/json")
            .build()
        val request: Request = Request.Builder()
            .headers(headers)
            .post(requestBody)
            .url(URL.graphql)
            .build()
        return okHttpClient.newCall(request)
    }

    private fun openRandomQuestion(titleSlug: String) {
        val bundle = bundleOf(
            "questionTitleSlug" to titleSlug,
            "questionHasSolution" to false,
            "questionID" to null
        )
        activity?.runOnUiThread {
            fragmentHomeBinding.root.findNavController()
                .navigate(R.id.action_homeFragment_to_questionFragment, bundle)
        }
    }
}