package com.fengnian.smallyellowo.foodie.diningcase;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.config.Value;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestInfoActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantInfo;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.intentdatas.RestInfoIntent;
import com.fengnian.smallyellowo.foodie.scoreshop.OnFinishListener;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.ReplaceViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-4-11.
 */

public class WorkDiningHeaderViewPager extends ViewPager {
    private WorkDiningActivity mActivity;
    private NearbyFragHeaderAdapter headerAdapter;
    private ReplaceViewHelper mReplaceViewHelper;
    public boolean isRequestedNet = false;

    public WorkDiningHeaderViewPager(Context context) {
        super(context);
    }

    public WorkDiningHeaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        mActivity = (WorkDiningActivity) getContext();
        mReplaceViewHelper = new ReplaceViewHelper(mActivity);

        setPageTransformer(true, new ScalePageTransformer());
        setOffscreenPageLimit(3);
        setLayoutParams();
        getRecommendList();
    }

    public NearbyFragHeaderAdapter getHeaderAdapter() {
        if (headerAdapter == null) {
            headerAdapter = new NearbyFragHeaderAdapter(mActivity);
        }
        return headerAdapter;
    }

    public void getRecommendList() {
        getRecommendList(null);
    }

    public void getRecommendList(GDPoiModel model) {
        //获取经纬度
        String longitude = Value.mLongitude + "";
        String latitude = Value.mLatitude + "";
        if (model != null) {
            if (!TextUtils.isEmpty(model.location)) {
                String[] loc = model.location.split(",");
                if (loc.length == 2) {
                    longitude = loc[0];
                    latitude = loc[1];
                }
            }
        }

//        mActivity.post(Constants.shareConstants().getNetHeaderAdress() + "/discover/workingMeal/workingMealList.do",
        mActivity.post(IUrlUtils.WorkDining.workingMealList,
                "", null, new FFNetWorkCallBack<MyDiningList>() {
                    @Override
                    public void onSuccess(final MyDiningList response, FFExtraParams extra) {
                        isRequestedNet = true;
                        if (response != null && response.syFindMerchantInfos != null && response.syFindMerchantInfos.size() > 0) {
                            getHeaderAdapter().setDataList(response.syFindMerchantInfos);
                            setAdapter(getHeaderAdapter());
                        }
                        setEmptyView();
                        mActivity.setAllEmptyView();
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "openGPS", "0"
                , "city", "北京"
                , "shopId", "0"
                , "pageSize", 100
                , "longitude", longitude
                , "latitude", latitude);
    }

    public void setEmptyView() {
        View line = mActivity.mHeaderView.findViewById(R.id.line2);

        if (getHeaderAdapter().getCount() <= 0) {
            mReplaceViewHelper.toReplaceView(WorkDiningHeaderViewPager.this, R.layout.ff_empty_layout);
            TextView textView = (TextView) mReplaceViewHelper.getView().findViewById(R.id.text);
            textView.setText("没有找到合适的餐厅哦~");
            line.setVisibility(View.VISIBLE);
        } else {
            mReplaceViewHelper.removeView();
            line.setVisibility(View.GONE);
        }
    }

    public void removeViewPagerItem(SYFindMerchantInfo item) {
        List<SYFindMerchantInfo> dataList = getHeaderAdapter().getDataList();
        for (SYFindMerchantInfo tempItem : dataList) {
            if (tempItem == item) {
                dataList.remove(item);
                getHeaderAdapter().setDataList(dataList);
                setAdapter(getHeaderAdapter());
                break;
            }
        }
        setEmptyView();
    }

    private void setLayoutParams() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        params.width = getViewPagerImageWidth();
        params.height = getViewPagerImageHeight() + DisplayUtil.dip2px(80f);
        setLayoutParams(params);

        // 参数值会被平均分配距左右的间距
//        setPageMargin(-DisplayUtil.dip2px(28f));
    }

    public static int getViewPagerImageWidth() {
        int padding = DisplayUtil.dip2px(14f);
        return DisplayUtil.screenWidth - padding * 4;
    }

    public static int getViewPagerImageHeight() {
        return (int) (getViewPagerImageWidth() / 1.5);
    }

    public static class NearbyFragHeaderAdapter extends PagerAdapter {
        private WorkDiningActivity mActivity;
        private List<View> mViewList = new ArrayList<>();
        private List<SYFindMerchantInfo> mDataList = new ArrayList<>();
        private int mChildCount = 0;

        public void setDataList(List<SYFindMerchantInfo> list) {
            mDataList = list;
            mViewList.clear();
            for (SYFindMerchantInfo model : list) {
                ViewGroup itemView = (ViewGroup) View.inflate(mActivity, R.layout.woking_dining_nearby_header_item, null);
                itemView.setTag(model);
                mViewList.add(itemView);
            }
            this.notifyDataSetChanged();
        }

        public List<SYFindMerchantInfo> getDataList() {
            return mDataList;
        }

        private NearbyFragHeaderAdapter(WorkDiningActivity context) {
            mActivity = context;
        }

        @Override
        public int getCount() {
            if (mViewList != null && mViewList.size() > 0) {
                return mViewList.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mViewList.get(position);
            container.addView(itemView);
            SYFindMerchantInfo model = (SYFindMerchantInfo) itemView.getTag();

            TextView titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.image);

            titleTextView.getPaint().setFakeBoldText(true);

            if (model.getMerchantImage() != null && !TextUtils.isEmpty(model.getMerchantImage().getThumbUrl())) {
                FFImageLoader.loadMiddleImage(mActivity, model.getMerchantImage().getThumbUrl(), imageView);
            } else {
                imageView.setImageResource(0);
            }

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.width = getViewPagerImageWidth();
            params.height = getViewPagerImageHeight();
            imageView.setLayoutParams(params);

            setItem(itemView, model, position);

            return mViewList.get(position);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        private void setItem(View itemView, final SYFindMerchantInfo model, final int position) {
            View dining_add = itemView.findViewById(R.id.dining_add);
            setText(itemView, model);

            itemView.findViewById(R.id.image).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    mActivity.pushEvent107(position+"",model);

                    RestInfoIntent intent = new RestInfoIntent();
                    intent.setId(model.getMerchantUid());
                    intent.setStar(model.getStartLevel());
                    mActivity.startActivity(RestInfoActivity.class, intent);
                }
            });

            dining_add.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.addToMyDiningList(model.getMerchantUid(), new OnFinishListener() {
                        @Override
                        public void onFinish(Object object) {
                            mActivity.pushEvent154(position+"", model);
                            mActivity.removeViewPagerItem(model);
                        }
                    });
                }
            });
        }

        private void setText(View itemView, final SYFindMerchantInfo item) {
            TextView tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            TextView tv_business_name = (TextView) itemView.findViewById(R.id.tv_business_name);
            RatingBar rating_bar = (RatingBar) itemView.findViewById(R.id.rating_bar);
            TextView tv_level = (TextView) itemView.findViewById(R.id.tv_level);
            TextView tv_avarge_money = (TextView) itemView.findViewById(R.id.tv_avarge_money);
            TextView tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);

            if (!TextUtils.isEmpty(item.getMerchantName())) {
                tv_title.setText(item.getMerchantName());
            } else {
                tv_title.setText("");
            }

            if (!TextUtils.isEmpty(item.getMerchantKind())) {
                tv_business_name.setText(item.getMerchantKind());
            } else {
                tv_business_name.setText("");
            }

            if (item.getMerchantPrice() > 0) {
                tv_avarge_money.setVisibility(View.VISIBLE);
                tv_avarge_money.setText("¥" + item.getMerchantPrice() + "/人");
            } else {
                tv_avarge_money.setVisibility(View.GONE);
                tv_avarge_money.setText("");
            }

            if (item.getStartLevel() > 0) {
                tv_level.setVisibility(View.VISIBLE);
                rating_bar.setVisibility(View.VISIBLE);
                tv_level.setText(item.getStartLevel() + "");
                rating_bar.setRating(item.getStartLevel());
            } else {
                tv_level.setVisibility(View.GONE);
                rating_bar.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.getMerchantDistance())) {
                tv_distance.setText(item.getMerchantDistance());
            } else {
                tv_distance.setText("");
            }
        }

    }

    private static final class MyDiningList extends BaseResult {
        public List<SYFindMerchantInfo> syFindMerchantInfos;
    }
}
