//
// Created by 姜康 on 2020/10/3.
//

#include <android/native_window.h>
#include <include/core/SkString.h>

enum MessageType {
    kUndefined,
    kSurfaceCreated,
    kSurfaceChanged,
    kSurfaceDestroyed,
    kDestroyApp,
    kContentInvalidated,
    kKeyPressed,
    kTouched,
    kUIStateChanged,
};

struct Message {
    MessageType fType = kUndefined;
    ANativeWindow* fNativeWindow = nullptr;
    int fKeycode = 0;
    int fTouchOwner, fTouchState;
    float fTouchX, fTouchY;

    SkString* stateName;
    SkString* stateValue;

    Message() {}
    Message(MessageType t) : fType(t) {}
};

