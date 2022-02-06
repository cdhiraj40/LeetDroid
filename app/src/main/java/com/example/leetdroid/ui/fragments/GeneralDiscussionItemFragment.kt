package com.example.leetdroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.leetdroid.R
import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentGeneralDiscussionItemBinding
import com.example.leetdroid.model.DiscussionItemModel
import com.example.leetdroid.utils.Constant
import com.example.leetdroid.utils.JsonUtils
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import ru.noties.markwon.Markwon
import java.io.IOException

class GeneralDiscussionItemFragment : Fragment() {
    private lateinit var fragmentGeneralDiscussionItemBinding: FragmentGeneralDiscussionItemBinding
    private var markwon: Markwon? = null
    private var discussionId: Int? = 0
    private lateinit var discussionContentJson: DiscussionItemModel
    private lateinit var loadingView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        fragmentGeneralDiscussionItemBinding =
            FragmentGeneralDiscussionItemBinding.inflate(inflater)

        val rootView = fragmentGeneralDiscussionItemBinding.root

        loadingView = rootView.findViewById(R.id.loading_view)

        loadingView.visibility = View.VISIBLE
        fragmentGeneralDiscussionItemBinding.generalDiscussionLayout.visibility = View.GONE

        val bundle = arguments

        discussionId = bundle?.getInt("discussionId")

        markwon = Markwon.create(requireContext())

        loadDiscussionItemContent(discussionId)
        return rootView

    }

    private fun loadDiscussionItemContent(discussionId: Int?) {
        val okHttpClient = OkHttpClient()
        val postBody =
            Gson().toJson(LeetCodeRequests.Helper.generalDiscussionItemRequest(discussionId))
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
                Log.d(Constant.TAG("GeneralDiscussionItemFragment").toString(), call.toString(), e)
            }

            override fun onResponse(call: Call, response: Response) {

                discussionContentJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    DiscussionItemModel::class.java
                )

                activity?.runOnUiThread {

                    var discussionContent: String =
                        discussionContentJson.data?.topic?.post?.content.toString()
                    discussionContent = discussionContent.replace("\\n", "\n")
                    discussionContent = discussionContent.replace("\\t", "\t")
                    markwon!!.setMarkdown(
                        fragmentGeneralDiscussionItemBinding.discussionContent,
                        discussionContent
                    )

                    fragmentGeneralDiscussionItemBinding.discussionTitle.isSelected = true
                    fragmentGeneralDiscussionItemBinding.discussionTitle.text =
                        discussionContentJson.data?.topic?.title

                    loadingView.visibility = View.GONE
                    fragmentGeneralDiscussionItemBinding.generalDiscussionLayout.visibility = View.VISIBLE

                }
            }
        })
    }
}