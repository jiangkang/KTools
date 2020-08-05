package com.jiangkang.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.jiangkang.model.TraceMethodConfig
import org.gradle.api.Project

class MethodTraceTransform(val project: Project) : Transform() {

    override fun getName(): String = "MethodTraceTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun isIncremental(): Boolean = false

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

    override fun transform(transformInvocation: TransformInvocation) {
//        super.transform(transformInvocation)
        val traceMethodConfig = project.extensions.getByType(TraceMethodConfig::class.java)
        val output = traceMethodConfig.output
        if (!traceMethodConfig.enable){
           return
        }
        val outputProvider = transformInvocation.outputProvider
        if (!transformInvocation.isIncremental){
            outputProvider.deleteAll()
        }
        transformInvocation.inputs.forEach { input ->
            input.directoryInputs.forEach { dirInput ->
                transformSrcFiles(dirInput,outputProvider,traceMethodConfig)
                val dest = outputProvider.getContentLocation(dirInput.name,dirInput.contentTypes,dirInput.scopes,Format.DIRECTORY)
                FileUtils.copyDirectory(dirInput.file,dest)
            }
            input.jarInputs.forEach { jarInput ->
                transformJarFiles(jarInput,outputProvider,traceMethodConfig)
                val dest = outputProvider.getContentLocation(jarInput.name,jarInput.contentTypes,jarInput.scopes,Format.JAR)
                FileUtils.copyFile(jarInput.file,dest)
            }
        }

    }

    private fun transformSrcFiles(dirInput: DirectoryInput, outputProvider: TransformOutputProvider, traceMethodConfig: TraceMethodConfig) {
        if (dirInput.file.isDirectory){
            FileUtils.getAllFiles(dirInput.file).forEach {
                project.logger.error(it.name)
            }
        }
    }

    private fun transformJarFiles(jarInput: JarInput, outputProvider: TransformOutputProvider, traceMethodConfig: TraceMethodConfig) {
          FileUtils.getAllFiles(jarInput.file).forEach {
              project.logger.error(it.name)
          }
    }
}