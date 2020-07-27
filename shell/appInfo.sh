#!/usr/bin/env bash

# 跳转到应用设置页

package=com.jiangkang.ktools
adb shell am start  -a "android.settings.APPLICATION_DETAILS_SETTINGS" -d "package:${package}"