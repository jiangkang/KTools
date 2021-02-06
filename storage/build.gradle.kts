dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constraintLayout)
    testImplementation(junit)
    implementation(AndroidX.recyclerView)
    okHttp()
    implementation(Kotlin.stdlib)
    implementation(project(":tools"))
}