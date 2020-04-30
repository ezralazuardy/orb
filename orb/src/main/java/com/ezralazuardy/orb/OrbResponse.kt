package com.ezralazuardy.orb

data class OrbResponse (
    val state: OrbState = OrbState.UNKNOWN,
    val errorMessage: String? = null
) {
    val connected = this.state == OrbState.CONNECTED
}