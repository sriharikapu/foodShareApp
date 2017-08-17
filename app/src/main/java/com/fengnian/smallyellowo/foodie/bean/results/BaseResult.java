package com.fengnian.smallyellowo.foodie.bean.results;

import com.fan.framework.http.FFBaseBean;
import com.fan.framework.http.FFNetWorkRequest;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.PlatformEngine;
import com.fengnian.smallyellowo.foodie.appbase.WeiChatOverdueManager;

/**
 * Created by Administrator on 2016-7-18.
 */
public class BaseResult implements FFBaseBean {
    private String serverMsg;
    private int errorCode;
    private String updateUrl;
    private String newVersion;

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    @Override
    public boolean judge() {
        return 0 == errorCode;
    }

    @Override
    public String getErrorMessage() {
        return serverMsg;
    }

    @Override
    public boolean isConsum(FFNetWorkRequest request) {
//        0 接口请求成功
//        1 token失效
//        2 未知错误
//        3 参数不合法
//        4 操作失败
//        5 无权限执行此操作
//        6 系统异常
//        7 邀请码错误或已被使用
//        8 短信发送过于频繁
//        9 该账号已冻结，请联系小黄圈官方微信：tinydonuts
//        10 您当前版本过低，需要更新后才能继续使用
        switch (errorCode) {
            case 1://token失效
                BaseActivity.logout(errorCode, null);
                return true;
            case 9://该账号已冻结，请联系小黄圈官方微信：tinydonuts
                BaseActivity.logout(errorCode, null);
                return true;
            case 10://您当前版本过低，需要更新后才能继续使用
                BaseActivity.logout(errorCode, updateUrl);
                return true;

            // 微信登录过期
            case 601:
                if (request.isHasFailed()) {
                    BaseActivity.logout(errorCode, null);
                    return true;
                }
                return WeiChatOverdueManager.onRequestFailed(request);

            // QQ登录过期
            case 901:
                if (request.isHasFailed()) {
                    BaseActivity.logout(errorCode, null);
                    return true;
                }
                return PlatformEngine.getInstance().getTencentManager().onRequestFailed(request);

            // 微博登录过期
            case 1001:
                if (request.isHasFailed()) {
                    BaseActivity.logout(errorCode, null);
                    return true;
                }
                return PlatformEngine.getInstance().getSinaManager().onRequestFailed(request);

        }
        return false;
    }

    @Override
    public boolean isNoData() {
        return false;
    }

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
