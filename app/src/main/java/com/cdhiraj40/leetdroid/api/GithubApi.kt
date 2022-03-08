package com.cdhiraj40.leetdroid.api

import com.cdhiraj40.leetdroid.model.ContributorListModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {
    @GET("repos/{owner}/{repo}/stats/contributors")
    fun getContributors(
        @Path("owner") owner: String?,
        @Path("repo") repositoryName: String
    ): Call<ContributorListModel>

    companion object {
        fun create(): GithubApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL.githubApi)
                .build()
            return retrofit.create(GithubApi::class.java)
        }
    }
}