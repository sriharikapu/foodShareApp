package com.fengnian.smallyellowo.foodie.homepage;

import android.animation.ValueAnimator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.ClubUserInfoActivity;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.PGCDetailActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.UserInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.AdModelsList;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.dialogs.PopRest;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.fragments.MainHomeUGCFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.PGCDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.DisplayUtil;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenglin on 2017-7-17.
 */

public class HomeChildSelectedAdapter extends RecyclerView.Adapter {
    public static final int TYPE_PGC_HEAD = 0;
    public static final int TYPE_FEED = 1;
    public static final int TYPE_RECOMMEND = 2;
    private BaseActivity mActivity;
    private HomeChildSelectedFrag mSelectedFrag;
    private ArrayList<SYUgcModel> mFeedsList = new ArrayList<>();
    private List<AdModelsList.ItemsAd> mHeaderPgcList = new ArrayList<>();
    private View mPgcView;
    private View mRecommendView;
    private RecyclerView mRecyclerView;
    private SYUgcModel RecommendModel;
    private Runnable runnable;

    public HomeChildSelectedAdapter(HomeChildSelectedFrag frag) {
        mSelectedFrag = frag;
        mActivity = (BaseActivity) frag.getActivity();
        mRecyclerView = (RecyclerView) mSelectedFrag.findViewById(R.id.my_recycle_view);
    }

    public void setDataList(ArrayList<SYUgcModel> list) {
        mFeedsList.clear();
        addHeaderBannerPosition();
        addRecommendPosition();
        appendDataList(list);
    }

    public void appendDataList(ArrayList<SYUgcModel> list) {
        if (list != null) {
            mFeedsList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public ArrayList<SYUgcModel> getFeedList() {
        return mFeedsList;
    }

    public void setHeaderPgcList(List<AdModelsList.ItemsAd> list) {
        if (list != null) {
            mHeaderPgcList.clear();
            mHeaderPgcList.addAll(list);
            addHeaderBannerPosition();
            notifyDataSetChanged();
        }
    }


    public View getHeaderView() {
        return mPgcView;
    }

    /**
     * add 精选头部banner
     */
    private void addHeaderBannerPosition() {
        //每次add 都先remove原有的精选头部banner
        if (mFeedsList.size() > 0 && mFeedsList.get(0).itemType != TYPE_PGC_HEAD) {
            for (SYUgcModel tempModel : mFeedsList) {
                if (tempModel.itemType == TYPE_PGC_HEAD) {
                    mFeedsList.remove(tempModel);
                    break;
                }
            }
        }

        if (mFeedsList.size() <= 0 || (mFeedsList.size() > 0 && mFeedsList.get(0).itemType != TYPE_PGC_HEAD)) {
            SYUgcModel model = new SYUgcModel();
            model.itemType = TYPE_PGC_HEAD;
            mFeedsList.add(0, model);
//            notifyDataSetChanged();
        }
    }

    public void setRecommendModel(SYUgcModel model) {
        RecommendModel = model;
        addRecommendPosition();
        notifyDataSetChanged();
    }

    /**
     * add 非计划性推荐位
     */
    private void addRecommendPosition() {
        //每次add 都先remove原有的推荐位item
        if (mFeedsList.size() > 0) {
            for (SYUgcModel tempModel : mFeedsList) {
                if (tempModel.itemType == TYPE_RECOMMEND) {
                    mFeedsList.remove(tempModel);
                    break;
                }
            }
        }

        if (RecommendModel != null) {
            RecommendModel.itemType = TYPE_RECOMMEND;
            if (mFeedsList.size() > 1) {
                if (mFeedsList.get(0).itemType == TYPE_PGC_HEAD) {
                    mFeedsList.add(1, RecommendModel);
                } else {
                    mFeedsList.add(0, RecommendModel);
                }
            } else if (mFeedsList.size() == 1) {
                if (mFeedsList.get(0).itemType == TYPE_PGC_HEAD) {
                    mFeedsList.add(1, RecommendModel);
                }
            } else {
                mFeedsList.add(0, RecommendModel);
            }
        }
//        notifyDataSetChanged();
    }

    public void onDestroy() {
        if (runnable != null) {
            FFUtils.getHandler().removeCallbacks(runnable);
            runnable = null;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PGC_HEAD) {
            mPgcView = View.inflate(mActivity, R.layout.item_home_ugc_pgc, null);
            return new MyHeaderHolder(mPgcView);
        }
        if (viewType == TYPE_FEED) {
            return new HolderFeeds(LayoutInflater.from(mActivity));
        }
        if (viewType == TYPE_RECOMMEND) {
            mRecommendView = View.inflate(mActivity, R.layout.home_selected_recommend_item, null);
            return new MyRecommendHolder(mRecommendView);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderFeeds) {
            ((HolderFeeds) holder).onBind(position);
        } else if (holder instanceof MyRecommendHolder) {
            ((MyRecommendHolder) holder).onBind(position);
        } else if (holder instanceof MyHeaderHolder) {
            ((MyHeaderHolder) holder).onBind(position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mFeedsList.get(position).itemType == TYPE_PGC_HEAD) {
            return TYPE_PGC_HEAD;
        } else if (mFeedsList.get(position).itemType == TYPE_RECOMMEND) {
            return TYPE_RECOMMEND;
        } else {
            return TYPE_FEED;
        }
    }

    @Override
    public int getItemCount() {
        return mFeedsList.size();
    }

    public void setDislikeIconHide() {
        if (mRecommendView != null) {
            View dislikeView = mRecommendView.findViewById(R.id.recommend_dislike);
            if (dislikeView.getVisibility() == View.VISIBLE) {
                restBtnState(false);
            }
        }
    }

    public void hideRecommendView() {
        if (mRecommendView.getTag() != null && mRecommendView.getTag() instanceof MyRecommendHolder) {
            MyRecommendHolder myRecommendHolder = (MyRecommendHolder) mRecommendView.getTag();
            myRecommendHolder.startHideRecommendViewAnimator();
        }
    }

    private void restBtnState(boolean isAnimate) {
        final View dislikeView = mRecommendView.findViewById(R.id.recommend_dislike);
        final View closeIcon = mRecommendView.findViewById(R.id.recommend_close_icon);

        final int duration = 200;
        int dislikeIconWidth = dislikeView.getResources().getDrawable(R.drawable.recommend_dislike__icon).getIntrinsicWidth();
        dislikeIconWidth = dislikeIconWidth + DisplayUtil.dip2px(10f);

        if (isAnimate) {
            dislikeView.animate().setDuration(duration).translationX(dislikeIconWidth);
            FFUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dislikeView.setVisibility(View.GONE);
                    closeIcon.setVisibility(View.VISIBLE);

                }
            }, duration);
        } else {
            dislikeView.setTranslationX(dislikeIconWidth);
            dislikeView.setVisibility(View.GONE);
            closeIcon.setVisibility(View.VISIBLE);
        }
    }

    private void showAddress(final SYUgcModel model) {
        if (model.getIsCustomMerchant() != null && model.getIsCustomMerchant().equals("1")) {
            mActivity.showToast("该商户为自定义创建，暂无地址信息");
            return;
        }
        mActivity.post(Constants.shareConstants().getNetHeaderAdress() + "/shop/queryShopDrawerInfoV250.do", "", null,
                new FFNetWorkCallBack<MainHomeUGCFragment.RestInfoDrawerResult>() {
                    @Override
                    public void onSuccess(MainHomeUGCFragment.RestInfoDrawerResult response, FFExtraParams extra) {
                        new PopRest(mActivity, mSelectedFrag, response.getBuinessDetail().getMerchantPhone()
                                , response.getBuinessDetail().getMerchantAddress()
                                , model.getMerchantName()
                                , response.getBuinessDetail().getMerchantPoi().getLatitude()
                                , response.getBuinessDetail().getMerchantPoi().getLongitude()
                                , model.getMerchantUid()).showAtLocation((View) mActivity.getContainer().getParent()
                                , Gravity.CENTER, 0, 0);
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "merchantId", model.getMerchantUid());
    }

    public class MyHeaderHolder extends RecyclerView.ViewHolder {
        ViewPager mViewPager;
        LinearLayout ll_point_container;
        MyViewPagerAdapter mViewPagerAdapter;

        public MyHeaderHolder(View view) {
            super(view);
            view.setTag(this);
            mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
            ll_point_container = (LinearLayout) view.findViewById(R.id.ll_point_container);
            if (mViewPagerAdapter == null) {
                mViewPagerAdapter = new MyViewPagerAdapter();
            }

            ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
            params.width = DisplayUtil.screenWidth;
            params.height = params.width / 2;
            mViewPager.setLayoutParams(params);

            if (runnable != null) {
                FFUtils.getHandler().removeCallbacks(runnable);
            }
            autoScrollHeaderViewPager();
            mSelectedFrag.prl.addTouchView(getHeaderView());
        }

        public void onBind(int position) {
            setBannerView();
        }


        private void setBannerView() {
            if (mHeaderPgcList == null || mHeaderPgcList.size() <= 0) {
                return;
            }

            ll_point_container.removeAllViews();
            mViewPagerAdapter.points.clear();
            mViewPagerAdapter.getViewList().clear();

            if (mViewPager.getAdapter() == null) {
                mViewPager.setAdapter(mViewPagerAdapter);
            }
            mViewPagerAdapter.setDataList(mHeaderPgcList);

            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    onMyPageSelected(position);
                }

            });

            for (int i = 0; i < mHeaderPgcList.size(); i++) {
                View view = View.inflate(mActivity, R.layout.item_home_ugc_pgcs, null);
                ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
                final AdModelsList.ItemsAd model = mHeaderPgcList.get(i);
                FFImageLoader.loadBigImage(mActivity, model.image, iv_img);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!FFUtils.checkNet()) {
                            mActivity.showToast("网络连接失败，请检查网络设置");
                            return;
                        }
                        PGCDetailIntent data = new PGCDetailIntent();
//                        data.setId(model.id);
                        data.setUrl(model.targetUrl);
                        data.setImgUrl(model.image);
                        data.setTitle(model.title);
                        data.setAccount(SP.getUid());
                        data.setToken(SP.getToken());
                        data.setVersion(FFUtils.getVerName());
                        mActivity.startActivity(PGCDetailActivity.class, data);
                    }
                });

                mViewPagerAdapter.getViewList().add(view);

                View point = new View(mActivity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FFUtils.getPx(4), FFUtils.getPx(4));
                point.setLayoutParams(params);
                ll_point_container.addView(point);
                if (i != 0) {
                    params.setMargins(FFUtils.getPx(13), 0, 0, 0);
                }
                mViewPagerAdapter.points.add(point);
            }
            mViewPagerAdapter.notifyDataSetChanged();
            if (mViewPagerAdapter.getViewList().size() > 0) {
                mViewPager.setCurrentItem(0);
            }
            onMyPageSelected(0);
        }

        private void onMyPageSelected(int position) {
            for (int i = 0; i < mViewPagerAdapter.points.size(); i++) {
                if (position == i) {
                    mViewPagerAdapter.points.get(i).setBackgroundResource(R.drawable.home_ugc_pgc_point_yellow);
                } else {
                    mViewPagerAdapter.points.get(i).setBackgroundResource(R.drawable.home_ugc_pgc_point_white);
                }
            }
        }

        /**
         * 自动滚动头部ViewPager的导航点
         */
        public void autoScrollHeaderViewPager() {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (mActivity == null || mSelectedFrag == null
                            || mSelectedFrag.getDestroyed() || mSelectedFrag.isDetached()) {
                        return;
                    }
                    autoScrollHeaderViewPager();
                    int size = mViewPagerAdapter.points.size();
                    if (size > 1) {
                        mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % size);
                    }
                }
            };
            FFUtils.getHandler().postDelayed(runnable, 2000);
        }
    }

    public class MyRecommendHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_name;
        TextView tv_desc;
        ImageView imageView;
        ImageView close_icon;
        View dislikeView;
        View dislikeDel;

        public MyRecommendHolder(View view) {
            super(view);
            view.setTag(this);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            imageView = (ImageView) view.findViewById(R.id.imageView1);
            close_icon = (ImageView) view.findViewById(R.id.recommend_close_icon);
            dislikeView = view.findViewById(R.id.recommend_dislike);
            tv_title.getPaint().setFakeBoldText(true);
            dislikeDel = view.findViewById(R.id.recommend_dislike_del_icon);
            restBtnState(false);

            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.width = DisplayUtil.screenWidth - DisplayUtil.dip2px(12 + 20) * 2;
            params.height = (int) (params.width * 0.51f);
            imageView.setLayoutParams(params);
        }

        public void onBind(int position) {
            final SYUgcModel model = mFeedsList.get(position);

            FFImageLoader.loadBigImage(mActivity, model.getFrontCoverImg(), imageView);

            if (!TextUtils.isEmpty(model.getMerchantName())) {
                tv_name.setVisibility(View.VISIBLE);
                tv_name.setText(model.getMerchantName());
            } else {
                tv_name.setVisibility(View.GONE);
            }

            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddress(model);
                }
            });

            if (!TextUtils.isEmpty(model.getFrontCoverContent())) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(model.getFrontCoverContent());
            } else {
                tv_title.setVisibility(View.GONE);
            }

            StringBuilder builder = new StringBuilder();
            if (!TextUtils.isEmpty(model.getFoodType())) {
                builder.append(model.getFoodType());
            }
            if (!TextUtils.isEmpty(model.getMerchantStreet())) {
                if (builder.length() > 0) {
                    builder.append(" • ");
                }
                builder.append(model.getMerchantStreet());
            }
            if (model.getWantEat() > 0) {
                if (builder.length() > 0) {
                    builder.append(" • ");
                }
                builder.append(model.getWantEat() + "人想吃");
            }
            if (builder.length() > 0) {
                tv_desc.setVisibility(View.VISIBLE);
                tv_desc.setText(builder);
            } else {
                tv_desc.setVisibility(View.GONE);
            }

            //点击XX图标，展示出不喜欢按钮
            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    close_icon.setVisibility(View.GONE);
                    dislikeView.setVisibility(View.VISIBLE);
                    dislikeView.animate().translationX(0).setInterpolator(new LinearInterpolator()).setDuration(200);
                }
            });

            //点击不喜欢按钮
            dislikeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startHideRecommendViewAnimator();
                    doDislike(model.getMerchantUid());
                }
            });

            //关闭不喜欢按钮
            dislikeDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restBtnState(true);
                }
            });

            //跳转到详情，加入想吃后就会触发推荐完成操作
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!FFUtils.checkNet()) {
                        mActivity.showToast("网络连接失败，请检查网络设置");
                        return;
                    }
                    DynamicDetailIntent intent = new DynamicDetailIntent();
                    intent.setId(model.getFeedId());
                    intent.setResource("9");//9非计划性推荐
                    mActivity.startActivity(DynamicDetailActivity.class, intent);
                }
            });
        }

        public void startHideRecommendViewAnimator() {
            ValueAnimator animator = ValueAnimator.ofInt(itemView.getHeight(), 0);
            animator.setDuration(300);
            animator.setInterpolator(new LinearInterpolator());

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                    params.height = value;
                    itemView.setLayoutParams(params);
                }
            });
            animator.start();
        }

        /**
         * 加入到不喜欢列表
         */
        private void doDislike(final String shopId) {
            if (!SP.isLogin()) {
                return;
            }
            mActivity.post(IUrlUtils.HomeUrl.SELECTED_RECOMMEND_DISLIKE, null, null, new FFNetWorkCallBack<BaseResult>() {
                @Override
                public void onSuccess(BaseResult response, FFExtraParams extra) {
                    if (response != null) {
                    }
                }

                @Override
                public boolean onFail(FFExtraParams extra) {
                    return false;
                }
            }, "shopId", shopId);
        }

    }


    public class HolderFeeds extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_show)
        ImageView iv_show;
        @Bind(R.id.is_add_crown)
        ImageView is_add_crown;
        @Bind(R.id.iv_avatar)
        ImageView iv_avatar;
        @Bind(R.id.iv_friend)
        ImageView iv_friend;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_title)
        TextView tv_title;
        @Bind(R.id.tv_content)
        TextView tv_content;
        @Bind(R.id.rb_level)
        RatingBar rb_level;
        @Bind(R.id.v_line_level)
        View v_line_level;
        @Bind(R.id.tv_level)
        TextView tv_level;
        @Bind(R.id.ll_level_container)
        LinearLayout ll_level_container;
        @Bind(R.id.tv_wanteat_and_eated)
        TextView tv_wanteat_and_eated;
        @Bind(R.id.rl_container)
        RelativeLayout rl_container;

        public HolderFeeds(LayoutInflater inflater) {
            super(inflater.inflate(R.layout.item_ugc, mRecyclerView, false));
            ButterKnife.bind(this, itemView);
            iv_show.getLayoutParams().height = (FFUtils.getDisWidth() - FFUtils.getPx(24)) * 546 / 672;
        }

        public void onBind(int position) {
            final SYUgcModel item = mFeedsList.get(position);
            FFImageLoader.loadAvatar(mActivity, item.getUser().getHeadImage().getUrl(), iv_avatar);

            if (item.getUser().getUserType() == 1) {
                is_add_crown.setVisibility(View.VISIBLE);
            } else {
                is_add_crown.setVisibility(View.GONE);
            }
            try {
                FFImageLoader.loadBigImage(mActivity, item.getFrontCoverImg(), iv_show);
            } catch (Exception e) {
            }

            if (item.getStarLevel() == 0) {
                ll_level_container.setVisibility(View.INVISIBLE);
            } else {
                ll_level_container.setVisibility(View.VISIBLE);
                rb_level.setRating(item.getStarLevel());
                tv_level.setText(SYFeed.pullStartLevelString(item.getStarLevel()));
            }

            if (item.getMerchantName() != null) {
                if (FFUtils.isStringEmpty(item.getFoodType())) {
                    tv_content.setText(item.getMerchantName());
                } else {
                    tv_content.setText(item.getMerchantName() + " · " + item.getFoodType());
                }
                tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showAddress(item);
                    }
                });

                String wanteat_and_eated = "";
                int wanteat = item.getWantEat();
                if (wanteat != 0) wanteat_and_eated += wanteat + "人想吃   ";
                int haveeat = item.getHaveEat();
                if (haveeat != 0) wanteat_and_eated += haveeat + "位好友吃过";
                tv_wanteat_and_eated.setText(wanteat_and_eated);
                if (wanteat == 0 && haveeat == 0) {
                    tv_wanteat_and_eated.setVisibility(View.GONE);
                } else {
                    tv_wanteat_and_eated.setVisibility(View.VISIBLE);
                }
            } else {
                tv_content.setText("");
                tv_content.setOnClickListener(null);
            }
            tv_name.setText(item.getUser().getNickName());
            iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!FFUtils.checkNet()) {
                        mActivity.showToast("网络连接失败，请检查网络设置");
                        return;
                    }
                    if (!item.getUser().getId().equals(SP.getUid())) {
                        UserInfoIntent intent = new UserInfoIntent();
                        intent.setId(item.getUser().getId());
                        if (item.getUser().getUserType() != 0) {
                            mActivity.startActivity(ClubUserInfoActivity.class, intent);
                        } else {
                            mActivity.startActivity(UserInfoActivity.class, intent);
                        }
                    } else {
                        ((MainActivity) mActivity).rb_user.setChecked(true);
                    }
                }
            });
            tv_title.setText(item.getFrontCoverContent());

            if (item.getUser().isFollowMe() && item.getUser().isByFollowMe()) {
                iv_friend.setVisibility(View.VISIBLE);
            } else {
                iv_friend.setVisibility(View.INVISIBLE);
            }
            iv_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!FFUtils.checkNet()) {
                        mActivity.showToast("网络连接失败，请检查网络设置");
                        return;
                    }
                    DynamicDetailIntent intent = new DynamicDetailIntent();
                    intent.setResource("1");//1是首页达人荐
                    intent.setId(item.getFeedId());
                    mActivity.startActivity(DynamicDetailActivity.class, intent);
                }
            });
        }
    }

    private static final class MyViewPagerAdapter extends PagerAdapter {
        public final ArrayList<View> points = new ArrayList<>();
        private final ArrayList<AdModelsList.ItemsAd> mPgcList = new ArrayList<>();
        private final ArrayList<View> mViewList = new ArrayList<>();

        public ArrayList<View> getViewList() {
            return mViewList;
        }

        public ArrayList<AdModelsList.ItemsAd> getDataList() {
            return mPgcList;
        }

        public void setDataList(List<AdModelsList.ItemsAd> pgcList) {
            if (pgcList != null) {
                mPgcList.clear();
                mPgcList.addAll(pgcList);
            }
        }

        @Override
        public int getCount() {
            return mPgcList.size();
        }

        private int mChildCount = 0;

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

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));
            return mViewList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));
        }
    }
}
