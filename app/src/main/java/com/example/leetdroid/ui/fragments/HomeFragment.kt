package com.example.leetdroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.leetdroid.adapter.ContestPagerAdapter

import com.example.leetdroid.api.ContestApi

import com.example.leetdroid.databinding.FragmentHomeBinding
import com.example.leetdroid.model.ContestsModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonArray

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import java.lang.reflect.Type


class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var contestContentJson: ContestsModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val rootView = fragmentHomeBinding.root

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
                    val listType: Type = object : TypeToken<List<ContestsModel>>() {}.type
                    val posts: ArrayList<ContestsModel> = gson.fromJson(body, listType)
                    Log.d("MyTag", posts.toString())
                    displayContests(posts)
                }
            }

            override fun onFailure(call: Call<JsonArray>?, t: Throwable) {
                Log.d("MyTag", "requestFailed", t)
            }

        })

    }

    private fun displayContests(listdata: ArrayList<ContestsModel>) {
        val contestPagerAdapter = ContestPagerAdapter(listdata, requireContext())

        fragmentHomeBinding.viewPager.adapter = contestPagerAdapter
        fragmentHomeBinding.viewPager.setPadding(0, 0, 0, 0)
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