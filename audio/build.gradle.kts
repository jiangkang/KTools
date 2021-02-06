//plugins {
//    id("org.jetbrains.kotlin.plugin.parcelize") version ("1.4.30-RC")
//}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerView)
    implementation(Google.oboe)
    testImplementation(junit)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":tools"))
}