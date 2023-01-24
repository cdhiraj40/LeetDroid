package com.cdhiraj40.leetdroid.ui.fragments.question

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cdhiraj40.leetdroid.R
import com.cdhiraj40.leetdroid.api.LeetCodeRequests
import com.cdhiraj40.leetdroid.api.URL
import com.cdhiraj40.leetdroid.databinding.FragmentQuestionSolutionBinding
import com.cdhiraj40.leetdroid.model.QuestionSolutionModel
import com.cdhiraj40.leetdroid.sharedViewModel.QuestionSharedViewModel
import com.cdhiraj40.leetdroid.utils.Constant
import com.cdhiraj40.leetdroid.utils.JsonUtils
import com.google.gson.Gson
import io.noties.markwon.Markwon
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class QuestionSolutionFragment : Fragment() {

    private lateinit var fragmentSolutionBinding: FragmentQuestionSolutionBinding
    private lateinit var questionTitleSlug: String
    private var questionHasSolution: Boolean = false
    private var isQuestionPaid: Boolean = false
    private lateinit var questionSharedViewModel: QuestionSharedViewModel
    private lateinit var questionSolutionJson: QuestionSolutionModel
    private lateinit var loadingView: View

    private var markwon: Markwon? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentSolutionBinding = FragmentQuestionSolutionBinding.inflate(layoutInflater)
        val rootView = fragmentSolutionBinding.root

        loadingView = rootView.findViewById(R.id.loading_view)

        loadingView.visibility = View.VISIBLE
        fragmentSolutionBinding.questionSolutionLayout.visibility = View.GONE

        val viewNoSolution: View = rootView.findViewById(R.id.view_no_solution)
        questionSharedViewModel =
            ViewModelProvider(requireActivity())[QuestionSharedViewModel::class.java]

        questionSharedViewModel.questionTitleSlug.observe(viewLifecycleOwner, {
            // updating data in Title-Text, and showing on toolbar
            questionTitleSlug = it
        })

        questionSharedViewModel.questionTitle.observe(viewLifecycleOwner, {
            // getting the question title and showing on toolbar
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
        })

        questionSharedViewModel.questionHasSolution.observe(viewLifecycleOwner, {
            // updating data in Title-Text
            questionHasSolution = it
            questionSharedViewModel.questionPaid.observe(viewLifecycleOwner, { paid ->
                isQuestionPaid = paid
            })
            if (questionHasSolution && !isQuestionPaid) {
                loadQuestion()
            } else {
                viewNoSolution.visibility = View.VISIBLE
                loadingView.visibility = View.GONE
                fragmentSolutionBinding.questionSolution.visibility = View.GONE
            }
        })
        markwon = Markwon.create(requireContext())

        return rootView
    }

    private fun loadQuestion() {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(LeetCodeRequests.Helper.getQuestionSolution(questionTitleSlug))
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
        val call: Call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.d(
                    Constant.TAG(QuestionSolutionFragment::class.java).toString(),
                    call.toString(),
                    e
                )
            }

            override fun onResponse(call: Call, response: Response) {

                questionSolutionJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    QuestionSolutionModel::class.java
                )

                activity?.runOnUiThread {
                    var discussionContent: String =
                        questionSolutionJson.data?.question?.solution?.content.toString()

                    discussionContent = discussionContent.replace("\\n", "\n")
                    discussionContent = discussionContent.replace("\\t", "\t")
                    discussionContent = discussionContent.replace("$$", "")

                    markwon!!.setMarkdown(
                        fragmentSolutionBinding.questionSolution,
                        discussionContent
                    )
                    loadingView.visibility = View.GONE
                    fragmentSolutionBinding.questionSolutionLayout.visibility = View.VISIBLE
                }
            }
        })
    }
}