package com.fengnian.smallyellowo.foodie.diningcase;

import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenglin on 2017-7-3.
 */

public class RecomRequestModel implements Serializable {
    public String shopId = "0";
    public String peopleTypeName;
    public String ptypes;
    public String tjID;
    public String longitude;
    public String latitude;
    public String address;
}
