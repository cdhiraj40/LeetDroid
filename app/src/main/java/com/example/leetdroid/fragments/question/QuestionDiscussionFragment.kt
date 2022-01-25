package com.example.leetdroid.fragments.question

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.leetdroid.sharedViewModel.QuestionSharedViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leetdroid.R
import com.example.leetdroid.adapter.QuestionDiscussionAdapter

import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentQuestionDiscussionBinding
import com.example.leetdroid.fragments.AllQuestionsFragment

import com.example.leetdroid.model.QuestionDiscussionsModel
import com.example.leetdroid.utils.JsonUtils
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*


class QuestionDiscussionFragment : Fragment(), QuestionDiscussionAdapter.OnItemClicked {

    private lateinit var fragmentQuestionDiscussionBinding: FragmentQuestionDiscussionBinding
    private lateinit var questionId: String
    private lateinit var questionDiscussionsJson: QuestionDiscussionsModel
    private lateinit var questionSharedViewModel: QuestionSharedViewModel
    private var questionDiscussionAdapter: QuestionDiscussionAdapter? = null
    private var limit = 10

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
            loadQuestionDiscussionList(questionId, limit)
        })
        // adding pagination
        fragmentQuestionDiscussionBinding.allDiscussionNested.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    // fetch more 10 questions when reached end
                    limit += 10
                    fragmentQuestionDiscussionBinding.discussionListProgressBar.visibility =
                        View.VISIBLE

                    questionSharedViewModel.questionID.observe(viewLifecycleOwner, {
                        // sending questionID to load discussion list
                        questionId = it
                        loadQuestionDiscussionList(questionId, limit)
                    })
                }
            })

        return rootView
    }

    private fun loadQuestionDiscussionList(questionId: String, limit: Int) {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(
                LeetCodeRequests.Helper.getQuestionDiscussions(
                    questionId,
                    "most_votes",
                    limit
                )
            )
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

                questionDiscussionsJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    QuestionDiscussionsModel::class.java
                )

                activity?.runOnUiThread {
                    questionDiscussionAdapter =
                        QuestionDiscussionAdapter(questionDiscussionsJson, requireContext())
                    fragmentQuestionDiscussionBinding.questionDiscussionsRecyclerView.layoutManager =
                        LinearLayoutManager(context)
                    fragmentQuestionDiscussionBinding.questionDiscussionsRecyclerView.adapter =
                        questionDiscussionAdapter

                    questionDiscussionAdapter!!.setOnClick(this@QuestionDiscussionFragment)
                    checkIfEmpty()
                }
            }
        })
    }

    private fun checkIfEmpty() {
        if (questionDiscussionAdapter!!.getDataItemCount() == 0) {
            fragmentQuestionDiscussionBinding.discussionListProgressBar.visibility = View.VISIBLE
        } else {
            fragmentQuestionDiscussionBinding.discussionListProgressBar.visibility = View.GONE
        }
    }

    override fun onItemClick(position: Int, discussionId: Int?) {
        val bundle = bundleOf(
            "discussionId" to discussionId,
        )
        fragmentQuestionDiscussionBinding.root.findNavController()
            .navigate(R.id.action_questionDiscussionFragment_to_discussionItemFragment, bundle)
    }
}