dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constraintLayout)
    testImplementation(junit)
    implementation(AndroidX.recyclerView)
    okHttp()
    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":tools"))
}