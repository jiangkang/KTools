#include <jni.h>
#include <string>
#include <iostream>
#include <include/core/SkEncodedImageFormat.h>
#include <include/core/SkImageEncoder.h>
#include "include/core/SkCanvas.h"
#include "include/core/SkBitmap.h"
#include "include/core/SkPaint.h"
#include "include/core/SkRect.h"
#include "include/core/SkRRect.h"

SkBitmap createSkBitmap(int width, int height) {
    SkBitmap skBitmap;
    skBitmap.setInfo(SkImageInfo::Make(width, height, kRGBA_8888_SkColorType, kOpaque_SkAlphaType));
    skBitmap.allocPixels();
    return skBitmap;
}

void saveAsPng(const char *filename, const SkBitmap &bitmap) {
    sk_sp<SkData> encodedData = SkEncodeBitmap(bitmap, SkEncodedImageFormat::kPNG, 100);
    FILE *pFile = fopen(filename, "wb+");
    if (!pFile) {
        std::cout << "文件打开失败！" << std::endl;
    } else {
        fwrite(encodedData->data(), encodedData->size(), 1, pFile);
        std::cout << "写入文件成功！" << std::endl;
    }
}

void drawShape(SkCanvas *canvas) {
    //绘制底色
    canvas->drawColor(SK_ColorWHITE);

    //设置Paint
    SkPaint paint;
    paint.setStyle(SkPaint::kStroke_Style);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(4);
    paint.setColor(0xff003366); //这里是ARGB格式，一定得注意带上透明度

    //矩形
    SkRect rect = SkRect::MakeXYWH(10, 10, 100, 160);
    canvas->drawRect(rect, paint);

    //圆角矩形实现的椭圆
    SkRRect oval;
    oval.setOval(rect); //以之前的矩形为基准
    oval.offset(40, 80);
    paint.setColor(0xffDB4437);
    canvas->drawRRect(oval, paint);

    paint.setColor(0xffF4B400);
    paint.setStyle(SkPaint::kFill_Style);
    rect.offset(160, 80);
    canvas->drawRoundRect(rect, 10, 10, paint);

    //圆
    paint.setColor(0xff0F9D58);
    paint.setStyle(SkPaint::kStroke_Style);
    canvas->drawCircle(150, 150, 60, paint);
}

void drawShapeTest(const char *filename){
    SkBitmap skBitmap = createSkBitmap(1080,2340);
    SkCanvas skCanvas(skBitmap);
    drawShape(&skCanvas);
    saveAsPng(filename,skBitmap);
}

extern "C" JNIEXPORT void JNICALL
Java_com_jiangkang_ndk_NdkMainActivity_drawShapeTest(JNIEnv *env, jobject thiz, jstring filename) {
    drawShapeTest(reinterpret_cast<const char *>(filename));

}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_jiangkang_ndk_NdkMainActivity_sayHello(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("Hello World");
}