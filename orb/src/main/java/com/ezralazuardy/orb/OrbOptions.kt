/*
 * Created by Ezra Lazuardy on 5/4/20 2:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 5/4/20 2:41 AM
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
    var lowPan: Boolean = true,
    var vpn: Boolean = true,
    var wifi: Boolean = true,
    var wifiAware: Boolean = true
)