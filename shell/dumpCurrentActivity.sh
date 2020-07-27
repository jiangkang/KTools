#!/usr/bin/env bash

#查看当前Activity

adb shell dumpsys window windows | grep -E 'mCurrentFocus|mFocusedApp' --color=always