package com.example.leetdroid.utils.dialog

import android.app.Dialog
import com.example.leetdroid.utils.dialog.AppDialogs

interface DialogShower {
  fun show(
      dialog: AppDialogs,
      vararg clickListeners: (() -> Unit)
  )

  fun create(
      dialog: AppDialogs,
      vararg clickListeners: (() -> Unit)
  ): Dialog
}
