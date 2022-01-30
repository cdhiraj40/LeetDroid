package com.example.leetdroid.ui.fragments

import android.content.Intent
import android.net.Uri
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
import com.example.leetdroid.data.entitiy.Contest
import com.example.leetdroid.data.entitiy.DailyQuestion
import com.example.leetdroid.data.viewModel.ContestViewModel
import com.example.leetdroid.data.viewModel.DailyQuestionViewModel
import com.example.leetdroid.databinding.FragmentHomeBinding
import com.example.leetdroid.extensions.copyToClipboard
import com.example.leetdroid.extensions.showSnackBarWithAction
import com.example.leetdroid.model.DailyQuestionModel
import com.example.leetdroid.ui.fragments.HomeFragment.Constant.TAG
import com.example.leetdroid.utils.Converters
import com.example.leetdroid.utils.Converters.DailyQuestionConverter.fromDailyQuestion
import com.example.leetdroid.utils.Converters.DailyQuestionConverter.fromStringDailyQuestion
import com.example.leetdroid.utils.Converters.DailyQuestionDailyConverter.fromDailyQuestionDaily
import com.example.leetdroid.utils.Converters.DailyQuestionDailyConverter.fromStringDailyQuestionDaily
import com.example.leetdroid.utils.Converters.DailyQuestionTagsConverter.fromDailyQuestionTags
import com.example.leetdroid.utils.JsonUtils
import com.example.leetdroid.utils.Preferences
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
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var preferences: Preferences
    private lateinit var contestViewModel: ContestViewModel
    private lateinit var dailyViewModel: DailyQuestionViewModel

    private val calendar = Calendar.getInstance()
    private var currentDate: Int = calendar.get(Calendar.DATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val rootView = fragmentHomeBinding.root

        fragmentHomeBinding.contestProgressBar.visibility = View.VISIBLE
        contestViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ContestViewModel::class.java]

        dailyViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[DailyQuestionViewModel::class.java]

        preferences = Preferences(requireContext())

        if (preferences.timerEnded || !preferences.contestsInserted) {
            getContestList()
        } else {
            displayContests()
        }

        // check if today is new day, if yes then only load new question
        if (!preferences.dailyQuestionLoaded && currentDate != preferences.lastVisitedDateTime) {
            preferences.lastVisitedDateTime = currentDate
            loadDailyQuestion()
            preferences.dailyQuestionLoaded = true
        } else {
            setupDailyQuestion()
        }
        return rootView
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
            formattedDate = SimpleDateFormat("d MMM, yyyy", Locale.getDefault()).format(parsedDate)
            fragmentHomeBinding.dailyQuestionDate.text = formattedDate
        } catch (exception: ParseException) {
            Log.d(TAG, "$exception occurred!")
            fragmentHomeBinding.dailyQuestionDate.text =
                SimpleDateFormat("d MMM, yyyy", Locale.getDefault()).format(
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
                    Log.d(TAG, body.toString())

                    saveContests(body)

                    displayContests()
                }
            }

            override fun onFailure(call: Call<JsonArray>?, throwable: Throwable) {
                Log.d(TAG, "requestFailed", throwable)
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

        lifecycleScope.launch {
            val preferences = Preferences(requireContext())
            if (!preferences.contestsInserted && !preferences.timerEnded) {
                insertContests(biWeeklyContest, weeklyContest)
            } else {
                updateContests(biWeeklyContest, weeklyContest)
            }
        }
    }

    // updates the contests
    private fun updateContests(biWeeklyContest: Contest, weeklyContest: Contest) {
        Preferences(requireContext()).timerEnded = false
        biWeeklyContest.id = 1
        contestViewModel.updateContest(biWeeklyContest)

        weeklyContest.id = 2
        contestViewModel.updateContest(weeklyContest)
    }

    // inserts the contests in database
    private fun insertContests(biWeeklyContest: Contest, weeklyContest: Contest) {
        val preferences = Preferences(requireContext())
        preferences.contestsInserted = true
        contestViewModel.addContest(biWeeklyContest)
        contestViewModel.addContest(weeklyContest)
    }

    // displays the contests from database
    private fun displayContests() {
        val contestList = ArrayList<Contest>()
        lifecycleScope.launch {
            val biWeeklyContest = contestViewModel.getContest(1)
            val weeklyContest = contestViewModel.getContest(2)

            contestList.add(weeklyContest)
            contestList.add(biWeeklyContest)

            val contestPagerAdapter =
                ContestPagerAdapter(contestList, requireContext(), requireActivity())

            fragmentHomeBinding.viewPager.adapter = contestPagerAdapter
            TabLayoutMediator(
                fragmentHomeBinding.pageIndicator,
                fragmentHomeBinding.viewPager
            ) { _, _ -> }.attach()
            fragmentHomeBinding.contestProgressBar.visibility = View.GONE
        }
    }

    // load loadDailyQuestion from online
    private fun loadDailyQuestion() {

        val call: okhttp3.Call = createApiCall()
        call.enqueue(object : okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(MyProfileFragment.Constant.TAG, call.toString(), e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val questionData: DailyQuestionModel = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    DailyQuestionModel::class.java
                )
                Log.d(TAG, questionData.toString())

                saveQuestion(questionData)
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
            val preferences = Preferences(requireContext())
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
        val preferences = Preferences(requireContext())
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

    object Constant {
        val TAG = HomeFragment::class.qualifiedName
    }

}