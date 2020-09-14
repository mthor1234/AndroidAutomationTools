/*
 * *
 *  * Copyright Tandem Diabetes Care, Inc. 2018-9/10/20 2:48 PM. All rights reserved.
 *  * TOFIBChecker.kt
 *  * This class has a group of functions that maps to Larcus Cloud API authentication.
 *  * @author mthornton Feb 28, 2018
 *  
 */

package com.example.rules

import com.android.tools.lint.checks.AccessibilityDetector.ISSUE
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

class TOFIBChecker : Detector(), SourceCodeScanner {

    companion object {
        private val IMPLEMENTATION = Implementation(
            TOFIBChecker::class.java,
            Scope.JAVA_FILE_SCOPE
        )

        val ISSUE: Issue = Issue
            .create(
                id = "AndroidLogDetector",
                briefDescription = "The android Log should not be used",
                explanation = """
                For amazing showcasing purposes we should not use the Android Log. We should the
                AmazingLog instead.
            """.trimIndent(),
                category = Category.CORRECTNESS,
                priority = 9,
                severity = Severity.ERROR,
                androidSpecific = true,
                implementation = IMPLEMENTATION
            )
    }

    override fun getApplicableMethodNames(): List<String> =
        listOf("tag", "format", "v", "d", "i", "w", "e", "wtf")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        super.visitMethodCall(context, node, method)
        val evaluator = context.evaluator
        if (evaluator.isMemberInClass(method, "android.util.Log")) {
            reportUsage(context, node)
        }
    }

    private fun reportUsage(context: JavaContext, node: UCallExpression) {
        context.report(
            issue = ISSUE,
            scope = node,
            location = context.getCallLocation(
                call = node,
                includeReceiver = true,
                includeArguments = true
            ),
            message = "android.util.Log usage is forbidden."
        )
    }

}