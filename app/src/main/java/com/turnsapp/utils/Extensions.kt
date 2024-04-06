package com.turnsapp.utils

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


/**
 * Context extension method to check network connected or not
 * @return boolean if network not connected at that time it returns false
 */
fun Context.isOnline(): Boolean {

    var isOnline = false
    val manager = getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    try {
        isOnline = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            val activeNetworkInfo = manager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return isOnline
}