package com.cdhiraj40.leetdroid.utils.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent

import android.widget.Toast

fun Context?.toast(
    stringId: Int,
    length: Int = Toast.LENGTH_LONG
) {
    this?.let {
        Toast.makeText(this, stringId, length)
            .show()
    }
}

fun Context?.toast(
    text: String,
    length: Int = Toast.LENGTH_LONG
) {
    this?.let {
        Toast.makeText(this, text, length)
            .show()
    }
}

fun <T> Context.openActivity(it: Class<T>) {
    val intent = Intent(this, it)
    startActivity(intent)
}

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

