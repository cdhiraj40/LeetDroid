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
import com.example.leetdroid.model.DiscussionItemModel
import com.example.leetdroid.model.GeneralDiscussionModel

import com.example.leetdroid.model.QuestionDiscussionsModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class GeneralDiscussionAdapter(
    private val generalDiscussionsList: GeneralDiscussionModel, private val context: Context
) : RecyclerView.Adapter<GeneralDiscussionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.general_discussion_item, parent, false)

        return ViewHolder(view)
    }

    private var onClick: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClick(position: Int, discussionId: Int?)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val discussionItem = generalDiscussionsList.data?.categoryTopicList?.edges?.get(position)?.node


        holder.generalDiscussionTitle.text =
            discussionItem?.title

        if( discussionItem?.post?.author?.profile?.userAvatar !=null) {
            Glide.with(context)
                .load(
                    discussionItem.post?.author?.profile?.userAvatar
                )
                .circleCrop()
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .into(holder.discussionUserAvatar)
        }else{
            holder.discussionUserAvatar.setImageResource(R.drawable.ic_round_person_24)
        }
        val viewCount = discussionItem?.viewCount
        if (viewCount!! >= 1000) {
            holder.generalDiscussionViews.text =
                """${(viewCount / 1000)}.${viewCount / 100 % 10}k"""
        } else {
            holder.generalDiscussionViews.text = viewCount.toString()
        }

        holder.generalDiscussionAuthor.text = discussionItem.post?.author?.username
        holder.generalDiscussionCommentCount.text = discussionItem.commentCount.toString()
        holder.generalDiscussionUpvotes.text = discussionItem.post?.voteCount.toString()

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val time = format.format(discussionItem.post?.creationDate?.times(1000L))
        var date: Date? = null
        try {
            date = format.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val dateString = String.format(Locale.ENGLISH, "%tB %<te,  %<tY", date)
        holder.generalDiscussionCreationDate.text = dateString

        val discussionId = discussionItem.id
        holder.itemView.setOnClickListener {
            onClick!!.onItemClick(
                position,
                discussionId
            )
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return generalDiscussionsList.data?.categoryTopicList?.edges?.size!!
    }

    fun getDataItemCount(): Int {
        return generalDiscussionsList.data?.categoryTopicList?.edges?.size ?: 0
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var discussionUserAvatar: ImageView = itemView.findViewById(R.id.discussion_user_avatar)
        val generalDiscussionTitle: TextView =
            itemView.findViewById(R.id.general_discussion_title)
        val generalDiscussionViews: TextView = itemView.findViewById(R.id.discussion_views)
        val generalDiscussionUpvotes: TextView = itemView.findViewById(R.id.discussion_upvotes)
        val generalDiscussionAuthor: TextView =
            itemView.findViewById(R.id.general_discussion_author)
        val generalDiscussionCreationDate: TextView =
            itemView.findViewById(R.id.general_discussion_creation_date)
        val generalDiscussionCommentCount: TextView =
            itemView.findViewById(R.id.discussion_comment_count)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }

}

