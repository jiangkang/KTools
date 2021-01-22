android {
    defaultConfig {
        sourceSets {
            getByName("main").manifest.srcFile("src/main/manifest/lib/AndroidManifest.xml")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constraintLayout)
    testImplementation(junit)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(project(":tools"))
    implementation(project(":widget"))
    implementation(kotlin("stdlib-jdk7"))
    lintChecks(project(":klint"))
}
