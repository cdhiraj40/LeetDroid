package com.example.leetdroid.utils

import androidx.recyclerview.widget.RecyclerView

class SimpleRecyclerViewScrollListener(
  private val onLayoutScrollListener: (RecyclerView, Int) -> Unit
) :
  RecyclerView.OnScrollListener() {
  override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
    super.onScrollStateChanged(recyclerView, newState)
    onLayoutScrollListener(
      recyclerView,
      newState
    )
  }
}
