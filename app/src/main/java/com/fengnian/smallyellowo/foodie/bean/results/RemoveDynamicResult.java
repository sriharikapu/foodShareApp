package com.fengnian.smallyellowo.foodie.bean.results;

/**
 * Created by Administrator on 2016-10-28.
 */

public class RemoveDynamicResult extends BaseResult {
    private PublishLimitBean publishLimit;

    public PublishLimitBean getPublishLimit() {
        return publishLimit;
    }

    public void setPublishLimit(PublishLimitBean publishLimit) {
        this.publishLimit = publishLimit;
    }

    public static class PublishLimitBean {
        private int limitScore;
        private String msg;

        public int getLimitScore() {
            return limitScore;
        }

        public void setLimitScore(int limitScore) {
            this.limitScore = limitScore;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
