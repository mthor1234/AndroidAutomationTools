/**
 * Copyright Tandem Diabetes Care, Inc. 2020. All rights reserved.
 * CustomRuleSetProvider.kt
 * Rules that are intended to be 'active' are supplied to the RuleSet
 * @author Mitchell Thornton Feb 28, 2018
 */

package com.tandemdiabetes.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class CustomRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "custom-detekt-rules"

    /**
     * Specifies the rules that want to be used
     * @param config A configuration holds information about how to configure specific rules
     */
    override fun instance(config: Config) = RuleSet(ruleSetId, listOf(LoggingRule()))
}
