/*
 * Created by Ezra Lazuardy on 5/6/20 8:23 AM
 * Copyright (c) 2020. All rights reserved.
 * Last modified 5/6/20 7:45 AM
 */

package com.ezralazuardy.orb

/**
 * OrbListener is an interface that can be applied to Orb using .setListener().
 * This listener can help to apply a callback on Orb lifecycle.
 */
interface OrbListener {

    /**
     * onOrbObserve() will be invoked on observation of an observer that specified in .observe().
     * This method will be called after the client code calls .observe().
     */
    fun onOrbObserve()

    /**
     * onOrbActive() will be invoked on activation of an observer. This method will be called when
     * .onActive() in OrbEngine is invoked by Android Lifecycle.
     */
    fun onOrbActive()

    /**
     * onOrbActive() will be invoked on inactivation of an observer. This method will be called when
     * .onInactive() in OrbEngine is invoked by Android Lifecycle.
     */
    fun onOrbInactive()

    /**
     * onOrbStop() will be invoked when Orb is being stopped. This method will be called after
     * client code calls .stop().
     */
    fun onOrbStop()
}