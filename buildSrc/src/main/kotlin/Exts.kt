import com.android.build.gradle.internal.dsl.BuildType
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.kotlin.dsl.DependencyHandlerScope
import java.io.File

fun DependencyHandlerScope.impl(dep: String) = scope("implementation", dep)
fun DependencyHandlerScope.debugImpl(dep: String) = scope("debugImplementation", dep)
fun DependencyHandlerScope.releaseImpl(dep: String) = scope("releaseImplementation", dep)
fun DependencyHandlerScope.api(dep: String) = scope("api", dep)
fun DependencyHandlerScope.kapt(dep: String) = scope("kapt", dep)
fun DependencyHandlerScope.testImpl(dep: String) = scope("testImplementation", dep)

internal fun DependencyHandlerScope.scope(scope: String, dep: String) {
    add(scope, dep)
}

fun NamedDomainObjectCollection<BuildType>.release(enableMinify: Boolean = false, shrinkRes: Boolean = false, proguardFiles: File) {
    getByName("release") {
        isMinifyEnabled = enableMinify
        isShrinkResources = shrinkRes
        proguardFiles(proguardFiles,"proguard-rules.pro")
    }
}

fun NamedDomainObjectCollection<BuildType>.debug(enableMinify: Boolean = false, shrinkRes: Boolean = false, proguardFiles: File) {
    getByName("debug") {
        isMinifyEnabled = enableMinify
        isShrinkResources = shrinkRes
        proguardFiles(proguardFiles,"proguard-rules.pro")
    }
}
