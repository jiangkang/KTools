import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.impl(dep: String) = scope("implementation", dep)
fun DependencyHandlerScope.debugImpl(dep: String) = scope("debugImplementation", dep)
fun DependencyHandlerScope.releaseImpl(dep: String) = scope("releaseImplementation", dep)
fun DependencyHandlerScope.api(dep: String) = scope("api", dep)
fun DependencyHandlerScope.kapt(dep: String) = scope("kapt", dep)
fun DependencyHandlerScope.testImpl(dep: String) = scope("testImplementation", dep)

internal fun DependencyHandlerScope.scope(scope: String, dep: String) {
    add(scope, dep)
}
