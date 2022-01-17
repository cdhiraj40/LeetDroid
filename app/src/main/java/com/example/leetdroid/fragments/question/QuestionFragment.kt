package com.example.leetdroid.fragments.question

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.leetdroid.R

import com.example.leetdroid.api.GraphQl
import com.example.leetdroid.api.URL
import com.example.leetdroid.databinding.FragmentQuestionBinding
import com.example.leetdroid.model.QuestionContentModel

import com.example.leetdroid.utils.JsonUtils

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class QuestionFragment : Fragment() {

    private lateinit var fragmentQuestionBinding: FragmentQuestionBinding
    private lateinit var questionContentJson: QuestionContentModel
    private lateinit var questionTitleSlug: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentQuestionBinding = FragmentQuestionBinding.inflate(layoutInflater)
        val rootView = fragmentQuestionBinding.root

        // get the data = questionTitleSlug from all questions list
        val bundle = arguments
        questionTitleSlug = bundle?.getString("questionTitleSlug").toString()

        loadQuestion()
        return rootView
    }

    private fun loadQuestion() {
        val okHttpClient = OkHttpClient()
        val postBody = java.lang.String.format(GraphQl.QUESTION_ITEM_DATA, questionTitleSlug)
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
                activity?.runOnUiThread {
                    val questionContentHtml = context?.getString(
                        R.string.question_content,
                        questionContentJson.data?.question?.content
                    )
                    val questionContent = HtmlCompat.fromHtml(
                        questionContentHtml.toString(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )


                    val example1 = findSubstringIndex(questionContent.toString(), "Example 1", 0)
                    val constraints =
                        findSubstringIndex(questionContent.toString(), "Constraints", example1)

                    val questionDescription = questionContent.substring(0, example1).trim()
                    val exampleStrings = questionContent.substring(example1, constraints).trim()
                    val constraintStrings = questionContent.substring(constraints).trim()

//                    val explanationCount = countMatches(exampleStrings, "Explanation")
//                    var index = 0
//                    for (i in 0..explanationCount) {
//                        index = findSubstringIndex(exampleStrings, "Explanation", index)
//                        insertString(exampleStrings, "\n ", index - 1)
//                    }

                    fragmentQuestionBinding.questionDescriptionText.text =
                        questionDescription

                    fragmentQuestionBinding.questionExamplesText.visibility = View.VISIBLE
                    fragmentQuestionBinding.questionExamplesText.text =
                        exampleStrings

                    fragmentQuestionBinding.questionConstraintsText.text =
                        constraintStrings

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

        // Create a new string

        // return the modified String
        return (originalString.substring(0, index + 1)
                + stringToBeInserted
                + originalString.substring(index + 1))
    }

    object Constant {
        const val TAG = "QuestionFragment"
    }
}