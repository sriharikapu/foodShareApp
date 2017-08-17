package com.fan.framework.http;

public enum FFResponseCode {
    /**
     * 默认
     */
    UNSET,
    /**
     * activity finished
     */
    ACTIVITY_FINISHED,
    /**
     * 网络已关闭
     */
    ERROR_NATIVE_NET_CLOST,
    /**
     * 网络响应超时
     */
    ERROR_NET_TIMEOUT_R,
    /**
     * 网络请求超时
     */
    ERROR_NET_TIMEOUT_S,
    /**
     * 404异常
     */
    ERROR_NET_404,
    /**
     * 505异常
     */
    ERROR_SITE_505,
    /**
     * 未知的网络异常
     */
    ERROR_SITE_XXX,
    /**
     * 成功
     */
    SUCCESS,
    /**
     * io 异常
     */
    ERROR_IO,
    /**
     * 图片类型不支持
     */
    ERROR_IMAGE_TYPE_NOSUPPORT,
    /**
     * json解析异常
     */
    ERROR_ANALYSIS,
    /**
     * 接口返回错误
     */
    ERROR_CONTEXT
}