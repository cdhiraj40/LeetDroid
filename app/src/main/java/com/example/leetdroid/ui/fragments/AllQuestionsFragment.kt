package com.example.leetdroid.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leetdroid.R
import com.example.leetdroid.adapter.AllQuestionsAdapter
import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentAllQuestionsBinding
import com.example.leetdroid.model.AllQuestionsModel
import com.example.leetdroid.ui.base.BaseFragment
import com.example.leetdroid.utils.Constant
import com.example.leetdroid.utils.JsonUtils
import com.example.leetdroid.utils.extensions.closeKeyboard
import com.example.leetdroid.utils.extensions.showSnackBar
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
    private lateinit var generalErrorView: View
    private lateinit var searchKeywords: String
    private var searched: Boolean = false
    private lateinit var loadingView: View

    private var categorySlug: String = ""
    private var selectedCategory: Int = 1

    private var tags: String? = null
    private var difficulty: String? = null
    private var listId: String? = null
    private var limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentAllQuestionsBinding = FragmentAllQuestionsBinding.inflate(layoutInflater)
        val rootView = fragmentAllQuestionsBinding.root

        loadingView = rootView.findViewById(R.id.loading_view)

        loadingView.visibility = View.VISIBLE
        fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.GONE

        generalErrorView = rootView.findViewById(R.id.view_general_error)
        allQuestionsAdapter = AllQuestionsAdapter(requireContext(), requireActivity())

        val bundle = arguments

        tags = bundle?.getString("tag")
        difficulty = bundle?.getString("difficulty")
        listId = bundle?.getString("listId")
        categorySlug = bundle?.getString("categorySlug").toString()

        if (categorySlug == "null") {
            loadQuestionList("", limit, tags, difficulty, listId)
        } else {
            loadQuestionList(categorySlug, limit, tags, difficulty, listId)
        }

        //  adding pagination
        fragmentAllQuestionsBinding.allQuestionsNested.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // fetch more 10 questions when reached end
                limit += 10
                fragmentAllQuestionsBinding.questionListProgressBar.visibility = View.VISIBLE
                if (!searched && selectedCategory == 1) {
                    loadQuestionList(categorySlug, limit, tags, difficulty, listId)
                } else if (searched) {
                    searchQuestions(limit, searchKeywords)
                } else {
                    when (selectedCategory) {
                        1 -> loadQuestionList("", limit, tags, difficulty, listId)

                        2 -> loadQuestionList("algorithms", limit, tags, difficulty, listId)

                        3 -> loadQuestionList("database", limit, tags, difficulty, listId)

                        4 -> loadQuestionList("shell", limit, tags, difficulty, listId)

                        5 -> loadQuestionList("concurrency", limit, tags, difficulty, listId)

                    }
                }
            }
        })

        clickCategory()
        showCategories()

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
                    if (fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility == View.VISIBLE) {
                        fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility =
                            View.GONE
                    }

                    resetCategory()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.question_categories -> {
                showHideCategories()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showHideCategories() {
        if (fragmentAllQuestionsBinding.categoryLayout.isVisible) {
            showCategories()
        } else {
            hideCategories()
        }
    }

    private fun hideCategories() {
        fragmentAllQuestionsBinding.categoryLayout.visibility = View.VISIBLE
    }

    private fun showCategories() {
        fragmentAllQuestionsBinding.categoryLayout.visibility = View.GONE

        when (selectedCategory) {
            1 -> {
                fragmentAllQuestionsBinding.allTopicsLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            2 -> {
                fragmentAllQuestionsBinding.algorithmsLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            3 -> {
                fragmentAllQuestionsBinding.databaseLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            4 -> {
                fragmentAllQuestionsBinding.shellLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            5 -> {
                fragmentAllQuestionsBinding.concurrencyLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
        }
    }

    private fun resetCategory() {
        fragmentAllQuestionsBinding.allTopicsLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        fragmentAllQuestionsBinding.algorithmsLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        fragmentAllQuestionsBinding.databaseLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        fragmentAllQuestionsBinding.shellLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        fragmentAllQuestionsBinding.concurrencyLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }

    private fun clickCategory() {
        fragmentAllQuestionsBinding.allTopicsLayout.setOnClickListener {
            fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility = View.GONE
            if (selectedCategory != 1) {
                selectedCategory = 1
                resetCategory()
                fragmentAllQuestionsBinding.allTopicsLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.GONE
                loadQuestionList("", limit, tags, difficulty, listId)
            }
        }
        fragmentAllQuestionsBinding.algorithmsLayout.setOnClickListener {
            fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility = View.GONE
            if (selectedCategory != 2) {
                selectedCategory = 2
                resetCategory()
                fragmentAllQuestionsBinding.algorithmsLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.GONE
                loadQuestionList("algorithms", limit, tags, difficulty, listId)
            }
        }
        fragmentAllQuestionsBinding.databaseLayout.setOnClickListener {
            fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility = View.GONE
            if (selectedCategory != 3) {
                selectedCategory = 3
                resetCategory()
                fragmentAllQuestionsBinding.databaseLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.GONE
                loadQuestionList("database", limit, tags, difficulty, listId)
            }
        }
        fragmentAllQuestionsBinding.shellLayout.setOnClickListener {
            fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility = View.GONE
            if (selectedCategory != 4) {
                selectedCategory = 4
                resetCategory()
                fragmentAllQuestionsBinding.shellLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.GONE
                loadQuestionList("shell", limit, tags, difficulty, listId)
            }
        }
        fragmentAllQuestionsBinding.concurrencyLayout.setOnClickListener {
            fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility = View.GONE
            if (selectedCategory != 5) {
                selectedCategory = 5
                resetCategory()
                fragmentAllQuestionsBinding.concurrencyLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.GONE
                loadQuestionList("concurrency", limit, tags, difficulty, listId)
            }
        }
    }

    private fun questionListApiCall(
        categorySlug: String,
        limit: Int,
        filters: LeetCodeRequests.Filters
    ): Call {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(
                LeetCodeRequests.Helper.getAllQuestionsRequest(
                    categorySlug, limit,
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

    private fun loadQuestionList(
        categorySlug: String,
        limit: Int,
        tags: String?,
        difficulty: String?,
        listId: String?
    ) {

        val call: Call =
            if ((tags == null || tags == "null") && (difficulty == null || difficulty == "null") && (listId == null || listId == "null")) {
                questionListApiCall(
                    categorySlug,
                    limit,
                    filters = LeetCodeRequests.Filters(
                        tags = listOf(),
                        difficulty = null,
                        listId = null
                    )
                )
            } else if ((tags == null || tags == "null") && (difficulty == null || difficulty == "null")) {
                ifTagsDiffNull(categorySlug, limit, listId)
            } else if ((difficulty == null || difficulty == "null") && (listId == null || listId == "null")) {
                isDiffListNull(categorySlug, limit, tags)
            } else if ((tags == null || tags == "null") && (listId == null || listId == "null")) {
                isTagsListIdNull(categorySlug, limit, difficulty)
            } else {
                questionListApiCall(
                    categorySlug,
                    limit,
                    filters = LeetCodeRequests.Filters(tags = listOf(), difficulty = difficulty)
                )
            }
        call.enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                //TODO add try catch block on every json thing, not sure why but gives error, maybe show something went wrong please try again
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

                        loadingView.visibility = View.GONE
                        fragmentAllQuestionsBinding.allQuestionsNested.visibility = View.VISIBLE
                        checkIfEmpty()

                    }
                } catch (exception: NullPointerException) {
                    generalErrorView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                showSnackBar(requireActivity(), e.message)
                Log.d(Constant.TAG(AllQuestionsFragment::class.java).toString(), call.toString(), e)
            }

        })
    }

    private fun searchQuestions(limit: Int, query: String) {
        val call: Call =
            questionListApiCall("", limit, LeetCodeRequests.Filters(searchKeywords = query))
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
                        fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility =
                            View.GONE
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
                        fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility =
                            View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                showSnackBar(requireActivity(), e.message)
                Log.d(Constant.TAG(AllQuestionsFragment::class.java).toString(), call.toString(), e)
            }
        })
    }

    private fun ifTagsDiffNull(
        categorySlug: String,
        limit: Int,
        listId: String?
    ): Call {
        return (
                questionListApiCall(
                    categorySlug,
                    limit,
                    filters = LeetCodeRequests.Filters(
                        listId = listId
                    )
                )
                )
    }

    private fun isDiffListNull(
        categorySlug: String,
        limit: Int,
        tags: String?
    ): Call {
        return (
                questionListApiCall(
                    categorySlug,
                    limit,
                    filters = LeetCodeRequests.Filters(
                        tags = listOf(tags)
                    )
                )
                )
    }

    private fun isTagsListIdNull(
        categorySlug: String,
        limit: Int,
        difficulty: String?
    ): Call {
        return (
                questionListApiCall(
                    categorySlug,
                    limit,
                    filters = LeetCodeRequests.Filters(
                        difficulty = difficulty,
                    )
                )
                )

    }

    private fun checkIfEmpty() {
        if (allQuestionsAdapter.getDataItemCount() == 0) {
            fragmentAllQuestionsBinding.viewNoQuestions.layoutNoQuestions.visibility = View.VISIBLE
//            fragmentAllQuestionsBinding.questionListProgressBar.visibility = View.VISIBLE
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
}
