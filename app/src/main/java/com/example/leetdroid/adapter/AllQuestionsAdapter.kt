package com.example.leetdroid.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.leetdroid.R
import com.example.leetdroid.extensions.showSnackBar
import com.example.leetdroid.model.AllQuestionsModel
import com.example.leetdroid.model.AllQuestionsModel.DataNode.ProblemSetQuestionListNode.Questions
import com.example.leetdroid.utils.CommonFunctions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class AllQuestionsAdapter(val context: Context, val activity: Activity) :
    RecyclerView.Adapter<AllQuestionsAdapter.ViewHolder>() {

    private var questionList = AllQuestionsModel()
    private lateinit var questions: List<Questions>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_questions_item, parent, false)

        return ViewHolder(view)
    }

    fun setData(questionsList: AllQuestionsModel) {
        questionList = questionsList
        questions = questionList.data?.problemsetQuestionList?.questions!!
    }

    private var onClick: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClick(
            position: Int,
            questionTitleSlug: String?,
            questionHasSolution: Boolean?,
            questionID: String?
        )
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val questionItem = questions[position]

        // sets the title of a question to the textView from our itemHolder class
        holder.questionTitle.text =
            questionItem.title

        holder.questionAcceptanceRate.text = HtmlCompat.fromHtml(
            context.getString(
                R.string.acceptance_rate,
                "<b>${CommonFunctions.Round.roundDouble(questionItem.acRate!!, 2)}</b"
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        when (questionItem.hasSolution) {
            true -> holder.questionSolution.visibility = View.VISIBLE
            else -> {
                holder.questionSolution.visibility = View.GONE
            }
        }

        when (questionItem.paidOnly) {
            true -> {
                holder.questionLayout.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.paid_question
                    )
                )
                holder.questionPaidStatus.text = context.getString(R.string.question_paid_status)
            }
            else -> {
            }
        }

        // setting the text and color of difficulty button
        when (questionItem.difficulty) {
            "Easy" -> {
                holder.questionDifficulty.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.easy_difficulty
                    )
                )
                holder.questionDifficulty.text = questionItem.difficulty
            }
            "Medium" -> {
                holder.questionDifficulty.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.medium_difficulty
                    )
                )
                holder.questionDifficulty.text = questionItem.difficulty
            }
            "Hard" -> {
                holder.questionDifficulty.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.hard_difficulty
                    )
                )
                holder.questionDifficulty.text = questionItem.difficulty
            }
        }

        val questionTitleSlug = questionItem.titleSlug
        val questionHasSolution = questionItem.hasSolution
        val questionId = questionItem.frontendQuestionId
        holder.itemView.setOnClickListener {
            if (!questionItem.paidOnly!!) {
                onClick!!.onItemClick(
                    position,
                    questionTitleSlug,
                    questionHasSolution,
                    questionId
                )
            } else {
                showSnackBar(activity, context.getString(R.string.paid_question))
            }
        }
    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return questions.size
    }

    fun getDataItemCount(): Int {
        return questions.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val questionLayout: RelativeLayout = itemView.findViewById(R.id.question_item_layout)
        val questionTitle: TextView = itemView.findViewById(R.id.question_title)
        val questionAcceptanceRate: TextView = itemView.findViewById(R.id.question_acceptance_rate)
        val questionPaidStatus: TextView = itemView.findViewById(R.id.question_paid_status)
        val questionSolution: ImageView = itemView.findViewById(R.id.question_solution)
        val questionDifficulty: ExtendedFloatingActionButton =
            itemView.findViewById(R.id.question_difficulty)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }
}

