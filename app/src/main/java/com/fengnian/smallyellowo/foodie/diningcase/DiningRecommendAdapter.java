package com.fengnian.smallyellowo.foodie.diningcase;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseAdapter;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.intentdatas.RestInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.ContextUtils;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-6-7.
 */

public class DiningRecommendAdapter extends MyBaseAdapter<SYFindMerchantInfo> {
    private DiningRecommendActivity mActivity;
    private int mFromWhere;
    private List<SYFindMerchantInfo> mCheckedList = new ArrayList<>();

    public DiningRecommendAdapter(DiningRecommendActivity activity, int fromWhere) {
        super(activity);
        mActivity = activity;
        mFromWhere = fromWhere;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.dining_recommend_item, null);
            holder.item_root = convertView.findViewById(R.id.item_root);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_business_name = (TextView) convertView.findViewById(R.id.tv_business_name);
            holder.check_box = (CheckBox) convertView.findViewById(R.id.check_box);
            holder.share_bottom = convertView.findViewById(R.id.share_bottom);
            holder.iv_xiang = (ImageView) convertView.findViewById(R.id.iv_xiang);
            holder.iv_guan = (ImageView) convertView.findViewById(R.id.iv_guan);
            holder.iv_jian = (ImageView) convertView.findViewById(R.id.iv_jian);
            holder.ff_jian = (ImageView) convertView.findViewById(R.id.ff_jian);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.share_people = (TextView) convertView.findViewById(R.id.share_people);
            holder.share_time = (TextView) convertView.findViewById(R.id.share_time);
            holder.rating_bar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
            holder.tv_avarge_money = (TextView) convertView.findViewById(R.id.tv_avarge_money);
            holder.share_num = (TextView) convertView.findViewById(R.id.share_num);
            holder.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            convertView.setTag(holder);

            if (mFromWhere == DiningRecommendActivity.TYPE_SINGLE) {
                holder.share_bottom.setVisibility(View.GONE);
                holder.check_box.setVisibility(View.INVISIBLE);
                holder.check_box.getLayoutParams().width = DisplayUtil.dip2px(3f);
            } else if (mFromWhere == DiningRecommendActivity.TYPE_MULTIPLE) {
                holder.iv_guan.setVisibility(View.GONE);
                holder.iv_jian.setVisibility(View.GONE);
                holder.iv_xiang.setVisibility(View.GONE);
                holder.tv_distance.setVisibility(View.GONE);
                holder.ff_jian.setVisibility(View.GONE);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SYFindMerchantInfo item = getItem(position);
        setText(holder, item);
        setImage(holder, item);
        setListener(holder, item, position);
        setFoodFlagIcon(holder, item);

        if (mFromWhere == DiningRecommendActivity.TYPE_SINGLE) {
            setIcon(holder, item);
        } else if (mFromWhere == DiningRecommendActivity.TYPE_MULTIPLE) {
            holder.check_box.setChecked(mCheckedList.contains(item));
            setShareText(holder, item);
        }

        return convertView;
    }

    private void setIcon(ViewHolder holder, SYFindMerchantInfo item) {
        int resId = 0;
        if (item.isMerchantIsTu()) {
            resId = R.drawable.dining_icon_tu_1;
        } else if (item.isMerchantIsXiang()) {
            resId = R.drawable.dining_icon_xiang_4;
        } else if (item.isMerchantIsPin()) {
            resId = R.drawable.dining_icon_pin_3;
        } else if (item.isMerchantIsJin()) {
            resId = R.drawable.dining_icon_jin_2;
        }

        if (resId > 0) {
            holder.ff_jian.setImageResource(resId);
            holder.ff_jian.setVisibility(View.VISIBLE);
        } else {
            holder.ff_jian.setImageResource(0);
            holder.ff_jian.setVisibility(View.GONE);
        }
    }

    private void setFoodFlagIcon(ViewHolder holder, SYFindMerchantInfo item) {
        if (item.isMerchantIsWant()) {
            holder.iv_xiang.setVisibility(View.VISIBLE);
        } else {
            holder.iv_xiang.setVisibility(View.GONE);
        }

        if (item.isMerchantIsRelation()) {
            holder.iv_guan.setVisibility(View.VISIBLE);
        } else {
            holder.iv_guan.setVisibility(View.GONE);
        }

        if (item.isMerchantIsDa()) {
            holder.iv_jian.setVisibility(View.VISIBLE);
        } else {
            holder.iv_jian.setVisibility(View.GONE);
        }
    }

    private void setText(ViewHolder holder, SYFindMerchantInfo item) {
        if (!TextUtils.isEmpty(item.getMerchantName())) {
            holder.tv_title.setText(item.getMerchantName());
        } else {
            holder.tv_title.setText("");
        }

        if (!TextUtils.isEmpty(item.getMerchantKind())) {
            holder.tv_business_name.setText(item.getMerchantKind());
        } else {
            holder.tv_business_name.setText("");
        }

        if (item.getMerchantPrice() > 0) {
            holder.tv_avarge_money.setVisibility(View.VISIBLE);
            holder.tv_avarge_money.setText("¥" + item.getMerchantPrice() + "/人");
        } else {
            holder.tv_avarge_money.setVisibility(View.GONE);
            holder.tv_avarge_money.setText("");
        }

        if (item.getStartLevel() > 0) {
            holder.tv_level.setVisibility(View.VISIBLE);
            holder.rating_bar.setVisibility(View.VISIBLE);
            holder.tv_level.setText(item.getStartLevel() + "");
            holder.rating_bar.setRating(item.getStartLevel());
        } else {
            holder.tv_level.setVisibility(View.GONE);
            holder.rating_bar.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getMerchantDistance())) {
            holder.tv_distance.setText(item.getMerchantDistance());
        } else {
            holder.tv_distance.setText("");
        }

        if (!TextUtils.isEmpty(item.getFriendShares())) {
            holder.share_num.setText(item.getFriendShares() + "位圈友分享过");
        } else {
            holder.share_num.setText("");
        }

        if (!TextUtils.isEmpty(item.getMerchantArea())) {
            holder.tv_area.setText(item.getMerchantArea());
        } else {
            holder.tv_area.setText("");
        }
    }

    private void setShareText(ViewHolder holder, SYFindMerchantInfo item) {
        if (item.getUserHeaderImage() != null && !TextUtils.isEmpty(item.getUserHeaderImage().getThumbUrl())) {
            FFImageLoader.loadAvatar(mActivity, item.getUserHeaderImage().getThumbUrl(), holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(0);
        }

        if (!TextUtils.isEmpty(item.getUserName())) {
            holder.share_people.setText(item.getUserName() + "分享了美食编辑");
        } else {
            holder.share_people.setText("");
        }

        if (!TextUtils.isEmpty(item.getCreateTime()) && TextUtils.isDigitsOnly(item.getCreateTime())) {
            holder.share_time.setText(ContextUtils.getFriendlyTime(Long.parseLong(item.getCreateTime()), false));
        } else {
            holder.share_time.setText("");
        }

    }


    private void setImage(ViewHolder holder, SYFindMerchantInfo item) {
        if (item.getMerchantImage() != null && !TextUtils.isEmpty(item.getMerchantImage().getThumbUrl())) {
            FFImageLoader.loadMiddleImage(mActivity, item.getMerchantImage().getThumbUrl(), holder.image);
        } else {
            holder.image.setImageResource(0);
        }
    }

    private void setListener(ViewHolder holder, final SYFindMerchantInfo item, final int position) {
        holder.check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckedList.size() > 5) {
                    mActivity.showToast("最多选择5个哦~~");
                    return;
                }

                if (!mCheckedList.contains(item)) {
                    mCheckedList.add(item);
                } else {
                    mCheckedList.remove(item);
                }

                notifyDataSetChanged();
                mActivity.setCheckedTextShow(mCheckedList.size());
            }
        });

        holder.item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mActivity.pushEvent107(position+"", item);

                RestInfoIntent intent = new RestInfoIntent();
                intent.setId(item.getMerchantUid());
                intent.setStar(item.getStartLevel());
                mActivity.startActivity(RestInfoActivity.class, intent);
            }
        });
    }

    public List<SYFindMerchantInfo> getCheckedList() {
        return mCheckedList;
    }

    private static class ViewHolder {
        public View item_root;
        public ImageView image;
        public TextView tv_business_name;
        public TextView tv_title;
        public CheckBox check_box;
        public View share_bottom;
        public ImageView iv_xiang;
        public ImageView iv_guan;
        public ImageView iv_jian;
        public ImageView ff_jian;
        public TextView tv_distance;
        public ImageView iv_avatar;
        public TextView share_people;
        public TextView share_time;
        public RatingBar rating_bar;
        public TextView tv_level;
        public TextView tv_avarge_money;
        public TextView share_num;
        public TextView tv_area;
    }
}
