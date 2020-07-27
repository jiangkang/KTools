import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.impl(dep: String) = scope("implementation", dep)
fun DependencyHandlerScope.testImpl(dep: String) = scope("testImplementation", dep)

internal fun DependencyHandlerScope.scope(scope: String, dep: String) {
    add(scope, dep)
}
