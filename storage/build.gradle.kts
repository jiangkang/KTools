plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
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
            abiFilters("arm64-v8a")
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(mapOf("moduleName" to project.name))
            }
        }

        sourceSets {
            getByName("main").manifest.srcFile("src/main/manifest/lib/AndroidManifest.xml")
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

    implementation(AndroidX.appcompat)
    implementation(AndroidX.constraintLayout)
    testImplementation(junit)

    implementation(AndroidX.recyclerView)
    implementation("com.jakewharton:butterknife:10.2.0")
    kapt("com.jakewharton:butterknife-compiler:10.2.0")

    implementation("com.squareup.okhttp3:okhttp:4.4.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.2.0")
    implementation("com.squareup.okhttp3:mockwebserver:4.2.0")
    implementation(kotlin("stdlib-jdk7"))
    implementation("org.jetbrains.anko:anko:0.10.8")
}

repositories {
    jcenter()
    google()
    mavenCentral()
}