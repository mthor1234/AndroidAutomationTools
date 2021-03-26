/*
 * Copyright Tandem Diabetes, Inc. 2020 All rights reserved.
 */

package com.tandemdiabetes.detekt

import com.tandemdiabetes.detekt.Constants.COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_ISSUE_CREATION_DATE_DOESNT_MATCH
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_ISSUE_FILE_NAME_DOESNT_MATCH
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_ISSUE_TOFIB_DOESNT_MATCH
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_ISSUE_UPDATE_LINE_DOESNT_MATCH
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_REGEX_AUTHOR_LINE
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_REGEX_CREATION_DATE
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_REGEX_DESCRIPTION
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_REGEX_FIRST_LAST_NAME
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_REGEX_TOFIB
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_REGEX_UPDATE_LINE
import com.tandemdiabetes.detekt.Constants.COPYRIGHT_REGEX_YEAR_SPAN
import com.tandemdiabetes.detekt.Constants.MIN_NUMBER_OF_TOFIB_LINES
import com.tandemdiabetes.detekt.Constants.fileNameRegex
import com.tandemdiabetes.detekt.Constants.tofibDescriptionIndicesFromTheEnd
import com.tandemdiabetes.detekt.Constants.tofibDescriptionStartIndex
import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtFile

/**
 * TOFIBRule.kt
 * Custom Detekt Rule that searches the specified directory to check if TOFIB's are correct
 * @author Mitch Thornton Feb 28, 2018
 */
class TofibRule : Rule() {


    private lateinit var currentFile: KtFile
    private lateinit var currentFileName: String

    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "This rule reports a file with an incorrect TOFIB.",
        Debt.FIVE_MINS
    )

    /**
     * Looks at the Kotlin file and checks if the TOFIB is correct
     * @param file The supplied Kotlin file
     */
    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)
        currentFile = file
        currentFileName = file.name
        crudeTOFIBCheck(file.text)
    }

    /**
     * Takes in the TOFIB text and checks if it matches [COPYRIGHT_REGEX_TOFIB]
     * Creates a Detekt Issue if there is not match
     * @param tofibText The supplied TOFIB String
     * @return Boolean -> True if the TOFIB matches | False if the TOFIB does not match
     */
    fun crudeTOFIBCheck(tofibText: String): Boolean {
        println("crudeTOFIBCheck(): $tofibText")
        if (COPYRIGHT_REGEX_TOFIB.find(tofibText) == null) {
            createIssue(COPYRIGHT_ISSUE_TOFIB_DOESNT_MATCH)
            return false
        }
        return true
    }

    /**
     * Takes in the TOFIB text and checks if the update line is correct
     * @param tofibText The supplied TOFIB String
     * @return Boolean -> Uses [COPYRIGHT_REGEX_UPDATE_LINE] for matching
     * True if match found
     * False if no match
     */
    fun checkUpdateLine(tofibText: String): Boolean {
        println("checkUpdateLine() : \n$tofibText")
        COPYRIGHT_REGEX_UPDATE_LINE.find(tofibText)?.let {
            println("checkUpdateLine() Success")
            return true
        } ?: createIssue(COPYRIGHT_ISSUE_UPDATE_LINE_DOESNT_MATCH)

        return false
    }

    /**
     * Takes in the TOFIB text and checks if the file name is correct
     * @param tofibText The supplied TOFIB String
     * @return Boolean -> Uses [COPYRIGHT_ISSUE_FILE_NAME_DOESNT_MATCH] for matching
     * True if match found
     * False if no match
     */
    fun checkFileName(tofibText: String): Boolean {
        println("checkFileName(): \n$tofibText")

        fileNameRegex(javaClass.simpleName).find(tofibText)?.let {
            println("checkFileName() Success")
            return true
        } ?: createIssue(COPYRIGHT_ISSUE_FILE_NAME_DOESNT_MATCH)
        println("checkFileName() FAILURE")
        return false
    }

    /**
     * Takes in the TOFIB text and checks if the description is valid
     * Splits the TOFIB by lines. Ensures the description falls in the correct index and is valid
     * Reports an issue if the TOFIB does not meet the minimum number of lines
     * @param tofibText The supplied TOFIB String
     * @return Boolean -> True if the description is valid | False if it is not valid
     */
    fun checkTOFIBForDescription(tofibText: String): Boolean {
        println("checkTOFIBForDescription(): \n$tofibText")

        tofibText.split("\n").let { tofibLines: List<String> ->

            val descriptionEndIndex = tofibLines.size - tofibDescriptionIndicesFromTheEnd

            if (tofibLines.size >= MIN_NUMBER_OF_TOFIB_LINES) {
                // Description starts at line 3 but does not have a limit on how long it can be
                // Description falls in between FileName and Author
                // Author should occur - 1 from the end
                // Use index 3 to indicate starting index of the File Description
                // Use index .size - 2  to indicate end index of the File Description
                for (lineNumber in tofibDescriptionStartIndex..descriptionEndIndex) {
                    val line = tofibLines.get(lineNumber)

                    println("checkTOFIBForDescription(): \n lineNumber: $lineNumber\n$line")
                    // If there is an issue found, stop the loop & return false
                    if (!checkIfValidDescriptionLine(line)) {
                        return false
                    }
                }
                // Entire Description has been processed with no issues, return true
                return true
            } else {
                createIssue(COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES)
                println("checkDescription(): $COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES")
                return false
            }
        }
    }

    /**
     * Takes in a String and determines if it is a valid description
     * Reports an issue if the line is incorrect
     * @param descriptionLine The supplied description String
     * @return Boolean -> True if the description is valid | False if it is not valid
     */
    fun checkIfValidDescriptionLine(descriptionLine: String): Boolean {
        println("checkIfValidDescriptionLine(): \n$descriptionLine")

        COPYRIGHT_REGEX_DESCRIPTION.find(descriptionLine)?.let {
            println("checkIfValidDescriptionLine(): SUCCESS")
            return true
        } ?: createIssue(COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH)

        println("checkIfValidDescriptionLine(): One $COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH")
        return false
    }

    /**
     * Takes in the TOFIB text and checks if the author line is correct
     * Reports an issue if the line is incorrect
     * @param tofibText The supplied TOFIB String
     * @return Boolean -> Uses [COPYRIGHT_REGEX_AUTHOR_LINE] for matching
     * True if match found
     * False if no match
     */
    fun checkAuthorLine(tofibText: String): Boolean {
        println("checkAuthorLine(): \n$tofibText")

        COPYRIGHT_REGEX_AUTHOR_LINE.find(tofibText)?.let {
            println("checkAuthorLine() Success")

            return true
        } ?: createIssue(COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH)

        return false
    }

    /**
     * Takes in the TOFIB text and checks if the year range is correct
     * Reports [COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH] if the year range is incorrect
     * @param tofibText The supplied TOFIB String
     * @return Boolean -> Uses [COPYRIGHT_REGEX_YEAR_SPAN] for matching
     * True if match found
     * False if no match
     */
    fun parseYearRange(primaryCopyrightString: String): Array<String?> {
        var creationYear: String? = null
        var updatedYear: String? = null
        val years = arrayOfNulls<String>(2)

        COPYRIGHT_REGEX_YEAR_SPAN.find(primaryCopyrightString)?.let {
            val tokenizedYears = it.value.split("-")

            when (tokenizedYears.size) {
                0 -> createIssue(COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH)
                1 -> creationYear = tokenizedYears.get(0)
                2 -> {
                    creationYear = tokenizedYears.get(0)

                    // TODO: 10/26/20 If I add this to the diff only files, we should always set to the current year
                    updatedYear = tokenizedYears.get(1)
                }
            }
            years[0] = creationYear
            years[1] = updatedYear
        } ?: run {
            println("parseYearRange() : Couldn't find year range!")
            createIssue(COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH)
        }
        return years
    }

    /**
     * Takes in the TOFIB text and attempts to retrieve the author
     * Reports [COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH] if the portion is incorrect
     * @param tofibText The supplied TOFIB String
     * @return String? -> Uses [COPYRIGHT_REGEX_FIRST_LAST_NAME] for matching
     * String if there is a match
     * null if no match
     */
    fun parseAuthor(tofibText: String): String? {
        return COPYRIGHT_REGEX_FIRST_LAST_NAME.find(tofibText)?.value ?: run {
            createIssue(COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH)
            return null
        }
    }

    /**
     * Takes in the TOFIB text and attempts to retrieve the author
     * Reports [COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH] if the portion is incorrect
     * @param tofibText The supplied TOFIB String
     * @return String? -> Uses [COPYRIGHT_REGEX_FIRST_LAST_NAME] for matching
     * String if there is a match
     * null if no match
     */
    fun parseCreationDate(tofibText: String): String? {
        return COPYRIGHT_REGEX_CREATION_DATE.find(tofibText)?.value ?: run {
            createIssue(COPYRIGHT_ISSUE_CREATION_DATE_DOESNT_MATCH)
            return null
        }
    }

    /**
     * Reports a Detekt Issue with the supplied message
     * @param message Detekt will print this message out in the report
     */
    fun createIssue(message: String) {
        report(
            CodeSmell(
                issue, Entity.atPackageOrFirstDecl(currentFile),
                message = message
            )
        )
    }
}
