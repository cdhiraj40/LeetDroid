package com.cdhiraj40.leetdroid.utils

class Constant {

    val userName = "cdhiraj40"
    val repositoryName = "LeetDroid"

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