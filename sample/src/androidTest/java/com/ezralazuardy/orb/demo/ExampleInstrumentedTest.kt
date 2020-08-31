/*
 * Created by Ezra Lazuardy on 8/31/20 4:51 PM
 * Copyright (c) 2020. All rights reserved.
 * Last modified 8/31/20 4:50 PM
 */

package com.ezralazuardy.orb.demo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ezralazuardy.orb.demo", appContext.packageName)
    }
}
