package com.fan.framework.http;

import com.fan.framework.utils.FFLogUtil;

public abstract class FFNetWorkCallBack<T extends FFBaseBean> {

    private static final String TAG = "FFNetWorkCallBack";
    private FFNetWorkRequest<T> request;

    public FFNetWorkRequest getRequest() {
        return request;
    }

    public FFNetWorkCallBack(){}
    Class<T> clazz;
    public FFNetWorkCallBack(Class<T> clazz){
        this.clazz = clazz;
    }



    /**
     * 当activity finished 将不再执行,除非再指定
     * {@link com.fan.framework.http.FFExtraParams#keepWhenActivityFinished} = true
     * {@link com.fan.framework.http.FFNetWork#post(String, String, FFExtraParams, FFNetWorkCallBack, Class, Object...)}
     */
    public final String fail(FFNetWorkRequest<T> request) {
        T response = request.getEntity();
        this.request = request;
//        FFLogUtil.e(TAG, "网络请求发生错误" + request.getErrMessage());
        // 是否统一处理
        if (response != null && response.isConsum(request)) {
            onFailPublic(response, request.getExtraParams());
            return null;
        }
        // 单独处理
        if (onFail(request.getExtraParams())) {
            return null;
        }
        // 服务器返回错误码自动处理
        if (response != null) {
            if (onFailContext(response, request.getExtraParams())) {
                return null;
            }
            return response.getErrorMessage();
        }
        // 网络错误自动处理
        String msg = null;
        switch (request.getStatus()) {
            case ERROR_NATIVE_NET_CLOST:// 网络未连接
                msg = "亲,操作失败了，请连接网络！";
                break;
            case ERROR_IMAGE_TYPE_NOSUPPORT:// 网络未连接
                msg = "亲,服务器不支持您选择的图片类型，请重新选择!";
                break;
            case ERROR_NET_TIMEOUT_R:// 请求超时
            case ERROR_NET_TIMEOUT_S:// 连接超时
                msg = "亲,由于您的网络状况不佳,操作失败了!";
                break;
            case ERROR_ANALYSIS:// 数据解析
            case ERROR_NET_404:// 404
            case ERROR_SITE_505:// 505
                msg = "亲,服务器开小差了,操作失败!";
                break;
            case ERROR_IO:// IO异常
            case ERROR_SITE_XXX:
            case ERROR_CONTEXT:
            case UNSET:// 未处理
                msg = "亲,操作失败了!";
                break;
            case ACTIVITY_FINISHED:
                return null;
            case SUCCESS:
                break;
        }
        if (onFailNet(msg, request.getStatus(), request.getExtraParams())) {
            return null;
        }
        return msg;
    }

    /**
     * 请求返回必执行 无论成功失败;<br/>
     * 可以避免一些重复代码 ;<br/>
     * 根据需求是否覆盖;
     *
     * @param extra
     */
    public void onBack(FFExtraParams extra) {
    }

    /**
     * 如果返回数据被统一处理拦截，同时又需要做一些后续处理可以覆盖此方法
     *
     * @param response
     * @param extraParams
     */
    public void onFailPublic(T response, FFExtraParams extraParams) {
    }

    /**
     * 请求成功执行此方法
     *
     * @param response
     * @param extra
     */
    public abstract void onSuccess(T response, FFExtraParams extra);

    /**
     * 网络请求失败的回调，当判断{@link FFBaseBean#isConsum(FFNetWorkRequest)}}返回true时不再回调
     *
     * @param extra
     * @return
     */
    public abstract boolean onFail(FFExtraParams extra);

    /**
     * 当服务器返回非正常结果时回调，当判断{@link FFBaseBean#judge()}}返回false时。<br/>
     * 可根据需求自行覆盖
     *
     * @param response
     * @param extra
     * @return
     */
    public boolean onFailContext(T response, FFExtraParams extra) {
        return false;
    }

    /**
     * 网络错误时回调 如超时，
     *
     * @param msg
     * @param ffResponseCode
     * @param extra
     * @return
     */
    public boolean onFailNet(String msg, FFResponseCode ffResponseCode, FFExtraParams extra) {
        return false;
    }

}
