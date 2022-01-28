package com.example.leetdroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.navigation.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leetdroid.R

import com.example.leetdroid.adapter.GeneralDiscussionAdapter
import com.example.leetdroid.api.*
import com.example.leetdroid.databinding.FragmentGeneralDiscussionBinding
import com.example.leetdroid.ui.fragments.GeneralDiscussionFragment.Constant.TAG

import com.example.leetdroid.model.GeneralDiscussionModel

import com.example.leetdroid.utils.JsonUtils
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*


class GeneralDiscussionFragment : Fragment(), GeneralDiscussionAdapter.OnItemClicked {

    private lateinit var fragmentGeneralDiscussionBinding: FragmentGeneralDiscussionBinding
    private lateinit var generalDiscussionsJson: GeneralDiscussionModel
//    private lateinit var leetCodeRequest: LeetCodeRequest = LeetCodeRequest()
    private var generalDiscussionAdapter: GeneralDiscussionAdapter? = null
    private var limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentGeneralDiscussionBinding =
            FragmentGeneralDiscussionBinding.inflate(layoutInflater)
        val rootView = fragmentGeneralDiscussionBinding.root

        loadGeneralDiscussionList(limit)

        // adding pagination
        fragmentGeneralDiscussionBinding.generalDiscussionNested.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    // fetch more 10 questions when reached end
                    limit += 10
                    fragmentGeneralDiscussionBinding.discussionListProgressBar.visibility =
                        View.VISIBLE
                    loadGeneralDiscussionList(limit)
                }
            })
        return rootView
    }

    private fun loadGeneralDiscussionList(limit: Int) {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(LeetCodeRequests.Helper.generalDiscussionRequest(limit))

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
                Log.d(TAG, call.toString(), e)
            }

            override fun onResponse(call: Call, response: Response) {

                generalDiscussionsJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    GeneralDiscussionModel::class.java
                )

                activity?.runOnUiThread {
                    generalDiscussionAdapter =
                        GeneralDiscussionAdapter(generalDiscussionsJson, requireContext())
                    fragmentGeneralDiscussionBinding.generalDiscussionsRecyclerView.layoutManager =
                        LinearLayoutManager(context)
                    fragmentGeneralDiscussionBinding.generalDiscussionsRecyclerView.adapter =
                        generalDiscussionAdapter

                    generalDiscussionAdapter!!.setOnClick(this@GeneralDiscussionFragment)
                    checkIfEmpty()
                }
            }
        })
    }

    private fun checkIfEmpty() {
        if (generalDiscussionAdapter!!.getDataItemCount() == 0) {
            fragmentGeneralDiscussionBinding.discussionListProgressBar.visibility = View.VISIBLE
        } else {
            fragmentGeneralDiscussionBinding.discussionListProgressBar.visibility = View.GONE
        }
    }

    override fun onItemClick(position: Int, discussionId: Int?) {
        val bundle = bundleOf(
            "discussionId" to discussionId,
        )
        fragmentGeneralDiscussionBinding.root.findNavController()
            .navigate(R.id.action_generalDiscussionFragment_to_generalDiscussionItemFragment, bundle)
    }

    object Constant {
        val TAG = GeneralDiscussionFragment::class.qualifiedName
    }
}