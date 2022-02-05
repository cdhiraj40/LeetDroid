package com.example.leetdroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leetdroid.R
import com.example.leetdroid.model.ContestRankingModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ContestHistoryAdapter(val context: Context) :
    RecyclerView.Adapter<ContestHistoryAdapter.ViewHolder>() {

    private var contestList = ContestRankingModel()
    private lateinit var contests: List<ContestRankingModel.DataNode.UserContestRankingHistoryNode>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contest_ranking_history_item, parent, false)

        return ViewHolder(view)
    }

    fun setData(contestsList: ContestRankingModel) {
        contestList = contestsList
        contests =
                // to get the latest contest
            contestsList.data?.userContestRankingHistory!!.sortedByDescending { it.contest?.startTime }
    }

    private var onClick: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClick(
            position: Int
        )
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contestItem = contests[position]

        // sets the title of a contest to the textView from our itemHolder class
        holder.contestTitle.text =
            contestItem.contest?.title

        if (contestItem.ranking!! <= 0L) {
            holder.contestRanking.text = context.getString(R.string.user_absent_contest)
        } else {
            holder.contestRanking.text = contestItem.ranking.toString()
        }


        holder.afterContestRating.text =
            contestItem.rating.toString()


        // setting thw date
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val time = format.format(contestItem.contest?.startTime!!.toLong().times(1000L))
        var date: Date? = null
        try {
            date = format.parse(time)
            holder.contestDate.text =
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        holder.itemView.setOnClickListener {
            onClick!!.onItemClick(
                position
            )
        }
    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return contests.size
    }

    fun getDataItemCount(): Int {
        return contests.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val contestTitle: TextView = itemView.findViewById(R.id.item_title)
        val contestRanking: TextView = itemView.findViewById(R.id.item_contest_ranking)
        val contestDate: TextView = itemView.findViewById(R.id.item_date)
        val afterContestRating: TextView = itemView.findViewById(R.id.item_after_rating)
    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }
}

