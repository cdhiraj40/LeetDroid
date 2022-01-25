package com.example.leetdroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.leetdroid.R

import com.example.leetdroid.model.QuestionDiscussionsModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class QuestionDiscussionAdapter(
    private val questionDiscussionsList: QuestionDiscussionsModel, private val context: Context
) : RecyclerView.Adapter<QuestionDiscussionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.question_discussion_item, parent, false)

        return ViewHolder(view)
    }

    private var onClick: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClick(position: Int, discussionId: Int?)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val question = questionDiscussionsList.data?.questionTopicsList?.edges?.get(position)?.node


        holder.questionDiscussionTitle.text =
            question?.title

        Glide.with(context)
            .load(
                question?.post?.author?.profile?.userAvatar
            )
            .circleCrop()
            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
            .into(holder.discussionUserAvatar)

        val viewCount = question?.viewCount
        if (viewCount!! >= 1000) {
            holder.questionDiscussionViews.text =
                """${(viewCount / 1000)}.${viewCount / 100 % 10}k"""
        } else {
            holder.questionDiscussionViews.text = viewCount.toString()
        }

        holder.questionDiscussionAuthor.text = question.post?.author?.username
        holder.questionDiscussionCommentCount.text = question.commentCount.toString()
        holder.questionDiscussionUpvotes.text = question.post?.voteCount.toString()

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val time = format.format(question.post?.creationDate?.times(1000L))
        var date: Date? = null
        try {
            date = format.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val dateString = String.format(Locale.ENGLISH, "%tB %<te,  %<tY", date)
        holder.questionDiscussionCreationDate.text = dateString

        val discussionId = question.post?.id
        holder.itemView.setOnClickListener {
            onClick!!.onItemClick(
                position,
                discussionId
            )
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return questionDiscussionsList.data?.questionTopicsList?.edges?.size!!
    }

    fun getDataItemCount(): Int {
        return questionDiscussionsList.data?.questionTopicsList?.edges?.size ?: 0
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val discussionUserAvatar: ImageView = itemView.findViewById(R.id.discussion_user_avatar)
        val questionDiscussionTitle: TextView =
            itemView.findViewById(R.id.question_discussion_title)
        val questionDiscussionViews: TextView = itemView.findViewById(R.id.discussion_views)
        val questionDiscussionUpvotes: TextView = itemView.findViewById(R.id.discussion_upvotes)
        val questionDiscussionAuthor: TextView =
            itemView.findViewById(R.id.question_discussion_author)
        val questionDiscussionCreationDate: TextView =
            itemView.findViewById(R.id.question_discussion_creation_date)
        val questionDiscussionCommentCount: TextView =
            itemView.findViewById(R.id.discussion_comment_count)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

}

