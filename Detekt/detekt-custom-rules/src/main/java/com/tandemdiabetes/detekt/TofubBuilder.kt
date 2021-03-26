/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * TofubBuilder.kt
 * Uses a Builder Pattern to create the TOFUB Regex
 * @author: Mitch Thornton Nov 06, 2020
 */

package com.tandemdiabetes.detekt

import java.lang.StringBuilder

class TofubBuilder {

    private val multiLineStartRegex = "^((?>\\/[\\*]{2})\\n)"
    private val multiLineEndRegex = "(?>[\\s]{5}\\*\\/)"
    private val descriptionRegex = "(((?>[\\s]{5}\\*[^@\\n]*+)\\n)++)"

    private val tofubRegexStrBuilder = StringBuilder("$multiLineStartRegex").append("$descriptionRegex")

    // TODO: 11/12/2020 Needs to support multiple line return, throws, and catches
    // TODO: 11/12/2020 Needs to check if this works with default params
    // TODO: 11/12/2020 Consider shorter expected characters after param, return, etc.

    /**
     * Adds the @param's lines to the REGEX
     * @param parameters The list of parameters to add to the Regex
     */
    fun addParameters(parameters: ArrayList<String>) {
        parameters.forEach { param ->
            // Look for @param and optionally... multiple lined @param's
            tofubRegexStrBuilder.append("((?>[\\s]{5}\\* @param $param.{10,}+)\\n)(((?>[\\s]{5}\\*[^@\\n]*+)\\n)++)?+")
        }
    }

    /**
     * Adds the @return to the REGEX
     * @param returnType The return type to add to the Regex
     */
    fun addReturn(returnType: String) {
        tofubRegexStrBuilder.append("((?>[\\s]{5}\\* @return $returnType.{10,})\\n)")
    }

    /**
     * Adds the @throw's lines to the REGEX
     * @param throws The list of throws to add to the Regex
     */
    fun addThrows(throws: ArrayList<String>) {
        repeat(throws.size) {
            tofubRegexStrBuilder.append("((?>[\\s]{5}\\* @throws.{10,})\\n)")
        }
    }

    /**
     * Adds the @catch's lines to the REGEX
     * @param catches The list of catches to add to the Regex
     */
    fun addCatches(catches: ArrayList<String>) {
        repeat(catches.size) {
            tofubRegexStrBuilder.append("((?>[\\s]{5}\\* @catch .{10,})\\n)")
        }
    }

    /**
     * Adds the @catch's lines to the REGEX
     * @param catches The list of catches to add to the Regex
     * @return Regex The built regex for checking the Tofub
     */
    fun build(): Regex {
        tofubRegexStrBuilder.append(multiLineEndRegex)
        return Regex(tofubRegexStrBuilder.toString(), RegexOption.MULTILINE)
    }
}
