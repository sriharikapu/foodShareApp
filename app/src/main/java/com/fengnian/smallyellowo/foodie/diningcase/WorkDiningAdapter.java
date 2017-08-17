package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseAdapter;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.RestInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-6-7.
 */

public class WorkDiningAdapter extends MyBaseAdapter<SYFindMerchantInfo> {
    private WorkDiningActivity mActivity;
    private boolean isToTop = false;
    private boolean isShareState = false;
    private List<SYFindMerchantInfo> mCheckedList = new ArrayList<>();

    public WorkDiningAdapter(WorkDiningActivity activity) {
        super(activity);
        mActivity = activity;
    }

    public void setToTop(boolean isTop) {
        if (isToTop != isTop) {
            isToTop = isTop;
            notifyDataSetChanged();
        }
    }

    public boolean isToTop() {
        return isToTop;
    }

    public void setShareState(boolean isShare) {
        isShareState = isShare;
        notifyDataSetChanged();
    }

    public boolean isShareState() {
        return isShareState;
    }

    public void setSelectAll(boolean isSelectAll) {
        if (isSelectAll) {
            for (int i = 0; i < getCount(); i++) {
                mCheckedList.add(getItem(i));
            }
        } else {
            mCheckedList.clear();
        }
        notifyDataSetChanged();
    }

    public List<SYFindMerchantInfo> getCheckedList() {
        return mCheckedList;
    }

    public void showDeleteDialog(final SYFindMerchantInfo model) {
        EnsureDialog.Builder dialog = new EnsureDialog.Builder(mActivity);
        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                delete(model);

            }
        }).create().show();
        dialog.setCancelable(true);
    }

    private void delete(final SYFindMerchantInfo model) {
//        mActivity.post(Constants.shareConstants().getNetHeaderAdress() + "/weekdayLunch/delShopToWeekdayLunch.do",
        mActivity.post(IUrlUtils.WorkDining.delShopToWeekdayLunch,
                "正在删除", null, new FFNetWorkCallBack<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                        if (response != null && response.getErrorCode() == 0) {
                            mActivity.showToast("删除成功");
                            remove(model);
                            mActivity.setMyDiningListPageState();
                            mActivity.setAllEmptyView();
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "shopId", model.getMerchantUid());
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mActivity, R.layout.work_dining_item, null);
            holder.item_root = convertView.findViewById(R.id.item_root);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_business_name = (TextView) convertView.findViewById(R.id.tv_business_name);
            holder.check_box = (CheckBox) convertView.findViewById(R.id.check_box);
            holder.rating_bar = (RatingBar) convertView.findViewById(R.id.rating_bar);
            holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
            holder.tv_avarge_money = (TextView) convertView.findViewById(R.id.tv_avarge_money);
            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.lin_2 = convertView.findViewById(R.id.lin_2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final SYFindMerchantInfo item = getItem(position);

        if (item.getMerchantImage() != null && !TextUtils.isEmpty(item.getMerchantImage().getThumbUrl())) {
            FFImageLoader.loadMiddleImage(mActivity, item.getMerchantImage().getThumbUrl(), holder.image);
        } else {
            holder.image.setImageResource(0);
        }

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
            holder.lin_2.setVisibility(View.VISIBLE);
            holder.tv_avarge_money.setText("¥" + item.getMerchantPrice() + "/人");
        } else {
            holder.tv_avarge_money.setVisibility(View.GONE);
            holder.lin_2.setVisibility(View.GONE);
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

        if (isShareState) {
            holder.check_box.setVisibility(View.VISIBLE);
            holder.check_box.setChecked(mCheckedList.contains(item));
        } else {
            holder.check_box.setVisibility(View.GONE);
        }

        holder.check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCheckedList.contains(item)) {
                    mCheckedList.add(item);
                } else {
                    mCheckedList.remove(item);
                }
                notifyDataSetChanged();

                boolean isSelectAll = mCheckedList.size() == getCount();

                if (isSelectAll) {
                    mActivity.setSelectAllText(R.string.un_select_all);
                } else {
                    mActivity.setSelectAllText(R.string.select_all);
                }
            }
        });

        holder.item_root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (FFUtils.checkNet()) {
                    showDeleteDialog(item);
                } else {
                    mActivity.showToast("您的网络不好哦!");
                }
                return true;
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

        return convertView;
    }

    private static class ViewHolder {
        public View item_root;
        public ImageView image;
        public TextView tv_business_name;
        public TextView tv_title;
        public View s_status_bar;
        public CheckBox check_box;
        public RatingBar rating_bar;
        public TextView tv_level;
        public TextView tv_avarge_money;
        public TextView tv_distance;
        public View lin_2;
    }
}
