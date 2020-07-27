import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

dependencies {
//    implementation(gradleApi())
    implementation("com.android.tools.build:gradle:4.0.0")
    implementation(kotlin("gradle-plugin", version = "1.3.72"))
//    implementation(gradleApi())
//    implementation(localGroovy())
//    implementation("org.ow2.asm:asm:7.1")
//    implementation("javassist:javassist:3.12.1.GA")
    implementation(kotlin("stdlib-jdk8"))
}

sourceSets {
    getByName("main") {
        java.srcDirs("src/main/kotlin")
    }
}

repositories {
    mavenCentral()
    jcenter()
    google()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}