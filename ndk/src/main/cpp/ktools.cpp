#include <jni.h>
#include <string>
#include <iostream>
#include "skia_demo.h"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_jiangkang_ndk_NdkMainActivity_sayHello(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("Hello World");
}


extern "C"
JNIEXPORT void JNICALL
Java_com_jiangkang_ndk_skia_SkiaJni_drawText(JNIEnv *env, jobject thiz, jstring filename) {
    drawText(env->GetStringUTFChars(filename, JNI_FALSE));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_jiangkang_ndk_skia_SkiaJni_drawShape(JNIEnv *env, jobject thiz, jstring filename) {
    drawShape(env->GetStringUTFChars(filename, JNI_FALSE));
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_jiangkang_ndk_skia_SkiaJni_createNativeApp(JNIEnv *env, jobject thiz,
                                                    jobject asset_manager) {
    
}

extern "C"
JNIEXPORT void JNICALL
Java_com_jiangkang_ndk_skia_SkiaJni_destroyNativeApp(JNIEnv *env, jobject thiz, jlong handle) {
    
}