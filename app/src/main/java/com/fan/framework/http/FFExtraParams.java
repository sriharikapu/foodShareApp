package com.fan.framework.http;

/**
 * 1、是否缓存数据{@link #isDoCache()}<br/>
 * 2、 附加一个任意对象{@link #getObj()}<br/>
 * 3、是否在当前线程请求{@link #isIsSynchronized()}<br/>
 * 4、当activity结束依然回调{@link #isKeepWhenActivityFinished()}<br/>
 * 5、缓存有效期{@link #getUseValidTime()}<br/>
 * 6、是否使用缓存数据{@link #isUseCache()}<br/>
 * 7、不弹出错误提示{@link #isQuiet()}<br/>
 * 8、服务器返回的字符串原串{@link #getReponseString()}<br>
 * 9、自动弹出的对话框是否可以被用户取消{@link #isProgressDialogcancelAble()}
 * 9、是否是加载页面请求{@link #isInitPage()}
 */
public class FFExtraParams {
    private boolean doCache = false;
    private Object obj = null;
    private boolean isSynchronized = false;
    private boolean keepWhenActivityFinished = false;
    private long useValidTime;
    private boolean useCache;
    private boolean quiet;
    private String reponseString;
    private boolean progressDialogcancelAble = true;
    private boolean isInitPage = false;

    public FFExtraParams() {
    }

    public FFExtraParams(FFExtraParams extraParams) {
        if (extraParams == null) {
            return;
        }
        setReponseString(extraParams.getReponseString());
        setDoCache(extraParams.doCache);
        setUseCache(extraParams.useCache, extraParams.useValidTime);
        setObj(extraParams.obj);
        setSynchronized(extraParams.isSynchronized);
        setKeepWhenActivityFinished(extraParams.keepWhenActivityFinished);
        setQuiet(extraParams.quiet);
        setProgressDialogcancelAble(extraParams.progressDialogcancelAble);
        setInitPage(extraParams.isInitPage);
    }


    /**
     * @return 是否是加载页面请求
     */
    public boolean isInitPage() {
        return isInitPage;
    }

    public FFExtraParams setInitPage(boolean initPage) {
        isInitPage = initPage;
        return this;
    }

    /**
     * 获取附加的任意对象
     *
     * @return
     */
    public Object getObj() {
        return obj;
    }

    /**
     * 附加一个任意对象
     */
    public FFExtraParams setObj(Object obj) {
        this.obj = obj;
        return this;
    }

    /**
     * 是否缓存数据
     */
    public boolean isDoCache() {
        return doCache;
    }

    /**
     * 是否使用缓存数据
     */
    public boolean isUseCache() {
        return useCache;
    }

    /**
     * 是否缓存数据
     */
    public FFExtraParams setDoCache(boolean doCache) {
        this.doCache = doCache;
        return this;
    }

    /**
     * @param useCache     是否使用缓存数据
     * @param useValidTime 缓存有效期
     */
    public FFExtraParams setUseCache(boolean useCache, long useValidTime) {
        this.useCache = useCache;
        this.useValidTime = useValidTime;
        return this;
    }

    /**
     * @return 获取有效时间，以毫秒为单位
     */
    public long getUseValidTime() {
        return useValidTime;
    }

    /**
     * @return 是否在当前线程请求
     */
    public boolean isIsSynchronized() {
        return isSynchronized;
    }

    /**
     * 是否在当前线程请求
     *
     * @param isSynchronized
     * @return
     */
    public FFExtraParams setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
        return this;
    }

    /**
     * @return 当activity结束依然回调
     */
    public boolean isKeepWhenActivityFinished() {
        return keepWhenActivityFinished;
    }

    /**
     * 当activity结束已然回调
     *
     * @param keepWhenActivityFinished
     * @return
     */
    public FFExtraParams setKeepWhenActivityFinished(boolean keepWhenActivityFinished) {
        this.keepWhenActivityFinished = keepWhenActivityFinished;
        return this;
    }

    /**
     * @return 不弹出错误提示
     */
    public boolean isQuiet() {
        return quiet;
    }

    /**
     * 不弹出错误提示
     */
    public FFExtraParams setQuiet(boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    /**
     * 服务器返回的字符串原串
     */
    public String getReponseString() {
        return reponseString;
    }

    /**
     * 服务器返回的字符串原串
     *
     * @return
     */
    public FFExtraParams setReponseString(String reponseString) {
        this.reponseString = reponseString;
        return this;
    }

    /**
     * 自动弹出的对话框是否可以被用户取消
     */
    public boolean isProgressDialogcancelAble() {
        return progressDialogcancelAble;
    }

    /**
     * 自动弹出的对话框是否可以被用户取消<br/>默认可以取消
     *
     * @return
     */
    public FFExtraParams setProgressDialogcancelAble(boolean progressDialogcancelAble) {
        this.progressDialogcancelAble = progressDialogcancelAble;
        return this;
    }
}
