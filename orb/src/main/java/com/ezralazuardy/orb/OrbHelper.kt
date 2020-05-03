/*
 * Created by Ezra Lazuardy on 5/4/20 2:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 5/4/20 2:42 AM
 */

package com.ezralazuardy.orb

import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LifecycleOwner

/**
 * OrbHelper is a singleton object that provide some method to help the Orb network
 * monitoring process.
 */
internal object OrbHelper {

    /**
     * Context.lifecycleOwner() used to determine a lifecycle owner by a specified context. It will
     * return a LifecycleOwner object if successfully determining, and null if don't.
     * This method is an extension function of Context.
     */
    internal fun Context.lifecycleOwner(): LifecycleOwner? {
        var context = this
        var maxDepth = 20
        while (maxDepth-- > 0 && context !is LifecycleOwner) context =
            (context as ContextWrapper).baseContext
        return if (context is LifecycleOwner) context else null
    }

    /**
     * OrbType.isEnabledInOrbOptions() used to determine a single OrbOptions properties by a
     * specified OrbType. It will return the properties value (boolean) and a default false when
     * OrbType is UNKNOWN. This method is an extension function of OrbType.
     *
     * @param orbOptions
     */
    internal fun OrbType.isEnabledInOrbOptions(orbOptions: OrbOptions): Boolean = when (this) {
        OrbType.BLUETOOTH -> orbOptions.bluetooth
        OrbType.CELLULAR -> orbOptions.cellular
        OrbType.ETHERNET -> orbOptions.ethernet
        OrbType.LOW_PAN -> orbOptions.lowPan
        OrbType.VPN -> orbOptions.vpn
        OrbType.WIFI -> orbOptions.wifi
        OrbType.WIFI_AWARE -> orbOptions.wifiAware
        else -> false
    }

    /**
     * getCurrentNetworkType() used to determine the current network type used in device. It will
     * return a OrbType, and a default OrbType.UNKNOWN if no network is detected.
     *
     * @param context
     */
    internal fun getCurrentNetworkType(context: Context): OrbType {
        val connectivityManager =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE)) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                return when {
                    hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> OrbType.BLUETOOTH
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> OrbType.CELLULAR
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> OrbType.ETHERNET
                    hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> OrbType.LOW_PAN
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> OrbType.VPN
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> OrbType.WIFI
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> OrbType.WIFI_AWARE
                    else -> OrbType.UNKNOWN
                }
            }
        } else {
            @Suppress("DEPRECATION")
            connectivityManager?.activeNetworkInfo?.run {
                return when (this.type) {
                    ConnectivityManager.TYPE_BLUETOOTH -> OrbType.BLUETOOTH
                    ConnectivityManager.TYPE_MOBILE -> OrbType.CELLULAR
                    ConnectivityManager.TYPE_ETHERNET -> OrbType.ETHERNET
                    ConnectivityManager.TYPE_VPN -> OrbType.VPN
                    ConnectivityManager.TYPE_WIFI -> OrbType.WIFI
                    else -> OrbType.UNKNOWN
                }
            }
        }
        return OrbType.UNKNOWN
    }
}