import Google.vHilt
import org.gradle.kotlin.dsl.DependencyHandlerScope

const val junit = "junit:junit:4.13"
const val mockito = "org.mockito:mockito-core:3.3.3"
const val eventBus = "org.greenrobot:eventbus:3.1.1"
const val material = "com.google.android.material:material:1.1.0"
const val zxing = "com.google.zxing:core:3.4.0"
const val lottie = "com.airbnb.android:lottie:3.4.1"
const val gson = "com.google.code.gson:gson:2.8.6"
const val anrWatchDog = "com.github.anrwatchdog:anrwatchdog:1.4.0"
const val hamcrest = "org.hamcrest:hamcrest-library:2.2"
const val javapoet = "com.squareup:javapoet:1.13.0"

object Google {
    const val vHilt = "2.28-alpha"
    const val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:$vHilt"
    const val oboe = "com.google.oboe:oboe:1.4.3"
}

object Square {
    object LeakCanary {
        private const val version = "2.5"
        const val android = "com.squareup.leakcanary:leakcanary-android:$version"
        const val sharkHprof = "com.squareup.leakcanary:shark-hprof:$version"
        const val sharkGraph = "com.squareup.leakcanary:shark-graph:$version"
        const val sharkAndroid = "com.squareup.leakcanary:shark-android:$version"
    }
}

fun DependencyHandlerScope.hilt() {
    impl("com.google.dagger:hilt-android:$vHilt")
    kapt("com.google.dagger:hilt-android-compiler:$vHilt")
}

object Kotlin {
    const val kotlinVersion = "1.4.21"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9"
    const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
}

object AndroidX {
    const val core = "androidx.core:core:1.3.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
    const val testRunner = "androidx.test:runner:1.2.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
    const val annotation = "androidx.annotation:annotation:1.1.0"
    const val appcompat = "androidx.appcompat:appcompat:1.1.0"
    const val cardView = "androidx.cardview:cardview:1.0.0"
    const val dynamicAnimation = "androidx.dynamicanimation:dynamicanimation:1.0.0"
    const val uiautomator = "androidx.test.uiautomator:uiautomator:2.2.0"
    const val multiDex = "androidx.multidex:multidex:2.0.1"
    const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"
    const val print = "androidx.print:print:1.0.0"
    const val startup = "androidx.startup:startup-runtime:1.0.0-beta01"
    const val futures = "androidx.concurrent:concurrent-futures-ktx:1.1.0"
    const val tracing = "androidx.tracing:tracing-ktx:1.0.0"
    const val exifinterface = "androidx.exifinterface:exifinterface:1.3.2"
    const val customview = "androidx.customview:customview:1.1.0"

    object Navigation {
        const val fragment = "androidx.navigation:navigation-fragment:2.3.0"
        const val ui = "androidx.navigation:navigation-ui:2.3.0"
    }

    object Room {
        const val runtime = "androidx.room:room-runtime:2.2.5"
        const val compiler = "androidx.room:room-compiler:2.2.5"
    }

    object Lifecycle {
        const val runtime = "androidx.lifecycle:lifecycle-runtime:2.2.0"
        const val extension = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    }

    object WorkManager {
        private const val version = "2.4.0"
        const val runtime = "androidx.work:work-runtime:$version"         // java only
        const val runtimeKtx = "androidx.work:work-runtime-ktx:$version"  // kotlin + coroutines
    }

    object DataStore {
        private const val version = "1.0.0-alpha01"
        const val preferences = "androidx.datastore:datastore-preferences:$version"   // preferences
        const val core = "androidx.datastore:datastore-core:$version"  // proto
    }
}

fun DependencyHandlerScope.okHttp() {
    impl("com.squareup.okhttp3:okhttp:4.4.0")
    testImpl("com.squareup.okhttp3:mockwebserver:4.2.0")
    impl("com.squareup.okhttp3:logging-interceptor:4.2.0")
}

fun DependencyHandlerScope.retrofit() {
    impl("com.squareup.retrofit2:retrofit:2.6.1")
    impl("com.squareup.retrofit2:converter-gson:2.6.1")
}

fun DependencyHandlerScope.glide() {
    impl("com.github.bumptech.glide:glide:4.11.0")
    impl("com.github.bumptech.glide:okhttp3-integration:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")
}

fun DependencyHandlerScope.fresco() {
    val vFresco = "2.3.0"
    api("com.facebook.fresco:fresco:$vFresco")
    api("com.facebook.fresco:animated-gif:$vFresco")
    api("com.facebook.fresco:webpsupport:$vFresco")
    api("com.facebook.fresco:animated-webp:$vFresco")
}

fun DependencyHandlerScope.leakcanary() {
    val vLeakCanary = "1.6.3"
    debugImpl("com.squareup.leakcanary:leakcanary-android:$vLeakCanary")
    releaseImpl("com.squareup.leakcanary:leakcanary-android-no-op:$vLeakCanary")
}