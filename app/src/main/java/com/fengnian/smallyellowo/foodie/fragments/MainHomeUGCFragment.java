package com.fengnian.smallyellowo.foodie.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFBaseActivity;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.PGCDetailActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.UserInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYChoiceModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.UGCResult;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.dialogs.PopRest;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.PGCDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import static com.fengnian.smallyellowo.foodie.R.id.is_add_crown;
import static com.fengnian.smallyellowo.foodie.R.id.tv_content;

public class MainHomeUGCFragment extends BaseFragment {
    private static UGCResult response;
    private ListView lv;
    private UGCAdapter adapter;
    int lastX = 0;
    int max = FFUtils.getPx(50);
    //    int i = 0;
    private PullToRefreshLayout prl;

    public void onTitleClicked(boolean showProgressDialog) {
        lv.setSelection(0);
        if (!isLoading) {
            refresh(true);
        }
    }

    public static class UGCHolder {
        ImageView iv_show;
        ImageView iv_avatar,is_add_crown;
        TextView tv_title;
        TextView tv_content;
        TextView tv_wanteat_and_eated;
        LinearLayout ll_like;
        LinearLayout ll_eat_status;
        TextView tv_name;
        TextView tv_eat_status;
        View rl_container;

        LinearLayout ll_level_container;
        TextView tv_level;
        RatingBar rb_level;

        int position = -1;


        public ImageView iv_friend;
    }

//    int mScrollState;

    public MainHomeUGCFragment() {
        super(null);
    }

    /**
     * @param context
     */
    @SuppressLint("ValidFragment")
    public MainHomeUGCFragment(FFBaseActivity context) {
        super(context);
    }

    @Override
    public void onFindView() {
        lv = (ListView) findViewById(R.id.lv_ugc);

        lv.setTag(MotionEvent.ACTION_UP);

        lv.setOnTouchListener(new View.OnTouchListener() {

            int lastAction = -100;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (lastAction != event.getAction()) {
                    lastAction = event.getAction();
                    v.setTag(lastAction);
                    if (lastAction == MotionEvent.ACTION_UP && lv.getChildCount() > 0) {
                        change();
                    }
                }
                return false;
            }
        });

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {


            int lastTop = 0;
            int lastPosition = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lv.getChildCount() > 0) {
//                    change();
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv.getChildCount() == 0) {
                    return;
                }

                if (((Integer) lv.getTag()) == MotionEvent.ACTION_MOVE) {
                    if (lv.getFirstVisiblePosition() == lastPosition) {
                        lastX = -lv.getChildAt(0).getTop() + lastTop;
                        lastTop = lv.getChildAt(0).getTop();
                    } else {
                        lastX = lv.getFirstVisiblePosition() - lastPosition;
                        lastPosition = lv.getFirstVisiblePosition();
                        lastTop = 0;
                    }
                }

                int pullUpY = (int) Math.abs(prl.pullUpY);

                int index = pullUpY >= max ? 0 : 1;
                View childAt = view.getChildAt(index);
                if (childAt == null) {
                    return;
                }
                if (childAt.getTag() == null || !(childAt.getTag() instanceof UGCHolder)) {
                    return;
                }
                UGCHolder holder = ((UGCHolder) childAt.getTag());
                if (holder.iv_avatar == null) {
                    return;
                }
                boolean b = !FFUtils.isListEmpty(response.getPgcs());
                boolean b1 = holder.position == 0;
                float x = childAt.getTop();
                boolean b2 = x > max;
                if (b && b1 && b2) {//如果有pgc并且是第一条ugc并且第一条ugc的y坐标大于屏幕的一半不放大
                    holder.rl_container.setScaleX(1);
                    holder.rl_container.setScaleY(1);
                    return;
                }
//                UGCImageView zoomImageView = holder.uiv_zoom;
                int abs = (int) Math.abs(childAt.getTop() - max + pullUpY);
                float f = abs / (max * 6f);
                if (abs < 3) {
                    f = 0;
                }

                holder.rl_container.setScaleX(Math.max(0.9f, 1 - f));
                holder.rl_container.setScaleY(Math.max(0.9f, 1 - f));
            }
        });
        adapter = new UGCAdapter();
        lv.setAdapter(adapter);

        prl = PullToRefreshLayout.supportPull(lv, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                refresh(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                refresh(false);
            }
        });

        prl.setDoPullUp(true);
        adapter.hasAndDoMore = false;
        prl.setDoPullDown(false);

        refresh(true);
    }

    public class UGCAdapter extends BaseAdapter {

        @Override
        public int getItemViewType(int position) {

            if (position == 0) {
                if (response == null) {
                    return 0;//没有内容的item
                }
                int pgcSize = response.getPgcs() == null ? 0 : response.getPgcs().size();
                int feesSize = response.getFeeds() == null ? 0 : response.getFeeds().size();
                if (pgcSize == 0 && feesSize == 0) {
                    return 0; //没有内容的item
                }
                return 2;//空白item
            }
            int pgcSize = response.getPgcs() == null ? 0 : response.getPgcs().size();
            int feesSize = response.getFeeds() == null ? 0 : response.getFeeds().size();

            if (pgcSize > 0 && position - 1 < pgcSize) {
                return 1;//pgc
            } else if (position - 1 < pgcSize + feesSize) {
                return 3;//ugc
            } else {//最后一条
                return hasAndDoMore ? 4 : 2;//没有更多
            }
        }


        @Override
        public int getViewTypeCount() {
            return 5;
        }

        public boolean hasAndDoMore = true;

        @Override
        public int getCount() {
            if (response == null) {
                return 0;//没有内容的item    //TODO zhangfan return 1
            }

            int pgcSize = response.getPgcs() == null ? 0 : response.getPgcs().size();
            int feesSize = response.getFeeds() == null ? 0 : response.getFeeds().size();

            if (pgcSize == 0 && feesSize == 0) {
                return 0; // TODO zhangfan
            }

            if (pgcSize == 0) {
                return feesSize + 2;
            } else {
                return pgcSize + feesSize + 2;
            }
        }

        @Override
        public Object getItem(int position) {
            if (response == null) {
                return null;//没有内容的item
            }
            int pgcSize = response.getPgcs() == null ? 0 : response.getPgcs().size();
            int feesSize = response.getFeeds() == null ? 0 : response.getFeeds().size();
            if (position == 0) {
                return null;//空白item
            }


            if (pgcSize > 0 && position - 1 < pgcSize) {
                return response.getPgcs().get(position - 1);//pgc
            } else if (position - 1 < pgcSize + feesSize) {
                return response.getFeeds().get(position - 1 - pgcSize);//ugc
            } else {
                return null;//没有更多
            }
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        class Holder {
            ImageView iv_show;
            TextView tv_title;
            TextView tv_content;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            switch (getItemViewType(position)) {
                case 0:// TODO: zhangfan 加载中
                    return convertView;
                case 1://pgc
                {
                    Holder holder;
                    if (convertView == null) {
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.item_ugclist_pgc, parent, false);
                        holder = new Holder();
                        convertView.setTag(holder);
                        holder.iv_show = (ImageView) convertView.findViewById(R.id.iv_show);
                        holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                        holder.tv_content = (TextView) convertView.findViewById(tv_content);
                    } else {
                        holder = (Holder) convertView.getTag();
                    }
                    final SYChoiceModel item = (SYChoiceModel) getItem(position);
                    FFImageLoader.loadBigImage(getBaseActivity(), item.getBackImage().getUrl(), holder.iv_show);
                    holder.tv_title.setText(item.getDetailDescription());

                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PGCDetailIntent data = new PGCDetailIntent();
                            data.setUrl(item.getHtmlUrl());
                            data.setTitle(item.getTitle());
                            data.setId(item.getId());
                            data.setAccount(SP.getUid());
                            data.setToken(SP.getToken());
                            data.setVersion(FFUtils.getVerName());
                            startActivity(PGCDetailActivity.class, data);
                            FFUtils.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    response.getPgcs().remove(item);
                                    notifyDataSetChanged();
                                }
                            }, 1000);

                        }
                    });
                    holder.tv_content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            response.getPgcs().remove(item);
                            notifyDataSetChanged();
//                            post(Constants.shareConstants().getNetHeaderAdress() + "/indexNew/addReadPgcIdV230.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                            post(IUrlUtils.Search.addReadPgcIdV230, null, null, new FFNetWorkCallBack<BaseResult>() {
                                @Override
                                public void onSuccess(BaseResult response, FFExtraParams extra) {

                                }

                                @Override
                                public boolean onFail(FFExtraParams extra) {
                                    return false;
                                }
                            }, "pgcId", item.getId());
                        }
                    });
                }
                return convertView;
                case 2://空白
                {
                    if (convertView == null) {
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.item_main_home_ugc_header, parent, false);
                    }

                    if (position == 0 && !FFUtils.isListEmpty(response.getPgcs())) {
                        convertView.setPadding(0, 0, 0, 0);
                    } else {
                        convertView.setPadding(0, FFUtils.getPx(50), 0, 0);
                    }

                }
                return convertView;
                case 3://feed
                    UGCHolder holder;
                    if (convertView == null) {
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.item_main_home_ugc, parent, false);
                        holder = new UGCHolder();
                        convertView.setTag(holder);
                        holder.rl_container = (RelativeLayout) convertView.findViewById(R.id.rl_container);
                        holder.iv_show = (ImageView) convertView.findViewById(R.id.iv_show);
                        holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);

                        holder.is_add_crown=(ImageView) convertView.findViewById(is_add_crown);
                        holder.iv_friend = (ImageView) convertView.findViewById(R.id.iv_friend);
                        holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                        holder.tv_content = (TextView) convertView.findViewById(tv_content);

                        holder.tv_wanteat_and_eated = (TextView) convertView.findViewById(R.id.tv_wanteat_and_eated);
                        holder.ll_like = (LinearLayout) convertView.findViewById(R.id.ll_like);
                        holder.ll_eat_status = (LinearLayout) convertView.findViewById(R.id.ll_eat_status);
                        holder.tv_eat_status = (TextView) convertView.findViewById(R.id.tv_eat_status);
                        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                        holder.ll_level_container = (LinearLayout) convertView.findViewById(R.id.ll_level_container);
                        holder.rb_level = (RatingBar) convertView.findViewById(R.id.rb_level);
                        holder.tv_level = (TextView) convertView.findViewById(R.id.tv_level);
                        onGetView(holder);
                    } else {
                        holder = (UGCHolder) convertView.getTag();
                    }
                    initView(convertView, holder, (UGCResult.UGCData) getItem(position));
                    return convertView;
                case 4://更多
                    if (convertView == null) {
                        convertView = getActivity().getLayoutInflater().inflate(R.layout.item_main_home_nomore, parent, false);
                        convertView.findViewById(R.id.rl_container).getLayoutParams().height = (int) (FFUtils.getDisHight() - FFUtils.getPx(48 * 2) - FFUtils.getStatusbarHight(context()) - FFUtils.getPx(100));
                    }

                    return convertView;
            }

            return convertView;
        }

        public void initView(final View convertView, final UGCHolder holder, final UGCResult.UGCData item) {
            int lastPosition = holder.position;
            holder.position = response.getFeeds().indexOf(item);
            FFImageLoader.loadAvatar(context(), item.getHeadImg(), holder.iv_avatar);

            if(item.getUserType()==1){
                holder.is_add_crown.setVisibility(View.VISIBLE);
            }else {
                holder.is_add_crown.setVisibility(View.GONE);
            }
            try {
                FFImageLoader.loadBigImage(context(), item.getFrontCoverImg(), holder.iv_show);
            } catch (Exception e) {
            }

            if (item.getStarLevel() == 0) {
                holder.ll_level_container.setVisibility(View.GONE);
            } else {
                holder.ll_level_container.setVisibility(View.VISIBLE);
                holder.rb_level.setRating(item.getStarLevel());
                holder.tv_level.setText(SYFeed.pullStartLevelString(item.getStarLevel()));
            }

            if (item.getMerchantName() != null) {
                holder.tv_content.setText(item.getMerchantName());
//                    holder.tv_content.getLayoutParams().width = (int) FFUtils.getTextViewLength(holder.tv_content, item_image_gallery.getSyNewFeed().getFood().getPoi().getTitle());
                holder.tv_content.invalidate();
                holder.tv_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (item.getIsCustomMerchant() != null && item.getIsCustomMerchant().equals("1")) {
                            showToast("该商户为自定义创建，暂无地址信息");
                            return;
                        }
//                        post(Constants.shareConstants().getNetHeaderAdress() + "/shop/queryShopDrawerInfoV250.do", "", null, new FFNetWorkCallBack<RestInfoDrawerResult>() {
                        post(IUrlUtils.Search.queryShopDrawerInfoV250, "", null, new FFNetWorkCallBack<RestInfoDrawerResult>() {
                            @Override
                            public void onSuccess(RestInfoDrawerResult response, FFExtraParams extra) {
                                new PopRest(getActivity(),MainHomeUGCFragment.this, response.getBuinessDetail().getMerchantPhone(), item.getMerchantAddress(), item.getMerchantName(), item.getMerchantLatitude(), item.getMerchantLongitude(),item.getMerchantUid()).showAtLocation((View) getBaseActivity().getContainer().getParent(), Gravity.CENTER, 0, 0);
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, "merchantId", item.getMerchantUid());
                    }
                });

                String wanteat_and_eated = "";
                int wanteat = item.getWantEat();
                if (wanteat != 0) wanteat_and_eated += wanteat + "人想吃   ";
                int haveeat = item.getHaveEat();
                if (haveeat != 0) wanteat_and_eated += haveeat + "位好友吃过";
                holder.tv_wanteat_and_eated.setText(wanteat_and_eated);
                if (wanteat == 0 && haveeat == 0) {
                    holder.tv_wanteat_and_eated.setVisibility(View.GONE);
                } else {
                    holder.tv_wanteat_and_eated.setVisibility(View.VISIBLE);
                }
            } else {
                holder.tv_content.setText("");
                holder.tv_content.setOnClickListener(null);
            }
            holder.tv_name.setText(item.getNickName());
            holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.getUserId().equals(SP.getUid())) {
                        UserInfoIntent intent = new UserInfoIntent();
                        intent.setId(item.getUserId());
                        startActivity(UserInfoActivity.class, intent);
//                        IsAddCrownUtils.FragmentActivtyStartAct(item_image_gallery,intent,activity);
                    } else {
//                        ((MainActivity) activity).rb_user.setChecked(true);
                    }
                }
            });
            holder.tv_title.setText(item.getFrontCoverContent());

            if (item.isFollowMe() && item.isByFollowMe()) {
                holder.iv_friend.setVisibility(View.VISIBLE);
            } else {
                holder.iv_friend.setVisibility(View.INVISIBLE);
            }


            if (item.isbEat()) {
                holder.ll_eat_status.setOnClickListener(null);
                holder.tv_eat_status.setText("已吃过");
                holder.tv_eat_status.setTextColor(getResources().getColor(R.color.ff_text_gray));
                holder.tv_eat_status.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_ugc_wanteat_pressed, 0, 0, 0);
            } else {
                holder.tv_eat_status.setText("吃过");
                holder.tv_eat_status.setTextColor(getResources().getColor(R.color.ff_text_black));
                holder.tv_eat_status.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_ugc_eated, 0, 0, 0);
                holder.ll_eat_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.setbEat(true);
                        holder.ll_eat_status.setOnClickListener(null);
                        holder.tv_eat_status.setText("已吃过");
                        holder.tv_eat_status.setTextColor(getResources().getColor(R.color.ff_text_gray));
                        holder.tv_eat_status.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_ugc_wanteat_pressed, 0, 0, 0);
                        if (lv.getFirstVisiblePosition() + 2 < getCount() - 1 - 1) {
                            int i = lv.getChildAt(1).getTop();
                            lv.smoothScrollToPositionFromTop(lv.getFirstVisiblePosition() + 2, around(i, max, Math.max(1500, Math.abs(i - max) / 20)));
                        }
//                        post(Constants.shareConstants().getNetHeaderAdress() + "/eat/hasEatFoodV250.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                        post(IUrlUtils.Search.hasEatFoodV250, null, null, new FFNetWorkCallBack<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult response, FFExtraParams extra) {

                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, "recordId", item.getFeedId(), "merchantUid", item.getMerchantUid(), "isResource", "1", "shopType", "0");
                    }
                });
            }

            holder.ll_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final SharedPreferences likeSp = activity.getSharedPreferences("like", Context.MODE_PRIVATE);
                    if(!likeSp.getBoolean("isNoLike", false)){
                        likeSp.edit().putBoolean("isNoLike", true).commit();
                        EnsureDialog.showEnsureDialog(context(), true, "点击“不喜欢”该内容将不再显示，是否要继续？", "继续",null, "取消", new EnsureDialog.EnsureDialogListener() {
                            @Override
                            public void onOk(DialogInterface dialog) {
                                dialog.dismiss();
                                onClick(v);
                            }

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        return;
                    }
                    response.getFeeds().remove(item);
                    notifyDataSetChanged();
//                    post(Constants.shareConstants().getNetHeaderAdress() + "/saveRecommendV250.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                    post(IUrlUtils.Search.saveRecommendV250, null, null, new FFNetWorkCallBack<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult response, FFExtraParams extra) {
                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            return false;
                        }
                    }, "handleState", "2", "recordId", item.getFeedId());
                }
            });

            if (!FFUtils.isListEmpty(response.getPgcs()) && ((lastPosition == -1) || (lastPosition == 0 && convertView.getTop() > max)) && holder.position == 0) {//第一条第一次加载
                holder.rl_container.setScaleX(1);
                holder.rl_container.setScaleY(1);
            } else {
                holder.rl_container.setScaleX(0.9f);
                holder.rl_container.setScaleY(0.9f);
            }
            holder.iv_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DynamicDetailIntent intent = new DynamicDetailIntent();
                    intent.setId(item.getFeedId());
                    startActivity(DynamicDetailActivity.class, intent);
                }
            });
        }

        public void onGetView(final UGCHolder holder) {
            holder.iv_show.getLayoutParams().height = (int) (FFUtils.getDisWidth() * 0.65);
            holder.rl_container.getLayoutParams().height = (int) (FFUtils.getDisHight() - FFUtils.getPx(48 * 2) - FFUtils.getStatusbarHight(context()) - FFUtils.getPx(100));
        }

    }

    boolean isLoading = false;


    public void refresh(final boolean isInit) {
        isLoading = true;
//        post(Constants.shareConstants().getNetHeaderAdress() + "/indexNew/queryIndexMasterV250.do", isInit ? "" : null, null, new FFNetWorkCallBack<UGCResult>() {
        post(IUrlUtils.Search.queryIndexMasterV250, isInit ? "" : null, null, new FFNetWorkCallBack<UGCResult>() {

            @Override
            public void onBack(FFExtraParams extra) {
                isLoading = false;
            }

            @Override
            public void onSuccess(final UGCResult response, FFExtraParams extra) {
                if (isInit || MainHomeUGCFragment.response == null) {
                    MainHomeUGCFragment.response = response;
                } else {
                    MainHomeUGCFragment.response.getFeeds().addAll(response.getFeeds());
                }
                if (FFUtils.isListEmpty(response.getFeeds())) {
                    prl.setDoPullUp(false);
                    adapter.hasAndDoMore = true;
                } else {
                    prl.setDoPullUp(true);
                    adapter.hasAndDoMore = false;
                }
                final int position = adapter.getCount() - 2;
                adapter.notifyDataSetChanged();
                if (!isInit) {
                    if (!FFUtils.isListEmpty(MainHomeUGCFragment.response.getFeeds()) && prl.pullUpY != 0) {
                        lv.setSelectionFromTop(lv.getFirstVisiblePosition() + 2, (int) (max + Math.abs(prl.pullUpY)));
                    }
                }

                FFUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (response.getFeeds().size() > 0)
                            prl.loadmoreFinish(prl.SUCCEED);
                        else prl.loadmoreFinish(prl.NO_DATA_SUCCEED);
                        prl.refreshFinish(prl.SUCCEED);
                    }
                }, 10);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.loadmoreFinish(prl.FAIL);
                prl.refreshFinish(prl.FAIL);
                return false;
            }
        }, "isFirst", isInit ? 1 : 0);
    }

    public static class RestInfoDrawerResult extends BaseResult {

        private BuinessDetailBean buinessDetail;

        public BuinessDetailBean getBuinessDetail() {
            return buinessDetail;
        }

        public void setBuinessDetail(BuinessDetailBean buinessDetail) {
            this.buinessDetail = buinessDetail;
        }

        public static class BuinessDetailBean {
            String	merchantUid	;//		店铺id	是
            SYImage merchantImage	;//		店铺图片信息	否
            String	merchantName	;//		店铺名称	否
            String	merchantDistance	;//		距离	否
            boolean	merchantIsWant	;//		是否想吃	否
            boolean	merchantIsRelation	;//		是否在关系人里边	否
            boolean	merchantIsDa	;//		是否在达人荐里边	否
            String	merchantKind	;//		店铺类型	否
            String	merchantPrice	;//		店铺人均（价格）	否
            String	merchantAddress	;//		店铺详细地址	否
            String	friendShares	;//		多少个圈友分享	否
            String	merchantArea	;//		商圈	否
            String	merchantPhone	;//		商户电话	否
            SYPoi merchantPoi	;//		商户Poi	否
            double	startLevel	;//		平均星级	否

            public String getMerchantUid() {
                return merchantUid;
            }

            public void setMerchantUid(String merchantUid) {
                this.merchantUid = merchantUid;
            }

            public SYImage getMerchantImage() {
                return merchantImage;
            }

            public void setMerchantImage(SYImage merchantImage) {
                this.merchantImage = merchantImage;
            }

            public String getMerchantName() {
                return merchantName;
            }

            public void setMerchantName(String merchantName) {
                this.merchantName = merchantName;
            }

            public String getMerchantDistance() {
                return merchantDistance;
            }

            public void setMerchantDistance(String merchantDistance) {
                this.merchantDistance = merchantDistance;
            }

            public boolean isMerchantIsWant() {
                return merchantIsWant;
            }

            public void setMerchantIsWant(boolean merchantIsWant) {
                this.merchantIsWant = merchantIsWant;
            }

            public boolean isMerchantIsRelation() {
                return merchantIsRelation;
            }

            public void setMerchantIsRelation(boolean merchantIsRelation) {
                this.merchantIsRelation = merchantIsRelation;
            }

            public boolean isMerchantIsDa() {
                return merchantIsDa;
            }

            public void setMerchantIsDa(boolean merchantIsDa) {
                this.merchantIsDa = merchantIsDa;
            }

            public String getMerchantKind() {
                return merchantKind;
            }

            public void setMerchantKind(String merchantKind) {
                this.merchantKind = merchantKind;
            }

            public String getMerchantPrice() {
                return merchantPrice;
            }

            public void setMerchantPrice(String merchantPrice) {
                this.merchantPrice = merchantPrice;
            }

            public String getMerchantAddress() {
                return merchantAddress;
            }

            public void setMerchantAddress(String merchantAddress) {
                this.merchantAddress = merchantAddress;
            }

            public String getFriendShares() {
                return friendShares;
            }

            public void setFriendShares(String friendShares) {
                this.friendShares = friendShares;
            }

            public String getMerchantArea() {
                return merchantArea;
            }

            public void setMerchantArea(String merchantArea) {
                this.merchantArea = merchantArea;
            }

            public String getMerchantPhone() {
                return merchantPhone;
            }

            public void setMerchantPhone(String merchantPhone) {
                this.merchantPhone = merchantPhone;
            }

            public SYPoi getMerchantPoi() {
                return merchantPoi;
            }

            public void setMerchantPoi(SYPoi merchantPoi) {
                this.merchantPoi = merchantPoi;
            }

            public double getStartLevel() {
                return startLevel;
            }

            public void setStartLevel(double startLevel) {
                this.startLevel = startLevel;
            }
        }
    }


//    private boolean isAutoIng = false;

    private void change() {
//        if (isAutoIng) {
//            return;
//        }
//        isAutoIng = true;
        Log.e("start", "自动归位");
        final int first = lv.getFirstVisiblePosition();
        int i = lv.getChildAt(1).getTop();
//        lv.smoothScrollToPositionFromTop(first, lv.getChildAt(0).getTop());
//        if (i == max) {
////            isAutoIng = false;
//            return;
//        }

//        final Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (i == max) {
////                    isAutoIng = false;
//                    System.out.println("a"+i);
//                    return;
//                }
//                if (((Integer) lv.getTag()) != MotionEvent.ACTION_UP) {
////                    isAutoIng = false;
//                    System.out.println("b"+i);
//                    return;
//                }//
        int step = 1500;

        if (lastX > 0) {
            lv.smoothScrollToPositionFromTop(first + 1, i = around(i, max, Math.max(step, Math.abs(i - max) / 20)), 100);
        } else if (lastX < 0) {
            lv.smoothScrollToPositionFromTop(first, i = around(i, max, Math.max(step, Math.abs(i - max) / 20)), 100);
        } else if (lv.getChildAt(0).getTop() > -FFUtils.getPx(180)) {
            lv.smoothScrollToPositionFromTop(first, i = around(i, max, Math.max(step, Math.abs(i - max) / 20)), 100);
        } else {
            lv.smoothScrollToPositionFromTop(first + 1, i = around(i, max, Math.max(step, Math.abs(i - max) / 20)), 100);
        }
//                sendEmptyMessageDelayed(1, 40);
//            }
//        };
//        handler.sendEmptyMessageDelayed(1, 40);
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home_ugc, container, false);
    }

    public static int around(int firstTop, int max, int step) {
        if (Math.abs(firstTop - max) <= step) {
            return max;
        }
        if (firstTop > max) {
            firstTop -= step;
            return firstTop;
        }
        if (firstTop < max) {
            firstTop += step;
            return firstTop;
        }
        return 0;
    }

    public class MyIm extends ImageSpan {
        public MyIm(Drawable arg0) {
            super(arg0);
        }

        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fm) {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fm.ascent = -bottom;
                fm.top = -bottom;
                fm.bottom = top;
                fm.descent = top;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {
            Drawable b = getDrawable();
            canvas.save();
            int transY = 0;
            transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }
    }
}
