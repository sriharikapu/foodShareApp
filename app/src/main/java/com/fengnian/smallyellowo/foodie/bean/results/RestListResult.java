package com.fengnian.smallyellowo.foodie.bean.results;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-9-7.
 */
public class RestListResult extends BasePullToRefreshResult<SYFindMerchantInfo> {
    private String nowWeight;
    private List<SYFindMerchantInfo> merchantList;

    public String getNowWeight() {
        return nowWeight;
    }

    public void setNowWeight(String nowWeight) {
        this.nowWeight = nowWeight;
    }

    public List<SYFindMerchantInfo> getMerchantList() {
        return merchantList;
    }

    public void setMerchantList(List<SYFindMerchantInfo> merchantList) {
        this.merchantList = merchantList;
    }

    @Override
    public List getData() {
        return merchantList;
    }
}
