package com.fengnian.smallyellowo.foodie.bean.results;

import java.util.List;

/**
 * Created by Administrator on 2016-10-21.
 * 添加水单识别 返回
 */

public class MemoReult  {

    private  String serverMsg;
    private  String errorCode;//0 是成功   别的都是识别失败
    private List<String> resultList;

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getResultList() {
        return resultList;
    }

    public void setResultList(List<String> resultList) {
        this.resultList = resultList;
    }
}
