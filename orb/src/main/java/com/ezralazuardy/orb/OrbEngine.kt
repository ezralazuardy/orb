package com.ezralazuardy.orb

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.lifecycle.LiveData

@Suppress("DEPRECATION")
class OrbEngine(private val context: Context, private val orbOptions: OrbOptions) : LiveData<OrbResponse>() {

    private lateinit var connectivityManagerCallback: ConnectivityManager.NetworkCallback
    private var connectivityManager: ConnectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) { updateConnection() }
    }
    private var currentState = OrbState.UNKNOWN

    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager.registerDefaultNetworkCallback(getConnectivityManagerCallback())
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> lollipopNetworkAvailableRequest()
            else -> context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    override fun onInactive() {
        super.onInactive()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
        else context.unregisterReceiver(networkReceiver)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun lollipopNetworkAvailableRequest() {
        with(NetworkRequest.Builder()) {
            if(orbOptions.bluetooth) addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
            if(orbOptions.cellular) addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            if(orbOptions.ethernet) addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            if(orbOptions.lowPan && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) addTransportType(NetworkCapabilities.TRANSPORT_LOWPAN)
            if(orbOptions.vpn) addTransportType(NetworkCapabilities.TRANSPORT_VPN)
            if(orbOptions.wifi) addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            if(orbOptions.wifiAware && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) addTransportType(NetworkCapabilities.TRANSPORT_WIFI_AWARE)
            connectivityManager.registerNetworkCallback(build(), getConnectivityManagerCallback())
        }
    }

    private fun getConnectivityManagerCallback(): ConnectivityManager.NetworkCallback {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManagerCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network?) {
                    if(currentState != OrbState.CONNECTED) {
                        postValue(OrbResponse(OrbState.CONNECTED))
                        setCurrentState(OrbState.CONNECTED)
                    }
                }

                override fun onLost(network: Network?) {
                    if(currentState != OrbState.DISCONNECTED) {
                        postValue(OrbResponse(OrbState.DISCONNECTED))
                        setCurrentState(OrbState.DISCONNECTED)
                    }
                }
            }
            return connectivityManagerCallback
        } else {
            with(IllegalAccessError()) {
                if (currentState != OrbState.FAILURE) {
                    postValue(OrbResponse(OrbState.FAILURE, this.message))
                    setCurrentState(OrbState.FAILURE)
                }
                throw this
            }
        }
    }

    private fun updateConnection() {
        connectivityManager.activeNetworkInfo?.let {
            if(it.isConnected) {
                if(currentState != OrbState.CONNECTED) {
                    postValue(OrbResponse(OrbState.CONNECTED))
                    setCurrentState(OrbState.CONNECTED)
                }
            } else {
                if(currentState != OrbState.DISCONNECTED) {
                    postValue(OrbResponse(OrbState.DISCONNECTED))
                    setCurrentState(OrbState.DISCONNECTED)
                }
            }
        }
    }

    private fun setCurrentState(newState: OrbState) {
        currentState = newState
    }

    fun allowBluetooth(state: Boolean = true) : OrbEngine {
        this@OrbEngine.orbOptions.bluetooth = state
        return this@OrbEngine
    }

    fun allowBluetoothOnly() : OrbEngine {
        this@OrbEngine.orbOptions.apply {
            bluetooth = true
            cellular = false
            ethernet = false
            lowPan = false
            vpn = false
            wifi = false
            wifiAware = false
        }
        return this@OrbEngine
    }

    fun allowCelullar(state: Boolean = true) : OrbEngine {
        this@OrbEngine.orbOptions.cellular = state
        return this@OrbEngine
    }

    fun allowCelullarOnly() : OrbEngine {
        this@OrbEngine.orbOptions.apply {
            bluetooth = false
            cellular = true
            ethernet = false
            lowPan = false
            vpn = false
            wifi = false
            wifiAware = false
        }
        return this@OrbEngine
    }

    fun allowEthernet(state: Boolean = true) : OrbEngine {
        this@OrbEngine.orbOptions.ethernet = state
        return this@OrbEngine
    }

    fun allowEthernetOnly() : OrbEngine {
        this@OrbEngine.orbOptions.apply {
            bluetooth = false
            cellular = false
            ethernet = true
            lowPan = false
            vpn = false
            wifi = false
            wifiAware = false
        }
        return this@OrbEngine
    }

    fun allowLowPan(state: Boolean = true) : OrbEngine {
        this@OrbEngine.orbOptions.lowPan = state
        return this@OrbEngine
    }

    fun allowLowPanOnly() : OrbEngine {
        this@OrbEngine.orbOptions.apply {
            bluetooth = false
            cellular = false
            ethernet = false
            lowPan = true
            vpn = false
            wifi = false
            wifiAware = false
        }
        return this@OrbEngine
    }

    fun allowVPN(state: Boolean = true) : OrbEngine {
        this@OrbEngine.orbOptions.vpn = state
        return this@OrbEngine
    }

    fun allowVPNOnly() : OrbEngine {
        this@OrbEngine.orbOptions.apply {
            bluetooth = false
            cellular = false
            ethernet = false
            lowPan = false
            vpn = true
            wifi = false
            wifiAware = false
        }
        return this@OrbEngine
    }

    fun allowWifi(state: Boolean = true) : OrbEngine {
        this@OrbEngine.orbOptions.wifi = state
        return this@OrbEngine
    }

    fun allowWifiOnly() : OrbEngine {
        this@OrbEngine.orbOptions.apply {
            bluetooth = false
            cellular = false
            ethernet = false
            lowPan = false
            vpn = false
            wifi = true
            wifiAware = false
        }
        return this@OrbEngine
    }

    fun allowWifiAware(state: Boolean = true) : OrbEngine {
        this@OrbEngine.orbOptions.wifiAware = state
        return this@OrbEngine
    }

    fun allowWifiAwareOnly() : OrbEngine {
        this@OrbEngine.orbOptions.apply {
            bluetooth = false
            cellular = false
            ethernet = false
            lowPan = false
            vpn = false
            wifi = false
            wifiAware = true
        }
        return this@OrbEngine
    }
}