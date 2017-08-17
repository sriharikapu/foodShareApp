package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.PublishLimit;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-12-5.
 */

public class PublishLimitResult extends BaseResult implements Serializable {

  private PublishLimit publishLimit;

    public PublishLimit getPublishLimit() {
        return publishLimit;
    }

    public void setPublishLimit(PublishLimit publishLimit) {
        this.publishLimit = publishLimit;
    }
}
