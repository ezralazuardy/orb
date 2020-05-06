/*
 * Created by Ezra Lazuardy on 5/6/20 8:23 AM
 * Copyright (c) 2020. All rights reserved.
 * Last modified 5/6/20 8:03 AM
 */

package com.ezralazuardy.orb

/**
 * OrbOptions class is used to save several option properties that used in OrbEngine.
 *
 * @param boolean
 * @param boolean
 * @param boolean
 * @param boolean
 * @param boolean
 * @param boolean
 * @param boolean
 */
data class OrbOptions(
    var bluetooth: Boolean = true,
    var cellular: Boolean = true,
    var ethernet: Boolean = true,
    var loWPAN: Boolean = true,
    var vpn: Boolean = true,
    var wifi: Boolean = true,
    var wifiAware: Boolean = true
)