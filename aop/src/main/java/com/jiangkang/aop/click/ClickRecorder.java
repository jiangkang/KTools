package com.jiangkang.aop.click;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by jiangkang on 2018/3/14.
 * description：
 */

@Aspect
public class ClickRecorder {

    private static final String TAG = "ClickRecorder";

    @Pointcut("execution(* android.view.View$OnClickListener.onClick(..))")
    public void onClick() {
    }


    @Around("onClick()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);
        Object result = joinPoint.proceed();
        exitMethod(joinPoint);
        return result;
    }



    private void enterMethod(ProceedingJoinPoint joinPoint) {

        Log.d(TAG,"点击按钮之前");


    }

    private void exitMethod(ProceedingJoinPoint joinPoint) {
        Log.d(TAG,"点击按钮之后");
    }


}
