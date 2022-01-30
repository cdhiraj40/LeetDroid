package com.example.leetdroid.extensions

import android.app.Activity
import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

import com.google.android.material.snackbar.Snackbar
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import android.os.Environment
import android.widget.ProgressBar
import com.example.leetdroid.R


fun Fragment.closeKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun View.closeKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun showSnackBar(activity: Activity, message: String?) {
    val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
    val snackbar = Snackbar.make(rootView, message!!, Snackbar.LENGTH_SHORT)
    snackbar.anchorView = activity.findViewById(R.id.bottom_navigation)
    snackbar.show()
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

fun showSnackBarWithAction(
    activity: Activity,
    message: String?,
    @StringRes actionRes: String,
    color: Int? = null,
    listener: (View) -> Unit
) {
    val rootView = activity.window.decorView.findViewById<View>(android.R.id.content)
    val snackbar = Snackbar.make(rootView, message!!, Snackbar.LENGTH_SHORT)
    snackbar.setAction(actionRes, listener)
    snackbar.anchorView = activity.findViewById(R.id.bottom_navigation)
    snackbar.show()
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(ContextCompat.getColor(context, color)) }
}

fun <T> Context.openActivity(it: Class<T>) {
    val intent = Intent(this, it)
    startActivity(intent)
}

fun Fragment.downloadFileWifi(fileUrl: String, fileName: String) {
    val request = DownloadManager.Request(Uri.parse(fileUrl))
        .setTitle(context!!.getString(R.string.app_name))
        .setDescription("Downloading $fileName")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

    val downloadManager = context!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}

fun Fragment.downloadFile(fileUrl: String, fileName: String) {
    val request = DownloadManager.Request(Uri.parse(fileUrl))
        .setTitle(context!!.getString(R.string.app_name))
        .setDescription("Downloading $fileName")
        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

    val downloadManager = context!!.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    downloadManager.enqueue(request)
}

fun timer(progressBar: ProgressBar, time: Long) {
    object : CountDownTimer(time, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            progressBar.visibility = View.VISIBLE
        }

        override fun onFinish() {
            progressBar.visibility = View.GONE
        }
    }.start()
}

