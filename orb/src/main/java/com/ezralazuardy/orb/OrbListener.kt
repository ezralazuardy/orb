/*
 * Created by Ezra Lazuardy on 5/4/20 2:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 5/4/20 2:41 AM
 */

package com.ezralazuardy.orb

/**
 * OrbListener is an interface that can be applied to Orb using .setListener().
 * This listener can help to apply a callback on Orb lifecycle.
 */
interface OrbListener {

    /**
     * onOrbCreate() will be invoked on creation of Orb instance. In short word, this method will
     * called after client code calls .with().
     */
    fun onOrbCreate()

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
    fun onOrbStop(stopped: Boolean)
}