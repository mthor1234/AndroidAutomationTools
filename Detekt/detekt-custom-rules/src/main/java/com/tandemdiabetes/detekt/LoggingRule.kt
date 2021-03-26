/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * LoggingRule.kt
 * Checks to see if our logging statements are wrapped with BuildConfig.DEBUG
 * @author: Mitch Thornton Nov 02, 2020
 */

package com.tandemdiabetes.detekt

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.*

class LoggingRule : Rule() {


    private val logRegex = Regex("Log\\.(wtf|[v,d,i,w,e])\\(")
    private val debugBuildRegex = Regex("\\(BuildConfig\\.DEBUG_LOG\\)")

    private val foundLogStatements = arrayListOf<String>()
    private val foundDebugWrappers = arrayListOf<String>()

    override val issue = Issue(
            javaClass.simpleName,
            Severity.CodeSmell,
            "This rule reports a file with incorrect Logging Statements.",
            Debt.FIVE_MINS
    )

    // TODO: Fails when historyLog.typeId is called since it's a qualified expression
    //  HistoryLogManager.kt:492:21
    //  HistoryLogRequest.kt:25:9

    // TODO: Fails when nested further down (AuthenticationStateMachine.kt:129:29)
    //  if (BuildConfig.DEBUG_LOG) {
    //      TLog.d(TAG, "AUTHORIZED_STATE: processing: $response")
    //      (response as? ErrorResponse)?.let {
    //          TLog.e("Received ErrorResponse from pump, request code: ${it.requestCode}, error code: ${it.errorCode}")
    //      }
    //  }

    override fun visitElement(element: PsiElement) {

        // Do not consider Imports because they count as qualified expressions, giving false
        // Log statement finds
        if (element is KtImportDirective) {
        } else {
            //Testing... This finds the debug if clause
            if(element is KtIfExpression && element.text.contains(debugBuildRegex)) {
                println("Debug Found: ${element.text}, Type: $element")

                foundDebugWrappers.add(element.text)

                if(hasTLog(element.text)){
                    println("Has TLog!")
                }else{
                    println("Missing TLog!")
                    createIssue("Not using TLog!", element)
                }

            }

            // Loop through and should handles the base case due no more matching elements
            else if (element is KtDotQualifiedExpression && element.text.contains(logRegex)) {

//                val parent = element.parent.parent.parent

                foundLogStatements.add(element.text)

                println("Needs to be wrapped with Debug Check")
                println("PSI Element: ${element.text}, Type: $element")

                // finds the 'if (BuildConfig.DEBUG_LOG) { ... }'
//                println("PSI Parent: ${parent.text}, " +
//                        "Type: $parent")

                // if all of the found debug clauses contain the log statements
//                if(hasDebugWrapper(parent.text)){
//                    println("Has Debug Wrapper!")
//                }else{
//                    println("Missing Debug Wrapper!")
//                    createIssue("Does not contain the debug wrapper!", element)
//                }
//                if(hasTLog(parent.text)){
                if(hasTLog(element.text)){
                    println("Has TLog!")
                }else{
                    println("Missing TLog!")
                    createIssue("Not using TLog!", element)
                }
            }
            else {
                // Recurse through all of the children
                element.children.forEach {
                    visitElement(it)
                }
            }
        }
    }

    /**
     * Visits all of the elements in the Kotlin file. Checks if there are Logging statements
     * Creates a Detekt issue if missing a Log wrapper
     * Creates a Detekt issue if not using TLog
     * @param element The found KtElement
     */
//    override fun visitKtElement(element: KtElement) {
//        val logStatement = "Log."
//
////        if (element is KtClass) {
////            println("visitKtElement: ${element.text}, type: $element")
////        }
//
//
//        if (element is KtClass && element.text.contains(logStatement)) {
//            element.children.forEach {
//                // Uncomment println for debugging
//                println("PsiElement: ${it.text}")
//            }
//
//            if (!hasDebugWrapper(element.text)) {
//                // Uncomment println for debugging
//                println("Does not contain the debug wrapper! ${element.text}")
//                createIssue("Does not contain the debug wrapper!", element)
//            }
//
//            if (!hasTLog(element.text)) {
//                // Uncomment println for debugging
//                println("Not using TLog!")
//                createIssue("Not using TLog!", element)
//            }
//        }
//
//        if (hasPrintLn(element.text)) {
//            // Uncomment println for debugging
//            println("Contains println: ${element.text}!")
//            createIssue("Contains Println", element)
//        }
//    }

    /**
     * Checks the supplied text for a debug wrapper
     * @param suppliedText The text to check for the debug wrapper
     * @return Boolean ->
     * True: The supplied text does have a debug wrapper
     * False: The supplied text does not have a debug wrapper
     */
    private fun hasDebugWrapper(suppliedText: String): Boolean {
        val debugWrapper = "if (BuildConfig.DEBUG_LOG) {"
        if (suppliedText.contains(debugWrapper)) {
            return true
        }
        return false
    }

    /**
     * Checks the supplied text is using TLog.
     * @param suppliedText The text to check for TLog.
     * @return Boolean ->
     * True: The supplied text does contain TLog.
     * False: The supplied text does not contain TLog.
     */
    fun hasTLog(suppliedText: String): Boolean {
        val tLogStatement = "TLog."
        if (suppliedText.contains(tLogStatement)) {
            return true
        }
        return false
    }

    /**
     * Checks the supplied text is using println.
     * @param suppliedText The text to check println
     * @return Boolean ->
     * True: The supplied text does contain println.
     * False: The supplied text does not contain println.
     */
    fun hasPrintLn(suppliedText: String): Boolean {
        val printLnStatement = "println("
        if (suppliedText.contains(printLnStatement)) {
            return true
        }
        return false
    }

    /**
     * Reports a Detekt Issue with the supplied message and KtElement
     * @param message Detekt will print this message out in the report
     * @param element Detekt uses the KtELement to determine an exact location
     */
    private fun createIssue(message: String, element: KtElement) {
        report(
                CodeSmell(
                        issue, Entity.from(element), message = message
                )
        )
    }
}
