/*
 * Created by Ezra Lazuardy on 5/6/20 8:23 AM
 * Copyright (c) 2020. All rights reserved.
 * Last modified 5/6/20 8:05 AM
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
object OrbHelper {

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
        OrbType.LOWPAN -> orbOptions.loWPAN
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
                    hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> OrbType.LOWPAN
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

    /**
     * orbObserve() is a helper function that return OrbResponse lambda to help building Orb observer.
     * This function take a lambda as the parameter.
     *
     * @param lambda
     */
    fun orbObserver(observer: (OrbResponse) -> Unit) = observer

    /**
     * orbOptions() is a helper function that return OrbOptions object to initialize the Orb options.
     * This function take a lambda that returning Map<OrbType, Boolean> as the parameter.
     *
     * @param lambda
     */
    fun orbOptions(options: () -> Map<OrbType, Boolean>): OrbOptions {
        return OrbOptions().apply {
            val mapOptions = options.invoke()
            bluetooth = mapOptions.getOrElse(OrbType.BLUETOOTH, { true })
            cellular = mapOptions.getOrElse(OrbType.CELLULAR, { true })
            ethernet = mapOptions.getOrElse(OrbType.ETHERNET, { true })
            loWPAN = mapOptions.getOrElse(OrbType.LOWPAN, { true })
            vpn = mapOptions.getOrElse(OrbType.VPN, { true })
            wifi = mapOptions.getOrElse(OrbType.WIFI, { true })
            wifiAware = mapOptions.getOrElse(OrbType.WIFI_AWARE, { true })
        }
    }
}