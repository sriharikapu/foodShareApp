package com.fengnian.smallyellowo.foodie.homepage;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYChoiceModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-4-10.
 */

public class HomeChildNearbyAdapter extends RecyclerView.Adapter<HomeChildNearbyAdapter.MyNearbyViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_NORMAL = 1;
    private final int HEADER_NORMAL = 10;
    private final int HEADER_EMPTY = 20;
    private final int HEADER_NO_NET = 30;
    private final int GRID_NUM = 2;
    private int mHeaderViewType = HEADER_NORMAL;
    private HomeChildNearbyFrag mFragment;
    private List<SYUgcModel> mDataList = new ArrayList<>();
    private List<SYChoiceModel> mHeadDataList = new ArrayList<>();
    private PullToRefreshLayout mPullToRefreshLayout;
    private View mCurrentView;
    private View mBeCoveredView;
    private View.OnClickListener mNoNetClickListener;

    public HomeChildNearbyAdapter(HomeChildNearbyFrag frag) {
        mFragment = frag;
    }

    public void setPullToRefreshLayout(PullToRefreshLayout refreshLayout) {
        mPullToRefreshLayout = refreshLayout;
    }

    /**
     * 设置当前被空页面或者无网页面覆盖的View是谁
     */
    public void setCurrentBeCoveredView(View view) {
        mBeCoveredView = view;
    }

    public void setHeaderDataList(List<SYChoiceModel> list) {
        if (list == null) {
            return;
        }
        mHeadDataList.clear();
        mHeadDataList.addAll(list);
    }

    public void setDataList(List<SYUgcModel> list) {
        if (list == null) {
            return;
        }
        mDataList.clear();

        //添加第一个Header被占位的项
        SYUgcModel model = new SYUgcModel();
        mDataList.add(model);

        appendDataList(list);
    }

    public void appendDataList(List<SYUgcModel> list) {
        if (list == null) {
            return;
        }
        mDataList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void setNormal() {
        mHeaderViewType = HEADER_NORMAL;
        this.notifyDataSetChanged();
    }

    public void setEmpty() {
        resetData();
        mHeaderViewType = HEADER_EMPTY;
        this.notifyDataSetChanged();
    }

    public void setNoNet(View.OnClickListener listener) {
        mNoNetClickListener = listener;
        resetData();
        mHeaderViewType = HEADER_NO_NET;
        this.notifyDataSetChanged();
    }

    private void resetData() {
        mHeadDataList.clear();
        mDataList.clear();

        //添加第一个Header被占位的项
        SYUgcModel model = new SYUgcModel();
        mDataList.add(model);
    }

    @Override
    public MyNearbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View headerView = View.inflate(mFragment.getActivity(), R.layout.home_child_nearby_header, null);
            MyNearbyViewHolder headerHolder = new MyNearbyViewHolder(headerView);
            mPullToRefreshLayout.addTouchView(headerHolder.viewPager);
            return headerHolder;
        } else {
            View itemView = View.inflate(mFragment.getActivity(), R.layout.home_child_nearby_item, null);
            MyNearbyViewHolder holder = new MyNearbyViewHolder(itemView);

            //设置ratingBar高度
            int ratingBarHeight = mFragment.getResources().getDrawable(R.drawable.rating_detail_light).getIntrinsicHeight();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.rating_bar.getLayoutParams();
            params.height = ratingBarHeight;
            holder.rating_bar.setLayoutParams(params);

            return holder;
        }
    }

    @Override
    public void onBindViewHolder(MyNearbyViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            if (mHeaderViewType == HEADER_NO_NET) {
                setNoNetView(holder);
            } else if (mHeaderViewType == HEADER_EMPTY) {
                setEmptyView(holder);
            } else {
                setNormalHeaderView(holder);
            }
            holder.viewPager.getHeaderAdapter(mFragment).setDataList(mHeadDataList);
        } else {
            SYUgcModel item = mDataList.get(position);
            if (item == null) {
                return;
            }
            setFoodImage(holder, item, position);
            setFoodName(holder, item);
            setRatingBar(holder, item);
            setUserName(holder, item);
            setUserAvatar(holder, item);
            setDistance(holder, item);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void onViewAttachedToWindow(MyNearbyViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        //为StaggeredGridLayoutManager添加Header
        ViewGroup.LayoutParams viewGroupParams = holder.itemView.getLayoutParams();
        if (viewGroupParams != null && viewGroupParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            if (holder.getLayoutPosition() == 0) {
                StaggeredGridLayoutManager.LayoutParams StaggeredParams = (StaggeredGridLayoutManager.LayoutParams) viewGroupParams;
                StaggeredParams.setFullSpan(true);
            }
        }
    }

    private void setFoodImage(MyNearbyViewHolder holder, final SYUgcModel item, final int position) {
        final int width = (int) (DisplayUtil.screenWidth - getItemPadding() * 3f) / GRID_NUM;
        final int height = (int) (item.imageHeight * width / item.imageWidth);

        //图片
        LinearLayout.LayoutParams imageParams = (LinearLayout.LayoutParams) holder.image.getLayoutParams();
        imageParams.width = width;
        imageParams.height = height;
        holder.image.setLayoutParams(imageParams);

        //图片外面的框
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) holder.linear.getLayoutParams();
        linearParams.width = width;
        linearParams.leftMargin = (int) getItemPadding();
        linearParams.rightMargin = 0;

        if (position == getItemCount() - 1) {
            linearParams.bottomMargin = DisplayUtil.dip2px(25f);
        } else {
            linearParams.bottomMargin = 0;
        }
        holder.linear.setLayoutParams(linearParams);

        FFImageLoader.loadMiddleImage((FFContext) mFragment.getActivity(), item.frontCoverImg, holder.image);

        //跳转到UGC详情页
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(item.feedId)) {
                    return;
                }
                DynamicDetailIntent intent = new DynamicDetailIntent();
                intent.setId(item.feedId);
                mFragment.startActivity(DynamicDetailActivity.class, intent);
            }
        });
    }

    private void setFoodName(MyNearbyViewHolder holder, SYUgcModel item) {
        if (!TextUtils.isEmpty(item.frontCoverContent)) {
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_title.setText(item.frontCoverContent);
        } else {
            holder.tv_title.setVisibility(View.GONE);
            holder.tv_title.setText("");
        }
    }

    private void setRatingBar(MyNearbyViewHolder holder, SYUgcModel item) {
        if (item.starLevel > 0) {
            holder.ll_ratings_container.setVisibility(View.VISIBLE);
            holder.rating_bar.setRating(item.starLevel);
            holder.rating_bar_text.setText(SYFeed.pullStartLevelString(item.starLevel));
        } else {
            holder.ll_ratings_container.setVisibility(View.GONE);
        }
    }

    private void setUserName(MyNearbyViewHolder holder, SYUgcModel item) {
        if (item.user != null && !TextUtils.isEmpty(item.user.getNickName())) {
            holder.tv_name.setText(item.user.getNickName());
        } else {
            holder.tv_name.setText("");
        }
    }

    private void setUserAvatar(MyNearbyViewHolder holder, SYUgcModel item) {
        if (item.user != null && item.user.getHeadImage() != null && !TextUtils.isEmpty(item.user.getHeadImage().getUrl())) {
            FFImageLoader.loadAvatar((FFContext) mFragment.getActivity(), item.user.getHeadImage().getUrl(), holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(0);
        }
    }

    private void setDistance(MyNearbyViewHolder holder, SYUgcModel item) {
        if (!TextUtils.isEmpty(item.distance)) {
            holder.tv_distance.setText(item.distance);
        } else {
            holder.tv_distance.setText("");
        }
    }

    /**
     * 设置正常的带banner的HeaderView
     */
    private void setNormalHeaderView(MyNearbyViewHolder holder) {
        if (mCurrentView != null) {
            holder.headerRoot.removeView(mCurrentView);
            mCurrentView = null;
        }
    }

    /**
     * 设置空的HeaderView
     */
    private void setEmptyView(MyNearbyViewHolder holder) {
        if (mCurrentView != null) {
            holder.headerRoot.removeView(mCurrentView);
            mCurrentView = null;
        }

        mCurrentView = View.inflate(mFragment.getActivity(), R.layout.ff_empty_layout, null);
        mCurrentView.setClickable(true);
        mCurrentView.setBackgroundColor(mFragment.getResources().getColor(R.color.normal_bg));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, mBeCoveredView.getHeight());
        holder.headerRoot.addView(mCurrentView, params);
    }

    /**
     * 设置无网络的HeaderView
     */
    private void setNoNetView(MyNearbyViewHolder holder) {
        if (mCurrentView != null) {
            holder.headerRoot.removeView(mCurrentView);
            mCurrentView = null;
        }

        mCurrentView = View.inflate(mFragment.getActivity(), R.layout.ff_nonet_layout, null);
        mCurrentView.setBackgroundColor(mFragment.getResources().getColor(R.color.normal_bg));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, mBeCoveredView.getHeight());
        holder.headerRoot.addView(mCurrentView, params);
        mCurrentView.setOnClickListener(mNoNetClickListener);
    }

    public float getItemPadding() {
        return mFragment.getResources().getDimension(R.dimen.home_gird_width);
    }

    public static class MyNearbyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_name, tv_distance, rating_bar_text;
        ImageView image, iv_avatar;
        RatingBar rating_bar;
        View linear, ll_ratings_container;

        //从这里开始是HeaderView
        NearbyFragHeaderViewPager viewPager;
        RelativeLayout headerRoot;

        public MyNearbyViewHolder(View view) {
            super(view);
            linear = view.findViewById(R.id.linear);
            rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);
            image = (ImageView) view.findViewById(R.id.image);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_distance = (TextView) view.findViewById(R.id.tv_distance);
            iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
            ll_ratings_container = view.findViewById(R.id.ll_ratings_container);
            rating_bar_text = (TextView) view.findViewById(R.id.rating_bar_text);

            //从这里开始是HeaderView
            viewPager = (NearbyFragHeaderViewPager) view.findViewById(R.id.view_pager);
            headerRoot = (RelativeLayout) view.findViewById(R.id.header_root);
        }
    }
}
