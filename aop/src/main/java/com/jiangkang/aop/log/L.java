package com.jiangkang.aop.log;

import android.os.Looper;
import android.util.Log;
import android.util.TimeUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;


/**
 *
 * @author jiangkang
 * @date 2017/10/24
 */

@Aspect
public class L {

    @Pointcut("execution(@com.jiangkang.annotations.log.DebugLog * *(..))")
    public void method() {
    }


    @Around("method()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        enterMethod(joinPoint);
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        long exeTime = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        exitMethod(joinPoint, result, exeTime);
        return result;
    }

    private void exitMethod(ProceedingJoinPoint joinPoint, Object result, long exeTime) {
        Signature signature = joinPoint.getSignature();
        Class<?> clz = signature.getDeclaringType();
        String methodName = signature.getName();

        boolean hasReturnType = signature instanceof MethodSignature && ((MethodSignature)signature).getReturnType() != void.class;

        StringBuilder builder = new StringBuilder("\n------------------------@DebugLog Exit---------------------------------\n");

        if (hasReturnType){
            builder.append("return = " + result + "\n");
        }

        builder.append(methodName).append("[execute time = " + exeTime + "ms]");

        builder.append("\n------------------------@DebugLog Exit end---------------------------------\n");
        Log.d(clz.getSimpleName(), builder.toString());

    }


    private void enterMethod(ProceedingJoinPoint joinPoint) {

        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        Class<?> clz = signature.getDeclaringType();
        String methodName = signature.getName();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        StringBuilder builder = new StringBuilder("\n------------------------@DebugLog Enter---------------------------------\n");
        if (Looper.getMainLooper() != Looper.myLooper()) {
            //非主线程，打印线程信息
            builder.append("[").append(Thread.currentThread().getName()).append("] ");
        }
        builder.append(methodName).append(":\n");
        for (int i = 0; i < paramValues.length; i++) {
            //此处可以对类型进行判断，读出更有意义的信息
            builder.append(paramNames[i])
                    .append("=")
                    .append(paramValues[i])
                    .append("\n");
        }
        builder.append("\n------------------------@DebugLog Enter end---------------------------------\n");
        Log.d(clz.getSimpleName(), builder.toString());
    }

}
