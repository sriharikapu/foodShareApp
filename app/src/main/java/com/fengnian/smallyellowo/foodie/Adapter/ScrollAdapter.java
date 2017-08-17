package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.fengnian.smallyellowo.foodie.bean.publics.ImageItem;

import java.util.List;

/**
 * Created by Administrator on 2017-3-6.
 */

public class ScrollAdapter extends PagerAdapter {
    List<ImageItem> list;
    private Context mcontext;
    public ScrollAdapter(List<ImageItem> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

      return  null;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }
    @Override
    public int getCount() {

        return list.size();
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
