package com.fan.framework.http;

public interface FFBaseBean {
    /**
     * 服务器返回结果是否可用
     *
     * @return 判断结果
     */
    boolean judge();

    /**
     * 获取错误信息
     * @return 错误信息
     */
    String getErrorMessage();

    /**
     * 是否是无数据
     * 仅当{@link FFExtraParams#isInitPage()}=true 有效
     * @return
     */
    boolean isNoData();

    /**
     * 拦截此返回结果的提示
     * @return 是否拦截
     */
    public boolean isConsum(FFNetWorkRequest request);
}
