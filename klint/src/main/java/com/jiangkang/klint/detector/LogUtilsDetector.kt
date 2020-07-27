package com.jiangkang.klint.detector

import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Category.Companion.MESSAGES
import com.android.tools.lint.detector.api.Category.Companion.create
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.*

class LogUtilsDetector : Detector(), SourceCodeScanner {
    //返回这个Detector感兴趣的method name 列表，如果返回不为null，则任何匹配列表中元素的AST节点都会被
    //传递给[.visitMethod]方法进行处理,被[.createPsiMethod]创建的visitor，不论是否为null，都会被传递给[.visitMethod]
    override fun getApplicableMethodNames(): List<String>? {
        return Arrays.asList(
                "v",
                "d",
                "i",
                "w",
                "e",
                "wtf"
        )
    }

    override fun visitMethod(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        if (!context.evaluator.isMemberInClass(method, "android.util.Log")) {
            return
        }

        //修改bug
        val fix = LintFix.create().name("Use LogUtils instead").replace()
                .pattern("Log")
                .with("LogUtils").build()
        context.report(ISSUE,
                node,
                context.getLocation(node),
                "You must use our `LogUtils`", fix)
    }

    companion object {
        @JvmField
        val ISSUE: Issue = Issue.create(
                "LogUtilsNotUsed",
                "应该使用`LogUtils`替换原生的`Log`",
                "LogUtil 设置了开关，功能更加强大",
                MESSAGES,
                9, Severity.ERROR,
                Implementation(LogUtilsDetector::class.java, JAVA_FILE_SCOPE)
        )
    }
}