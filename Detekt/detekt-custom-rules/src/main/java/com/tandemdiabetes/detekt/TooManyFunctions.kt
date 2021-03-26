/**
 * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.
 * TooManyFunctions.kt
 * This is a sample rule reporting too many functions inside a file.
 * @author Mitchell Thornton Feb 28, 2018
 */

package com.tandemdiabetes.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNamedFunction

// Number of functions threshold
const val THRESHOLD = 2

// TODO: 11/16/2020 Check with the team to see if we want to use this
class TooManyFunctions : Rule() {

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "This rule reports a file with an excessive function count.",
        Debt.TWENTY_MINS
    )

    private var amount: Int = 0

    /**
     * Looks at the Kotlin file and uses the named function count to determine if it violates the
     * threshold. If it violates the threshold, it will report a Detekt issue
     * @param file The supplied Kotlin file
     */
    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)
        if (amount > THRESHOLD) {
            report(
                CodeSmell(
                    issue, Entity.atPackageOrFirstDecl(file),
                    message = "The file ${file.name} has $amount function declarations. " +
                        "Threshold is specified with $THRESHOLD."
                )
            )
        }
        amount = 0
    }

    /**
     * Looks at each named function in the KtFile and increments the function counter
     * @param function The supplied named function
     */
    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)
        amount++
    }
}
