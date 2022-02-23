package com.example.leetdroid.ui.fragments.discussion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leetdroid.R
import com.example.leetdroid.adapter.TrendingDiscussionAdapter
import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentTrendingDiscussionBinding
import com.example.leetdroid.model.TrendingDiscussionModel
import com.example.leetdroid.ui.fragments.HomeFragment
import com.example.leetdroid.utils.Constant
import com.example.leetdroid.utils.JsonUtils
import com.example.leetdroid.utils.extensions.showSnackBar
import com.google.gson.Gson
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class TrendingDiscussionFragment : Fragment(), TrendingDiscussionAdapter.OnItemClicked {

    private lateinit var trendingDiscussionBinding: FragmentTrendingDiscussionBinding
    private lateinit var trendingDiscussionAdapter: TrendingDiscussionAdapter
    private lateinit var loadingView: View
    private var limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        trendingDiscussionBinding = FragmentTrendingDiscussionBinding.inflate(inflater)

        val rootView = trendingDiscussionBinding.root

        trendingDiscussionAdapter = TrendingDiscussionAdapter(requireContext())
        trendingDiscussionAdapter.setOnClick(this)

        loadingView = rootView.findViewById(R.id.loading_view)
        trendingDiscussionBinding.trendingDiscussionLayout.visibility = View.GONE
        loadingView.visibility = View.VISIBLE

        loadTrendingDiscussions(limit)

        // adding pagination
        if (limit < 20) {
            trendingDiscussionBinding.trendingDiscussionNested.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    // on scroll change we are checking when users scroll as bottom.
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        // fetch more 10 questions when reached end
                        limit = 20
                        trendingDiscussionBinding.discussionListProgressBar.visibility =
                            View.VISIBLE
                        loadTrendingDiscussions(limit)
                    }
                })
        }

        return rootView
    }

    // loads trending Discussion from online
    private fun loadTrendingDiscussions(limit: Int) {

        val call: okhttp3.Call = apiCallTrendingDiss(limit)
        call.enqueue(object : okhttp3.Callback {

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(Constant.TAG(HomeFragment::class.java).toString(), call.toString(), e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {

                val trendingDiscussion: TrendingDiscussionModel = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    TrendingDiscussionModel::class.java
                )
                Log.d(
                    Constant.TAG(HomeFragment::class.java).toString(),
                    trendingDiscussion.toString()
                )
                activity?.runOnUiThread {
                    displayTrendingDiscussions(trendingDiscussion)
                    checkIfEmpty()
                }
            }
        })
    }

    private fun displayTrendingDiscussions(trendingDiscussion: TrendingDiscussionModel) {
        trendingDiscussionAdapter.setData(trendingDiscussion)
        trendingDiscussionBinding.trendingDiscussRecyclerView.layoutManager =
            LinearLayoutManager(context)
        trendingDiscussionBinding.trendingDiscussRecyclerView.adapter =
            trendingDiscussionAdapter
        trendingDiscussionBinding.trendingDiscussionLayout.visibility = View.VISIBLE
        loadingView.visibility = View.GONE
    }

    // creates an okHttpClient call for Trending Discussions
    private fun apiCallTrendingDiss(limit: Int): okhttp3.Call {
        val okHttpClient = OkHttpClient()
        val postBody = Gson().toJson(LeetCodeRequests.Helper.getTrendingDiscussion(limit))
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

    private fun checkIfEmpty() {
        if (trendingDiscussionAdapter.getDataItemCount() == 0) {
            trendingDiscussionBinding.discussionListProgressBar.visibility =
                View.VISIBLE
        } else {
            trendingDiscussionBinding.discussionListProgressBar.visibility =
                View.GONE
        }
    }

    override fun onItemClick(position: Int) {
        showSnackBar(requireActivity(), "asdsa")
    }
}