buildscript {
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.greenrobot:greendao-gradle-plugin:3.2.2")
        classpath(kotlin("gradle-plugin", version = "1.4.21"))
        classpath(Google.hiltPlugin)
    }
}

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
    }
}

val moduleList = listOf(
        "annotations", "buildSrc", "compiler", "klint"
)


subprojects {
    if (moduleList.contains(project.name)) {
        return@subprojects
    }
    val isAppModule = project.name == "app"
    // caution: 这里的apply并不是Kotlin中的apply方法，而是Project对象用于注册插件的方法
    project.apply {
        if (isAppModule) {
            plugin("com.android.application")
        } else {
            plugin("com.android.library")
        }
        plugin("kotlin-android")
        plugin("kotlin-kapt")
//        plugin("org.jetbrains.kotlin.plugin.parcelize")
    }
    if (isAppModule) {
        project.extensions.configure<com.android.build.gradle.internal.dsl.BaseAppModuleExtension>("android") {
            configAndroidExtension(this, project)
        }
    } else {
        project.extensions.configure<com.android.build.gradle.LibraryExtension>("android") {
            configAndroidExtension(this, project)
        }
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}


tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "6.5.1"
}


fun configAndroidExtension(android: com.android.build.gradle.BaseExtension, project: Project) {
    val isAppModule = project.name == "app"
    android.apply {
        compileSdkVersion(vCompileSdkVersion)
        buildToolsVersion(vBuildToolsVersion)
        defaultConfig {
            if (isAppModule) {
                applicationId = "com.jiangkang.ktools"
            }
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

            javaCompileOptions {
                annotationProcessorOptions {
                    arguments(mapOf("moduleName" to project.project.name))
                }
            }
        }
        buildTypes {
            getByName("release") {
                isMinifyEnabled = true
                isShrinkResources = false
                proguardFiles(android.getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            }
        }
        configurations.all {
            resolutionStrategy.force("com.google.code.findbugs:jsr305:3.0.1")
            exclude("com.google.guava", "listenablefuture")
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        buildFeatures.viewBinding = true
    }
}