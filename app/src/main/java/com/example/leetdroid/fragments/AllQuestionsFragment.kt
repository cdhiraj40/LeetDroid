package com.example.leetdroid.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gdsc_hackathon.extensions.showSnackBar
import com.example.leetdroid.R

import com.example.leetdroid.adapter.AllQuestionsAdapter
import com.example.leetdroid.api.GraphQl
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentAllQuestionsBinding

import com.example.leetdroid.model.AllQuestionsModel
import com.example.leetdroid.utils.JsonUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.String.*

class AllQuestionsFragment : Fragment(), AllQuestionsAdapter.OnItemClicked {

    private lateinit var fragmentAllQuestionsBinding: FragmentAllQuestionsBinding
    private lateinit var questionJson: AllQuestionsModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllQuestionsBinding = FragmentAllQuestionsBinding.inflate(layoutInflater)
        val rootView = fragmentAllQuestionsBinding.root

        loadQuestionList()
        return rootView
    }

    private fun loadQuestionList() {
        val okHttpClient = OkHttpClient()
        val postBody: String = format(GraphQl.ALL_QUESTION_LIST, "", 0, 100)
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
                Log.d(Constant.TAG, call.toString(), e)
            }

            override fun onResponse(call: Call, response: Response) {

                questionJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    AllQuestionsModel::class.java
                )
                activity?.runOnUiThread {
                    val allQuestionsAdapter = AllQuestionsAdapter(questionJson)
                    fragmentAllQuestionsBinding.allQuestionsRecyclerView.layoutManager =
                        LinearLayoutManager(context)
                    fragmentAllQuestionsBinding.allQuestionsRecyclerView.adapter =
                        allQuestionsAdapter
                    allQuestionsAdapter.setOnClick(this@AllQuestionsFragment)
                }
            }
        })
    }

    override fun onItemClick(position: Int, questionTitleSlug:String?) {
        val bundle = bundleOf("questionTitleSlug" to questionTitleSlug)
        fragmentAllQuestionsBinding.root.findNavController().navigate(R.id.action_allQuestionsFragment_to_questionFragment,bundle)
    }

    object Constant {
        const val TAG = "AllQuestionsFragment"
    }
}