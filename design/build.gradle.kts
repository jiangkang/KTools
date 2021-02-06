dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(AndroidX.appcompat)
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    testImplementation(junit)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(Kotlin.stdlib)

    implementation(AndroidX.constraintLayout)

    implementation(lottie)
    implementation(project(":tools"))

}
