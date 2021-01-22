dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.appcompat)
    implementation(AndroidX.Lifecycle.extension)
    testImplementation(junit)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    fresco()
//    glide()
    implementation(kotlin("stdlib-jdk7"))
    implementation(AndroidX.constraintLayout)
}
