package com.jiangkang.compiler.utils;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.Messager;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

public class MsgUtils {

    private Messager messager;

    public MsgUtils(Messager messager) {
        this.messager = messager;
    }

    public void info(CharSequence msg) {
        if (StringUtils.isNotEmpty(msg)) {
            messager.printMessage(NOTE, msg);
        }
    }

    public void error(CharSequence msg){
        if (StringUtils.isNotEmpty(msg)){
            messager.printMessage(ERROR,msg);
        }
    }
}
