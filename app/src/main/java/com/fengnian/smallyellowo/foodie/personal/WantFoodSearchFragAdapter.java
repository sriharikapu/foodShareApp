package com.fengnian.smallyellowo.foodie.personal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.WanEatDetailIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-3-23.
 */

public class WantFoodSearchFragAdapter extends RecyclerView.Adapter<WantFoodViewHolder> {
    private WantFoodSearchListFragment mFragment;
    private List<DeliciousFoodModel.SYSearchUserFoodModel> mDataList = new ArrayList<>();
    private String mKeyword;
    private int mLoadState;

    public WantFoodSearchFragAdapter(WantFoodSearchListFragment fragment) {
        mFragment = fragment;
    }

    public void setLoadState(int loadState) {
        mLoadState = loadState;
        this.notifyDataSetChanged();
    }

    public void setDataList(List<DeliciousFoodModel.SYSearchUserFoodModel> list) {
        if (list == null) {
            return;
        }
        mDataList.clear();
        appendDataList(list);
    }

    public List<DeliciousFoodModel.SYSearchUserFoodModel> getDataList() {
        return mDataList;
    }

    public void setKeyword(final String keyword) {
        mKeyword = keyword;
    }

    public void appendDataList(List<DeliciousFoodModel.SYSearchUserFoodModel> list) {
        if (list == null) {
            return;
        }
        mDataList.addAll(list);
        this.notifyDataSetChanged();
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
        setItemClick(holder, item);
        setFoodImage(holder, item);
        setTitle(holder, item);
        setRatingBar(holder, item);
        setLocation(holder, item);
        setShopInfo(holder, item);
        setLoadMoreState(holder, position);
        hasEated(holder, item);
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
        if (item != null && !TextUtils.isEmpty(item.foodImage)) {
            FFImageLoader.loadSmallImage((FFContext) mFragment.getActivity(), item.foodImage, holder.image);
        } else {
            holder.image.setImageResource(0);
        }
    }

    private void setTitle(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (item != null && !TextUtils.isEmpty(item.merchantName)) {
            SpannableString spannableString = FFUtils.getSpanByKeyword(item.merchantName, mKeyword, R.color.colorPrimary);
            holder.tv_title.setText(spannableString);
        } else {
            holder.tv_title.setText("");
        }
    }

    private void setRatingBar(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (item != null && item.starLevel > 0) {
            holder.rating_bar.setVisibility(View.VISIBLE);
            holder.rating_bar.setRating(Float.parseFloat(item.starLevel + ""));
            holder.tv_level.setText(getSubFloat(item.starLevel));

            //设置ratingBar高度
            int ratingBarHeight = mFragment.getResources().getDrawable(R.drawable.rating_detail_light).getIntrinsicHeight();
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.rating_bar.getLayoutParams();
            params.height = ratingBarHeight;
            holder.rating_bar.setLayoutParams(params);
        } else {
            holder.rating_bar.setVisibility(View.INVISIBLE);
            holder.tv_level.setVisibility(View.INVISIBLE);
        }
    }

    private void setShopInfo(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (item != null) {
            holder.shop_linear.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(item.ptype)) {
                holder.tv_business_name.setVisibility(View.VISIBLE);
                SpannableString spannableString = FFUtils.getSpanByKeyword(item.ptype, mKeyword, R.color.colorPrimary);
                holder.tv_business_name.setText(spannableString);
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
        } else {
            holder.shop_linear.setVisibility(View.GONE);
        }
    }

    private void setLocation(WantFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        if (item == null) {
            holder.tv_location.setVisibility(View.GONE);
            holder.tv_location.setText("");
            return;
        }

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
            SpannableString spannableString = FFUtils.getSpanByKeyword(str, mKeyword, R.color.colorPrimary);
            holder.tv_location.setText(spannableString);
        } else {
            holder.tv_location.setVisibility(View.GONE);
            holder.tv_location.setText("");
        }
    }

    private void setLoadMoreState(WantFoodViewHolder holder, int position) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.footerView.getLayoutParams();
        if (params.bottomMargin > 0) {
            params.bottomMargin = 0;
            holder.footerView.setLayoutParams(params);
        }

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

                            if (getItemCount() <= 0) {
                                if (mFragment.getParentFragment() instanceof ProfileSearchResultFragment) {
                                    ProfileSearchResultFragment resultFragment = (ProfileSearchResultFragment) mFragment.getParentFragment();
                                    resultFragment.setEmptyView();
                                }
                            }
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

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
