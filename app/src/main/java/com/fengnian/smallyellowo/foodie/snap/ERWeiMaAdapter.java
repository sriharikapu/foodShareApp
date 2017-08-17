package com.fengnian.smallyellowo.foodie.snap;

import com.fengnian.smallyellowo.foodie.feeddetail.snap.OnSnapViewCreatedListener;

/**
 * Created by Administrator on 2017-3-1.
 */

public interface ERWeiMaAdapter {


    public  int getCount();

    public  void getView(int position, OnSnapViewCreatedListener listener);
}
