dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.appcompat)
    implementation(AndroidX.core)
    implementation(AndroidX.constraintLayout)
    implementation("androidx.media2:media2-session:1.0.3")
    implementation("androidx.media2:media2-widget:1.0.3")
    implementation("androidx.media2:media2-player:1.0.3")
    testImplementation(junit)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(kotlin("stdlib-jdk7"))
}
