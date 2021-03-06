package com.cdhiraj40.leetdroid.ui.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdhiraj40.leetdroid.R
import com.cdhiraj40.leetdroid.adapter.ContestPagerAdapter
import com.cdhiraj40.leetdroid.adapter.TrendingDiscussionAdapter
import com.cdhiraj40.leetdroid.api.ContestApi
import com.cdhiraj40.leetdroid.api.LeetCodeRequests
import com.cdhiraj40.leetdroid.api.URL
import com.cdhiraj40.leetdroid.data.dao.ContestDao
import com.cdhiraj40.leetdroid.data.db.ContestsDatabase
import com.cdhiraj40.leetdroid.data.entitiy.Contest
import com.cdhiraj40.leetdroid.data.entitiy.DailyQuestion
import com.cdhiraj40.leetdroid.data.repository.ContestRepository
import com.cdhiraj40.leetdroid.data.viewModel.ContestViewModel
import com.cdhiraj40.leetdroid.data.viewModel.DailyQuestionViewModel
import com.cdhiraj40.leetdroid.databinding.FragmentHomeBinding
import com.cdhiraj40.leetdroid.model.DailyQuestionModel
import com.cdhiraj40.leetdroid.model.RandomQuestionModel
import com.cdhiraj40.leetdroid.model.TrendingDiscussionModel
import com.cdhiraj40.leetdroid.utils.*
import com.cdhiraj40.leetdroid.utils.AlarmUtils.setAlarm
import com.cdhiraj40.leetdroid.utils.CommonUtils.openLink
import com.cdhiraj40.leetdroid.utils.Converters.DailyQuestionConverter.fromDailyQuestion
import com.cdhiraj40.leetdroid.utils.Converters.DailyQuestionConverter.fromStringDailyQuestion
import com.cdhiraj40.leetdroid.utils.Converters.DailyQuestionDailyConverter.fromDailyQuestionDaily
import com.cdhiraj40.leetdroid.utils.Converters.DailyQuestionDailyConverter.fromStringDailyQuestionDaily
import com.cdhiraj40.leetdroid.utils.Converters.DailyQuestionTagsConverter.fromDailyQuestionTags
import com.cdhiraj40.leetdroid.utils.extensions.copyToClipboard
import com.cdhiraj40.leetdroid.utils.extensions.showSnackBarWithAction
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


// TODO add on clicks to trending discussions
class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var contestViewModel: ContestViewModel
    private lateinit var dailyViewModel: DailyQuestionViewModel
    private lateinit var generalErroView: View
    private lateinit var contestDB: ContestDao
    private lateinit var contestRepository: ContestRepository
    private lateinit var contestPagerAdapter: ContestPagerAdapter
    private lateinit var trendingDiscussionAdapter: TrendingDiscussionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val rootView = fragmentHomeBinding.root

        generalErroView = rootView.findViewById(R.id.general_error_view)

        createNotificationChannel()

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

        trendingDiscussionAdapter = TrendingDiscussionAdapter(requireContext())

        fragmentHomeBinding.trendingDiscussRecyclerView.visibility = View.GONE
        fragmentHomeBinding.discussionProgressBar.visibility = View.VISIBLE

        loadTrendingDiscussions(10)
        sharedPreferences = SharedPreferences(requireContext())

        /**
         *  check if contest timer has been ended or app is newly installed, if yes then reset all preferences for reminders.
         */
        if (sharedPreferences.timerEnded || !sharedPreferences.contestsInserted) {
            sharedPreferences.dayWeeklyNotificationPushed = false
            sharedPreferences.dayBiWeeklyNotificationPushed = false
            sharedPreferences.minsBiWeeklyNotificationPushed = false
            sharedPreferences.minsWeeklyNotificationPushed = false
            getContestList()
        } else {
            displayContests()
        }

        loadDailyQuestion()
        setupDailyQuestion()

        fragmentHomeBinding.randomQuestionLayout.setOnClickListener {
            loadRandomQuestion()
        }

        fragmentHomeBinding.allQuestionsLayout.setOnClickListener {
            fragmentHomeBinding.root.findNavController().navigate(R.id.allQuestionsFragment)
        }

        if (!sharedPreferences.statusShown) {
            sharedPreferences.statusShown = true
            spotlightShowCase(
                fragmentHomeBinding.root,
                "Upcoming contest details",
                getString(R.string.upcoming_contest_details),
                2,
                fragmentHomeBinding.weeklyContestLayout.id
            )
        }

        if (!sharedPreferences.dailyNotificationPushed) {
            sharedPreferences.dailyNotificationPushed = true
            setAlarm(requireContext())
            dailyUpdateQuestion()
        }

        fragmentHomeBinding.trendingDiscussButton.setOnClickListener {
            findNavController().navigate(R.id.trendingDiscussionFragment)
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
                        fragmentHomeBinding.weeklyContestLayout.id
                    )
//                    3 -> spotlightShowCase(
//                        contentView,
//                        "Problem Set",
//                        getString(R.string.problem_set_details),
//                        4,
//                        fragmentHomeBinding.allQuestionsLayout.id
//                    )
//                    4 -> spotlightShowCase(
//                        contentView,
//                        "Random Questions",
//                        getString(R.string.random_question_details),
//                        5,
//                        fragmentHomeBinding.randomQuestionLayout.id
//                    )
                    3 -> spotlightShowCase(
                        contentView,
                        "Daily Challenge",
                        getString(R.string.daily_challenge_details),
                        4,
                        fragmentHomeBinding.dailyQuestionRelativeLayout.id
                    )
                    4 -> spotlightShowCase(
                        contentView,
                        "Trending Discussions",
                        getString(R.string.trending_home_details),
                        6,
                        fragmentHomeBinding.trendingDiscussLayout.id
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
            formattedDate = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(parsedDate)
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
            openLink(requireContext(), link)
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

                    val startingWeeklyDate = DateUtils.parseISO8601Date(weeklyContest.start_time)
                    val endingWeeklyDate = DateUtils.parseISO8601Date(weeklyContest.end_time)

                    val startingBiWeeklyDate =
                        DateUtils.parseISO8601Date(biWeeklyContest.start_time)
                    val endingBiWeeklyDate = DateUtils.parseISO8601Date(biWeeklyContest.end_time)


                    if (!sharedPreferences.dayWeeklyNotificationPushed) {
                        // alarm for a day before contests
                        AlarmUtils.setDayAlarm(
                            requireContext(),
                            startingWeeklyDate.time,
                            weeklyContest.name
                        )
                        sharedPreferences.dayWeeklyNotificationPushed = true
                    }
                    if (!sharedPreferences.dayBiWeeklyNotificationPushed) {
                        AlarmUtils.setDayAlarm(
                            requireContext(),
                            startingBiWeeklyDate.time,
                            weeklyContest.name
                        )
                        sharedPreferences.dayBiWeeklyNotificationPushed = true
                    }

                    // alarm for 30 mins before contests
                    if (sharedPreferences.dayBiWeeklyNotificationPushed) {
                        AlarmUtils.set30MinsAlarm(
                            requireContext(),
                            startingWeeklyDate.time,
                            weeklyContest.name
                        )
                        sharedPreferences.minsWeeklyNotificationPushed = true
                    }
                    if (sharedPreferences.minsBiWeeklyNotificationPushed) {
                        AlarmUtils.set30MinsAlarm(
                            requireContext(),
                            startingBiWeeklyDate.time,
                            weeklyContest.name
                        )
                        sharedPreferences.minsBiWeeklyNotificationPushed = true
                    }
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

        val call: okhttp3.Call = apiCallDailyQ()
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

    // loads trending Discussion from online
    private fun loadTrendingDiscussions(limit: Int) {

        val call: okhttp3.Call = apiCallTrendingDiss(limit)
        call.enqueue(object : okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(Constant.TAG(HomeFragment::class.java).toString(), call.toString(), e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val trendingDiscussion: TrendingDiscussionModel = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    TrendingDiscussionModel::class.java
                )
                Log.d(
                    Constant.TAG(HomeFragment::class.java).toString(),
                    trendingDiscussion.toString()
                )
                activity?.runOnUiThread {
                    displayTrendingDiscussions(trendingDiscussion)
                }
            }
        })
    }

    private fun displayTrendingDiscussions(trendingDiscussion: TrendingDiscussionModel) {
        trendingDiscussionAdapter.setData(trendingDiscussion)
        fragmentHomeBinding.trendingDiscussRecyclerView.layoutManager =
            LinearLayoutManager(context)
        fragmentHomeBinding.trendingDiscussRecyclerView.adapter =
            trendingDiscussionAdapter
        fragmentHomeBinding.trendingDiscussRecyclerView.visibility = View.VISIBLE
        fragmentHomeBinding.discussionProgressBar.visibility = View.GONE
    }

    // update daily question
    private fun dailyUpdateQuestion() {
        val call: okhttp3.Call = apiCallDailyQ()
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
    private fun apiCallDailyQ(): okhttp3.Call {
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

    // creates an okHttpClient call for Trending Discussions
    private fun apiCallTrendingDiss(limit: Int): okhttp3.Call {
        val okHttpClient = OkHttpClient()
        val postBody = Gson().toJson(LeetCodeRequests.Helper.getTrendingDiscussion(limit))
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