package com.example.leetdroid.utils

class Constant {
    enum class DIFFICULTY(name: String) {
        Easy("Easy"),
        Medium("Medium"),
        Hard("Hard")
    }

    companion object {
        val TAG = fun(className: String) {
            className::class.qualifiedName
        }
    }
}