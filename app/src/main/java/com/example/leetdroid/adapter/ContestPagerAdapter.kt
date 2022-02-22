package com.example.leetdroid.adapter

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.leetdroid.R
import com.example.leetdroid.data.entitiy.Contest
import com.example.leetdroid.utils.DateUtils.getDate
import com.example.leetdroid.utils.DateUtils.getHours
import com.example.leetdroid.utils.DateUtils.getTime
import com.example.leetdroid.utils.DateUtils.parseISO8601Date
import com.example.leetdroid.utils.SharedPreferences
import com.example.leetdroid.utils.extensions.showSnackBar
import com.google.android.material.button.MaterialButton
import java.util.*

class ContestPagerAdapter
    (
    private val contests: ArrayList<Contest>,
    private val context: Context,
    private val activity: Activity
) :
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

//        if (contest.in_24_hours == "Yes" && !SharedPreferences(context).dayNotificationPushed) {
//            // sending notification a day before contest
//            SharedPreferences(context).dayNotificationPushed = true
//            Notification.showNotification(
//                context,
//                contest.name,
//                "There is only a day remaining to ${contest.name}\n register now!",
//                "contest_reminder_day",
//                "contest_reminder_day_channel",
//                101
//            )
//        }


        // showing timer before contest starts
        val currentTime = Calendar.getInstance().time
        val different = startingDate.time - currentTime.time

        object : CountDownTimer(different, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                var diff = millisUntilFinished

                // TODO some bug makes this notification push even if remaining time is more than 30 mins
                // sending notification 30 mins before
//                if (!SharedPreferences(context).minsNotificationPushed && (diff <= 1800000L || diff >= 0)) {
//                    SharedPreferences(context).minsNotificationPushed = true
//                    Notification.showNotification(
//                        context,
//                        contest.name,
//                        "There is only 30 mins remaining to ${contest.name}\n register now!",
//                        "contest_reminder_30Mins",
//                        "contest_reminder_30Mins_channel",
//                        102
//                    )
//                }

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
                SharedPreferences(context).timerEnded = true
                refreshCurrentFragment()
            }
        }.start()

        holder.contestCalendarButton.setOnClickListener()
        {
            // opening google calendar
            try {
                val intent = Intent(Intent.ACTION_EDIT)
                intent.type = "vnd.android.cursor.item/event"
                intent.putExtra("beginTime", startingDate.time)
                intent.putExtra("allDay", false)
                intent.putExtra("endTime", endingDate.time)
                intent.putExtra("title", contest.name)
                intent.putExtra("rrule", "FREQ=NO")
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                showSnackBar(activity, "There is no google account signed in with calendar")
            }
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

    private fun refreshCurrentFragment() {
        val navController: NavController =
            activity.findNavController(R.id.hostFragment)
        navController.run {
            popBackStack()
            navigate(R.id.homeFragment)
        }
    }
}