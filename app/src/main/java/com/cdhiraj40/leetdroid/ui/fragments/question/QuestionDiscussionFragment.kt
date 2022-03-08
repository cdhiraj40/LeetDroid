package com.cdhiraj40.leetdroid.ui.fragments.question

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdhiraj40.leetdroid.R
import com.cdhiraj40.leetdroid.adapter.QuestionDiscussionAdapter
import com.cdhiraj40.leetdroid.api.LeetCodeRequests
import com.cdhiraj40.leetdroid.api.URL
import com.cdhiraj40.leetdroid.databinding.FragmentQuestionDiscussionBinding
import com.cdhiraj40.leetdroid.model.QuestionDiscussionsModel
import com.cdhiraj40.leetdroid.sharedViewModel.QuestionDiscussionSharedViewModel
import com.cdhiraj40.leetdroid.sharedViewModel.QuestionSharedViewModel
import com.cdhiraj40.leetdroid.utils.Constant
import com.cdhiraj40.leetdroid.utils.JsonUtils
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
    private lateinit var discussionSharedViewModel: QuestionDiscussionSharedViewModel
    private var questionDiscussionAdapter: QuestionDiscussionAdapter? = null
    private lateinit var loadingView: View
    private var limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentQuestionDiscussionBinding =
            FragmentQuestionDiscussionBinding.inflate(layoutInflater)
        val rootView = fragmentQuestionDiscussionBinding.root

        loadingView = rootView.findViewById(R.id.loading_view)

        loadingView.visibility = View.VISIBLE
        fragmentQuestionDiscussionBinding.allDiscussionNested.visibility = View.GONE

        questionSharedViewModel =
            ViewModelProvider(requireActivity())[QuestionSharedViewModel::class.java]

        discussionSharedViewModel =
            ViewModelProvider(requireActivity())[QuestionDiscussionSharedViewModel::class.java]
        questionSharedViewModel.questionID.observe(viewLifecycleOwner, {
            // sending questionID to load discussion list
            questionId = it
            loadQuestionDiscussionList(questionId, limit)
        })

        questionSharedViewModel.questionTitle.observe(viewLifecycleOwner, {
            // getting the question title and showing on toolbar
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
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
                Log.d(
                    Constant.TAG(QuestionDiscussionFragment::class.java).toString(),
                    call.toString(),
                    e
                )
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

                    loadingView.visibility = View.GONE
                    fragmentQuestionDiscussionBinding.allDiscussionNested.visibility = View.VISIBLE
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

    override fun onItemClick(position: Int, discussionId: Int?, discussionTitle: String) {
        val bundle = bundleOf(
            "discussionId" to discussionId,
        )
        discussionSharedViewModel.getDiscussionTitle(discussionTitle)
        fragmentQuestionDiscussionBinding.root.findNavController()
            .navigate(R.id.action_questionDiscussionFragment_to_discussionItemFragment, bundle)
    }
}