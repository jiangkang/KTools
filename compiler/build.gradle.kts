plugins {
    id("java-library")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":annotations"))
    implementation("com.google.auto:auto-common:0.11")
    //帮助我们通过类调用的形式来生成代码
    implementation(javapoet)
    //注解 processor 类，并对其生成 META-INF 的配置信息
    implementation("com.google.auto.service:auto-service:1.0-rc7")
    implementation("org.apache.commons:commons-lang3:3.11")
    implementation("org.apache.commons:commons-collections4:4.4")
}