plugins {
    id("java-library")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.annotation)
}
repositories {
    jcenter()
    google()
    mavenCentral()
}