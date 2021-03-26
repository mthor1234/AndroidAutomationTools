/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * LoggingRuleTest.kt
 * Tests LoggingRule.kt
 * @author: Mitch Thornton Nov 02, 2020
 */

package com.tandemdiabetes.detekt

import io.gitlab.arturbosch.detekt.test.assertThat
import io.gitlab.arturbosch.detekt.test.lint
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class LoggingRuleTest {

    val fileStringWithNoLogStatement = "    /**\n" +
        "     * Unregisters our callback from the Android framework\n" +
        "     * Useful because unregistration isn't guaranteed by the Android OS.\n" +
        "     * Should call this if our app is being killed\n" +
        "     */\n" +
        "    fun unregisterCallback() {\n" +
        "        try {\n" +
        "            connectivityManager.unregisterNetworkCallback(networkCallback)\n" +
        "        } catch (e: Exception) {\n" +
        " //       }\n" +
        "    } //unregisterCallback\n"

    val fileStringNoWrapper = "    /**\n" +
        "     * Unregisters our callback from the Android framework\n" +
        "     * Useful because unregistration isn't guaranteed by the Android OS.\n" +
        "     * Should call this if our app is being killed\n" +
        "     */\n" +
        "    fun unregisterCallback() {\n" +
        "        try {\n" +
        "            connectivityManager.unregisterNetworkCallback(networkCallback)\n" +
        "        } catch (e: Exception) {\n" +
        "            TLog.d(TAG, \"NetworkCallback for Wi-fi was not registered or already unregistered\")\n" +
        "        }\n" +
        "    } //unregisterCallback\n" +
        " /**\n" +
        " * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.\n" +
        " * LogWrapperRule.kt\n" +
        " * @summary:\n" +
        " * @author: Mitch Thornton Nov 02, 2020\n" +
        " */"

    val fileStringWithWrapper = "    /**\n" +
        "     * Unregisters our callback from the Android framework\n" +
        "     * Useful because unregistration isn't guaranteed by the Android OS.\n" +
        "     * Should call this if our app is being killed\n" +
        "     */\n" +
        "    fun unregisterCallback() {\n" +
        "        try {\n" +
        "            connectivityManager.unregisterNetworkCallback(networkCallback)\n" +
        "        } catch (e: Exception) {\n" +
        "           if (BuildConfig.DEBUG_LOG) {\n" +
        "               TLog.d(TAG, \"NetworkCallback for Wi-fi was not registered or already unregistered\")\n" +
        "           }\n" +
        "        }\n" +
        "    } //unregisterCallback\n"

    val fileStringWithCommentedWrapper = "    /**\n" +
        "     * Unregisters our callback from the Android framework\n" +
        "     * Useful because unregistration isn't guaranteed by the Android OS.\n" +
        "     * Should call this if our app is being killed\n" +
        "     */\n" +
        "    fun unregisterCallback() {\n" +
        "        try {\n" +
        "            connectivityManager.unregisterNetworkCallback(networkCallback)\n" +
        "        } catch (e: Exception) {\n" +
        " //          if (BuildConfig.DEBUG_LOG) {\n" +
        "               TLog.d(TAG, \"NetworkCallback for Wi-fi was not registered or already unregistered\")\n" +
        "           }\n" +
        " //       }\n" +
        "    } //unregisterCallback\n"

    val fileStringWithCommentedLogStatement = "    /**\n" +
        "     * Unregisters our callback from the Android framework\n" +
        "     * Useful because unregistration isn't guaranteed by the Android OS.\n" +
        "     * Should call this if our app is being killed\n" +
        "     */\n" +
        "    fun unregisterCallback() {\n" +
        "        try {\n" +
        "            connectivityManager.unregisterNetworkCallback(networkCallback)\n" +
        "        } catch (e: Exception) {\n" +
        " //          if (BuildConfig.DEBUG_LOG) {\n" +
        " //              TLog.d(TAG, \"NetworkCallback for Wi-fi was not registered or already unregistered\")\n" +
        " //          }\n" +
        " //       }\n" +
        "    } //unregisterCallback\n"

    val fileStringWithLogDStatement = "    /**\n" +
        "     * Unregisters our callback from the Android framework\n" +
        "     * Useful because unregistration isn't guaranteed by the Android OS.\n" +
        "     * Should call this if our app is being killed\n" +
        "     */\n" +
        "    fun unregisterCallback() {\n" +
        "        try {\n" +
        "            connectivityManager.unregisterNetworkCallback(networkCallback)\n" +
        "        } catch (e: Exception) {\n" +
        "           if (BuildConfig.DEBUG_LOG) {\n" +
        "               Log.d(TAG, \"NetworkCallback for Wi-fi was not registered or already unregistered\")\n" +
        "           }\n" +
        " //       }\n" +
        "    } //unregisterCallback\n"

    val fileStringWithPrintStatement = "    /**\n" +
        "     * Unregisters our callback from the Android framework\n" +
        "     * Useful because unregistration isn't guaranteed by the Android OS.\n" +
        "     * Should call this if our app is being killed\n" +
        "     */\n" +
        "    fun unregisterCallback() {\n" +
        "        try {\n" +
        "            connectivityManager.unregisterNetworkCallback(networkCallback)\n" +
        "        } catch (e: Exception) {\n" +
        "           if (BuildConfig.DEBUG_LOG) {\n" +
        "               println(\"NetworkCallback for Wi-fi was not registered or already unregistered\")\n" +
        "           }\n" +
        " //       }\n" +
        "    } //unregisterCallback\n"

    val tLogLine = "            TLog.d(TAG, \"NetworkCallback for Wi-fi was not registered or already unregistered\")"
    val logLine = "            Log.d(TAG, \"NetworkCallback for Wi-fi was not registered or already unregistered\")"
    val printlnLine = "           println(Test Line)"
    val systemPrintlnLine = "           System.out.println(Test Line)"

    // ----- checkForLogStatement() ----- //
    @Test
    fun `LogWrapperRule()`() {
        val logWrapperRule = LoggingRule()

            val workingDir = Paths.get("").toAbsolutePath().toString()
            val testFileRelativeDir = "C:\\Users\\mthornton\\StudioProjects\\mpp\\app\\src\\main\\java\\com\\tandemdiabetes\\ble\\packetizer\\StreamPacketArrayList.kt"
            val testFileAbsDir = "$workingDir\\$testFileRelativeDir"
            val file = File(testFileRelativeDir)

        logWrapperRule.lint(file.toPath())
    }


    // ----- checkForLogStatement() ----- //
    @Test
    fun `LogWrapperRule() String Does NOT Include Debug Wrapper Expect One Finding`() {
        val logWrapperRule = LoggingRule()

        val findings = logWrapperRule.lint(
            """
            $fileStringNoWrapper
            """.trimIndent()
        )
        assertThat(findings).hasSize(1)
    }

    @Test
    fun `LogWrapperRule() String Does Include Debug Wrapper Expect No Findings`() {
        val logWrapperRule = LoggingRule()

        val findings = logWrapperRule.lint(
            """
            $fileStringWithWrapper
            """.trimIndent()
        )
        assertThat(findings).hasSize(0)
    }

    @Test
    fun `LogWrapperRule() COMMENTED OUT Debug Wrapper Expect No Finding`() {
        val logWrapperRule = LoggingRule()

        val findings = logWrapperRule.lint(
            """
            $fileStringWithCommentedWrapper
            """.trimIndent()
        )
        assertThat(findings).hasSize(0)
    }

    @Test
    fun `LogWrapperRule() COMMENTED OUT Log Statement Expect No Findings`() {
        val logWrapperRule = LoggingRule()

        val findings = logWrapperRule.lint(
            """
            $fileStringWithNoLogStatement
            """.trimIndent()
        )
        assertThat(findings).hasSize(0)
    }

    @Test
    fun `LogWrapperRule() Using LogD Expect One Finding`() {
        val logWrapperRule = LoggingRule()

        val findings = logWrapperRule.lint(
            """
            $fileStringWithLogDStatement
            """.trimIndent()
        )
        assertThat(findings).hasSize(1)
    }

    @Test
    fun `LogWrapperRule() No Log Statement Expect No Findings`() {
        val logWrapperRule = LoggingRule()

        val findings = logWrapperRule.lint(
            """
            $fileStringWithNoLogStatement
            """.trimIndent()
        )
        assertThat(findings).hasSize(0)
    }

    @Test
    fun `LogWrapperRule() PrintStatement Statement Expect One Finding`() {
        val logWrapperRule = LoggingRule()

        val findings = logWrapperRule.lint(
            """
            $fileStringWithPrintStatement
            """.trimIndent()
        )
        assertThat(findings).hasSize(1)
    }

    // ----- isUsingTLog() ----- //
    @Test
    fun `isUsingTLog() String Does NOT Use TLog Expect False`() {
        val logWrapperRule = LoggingRule()
        logWrapperRule.lint(
            """
            ${Constants.COPYRIGHT}
            """.trimIndent()
        )
        assertFalse(logWrapperRule.hasTLog(logLine))
    }

    @Test
    fun `isUsingTLog() String Does Use TLog Expect True`() {
        val logWrapperRule = LoggingRule()
        logWrapperRule.lint(
            """
            ${Constants.COPYRIGHT}
            """.trimIndent()
        )
        assertTrue(logWrapperRule.hasTLog(tLogLine))
    }

    // ----- isUsingPrintLn ----- //
    @Test
    fun `isUsingPrintLn() String Does Have PrintLn Expect True`() {
        val logWrapperRule = LoggingRule()
        logWrapperRule.lint(
            """
            ${Constants.COPYRIGHT}
            """.trimIndent()
        )
        assertTrue(logWrapperRule.hasPrintLn(printlnLine))
    }

    @Test
    fun `isUsingPrintLn() String Does Have SystemPrintLn Expect True`() {
        val logWrapperRule = LoggingRule()
        logWrapperRule.lint(
            """
            ${Constants.COPYRIGHT}
            """.trimIndent()
        )
        assertTrue(logWrapperRule.hasPrintLn(systemPrintlnLine))
    }

    @Test
    fun `isUsingPrintLn() String Does NOT Have PrintLn Expect True`() {
        val logWrapperRule = LoggingRule()
        logWrapperRule.lint(
            """
            ${Constants.COPYRIGHT}
            """.trimIndent()
        )
        assertTrue(logWrapperRule.hasPrintLn(systemPrintlnLine))
    }
}
