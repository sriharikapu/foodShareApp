package com.fengnian.smallyellowo.foodie.personal;

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
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.View.RecyclerLoadMoreFooterView;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.FastDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastDetailIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-3-23.
 */

public class MyFoodSearchFragAdapter extends RecyclerView.Adapter<MyFoodViewHolder> {
    private MyFoodSearchListFragment mFragment;
    private List<DeliciousFoodModel.SYSearchUserFoodModel> mAllList = new ArrayList<>();
    private String mKeyword;
    private int mLoadState;

    public MyFoodSearchFragAdapter(MyFoodSearchListFragment fragment) {
        mFragment = fragment;
    }

    public void setKeyword(final String keyword) {
        mKeyword = keyword;
    }

    public void setDataList(List<DeliciousFoodModel.SYSearchUserFoodModel> list) {
        if (list == null) {
            return;
        }
        mAllList.clear();
        appendDataList(list);
    }

    public void appendDataList(List<DeliciousFoodModel.SYSearchUserFoodModel> list) {
        if (list == null) {
            return;
        }
        mAllList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void setLoadState(int loadState) {
        mLoadState = loadState;
        this.notifyDataSetChanged();
    }

    public List<DeliciousFoodModel.SYSearchUserFoodModel> getDataList() {
        return mAllList;
    }


    @Override
    public MyFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(mFragment.getActivity(), R.layout.user_delicious_food_item, null);

        MyFoodViewHolder holder = new MyFoodViewHolder(itemView);
        removeUnlessView(holder);
        return holder;
    }

    private void removeUnlessView(MyFoodViewHolder holder) {
        //由于这个页面是复用的，所以这里去除一些无用的View
        ViewGroup viewGroup = (ViewGroup) (holder.rotation_loadingview.getParent());
        if (holder.rotation_loadingview != null) {
            viewGroup.removeView(holder.rotation_loadingview);
        }
        if (holder.tv_publish_status != null) {
            viewGroup.removeView(holder.tv_publish_status);
        }
        if (holder.profile_camera != null) {
            viewGroup.removeView(holder.profile_camera);
        }
    }

    @Override
    public void onBindViewHolder(MyFoodViewHolder holder, int position) {
        DeliciousFoodModel.SYSearchUserFoodModel item = mAllList.get(position);
        setOnItemClick(holder, item);
        setFoodImage(holder, item);
        setTitle(holder, item);
        setRatingBar(holder, item);
        setBusinessName(holder, item);
        setLocation(holder, item);
        setTime(holder, item);
        setPictureCount(holder, item);
        setShareState(holder, item);
        setLoadMoreState(holder, position);
    }

    private void setOnItemClick(MyFoodViewHolder holder, final DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.item_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FFUtils.checkNet()) {
                    mFragment.showToast(mFragment.getString(R.string.lsq_network_connection_interruption));
                    return;
                }

                boolean isMeishiBianji = !item.isShorthandFood;//富文本美食编辑
                if (isMeishiBianji) {
                    DynamicDetailIntent intent = new DynamicDetailIntent(item.foodNoteId);
                    intent.setMineMode(true);
                    mFragment.startActivity(DynamicDetailActivity.class, intent);
                } else {//速记
                    FastDetailIntent intent = new FastDetailIntent(item.foodNoteId);
                    intent.setMineMode(true);
                    mFragment.startActivity(FastDetailActivity.class, intent);
                }
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

    private void setFoodImage(MyFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.image.setImageResource(0);
        if (item != null) {
            if (!TextUtils.isEmpty(item.foodImage)) {
                FFImageLoader.loadSmallImage((FFContext) mFragment.getActivity(), item.foodImage, holder.image);
            }
        }
    }

    private void setTitle(MyFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.tv_title.setText("");
        if (item != null) {
            if (!TextUtils.isEmpty(item.foodTitle)) {
                SpannableString spannableString = FFUtils.getSpanByKeyword(item.foodTitle, mKeyword, R.color.colorPrimary);
                holder.tv_title.setText(spannableString);
            }
        }
    }

    private void setRatingBar(MyFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.rating_bar.setVisibility(View.GONE);
        //设置ratingBar高度
        int ratingBarHeight = mFragment.getResources().getDrawable(R.drawable.rating_detail_light).getIntrinsicHeight();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.rating_bar.getLayoutParams();
        params.height = ratingBarHeight;
        holder.rating_bar.setLayoutParams(params);

        if (item != null) {
            if (item.starLevel > 0) {
                holder.rating_bar.setRating((float) item.starLevel);
                holder.rating_bar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setBusinessName(MyFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.tv_business_name.setText("");
        holder.tv_business_name.setVisibility(View.GONE);
        if (item != null) {
            if (!TextUtils.isEmpty(item.merchantName)) {
                holder.tv_business_name.setVisibility(View.VISIBLE);
                SpannableString spannableString = FFUtils.getSpanByKeyword(item.merchantName, mKeyword, R.color.colorPrimary);
                holder.tv_business_name.setText(spannableString);
            }
        }
    }

    private void setLocation(MyFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.tv_location.setVisibility(View.GONE);
        if (item != null) {
            if (!TextUtils.isEmpty(item.street)) {
                holder.tv_location.setVisibility(View.VISIBLE);
                SpannableString spannableString = FFUtils.getSpanByKeyword(item.street, mKeyword, R.color.colorPrimary);
                holder.tv_location.setText(spannableString);
            }
        }
    }

    private void setTime(MyFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.tv_time.setVisibility(View.GONE);
        if (item != null) {
            if (!TextUtils.isEmpty(item.pushTime) && TextUtils.isDigitsOnly(item.pushTime)) {
                holder.tv_time.setVisibility(View.VISIBLE);
                holder.tv_time.setText(TimeUtils.getTime("MM-dd", Long.parseLong(item.pushTime + "000")));
            }
        }
    }

    private void setPictureCount(MyFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.tv_pic_count.setVisibility(View.GONE);
        if (item != null) {
            if (item.foodImageCount > 0) {
                holder.tv_pic_count.setVisibility(View.VISIBLE);
                holder.tv_pic_count.setText("" + item.foodImageCount);
            }
        }
    }

    private void setShareState(MyFoodViewHolder holder, DeliciousFoodModel.SYSearchUserFoodModel item) {
        holder.tv_share_status.setVisibility(View.GONE);
        if (item != null) {
            if (!item.isSharedToAct) {
                holder.tv_share_status.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setLoadMoreState(MyFoodViewHolder holder, int position) {
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

    public void getPopWindow(final DeliciousFoodModel.SYSearchUserFoodModel item) {
        EnsureDialog.Builder dialog = new EnsureDialog.Builder(mFragment.getActivity());
        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(item);

            }
        }).create().show();
        dialog.setCancelable(true);
    }

    //删除网络的一条记录
    private void delete(final DeliciousFoodModel.SYSearchUserFoodModel item) {
//        mFragment.post(Constants.shareConstants().getNetHeaderAdress() + "/notes/removeFoodNoteV250.do", "", null, new FFNetWorkCallBack<BaseResult>() {
        mFragment.post(IUrlUtils.Search.removeFoodNoteV250, "", null, new FFNetWorkCallBack<BaseResult>() {
            @Override
            public void onSuccess(BaseResult response, FFExtraParams extra) {
                if (response.getErrorCode() == 0) {
                    if (getItemCount() <= 0) {
                        if (mFragment.getParentFragment() instanceof ProfileSearchResultFragment) {
                            ProfileSearchResultFragment resultFragment = (ProfileSearchResultFragment) mFragment.getParentFragment();
                            resultFragment.setEmptyView();
                        }
                    }
                    mAllList.remove(item);
                    notifyDataSetChanged();
                    mFragment.showToast("删除成功");
                } else {
                    mFragment.showToast(response.getErrorMessage() + "");
                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "cusId", item.foodNoteId, "recordId", item.foodNoteId);
    }

    @Override
    public int getItemCount() {
        return mAllList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

}
