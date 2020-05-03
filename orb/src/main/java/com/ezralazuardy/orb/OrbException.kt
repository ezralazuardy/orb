/*
 * Created by Ezra Lazuardy on 5/4/20 2:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 5/4/20 2:42 AM
 */

package com.ezralazuardy.orb

/**
 * OrbException is a class to handle an exception and stack trace that may occurred in Orb.
 * This class extends a Exception() class, and override it's exception message. The string message
 * parameter is important to print the stacktrace.
 *
 * OrbException have several default exception message constanta (string).
 *
 * @param string
 */
data class OrbException(override val message: String) : Exception() {
    companion object {
        const val DISALLOW_ORB_TYPE_UNKNOWN: String = "Unknown Orb state is now allowed"
    }
}