println("---------start:settings.gradle.kt---------------")
include(":video")
include(":audio")
include(
        ":app",
        ":tools",
        ":annotations",
        ":storage",
        ":hack",
        ":widget",
        ":compiler",
        ":hybrid",
        ":design",
        ":image",
        ":klint",
        ":kanimation",
        ":container",
        ":media",
        ":ndk",
        ":vpn",
        ":sensor",
        ":bluetooth",
        ":nfc"
)
println("project name : ${rootProject.name}")
gradle.addBuildListener(object: BuildListener{
    override fun buildStarted(gradle: Gradle) {
        println("--------buildStarted------------")
    }

    override fun settingsEvaluated(settings: Settings) {
        println("--------settingsEvaluated------------")
    }

    override fun projectsLoaded(gradle: Gradle) {
        println("--------projectsLoaded------------")
    }

    override fun projectsEvaluated(gradle: Gradle) {
        println("--------projectsEvaluated------------")
    }

    override fun buildFinished(result: BuildResult) {
        println("---------buildFinished-----------")
    }
})

gradle.beforeSettings {
    println("---------beforeSettings : single-----------")
}
gradle.settingsEvaluated {
    println("---------settingsEvaluated : single-----------")
}
gradle.projectsLoaded {
    println("---------projectsLoaded : single-----------")
}
gradle.projectsEvaluated {
    println("---------projectsEvaluated : single-----------")
}
gradle.buildStarted {
    println("---------buildStarted : single-----------")
}
gradle.buildFinished {
    println("---------buildFinished : single-----------")
}
sourceControl.vcsMappings
println("---------end:settings.gradle.kt---------------")