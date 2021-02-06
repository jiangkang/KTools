dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.cardView)
    implementation(kotlin("stdlib-jdk7"))
    implementation(Kotlin.coroutines)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(AndroidX.Lifecycle.runtime)
    implementation(AndroidX.Lifecycle.extension)
    implementation(project(":tools"))
}