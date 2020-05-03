/*
 * Created by Ezra Lazuardy on 5/4/20 4:18 AM
 * Copyright (c) 2020. All rights reserved.
 * Last modified 5/4/20 4:17 AM
 */

package com.ezralazuardy.orb

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LiveData
import com.ezralazuardy.orb.OrbHelper.getCurrentNetworkType
import com.ezralazuardy.orb.OrbHelper.isEnabledInOrbOptions

/**
 * OrbEngine is a main class for Orb API. This class extends a LiveData class (androidx.lifecycle).
 * This means that OrbEngine is an observable and can be observed by Orb API. When observed,
 * OrbEngine will notify an OrbResponse object that hold device network state information.
 *
 * Due to Android API limitation, we need to use @suppress('DEPRECATION') to remove the deprecation
 * warning.
 *
 * @param context
 * @param orpOptions
 * @param orbListener
 */
@Suppress("DEPRECATION")
class OrbEngine(
    private val context: Context,
    internal var orbOptions: OrbOptions,
    internal var orbListener: OrbListener? = null
) : LiveData<OrbResponse>() {

    private var currentState = OrbState.UNKNOWN
    private var connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) { updateConnection() }
    }
    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback

    /**
     * Overriding onActive() method from LiveData class in order to start the network
     * monitoring service. This method is fully handled and executed by Android lifecycle,
     * so no need to invoke it manually.
     *
     * OrbListener.onOrbActive() will called in this method.
     */
    override fun onActive() {
        super.onActive()
        orbListener?.onOrbActive()
        updateConnection()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) registerNetworkCallback()
        else context.registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    /**
     * Overriding onInactive() method from LiveData class in order to stop the current network
     * monitoring service. This method is fully handled and executed by Android lifecycle,
     * so no need to invoke it manually.
     *
     * OrbListener.onOrbInactive() will called in this method.
     */
    override fun onInactive() {
        super.onInactive()
        orbListener?.onOrbInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        else context.unregisterReceiver(networkReceiver)
    }

    /**
     * getConnectivityManagerCallback() used to initializing ConnectivityManagerCallback for network
     * monitoring purposes in Android API level 21+. This method will only be called on Android
     * API Level 21+.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network?) {
                setCurrentState(OrbState.CONNECTED)
            }

            override fun onLost(network: Network?) {
                setCurrentState(OrbState.DISCONNECTED)
            }
        }
        return connectivityManagerCallback
    }

    /**
     * registerNetworkCallback() used to build and register NetworkCallback for network monitoring
     * purposes in Android API level 21+. This method will only be called on Android API Level 21+.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun registerNetworkCallback() {
        with(NetworkRequest.Builder()) {
            if (OrbType.BLUETOOTH.isEnabledInOrbOptions(orbOptions))
                addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
            if (OrbType.CELLULAR.isEnabledInOrbOptions(orbOptions))
                addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            if (OrbType.ETHERNET.isEnabledInOrbOptions(orbOptions))
                addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            if (OrbType.LOW_PAN.isEnabledInOrbOptions(orbOptions) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
                addTransportType(NetworkCapabilities.TRANSPORT_LOWPAN)
            if (OrbType.VPN.isEnabledInOrbOptions(orbOptions))
                addTransportType(NetworkCapabilities.TRANSPORT_VPN)
            if (OrbType.WIFI.isEnabledInOrbOptions(orbOptions))
                addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            if (OrbType.WIFI_AWARE.isEnabledInOrbOptions(orbOptions) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                addTransportType(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
            connectivityManager.registerNetworkCallback(build(), getConnectivityManagerCallback())
        }
    }

    /**
     * updateConnection() used to update the current network state in OrbEngine. This method will
     * only be called in Android API level < 21, while the API level 21+ already handling OrbEngine
     * state update in the ConnectivityManagerCallback.
     */
    private fun updateConnection() {
        connectivityManager.activeNetworkInfo?.let {
            if (it.isConnected && getCurrentNetworkType(context).isEnabledInOrbOptions(orbOptions))
                setCurrentState(OrbState.CONNECTED)
            else
                setCurrentState(OrbState.DISCONNECTED)
        }
    }

    /**
     * setCurrentState used to set the current network state of OrbEngine and also notify an
     * OrbResponse to client code.
     *
     * @param orbState
     */
    private fun setCurrentState(newState: OrbState) {
        currentState = newState
        postValue(OrbResponse(currentState, getCurrentNetworkType(context)))
    }

    /**
     * setOption() used to set a single properties of OrbOption that used in OrbEngine. This method
     * will allow to change the network type filtering in OrbEngine.
     *
     * This method will @throws an OrbError when the orbType parameter is OrbType.UNKNOWN
     *
     * @param orbType
     * @param boolean
     */
    internal fun setOption(orbType: OrbType, state: Boolean = true): OrbEngine {
        try {
            when (orbType) {
                OrbType.BLUETOOTH -> orbOptions.bluetooth = state
                OrbType.CELLULAR -> orbOptions.cellular = state
                OrbType.ETHERNET -> orbOptions.ethernet = state
                OrbType.LOW_PAN -> orbOptions.lowPan = state
                OrbType.VPN -> orbOptions.vpn = state
                OrbType.WIFI -> orbOptions.wifi = state
                OrbType.WIFI_AWARE -> orbOptions.wifiAware = state
                else -> throw OrbException(OrbException.DISALLOW_ORB_TYPE_UNKNOWN)
            }
        } catch (e: OrbException) {
            e.printStackTrace()
        }
        return this
    }
}