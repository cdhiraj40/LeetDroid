package com.example.leetdroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.leetdroid.R
import com.example.leetdroid.model.TrendingDiscussionModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TrendingDiscussionAdapter(val context: Context) :
    RecyclerView.Adapter<TrendingDiscussionAdapter.ViewHolder>() {

    private var trendingDiscussionList = TrendingDiscussionModel()
    private lateinit var trendingDiscussions: List<TrendingDiscussionModel.DataNode.CachedTrendingCategoryTopics>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trending_discussion_item, parent, false)

        return ViewHolder(view)
    }

    fun setData(trendingDiscussionsModel: TrendingDiscussionModel) {
        trendingDiscussionList = trendingDiscussionsModel
        trendingDiscussions = trendingDiscussionsModel.data?.cachedTrendingCategoryTopics!!
    }

    private var onClick: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClick(
            position: Int
        )
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val trendingDiscussionItem = trendingDiscussions[position]

        // sets the title of a question to the textView from our itemHolder class
        holder.trendingDiscussionTitle.text =
            trendingDiscussionItem.title

        holder.trendingDiscussionSubTitle.text = trendingDiscussionItem.post?.contentPreview

        if (trendingDiscussionItem.post?.author != null) {
            holder.trendingDiscussionAuthor.text =
                "By ".plus(trendingDiscussionItem.post.author.username)

            Glide.with(context)
                .load(
                    trendingDiscussionItem.post.author.profile?.userAvatar
                )
                .circleCrop()
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .into(holder.trendingDiscussionAuthorAvatar)
        } else {
            holder.trendingDiscussionAuthor.text = "By An anonymous user"
        }
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val time = format.format(trendingDiscussionItem.post?.creationDate?.times(1000L))
        var date: Date? = null
        try {
            date = format.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val dateString = String.format(Locale.ENGLISH, "%tB %<te,  %<tY", date)
        holder.trendingDiscussionCreationDate.text = dateString

        holder.itemView.setOnClickListener {
            onClick!!.onItemClick(
                position
            )
        }

    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return trendingDiscussions.size
    }

    fun getDataItemCount(): Int {
        return trendingDiscussions.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val trendingDiscussionTitle: TextView = itemView.findViewById(R.id.general_discussion_title)
        val trendingDiscussionSubTitle: TextView =
            itemView.findViewById(R.id.general_discussion_sub_title)
        val trendingDiscussionAuthor: TextView =
            itemView.findViewById(R.id.general_discussion_author)
        val trendingDiscussionAuthorAvatar: ImageView =
            itemView.findViewById(R.id.discussion_user_avatar)


        val trendingDiscussionCreationDate: TextView =
            itemView.findViewById(R.id.general_discussion_creation_date)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }
}