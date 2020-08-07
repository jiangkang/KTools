package com.jiangkang.visitor

import com.jiangkang.model.TraceMethodConfig
import org.gradle.api.Project
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class TraceMethodClassVisitor(val project: Project, val classWriter: ClassWriter, val config: TraceMethodConfig) : ClassVisitor(Opcodes.ASM5,classWriter) {

    private var className:String? = null

    /**
     * 抽象类或者接口
     */
    private var isAbsClass = false
    private var isInjectClass = false
    private var isTargetClass = false

    override fun visit(version: Int, access: Int, name: String?, signature: String?, superName: String?, interfaces: Array<out String>?) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
        project.logger.debug(className)
        if (access and Opcodes.ACC_ABSTRACT > 0 || (access and Opcodes.ACC_INTERFACE > 0)) {
            isAbsClass = true
        }
        if (name == "com.jiangkang.MethodTraceInjector"){
            isInjectClass = true
        }
    }

    override fun visitMethod(access: Int, name: String?, descriptor: String?, signature: String?, exceptions: Array<out String>?): MethodVisitor {
        if (isAbsClass or  isInjectClass or  (name?.contains("<init>") == true)){
            return super.visitMethod(access, name, descriptor, signature, exceptions)
        } else{
            val methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
            return TraceMethodVisitor(methodVisitor,access, name, descriptor,className,config)
        }
    }

}