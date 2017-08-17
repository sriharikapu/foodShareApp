package com.fengnian.smallyellowo.foodie.diningcase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;

/**
 * Created by chenglin on 2017-7-5.
 */

public class WorkDiningHelper {
    private WorkDiningActivity mActivity;
    private ImageView mEmptyView1;
    private WorkDiningItemDialog mItemDialog;
    private View mAllEmptyView;

    public WorkDiningHelper(WorkDiningActivity activity) {
        mActivity = activity;
    }

    public void setMyDiningListPageState() {
        ImageView dining_get_item_1 = (ImageView) mActivity.mListTitle.findViewById(R.id.dining_get_item);
        ImageView dining_share_1 = (ImageView) mActivity.mListTitle.findViewById(R.id.dining_share);
        ImageView dining_get_item_2 = (ImageView) mActivity.mHeaderView.findViewById(R.id.list_title).findViewById(R.id.dining_get_item);
        ImageView dining_share_2 = (ImageView) mActivity.mHeaderView.findViewById(R.id.list_title).findViewById(R.id.dining_share);

        if (mActivity.mAdapter.getCount() <= 0) {
            addListEmptyView();
            mActivity.prl.setDoPullUp(false);
        } else {
            removeListEmptyView();
            mActivity.prl.setDoPullUp(true);
        }

        if (mActivity.mAdapter.getCount() <= 1) {
            dining_get_item_1.setImageResource(R.drawable.dining_get_disnable);
            dining_share_1.setImageResource(R.drawable.dining_share_disnable);
            dining_get_item_2.setImageResource(R.drawable.dining_get_disnable);
            dining_share_2.setImageResource(R.drawable.dining_share_disnable);
        } else {
            dining_get_item_1.setImageResource(R.drawable.dining_get);
            dining_share_1.setImageResource(R.drawable.dining_share);
            dining_get_item_2.setImageResource(R.drawable.dining_get);
            dining_share_2.setImageResource(R.drawable.dining_share);
        }

        TextView tvCount = (TextView) mActivity.mListTitle.findViewById(R.id.tv_count);
        tvCount.setText(mActivity.mAdapter.getCount() + "条");
        tvCount = (TextView) mActivity.mHeaderView.findViewById(R.id.list_title).findViewById(R.id.tv_count);
        tvCount.setText(mActivity.mAdapter.getCount() + "条");
    }

    public void setAllEmptyView() {
        RelativeLayout relativeLayout = (RelativeLayout) mActivity.mHeaderView.findViewById(R.id.head_view);

        if (mActivity.isRequestedNet && mActivity.mViewPager.isRequestedNet
                && mActivity.mAdapter.getCount() <= 0 && mActivity.getRecommendListCount() <= 0) {
            if (mAllEmptyView != null) {
                relativeLayout.removeView(mAllEmptyView);
                mAllEmptyView = null;
            }
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -1);
            params.addRule(RelativeLayout.BELOW, R.id.dining_nearby);
            mAllEmptyView = View.inflate(mActivity, R.layout.work_dining_empty_layout, null);
            relativeLayout.addView(mAllEmptyView, params);

            mAllEmptyView.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NearbyRestListActivity.start(mActivity, Value.mLongitude, Value.mLatitude);
                }
            });
        } else {
            if (mAllEmptyView != null) {
                relativeLayout.removeView(mAllEmptyView);
            }
        }
    }

    public void addListEmptyView() {
        RelativeLayout relativeLayout = (RelativeLayout) mActivity.mHeaderView.findViewById(R.id.head_view);
        if (mEmptyView1 != null) {
            relativeLayout.removeView(mEmptyView1);
            mEmptyView1 = null;
        }

        mEmptyView1 = new ImageView(mActivity);
        mEmptyView1.setImageResource(R.drawable.work_dining_head_empty);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
        params.topMargin = DisplayUtil.dip2px(20f);
        params.bottomMargin = DisplayUtil.dip2px(15f);
        params.addRule(RelativeLayout.BELOW, R.id.list_title);
        relativeLayout.addView(mEmptyView1, params);
    }

    public void removeListEmptyView() {
        if (mEmptyView1 != null) {
            RelativeLayout relativeLayout = (RelativeLayout) mActivity.mHeaderView.findViewById(R.id.head_view);
            relativeLayout.removeView(mEmptyView1);
            mEmptyView1 = null;
        }
    }

    public void getMyDiningListItem() {
        if (!FFUtils.checkNet()) {
            mActivity.showToast(mActivity.getString(R.string.lsq_network_connection_interruption));
            return;
        } else if (mActivity.mAdapter.getCount() <= 1) {
            return;
        }
        if (mItemDialog == null) {
            mItemDialog = new WorkDiningItemDialog(mActivity);
        }
        mItemDialog.show();
        mItemDialog.getData();
    }

    /**
     * 生成投票H5的URL
     */
    public void getVoteUrl() {
        StringBuilder builder = new StringBuilder();
        for (SYFindMerchantInfo item : mActivity.mAdapter.getCheckedList()) {
            if (builder.length() <= 0) {
                builder.append(item.getMerchantUid());
            } else {
                builder.append("," + item.getMerchantUid());
            }
        }

        mActivity.pushEvent158(builder.toString());

//        mActivity.post(Constants.shareConstants().getNetHeaderAdress() + "/weekdayLunch/createVoteActivity.do",
        mActivity.post(IUrlUtils.WorkDining.createVoteActivity,
                "", null, new FFNetWorkCallBack<ShareLinkModel>() {
                    @Override
                    public void onSuccess(ShareLinkModel response, FFExtraParams extra) {
                        if (response != null && !TextUtils.isEmpty(response.data)) {
                            Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.dining_share_icon);
                            WeixinOpen.getInstance().share2weixin(response.data, getShareContent(), getShareTitle(), bitmap);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "list", builder.toString());
    }

    private String getShareContent() {
        return mActivity.getString(R.string.dining_recommend_share_4);
    }

    private String getShareTitle() {
        return SP.getUser().getNickName() + "发起的午餐投票";
    }

    public void stopListViewScroll(ListView mListView) {
        mListView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
    }

    private static final class ShareLinkModel extends BaseResult {
        public String data;
    }
}
