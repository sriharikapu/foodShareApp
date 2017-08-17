/*
 * Date: 14-8-18
 * Project: Access-Control-V2
 */
package com.fengnian.smallyellowo.foodie.appbase;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.fan.framework.utils.FFLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的PagerAdapter实现类,封装了内容为View的公共操作.
 * Author: msdx (645079761@qq.com)
 * Time: 14-8-18 下午2:34
 */
public abstract class AbstractViewPagerAdapter<T> extends PagerAdapter {
    protected List<T> mData;
    private ArrayList<View> mViews;
    private SparseArray<View> views;

    public AbstractViewPagerAdapter(List<T> data) {
        mData = data;
        mViews = new ArrayList<>();
        views = new SparseArray<>(data.size());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        if (mViews.size() != 0) {
            view = mViews.remove(0);
        } else {
            view = newView();
        }

        views.put(position, view);

        refreshView(view, position);

        container.addView(view);
        return view;
    }

    public abstract View newView();

    public abstract void refreshView(View view, int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = views.get(position);
        if (view != null) {
            views.remove(position);
            container.removeView(view);
            mViews.add(view);
        } else {
            FFLogUtil.e("ViewPager", "我擦回收的view为空，不应该啊");
        }
    }

    public T getItem(int position) {
        return mData.get(position);
    }
}