/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * Tofub.kt
 * @summary: Model representation of a TOFUB
 * @author: Mitch Thornton Nov 06, 2020
 */

package com.tandemdiabetes.detekt

data class Tofub(
    val description: ArrayList<String>,
    val params: ArrayList<String>?,
    val retrn: String?,
    val thrws: ArrayList<String>?
)
