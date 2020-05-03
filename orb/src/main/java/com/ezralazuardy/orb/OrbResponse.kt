/*
 * Created by Ezra Lazuardy on 5/4/20 2:42 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 5/4/20 2:41 AM
 */

package com.ezralazuardy.orb

/**
 * OrbResponse class is used as a response sent by Orb to the client code. This class hold several
 * final properties that determine the device current network information.
 *
 * OrbResponse also has .connected properties to easily determine if the current state is equal
 * to OrbType.CONNECTED
 *
 * @param orbState
 * @param orbType
 * @param string
 */
data class OrbResponse (
    val state: OrbState = OrbState.UNKNOWN,
    val type: OrbType = OrbType.UNKNOWN,
    val errorMessage: String? = null
) {
    val connected = this.state == OrbState.CONNECTED
}