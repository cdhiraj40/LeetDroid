package com.example.leetdroid.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leetdroid.R
import com.example.leetdroid.adapter.AllQuestionsAdapter
import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentAllQuestionsBinding
import com.example.leetdroid.extensions.closeKeyboard
import com.example.leetdroid.extensions.showSnackBar
import com.example.leetdroid.model.AllQuestionsModel
import com.example.leetdroid.ui.base.BaseFragment
import com.example.leetdroid.utils.JsonUtils
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.String.*
import java.util.*


class AllQuestionsFragment : BaseFragment(), AllQuestionsAdapter.OnItemClicked {

    private lateinit var fragmentAllQuestionsBinding: FragmentAllQuestionsBinding
    private lateinit var questionJson: AllQuestionsModel
    private lateinit var allQuestionsAdapter: AllQuestionsAdapter
    private lateinit var noQuestionsView: View
    private lateinit var generalErrorView: View
    private lateinit var searchKeywords: String
    private var searched: Boolean = false
    private var limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllQuestionsBinding = FragmentAllQuestionsBinding.inflate(layoutInflater)
        val rootView = fragmentAllQuestionsBinding.root

        noQuestionsView = rootView.findViewById(R.id.view_no_questions)
        generalErrorView = rootView.findViewById(R.id.view_general_error)
        allQuestionsAdapter = AllQuestionsAdapter(requireContext(), requireActivity())
        loadQuestionList(limit)

        //  adding pagination
        fragmentAllQuestionsBinding.allQuestionsNested.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // fetch more 10 questions when reached end
                limit += 10
                fragmentAllQuestionsBinding.questionListProgressBar.visibility = View.VISIBLE
                if (!searched) {
                    loadQuestionList(limit)
                } else {
                    searchQuestions(limit, searchKeywords)
                }
            }
        })

        return rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // clear the Main Activity's menu
        menu.clear()
        inflater.inflate(R.menu.all_questions_list_menu, menu)

        val searchItem = menu.findItem(R.id.search_bar)
        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isEmpty()) {
                        showSnackBar(requireActivity(), "Please enter a keyword")
                    }
                    searchKeywords = query
                    searched = true
                    if (noQuestionsView.visibility == View.VISIBLE) {
                        noQuestionsView.visibility = View.GONE
                    }

                    fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.GONE
                    searchQuestions(limit, searchKeywords)
                    fragmentAllQuestionsBinding.searchProgressBar.visibility = View.VISIBLE
                    closeKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun questionListApiCall(limit: Int, filters: LeetCodeRequests.Filters): Call {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(
                LeetCodeRequests.Helper.getAllQuestionsRequest(
                    "", limit,
                    filters
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

    private fun loadQuestionList(limit: Int) {

        val call: Call =
            questionListApiCall(limit, filters = LeetCodeRequests.Filters(tags = listOf()))
        call.enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                //TODO add try catch block on every json thing, not sure why but gives error, maybe show somethign went wrong please try again
                try {
                    questionJson = JsonUtils.generateObjectFromJson(
                        response.body!!.string(),
                        AllQuestionsModel::class.java
                    )
                    activity?.runOnUiThread {
                        fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.VISIBLE
                        allQuestionsAdapter.setData(questionJson)
                        fragmentAllQuestionsBinding.allQuestionsRecyclerView.layoutManager =
                            LinearLayoutManager(context)
                        fragmentAllQuestionsBinding.allQuestionsRecyclerView.adapter =
                            allQuestionsAdapter

                        allQuestionsAdapter.setOnClick(this@AllQuestionsFragment)
                        checkIfEmpty()

                    }
                } catch (exception: Exception) {
                    generalErrorView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                showSnackBar(requireActivity(), e.message)
                Log.d(Constant.TAG, call.toString(), e)
            }

        })
    }

    private fun searchQuestions(limit: Int, query: String) {
        val call: Call =
            questionListApiCall(limit, LeetCodeRequests.Filters(searchKeywords = query))
        call.enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                questionJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    AllQuestionsModel::class.java
                )

                if (questionJson.data?.problemsetQuestionList != null) {
                    activity?.runOnUiThread {
                        fragmentAllQuestionsBinding.searchProgressBar.visibility = View.GONE
                        fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.VISIBLE
                        noQuestionsView.visibility = View.GONE
                        allQuestionsAdapter.setData(questionJson)
                        fragmentAllQuestionsBinding.allQuestionsRecyclerView.layoutManager =
                            LinearLayoutManager(context)
                        fragmentAllQuestionsBinding.allQuestionsRecyclerView.adapter =
                            allQuestionsAdapter

                        allQuestionsAdapter.setOnClick(this@AllQuestionsFragment)
                        checkIfEmpty()
                    }
                } else {
                    activity?.runOnUiThread {
                        fragmentAllQuestionsBinding.searchProgressBar.visibility = View.GONE
                        noQuestionsView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                showSnackBar(requireActivity(), e.message)
                Log.d(Constant.TAG, call.toString(), e)
            }
        })
    }

    private fun checkIfEmpty() {
        if (allQuestionsAdapter.getDataItemCount() == 0) {
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