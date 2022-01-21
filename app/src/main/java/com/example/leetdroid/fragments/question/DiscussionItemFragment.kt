package com.example.leetdroid.fragments.question

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.leetdroid.api.GraphQl
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentDiscussionItemBinding
import com.example.leetdroid.fragments.AllQuestionsFragment
import com.example.leetdroid.model.DiscussionItemModel
import com.example.leetdroid.utils.JsonUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import ru.noties.markwon.Markwon
import java.io.IOException
import java.util.*

class DiscussionItemFragment : Fragment() {
    private lateinit var fragmentDiscussionItemBinding: FragmentDiscussionItemBinding
    private var markwon: Markwon? = null
    private var discussionId: Int? = 0
    private lateinit var discussionContentJson: DiscussionItemModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        fragmentDiscussionItemBinding = FragmentDiscussionItemBinding.inflate(inflater)

        val rootView = fragmentDiscussionItemBinding.root

        val bundle = arguments

        discussionId = bundle?.getInt("discussionId")

        markwon = Markwon.create(requireContext())

        loadDiscussionItemContent(discussionId)
        return rootView

    }

    private fun loadDiscussionItemContent(discussionId: Int?) {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            java.lang.String.format(
                Locale.ENGLISH,
                GraphQl.DISCUSS_ITEM, discussionId
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
                        fragmentDiscussionItemBinding.discussionContent,
                        discussionContent
                    )

                    fragmentDiscussionItemBinding.topicTitle.isSelected = true
                    fragmentDiscussionItemBinding.topicTitle.text =
                        discussionContentJson.data?.topic?.title
//                    (requireActivity() as AppCompatActivity).supportActionBar?.title =


                }
            }
        })
    }
}