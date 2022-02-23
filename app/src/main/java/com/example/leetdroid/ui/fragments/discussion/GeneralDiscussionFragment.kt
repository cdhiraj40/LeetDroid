package com.example.leetdroid.ui.fragments.discussion

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.leetdroid.R
import com.example.leetdroid.adapter.GeneralDiscussionAdapter
import com.example.leetdroid.api.*
import com.example.leetdroid.databinding.FragmentGeneralDiscussionBinding
import com.example.leetdroid.model.GeneralDiscussionModel
import com.example.leetdroid.utils.Constant.Companion.TAG
import com.example.leetdroid.utils.JsonUtils
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.*


class GeneralDiscussionFragment : Fragment(), GeneralDiscussionAdapter.OnItemClicked {

    private lateinit var fragmentGeneralDiscussionBinding: FragmentGeneralDiscussionBinding
    private lateinit var generalDiscussionsJson: GeneralDiscussionModel
    private lateinit var loadingView: View
    private var category: List<String> = listOf("general-discussion")
    private var selectedCategory: Int = 6

    private var generalDiscussionAdapter: GeneralDiscussionAdapter? = null
    private var limit = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentGeneralDiscussionBinding =
            FragmentGeneralDiscussionBinding.inflate(layoutInflater)
        val rootView = fragmentGeneralDiscussionBinding.root


        loadingView = rootView.findViewById(R.id.loading_view)

        loadingView.visibility = View.VISIBLE
        fragmentGeneralDiscussionBinding.generalDiscussionNested.visibility = View.GONE

        loadGeneralDiscussionList(limit, category)

        // adding pagination
        fragmentGeneralDiscussionBinding.generalDiscussionNested.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    // fetch more 10 questions when reached end
                    limit += 10
                    fragmentGeneralDiscussionBinding.discussionListProgressBar.visibility =
                        View.VISIBLE
                    if (selectedCategory == 6) {
                        loadGeneralDiscussionList(limit, category)
                    } else {
                        when (selectedCategory) {
                            1 -> loadGeneralDiscussionList(limit, listOf("interview-question"))

                            2 -> loadGeneralDiscussionList(limit, listOf("interview-experience"))

                            3 -> loadGeneralDiscussionList(limit, listOf("compensation"))

                            4 -> loadGeneralDiscussionList(limit, listOf("career"))

                            5 -> loadGeneralDiscussionList(limit, listOf("study-guide"))

                            6 -> loadGeneralDiscussionList(limit, listOf("general-discussion"))

                            7 -> loadGeneralDiscussionList(limit, listOf("feedback"))
                        }
                    }
                }
            })

        clickCategory()
        showCategories()
        return rootView
    }

    private fun createApiCall(limit: Int, category: List<String>): Call {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(LeetCodeRequests.Helper.generalDiscussionRequest(limit, category))

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

    private fun loadGeneralDiscussionList(limit: Int, category: List<String>) {

        val call: Call = createApiCall(limit, category)
        call.enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG(GeneralDiscussionFragment::class.java).toString(), call.toString(), e)
            }

            override fun onResponse(call: Call, response: Response) {

                generalDiscussionsJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    GeneralDiscussionModel::class.java
                )

                activity?.runOnUiThread {
                    generalDiscussionAdapter =
                        GeneralDiscussionAdapter(generalDiscussionsJson, requireContext())
                    fragmentGeneralDiscussionBinding.generalDiscussionsRecyclerView.layoutManager =
                        LinearLayoutManager(context)
                    fragmentGeneralDiscussionBinding.generalDiscussionsRecyclerView.adapter =
                        generalDiscussionAdapter

                    generalDiscussionAdapter!!.setOnClick(this@GeneralDiscussionFragment)

                    loadingView.visibility = View.GONE
                    fragmentGeneralDiscussionBinding.generalDiscussionNested.visibility =
                        View.VISIBLE
                    checkIfEmpty()
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // clear the Main Activity's menu
        menu.clear()
        inflater.inflate(R.menu.general_discussion_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.categories -> {
                showHideCategories()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showHideCategories() {
        if (fragmentGeneralDiscussionBinding.categoryLayout.isVisible) {
            showCategories()
        } else {
            hideCategories()
        }
    }

    private fun hideCategories() {
        fragmentGeneralDiscussionBinding.categoryLayout.visibility = View.VISIBLE
    }

    private fun showCategories() {
        fragmentGeneralDiscussionBinding.categoryLayout.visibility = View.GONE

        when (selectedCategory) {
            1 -> {
                fragmentGeneralDiscussionBinding.interviewQuestionsLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            2 -> {
                fragmentGeneralDiscussionBinding.interviewExperienceLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            3 -> {
                fragmentGeneralDiscussionBinding.compensationLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            4 -> {
                fragmentGeneralDiscussionBinding.careerLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            5 -> {
                fragmentGeneralDiscussionBinding.studyGuideLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            6 -> {
                fragmentGeneralDiscussionBinding.generalDiscussionLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }

            7 -> {
                fragmentGeneralDiscussionBinding.supportAndFeedbackLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
        }
    }

    private fun resetCategory() {
        fragmentGeneralDiscussionBinding.interviewQuestionsLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        fragmentGeneralDiscussionBinding.interviewExperienceLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        fragmentGeneralDiscussionBinding.compensationLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        fragmentGeneralDiscussionBinding.careerLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        fragmentGeneralDiscussionBinding.studyGuideLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
        fragmentGeneralDiscussionBinding.generalDiscussionLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )

        fragmentGeneralDiscussionBinding.supportAndFeedbackLayout.setCardBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }

    private fun clickCategory() {

        fragmentGeneralDiscussionBinding.interviewQuestionsLayout.setOnClickListener {
            if (selectedCategory != 1) {
                selectedCategory = 1
                resetCategory()
                fragmentGeneralDiscussionBinding.interviewQuestionsLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentGeneralDiscussionBinding.generalDiscussionNested.visibility =
                    View.GONE
                loadGeneralDiscussionList(limit, listOf("interview-question"))
            }
        }
        fragmentGeneralDiscussionBinding.interviewExperienceLayout.setOnClickListener {
            if (selectedCategory != 2) {
                selectedCategory = 2
                resetCategory()
                fragmentGeneralDiscussionBinding.interviewExperienceLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentGeneralDiscussionBinding.generalDiscussionNested.visibility =
                    View.GONE
                loadGeneralDiscussionList(limit, listOf("interview-experience"))
            }
        }
        fragmentGeneralDiscussionBinding.compensationLayout.setOnClickListener {
            if (selectedCategory != 3) {
                selectedCategory = 3
                resetCategory()
                fragmentGeneralDiscussionBinding.compensationLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentGeneralDiscussionBinding.generalDiscussionNested.visibility =
                    View.GONE
                loadGeneralDiscussionList(limit, listOf("compensation"))
            }
        }
        fragmentGeneralDiscussionBinding.careerLayout.setOnClickListener {
            if (selectedCategory != 4) {
                selectedCategory = 4
                resetCategory()
                fragmentGeneralDiscussionBinding.careerLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentGeneralDiscussionBinding.generalDiscussionNested.visibility =
                    View.GONE
                loadGeneralDiscussionList(limit, listOf("career"))
            }
        }
        fragmentGeneralDiscussionBinding.studyGuideLayout.setOnClickListener {
            if (selectedCategory != 5) {
                selectedCategory = 5
                resetCategory()
                fragmentGeneralDiscussionBinding.studyGuideLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentGeneralDiscussionBinding.generalDiscussionNested.visibility =
                    View.GONE
                loadGeneralDiscussionList(limit, listOf("study-guide"))
            }
        }
        fragmentGeneralDiscussionBinding.generalDiscussionLayout.setOnClickListener {
            if (selectedCategory != 6) {
                selectedCategory = 6
                resetCategory()
                fragmentGeneralDiscussionBinding.generalDiscussionLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentGeneralDiscussionBinding.generalDiscussionNested.visibility =
                    View.GONE
                loadGeneralDiscussionList(limit, listOf("general-discussion"))
            }
        }
        fragmentGeneralDiscussionBinding.supportAndFeedbackLayout.setOnClickListener {
            if (selectedCategory != 7) {
                selectedCategory = 7
                resetCategory()
                fragmentGeneralDiscussionBinding.supportAndFeedbackLayout.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.light_grey
                    )
                )
                loadingView.visibility = View.VISIBLE
                fragmentGeneralDiscussionBinding.generalDiscussionNested.visibility =
                    View.GONE
                loadGeneralDiscussionList(limit, listOf("feedback"))
            }
        }
    }

    private fun checkIfEmpty() {
        if (generalDiscussionAdapter!!.getDataItemCount() == 0) {
            fragmentGeneralDiscussionBinding.discussionListProgressBar.visibility =
                View.VISIBLE
        } else {
            fragmentGeneralDiscussionBinding.discussionListProgressBar.visibility =
                View.GONE
        }
    }

    override fun onItemClick(position: Int, discussionId: Int?) {
        val bundle = bundleOf(
            "discussionId" to discussionId,
        )
        fragmentGeneralDiscussionBinding.root.findNavController()
            .navigate(
                R.id.action_generalDiscussionFragment_to_generalDiscussionItemFragment,
                bundle
            )
    }
}