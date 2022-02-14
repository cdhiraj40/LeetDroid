package com.example.leetdroid.utils

class Constant {
    enum class DIFFICULTY(name: String) {
        Easy("Easy"),
        Medium("Medium"),
        Hard("Hard")
    }

    companion object {
        fun <T> TAG(className: Class<T>?): String? {
            return className?.name
        }
    }
}