package com.fengnian.smallyellowo.foodie.homepage;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.CommonWebviewUtilActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.AdModelsList;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.DynamicImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-4-12.
 */

public class HomeChildMeetAdapter extends BaseAdapter {
    private HomeChildMeetFrag mFragment;
    private List<AdModelsList.ItemsAd> mDataList = new ArrayList<>();
    private boolean isClicked = false;

    private HomeChildMeetAdapter() {
    }

    public HomeChildMeetAdapter(HomeChildMeetFrag frag) {
        mFragment = frag;
    }

    public void setDataList(List<AdModelsList.ItemsAd> list) {
        if (list == null) {
            return;
        }
        mDataList.clear();
        appendDataList(list);
    }

    public void appendDataList(List<AdModelsList.ItemsAd> list) {
        if (list == null) {
            return;
        }
        mDataList.addAll(list);
        this.notifyDataSetChanged();


//        //测试代码
//        for (int i = 0; i < 30; i++) {
//            SYHomeActivityModel model = new SYHomeActivityModel();
//            model.imageUrl = "http://resource.58game.com/uploads/tuku/thumb/20160126/56a6d7ba3896f.jpg";
//            model.messageTitle = "周末去哪儿长白山周末去哪儿长白山周末去哪儿长白山";
//            model.messageContent = "人参好吃人参好吃人参好吃人参好吃人参好吃人参好吃";
//            model.messageHtmlUrl = "http://wap.baidu.com";
//            model.messageTime = "2017-05-08 11:45:20";
//            mDataList.add(model);
//        }
        this.notifyDataSetChanged();
    }

    public List<AdModelsList.ItemsAd> getDataList() {
        return mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public AdModelsList.ItemsAd getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mFragment.getActivity(), R.layout.home_child_meet_adapter_item, null);
            holder.image = (DynamicImageView) convertView.findViewById(R.id.image);
            holder.image.setWidth(DisplayUtil.screenWidth);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AdModelsList.ItemsAd item = getItem(position);
        if (item == null) {
            item = new AdModelsList.ItemsAd();
        }

        setMeetImage(holder, item);
        return convertView;
    }

    public void onResume() {
        isClicked = false;
    }

    private void setMeetImage(ViewHolder holder, final AdModelsList.ItemsAd item) {
        if (!TextUtils.isEmpty(item.image)) {
            FFImageLoader.loadBigImage((FFContext) mFragment.getActivity(), item.image, holder.image);
        } else {
            holder.image.setImageResource(0);
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    isClicked = true;
                    if (!TextUtils.isEmpty(item.targetUrl)) {
                        WebInfo info = new WebInfo();
                        info.setUrl(item.targetUrl + "");
                        info.setTitle(item.title);
                        mFragment.startActivity(CommonWebviewUtilActivity.class, info);
                    }
                }
            }
        });
    }

    private static class ViewHolder {
        public DynamicImageView image;
    }
}
