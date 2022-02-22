package com.example.leetdroid.utils.dialog

import android.view.View
import com.example.leetdroid.R

sealed class AppDialogs(
    val title: Int?,
    val message: Int?,
    val positiveMessage: Int,
    val negativeMessage: Int? = null,
    val cancelable: Boolean = true,
    val icon: Int? = null,
    val neutralMessage: Int? = null,
    val getView: (() -> View)? = null
) {
    object Logout : AppDialogs(
        title = R.string.logout_dialog_title,
        message = R.string.logout_dialog_message,
        positiveMessage = R.string.yes,
        negativeMessage = android.R.string.cancel,
        cancelable = true,
        icon = R.drawable.ic_baseline_logout_24
    )

    object SyncData : AppDialogs(
        title = R.string.sync_data,
        message = R.string.sync_data_dialog_message,
        positiveMessage = R.string.yes,
        negativeMessage = android.R.string.cancel,
        cancelable = true,
        icon = R.drawable.ic_baseline_sync_24
    )

    object UsernameWarning : AppDialogs(
        title = R.string.warning,
        message = R.string.username_warning_message,
        positiveMessage = R.string.username_warning_positive_message,
        negativeMessage = R.string.username_warning_negative_message,
        cancelable = true,
        icon = R.drawable.ic_baseline_warning_24
    )

    object PremiumList : AppDialogs(
        title = R.string.warning,
        message = R.string.premium_list_message,
        positiveMessage = R.string.premium_list_positive_message,
        negativeMessage = android.R.string.cancel,
        cancelable = true,
        icon = R.drawable.ic_baseline_warning_24
    )

    object ReportBug : AppDialogs(
        title = R.string.report_bug,
        message = R.string.report_bug_message,
        positiveMessage = R.string.report_bug_positive_message,
        neutralMessage = R.string.report_bug_neutral_message,
        cancelable = true,
        icon = R.drawable.ic_baseline_bug_report_24
    )


    interface HasBodyFormatArgs {
        val args: List<Any>
    }
}
