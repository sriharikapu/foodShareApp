package com.fengnian.smallyellowo.foodie.personal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fan.framework.base.FFContext;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;
import com.fengnian.smallyellowo.foodie.WantEatDetailActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYBusiness;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.WanEatDetailIntent;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-3-23.
 */

public class WantFoodFragAdapter extends RecyclerView.Adapter<WantFoodViewHolder> {
    private WantFoodListFragment mFragment;
    private List<DeliciousFoodModel.SYSearchUserFoodModel> mDataList = new ArrayList<>();
    private int mLoadState;
    public boolean isFilterEmpty = false;

    public WantFoodFragAdapter(WantFoodListFragment fragment) {
        mFragment = fragment;
    }

    public void setLoadState(int loadState) {
        mLoadState = loadState;
        this.notifyDataSetChanged();
    }

    public void setDataList(List<DeliciousFoodModel.SYSearchUserFoodModel> list) {
        if (list == null) {
            DeliciousFoodModel.SYSearchUserFoodModel emptyModel = new DeliciousFoodModel.SYSearchUserFoodModel();
            emptyModel.type = DeliciousFoodModel.EMPTY;
            mDataList.add(emptyModel);
            this.notifyDataSetChanged();
            return;
        }
        mDataList.clear();
        appendDataList(list);
    }

    public void appendDataList(List<DeliciousFoodModel.SYSearchUserFoodModel> list) {
        if (list == null) {
            return;
        }
        mDataList.addAll(list);
        this.notifyDataSetChanged();
    }

    public List<DeliciousFoodModel.SYSearchUserFoodModel> getDataList() {
        return mDataList;
    }

    @Override
    public WantFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mFragment.getActivity(), R.layout.want_eat_food_item, null);
        WantFoodViewHolder holder = new WantFoodViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(WantFoodViewHolder holder, int position) {
        DeliciousFoodModel.SYSearchUserFoodModel item = mDataList.get(position);
        if (item == null) {
            return;
        }
        if (getItemCount() == 1 && item.type == DeliciousFoodModel.EMPTY) {
            setEmptyView(true, holder, item);
            return;
        } else {
            setEmptyView(false, holder, item);
        }
        setItemClick(holder, item);
        setFoodImage(holder, item);
        setTitle(holder, item);
        setRatingBar(holder, item);
        setLocation(holder, item);
        setShopInfo(holder, item);
        setLoadMoreState(holder, position);
        hasEated(holder, item);
    }

    private void setEmptyView(boolean isEmpty, WantFoodViewHolder holder, final DeliciousFoodModel.SYSearchUserFoodModel item) {
        LinearLayout parentLinear = null;
        if (holder.itemView instanceof LinearLayout) {
            parentLinear = (LinearLayout) holder.itemView;
        }
        if (parentLinear == null) {
            return;
        }

        if (holder.emptyView != null) {
            parentLinear.removeView(holder.emptyView);
            holder.emptyView = null;
        }

        if (isEmpty) {
            holder.item_root.setVisibility(View.GONE);
            holder.line_bottom.setVisibility(View.GONE);
            holder.footerView.setVisibility(View.GONE);

            holder.emptyView = View.inflate(mFragment.getActivity(), R.layout.ff_empty_layout, null);
            holder.emptyView.findViewById(R.id.text).setVisibility(View.GONE);
            ImageView imageView = (ImageView) holder.emptyView.findViewById(R.id.image);
            int height = DisplayUtil.screenHeight
                    - FFUtils.getStatusbarHight(mFragment.getActivity())
                    - mFragment.getResources().getDimensionPixelSize(R.dimen.profile_bg_height)
                    - mFragment.getResources().getDimensionPixelSize(R.dimen.user_tab_height)
                    - mFragment.getResources().getDimensionPixelSize(R.dimen.user_tab_filter_item_height)
                    - DisplayUtil.dip2px(58f);
            if (isFilterEmpty) {
                imageView.setImageResource(R.drawable.want_eat_list_filter_empty);
            } else {
                imageView.setImageResource(R.drawable.want_eat_empty_icon);
            }
            LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(DisplayUtil.screenWidth, height);
            parentLinear.addView(holder.emptyView, 0, emptyParams);
        } else {
            holder.item_root.setVisibility(View.VISIBLE);
            holder.line_bottom.setVisibility(View.VISIBLE);
            holder.footerView.setVisibility(View.VISIBLE);
        }
    }

    private void setItemClick(WantFoodViewHolder holder, final DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    mFragment.showToast(mFragment.getString(R.string.lsq_network_connection_interruption));
                    return;
                }

                WanEatDetailIntent eat = new WanEatDetailIntent();
                eat.setRecordId(item.recordId);
                eat.setBusinessId(item.merchantId);
                eat.setEatId(item.wantEatId);
                eat.setBus(WantFoodFragAdapter.getSYBusiness(item));
                ((BaseActivity) mFragment.getActivity()).startActivity(WantEatDetailActivity.class, eat);
            }
        });

        holder.item_root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (FFUtils.checkNet()) {
                    getPopWindow(item);
                } else {
                    mFragment.showToast("您的网络不好哦!");
                }
                return true;
            }
        });
    }

    private void setFoodImage(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (!TextUtils.isEmpty(item.foodImage)) {
            FFImageLoader.loadSmallImage((FFContext) mFragment.getActivity(), item.foodImage, holder.image);
        } else {
            holder.image.setImageResource(0);
        }
    }

    private void setTitle(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (!TextUtils.isEmpty(item.merchantName)) {
            holder.tv_title.setText(item.merchantName);
        } else {
            holder.tv_title.setText("");
        }
    }

    private void setRatingBar(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (item.starLevel > 0) {
            holder.rating_bar.setVisibility(View.VISIBLE);
            holder.tv_level.setVisibility(View.VISIBLE);
            holder.rating_bar.setRating(Float.parseFloat(item.starLevel + ""));
            holder.tv_level.setText(getSubFloat(item.starLevel));

            //设置ratingBar高度
            int ratingBarHeight = mFragment.getResources().getDrawable(R.drawable.rating_detail_light).getIntrinsicHeight();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.rating_bar.getLayoutParams();
            params.height = ratingBarHeight;
            holder.rating_bar.setLayoutParams(params);
        } else {
            holder.rating_bar.setVisibility(View.GONE);
            holder.tv_level.setVisibility(View.GONE);
        }
    }

    private void setShopInfo(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (!TextUtils.isEmpty(item.ptype)) {
            holder.tv_business_name.setVisibility(View.VISIBLE);
            holder.tv_business_name.setText(item.ptype);
        } else {
            holder.tv_business_name.setVisibility(View.GONE);
            holder.tv_business_name.setText("");
        }

        //人均
        holder.tv_avarge_money.setVisibility(View.GONE);
        holder.tv_avarge_money.setText("");
        if (!TextUtils.isEmpty(item.merchantPrice) && TextUtils.isDigitsOnly(item.merchantPrice.replace(".", ""))) {
            double price = Double.parseDouble(item.merchantPrice);
            if (price > 0) {
                holder.tv_avarge_money.setVisibility(View.VISIBLE);
                holder.tv_avarge_money.setText("¥" + FFUtils.formatDouble(price, 0) + "/人");
            }
        }

        if (holder.tv_business_name.getVisibility() == View.VISIBLE
                && holder.tv_avarge_money.getVisibility() == View.VISIBLE) {
            holder.lin_1.setVisibility(View.VISIBLE);
        } else {
            holder.lin_1.setVisibility(View.GONE);
        }

    }

    private void setLocation(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (item.street == null) {
            item.street = "";
        }
        if (item.distance == null) {
            item.distance = "";
        }

        String str = null;
        if (!TextUtils.isEmpty(item.street) && !TextUtils.isEmpty(item.distance)) {
            str = item.street + "  •  " + item.distance;
        } else if (TextUtils.isEmpty(item.street)) {
            str = item.distance;
        } else if (TextUtils.isEmpty(item.distance)) {
            str = item.street;
        }

        if (!TextUtils.isEmpty(str)) {
            holder.tv_location.setVisibility(View.VISIBLE);
            holder.tv_location.setText(str);
        } else {
            holder.tv_location.setVisibility(View.GONE);
            holder.tv_location.setText("");
        }
    }

    private void setLoadMoreState(WantFoodViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            holder.footerView.setVisibility(View.VISIBLE);
            if (mLoadState == RecyclerLoadMoreFooterView.LOADING) {
                holder.footerView.setLoading();
            } else {
                holder.footerView.setLoadFinish();
            }
        } else {
            holder.footerView.setVisibility(View.GONE);
        }
    }

    private void hasEated(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (item.hasEat) {
            holder.tv_eated.setVisibility(View.VISIBLE);
        } else {
            holder.tv_eated.setVisibility(View.GONE);
        }
    }

    private String getSubFloat(double f) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        String string = fnum.format(f);
        return string.equals("-0") ? "0" : string;
    }

    public static SYBusiness getSYBusiness(DeliciousFoodModel.SYSearchUserFoodModel item) {
        SYBusiness bus_eat = new SYBusiness();
        bus_eat.setMerchantId(item.merchantId);
        if (item.merchantPrice != null && TextUtils.isDigitsOnly(item.merchantPrice.replace(".", ""))) {
            bus_eat.setPerAverage(Double.parseDouble(item.merchantPrice));
        } else {
            bus_eat.setPerAverage(0);
        }
        bus_eat.setCategory(item.ptype);
        bus_eat.setDistance(item.distance);
        bus_eat.setEatNumber(item.eatNumber);
        bus_eat.setDetailAddress(item.shopAddress);//增加 地址信息
        SYPoi poi = new SYPoi();
        poi.setTitle(item.merchantName);
        //poi.setAddress(item_image_gallery.shopAddress);  由于 字段信息不对  改成需要信息
        poi.setAddress(item.street);
        poi.setLatitude(item.latitude);
        poi.setLongitude(item.longitude);
        poi.setCategory(item.ptype);
        bus_eat.setPoi(poi);

        SYImage image = new SYImage();
        image.setUrl(item.foodImage);
        image.setThumbUrl(item.foodImage);
        bus_eat.setImage(image);

        bus_eat.setMerTeleNumber(item.merTeleNumber);
        bus_eat.setIsOnline(item.isOnline);

        return bus_eat;
    }

    private void getPopWindow(final DeliciousFoodModel.SYSearchUserFoodModel item) {
        EnsureDialog.Builder dialog = new EnsureDialog.Builder(mFragment.getActivity());
        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(item);
            }
        }).create().show();
        dialog.setCancelable(true);
    }


    //删除条目
    private void deleteItem(final DeliciousFoodModel.SYSearchUserFoodModel item) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        mFragment.post(Constants.shareConstants().getNetHeaderAdress() + "/eat/delFoodEatV250.do", "", extra, new FFNetWorkCallBack<BaseResult>() {
        mFragment.post(IUrlUtils.Search.delFoodEatV250, "", extra, new FFNetWorkCallBack<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                        if (response == null) {
                            mFragment.showToast("删除失败");
                            return;
                        }
                        if (0 == response.getErrorCode()) {
                            mFragment.showToast("删除成功");
                            mDataList.remove(item);
                            notifyDataSetChanged();

                            if (mFragment.mAdapter.getItemCount() <= 0) {
                                mFragment.setEmptyView();
                            }

                            Intent intent = new Intent(MyActions.ACTION_PROFILE);
                            intent.putExtra("type", "type_delete_item");
                            LocalBroadcastManager.getInstance(mFragment.getActivity()).sendBroadcast(intent);
                        } else {
                            mFragment.showToast(response.getErrorMessage());
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "eatId", item.wantEatId,
                "merchantId", item.merchantId,
                "shopType", item.shopType,
                "eatStatus", item.hasEat ? 1 : 0);

    }

    /**
     * 设置筛选为空的状态，而不是没数据为空
     */
    public void setFilterEmptyState(boolean isEmpty) {
        isFilterEmpty = isEmpty;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
    
}
