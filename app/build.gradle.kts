import java.net.URI

//val storageUrl = System.env.FLUTTER_STORAGE_BASE_URL ?: "https://storage.googleapis.com"
val storageUrl = "https://storage.googleapis.com"
repositories {
    maven {
        url = URI.create("file://${rootDir.absolutePath}/flutter_module/build/host/outputs/repo")
    }
    maven {
        url = URI.create("$storageUrl/download.flutter.io")
    }
}


android {

    ndkVersion = vNdkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    lintOptions {
        isAbortOnError = false
    }

    dexOptions {
        javaMaxHeapSize = "4g"
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
    implementation(AndroidX.appcompat)
    implementation(AndroidX.constraintLayout)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlin_version"]}")
    androidTestImplementation(AndroidX.espressoCore) {
        exclude("com.android.support", "support-annotations")
    }

    androidTestImplementation(AndroidX.annotation)

    androidTestImplementation(hamcrest)
    // Optional -- UI testing with UI Automator
    androidTestImplementation(AndroidX.uiautomator)

    testImplementation(junit)
    testImplementation(mockito)

//    leakcanary()
    implementation(AndroidX.appcompat)
    implementation(AndroidX.recyclerView)
    implementation(AndroidX.cardView)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.startup)
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
    kapt(AndroidX.Lifecycle.compiler)
    implementation(Kotlin.stdlib)
    implementation(Kotlin.coroutines)
    implementation(AndroidX.multiDex)
    implementation(eventBus)
    implementation(anrWatchDog)
    implementation(AndroidX.Navigation.fragment)
    implementation(AndroidX.Navigation.ui)
    hilt()
    implementation(AndroidX.dynamicAnimation)
    implementation(AndroidX.WorkManager.runtimeKtx)
    implementation(AndroidX.DataStore.core)
    implementation(AndroidX.DataStore.preferences)
    implementation(AndroidX.futures)
    implementation(AndroidX.paging)
//    implementation(Square.LeakCanary.android)
    implementation(project(":widget"))
    implementation(project(":annotations"))
    implementation(project(":hack"))
    implementation(project(":tools"))
    implementation(project(":hybrid"))
    implementation(project(":storage"))
    kapt(project(":compiler"))
    implementation(project(":image"))
//    lintChecks(project(":klint"))
    implementation(project(":design"))
    implementation(project(":container"))
    implementation(project(":vpn"))
    implementation(project(":ndk"))
    implementation(project(":video"))
    implementation("com.ktools.flutter_module:flutter_release:1.0")
}

task("copy", Copy::class) {
    from("build/outputs/apk/debug/")
    into("build/outputs/apk/")
    include("**/*")
    delete("build/outputs/apk/debug/*")
}

//traceMethod {
//    enable = false
//}