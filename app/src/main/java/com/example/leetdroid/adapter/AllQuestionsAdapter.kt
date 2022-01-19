package com.example.leetdroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leetdroid.R
import com.example.leetdroid.model.AllQuestionsModel

class AllQuestionsAdapter(
    private val allQuestionsList: AllQuestionsModel
) : RecyclerView.Adapter<AllQuestionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_questions_item, parent, false)

        return ViewHolder(view)
    }

    private var onClick: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClick(position: Int, questionTitleSlug: String?, questionHasSolution: Boolean?, questionID:String?)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // sets the title of a question to the imageview from our itemHolder class
        val question = allQuestionsList.data?.problemsetQuestionList?.questions?.get(position)

        holder.questionTitle.text =
            question?.title

        val questionTitleSlug = question?.titleSlug
        val questionHasSolution = question?.hasSolution
        val questionId = question?.frontendQuestionId
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
        return allQuestionsList.data?.problemsetQuestionList?.questions?.size!!
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val questionTitle: TextView = itemView.findViewById(R.id.all_questions_item_title)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

}

