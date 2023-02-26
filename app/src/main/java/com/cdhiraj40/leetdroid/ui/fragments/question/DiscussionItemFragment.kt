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
import com.cdhiraj40.leetdroid.databinding.FragmentDiscussionItemBinding
import com.cdhiraj40.leetdroid.model.DiscussionItemModel
import com.cdhiraj40.leetdroid.sharedViewModel.QuestionDiscussionSharedViewModel
import com.cdhiraj40.leetdroid.utils.Constant
import com.cdhiraj40.leetdroid.utils.JsonUtils
import com.google.gson.Gson
import io.noties.markwon.Markwon
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.apache.commons.text.StringEscapeUtils
import java.io.IOException

class DiscussionItemFragment : Fragment() {
    private lateinit var fragmentDiscussionItemBinding: FragmentDiscussionItemBinding
    private var markwon: Markwon? = null
    private var discussionId: Int? = 0
    private lateinit var discussionContentJson: DiscussionItemModel
    private lateinit var loadingView: View
    private lateinit var discussionSharedViewModel: QuestionDiscussionSharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        fragmentDiscussionItemBinding = FragmentDiscussionItemBinding.inflate(inflater)

        val rootView = fragmentDiscussionItemBinding.root

        loadingView = rootView.findViewById(R.id.loading_view)

        discussionSharedViewModel =
            ViewModelProvider(requireActivity())[QuestionDiscussionSharedViewModel::class.java]

        discussionSharedViewModel.discussionTitle.observe(viewLifecycleOwner, {
            // getting the discussion title and showing on toolbar
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
        })


        loadingView.visibility = View.VISIBLE
        fragmentDiscussionItemBinding.questionDiscussionLayout.visibility = View.GONE

        val bundle = arguments

        discussionId = bundle?.getInt("discussionId")

        markwon = Markwon.create(requireContext())

        loadDiscussionItemContent(discussionId)
        return rootView

    }

    private fun loadDiscussionItemContent(discussionId: Int?) {
        val okHttpClient = OkHttpClient()
        val postBody =
            Gson().toJson(LeetCodeRequests.Helper.getQuestionDiscussionItem(discussionId))
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
                    Constant.TAG(DiscussionItemFragment::class.java).toString(),
                    call.toString(),
                    e
                )
            }

            override fun onResponse(call: Call, response: Response) {

                discussionContentJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    DiscussionItemModel::class.java
                )

                activity?.runOnUiThread {

                    (requireActivity() as AppCompatActivity).supportActionBar?.title =
                        discussionContentJson.data?.topic?.title

                    var discussionContent: String =
                        discussionContentJson.data?.topic?.post?.content.toString()
                    discussionContent = discussionContent.replace("\\n", "\n")
                    discussionContent = discussionContent.replace("\\t", "\t")
                    markwon!!.setMarkdown(
                        fragmentDiscussionItemBinding.discussionContent,
                        StringEscapeUtils.unescapeJava(discussionContent)
                    )

                    fragmentDiscussionItemBinding.topicTitle.isSelected = true
                    fragmentDiscussionItemBinding.topicTitle.text =
                        discussionContentJson.data?.topic?.title

                    loadingView.visibility = View.GONE
                    fragmentDiscussionItemBinding.questionDiscussionLayout.visibility = View.VISIBLE
                }
            }
        })
    }
}