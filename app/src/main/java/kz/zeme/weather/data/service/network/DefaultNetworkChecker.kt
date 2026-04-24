package kz.zeme.weather.data.service.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kz.zeme.weather.domain.service.NetworkService

class DefaultNetworkChecker(
    private val context: Context
): NetworkService {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun checkForConnectivity(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}