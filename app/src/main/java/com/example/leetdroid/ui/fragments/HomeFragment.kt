package com.example.leetdroid.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.example.leetdroid.R
import com.example.leetdroid.adapter.ContestPagerAdapter

import com.example.leetdroid.api.ContestApi

import com.example.leetdroid.databinding.FragmentHomeBinding
import com.example.leetdroid.model.ContestsModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonArray

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import org.json.JSONArray
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
        val listdata = ArrayList<ContestsModel>()


        apiInterface.enqueue(object : Callback<JsonArray> {
            override fun onResponse(Call: Call<JsonArray>?, response: Response<JsonArray>?) {
                if (response?.body() != null) {
                    val body = response.body()!!
                    val gson = Gson()
                    val listType: Type = object : TypeToken<List<ContestsModel>>() {}.type
                    val posts: ArrayList<ContestsModel> = gson.fromJson(body, listType)
                    Log.d("MyTag", posts[0].toString())
                    displayContests(posts)
                }
            }

            override fun onFailure(call: Call<JsonArray>?, t: Throwable) {
                Log.d("MyTag", "requestFailed", t)
            }

        })

    }

    private fun displayContests(listdata: ArrayList<ContestsModel>) {
        val contestPagerAdapter = ContestPagerAdapter(listdata,requireContext())

        fragmentHomeBinding.viewPager.adapter = contestPagerAdapter
        fragmentHomeBinding.viewPager.setPadding(0, 0, 0, 0)
        TabLayoutMediator(
            fragmentHomeBinding.pageIndicator,
            fragmentHomeBinding.viewPager
        ) { _, _ -> }.attach()

//        fragmentHomeBinding.viewPager.registerOnPageChangeCallback(object :
//            ViewPager2.OnPageChangeCallback() {
//            // This method is triggered when there is any scrolling activity for the current page
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            }
//
//            // triggered when you select a new page
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                when (position) {
//                    0 -> {
//                        val view: Int = R.layout.contest_pager_item
//                        val button = findViewById(R.id.first_button) as Button
//                        button_1.setOnClickListener(object : OnClickListener() {
//                            fun onClick(v: View?) {
//                                finish()
//                            }
//                        })
//
//
//                    }
//                    1 ->{
//
//                    }
//                }
//            }
//
//            // triggered when there is
//            // scroll state will be changed
//            override fun onPageScrollStateChanged(state: Int) {
//                super.onPageScrollStateChanged(state)
//            }
//        })
    }

    object Constant {
        val TAG = HomeFragment::class.qualifiedName
    }

}