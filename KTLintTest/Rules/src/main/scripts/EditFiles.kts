import java.io.File

/**
 * Copyright Tandem Diabetes Care, Inc. 2020.  All rights reserved.
 * EditFiles.kt
 * @summary:
 * @author: Mitch Thornton Sep 11, 2020
 *
 * NOTES
 * '.' --> Gives the current directory
 */



//fun replaceText(){
//
//    var filePath = "C:\\Users\\mthornton\\StudioProjects\\KTLintTest\\Rules\\src\\main\\java\\Test.kt"
//
//    try {
//        val inputStream:InputStream = assets.open(filePath)
//        val text = inputStream.bufferedReader().use{it.readText()}
//
//        println(text)
//        text = text.replace("Copyright".toRegex(), "REPLACED!")
//        f.writeText(text)
//    }catch (e:Exception){
//        Log.d(TAG, e.toString())
//    }
//}
//
//replaceText()

//fun printDirs() {
////    val folders: Array<File>? = File(".").listFiles { file -> file.isDirectory }
//    val folders: Array<File>? = File("C:\\Users\\mthornton\\StudioProjects\\KTLintTest\\app").listFiles { file -> file.isDirectory }
//    folders?.forEach { folder ->
//        println(folder.absolutePath)
//    }
//}
//
//printDirs()

fun File.printPathAndSubdirs() {
    println(path)
    listFiles { file -> file.isDirectory }?.forEach {
        it.printPathAndSubdirs()
    }
}

File("C:\\Users\\mthornton\\StudioProjects\\KTLintTest\\app").printPathAndSubdirs()

// ----------------------------------------------------
// $ kotlinc -script dirsExploreRecursionExtension.kts
//
// .
// ./dir2
// ./dir2/dir2.1
// ./dir2/dir2.2
// ./dir1