package com.example.leetdroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.example.leetdroid.adapter.ContestPagerAdapter
import com.example.leetdroid.api.ContestApi
import com.example.leetdroid.data.entitiy.Contest
import com.example.leetdroid.data.viewModel.ContestViewModel
import com.example.leetdroid.databinding.FragmentHomeBinding

import com.example.leetdroid.ui.fragments.HomeFragment.Constant.TAG
import com.example.leetdroid.utils.Converters
import com.example.leetdroid.utils.Preferences
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonArray
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var preferences: Preferences
    private lateinit var contestViewModel: ContestViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val rootView = fragmentHomeBinding.root

        fragmentHomeBinding.contestProgressBar.visibility = View.VISIBLE
        contestViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[ContestViewModel::class.java]

        preferences = Preferences(requireContext())

        if (preferences.timerEnded || !preferences.contestsInserted) {
            getContestList()
        } else {
            displayContests()
        }
        return rootView
    }

    // gets the contest list
    private fun getContestList() {
        val apiInterface = ContestApi.create().getContent()

        apiInterface.enqueue(object : Callback<JsonArray> {
            override fun onResponse(Call: Call<JsonArray>?, response: Response<JsonArray>?) {
                if (response?.body() != null) {
                    val body = response.body()!!
                    Log.d(TAG, body.toString())

                    saveContests(body)

                    displayContests()
                }
            }

            override fun onFailure(call: Call<JsonArray>?, throwable: Throwable) {
                Log.d(TAG, "requestFailed", throwable)
            }

        })
    }

    // saves the list in local database
    private fun saveContests(body: JsonArray) {
        val contestList =
            Converters.ContestConverter.fromStringContest(body.toString())!!

        val weeklyContest = Contest(
            contestList[0].name,
            contestList[0].url,
            contestList[0].duration,
            contestList[0].start_time,
            contestList[0].end_time,
            contestList[0].in_24_hours,
            contestList[0].status,
        )
        val biWeeklyContest = Contest(
            contestList[1].name,
            contestList[1].url,
            contestList[1].duration,
            contestList[1].start_time,
            contestList[1].end_time,
            contestList[1].in_24_hours,
            contestList[1].status,
        )

        lifecycleScope.launch {
            val preferences = Preferences(requireContext())
            if (!preferences.contestsInserted && !preferences.timerEnded) {
                insertContests(biWeeklyContest, weeklyContest)
            } else {
                updateContests(biWeeklyContest, weeklyContest)
            }
        }
    }

    // updates the contests
    private fun updateContests(biWeeklyContest: Contest, weeklyContest: Contest) {
        Preferences(requireContext()).timerEnded = false
        biWeeklyContest.id = 1
        contestViewModel.updateContest(biWeeklyContest)

        weeklyContest.id = 2
        contestViewModel.updateContest(weeklyContest)
    }

    // inserts the contests in database
    private fun insertContests(biWeeklyContest: Contest, weeklyContest: Contest) {
        val preferences = Preferences(requireContext())
        preferences.contestsInserted = true
        contestViewModel.addContest(biWeeklyContest)
        contestViewModel.addContest(weeklyContest)
    }

    // displays the contests from database
    private fun displayContests() {
        val contestList = ArrayList<Contest>()
        lifecycleScope.launch {
            val biWeeklyContest = contestViewModel.getContest(1)
            val weeklyContest = contestViewModel.getContest(2)

            contestList.add(weeklyContest)
            contestList.add(biWeeklyContest)

            val contestPagerAdapter =
                ContestPagerAdapter(contestList, requireContext(), requireActivity())

            fragmentHomeBinding.viewPager.adapter = contestPagerAdapter
            TabLayoutMediator(
                fragmentHomeBinding.pageIndicator,
                fragmentHomeBinding.viewPager
            ) { _, _ -> }.attach()
            fragmentHomeBinding.contestProgressBar.visibility = View.GONE
        }
    }

    object Constant {
        val TAG = HomeFragment::class.qualifiedName
    }

    override fun onResume() {
        super.onResume()
        fragmentHomeBinding.viewPager.adapter
    }
}