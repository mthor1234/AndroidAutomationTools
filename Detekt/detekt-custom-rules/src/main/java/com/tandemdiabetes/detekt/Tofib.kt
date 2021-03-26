/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * Tofib.kt
 * Model representation of the TOFIB
 * @author: Mitch Thornton Nov 02, 2020
 */

package com.tandemdiabetes.detekt

data class Tofib(
    val creationYear: String,
    val updatedYear: String,
    val fileName: String,
    val description: String,
    val author: String,
    val creationDate: String
)
