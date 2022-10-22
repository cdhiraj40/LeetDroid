package com.cdhiraj40.leetdroid.ui.fragments.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdhiraj40.leetdroid.R
import com.cdhiraj40.leetdroid.adapter.ContestHistoryAdapter
import com.cdhiraj40.leetdroid.api.LeetCodeRequests
import com.cdhiraj40.leetdroid.api.URL
import com.cdhiraj40.leetdroid.databinding.FragmentContestDetailsBinding
import com.cdhiraj40.leetdroid.utils.extensions.showSnackBar
import com.cdhiraj40.leetdroid.model.ContestRankingModel
import com.cdhiraj40.leetdroid.utils.Constant
import com.cdhiraj40.leetdroid.utils.JsonUtils
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ContestDetailsFragment : Fragment(), ContestHistoryAdapter.OnItemClicked {

    private lateinit var fragmentContestDetailsBinding: FragmentContestDetailsBinding
    private lateinit var contestRankingJson: ContestRankingModel
    private lateinit var contestHistoryAdapter: ContestHistoryAdapter
    private lateinit var generalErrorView: View
    private lateinit var username: String
    private lateinit var loadingView: View
    private var limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentContestDetailsBinding = FragmentContestDetailsBinding.inflate(layoutInflater)
        val rootView = fragmentContestDetailsBinding.root
        loadingView = rootView.findViewById(R.id.loading_view)
        generalErrorView = rootView.findViewById(R.id.view_general_error)
        contestHistoryAdapter = ContestHistoryAdapter(requireContext())
        val bundle = arguments
        username = bundle?.getString("username").toString()
        loadContestDetails(username)

        return rootView
    }

    private fun rankingDetailsApiCall(username: String): Call {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(
                LeetCodeRequests.Helper.getContestRankingData(
                    username
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

    private fun loadContestDetails(username: String) {
        fragmentContestDetailsBinding.contestHistoryProgressBar.visibility = View.VISIBLE
        loadingView.visibility = View.VISIBLE
        val call: Call =
            rankingDetailsApiCall(username)
        call.enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                //TODO add try catch block on every json thing, not sure why but gives error, maybe show somethign went wrong please try again
                try {
                    contestRankingJson = JsonUtils.generateObjectFromJson(
                        response.body!!.string(),
                        ContestRankingModel::class.java
                    )
                    activity?.runOnUiThread {
                        fragmentContestDetailsBinding.noContestAttended.text =
                            contestRankingJson.data?.userContestRanking?.attendedContestsCount.toString()
                        fragmentContestDetailsBinding.userContestGlobalRanking.text =
                            contestRankingJson.data?.userContestRanking?.globalRanking.toString()
                        fragmentContestDetailsBinding.userContestRating.text =
                            contestRankingJson.data?.userContestRanking?.rating.toString()

                        contestHistoryAdapter.setData(contestRankingJson)
                        fragmentContestDetailsBinding.contestHistoryRecyclerView.layoutManager =
                            LinearLayoutManager(context)
                        fragmentContestDetailsBinding.contestHistoryRecyclerView.adapter =
                            contestHistoryAdapter

                        contestHistoryAdapter.setOnClick(this@ContestDetailsFragment)
                        fragmentContestDetailsBinding.contestHistoryProgressBar.visibility =
                            View.GONE
                        loadingView.visibility = View.GONE;

                    }
                } catch (exception: Exception) {
                    loadingView.visibility = View.GONE
                    generalErrorView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                showSnackBar(requireActivity(), e.message)
                Log.d(Constant.TAG(ContestDetailsFragment::class.java).toString(), call.toString(), e)
            }

        })
    }

    override fun onItemClick(position: Int) {
        showSnackBar(requireActivity(), "More Analysis on contests coming soon!")
    }
}