package com.example.leetdroid.utils

import android.content.res.AssetManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray

object JsonUtils {
    fun AssetManager.readAssetsFile(fileName: String): String =
        open(fileName).bufferedReader().use { it.readText() }

    fun <T> generateObjectFromJson(json: String?, type: Class<T>?): T {
        Log.d(Constant.TAG(JsonUtils::class.java).toString(), Gson().toString())
        return Gson().fromJson(json, type)
    }

    fun <T> generateFromJsonArray(json: JsonArray?, type: Class<T>?): T {
        Log.d(Constant.TAG(JsonUtils::class.java).toString(), Gson().toString())
        return Gson().fromJson(json, type)
    }

    fun <T> generateObjectFromJsonArray(json: String?, clazz: Class<Array<T>?>?): List<T> {
        Log.d(Constant.TAG(JsonUtils::class.java).toString(), "generateObjectFromJsonArray:Before")
        val array: Array<T> = Gson().fromJson(json, clazz)!!
        Log.d(Constant.TAG(JsonUtils::class.java).toString(), "generateObjectFromJsonArray: After")
        return mutableListOf(*array)
    }
}