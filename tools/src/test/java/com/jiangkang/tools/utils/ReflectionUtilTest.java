package com.jiangkang.tools.utils;

import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by jiangkang on 2018/1/29.
 * description：ReflectionUtil的测试类
 */
public class ReflectionUtilTest {

    @Test
    public void testGetPackageName() throws Exception {

        //class
        assertEquals("java.lang", ReflectionUtil.getPackageName(Object.class));
        assertEquals("java.io", ReflectionUtil.getPackageName(File.class));


        //className
        assertEquals("java.lang", ReflectionUtil.getPackageName(Object.class.getName()));
        assertEquals("java.lang", ReflectionUtil.getPackageName("java.lang.Object"));

        //no package
        assertEquals("", ReflectionUtil.getPackageName("No Package"));

    }


    private static int classesInitialized = 0;
    private static class A {
        static {
            ++classesInitialized;
        }
    }
    private static class B {
        static {
            ++classesInitialized;
        }
    }
    private static class C {
        static {
            ++classesInitialized;
        }
    }

    @Test
    public void testInitialize() {
        assertEquals("This test can't be included twice in the same suite.", 0, classesInitialized);

        ReflectionUtil.initialize(A.class);
        assertEquals(1, classesInitialized);

        ReflectionUtil.initialize(
                A.class,  // Already initialized (above)
                B.class,
                C.class);
        assertEquals(3, classesInitialized);
    }


    private static InvocationHandler sHandler = new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return "X";
        }
    };

    @Test
    public void newProxy() throws Exception {
        Runnable runnable = ReflectionUtil.newProxy(Runnable.class, sHandler);
        assertEquals("X", runnable.toString());
    }


    @Test
    public void newProxyCannotWorkOnClass() throws Exception {
        try {
            ReflectionUtil.newProxy(Object.class,sHandler);
            fail();
        }catch (Exception e){

        }

    }

}