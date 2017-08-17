package com.fengnian.smallyellowo.foodie.bean.publish;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016-10-20.
 */

@DatabaseTable
public class NativeNetImageMap {
    @DatabaseField(id = true)
    private String netImage;

    @DatabaseField
    private String nativeImage;

    @DatabaseField
    private long createTime;

    @DatabaseField
    private String foodId;

}
