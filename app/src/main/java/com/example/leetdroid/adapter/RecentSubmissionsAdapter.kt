package com.example.leetdroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.leetdroid.R
import com.example.leetdroid.model.RecentSubmissionsModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RecentSubmissionsAdapter(val context: Context) :
    RecyclerView.Adapter<RecentSubmissionsAdapter.ViewHolder>() {

    private var submissionList = RecentSubmissionsModel()
    private lateinit var submissions: List<RecentSubmissionsModel.DataNode.RecentSubmissionListNode>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recent_submissions_item, parent, false)

        return ViewHolder(view)
    }

    fun setData(submissionsList: RecentSubmissionsModel) {
        submissionList = submissionsList
        submissions = submissionList.data?.recentSubmissionList!!
    }

    private var onClick: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClick(
            position: Int,
            questionTitleSlug: String?
        )
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val submissionItem = submissions[position]

        // sets the title of a question to the textView from our itemHolder class
        holder.submissionTitle.text =
            submissionItem.title


        // setting the text and color of status layout
        when (submissionItem.statusDisplay) {
            "Accepted" -> {
                holder.submissionStatus.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.accepted_submission
                    )
                )
                holder.submissionStatus.text = submissionItem.statusDisplay
            }
            else -> {
                holder.submissionStatus.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.wrong_submission
                    )
                )
                holder.submissionStatus.text = submissionItem.statusDisplay
            }
        }

        // setting thw date
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val time = format.format(submissionItem.timestamp!!.toLong().times(1000L))
        var date: Date? = null
        try {
            date = format.parse(time)
            holder.submissionDate.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // setting the color of language layout
        holder.submissionLanguage.setBackgroundColor(
            ContextCompat.getColor(
                context,
                R.color.alice_blue
            )
        )
        // setting the text and color of language button
        when (submissionItem.lang.toString()) {
            "cpp" -> {
                holder.submissionLanguage.text = "C++"
            }
            "java" -> {
                holder.submissionLanguage.text = context.getString(R.string.java)
            }
            "python" -> {
                holder.submissionLanguage.text = context.getString(R.string.python)
            }
            "python3" -> {
                holder.submissionLanguage.text = context.getString(R.string.python3)
            }
            "mysql" -> {
                holder.submissionLanguage.text = context.getString(R.string.mysql)
            }
            "mssql" -> {
                holder.submissionLanguage.text = context.getString(R.string.ms_sql_server)
            }
            "oraclesql" -> {
                holder.submissionLanguage.text = context.getString(R.string.oracle)
            }
            "c" -> {
                holder.submissionLanguage.text = "C"
            }
            "csharp" -> {
                holder.submissionLanguage.text = "C#"
            }

            "javascript" -> {
                holder.submissionLanguage.text = context.getString(R.string.javascript)
            }
            "ruby" -> {
                holder.submissionLanguage.text = context.getString(R.string.ruby)
            }
            "bash" -> {
                holder.submissionLanguage.text = context.getString(R.string.bash)
            }
            "swift" -> {
                holder.submissionLanguage.text = context.getString(R.string.swift)
            }
            "golang" -> {
                holder.submissionLanguage.text = context.getString(R.string.go)
            }
            "scala" -> {
                holder.submissionLanguage.text = context.getString(R.string.scala)
            }
            "html" -> {
                holder.submissionLanguage.text = context.getString(R.string.html)
            }
            "pythonml" -> {
                holder.submissionLanguage.text = context.getString(R.string.python_ml)
            }
            "kotlin" -> {
                holder.submissionLanguage.text = context.getString(R.string.kotlin)
            }
            "rust" -> {
                holder.submissionLanguage.text = context.getString(R.string.rust)
            }
            "php" -> {
                holder.submissionLanguage.text = context.getString(R.string.php)
            }
            "typescript" -> {
                holder.submissionLanguage.text = context.getString(R.string.typscript)
            }
            "racket" -> {
                holder.submissionLanguage.text = context.getString(R.string.rocket)
            }
            "erlang" -> {
                holder.submissionLanguage.text = context.getString(R.string.erlang)
            }
            "elixir" -> {
                holder.submissionLanguage.text = context.getString(R.string.elixir)
            }
        }

        val questionTitleSlug = submissionItem.titleSlug
        holder.itemView.setOnClickListener {
            onClick!!.onItemClick(
                position,
                questionTitleSlug
            )
        }
    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return submissions.size
    }

    fun getDataItemCount(): Int {
        return submissions.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val submissionTitle: TextView = itemView.findViewById(R.id.submission_title)
        val submissionDate: TextView = itemView.findViewById(R.id.submission_date)
        val submissionLanguage: ExtendedFloatingActionButton =
            itemView.findViewById(R.id.submission_language)
        val submissionStatus: ExtendedFloatingActionButton =
            itemView.findViewById(R.id.submission_status)

    }

    fun setOnClick(onClick: OnItemClicked?) {
        this.onClick = onClick
    }
}

