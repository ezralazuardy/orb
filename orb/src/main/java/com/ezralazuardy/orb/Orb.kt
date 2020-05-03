/*
 * Created by Ezra Lazuardy on 5/4/20 2:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 5/4/20 2:42 AM
 */

package com.ezralazuardy.orb

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.annotation.MainThread
import androidx.lifecycle.Observer
import com.ezralazuardy.orb.OrbHelper.lifecycleOwner

/**
 * Orb is a class that provide functionality to observe network events.
 * Make sure to call Orb.with() at least once in current context (view) before calling other
 * function to initialize the current context and lifecycle observer that will used in OrbEngine.
 * If don't, Orb will @throws an OrbError.
 *
 * Orb is not allowing constructor invocation. This means that client code must use .with() to
 * create a new instance of Orb.
 */
class Orb private constructor() {

    private lateinit var observer: Observer<OrbResponse>
    private lateinit var context: Context
    private lateinit var orbEngine: OrbEngine
    private var orbListener: OrbListener? = null
        set(value) {
            if (this::orbEngine.isInitialized) this.orbEngine.orbListener = value
            field = value
        }

    companion object {
        /**
         * Orb.with() used to initialize Orb functionality by instantiating an OrbEngine.
         * This method will construct the parameter that used in OrbEngine. Make sure to call
         * this method first before calling the other method as it is used to initializing the Orb.
         *
         * OrbListener.onOrbCreate() will be called in this method.
         *
         * @param context
         * @param orbOptions
         */
        @MainThread
        fun with(context: Context, options: OrbOptions = OrbOptions()): Orb = Orb().apply {
            this.orbListener?.onOrbCreate()
            if (!this::context.isInitialized) {
                this.context = context
                this.orbEngine = OrbEngine(this.context, options)
            }
        }
    }

    /**
     * Orb.setListener() used to install an OrbListener to current Orb instance. This means that
     * now Orb can notify via interface listener to the client code.
     *
     * @param orbListener
     */
    @MainThread
    fun setListener(orbListener: OrbListener): Orb = this.apply {
        this.orbListener = orbListener
    }

    /**
     * Orb.observe() used to instruct Orb to start observing the OrbEngine.
     * This means that client code is now can start observing the network events by using OrbEngine
     * Live Data. The OrbEngine will notify by using an object OrbResponse that contain current
     * network state information. This method require lambda parameter to be invoked when network
     * event changes.
     *
     * Calling this method before {@link Orb#with()} will @throws an OrbError.
     *
     * OrbListener.onOrbObserve() will be called in this method.
     *
     * @param lambda
     */
    @MainThread
    fun observe(observer: (OrbResponse) -> Unit): Orb {
        this.orbListener?.onOrbObserve()
        try {
            if (context.lifecycleOwner() != null) {
                if (this::context.isInitialized) {
                    if (this::observer.isInitialized) orbEngine.removeObserver(this.observer)
                    this.observer = Observer(observer)
                    orbEngine.observe(context.lifecycleOwner()!!, this.observer)
                } else throw OrbError(OrbError.ORB_NOT_INITIALIZED)
            } else throw OrbError(OrbError.LIFECYCLE_OWNER_NOT_FOUND)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    /**
     * Orb.stop() used to instruct Orb to stop observing the current OrbEngine.
     * This means that current context will get OrbResponse no more. You need to call
     * Orb.observe() in order to start observing the network events again.
     *
     * Calling this method before {@link Orb#with()} will @throws an OrbError.
     *
     * This method return boolean true if action succeed and false if don't.
     *
     * OrbListener.onOrbStop() will be called in this method.
     */
    @MainThread
    fun stop(): Boolean {
        try {
            if (this::orbEngine.isInitialized) {
                if (this::observer.isInitialized) {
                    orbEngine.removeObserver(this.observer)
                    this.orbListener?.onOrbStop(true)
                    return true
                } else throw OrbError(OrbError.ORB_NOT_INITIALIZED)
            } else throw OrbError(OrbError.ORB_NOT_INITIALIZED)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        this.orbListener?.onOrbStop(false)
        return false
    }

    /**
     * allowBluetooth() is a helper function to toggle the bluetooth network type filtering in Orb.
     * Disallowing bluetooth will make Orb to not detecting bluetooth network type.
     *
     * Default parameter is boolean true.
     *
     * @param boolean
     */
    fun allowBluetooth(state: Boolean = true): Orb {
        this.orbEngine.setOption(OrbType.BLUETOOTH, state)
        return this
    }

    /**
     * allowCellular() is a helper function to toggle the cellular network type filtering in Orb.
     * Disallowing cellular will make Orb to not detecting cellular network type.
     *
     * Default parameter is boolean true.
     *
     * @param boolean
     */
    fun allowCellular(state: Boolean = true): Orb {
        this.orbEngine.setOption(OrbType.CELLULAR, state)
        return this
    }

    /**
     * allowEthernet() is a helper function to toggle the ethernet network type filtering in Orb.
     * Disallowing ethernet will make Orb to not detecting ethernet network type.
     *
     * Default parameter is boolean true.
     *
     * @param boolean
     */
    fun allowEthernet(state: Boolean = true): Orb {
        this.orbEngine.setOption(OrbType.ETHERNET, state)
        return this
    }

    /**
     * allowLowPan() is a helper function to toggle the low pan network type filtering in Orb.
     * Disallowing low pan will make Orb to not detecting low pan network type.
     *
     * This function will only work on API Level 23+ (Marshmallow), otherwise Orb will not detecting
     * low pan network type at all.
     *
     * Default parameter is boolean true.
     *
     * @param boolean
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun allowLowPan(state: Boolean = true): Orb {
        this.orbEngine.setOption(OrbType.LOW_PAN, state)
        return this
    }

    /**
     * allowVPN() is a helper function to toggle the VPN network type filtering in Orb.
     * Disallowing VPN will make Orb to not detecting VPN network type.
     *
     * Default parameter is boolean true.
     *
     * @param boolean
     */
    fun allowVPN(state: Boolean = true): Orb {
        this.orbEngine.setOption(OrbType.VPN, state)
        return this
    }

    /**
     * allowWifi() is a helper function to toggle the wifi network type filtering in Orb.
     * Disallowing wifi will make Orb to not detecting wifi network type.
     *
     * Default parameter is boolean true.
     *
     * @param boolean
     */
    fun allowWifi(state: Boolean = true): Orb {
        this.orbEngine.setOption(OrbType.WIFI, state)
        return this
    }

    /**
     * allowWifiAware() is a helper function to toggle the wifi aware network type filtering in Orb.
     * Disallowing wifi aware will make Orb to not detecting wifi aware network type.
     *
     * This function will only work on API Level 23+ (Marshmallow), otherwise Orb will not detecting
     * wifi aware network type at all.
     *
     * Default parameter is boolean true.
     *
     * @param boolean
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun allowWifiAware(state: Boolean = true): Orb {
        this.orbEngine.setOption(OrbType.WIFI_AWARE, state)
        return this
    }

    /**
     * allowBluetoothOnly() is a helper function to enable the bluetooth only network type filtering in Orb.
     * Calling this function will make Orb to detect bluetooth network type only.
     */
    fun allowBluetoothOnly(): Orb {
        this.orbEngine.orbOptions = orbOptions {
            mapOf(
                OrbType.CELLULAR to false,
                OrbType.ETHERNET to false,
                OrbType.LOW_PAN to false,
                OrbType.VPN to false,
                OrbType.WIFI to false,
                OrbType.WIFI_AWARE to false
            )
        }
        return this
    }

    /**
     * allowCelullarOnly() is a helper function to enable the cellular only network type filtering in Orb.
     * Calling this function will make Orb to detect cellular network type only.
     */
    fun allowCelullarOnly(): Orb {
        this.orbEngine.orbOptions = orbOptions {
            mapOf(
                OrbType.BLUETOOTH to false,
                OrbType.ETHERNET to false,
                OrbType.LOW_PAN to false,
                OrbType.VPN to false,
                OrbType.WIFI to false,
                OrbType.WIFI_AWARE to false
            )
        }
        return this
    }

    /**
     * allowEthernetOnly() is a helper function to enable the ethernet only network type filtering in Orb.
     * Calling this function will make Orb to detect ethernet network type only.
     */
    fun allowEthernetOnly(): Orb {
        this.orbEngine.orbOptions = orbOptions {
            mapOf(
                OrbType.BLUETOOTH to false,
                OrbType.CELLULAR to false,
                OrbType.LOW_PAN to false,
                OrbType.VPN to false,
                OrbType.WIFI to false,
                OrbType.WIFI_AWARE to false
            )
        }
        return this
    }

    /**
     * allowLowPanOnly() is a helper function to enable the low pan only network type filtering in Orb.
     * Calling this function will make Orb to detect low pan network type only.
     *
     * This function will only work on API Level 23+ (Marshmallow), otherwise Orb will not detecting
     * low pan network type at all.
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun allowLowPanOnly(): Orb {
        this.orbEngine.orbOptions = orbOptions {
            mapOf(
                OrbType.BLUETOOTH to false,
                OrbType.CELLULAR to false,
                OrbType.ETHERNET to false,
                OrbType.VPN to false,
                OrbType.WIFI to false,
                OrbType.WIFI_AWARE to false
            )
        }
        return this
    }

    /**
     * allowWifiOnly() is a helper function to enable the wifi only network type filtering in Orb.
     * Calling this function will make Orb to detect wifi network type only.
     */
    fun allowWifiOnly(): Orb {
        this.orbEngine.orbOptions = orbOptions {
            mapOf(
                OrbType.BLUETOOTH to false,
                OrbType.CELLULAR to false,
                OrbType.ETHERNET to false,
                OrbType.VPN to false,
                OrbType.LOW_PAN to false,
                OrbType.WIFI_AWARE to false
            )
        }
        return this
    }

    /**
     * allowWifiAwareOnly() is a helper function to enable the wifi aware only network type filtering in Orb.
     * Calling this function will make Orb to detect wifi aware network type only.
     *
     * This function will only work on API Level 23+ (Marshmallow), otherwise Orb will not detecting
     * wifi aware network type at all.
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun allowWifiAwareOnly(): Orb {
        this.orbEngine.orbOptions = orbOptions {
            mapOf(
                OrbType.BLUETOOTH to false,
                OrbType.CELLULAR to false,
                OrbType.ETHERNET to false,
                OrbType.VPN to false,
                OrbType.LOW_PAN to false,
                OrbType.WIFI to false
            )
        }
        return this
    }
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
        lowPan = mapOptions.getOrElse(OrbType.LOW_PAN, { true })
        vpn = mapOptions.getOrElse(OrbType.VPN, { true })
        wifi = mapOptions.getOrElse(OrbType.WIFI, { true })
        wifiAware = mapOptions.getOrElse(OrbType.WIFI_AWARE, { true })
    }
}