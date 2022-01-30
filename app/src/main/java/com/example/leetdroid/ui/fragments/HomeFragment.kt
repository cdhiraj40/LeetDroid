package com.example.leetdroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope

import com.example.leetdroid.adapter.ContestPagerAdapter

import com.example.leetdroid.api.ContestApi
import com.example.leetdroid.data.db.ContestsDatabase
import com.example.leetdroid.data.db.UserDatabase
import com.example.leetdroid.data.entitiy.Contest
import com.example.leetdroid.data.entitiy.User

import com.example.leetdroid.databinding.FragmentHomeBinding
import com.example.leetdroid.model.ContestsModel
import com.example.leetdroid.ui.fragments.HomeFragment.Constant.TAG
import com.example.leetdroid.utils.Converters
import com.example.leetdroid.utils.Preferences
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonArray

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.lang.reflect.Type


class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var contestContentJson: ContestsModel
    private lateinit var preferences: Preferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val rootView = fragmentHomeBinding.root

        preferences = Preferences(requireContext())
        getContestList()
        return rootView
    }

    private fun getContestList() {
        val apiInterface = ContestApi.create().getContent()

        apiInterface.enqueue(object : Callback<JsonArray> {
            override fun onResponse(Call: Call<JsonArray>?, response: Response<JsonArray>?) {
                if (response?.body() != null) {
                    val body = response.body()!!
                    val gson = Gson()
                    val listType: Type = object : TypeToken<List<Contest>>() {}.type
                    var contestList: ArrayList<Contest> = gson.fromJson(body, listType)
                    Log.d(TAG, contestList.toString())

                    val biWeeklyContest = Contest(
                        "Asdasd",
                        contestList[0].url,
                        contestList[0].duration,
                        contestList[0].start_time,
                        contestList[0].end_time,
                        contestList[0].in_24_hours,
                        contestList[0].status,
                    )
                    val weeklyContest = Contest(
                        contestList[1].name,
                        contestList[1].url,
                        contestList[1].duration,
                        contestList[1].start_time,
                        contestList[1].end_time,
                        contestList[1].in_24_hours,
                        contestList[1].status,
                    )
                    contestList = java.util.ArrayList<Contest>()

                    lifecycleScope.launch {
                        val biWeeklyContest1 =
                            ContestsDatabase.getInstance(requireContext()).contestDao()
                                .getContest(1)
                        if (!preferences.contestsInserted) {
                            insertData(biWeeklyContest, weeklyContest)
                        } else {
                            ContestsDatabase.getInstance(requireContext()).contestDao()
                                .update(biWeeklyContest1)
                            ContestsDatabase.getInstance(requireContext()).contestDao()
                                .update(weeklyContest)
                        }
//                        fillList(contestList)
//                        displayContests(contestList)
                    }

                }
            }

            override fun onFailure(call: Call<JsonArray>?, t: Throwable) {
                Log.d("MyTag", "requestFailed", t)
            }

        })

    }

    private suspend fun insertData(biWeeklyContest: Contest, weeklyContest: Contest) {
        preferences.contestsInserted = true
        ContestsDatabase.getInstance(requireContext()).contestDao()
            .insert(biWeeklyContest)
        ContestsDatabase.getInstance(requireContext()).contestDao()
            .insert(weeklyContest)
    }

    private fun fillList(contestList: ArrayList<Contest>) {
        lifecycleScope.launch {
            val biWeeklyContest =
                ContestsDatabase.getInstance(requireContext()).contestDao().getContest(1)
            val weeklyContest =
                ContestsDatabase.getInstance(requireContext()).contestDao().getContest(2)
            contestList.addAll(listOf(biWeeklyContest))

//            add(
//                biWeeklyContest.name,
//                biWeeklyContest.url,
//                biWeeklyContest.duration,
//                biWeeklyContest.start_time,
//                biWeeklyContest.end_time,
//                biWeeklyContest.in_24_hours,
//                biWeeklyContest.status
//            )
        }
    }

    private fun displayContests(listdata: ArrayList<Contest>) {
        val contestPagerAdapter = ContestPagerAdapter(listdata, requireContext())

        fragmentHomeBinding.viewPager.adapter = contestPagerAdapter
        TabLayoutMediator(
            fragmentHomeBinding.pageIndicator,
            fragmentHomeBinding.viewPager
        ) { _, _ -> }.attach()
    }

    object Constant {
        val TAG = HomeFragment::class.qualifiedName
    }

    override fun onResume() {
        super.onResume()
        fragmentHomeBinding.viewPager.adapter
    }
}