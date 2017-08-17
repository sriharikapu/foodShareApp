package com.fengnian.smallyellowo.foodie.homepage;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.PGCDetailActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.publics.SYChoiceModel;
import com.fengnian.smallyellowo.foodie.intentdatas.PGCDetailIntent;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-4-11.
 */

public class NearbyFragHeaderViewPager extends ViewPager {
    private NearbyFragHeaderAdapter headerAdapter;

    public NearbyFragHeaderViewPager(Context context) {
        super(context);
        init();
    }

    public NearbyFragHeaderViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }

    public NearbyFragHeaderAdapter getHeaderAdapter(HomeChildNearbyFrag fragment) {
        if (headerAdapter == null) {
            headerAdapter = new NearbyFragHeaderAdapter(getContext(), fragment);
        }
        return headerAdapter;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.setAdapter(headerAdapter);
        setOffscreenPageLimit(5);
        setLayoutParams();
    }

    private void setLayoutParams() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        int padding = getResources().getDimensionPixelSize(R.dimen.home_nearby_header_padding);
        params.width = DisplayUtil.screenWidth - padding * 4;
        params.height = params.width / 2;
        setLayoutParams(params);
        setPageMargin(padding);
    }


    public static class NearbyFragHeaderAdapter extends PagerAdapter {
        private HomeChildNearbyFrag fragment;
        private Context mContext;
        private List<View> mViewList = new ArrayList<>();
        private int mChildCount = 0;

        private NearbyFragHeaderAdapter(Context context) {
            mContext = context;

            //默认有一条空白的
            View itemView = View.inflate(mContext, R.layout.home_child_nearby_header_item, null);
            mViewList.add(itemView);
        }

        public void setDataList(List<SYChoiceModel> list) {
            mViewList.clear();
            for (SYChoiceModel model : list) {
                View itemView = View.inflate(mContext, R.layout.home_child_nearby_header_item, null);
                itemView.setTag(model);
                mViewList.add(itemView);
            }
            this.notifyDataSetChanged();
        }

        private NearbyFragHeaderAdapter(Context context, HomeChildNearbyFrag fragment) {
            this(context);
            this.fragment = fragment;
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

            CircleImageView imageView = (CircleImageView) itemView.findViewById(R.id.image);
            imageView.setRectAdius(5f);

            final SYChoiceModel model = (SYChoiceModel) itemView.getTag();
            if (model != null && model.getBackImage() != null && model.getBackImage().getUrl() != null) {
                FFImageLoader.loadBigImage((FFContext) mContext, model.getBackImage().getUrl(), imageView);

                if (!TextUtils.isEmpty(model.getHtmlUrl())) {
                    itemView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PGCDetailIntent data = new PGCDetailIntent();
                            data.setUrl(model.getHtmlUrl());
                            data.setTitle(model.getTitle());
                            if (model.getBackImage() != null) {
                                data.setImgUrl(model.getBackImage().getUrl());
                            }
                            data.setId(model.getId());
                            data.setAccount(SP.getUid());
                            data.setToken(SP.getToken());
                            data.setVersion(FFUtils.getVerName());
                            fragment.startActivity(PGCDetailActivity.class, data);
                        }
                    });
                }

            }
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
    }

}
