package com.example.leetdroid.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object JsonUtils {
    fun getJson(context: Context, fileName: String?): String {
        val stringBuilder = StringBuilder()
        val assetManager = context.assets
        try {
            val bufferedReader = BufferedReader(
                InputStreamReader(
                    assetManager.open(
                        fileName!!
                    ), "utf-8"
                )
            )
            var line: String
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

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