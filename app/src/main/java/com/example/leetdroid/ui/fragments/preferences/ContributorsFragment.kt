package com.example.leetdroid.ui.fragments.preferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leetdroid.adapter.ContributorListAdapter
import com.example.leetdroid.api.GithubApi
import com.example.leetdroid.databinding.FragmentContributorsBinding
import com.example.leetdroid.model.ContributorListModel
import com.example.leetdroid.utils.CommonUtils.openLink
import com.example.leetdroid.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContributorsFragment : Fragment(), ContributorListAdapter.ContributorClickInterface {

    private lateinit var contributorsBinding: FragmentContributorsBinding
    private lateinit var contributorListAdapter: ContributorListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        contributorsBinding = FragmentContributorsBinding.inflate(inflater)

        val rootView = contributorsBinding.root

        getContributors()
        return rootView
    }

    private fun getContributors(
    ) {
        val apiInterface =
            GithubApi.create().getContributors(Constant().userName, Constant().repositoryName)

        apiInterface.enqueue(object : Callback<ContributorListModel> {
            override fun onResponse(
                Call: Call<ContributorListModel>?,
                response: Response<ContributorListModel>?
            ) {
                if (response?.body() != null) {
                    val body = response.body()!!
                    Log.d(
                        Constant.TAG(ContributorsFragment::class.java).toString(), body.toString()
                    )

                    setUpRecyclerView(body)

                }
            }

            override fun onFailure(call: Call<ContributorListModel>?, throwable: Throwable) {
                Log.d(
                    Constant.TAG(ContributorsFragment::class.java).toString(),
                    throwable.message,
                    throwable
                )
            }
        })
    }

    private fun setUpRecyclerView(body: ContributorListModel) {
        contributorListAdapter = ContributorListAdapter(requireContext(), this, body)

        contributorsBinding.contributorRecyclerView.layoutManager =
            LinearLayoutManager(context)
        contributorsBinding.contributorRecyclerView.adapter =
            contributorListAdapter
    }

    override fun onContributorClick(contributor: ContributorListModel.ContributorListModelItem) {
        openLink(requireContext(), contributor.author?.html_url.toString())
    }
}