package com.jiangkang.annotations.apt;

import androidx.annotation.LayoutRes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by jiangkang on 2018/3/29.
 * description：
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface BindLayout {

//    @LayoutRes int value();

}
