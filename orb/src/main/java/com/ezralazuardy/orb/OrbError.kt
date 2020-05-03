/*
 * Created by Ezra Lazuardy on 5/4/20 2:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 5/4/20 2:42 AM
 */

package com.ezralazuardy.orb

/**
 * OrbError is a class to handle an error and stack trace that may occurred in Orb.
 * This class extends a Error() class, and override it's error message. The string message
 * parameter is important to print the stacktrace.
 *
 * OrbError have several default error message constanta (string).
 *
 * @param string
 */
data class OrbError(override val message: String) : Error() {
    companion object {
        const val LIFECYCLE_OWNER_NOT_FOUND: String =
            "Orb can't determine the context's lifecycle owner"
        const val ORB_NOT_INITIALIZED: String =
            "Orb is not initialized. Make sure to call .with() before calling other method"
    }
}