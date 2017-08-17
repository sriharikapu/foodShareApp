package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.personal.MyActions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-6-19.
 */

public class NearbyGdDiningRoomAdapter extends BaseAdapter {
    private BaseActivity mActivity;
    private List<GDPoiModel> mDataList = new ArrayList<>();

    private NearbyGdDiningRoomAdapter() {
    }

    public NearbyGdDiningRoomAdapter(BaseActivity activity) {
        mActivity = activity;

    }

    public void setDataList(List<GDPoiModel> list) {
        if (list == null) {
            return;
        }
        mDataList.clear();
        appendDataList(list);
    }

    public void appendDataList(List<GDPoiModel> list) {
        if (list == null) {
            return;
        }
        mDataList.addAll(list);
        this.notifyDataSetChanged();
    }

    public List<GDPoiModel> getDataList() {
        return mDataList;
    }

    /**
     * 传入用户曾经选择过的地理位置，放在第一位
     */
    public void setGDPoiModel(GDPoiModel model) {
        if (model != null && !model.isCurrentLocation) {
            mDataList.add(0, model);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public GDPoiModel getItem(int position) {
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
            convertView = View.inflate(mActivity, R.layout.nearby_room_adapter_item, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_title_2 = (TextView) convertView.findViewById(R.id.tv_title_2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GDPoiModel item = getItem(position);

        holder.tv_title_2.setText(item.cityname + item.adname + item.address);
        setTitle(holder, item);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActions.ACTION_GD_POI);
                intent.putExtra("item", item);
                LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
                mActivity.finish();
            }
        });

        return convertView;
    }

    private void setTitle(ViewHolder holder, GDPoiModel item) {
        if (item.isCurrentLocation) {
            holder.tv_title.setText(getTextSpan("[当前] ", item.name));
        } else {
            holder.tv_title.setText(item.name);
        }
    }

    private SpannableString getTextSpan(final String spanText, String text) {
        SpannableString spannableString = new SpannableString(spanText + text);
        int color = mActivity.getResources().getColor(R.color.search_color);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, spanText.length(), 0);
        return spannableString;
    }

    private static class ViewHolder {
        public TextView tv_title;
        public TextView tv_title_2;
    }
}
