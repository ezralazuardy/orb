package com.ezralazuardy.orb

import android.content.Context

object Orb {

    private lateinit var context: Context
    private lateinit var orbEngine: OrbEngine

    fun with(context: Context, options: OrbOptions = OrbOptions()) : OrbEngine {
        if(!this::context.isInitialized) Orb.context = context
        return if(this::orbEngine.isInitialized) orbEngine else OrbEngine(
            Orb.context, options)
    }
}

fun orbOptionsBuilder(
    bluetooth: Boolean = true, cellular: Boolean = true, ethernet: Boolean = true,
    lowPan: Boolean = true, vpn: Boolean = true, wifi: Boolean = true, wifiAware: Boolean = true
): OrbOptions = OrbOptions(bluetooth, cellular, ethernet, lowPan, vpn, wifi, wifiAware)