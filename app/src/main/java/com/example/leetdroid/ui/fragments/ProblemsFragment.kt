package com.example.leetdroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.leetdroid.R
import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentProblemsBinding
import com.example.leetdroid.model.RandomQuestionModel
import com.example.leetdroid.utils.Constant
import com.example.leetdroid.utils.JsonUtils
import com.example.leetdroid.utils.dialog.AlertDialogShower
import com.example.leetdroid.utils.dialog.AppDialogs
import com.example.leetdroid.utils.extensions.below
import com.google.gson.Gson
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ProblemsFragment : Fragment() {

    private lateinit var problemsBinding: FragmentProblemsBinding
    private var isDropdownTopics = false
    private var isDropdownDifficulty = false
    private var isDropdownList = false
    private var isDropdownTags = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        problemsBinding = FragmentProblemsBinding.inflate(inflater, container, false)

        val rootView = problemsBinding.root

        // TODO: After going to question fragment and coming back, somehow clicking on dropdown for first time does not work that means the values are not getting created again.
        setupProblemTopics()
        setupProblemDifficulties()
        setupFeaturedLists()

        problemsBinding.problemSetLayout.setOnClickListener {
            findNavController().navigate(R.id.allQuestionsFragment)
        }

        problemsBinding.randomQuestionLayout.setOnClickListener {
            loadRandomQuestion()
        }
        setupTopicClickListeners()
        setupDiffClickListeners()
        setupListsClickListeners()
        return rootView
    }

    private fun setupFeaturedLists() {
        problemsBinding.problemsFeaturedListDropdownMenu.setOnClickListener {
            if (isDropdownList) {
                isDropdownList = false
                // changing the dropdown rotation and its background
                problemsBinding.problemsFeaturedListDropdownMenu.rotation = 0F
                problemsBinding.problemsFeaturedListDropdownMenu.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.light_glass_background)
                problemsBinding.problemsFeaturedListSubLayout.visibility = View.GONE
                problemsBinding.problemExplore4Layout.below(problemsBinding.problemExplore3Layout)
            } else {
                goDefault()
                isDropdownList = true
                // changing the dropdown rotation and its background
                problemsBinding.problemsFeaturedListDropdownMenu.rotation = 180F
                problemsBinding.problemsFeaturedListDropdownMenu.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.dark_glass_background)
                problemsBinding.problemsFeaturedListSubLayout.visibility = View.VISIBLE
                problemsBinding.problemExplore4Layout.below(problemsBinding.problemsFeaturedListSubLayout)
            }
        }
    }

    private fun setupListsClickListeners() {
        problemsBinding.topInterviewLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "listId" to "top-interview-questions"
                )
            )
        }

        problemsBinding.top100LikedLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "listId" to "top-100-liked-questions"
                )
            )
        }

        problemsBinding.curatedAlgo170Layout.setOnClickListener {
            showPremiumListWarning(key = "listId", value = "leetcode-curated-algo-170")
//            findNavController().navigate(
//                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
//                    "listId" to "leetcode-curated-algo-170"
//                )
//            )
        }

        problemsBinding.curatedSql70Layout.setOnClickListener {
            showPremiumListWarning(key = "listId", value = "leetcode-curated-sql-70")

//            findNavController().navigate(
//                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
//                    "listId" to "leetcode-curated-sql-70"
//                )
//            )
//        }
        }
    }

    private fun setupDiffClickListeners() {
        problemsBinding.problemEasyLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "difficulty" to "EASY"
                )
            )
        }

        problemsBinding.problemMediumLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "difficulty" to "MEDIUM"
                )
            )
        }

        problemsBinding.problemDifficultyHardLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "difficulty" to "HARD"
                )
            )
        }
    }

    private fun setupTopicClickListeners() {
        problemsBinding.problemArrayLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "tag" to "array"
                )
            )
        }

        problemsBinding.problemStringLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "tag" to "string"
                )
            )
        }

        problemsBinding.problemHashTableLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "tag" to "hash-table"
                )
            )
        }

        problemsBinding.problemDynamicProgrammingLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "tag" to "dynamic-programming"
                )
            )
        }

        problemsBinding.problemBackTrackingLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "tag" to "backtracking"
                )
            )
        }

        problemsBinding.problemBitManipulationLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "tag" to "bit-manipulation"
                )
            )
        }

        problemsBinding.problemSlidingWindowLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "tag" to "sliding-window"
                )
            )
        }

        problemsBinding.problemRecursionLayout.setOnClickListener {
            findNavController().navigate(
                R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                    "tag" to "recursion"
                )
            )
        }
    }

    private fun setupProblemTopics() {
        problemsBinding.problemTopicsDropdownMenu.setOnClickListener {
            if (isDropdownTopics) {
                isDropdownTopics = false
                // changing the dropdown rotation and its background
                problemsBinding.problemTopicsDropdownMenu.rotation = 0F
                problemsBinding.problemTopicsDropdownMenu.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.light_glass_background)
                problemsBinding.problemTopicsSubLayout.visibility = View.GONE
                problemsBinding.problemExplore3Layout.below(problemsBinding.problemExplore2Layout)
            } else {
                goDefault()
                isDropdownTopics = true
                // changing the dropdown rotation and its background
                problemsBinding.problemTopicsDropdownMenu.rotation = 180F
                problemsBinding.problemTopicsDropdownMenu.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.dark_glass_background)
                problemsBinding.problemTopicsSubLayout.visibility = View.VISIBLE
                problemsBinding.problemExplore3Layout.below(problemsBinding.problemTopicsSubLayout)
            }
        }
    }

    private fun setupProblemDifficulties() {
        problemsBinding.problemDifficultyDropdownMenu.setOnClickListener {
            if (isDropdownDifficulty) {
                isDropdownDifficulty = false
                // changing the dropdown rotation and its background
                problemsBinding.problemDifficultyDropdownMenu.rotation = 0F
                problemsBinding.problemDifficultyDropdownMenu.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.light_glass_background)
                problemsBinding.problemDifficultySubLayout.visibility = View.GONE
                problemsBinding.problemExplore3Layout.below(problemsBinding.problemExplore2Layout)
            } else {
                goDefault()
                isDropdownDifficulty = true
                // changing the dropdown rotation and its background
                problemsBinding.problemDifficultyDropdownMenu.rotation = 180F
                problemsBinding.problemDifficultyDropdownMenu.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.dark_glass_background)
                problemsBinding.problemDifficultySubLayout.visibility = View.VISIBLE
                problemsBinding.problemExplore3Layout.below(problemsBinding.problemDifficultySubLayout)
            }
        }
    }

    private fun goDefault() {
        defaultDropdownValues()
        // changing every dropdown rotation and its background to default
        defaultDropdownMenu()
        // making the layouts below the default ones
        defaultLayoutBelow()
        // removing visibility of sub layouts
        defaultLayout()
    }

    private fun defaultDropdownValues() {
        isDropdownDifficulty = false
        isDropdownList = false
        isDropdownTopics = false
        isDropdownTags = false
    }

    private fun defaultLayout() {
        problemsBinding.problemTopicsSubLayout.visibility = View.GONE
        problemsBinding.problemDifficultySubLayout.visibility = View.GONE
        problemsBinding.problemsFeaturedListSubLayout.visibility = View.GONE
    }

    private fun defaultLayoutBelow() {
        problemsBinding.problemExplore3Layout.below(problemsBinding.problemExplore2Layout)
    }

    private fun defaultDropdownMenu() {
        problemsBinding.problemTopicsDropdownMenu.rotation = 0F
        problemsBinding.problemTopicsDropdownMenu.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.light_glass_background)

        problemsBinding.problemDifficultyDropdownMenu.rotation = 0F
        problemsBinding.problemDifficultyDropdownMenu.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.light_glass_background)

        problemsBinding.problemsFeaturedListDropdownMenu.rotation = 0F
        problemsBinding.problemsFeaturedListDropdownMenu.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.light_glass_background)

        problemsBinding.problemsTagsDropdownMenu.rotation = 0F
        problemsBinding.problemsFeaturedListDropdownMenu.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.light_glass_background)
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

    private fun showPremiumListWarning(key: String, value: String) {
        AlertDialogShower(requireActivity()).show(
            AppDialogs.PremiumList, {
                findNavController().navigate(
                    R.id.action_problemsFragment_to_allQuestionsFragment, bundleOf(
                        key to value
                    )
                )
            }, {

            }
        )
    }

    private fun openRandomQuestion(titleSlug: String) {
        val bundle = bundleOf(
            "questionTitleSlug" to titleSlug,
            "questionHasSolution" to false,
            "questionID" to null
        )
        activity?.runOnUiThread {
            findNavController().navigate(R.id.action_problemsFragment_to_questionFragment, bundle)
        }
    }
}