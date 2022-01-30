package com.example.leetdroid.api

import com.google.gson.JsonArray
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ContestApi {
    @GET("leet_code")
    fun getContent(): Call<JsonArray>

    companion object {
        fun create(): ContestApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL.leetcodeUpcomingContest)
                .build()
            return retrofit.create(ContestApi::class.java)
        }
    }
}