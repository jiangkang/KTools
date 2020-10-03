//
// Created by 姜康 on 2020/9/29.
//

#include <jni.h>
#include <string>
#include <iostream>
#include "skia_demo.h"
#include "utils.h"
#include <include/core/SkEncodedImageFormat.h>
#include <include/core/SkImageEncoder.h>
#include "include/core/SkCanvas.h"
#include "include/core/SkBitmap.h"
#include "include/core/SkPaint.h"
#include "include/core/SkRect.h"
#include "include/core/SkRRect.h"
#include "include/core/SkFontMgr.h"
#include "include/core/SkFontStyle.h"
#include "include/core/SkFont.h"
#include "include/core/SkTextBlob.h"

SkBitmap createSkBitmap(int width, int height) {
    SkBitmap skBitmap;
    skBitmap.setInfo(SkImageInfo::Make(width, height, kRGBA_8888_SkColorType, kOpaque_SkAlphaType));
    skBitmap.allocPixels();
    logd("创建SKBitmap成功");
    return skBitmap;
}

void saveAsPng(const char *filename, const SkBitmap &bitmap) {
    logd(filename);
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

void drawShape(const char *filename) {
    SkBitmap skBitmap = createSkBitmap(600, 600);
    SkCanvas skCanvas(skBitmap);
    drawShape(&skCanvas);
    saveAsPng(filename, skBitmap);
}


void drawText(SkCanvas *canvas) {

    canvas->drawColor(SK_ColorWHITE);

    const char *fontFamily = nullptr;
    SkFontStyle fontStyle;
    sk_sp<SkFontMgr> fontManager = SkFontMgr::RefDefault();
    sk_sp<SkTypeface> typeface = fontManager->legacyMakeTypeface(fontFamily, fontStyle);

    SkFont font1(typeface, 64.0f);
    SkFont font2(typeface, 32.0f, 1.5f, 0.0f);
    font1.setEdging(SkFont::Edging::kAntiAlias);
    font2.setEdging(SkFont::Edging::kAntiAlias);

    //这里可能会失败，因为有不支持的语言，比如中文
    sk_sp<SkTextBlob> blob1 = SkTextBlob::MakeFromString("Jiang Kang", font1);
    sk_sp<SkTextBlob> blob2 = SkTextBlob::MakeFromString("Jiang Kang", font2);
    sk_sp<SkTextBlob> blob3 = SkTextBlob::MakeFromString("Skia", font2);

    SkPaint paint1, paint2, paint3;

    paint1.setAntiAlias(true);
    paint1.setColor(SkColorSetARGB(0xFF, 0x42, 0x85, 0xF4));

    paint2.setAntiAlias(true);
    paint2.setColor(SkColorSetARGB(0xFF, 0xDB, 0x44, 0x37));
    paint2.setStyle(SkPaint::kStroke_Style);
    paint2.setStrokeWidth(3.0f);

    paint3.setAntiAlias(true);
    paint3.setColor(SkColorSetARGB(0xFF, 0x0F, 0x9D, 0x58));

    canvas->clear(SK_ColorWHITE);
    canvas->drawTextBlob(blob1.get(), 20.0f, 64.0f, paint1);
    canvas->drawTextBlob(blob2.get(), 20.0f, 144.0f, paint2);
    canvas->drawTextBlob(blob3.get(), 20.0f, 224.0f, paint3);

}

void drawText(const char *filename){
    SkBitmap skBitmap = createSkBitmap(600, 600);
    SkCanvas skCanvas(skBitmap);
    drawText(&skCanvas);
    saveAsPng(filename, skBitmap);
}




