//
// Created by 姜康 on 2017/12/14.
//
#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_jiangkang_highperformanceapp_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_jiangkang_ktools_MainActivity_stringFromJNI(JNIEnv *env, jobject instance) {
    std::string hello = "Hello World!";
    return env->NewStringUTF(hello.c_str());
}


