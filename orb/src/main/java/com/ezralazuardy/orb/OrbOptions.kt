package com.ezralazuardy.orb

data class OrbOptions(
    var bluetooth: Boolean = true,
    var cellular: Boolean = true,
    var ethernet: Boolean = true,
    var lowPan: Boolean = true,
    var vpn: Boolean = true,
    var wifi: Boolean = true,
    var wifiAware: Boolean = true
)