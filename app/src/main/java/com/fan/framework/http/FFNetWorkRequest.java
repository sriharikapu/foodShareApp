package com.fan.framework.http;

import com.fan.framework.base.FFApplication;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

//import java.io.File;

public class FFNetWorkRequest<T extends FFBaseBean> {

    protected static final String TAG = "FFNetWorkRequest";

    private final FFNetWork net;

    private boolean hasFailed = false;
    TreeMap<Object, Object> map1;
    private Object[] params = null;
    private int dialogId;
    private String url;
    private FFNetWorkCallBack<T> callBack;
    private FFResponseCode status = FFResponseCode.UNSET;
    private T entity;
    private String word;
    private String errMessage = "初始化请求";
    private HashMap<String, File> fileMaps = new HashMap<String, File>();
    private Class<T> clazz;
    private FFExtraParams extraParams;
    private String entityString;

    public FFNetWorkRequest(FFNetWork net, String word, String url, FFNetWorkCallBack<T> callBack, HashMap<String, File> fileMaps, Object[] params, FFExtraParams extraParams, TreeMap<Object, Object> map1) {
        this.params = params;
        this.net = net;
        this.url = url;
        this.callBack = callBack;
        if (word != null && word.length() == 0) {
            word = FFApplication.app.getResources().getString(R.string.loading);
        }
        this.word = word;
        this.addFileMaps(fileMaps);
        if (callBack.clazz == null) {
            this.clazz = FFUtils.getTClass(callBack);
        } else {
            this.clazz = callBack.clazz;
        }
        this.extraParams = new FFExtraParams(extraParams);
        this.map1 = map1;
    }

    public FFNetWork getNet() {
        return net;
    }

    public boolean isHasFailed() {
        return hasFailed;
    }

    public void setHasFailed(boolean hasFailed) {
        this.hasFailed = hasFailed;
    }

    public String getCacheKey() {
        return getUrl() + FFNetWorkUtils.getGetString(getParams());
    }

    public void setStatus(FFResponseCode status, String message) {
        this.errMessage = message;
        this.status = status;
        FFLogUtil.e("请求结果" + hashCode(), getErrMessage());
    }

    public void setEntity(T entity, String entityString) {
        this.entity = entity;
        this.entityString = entityString;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public void addFileMaps(HashMap<String, File> map) {
        if (map != null && map.size() > 0) {
            this.fileMaps.putAll(map);
        }
    }

    public HashMap<String, File> getFileMaps() {
        return fileMaps;
    }

//    public File[] getFiles() {
//        return files;
//    }
//
//    public void setFiles(File[] files) {
//        this.files = files;
//    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public String getEntityString() {
        return entityString;
    }

    public void setEntityString(String entityString) {
        this.entityString = entityString;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public FFResponseCode getStatus() {
        return status;
    }

    public void setStatus(FFResponseCode status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public FFExtraParams getExtraParams() {
        return extraParams;
    }

    public void setExtraParams(FFExtraParams extraParams) {
        this.extraParams = extraParams;
    }

    public FFNetWorkCallBack<T> getCallBack() {
        return callBack;
    }

    public void setCallBack(FFNetWorkCallBack<T> callBack) {
        this.callBack = callBack;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getDialogId() {
        return dialogId;
    }

    public void setDialogId(int dialogId) {
        this.dialogId = dialogId;
    }
}
