package com.example.leetdroid.utils

import android.app.Activity
import com.example.leetdroid.extensions.openActivity
import com.example.leetdroid.ui.authentication.LoginActivity
import com.example.leetdroid.utils.dialog.AlertDialogShower
import com.example.leetdroid.utils.dialog.AppDialogs
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.pow
import kotlin.math.roundToInt

class CommonFunctions {
    object Logout {
        fun showLogOutDialog(activity: Activity) {
            AlertDialogShower(activity).show(AppDialogs.LogOut, {
                FirebaseAuth.getInstance().signOut()
                activity.openActivity(LoginActivity::class.java)
                activity.finish()
            }, {

            })
        }
    }

    object Round {
        private fun roundDouble(value: Double, precision: Int): Double {
            val scale = 10.0.pow(precision.toDouble()).toInt()
            return (value * scale).roundToInt().toDouble() / scale
        }
    }
}