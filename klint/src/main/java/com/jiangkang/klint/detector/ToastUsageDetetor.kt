package com.jiangkang.klint.detector

import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.ULiteralExpression

open class ToastUsageDetector : Detector(), SourceCodeScanner {

    companion object {
        val ISSUE = Issue.create(
                "ToastUsage",
                "Toast没有show",
                "Toast不要忘了使用show",
                Category.MESSAGES,
                8,
                Severity.ERROR,
                Implementation(ToastUsageDetector::class.java, JAVA_FILE_SCOPE)
        )
    }


    override fun getApplicableMethodNames(): List<String>? {
        return listOf(
                "makeText"
        )
    }


    override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {

        if (!context.evaluator.isMemberInClass(method, "android.widget.Toast")) {
            return
        }

        val args = node.valueArguments
        if (args.size == 3) {
            val duration = args[2]
            if (duration is ULiteralExpression) {
                context.report(
                        ISSUE,
                        duration,
                        context.getLocation(duration),
                        "Expected duration `Toast.LENGTH_SHORT` or `Toast.LENGTH_LONG`, a custom " + "duration value is not supported",
                        LintFix.create()
                                .name("用Toast.LENGTH_SHORT替换数字")
                                .replace()
                                .pattern("[0-9]+")
                                .with("Toast.LENGTH_SHORT")
                                .build()
                )
            }
        }


    }


}