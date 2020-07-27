import org.gradle.kotlin.dsl.DependencyHandlerScope

const val junit = "junit:junit:4.13"
const val eventBus = "org.greenrobot:eventbus:3.1.1"
const val material = "com.google.android.material:material:1.1.0"

object Kotlin {
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5"
}

object AndroidX {
    const val core = "androidx.core:core:1.3.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"
    const val testRunner = "androidx.test:runner:1.2.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
    const val annotation = "androidx.annotation:annotation:1.1.0"
    const val appcompat = "androidx.appcompat:appcompat:1.1.0"
    const val cardView = "androidx.cardview:cardview:1.0.0"
    const val dynamicAnimation = "androidx.dynamicanimation:dynamicanimation:1.0.0"
    const val uiautomator = "androidx.test.uiautomator:uiautomator:2.2.0"
    const val multiDex = "androidx.multidex:multidex:2.0.1"

    object Navigation {
        const val fragment = "androidx.navigation:navigation-fragment:2.3.0"
        const val ui = "androidx.navigation:navigation-ui:2.3.0"
    }
    object Room {}
    object Lifecycle {}
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