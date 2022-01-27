package com.example.leetdroid.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leetdroid.R
import com.example.leetdroid.adapter.AllQuestionsAdapter
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentAllQuestionsBinding
import com.example.leetdroid.model.AllQuestionsModel
import com.example.leetdroid.utils.JsonUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.String.*
import androidx.core.widget.NestedScrollView
import com.example.leetdroid.extensions.showSnackBar
import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.ui.fragments.question.QuestionFragment
import com.google.gson.Gson
import java.util.*


class AllQuestionsFragment : Fragment(), AllQuestionsAdapter.OnItemClicked {

    private lateinit var fragmentAllQuestionsBinding: FragmentAllQuestionsBinding
    private lateinit var questionJson: AllQuestionsModel
    private var allQuestionsAdapter: AllQuestionsAdapter? = null

    private var limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllQuestionsBinding = FragmentAllQuestionsBinding.inflate(layoutInflater)
        val rootView = fragmentAllQuestionsBinding.root

        loadQuestionList(limit)

        // adding pagination
        fragmentAllQuestionsBinding.allQuestionsNested.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // fetch more 10 questions when reached end
                limit += 10
                fragmentAllQuestionsBinding.questionListProgressBar.visibility = View.VISIBLE
                loadQuestionList(limit)
            }
        })
        return rootView
    }

    private fun loadQuestionList(limit: Int) {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(LeetCodeRequests.Helper.getAllQuestionsRequest("",limit))
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
                showSnackBar(requireActivity(), e.message)
                Log.d(Constant.TAG, call.toString(), e)
            }

            override fun onResponse(call: Call, response: Response) {

                questionJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    AllQuestionsModel::class.java
                )

                activity?.runOnUiThread {
                    allQuestionsAdapter = AllQuestionsAdapter(questionJson)
                    fragmentAllQuestionsBinding.allQuestionsRecyclerView.layoutManager =
                        LinearLayoutManager(context)
                    fragmentAllQuestionsBinding.allQuestionsRecyclerView.adapter =
                        allQuestionsAdapter

                    allQuestionsAdapter!!.setOnClick(this@AllQuestionsFragment)
                    checkIfEmpty()

                }
            }
        })
    }

    private fun checkIfEmpty() {
        if (allQuestionsAdapter!!.getDataItemCount() == 0) {
            fragmentAllQuestionsBinding.questionListProgressBar.visibility = View.VISIBLE
        } else {
            fragmentAllQuestionsBinding.questionListProgressBar.visibility = View.GONE
        }
    }

    override fun onItemClick(
        position: Int,
        questionTitleSlug: String?,
        questionHasSolution: Boolean?,
        questionID: String?
    ) {

        val bundle = bundleOf(
            "questionTitleSlug" to questionTitleSlug,
            "questionHasSolution" to questionHasSolution,
            "questionID" to questionID
        )
        fragmentAllQuestionsBinding.root.findNavController()
            .navigate(R.id.action_allQuestionsFragment_to_questionFragment, bundle)
    }

    object Constant {
        val TAG = AllQuestionsFragment::class.qualifiedName
    }
}