package com.jiangkang.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.jiangkang.model.TraceMethodConfig
import com.jiangkang.visitor.TraceMethodClassVisitor
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.FileOutputStream

class MethodTraceTransform(val project: Project) : Transform() {

    override fun getName(): String = "MethodTraceTransform"

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun isIncremental(): Boolean = false

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

    override fun transform(transformInvocation: TransformInvocation) {
        val traceMethodConfig = project.extensions.getByType(TraceMethodConfig::class.java)
        if (!traceMethodConfig.enable) {
            super.transform(transformInvocation)
            return
        }
        val outputProvider = transformInvocation.outputProvider
        if (!transformInvocation.isIncremental) {
            outputProvider.deleteAll()
        }
        transformInvocation.inputs.forEach { input ->
            input.directoryInputs.forEach { dirInput ->
                transformSrcFiles(dirInput, outputProvider, traceMethodConfig)
                val dest = outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(dirInput.file, dest)
            }
            input.jarInputs.forEach { jarInput ->
                transformJarFiles(jarInput, outputProvider, traceMethodConfig)
                val dest = outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }
        }

    }

    private fun transformSrcFiles(dirInput: DirectoryInput, outputProvider: TransformOutputProvider, traceMethodConfig: TraceMethodConfig) {
        if (dirInput.file.isDirectory) {
            FileUtils.getAllFiles(dirInput.file).forEach {
                if (it.name.endsWith(".kotlin_module")){
                    return@forEach
                }
                val classReader = ClassReader(it.inputStream())
                val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                val classVisitor = TraceMethodClassVisitor(project,classWriter, traceMethodConfig)
                classReader.accept(classVisitor,ClassReader.EXPAND_FRAMES)
                val fos = FileOutputStream(it.parentFile.absolutePath + File.separator + it.name)
                fos.write(classWriter.toByteArray())
                fos.flush()
            }
        }
    }

    private fun transformJarFiles(jarInput: JarInput, outputProvider: TransformOutputProvider, traceMethodConfig: TraceMethodConfig) {
        FileUtils.getAllFiles(jarInput.file).forEach {
//            project.logger.error(it.name)
        }
    }
}