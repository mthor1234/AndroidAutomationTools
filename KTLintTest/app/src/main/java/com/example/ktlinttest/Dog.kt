/*
 * *
 *  * Copyright Tandem Diabetes Care, Inc. 2018-9/10/20 3:06 PM. All rights reserved.
 *  * Dog.kt
 *  * This class has a group of functions that maps to Larcus Cloud API authentication.
 *  * @author mthornton Feb 28, 2018
 *
 */

/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * Dog.kt
 * @summary:
 * @author: Mitch Thornton Sep 10, 2020
 */

package com.example.ktlinttest

import android.util.Log


class Dog {

    fun bark() {

        Log.d(TAG, "woof! woof!")
        Log.e(TAG, "woof! woof!")
    }

    companion object {
        private const val TAG = "Sample"
    }
}