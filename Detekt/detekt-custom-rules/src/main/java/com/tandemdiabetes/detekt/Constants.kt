/**
 * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.
 * Constants.kt
 * Holds Constants used throughout Custom Rules Detekt.
 * @author Mitchell Thornton Feb 28, 2018
 */

package com.tandemdiabetes.detekt

object Constants {

    /* Used to check if the TOFIB is formatted correctly
     * 0 /**
     * 1 * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.
     * 2 * Constants.kt
     * 3 * Holds Constants used throughout Custom Rules Detekt.
     * 4 * @author Mitchell Thornton Feb 28, 2018
     * 5 */
     */
    const val tofibDescriptionStartIndex = 3 // Description will start 3 lines down from beginning
    const val tofibDescriptionIndicesFromTheEnd = 3 // Description will end 3 lines from tofib.size

    // Expected line where the description is supposed to start
    const val MIN_NUMBER_OF_TOFIB_LINES = 5

    const val COPYRIGHT =
        "/**\n" +
            " * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.\n" +
            " * TOFIBRule.kt\n" +
            " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
            " * @author Mitchell Thornton Feb 28, 2018\n" +
            " */"

    // ---- REGEX'S ---- //

    // COPYRIGHT Regex's Below //
    // /**
    val COPYRIGHT_REGEX_MULTILINE_START = Regex("^(/[\\*]{2})")

    // Multi-Line Comment End
    //  */
    val COPYRIGHT_REGEX_MULTILINE_END = Regex("( \\*\\/)")

    // Update Line
    val COPYRIGHT_REGEX_UPDATE_LINE =
        Regex(
            "(( \\* Copyright Tandem Diabetes Care, Inc.)" +
                "[\\s]([0-9]{4})([-]([0-9]{4}))?)(. All rights reserved.)"
        )

    // File Name
    val COPYRIGHT_REGEX_FILE_NAME = Regex("\\s(\\* .*\\.kt)")

    // Description
    val COPYRIGHT_REGEX_DESCRIPTION = Regex("( \\* (.{20,}))") // Starts with  * and has at least 20 characters

    // Author
    val COPYRIGHT_REGEX_AUTHOR = Regex("(( \\* @author )([A-Z][a-z]+([ ][A-Z][a-z]+)))")
    val COPYRIGHT_REGEX_FIRST_LAST_NAME = Regex("([A-Z][a-z]+([ ]?[A-Z][a-z]+))")
    val COPYRIGHT_REGEX_CREATION_DATE = Regex("([A-Z][a-z]{2} [0-2]?[0-9], [0-9]{4})")
    val COPYRIGHT_REGEX_AUTHOR_LINE =
        Regex("${COPYRIGHT_REGEX_AUTHOR.pattern} $COPYRIGHT_REGEX_CREATION_DATE")

    val COPYRIGHT_REGEX_YEAR_SPAN = Regex("([0-9]{4})([-]([0-9]{4}))?")

    // Entire TOFIB REGEX
    /**
     * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.
     * TOFIBRule.kt
     * This class has a group of functions that maps to Larcus Cloud API authentication.
     * @author Mitchell Thornton Feb 28, 2018
     */
    val COPYRIGHT_REGEX_TOFIB =
        Regex(
            "${COPYRIGHT_REGEX_MULTILINE_START.pattern}\n" +
                "${COPYRIGHT_REGEX_UPDATE_LINE.pattern}\n" +
                "${COPYRIGHT_REGEX_FILE_NAME.pattern}\n" +
                "(${COPYRIGHT_REGEX_DESCRIPTION.pattern}\n)*" +
                "${COPYRIGHT_REGEX_AUTHOR_LINE.pattern}\n" +
                "${COPYRIGHT_REGEX_MULTILINE_END.pattern}"
        )

    // ISSUES
    const val COPYRIGHT_ISSUE_TOFIB_DOESNT_MATCH =
        "TOFIB Does not Match Expected. \n" +
            "Ex: \"/**\n" +
            " * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.\n" +
            " * TOFIBRule.kt\n" +
            " * This class has a group of functions that maps to Larcus Cloud API authentication.\n" +
            " * @author Mitchell Thornton Feb 28, 2018\n" +
            " */\""

    const val COPYRIGHT_ISSUE_UPDATE_LINE_DOESNT_MATCH =
        "Update Line in TOFIB Does not Match Expected. \n" +
            "Ex: \"* Copyright Tandem Diabetes Care, Inc....\""

    const val COPYRIGHT_ISSUE_FILE_NAME_DOESNT_MATCH =
        "File name Does not Match Expected. \n" +
            "Ex: \"* TestFile.kt\""

    const val COPYRIGHT_ISSUE_DESCRIPTION_DOESNT_MATCH =
        "Description is not found or formatted properly. \n" +
            "Ex: \"* This is a test description. \""

    const val COPYRIGHT_ISSUE_AUTHOR_DOESNT_MATCH =
        "@author line is not found or formatted properly: \n" +
            "Ex: \"* @author Mitchell Thornton Feb 28, 2018\""

    const val COPYRIGHT_ISSUE_YEAR_RANGE_DOESNT_MATCH =
        "Something wrong with the year-range format. Please double check"

    const val COPYRIGHT_ISSUE_CREATION_DATE_DOESNT_MATCH =
        "Initial creation date is not found or formatted properly"

    const val COPYRIGHT_ISSUE_TOFIB_NUMBER_OF_LINES =
        "The TOFIB Does Not Have 5 or More Lines"

    /**
     * Builds the Regex for matching the file name in the TOFIB
     * @param className The name of the class
     * @return Regex for matching the file name in the TOFIB
     */
    fun fileNameRegex(className: String): Regex {
        return Regex("^\\s(\\* ${className}\\.kt)")
    }

    // TOFUB //
    val TOFUB_REGEX_MULTILINE_START = Regex("^(/[\\*]{2})")

    val TOFUB_REGEX_DESCRIPTION = Regex("(((?<=[\\s]?\\* )[^@|\\*]+))$", RegexOption.MULTILINE)
    val TOFUB_REGEX_PARAM = Regex("((?<=\\s\\* @param ).{20,})$", RegexOption.MULTILINE)
    val TOFUB_REGEX_RETURN = Regex("((?<=\\s\\* @return ).{15,})")
    val TOFUB_REGEX_THROWS = Regex("((?<=\\s\\* @throws ).{20,})+")

    val TOFUB_REGEX_DESCRIPTION_TWO = Regex("(([\\s]?\\* [^@]+)+)")
    val TOFUB_REGEX_PARAM_TWO = Regex("((\\s*\\* @param .{20,})*)$", RegexOption.MULTILINE)
    val TOFUB_REGEX_RETURN_TWO = Regex("((\\s*\\* @return .{15,})?)$", RegexOption.MULTILINE)
    val TOFUB_REGEX_THROWS_TWO = Regex("((\\s*\\* @throws .{20,})?)$", RegexOption.MULTILINE)

    val TOFUB_REGEX = Regex(
        (
            "${TOFUB_REGEX_DESCRIPTION_TWO.pattern}" +
                "${TOFUB_REGEX_PARAM_TWO.pattern}" +
                "${TOFUB_REGEX_RETURN_TWO.pattern}" +
                "${TOFUB_REGEX_THROWS_TWO.pattern}"
            ),
        RegexOption.MULTILINE
    )
}
