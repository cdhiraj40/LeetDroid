package com.example.gdsc_hackathon.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object NetworkUtils {
    /**
     * check availability of any network
     *
     * @return true if a network is ready to be used
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfos = connectivity.allNetworkInfo
        for (networkInfo in networkInfos) {
            if (isNetworkConnectionOK(networkInfo)) {
                return true
            }
        }
        return false
    }

    private fun isNetworkConnectionOK(networkInfo: NetworkInfo): Boolean {
        return networkInfo.state == NetworkInfo.State.CONNECTED
    }
}