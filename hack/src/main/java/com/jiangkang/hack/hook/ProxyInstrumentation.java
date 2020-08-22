package com.jiangkang.hack.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.jiangkang.hack.activity.ReplacementActivity;
import com.jiangkang.tools.utils.LogUtils;
import com.jiangkang.tools.utils.ToastUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyInstrumentation extends Instrumentation {

    private static final String TAG = "Hook";

    private final Instrumentation mInstrumentation;

    private final String EXEC_START_ACTIVITY = "execStartActivity";

    public ProxyInstrumentation(Instrumentation instrumentation) {
        this.mInstrumentation = instrumentation;
    }

    /**
     * Hook execStartActivity
     */
    @SuppressLint("DiscouragedPrivateApi")
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        LogUtils.d(TAG, "Hook Instrumentation#execStartActivity");
        processIntent(who, contextThread, token, target, intent, requestCode, options);
        intent = new Intent(who, ReplacementActivity.class);
        try {
            Method execStartActivityMethod = Instrumentation.class.getDeclaredMethod(
                    EXEC_START_ACTIVITY,
                    Context.class, IBinder.class, IBinder.class,
                    Activity.class, Intent.class, int.class, Bundle.class
            );
            return (ActivityResult) execStartActivityMethod.invoke(mInstrumentation, who, contextThread, token, target, intent, requestCode, options);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void processIntent(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options) {
        LogUtils.d(TAG, "activity=" + target);
        LogUtils.d(TAG, "intent=" + intent);
        ToastUtils.showShortToast("target activity=" + target + "\nintent=" + intent);
    }
}
