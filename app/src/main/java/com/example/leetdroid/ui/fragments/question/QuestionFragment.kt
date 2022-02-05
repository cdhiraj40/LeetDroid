package com.example.leetdroid.ui.fragments.question

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.leetdroid.R
import com.example.leetdroid.api.LeetCodeRequests
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentQuestionBinding
import com.example.leetdroid.model.QuestionContentModel
import com.example.leetdroid.sharedViewModel.QuestionSharedViewModel
import com.example.leetdroid.utils.JsonUtils
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import ru.noties.markwon.Markwon
import java.io.IOException


class QuestionFragment : Fragment() {

    private lateinit var fragmentQuestionBinding: FragmentQuestionBinding
    private lateinit var questionContentJson: QuestionContentModel
    private lateinit var questionTitleSlug: String
    private lateinit var questionID: String
    private var questionHasSolution: Boolean? = false
    private lateinit var questionSharedViewModel: QuestionSharedViewModel
    private lateinit var paidQuestionView: View
    private var markwon: Markwon? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentQuestionBinding = FragmentQuestionBinding.inflate(layoutInflater)
        val rootView = fragmentQuestionBinding.root

        questionSharedViewModel =
            ViewModelProvider(requireActivity())[QuestionSharedViewModel::class.java]

        // get the data = questionTitleSlug from all questions list
        val bundle = arguments
        questionTitleSlug = bundle?.getString("questionTitleSlug").toString()
        questionHasSolution = bundle?.getBoolean("questionHasSolution")
        questionID = bundle?.getString("questionID").toString()

        // add title slug and hasSolution in sharedViewModel
        questionSharedViewModel.getQuestionTitleSlug(questionTitleSlug)
        questionSharedViewModel.getQuestionHasSolution(questionHasSolution!!)
        questionSharedViewModel.getQuestionId(questionID)

        paidQuestionView = rootView.findViewById(R.id.view_paid_question)

        markwon = Markwon.create(requireContext())
        loadQuestion()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(requireView(), savedInstanceState)

        fragmentQuestionBinding.questionBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.solutionFragment -> {
                    fragmentQuestionBinding.root.findNavController()
                        .navigate(R.id.action_questionsFragment_to_questionSolutionFragment)
                }
                R.id.questionDiscussionFragment -> {
                    fragmentQuestionBinding.root.findNavController()
                        .navigate(R.id.action_questionsFragment_to_questionDiscussionFragment)
                }
            }
            true
        }
    }

    private fun loadQuestion() {
        val okHttpClient = OkHttpClient()
        val postBody: String =
            Gson().toJson(LeetCodeRequests.Helper.getQuestionContent(questionTitleSlug))
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

                questionContentJson = JsonUtils.generateObjectFromJson(
                    response.body!!.string(),
                    QuestionContentModel::class.java
                )

                if (questionContentJson.data?.question?.solution == null) {
                    questionSharedViewModel.getQuestionHasSolution(false)
                } else {
                    questionSharedViewModel.getQuestionHasSolution(questionContentJson.data?.question?.solution?.canSeeDetail!!)
                }
                if (questionContentJson.data?.question?.isPaidOnly!!) {
                    questionSharedViewModel.isQuestionPaid(true)
                } else {
                    questionSharedViewModel.isQuestionPaid(false)
                }

                questionSharedViewModel.getQuestionId(questionContentJson.data?.question?.questionFrontendId.toString())
                activity?.runOnUiThread {
                    if (questionContentJson.data?.question?.isPaidOnly!!)
                        paidQuestionView.visibility = View.VISIBLE
                    else {
                        var questionContentHtml = context?.getString(
                            R.string.question_content,
                            questionContentJson.data?.question?.content
                        )
                        questionContentHtml =
                            questionContentHtml!!.replace("<p>", "").replace("</p>", "")
                                .replace("</pre>", "")
                                .replace("<pre>", "")

                        val firstExample =
                            findSubstringIndex(questionContentHtml, "Example 1", 0)

                        val constraintsBlock =
                            findSubstringIndex(
                                questionContentHtml,
                                "Constraints",
                                firstExample
                            )
                        val check = questionContentHtml.substring(constraintsBlock).trim()

                        var string: String = markwon?.toMarkdown(
                            questionContentHtml
                        ).toString()

                        val example1 =
                            findSubstringIndex(string, "Example 1", 0)

                        val constraints =
                            findSubstringIndex(
                                string,
                                "Constraints",
                                example1
                            )

                        var questionDescription =
                            string.substring(0, example1)

                        questionDescription = questionDescription.replace("\n", "<br/>")


                        var exampleStrings =
                            string.substring(example1, constraints).trim()

                        val constraintStrings =
                            string.substring(constraints).trim()

                        val firstOccurance = exampleStrings.indexOf("Example") + 7

                        exampleStrings = exampleStrings.substring(firstOccurance)
                        exampleStrings = exampleStrings.substring(1).replace(
                            "Example",
                            "<br/><br/><b>Example</b>"
                        )
                        exampleStrings = exampleStrings.replace("Input", "Input:")
                        exampleStrings = exampleStrings.replace("Input::", "Input:")
                        exampleStrings = exampleStrings.replace("Input:", "<br/><b>Input:</b>")
                        exampleStrings = exampleStrings.replace("Output", "Output:")
                        exampleStrings = exampleStrings.replace("Output::", "Output:")
                        exampleStrings = exampleStrings.replace("Output:", "<br/><b>Output:</b>")
                        exampleStrings = exampleStrings.replace("Explanation", "Explanation:")
                        exampleStrings = exampleStrings.replace("Explanation::", "Explanation:")
                        exampleStrings =
                            exampleStrings.replace("Explanation:", "<br/><b>Explanation:</b>")


                        exampleStrings = exampleStrings.replace(":", ":<br/>")
                        exampleStrings = exampleStrings.replace("\n", "")
                        exampleStrings = exampleStrings.replace("[]", "[ ]")
                        fragmentQuestionBinding.questionDescriptionText.setHtml(
                            questionDescription.trim()
                        )

                        fragmentQuestionBinding.questionExamplesText.visibility = View.VISIBLE


                        fragmentQuestionBinding.questionExamplesText.setHtml(
                            "<b>Example </b>".plus(
                                exampleStrings.trim()
                            )
                        )

                        fragmentQuestionBinding.questionConstraintsText.setHtml(
                            check.trim()
                        )
                    }
                }
            }
        })
    }

    // find example1/Constraints in question content to change background of it for better view
    fun findSubstringIndex(inputString: String, whatToFind: String, startIndex: Int = 0): Int {
        val matchIndex = inputString.indexOf(whatToFind, startIndex)
        return if (matchIndex >= 0) matchIndex else 0
    }

    fun countMatches(string: String, subString: String): Int {
        return string.split(subString)
            .dropLastWhile { it.isEmpty() }
            .toTypedArray().size - 1
    }

    fun insertString(
        originalString: String,
        stringToBeInserted: String,
        index: Int
    ): String {
        // return the modified String
        return (originalString.substring(0, index + 1)
                + stringToBeInserted
                + originalString.substring(index + 1))
    }

    object Constant {
        val TAG = QuestionFragment::class.qualifiedName
    }
}