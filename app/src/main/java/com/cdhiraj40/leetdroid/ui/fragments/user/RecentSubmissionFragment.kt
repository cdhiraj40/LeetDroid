package com.cdhiraj40.leetdroid.ui.fragments.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdhiraj40.leetdroid.R
import com.cdhiraj40.leetdroid.adapter.RecentSubmissionsAdapter
import com.cdhiraj40.leetdroid.api.LeetCodeRequests
import com.cdhiraj40.leetdroid.api.URL
import com.cdhiraj40.leetdroid.databinding.FragmentRecentSubmissionBinding
import com.cdhiraj40.leetdroid.model.RecentSubmissionsModel
import com.cdhiraj40.leetdroid.utils.Constant
import com.cdhiraj40.leetdroid.utils.JsonUtils
import com.cdhiraj40.leetdroid.utils.extensions.copyToClipboard
import com.cdhiraj40.leetdroid.utils.extensions.showSnackBar
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class RecentSubmissionFragment : Fragment(), RecentSubmissionsAdapter.OnItemClicked {

    private lateinit var fragmentRecentSubmissionBinding: FragmentRecentSubmissionBinding
    private lateinit var submissionsJson: RecentSubmissionsModel
    private lateinit var submissionsAdapter: RecentSubmissionsAdapter
    private lateinit var generalErrorView: View
    private lateinit var username: String
    private var limit = 10
    private lateinit var loadingView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentRecentSubmissionBinding = FragmentRecentSubmissionBinding.inflate(layoutInflater)
        val rootView = fragmentRecentSubmissionBinding.root
        loadingView = rootView.findViewById(R.id.loading_view)
        loadingView.visibility = View.VISIBLE
        generalErrorView = rootView.findViewById(R.id.view_general_error)
        submissionsAdapter = RecentSubmissionsAdapter(requireContext())

        val bundle = arguments
        username = bundle?.getString("username").toString()
        loadRecentSubmissions(username, limit)


        //  adding pagination
        fragmentRecentSubmissionBinding.allSubmissionsNested.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    // fetch more 10 questions when reached end
                    limit += 10
                    loadingView.visibility =
                        View.VISIBLE
                    loadRecentSubmissions(username, limit)

                }
            })
        return rootView
    }

    private fun questionListApiCall(username: String, limit: Int): Call {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(
                LeetCodeRequests.Helper.getRecentSubmissions(
                    username, limit
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
        return okHttpClient.newCall(request)
    }

    private fun loadRecentSubmissions(username: String, limit: Int) {

        val call: Call =
            questionListApiCall(username, limit)
        call.enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                //TODO add try catch block on every json thing, not sure why but gives error, maybe show somethign went wrong please try again
                try {
                    submissionsJson = JsonUtils.generateObjectFromJson(
                        response.body!!.string(),
                        RecentSubmissionsModel::class.java
                    )
                    activity?.runOnUiThread {
                        fragmentRecentSubmissionBinding.allSubmissionsNested.visibility =
                            View.VISIBLE
                        submissionsAdapter.setData(submissionsJson)
                        fragmentRecentSubmissionBinding.recentSubmissionsRecycler.layoutManager =
                            LinearLayoutManager(context)
                        fragmentRecentSubmissionBinding.recentSubmissionsRecycler.adapter =
                            submissionsAdapter

                        submissionsAdapter.setOnClick(this@RecentSubmissionFragment)
                        checkIfEmpty()

                    }
                } catch (exception: Exception) {
                    generalErrorView.visibility = View.VISIBLE
                    loadingView.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                showSnackBar(requireActivity(), e.message)
                loadingView.visibility = View.GONE
                Log.d(
                    Constant.TAG(RecentSubmissionFragment::class.java).toString(),
                    call.toString(),
                    e
                )
            }

        })
    }

    private fun checkIfEmpty() {
        if (submissionsAdapter.getDataItemCount() == 0) {
            loadingView.visibility = View.VISIBLE
        } else {
            loadingView.visibility = View.GONE
        }
    }

    override fun onItemClick(position: Int, questionTitleSlug: String?) {
        requireContext().copyToClipboard(questionTitleSlug.toString())
        showSnackBar(requireActivity(), "Question title copied")
    }

}