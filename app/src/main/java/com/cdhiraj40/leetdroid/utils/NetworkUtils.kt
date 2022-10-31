package com.cdhiraj40.leetdroid.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi

fun Context.hasNetwork(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        checkConnected(connectivityManager)
    } else {
        checkConnectedLegacy(connectivityManager)
    }
}


@RequiresApi(Build.VERSION_CODES.M)
fun checkConnected(connectivityManager: ConnectivityManager): Boolean {
    val activeNetwork = connectivityManager.activeNetwork
    activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
    capabilities ?: return false
    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
        NetworkCapabilities.TRANSPORT_WIFI
    )
}

fun checkConnectedLegacy(connectivityManager: ConnectivityManager): Boolean {
    val networkInfo = connectivityManager.activeNetworkInfo
    networkInfo ?: return false
    return networkInfo.isConnected && (networkInfo.type == ConnectivityManager.TYPE_WIFI || networkInfo.type == ConnectivityManager.TYPE_MOBILE)
}

fun checkConnectivity(context: Context): Boolean{
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo

    if(activeNetwork?.isConnected != null){
        return activeNetwork.isConnected
    } else{
        return false
    }

}