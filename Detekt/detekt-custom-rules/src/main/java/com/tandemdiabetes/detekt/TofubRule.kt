/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * TofubRule.kt
 * Custom Detekt Rule that searches the specified directory to check if TOFUB's are correct
 * @author: Mitch Thornton Nov 04, 2020
 */

package com.tandemdiabetes.detekt

import com.tandemdiabetes.detekt.Constants.TOFUB_REGEX_DESCRIPTION
import com.tandemdiabetes.detekt.Constants.TOFUB_REGEX_PARAM
import com.tandemdiabetes.detekt.Constants.TOFUB_REGEX_RETURN
import com.tandemdiabetes.detekt.Constants.TOFUB_REGEX_THROWS
import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtNamedFunction

class TofubRule : Rule() {

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "This rule reports a file with an incorrect TOFUB",
        Debt.FIVE_MINS
    )

    /**
     * Visits each named function in the file and checks if the TOFUB matches our guidelines.
     * @param function Kotlin library PSI element that holds details concerning the function
     */
    override fun visitNamedFunction(function: KtNamedFunction) {
        val paramList = ArrayList<String>()
        val returnType = function.typeReference?.text
        val throwsList = ArrayList<String>()
        val catchesList = ArrayList<String>()

        function.getValueParameters().forEach {
            println("ValueParameter: ${it.firstChild.text}")
            paramList.add(it.firstChild.text)
        }

        function.bodyExpression?.children?.forEach {
            // TODO: Need to ignore comments.... Comments will most likely state something about a Throw
            if (it.text.contains("throw ")) {
                throwsList.add(it.text)
            }
            // TODO: Need to ignore comments.... Comments will most likely state something about a catch
            if (it.text.contains("catch")) {
                catchesList.add(it.text)
            }
        }

        // Build the TOFUB Regex
        val builtRegex = buildExpectedTOFUBRegex(paramList, returnType, throwsList, catchesList)
        println("visitNamedFunction: BuiltRegex: $builtRegex")

        // Go through the function's retrieved KDoc and check if it matches the built regex
        function.docComment?.text?.run {
            println("Found KDoc: \n$this")

            // Grab the TOFUB Values
            // TODO: 11/16/2020 Will use this for auto-correcting TOFUB's in the future
//            retrieveTOFUB(this)

            // See if the current KDoc is correct
            builtRegex.matchEntire(this)?.run {
                println("TOFUB MATCHES Generated!")
            } ?: createIssue("TOFUB Does Not Match Expected", function)
        } ?: createIssue("No TOFUB Found!", function)
    }

    /**
     * Builds a Regex based on the data retrieved from the KtNamedFunction
     * @param parameterList Parameters fetched from the KtNamedFunction
     * @param returnType The return type fetched from the KtNamedFunction... ex: Int
     * @param throwsList Throws fetched from the function contents
     * @param catchesList Catches fetched from the function contents
     * @return The Regex that is used to check if the TOFUB matches the function and contents
     */
    private fun buildExpectedTOFUBRegex(
        parameterList: ArrayList<String>?,
        returnType: String?,
        throwsList: ArrayList<String>?,
        catchesList: ArrayList<String>?
    ): Regex {
        // Build the TOFUB
        val expectedTOFUB = TofubBuilder()

        parameterList?.run {
            expectedTOFUB.addParameters(this)
        }

        returnType?.run {
            expectedTOFUB.addReturn(this)
        }

        throwsList?.run {
            expectedTOFUB.addThrows(this)
        }

        catchesList?.run {
            expectedTOFUB.addCatches(this)
        }

        return expectedTOFUB.build()
    }

    /**
     * Creates the TOFUB object based on the retrieved KDoc
     * @param tofubString
     * @return Tofub? -> Tofub if !null | null if null
     */
    private fun retrieveTOFUB(tofubString: String): Tofub? {
        val descriptionKDoc = retrieveDescription(tofubString)
        val paramsKDoc = retrieveParams(tofubString)
        val returnKDoc = retrieveReturn(tofubString)
        val throwsKDoc = retrieveThrows(tofubString)

        if (descriptionKDoc != null) {
            return Tofub(
                descriptionKDoc,
                paramsKDoc,
                returnKDoc,
                throwsKDoc
            )
        }
        return null
    }

    /**
     * Takes the supplied String and retrieves a list of strings that matches
     * [TOFUB_REGEX_DESCRIPTION]
     * @param kDocComment the supplied TOFUB
     * @return ArrayList<String>? Each line of a description creates a new item to the list.
     * If there is no description found, returns null
     */
    private fun retrieveDescription(kDocComment: String): ArrayList<String>? {
        val returnKDoc = ArrayList<String>()

        TOFUB_REGEX_DESCRIPTION.findAll(kDocComment).forEach {
            returnKDoc.add(it.value)
        }

        if (returnKDoc.size > 0) {
            return returnKDoc
        }
        return null
    }

    /**
     * Takes the supplied String and retrieves a list of strings that matches
     * [TOFUB_REGEX_PARAM]
     * @param kDocComment the supplied TOFUB
     * @return ArrayList<String>? Each param found adds a new item to the list.
     * If there is no parameters found, returns null
     */
    private fun retrieveParams(kDocComment: String): ArrayList<String>? {
        val paramsKDoc = ArrayList<String>()

        TOFUB_REGEX_PARAM.findAll(kDocComment).forEach {
            paramsKDoc.add(it.value)
        }

        if (paramsKDoc.size > 0) {
            return paramsKDoc
        }
        return null
    }

    /**
     * Takes the supplied String and retrieves a list of strings that matches
     * [TOFUB_REGEX_RETURN]
     * @param kDocComment the supplied TOFUB
     * @return String? The found Return type, otherwise, returns null
     */
    private fun retrieveReturn(kDocComment: String): String? {
        TOFUB_REGEX_RETURN.find(kDocComment)?.let {
            return it.value
        }
        return null
    }

    /**
     * Takes the supplied String and retrieves a list of strings that matches
     * [TOFUB_REGEX_THROWS]
     * @param kDocComment the supplied TOFUB
     * @return ArrayList<String>? Each throw found adds a new item to the list.
     * If there is no throws found, returns null
     */
    private fun retrieveThrows(kDocComment: String): ArrayList<String>? {
        val throwsKDoc = ArrayList<String>()

        TOFUB_REGEX_THROWS.findAll(kDocComment).forEach {
            throwsKDoc.add(it.value)
        }

        if (throwsKDoc.size > 0) {
            return throwsKDoc
        }
        return null
    }

    /**
     * Creates the Detekt Issue
     * @param message The String that you want Detekt to report
     * @param function The PSI element that Detekt requires in order to create a report
     */
    private fun createIssue(message: String, function: KtNamedFunction) {
        println("$message : ${function.name}")
        report(
            CodeSmell(
                issue, Entity.from(function),
                message = message
            )
        )
    }
}
