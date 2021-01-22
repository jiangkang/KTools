dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerView)
    testImplementation(junit)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":tools"))
}