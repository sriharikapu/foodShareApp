package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.fengnian.smallyellowo.foodie.bean.publics.DotInfo;

import java.util.List;


public class MyPageAdapter extends PagerAdapter {

    private Context mcontext;
    private List<View> mlist;

    private List<DotInfo> templist;

    private int pos = 0;
    private int Pos = 0;

    public MyPageAdapter(List<View> mlist, List<DotInfo> templist, Context mcontext) {
        this.mlist = mlist;
        this.mcontext = mcontext;
        this.templist = templist;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        if (mlist.size() != 0)
            pos = position % mlist.size();
        else return "";

        View view = mlist.get(pos);
        if (view.getParent() == null)
            container.addView(view);
        else {
            ((ViewGroup) (view.getParent())).removeView(view);
            container.addView(view);
        }
        return view;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int pos = 0;
        if (mlist.size() != 0) {
            pos = position % mlist.size();
        } else return;
        View view = mlist.get(pos);
        if (container.getChildAt(position) == view)
            container.removeView(view);
    }

    @Override
    public int getCount() {
        if (templist == null) {
            return 0;
        }

        return templist.size();
        /*if(templist.size()==1) return 1;
        return Integer.MAX_VALUE;*/
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
