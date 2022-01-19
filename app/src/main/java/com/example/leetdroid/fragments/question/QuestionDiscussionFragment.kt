package com.example.leetdroid.fragments.question

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.leetdroid.sharedViewModel.QuestionSharedViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leetdroid.adapter.QuestionDiscussionAdapter
import com.example.leetdroid.api.GraphQl
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentQuestionDiscussionBinding
import com.example.leetdroid.fragments.AllQuestionsFragment

import com.example.leetdroid.model.QuestionDiscussionModel
import com.example.leetdroid.utils.JsonUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*


class QuestionDiscussionFragment : Fragment() {

    private lateinit var fragmentQuestionDiscussionBinding: FragmentQuestionDiscussionBinding
    private lateinit var questionId: String
    private lateinit var questionDiscussionJson: QuestionDiscussionModel
    private lateinit var questionSharedViewModel: QuestionSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentQuestionDiscussionBinding =
            FragmentQuestionDiscussionBinding.inflate(layoutInflater)
        val rootView = fragmentQuestionDiscussionBinding.root

        questionSharedViewModel =
            ViewModelProvider(requireActivity()).get(QuestionSharedViewModel::class.java)

        questionSharedViewModel.questionID.observe(viewLifecycleOwner, {
            // sending questionID to load discussion list
            questionId = it
            loadQuestionDiscussionList(questionId)
        })

        return rootView
    }

    private fun loadQuestionDiscussionList(questionId: String) {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            java.lang.String.format(Locale.ENGLISH,GraphQl.QUESTION_DISCUSSION, "most_votes", 0, 15, questionId)
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
                Log.d(AllQuestionsFragment.Constant.TAG, call.toString(), e)
            }

            override fun onResponse(call: Call, response: Response) {

                questionDiscussionJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    QuestionDiscussionModel::class.java
                )

                activity?.runOnUiThread {
                    val allQuestionsAdapter =
                        QuestionDiscussionAdapter(questionDiscussionJson, requireContext())
                    fragmentQuestionDiscussionBinding.questionDiscussionsRecyclerView.layoutManager =
                        LinearLayoutManager(context)
                    fragmentQuestionDiscussionBinding.questionDiscussionsRecyclerView.adapter =
                        allQuestionsAdapter
                }
            }
        })
    }
}