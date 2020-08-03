package plugins.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

abstract class VerifyManifestTask : DefaultTask(){

    @get:InputFile
    abstract val mergedManifest:RegularFileProperty

    @TaskAction
    fun taskAction(){
        val mergedManifestFile = mergedManifest.get().asFile
        println("-----------------------------------------------")
        println(mergedManifestFile)
        println("-----------------------------------------------")
    }


}