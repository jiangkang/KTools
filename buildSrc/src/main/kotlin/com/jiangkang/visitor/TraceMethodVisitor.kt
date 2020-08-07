package com.jiangkang.visitor

import com.jiangkang.model.TraceMethodConfig
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class TraceMethodVisitor(methodVisitor: MethodVisitor, access: Int, name: String?, descriptor: String?, className: String?, config: TraceMethodConfig) : MethodVisitor(Opcodes.ASM5) {
}