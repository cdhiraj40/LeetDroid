package com.example.leetdroid.utils.dialog

import android.view.View

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
//  object DeleteTodo : AppDialogs(
//    R.string.delete_todo_title,
//    R.string.delete_todo_message,
//    android.R.string.yes,
//    android.R.string.cancel,
//    true,
//    R.drawable.ic_baseline_delete_24
//  )
//
//    class FmcFilePath(private val customGetView: (() -> View)?) : AppDialogs(
//    R.string.fmc_file_path_dialog_title,
//    null,
//    R.string.save,
//    null,
//    false,
//    R.drawable.ic_baseline_save_24,
//    getView = customGetView
//  )
  interface HasBodyFormatArgs {
    val args: List<Any>
  }
}
