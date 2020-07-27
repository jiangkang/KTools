#!/usr/bin/env bash

# 清除App数据

package=com.jiangkang.ktools
adb shell pm clear ${package} && say "数据清除成功"

