package com.example.leetdroid.utils.dialog

import android.app.Activity
import android.app.Dialog
import androidx.appcompat.app.AlertDialog

class AlertDialogShower constructor(private val activity: Activity) :
    DialogShower {
    override fun show(dialog: AppDialogs, vararg clickListeners: () -> Unit) {
        create(dialog, *clickListeners).show()
    }

    override fun create(dialog: AppDialogs, vararg clickListeners: () -> Unit): Dialog {
        return AlertDialog.Builder(activity)
            .apply {
                dialog.title?.let(this::setTitle)
                dialog.icon?.let(this::setIcon)
                dialog.message?.let { setMessage(activity.getString(it, *bodyArguments(dialog))) }
                setPositiveButton(dialog.positiveMessage) { _, _ ->
                    clickListeners.getOrNull(0)
                        ?.invoke()
                }
                dialog.negativeMessage?.let {
                    setNegativeButton(it) { _, _ ->
                        clickListeners.getOrNull(1)
                            ?.invoke()
                    }
                }
                dialog.neutralMessage?.let {
                    setNeutralButton(it) { _, _ ->
                        clickListeners.getOrNull(2)
                            ?.invoke()
                    }
                }
                dialog.getView?.let { setView(it()) }
                setCancelable(dialog.cancelable)
            }
            .create()
    }

    private fun bodyArguments(dialog: AppDialogs) =
        if (dialog is AppDialogs.HasBodyFormatArgs) dialog.args.toTypedArray()
        else emptyArray()
}
