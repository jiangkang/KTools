plugins {
    id("java-library")
}


dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //官方给出的API,这里必须用compileOnly，使用implementation和api都会报错
    compileOnly("com.android.tools.lint:lint-api:27.1.2")

    //已有的检查
    compileOnly("com.android.tools.lint:lint-checks:27.1.2")
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Lint-Registry-v2"] = "com.jiangkang.klint.KLintRegistry"
    }
}

