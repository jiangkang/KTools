#include <jni.h>
#include <string>
#include <iostream>
#include "skia_demo.h"

extern "C" JNIEXPORT void JNICALL
Java_com_jiangkang_ndk_NdkMainActivity_drawShapeTest(JNIEnv *env, jobject thiz, jstring filename) {
    drawShape(env->GetStringUTFChars(filename, JNI_FALSE));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_jiangkang_ndk_NdkMainActivity_sayHello(JNIEnv *env, jobject thiz) {
    jstring js = env->NewStringUTF("Hello World");
    return env->NewStringUTF("Hello World");
}