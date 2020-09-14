import java.io.IOException

/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * GetFileHistory.kt
 * @summary:
 * @author: Mitch Thornton Sep 11, 2020
 */

//Runtime.getRuntime().exec("powershell.exe -File d:\\gitHistory2.ps1")

var script = "echo Test"
//
//var script = "cd C:\\Users\\mthornton\\StudioProjects\\KTLintTest\\Rules\\src\\main\\scripts\n" +
//        "echo \"\$(git log -1 --format=\"%ad\" -- C:\\Users\\mthornton\\StudioProjects\\KTLintTest\\app\\src\\main\\java\\com\\example\\ktlinttest\\MainActivity.kt)\""

//Runtime.getRuntime().exec("powershell.exe -File d:\\C:\\Users\\mthornton\\StudioProjects\\KTLintTest\\Rules\\src\\main\\scripts\\gitHistory2.ps1")
Runtime.getRuntime().exec("powershell.exe -command echo test")

//Runtime.getRuntime().exec("ls")

//runCommand("ls")

//@Throws(IOException::class)
//fun runCommand(command: String?): Int {
//    var returnValue = -1
//    try {
//        val process = Runtime.getRuntime().exec(command)
//        process.waitFor()
//        returnValue = process.exitValue()
//    } catch (e: InterruptedException) {
//        e.printStackTrace()
//    }
//    return returnValue
//}