package com.example.gif_searcher.network.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkHelper(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean> = _isNetworkAvailable

    init {
        checkNetworkStatus()
    }

    private fun checkNetworkStatus() {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        val isConnected =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        _isNetworkAvailable.postValue(isConnected)
    }

    fun registerNetworkCallback() {
        val networkRequest = android.net.NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: android.net.Network) {
                    _isNetworkAvailable.postValue(true)
                }

                override fun onLost(network: android.net.Network) {
                    _isNetworkAvailable.postValue(false)
                }
            })
    }
}
