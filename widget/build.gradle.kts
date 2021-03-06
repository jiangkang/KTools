dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.cardView)
    implementation(Kotlin.stdlib)
    implementation(Kotlin.coroutines)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(AndroidX.Lifecycle.runtime)
    implementation(AndroidX.Lifecycle.extension)
    implementation(project(":tools"))
}