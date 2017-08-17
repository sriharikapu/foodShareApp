package com.fengnian.smallyellowo.foodie.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-3-1.
 */

public class ClubMyselfIntroductionAdapter extends BaseAdapter {
    private FFContext mcontext;
    private List<String> mlist = new ArrayList<String>();
    int height = FFUtils.getDisWidth();

    public ClubMyselfIntroductionAdapter(FFContext context) {
        this.mcontext = context;
    }

    public void setDataList(List<String> list){
        if (list != null){
            mlist.clear();
            mlist.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewholder holder = null;
        if (convertView == null) {
            holder = new viewholder();
            convertView = View.inflate((Context) mcontext, R.layout.item_club_myself_introducion, null);
            holder.image = (DynamicImageView) convertView.findViewById(R.id.image);
            holder.image.setWidth(DisplayUtil.screenWidth);
            convertView.setTag(holder);
        } else {
            holder = (viewholder) convertView.getTag();
        }
        FFImageLoader.loadOriginalImage(mcontext, mlist.get(position), holder.image);
//        FFImageLoader.load_base(mcontext, mlist.get(position), holder.image, true, 10000, 10000, R.drawable.alpha, FFImageLoader.TYPE_NONE, null);
        return convertView;
    }

    private class viewholder {
        private DynamicImageView image;
    }
}
