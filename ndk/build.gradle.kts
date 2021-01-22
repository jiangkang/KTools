android {
    ndkVersion = vNdkVersion
    externalNativeBuild {
        cmake {
            version = "3.10.2"
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("./libs/arm64-v8a")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.appcompat)
    implementation(AndroidX.cardView)
    implementation(AndroidX.Lifecycle.runtime)
    implementation(AndroidX.Lifecycle.extension)
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(project(":tools"))
}