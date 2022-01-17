package com.example.leetdroid.helper

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class ScrollingViewWithBottomNavigationBehavior(context: Context, attrs: AttributeSet) :
AppBarLayout.ScrollingViewBehavior(context, attrs) {
    private var bottomMargin = 0

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean =
        super.layoutDependsOn(parent, child, dependency) || dependency is BottomNavigationView

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val result = super.onDependentViewChanged(parent, child, dependency)
        if (dependency is BottomNavigationView) {
            if (moveFragmentWithNavigationBar(child, dependency, parent)) return true
        }
        return result
    }

    private fun moveFragmentWithNavigationBar(
        fragmentContainer: View,
        navigationBar: BottomNavigationView,
        coordinatorLayout: CoordinatorLayout
    ): Boolean {
            val newBottomMargin = calculateNewBottomMargin(navigationBar, coordinatorLayout)
            if (newBottomMargin != bottomMargin) {
                changeReaderToolbarMargin(newBottomMargin, fragmentContainer)
                return true
            }
        return false
    }

    private fun calculateNewBottomMargin(
        navigationBar: BottomNavigationView,
        coordinatorLayout: CoordinatorLayout
    ): Int = (coordinatorLayout.height - navigationBar.y).toInt()

    private fun changeReaderToolbarMargin(
        newBottomMargin: Int,
        fragmentContainer: View
    ) {
        bottomMargin = newBottomMargin
        val layout = fragmentContainer.layoutParams as ViewGroup.MarginLayoutParams
        layout.bottomMargin = bottomMargin
        fragmentContainer.requestLayout()
    }
}
