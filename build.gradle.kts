buildscript {
    repositories {
        jcenter()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.greenrobot:greendao-gradle-plugin:3.2.2")
        classpath(kotlin("gradle-plugin", version = "1.3.72"))
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

println("KTools : build.gradle.kts")

subprojects {
    println("KTools: subprojects")
    beforeEvaluate {
        println("-----beforeEvaluate------")
    }
    afterEvaluate {
        println("-----start afterEvaluate------")
        val isAppModule = plugins.hasPlugin("android") || plugins.hasPlugin("com.android.application")
        val isAndroidLibModule = plugins.hasPlugin("android-library") || plugins.hasPlugin("com.android.library")
        println("${name}: isApp = ${isAppModule}, isAndroidLib = $isAndroidLibModule")
        println("-----end afterEvaluate------")
    }
}

task("clean", Delete::class) {
    delete(rootProject.buildDir)
}


tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.ALL
    gradleVersion = "6.5.1"
}


