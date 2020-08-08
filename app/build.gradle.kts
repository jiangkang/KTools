
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.jiangkang.kplugin")
}

android {
    
    //仅仅在编译的时候起作用，建议总是使用最新版本，值是一个API Level
    compileSdkVersion(vCompileSdkVersion)

    //构建工具的版本，在build-tools中的那些(aapt,dexdump,zipalign,apksigner)，一般是API-Level.x.x
    buildToolsVersion(vBuildToolsVersion)
    
    ndkVersion = vNdkVersion

    defaultConfig {
        applicationId = "com.jiangkang.ktools"
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
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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

    sourceSets {
        getByName("main").res.srcDirs(
                "src/main/res/layout/activity",
                "src/main/res/layout/fragment",
                "src/main/res/layout/widget",
                "src/main/res/layout/item",
                "src/main/res"
        )
    }

    testOptions {
        unitTests {

        }
    }

    dexOptions {
        javaMaxHeapSize = "4g"
    }

    lintOptions {

        lintConfig = file("$rootDir/quality/lint/lint.xml")

        //关闭检查指定的Issue Id
        disable("TypographyFractions", "TypographyQuotes")

        //打开指定的Issue的检查
        enable("RtlHardcoded", "RtlCompat", "RtlEnabled")

        //仅仅只检查这些的子集，其他的不检查，这个选项会覆盖上面的disable，enable配置
        check("NewApi", "InlinedApi")

        //如果设置为true，则会关闭lint的分析进度
        isQuiet = false

        //如果设置为true(默认)，如果发现错误就停止构建
        isAbortOnError = false

        //如果设置为true，则只报告error
        isIgnoreWarnings = false
    }

    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
        }
    }
    
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.viewpager2)
    androidTestImplementation(AndroidX.espressoCore) {
        exclude("com.android.support", "support-annotations")
    }

    androidTestImplementation(AndroidX.annotation)

    androidTestImplementation("org.hamcrest:hamcrest-library:2.1")
    // Optional -- UI testing with UI Automator
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")

    testImplementation(junit)
    testImplementation("org.mockito:mockito-core:3.3.3")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:1.5.1")
    releaseImplementation("com.squareup.leakcanary:leakcanary-android-no-op:1.5.1")

    debugImplementation("com.amitshekhar.android:debug-db:1.0.0")
    debugImplementation("com.facebook.sonar:sonar:0.6.13") {
        exclude("android.arch.lifecycle", "runtime")
    }
    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.cardView)
    implementation(AndroidX.constraintLayout)
    implementation(material)
    retrofit()
    debugImplementation("com.readystatesoftware.chuck:library:1.1.0")
    releaseImplementation("com.readystatesoftware.chuck:library-no-op:1.1.0")
    glide()
    okHttp()
    implementation(AndroidX.Lifecycle.runtime)
    implementation(AndroidX.Lifecycle.extension)
    implementation(AndroidX.Room.runtime)
    kapt(AndroidX.Room.compiler)
    implementation(kotlin("stdlib-jdk7"))
    implementation(Kotlin.coroutines)
    implementation(AndroidX.multiDex)
    implementation(eventBus)
    implementation(anrWatchDog)
    implementation(AndroidX.Navigation.fragment)
    implementation(AndroidX.Navigation.ui)
    hilt()
    implementation(AndroidX.dynamicAnimation)
    implementation(project(":widget"))
    implementation(project(":annotations"))
    implementation(project(":hack"))
    implementation(project(":tools"))
    implementation(project(":hybrid"))
    implementation(project(":storage"))
    kapt(project (":compiler"))
    implementation(project(":image"))
    lintChecks(project (":klint"))
    implementation(project(":design"))
    implementation(project(":container"))
    implementation(project(":vpn"))
}

task("copy", Copy::class) {
    from("build/outputs/apk/debug/")
    into("build/outputs/apk/")
    include("**/*")
    delete("build/outputs/apk/debug/*")
}

traceMethod {
    enable = false
}