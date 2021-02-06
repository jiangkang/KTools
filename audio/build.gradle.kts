//plugins {
//    id("org.jetbrains.kotlin.plugin.parcelize") version ("1.4.30-RC")
//}

dependencies {
    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerView)
    implementation(Google.oboe)
    testImplementation(junit)
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(Kotlin.stdlib)
    implementation(project(":tools"))
}