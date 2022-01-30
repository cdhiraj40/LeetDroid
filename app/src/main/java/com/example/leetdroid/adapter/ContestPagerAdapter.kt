package com.example.leetdroid.adapter

import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.leetdroid.R
import com.example.leetdroid.data.entitiy.Contest
import com.example.leetdroid.model.ContestsModel
import com.example.leetdroid.utils.DateUtils.getDate
import com.example.leetdroid.utils.DateUtils.getHours
import com.example.leetdroid.utils.DateUtils.getTime
import com.example.leetdroid.utils.DateUtils.parseISO8601Date
import com.google.android.material.button.MaterialButton
import java.util.*
import kotlin.collections.ArrayList

class ContestPagerAdapter
    (private val contests: ArrayList<Contest>, private val context: Context) :
    RecyclerView.Adapter<ContestPagerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.contest_pager_item, parent, false)
        return ViewHolder(view)
    }

    private var onClick: OnItemClicked? = null

    interface OnItemClicked {
        fun onItemClick(
            position: Int,
            contestTitle: String?,
            contestStartTime: String?,
            contestEndTime: String?
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val contest = contests[position]

        holder.contestTitle.text = contest.name
        val startingDate = parseISO8601Date(contest.start_time)
        val endingDate = parseISO8601Date(contest.end_time)

        holder.contestDate.text = getDate(startingDate)
        holder.contestDuration.text = getHours(contest.duration).plus(" Hrs")
        holder.contestStartTime.text = getTime(startingDate)
        holder.contestEndTime.text = getTime(endingDate)
        holder.contestRunningText.text = contest.in_24_hours

        // contest timer
        val currentTime = Calendar.getInstance().time
        val different = endingDate.time - currentTime.time
        val countDownTimer: CountDownTimer = object : CountDownTimer(different, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = diff / daysInMilli
                diff %= daysInMilli

                val elapsedHours = diff / hoursInMilli
                diff %= hoursInMilli

                val elapsedMinutes = diff / minutesInMilli
                diff %= minutesInMilli

                val elapsedSeconds = diff / secondsInMilli

                holder.contestRunningText.text =
                    "$elapsedDays days $elapsedHours hs $elapsedMinutes min $elapsedSeconds sec"
            }

            override fun onFinish() {
                holder.contestRunningText.text = "done!"
            }
        }.start()

        holder.contestCalendarButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra("beginTime", startingDate.time)
            intent.putExtra("allDay", false)
            intent.putExtra("endTime", endingDate.time)
            intent.putExtra("title", contest.name)
            intent.putExtra("rrule", "FREQ=NO")
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return contests.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val contestTitle: TextView = itemView.findViewById(R.id.contest_title)
        val contestDate: TextView =
            itemView.findViewById(R.id.contest_date)
        val contestDuration: TextView = itemView.findViewById(R.id.contest_duration)
        val contestStartTime: TextView = itemView.findViewById(R.id.contest_start_time)
        val contestEndTime: TextView = itemView.findViewById(R.id.contest_end_time)
        val contestRunningText: TextView =
            itemView.findViewById(R.id.contest_running_text)
        val contestCalendarButton: MaterialButton =
            itemView.findViewById(R.id.contest_calendar_button)
    }

}