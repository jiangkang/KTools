//
// Created by 姜康 on 2020/9/29.
//

#include "utils.h"
#include <android/log.h>

void logd(const char *msg) {
    __android_log_print(ANDROID_LOG_DEBUG, "ktools:", "%s", msg);
}
