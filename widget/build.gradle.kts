dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.cardView)
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(AndroidX.Lifecycle.runtime)
    implementation(AndroidX.Lifecycle.extension)
    implementation(project(":tools"))
}