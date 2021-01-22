dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.print)
    androidTestImplementation(AndroidX.espressoCore) {
        exclude("com.android.support","support-annotations")
    }
    testImplementation(junit)
    testImplementation(mockito)
    implementation(zxing)
    okHttp()
    glide()
    implementation(AndroidX.annotation)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.Lifecycle.runtime)
    implementation(AndroidX.Lifecycle.extension)
    implementation(Kotlin.coroutines)
    implementation(kotlin("stdlib-jdk7"))
}
