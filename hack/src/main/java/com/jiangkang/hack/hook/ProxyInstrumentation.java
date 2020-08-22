package com.jiangkang.hack.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.jiangkang.tools.utils.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ProxyInstrumentation extends Instrumentation {

    private static final String TAG = "Hook";

    private final Instrumentation mInstrumentation;
    private ActivityStartingCallback mCallback;

    private final String EXEC_START_ACTIVITY = "execStartActivity";

    public ProxyInstrumentation(Instrumentation instrumentation) {
        this.mInstrumentation = instrumentation;
    }

    public ProxyInstrumentation(Instrumentation instrumentation, ActivityStartingCallback callback) {
        this.mInstrumentation = instrumentation;
        mCallback = callback;
    }

    /**
     * Hook execStartActivity
     */
    @SuppressLint("DiscouragedPrivateApi")
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        LogUtils.d(TAG, "Hook Instrumentation#execStartActivity");
        if (mCallback != null){
            mCallback.activityStarting(who,target,intent);
        }
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

}
