package com.fengnian.smallyellowo.foodie.bean.results;

import com.fan.framework.utils.FFUtils;

import java.util.List;

/**
 * Created by Administrator on 2017-5-31.
 */

public abstract class BasePullToRefreshResult<D> extends BaseResult {
    public abstract List<D> getData();

    @Override
    public boolean isNoData() {
        return FFUtils.isListEmpty(getData());
    }
}
