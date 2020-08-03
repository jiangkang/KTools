buildscript {

    repositories {
        jcenter()
        mavenCentral()
        google()
    }
    
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.greenrobot:greendao-gradle-plugin:3.2.2")
        classpath(kotlin("gradle-plugin", version = "1.3.72"))
    }


}

allprojects {
    repositories {
        jcenter()
        google()
        mavenCentral()
    }
}

task("clean",Delete::class){
    delete(rootProject.buildDir)
}


