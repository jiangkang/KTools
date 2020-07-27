package com.jiangkang.klint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue
import com.jiangkang.klint.detector.LogUtilsDetector
import java.util.*

class KLintRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = listOf(
                LogUtilsDetector.ISSUE
        )
}