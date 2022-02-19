package com.example.leetdroid.utils.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.leetdroid.R
import com.google.android.material.snackbar.Snackbar


fun Fragment.closeKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
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


fun showSnackBarWithAction(
    activity: Activity,
    message: String?,
    actionRes: String,
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

// usage: view1.below(view2)
infix fun View.below(view: View) {
    (this.layoutParams as? RelativeLayout.LayoutParams)?.addRule(RelativeLayout.BELOW, view.id)
}

// usage: view1.above(view2)
infix fun View.above(view: View) {
    (this.layoutParams as? RelativeLayout.LayoutParams)?.addRule(RelativeLayout.ABOVE, view.id)
}


