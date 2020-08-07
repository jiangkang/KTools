package com.jiangkang

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.jiangkang.model.TraceMethodConfig
import com.jiangkang.transform.MethodTraceTransform

abstract class KPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.extensions.create("traceMethod",TraceMethodConfig::class.java)
//        val android = target.extensions.getByType(AppExtension::class.java)
//        android.registerTransform(MethodTraceTransform(target))
    }

}