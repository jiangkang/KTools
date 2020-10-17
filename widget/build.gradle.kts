plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlin-android")
}

android {

    //仅仅在编译的时候起作用，建议总是使用最新版本，值是一个API Level
    compileSdkVersion(vCompileSdkVersion)

    //构建工具的版本，在build-tools中的那些(aapt,dexdump,zipalign,apksigner)，一般是API-Level.x.x
    buildToolsVersion(vBuildToolsVersion)

    defaultConfig {
        minSdkVersion(vMinSdkVersion)
        targetSdkVersion(vTargetSdkVersion)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        ndk {
            abiFilters.add("arm64-v8a")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.cardView)
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    androidTestImplementation(AndroidX.testRunner)
    androidTestImplementation(AndroidX.espressoCore)
    implementation(AndroidX.Lifecycle.runtime)
    implementation(AndroidX.Lifecycle.extension)
    implementation(project(":tools"))
}