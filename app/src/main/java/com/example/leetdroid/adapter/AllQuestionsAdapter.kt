package com.example.leetdroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leetdroid.R
import com.example.leetdroid.model.AllQuestionsModel
import com.example.leetdroid.model.AllQuestionsModel.DataNode.ProblemSetQuestionListNode.Questions

class AllQuestionsAdapter : RecyclerView.Adapter<AllQuestionsAdapter.ViewHolder>() {

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

        val questionTitleSlug = questionItem.titleSlug
        val questionHasSolution = questionItem.hasSolution
        val questionId = questionItem.frontendQuestionId
        holder.itemView.setOnClickListener {
            onClick!!.onItemClick(
                position,
                questionTitleSlug,
                questionHasSolution,
                questionId
            )
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
        val questionTitle: TextView = itemView.findViewById(R.id.all_questions_item_title)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

}

