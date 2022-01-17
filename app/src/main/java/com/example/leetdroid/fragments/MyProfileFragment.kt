package com.example.leetdroid.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.bumptech.glide.Glide

import com.example.leetdroid.api.GraphQl
import com.example.leetdroid.api.URL

import com.example.leetdroid.databinding.FragmentMyProfileBinding
import com.example.leetdroid.model.UserProfileModel
import com.example.leetdroid.utils.JsonUtils

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class MyProfileFragment : Fragment() {

    private lateinit var myProfileBinding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        myProfileBinding = FragmentMyProfileBinding.inflate(layoutInflater)
        val rootView = myProfileBinding.root

        loadData()
        return rootView
    }

    private fun loadData() {
        val okHttpClient = OkHttpClient()
        val postBody = java.lang.String.format(GraphQl.GET_USER_PROFILE, "cdhiraj40")
        val requestBody: RequestBody =
            RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), postBody)
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
                Log.d(Constant.TAG, call.toString(),e)
            }

            override fun onResponse(call: Call, response: Response) {

                val discussItem: UserProfileModel = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    UserProfileModel::class.java
                )
                activity?.runOnUiThread {

                    myProfileBinding.username.text =
                        discussItem.data!!.matchedUser?.profile?.realName
                    Glide.with(requireContext())
                        .load(
                            discussItem.data!!.matchedUser?.profile?.userAvatar
                        )
                        .circleCrop()
                        .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                        .into(myProfileBinding.userProfileAvatar)
                }
            }
        })
    }
    object Constant {
        const val TAG = "MyProfileFragment"
    }
}