package com.example.leetdroid.utils.dialog

import android.view.View
import com.example.leetdroid.R

sealed class AppDialogs(
    val title: Int?,
    val message: Int?,
    val positiveMessage: Int,
    val negativeMessage: Int?,
    val cancelable: Boolean = true,
    val icon: Int? = null,
    val neutralMessage: Int? = null,
    val getView: (() -> View)? = null
) {
    object LogOut : AppDialogs(
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

    interface HasBodyFormatArgs {
        val args: List<Any>
    }
}
