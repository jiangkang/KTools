package com.jiangkang.hybrid.web

/**
 * Created by jiangkang on 2018/2/27.
 * description：
 */

class WebArgs {

    var isLoadImgLazy: Boolean = false

    var isInterceptResources: Boolean = false

    companion object {

        val IS_LOAD_IMG_LAZY = "is_load_img_lazy"

        val IS_INTERCEPT_RESOURCES = "IS_INTERCEPT_RESOURCES"

        val STR_INJECTED_JS = "str_injected_js"

    }

    var jsInjected: String = ""


}