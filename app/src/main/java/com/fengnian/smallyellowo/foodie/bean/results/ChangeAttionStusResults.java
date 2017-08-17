package com.fengnian.smallyellowo.foodie.bean.results;

/**
 * 是否关注
 */

public class ChangeAttionStusResults extends BaseResult {

    private String result;//本次业务请求是否成功
    private String returnmessage;//返回错误信息message
    private String attentionState;//关注状态 （00、01均是未关注状态）10已关注 11互相关注

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getReturnmessage() {
        return returnmessage;
    }

    public void setReturnmessage(String returnmessage) {
        this.returnmessage = returnmessage;
    }

    public String getAttentionState() {
        return attentionState;
    }

    public void setAttentionState(String attentionState) {
        this.attentionState = attentionState;
    }
}
