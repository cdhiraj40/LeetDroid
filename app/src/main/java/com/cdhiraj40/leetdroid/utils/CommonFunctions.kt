package com.cdhiraj40.leetdroid.utils

import android.app.Activity
import android.content.Context
import android.view.Menu
import android.view.MenuItem
import com.cdhiraj40.leetdroid.ui.authentication.LoginActivity
import com.cdhiraj40.leetdroid.utils.dialog.AlertDialogShower
import com.cdhiraj40.leetdroid.utils.dialog.AppDialogs
import com.cdhiraj40.leetdroid.utils.extensions.openActivity
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.pow
import kotlin.math.roundToInt


class CommonFunctions {
    object Logout {
        fun showLogOutDialog(activity: Activity, context: Context): Boolean {
            AlertDialogShower(activity).show(AppDialogs.Logout, {
                FirebaseAuth.getInstance().signOut()
                activity.openActivity(LoginActivity::class.java)
                activity.finish()
                return@show
            }, {

            })
            return false
        }
    }

    object Round {
        fun roundDouble(value: Double, precision: Int): Double {
            val scale = 10.0.pow(precision.toDouble()).toInt()
            return (value * scale).roundToInt().toDouble() / scale
        }
    }

    object MenuItemVisibility {
        fun setItemsVisibility(menu: Menu, exception: MenuItem, visible: Boolean) {
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                if (item !== exception) item.isVisible = visible
            }
        }
    }
}