package com.example.leetdroid.utils

import android.content.Context

import android.util.Log
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

object JsonUtils {
    private const val TAG = "JsonUtils"
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
        val gson = Gson()
        Log.d(TAG, gson.toString())
        return gson.fromJson(json, type)
    }

    fun <T> generateObjectFromJsonArray(json: String?, clazz: Class<Array<T>?>?): List<T> {
        Log.d(TAG, "generateObjectFromJsonArray:Before")
        val gson = Gson()
        val array: Array<T> = gson.fromJson(json, clazz)!!
        Log.d(TAG, "generateObjectFromJsonArray: After")
        return mutableListOf(*array)
    }
}