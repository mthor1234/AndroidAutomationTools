/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * AutoTAGs.kt
 * @summary:
 * @author: Mitch Thornton Aug 26, 2020
 */

import java.io.File

val projectDirectory = "C:\\Users\\mthornton\\StudioProjects\\mpp\\app\\src\\main\\java\\com\\tandemdiabetes"

/**
 * Kotlin Example to traverse directory and its contents
 */
// using extension function walkTopDown\
//loopThroughFileDirectory()
//getAllTAGs()
printNumberOfTags()

fun loopThroughFileDirectory() {
// using extension function walk
    File(projectDirectory).walk().forEach { file: File ->

        if (!file.isDirectory && file.extension == "kt") {

            file.forEachLine { line: String ->
                if (line.contains("TAG =")) {
//                 print("${file.nameWithoutExtension} | ")
                 println( (line.substringAfter("TAG = \"").substringBefore("\"")))
                }
            }
        }
    }
}


fun getAllTAGs() : ArrayList<String> {

 val listOfTags = arrayListOf<String>()

// using extension function walk
 File(projectDirectory).walk().forEach { file: File ->

  if (!file.isDirectory && file.extension == "kt") {

   file.forEachLine { line: String ->
    if (line.contains("TAG =")) {
     val tag = (line.substringAfter("TAG = \"").substringBefore("\""))
     println(tag)
     listOfTags.add(tag)
    }
   }
  }
 }
 return listOfTags
}


fun printNumberOfTags(){
 println("Number of Tags: ${getAllTAGs().size}")

}
