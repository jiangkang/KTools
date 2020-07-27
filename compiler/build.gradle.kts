plugins {
    id("java-library")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":annotations"))
    implementation("com.google.auto:auto-common:0.10")
    //帮助我们通过类调用的形式来生成代码
    implementation("com.squareup:javapoet:1.11.1")
    //注解 processor 类，并对其生成 META-INF 的配置信息
    implementation("com.google.auto.service:auto-service:1.0-rc6")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("org.apache.commons:commons-collections4:4.4")
}

repositories {
    jcenter()
    mavenCentral()
    google()
}