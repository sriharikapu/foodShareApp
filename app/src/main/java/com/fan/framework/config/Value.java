package com.fan.framework.config;

import com.fengnian.smallyellowo.foodie.bean.publics.ImageItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-4-12.
 */

public class Value {
    public static double mLatitude, mLongitude;
    public static final String GDkey = "7bc922a36370604b885df9eb15a7b147";
    public static final String GD_AROUND = "http://restapi.amap.com/v3/place/around";
    public static final String GD_AROUND_SEARCH = "http://restapi.amap.com/v3/place/text";
    public static final int GD_AROUND_RADIUS = 10000;
    public static List<ImageItem> PICS_LIST = new ArrayList<>();
}
