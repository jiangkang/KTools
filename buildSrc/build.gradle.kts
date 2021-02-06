import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle:4.1.2")
    implementation(kotlin("gradle-plugin", version = "1.4.21"))
//    implementation(gradleApi())
//    implementation(localGroovy())
//    implementation("org.ow2.asm:asm:7.1")
//    implementation("javassist:javassist:3.12.1.GA")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.21")
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
    jvmTarget = "1.7"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.7"
}