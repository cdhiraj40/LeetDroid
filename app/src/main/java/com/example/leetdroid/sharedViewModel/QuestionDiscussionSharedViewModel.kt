package com.example.leetdroid.sharedViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuestionDiscussionSharedViewModel : ViewModel() {

    val discussionTitle = MutableLiveData<String>()

    // function to get question's discussion item title
    fun getDiscussionTitle(title: String) {
        discussionTitle.postValue(title)
    }
}