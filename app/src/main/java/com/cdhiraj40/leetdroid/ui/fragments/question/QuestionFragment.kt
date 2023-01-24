package com.cdhiraj40.leetdroid.ui.fragments.question

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.cdhiraj40.leetdroid.R
import com.cdhiraj40.leetdroid.api.LeetCodeRequests
import com.cdhiraj40.leetdroid.api.URL
import com.cdhiraj40.leetdroid.databinding.FragmentQuestionBinding
import com.cdhiraj40.leetdroid.model.QuestionContentModel
import com.cdhiraj40.leetdroid.sharedViewModel.QuestionSharedViewModel
import com.cdhiraj40.leetdroid.utils.Constant
import com.cdhiraj40.leetdroid.utils.JsonUtils
import com.cdhiraj40.leetdroid.utils.extensions.showSnackBar
import com.google.gson.Gson
import io.noties.markwon.Markwon
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.sufficientlysecure.htmltextview.HtmlTextView
import java.io.IOException


class QuestionFragment : Fragment() {

    private lateinit var fragmentQuestionBinding: FragmentQuestionBinding
    private lateinit var questionContentJson: QuestionContentModel
    private lateinit var questionTitleSlug: String
    private lateinit var questionID: String
    private var questionHasSolution: Boolean? = false
    private lateinit var questionSharedViewModel: QuestionSharedViewModel
    private lateinit var paidQuestionView: View
    private lateinit var loadingView: View

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

        loadingView = rootView.findViewById(R.id.loading_view)

        loadingView.visibility = View.VISIBLE
        fragmentQuestionBinding.questionLayout.visibility = View.GONE

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

        fragmentQuestionBinding.questionSolutionLayout.setOnClickListener {
            fragmentQuestionBinding.root.findNavController()
                .navigate(R.id.action_questionsFragment_to_questionSolutionFragment)
        }

        fragmentQuestionBinding.questionDiscussionLayout.setOnClickListener {
            fragmentQuestionBinding.root.findNavController()
                .navigate(R.id.action_questionsFragment_to_questionDiscussionFragment)
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
                Log.d(Constant.TAG(QuestionFragment::class.java).toString(), call.toString(), e)
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
                    try {
                        if (questionContentJson.data?.question?.isPaidOnly!!) {
                            paidQuestionView.visibility = View.VISIBLE
                            loadingView.visibility = View.GONE
                        } else {

                            (requireActivity() as AppCompatActivity).supportActionBar?.title =
                                questionContentJson.data?.question?.title

                            val questionContentHtml = context?.getString(
                                R.string.question_content,
                                questionContentJson.data?.question?.content
                            )

                            // adding title to sharedViewModel
                            questionSharedViewModel.getQuestionTitle(questionContentJson.data?.question?.title.toString())
                            displayQuestion(questionContentHtml)
                        }
                    } catch (exception: Exception) {
                        // TODO add the general error view and show here
                        showSnackBar(requireActivity(), "Something went wrong")
                        Log.d(
                            Constant.TAG(QuestionFragment::class.java).toString(),
                            exception.toString()
                        )
                    }
                }
            }
        })
    }

    private fun displayQuestion(questionContent: String?) {
        // removing the tags not supported by markwon library
        val questionContentHtml =
            questionContent!!.replace("<p>", "").replace("</p>", "")
                .replace("</pre>", "")
                .replace("<pre>", "")

        // finding the index of first occurrence of example in html string, to find constraints text
        var firstExampleOccurrence =
            findSubstringIndex(
                inputString = questionContentHtml,
                whatToFind = "Example 1",
                startIndex = 0
            )

        // finding the boundaries of list in question description
        val descriptionListBeginning =
            findSubstringIndex(
                inputString = questionContentHtml,
                whatToFind = "<ul>",
                startIndex = 0
            )

        val descriptionListEnd =
            findSubstringIndex(
                inputString = questionContentHtml,
                whatToFind = "</ul>",
                startIndex = 0
            )+5

        var descriptionListBlock = ""

        // check if list is in the question description
        if (descriptionListBeginning < firstExampleOccurrence
            && descriptionListEnd < firstExampleOccurrence) {
            // getting the substring of list in question description
            descriptionListBlock = questionContentHtml.substring(descriptionListBeginning, descriptionListEnd).trim()
        }

        /**
         * finding the occurrence of constraints in html string for the separate constraints text,
         * after example block
         */
        var constraintsIndex =
            findSubstringIndex(
                inputString = questionContentHtml,
                whatToFind = "Constraints",
                startIndex = firstExampleOccurrence
            )

        // getting the substring of only constraints for the separate constraints text
        val constraintBlock =
            questionContentHtml.substring(constraintsIndex).trim()

        // converting html content to normal text with the help of markwon library
        val markwonedQuestionContent: String = markwon?.toMarkdown(
            questionContentHtml
        ).toString()

        // getting index of first Occurrence Example in normal text for separate example IO/OP block
        firstExampleOccurrence =
            findSubstringIndex(
                inputString = markwonedQuestionContent,
                whatToFind = "Example 1",
                startIndex = 0
            )

        // getting index of constraint in normal text for separate example IO/OP block
        constraintsIndex =
            findSubstringIndex(
                inputString = markwonedQuestionContent,
                whatToFind = "Constraints",
                startIndex = firstExampleOccurrence
            )

        // getting the substring of only first part of question, i.e. description, before example block
        var questionDescription =
            markwonedQuestionContent.substring(0, firstExampleOccurrence)

        /**
         * replacing ""\n"" with html's ""<br/>"" tag, because the content from API contains many consecutive ""\n"" that
         * gets removed after converting to normal text, most probably its a bug
         * more information here: https://stackoverflow.com/questions/27640401/remove-html-tags-from-a-string-but-restore-n
         * https://stackoverflow.com/questions/9326447/removing-html-tags-except-line-breaks
         */
        questionDescription = questionDescription.replace("\n", "<br/>").trim()

        // if there is a list in the question description, add it
        if (descriptionListBlock != "") {
            val br = questionDescription.substring(questionDescription.length-10,questionDescription.length)

            if (br == "<br/><br/>") {
                questionDescription = questionDescription.substring(0, questionDescription.length-10)
            }

            questionDescription = questionDescription.plus(descriptionListBlock)
        }

        // creating example block string from normal string for example block
        var exampleStrings =
            markwonedQuestionContent.substring(
                firstExampleOccurrence,
                constraintsIndex
            ).trim()


        /**
         * I am adding a line break after and before every example or more specifically after "Example no. :"
         * but adding before first example makes the block look little ugly.
         * To resolve this, we are adding 7 in first occurrence to skip first "Example" in string.
         */
        firstExampleOccurrence = exampleStrings.indexOf("Example") + 7

        exampleStrings = exampleStrings.substring(firstExampleOccurrence)

        // making every instance of example bold  and adding two line breaks before to make it look better
        exampleStrings = exampleStrings.substring(1).replace(
            "Example",
            "<br/><br/><b>Example</b>"
        )

        /**
         * Summary: making strings like Input, Output and Explanation bold and Adding line breaks before them.
         *
         * Long Explanation: Most of the cases the strings coming from API like Input, Output and Explanation had
         * ":" after them, but just to be safe we are taking care of both cases if there("Input:") and not there ("Input").
         * Lastly we are making them bold and adding line breaks after them to make the question look less compact.
         */
        exampleStrings = exampleStrings.replace("Input", "Input:")
        exampleStrings = exampleStrings.replace("Input::", "Input:")
        exampleStrings = exampleStrings.replace("Input:", "<br/><b>Input:</b>")
        exampleStrings = exampleStrings.replace("Output", "Output:")
        exampleStrings = exampleStrings.replace("Output::", "Output:")
        exampleStrings =
            exampleStrings.replace("Output:", "<br/><b>Output:</b>")
        exampleStrings = exampleStrings.replace("Explanation", "Explanation:")
        exampleStrings = exampleStrings.replace("Explanation::", "Explanation:")
        exampleStrings =
            exampleStrings.replace("Explanation:", "<br/><b>Explanation:</b>")

        exampleStrings = exampleStrings.replace(":", ":<br/>")

        /**
         * Extra information: removing more "\n" if there exists some with empty string as we are already adding line breaks,
         * empty lists "[]" were looking little compact so replaced by one extra space "[ ]"
         */
        exampleStrings = exampleStrings.replace("\n", "")
        exampleStrings = exampleStrings.replace("[]", "[ ]")
        fragmentQuestionBinding.questionDescriptionText.setHtmlFromString(
            questionDescription.trim(), HtmlTextView.LocalImageGetter()
        )

        /**
         * Making the example block with colored visible.
         * The reason it's visibility was gone because currently there is no loading screen,
         * and an empty colored block looks ugly.
         */
        fragmentQuestionBinding.questionExamplesText.visibility = View.VISIBLE


        /**
         * Adding and Making first Example string bold, which was removed to handle other cases mentioned above.
         */
        fragmentQuestionBinding.questionExamplesText.setHtmlFromString(
            "<b>Example </b>".plus(
                exampleStrings.trim()
            ), HtmlTextView.LocalImageGetter()
        )

        fragmentQuestionBinding.questionConstraintsText.setHtmlFromString(
            constraintBlock.trim(), HtmlTextView.LocalImageGetter()
        )

        loadingView.visibility = View.GONE
        fragmentQuestionBinding.questionLayout.visibility = View.VISIBLE
        fragmentQuestionBinding.questionBottomNavigation.visibility =
            View.VISIBLE
    }

    // find example1/Constraints in question content to change background of it for better view
    private fun findSubstringIndex(
        inputString: String,
        whatToFind: String,
        startIndex: Int = 0
    ): Int {
        val matchIndex = inputString.indexOf(whatToFind, startIndex)
        return if (matchIndex >= 0) matchIndex else 0
    }
}