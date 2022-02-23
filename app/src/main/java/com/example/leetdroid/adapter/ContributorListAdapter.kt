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
import com.example.leetdroid.model.ContributorListModel
import java.util.*

class ContributorListAdapter(
    val context: Context,
    private val contributorClickInterface: ContributorClickInterface,
    private val allContributors: ArrayList<ContributorListModel.ContributorListModelItem>
) :
    RecyclerView.Adapter<ContributorListAdapter.ViewHolder>() {

    interface ContributorClickInterface {
        fun onContributorClick(contributor: ContributorListModel.ContributorListModelItem)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contributorAuthor: TextView = itemView.findViewById(R.id.contributor_username)
        val contributorAuthorAvatar: ImageView =
            itemView.findViewById(R.id.contributor_username_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.contibutors_item,
            parent, false
        )

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contributor = allContributors[position]
        holder.contributorAuthor.text = contributor.author?.login

        Glide.with(context)
            .load(contributor.author?.avatar_url)
            .circleCrop()
            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
            .into(holder.contributorAuthorAvatar)

        holder.itemView.setOnClickListener {
            contributorClickInterface.onContributorClick(contributor)
        }
    }

    override fun getItemCount(): Int {
        return allContributors.size
    }
}