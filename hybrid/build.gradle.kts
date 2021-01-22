dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.appcompat)
    testImplementation(junit)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":tools"))
    implementation(gson)
    implementation(Kotlin.coroutines)
}