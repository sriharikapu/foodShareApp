package com.fengnian.smallyellowo.foodie.appbase;

import com.fan.framework.http.FFNetWorkRequest;
import com.fan.framework.utils.FFLogUtil;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by elaine on 2017/6/14.
 */

public abstract class AbstrcPlatfromManager {

    private final Vector<FFNetWorkRequest> requests = new Vector<>();

    private final int status_init = 0;
    private final int status_executing = 1;
    private final int status_failed = 3;
    private final int status_success = 4;
    private final int status_canceled = 5;

    private final Object LOCK = new Object();

    private String token = null;
    private String openId = null;
    private int status = status_init;

    public synchronized boolean onRequestFailed(final FFNetWorkRequest request) {
        request.setHasFailed(true);
        switch (status) {
            case status_init: {
                synchronized (LOCK) {
                    status = status_executing;
                }
                requests.add(request);

                startReQuest(request);
            }
            return true;
            case status_executing: {
                requests.add(request);
            }
            return true;
            case status_failed: {
                onDestory();
                request.getCallBack().fail(request);
            }
            return false;
            case status_success: {
                onDestory();
                reExcuteRequest(request);
            }
            return false;
            case status_canceled: {
                onDestory();
                request.getCallBack().fail(request);
            }
            return false;
        }
        return false;
    }

    private void reExcuteRequest(FFNetWorkRequest request) {
        /*boolean bExisit = false;
        ArrayList<String> params = new ArrayList<>();
        for (int i = 0; i < request.getParams().length; i += 2) {

            for (Map.Entry<String, String> map : setRequestParams().entrySet()) {
                if (request.getParams()[i].equals(map.getKey())) {
                    request.getParams()[i + 1] = map.getValue();
                    bExisit = true;
                    break;
                }
            }

        }

        if (!bExisit) {
            for (int i = 0; i < request.getParams().length; i += 2) {
                params.add(request.getParams()[i].toString());
                params.add(request.getParams()[i + 1].toString());
            }
        }*/

        ArrayList<String> params = new ArrayList<>();
        for (int i = 0; i < request.getParams().length; i += 2) {
            if (setRequestParams().get(request.getParams()[i]) != null) {
                params.add(request.getParams()[i].toString());
                params.add(setRequestParams().get(request.getParams()[i]));
                setRequestParams().remove(setRequestParams().get(request.getParams()[i]));

            } else {
                params.add(request.getParams()[i].toString());
                params.add(request.getParams()[i + 1].toString());
            }
        }

        for (Map.Entry<String, String> map : setRequestParams().entrySet()) {
            params.add(map.getKey());
            params.add(map.getValue());
        }

        for (String p : params) {
            FFLogUtil.e("AbstrcPlatfromManager", "params = " + p);
        }
        request.setParams(params.toArray());
        request.getNet().excute(request);
    }

    /**
     * 授权成功
     *
     * @param map
     */
    public void onOssSuccess(HashMap<String, String> map) {
        synchronized (LOCK) {
            status = status_success;
        }

        if (map != null) {
            for (Map.Entry<String, String> params : map.entrySet()) {
                SP.saveKeyValue(params.getKey(), params.getValue());
            }
        }

        while (requests.size() > 0) {
            FFNetWorkRequest request = requests.remove(0);
            reExcuteRequest(request);
        }

    }

    /**
     * 授权失败回调
     */
    public void onOssFail() {
        synchronized (LOCK) {
            status = status_failed;
        }
        while (requests.size() > 0) {
            FFNetWorkRequest request = requests.remove(0);
            request.getCallBack().fail(request);
        }

        synchronized (LOCK){
            status = status_init;
        }
    }

    /**
     * 取消授权
     */
    public void onOssCanceled() {
        synchronized (LOCK) {
            status = status_canceled;
        }
        while (requests.size() > 0) {
            FFNetWorkRequest request = requests.remove(0);
            request.getCallBack().fail(request);
        }
        synchronized (LOCK){
            status = status_init;
        }
    }

    public class LoginData extends BaseResult {
        public SYUser getUser() {
            return user;
        }

        public void setUser(SYUser user) {
            this.user = user;
        }

        SYUser user;
    }

    abstract void startReQuest(FFNetWorkRequest request);

    abstract HashMap<String, String> setRequestParams();

    abstract void onDestory();
}
