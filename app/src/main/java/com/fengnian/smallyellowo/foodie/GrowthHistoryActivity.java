package com.fengnian.smallyellowo.foodie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.TimeUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.food_history.BaseGrowthHistoryItem;
import com.fengnian.smallyellowo.foodie.bean.food_history.GrowthHistoryResult;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYCommentGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYCompleteCount;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYCompleteRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYConcernedFansGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYFoodGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYFulfillGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYGoodChoiceGrowthRecord;
import com.fengnian.smallyellowo.foodie.bean.food_history.SYWantEatGrowthRecord;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.FastDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.WanEatDetailIntent;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.fan.framework.utils.FFUtils.getSubFloat;


/**
 * Holder继承关系图
 * {@link BaseHolder}                                            *  {@link CompleteViewHolder}
 * {@link BaseCompleteHolder}               * {@link ElseHolder} *
 * {@link FeedHolder}                       *
 * {@link FastHolder}  *  {@link RichHolder}*
 */
public class GrowthHistoryActivity extends BaseActivity<IntentData> {

    @Bind(R.id.listView1)
    RecyclerView listView1;
    @Bind(R.id.s_status_bar)
    View s_status_bar;
    @Bind(R.id.iv_back)
    ImageView iv_back;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.v_title_bottom)
    View v_title_bottom;
    @Bind(R.id.ll_dynamic_title)
    View ll_dynamic_title;

    ArrayList<BaseGrowthHistoryItem> items = new ArrayList<>();
    private GrowthHistoryActivity.MyAdapter adapter;

    Drawable avatar;
    Rect avatarBounds;
    private PullToRefreshLayout prl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_growth_history);
        ButterKnife.bind(this);
        setNotitle(true);

        avatar = getResources().getDrawable(R.mipmap.moren_head_img);
        avatarBounds = new Rect(0, 0, FFUtils.getPx(20), FFUtils.getPx(20));
        avatar.setBounds(avatarBounds);

        listView1.setLayoutManager(new LinearLayoutManager(this));
        initBottom();
        adapter = new MyAdapter();
        listView1.setAdapter(adapter);
        getData();
        prl = PullToRefreshLayout.supportPull(listView1, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData();
            }
        });
        prl.setDoPullUp(true);
        prl.setDoPullDown(false);

        listView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollOffset = s_status_bar.getLayoutParams().height + FFUtils.getPx(60);

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
                if (view.getChildCount() == 0) {
                    ll_dynamic_title.setBackgroundColor(0xffffffff);
                    s_status_bar.setBackgroundColor(0xff000000);
                    iv_back.setImageResource(R.mipmap.ff_ic_back_pressed);
                    tv_title.setTextColor(0xff000000);
                    v_title_bottom.setVisibility(View.VISIBLE);
                    return;
                }
                if (view.getChildAdapterPosition(view.getChildAt(0)) == 0 && view.getChildCount() > 0) {
                    if (scrollOffset > Math.abs(view.getChildAt(0).getTop())) {
                        int alpha = 0xff * Math.abs(view.getChildAt(0).getTop()) / scrollOffset;
                        int color = alpha << 24 | 0x00ffffff;
                        ll_dynamic_title.setBackgroundColor(color);
                        s_status_bar.setBackgroundColor(0);
                        iv_back.setImageResource(R.mipmap.ff_ic_back_normal);
                        tv_title.setTextColor(0xffffffff);
                        v_title_bottom.setVisibility(View.INVISIBLE);
                    } else {//100 %
                        ll_dynamic_title.setBackgroundColor(0xffffffff);
                        s_status_bar.setBackgroundColor(0xff000000);
                        iv_back.setImageResource(R.mipmap.ff_ic_back_pressed);
                        tv_title.setTextColor(0xff000000);

                        v_title_bottom.setVisibility(View.VISIBLE);
                    }
                } else {//100 %
                    ll_dynamic_title.setBackgroundColor(0xffffffff);
                    s_status_bar.setBackgroundColor(0xff000000);
                    iv_back.setImageResource(R.mipmap.ff_ic_back_pressed);
                    tv_title.setTextColor(0xff000000);
                    v_title_bottom.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getData() {
//        post(Constants.shareConstants().getNetHeaderAdress() + "/progess/queryProgessListV290" + ".do", "", null, new FFNetWorkCallBack<GrowthHistoryResult>() {
        post(IUrlUtils.Search.queryProgessListV290, "", null, new FFNetWorkCallBack<GrowthHistoryResult>() {
            @Override
            public void onSuccess(GrowthHistoryResult response, FFExtraParams extra) {
//                items = new ArrayList<BaseGrowthHistoryItem>();
                page++;
                if (response.getGrowthRecordList() != null) {
                    sendGrowthRedDotBroadcast("");
                    for (int i = 0; i < response.getGrowthRecordList().size(); i++) {
                        items.add((BaseGrowthHistoryItem) response.getGrowthRecordList().get(i));
                    }
                    adapter.notifyDataSetChanged();
                    if (response.getGrowthRecordList().isEmpty()) {
                        prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
                    } else {
                        prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                } else {
                    prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }

//                for (int i = 0; i < 100; i++) {
//                    SYConcernedFansGrowthRecord e = new SYConcernedFansGrowthRecord();
//                    e.setType(1);
//                    e.setCount(100);
//                    SYUser user = new SYUser();
//                    user.setNickName("asdfasdf");
//                    user.getHeadImage().setUrl(avatarUrls[i]);
//                    e.setUser(user);
//                    e.setTimeInterval(System.currentTimeMillis() / 1000);
//                    items.add(e);
//                }
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.loadmoreFinish(PullToRefreshLayout.FAIL);
                return false;
            }
        }, "lastId", "0", "dateTimestamp", FFUtils.isListEmpty(items) ? 0 : items.get(items.size() - 1).getTimeInterval(), "pageSize", 20, "userId", SP.getUid(), "pageNumber", page);
    }

    private int page = 0;

    private final int imgHeight = (FFUtils.getDisWidth() - FFUtils.getPx(60 + 7 + 10 + 7 + 10 + 10)) / 3;

    /**
     * 基础
     * 包含时间年份的显示
     */
    private abstract class BaseHolder extends RecyclerView.ViewHolder {
        protected TextView tv_year;
        protected TextView tv_date;
        protected TextView tv_month;
        protected ImageView iv_complete;
        protected FrameLayout fl_content_container;


        public BaseHolder(int layout) {
            super(getLayoutInflater().inflate(R.layout.item_grow_parent, listView1, false));
            tv_year = (TextView) itemView.findViewById(R.id.tv_year);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_month = (TextView) itemView.findViewById(R.id.tv_month);
            iv_complete = (ImageView) itemView.findViewById(R.id.iv_complete);
            fl_content_container = (FrameLayout) itemView.findViewById(R.id.fl_content_container);
            fl_content_container.addView(getLayoutInflater().inflate(layout, fl_content_container, false));
        }

        public void onBind(int position, final BaseGrowthHistoryItem item) {
            iv_complete.setVisibility(View.GONE);
            fl_content_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item instanceof SYCommentGrowthRecord) {//评论或赞
                        if (((SYCommentGrowthRecord) item).getCommentGrowthFoodtype() == 1) {//速记
                            startActivity(FastDetailActivity.class, new FastDetailIntent(((SYCommentGrowthRecord) item).getFeedID()));
                        } else {//富文本
                            startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(((SYCommentGrowthRecord) item).getFeedID()));
                        }
                    } else if (item instanceof SYConcernedFansGrowthRecord) {//关注或粉丝历程
                        if (((SYConcernedFansGrowthRecord) item).getUser() == null) {
                            return;
                        }
                        UserInfoIntent data = new UserInfoIntent();
                        data.setId(((SYConcernedFansGrowthRecord) item).getUser().getId());
                        data.setUser(((SYConcernedFansGrowthRecord) item).getUser());
                        if (((SYConcernedFansGrowthRecord) item).getUser().getUserType() == 1) {
                            startActivity(ClubUserInfoActivity.class, data);
                        } else {
                            startActivity(UserInfoActivity.class, data);
                        }
                    } else if (item instanceof SYFoodGrowthRecord) {//富文本美食或速记美食历程
                        if (((SYFoodGrowthRecord) item).getType() == 1) {//速记
                            startActivity(FastDetailActivity.class, new FastDetailIntent(((SYFoodGrowthRecord) item).getFeedID()));
                        } else {//富文本
                            startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(((SYFoodGrowthRecord) item).getFeedID()));
                        }
                    } else if (item instanceof SYFulfillGrowthRecord) {//美食分享被加入想吃清单或被践行历程
                        if (FFUtils.isStringEmpty(((SYFulfillGrowthRecord) item).getFeedID())) {
                            return;
                        }
                        int type = ((SYFulfillGrowthRecord) item).getType();
                        if (type == 1 || type == 2) {
                            if (((SYFulfillGrowthRecord) item).getFoodtype() == 1) {//速记
                                startActivity(FastDetailActivity.class, new FastDetailIntent(((SYFulfillGrowthRecord) item).getFeedID()));
                            } else {//富文本
                                startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(((SYFulfillGrowthRecord) item).getFeedID()));
                            }
                        } else if (type == 3 || type == 4) {//践行用户对应内容
                            if (((SYFulfillGrowthRecord) item).getFoodtype() == 1) {//速记
                                startActivity(FastDetailActivity.class, new FastDetailIntent(((SYFulfillGrowthRecord) item).getFeedID()));
                            } else {//富文本
                                startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(((SYFulfillGrowthRecord) item).getFeedID()));
                            }
                        } else if (type == 5) {
                            if (FFUtils.isStringEmpty(((SYFulfillGrowthRecord) item).getBusinessID())) {
                                return;
                            }
                            WanEatDetailIntent data = new WanEatDetailIntent();
                            data.setBusinessId(((SYFulfillGrowthRecord) item).getBusinessID());
                            data.setRecordId(((SYFulfillGrowthRecord) item).getFeedID());
                            startActivity(WantEatDetailActivity.class, data);
                        } else {

                        }
                    } else if (item instanceof SYGoodChoiceGrowthRecord) {//被设置为精选的历程
                        if (FFUtils.isStringEmpty(((SYGoodChoiceGrowthRecord) item).getFeedID())) {
                            return;
                        }
                        if (((SYGoodChoiceGrowthRecord) item).getType() == 1) {//速记
                            startActivity(FastDetailActivity.class, new FastDetailIntent(((SYGoodChoiceGrowthRecord) item).getFeedID()));
                        } else {//富文本
                            startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(((SYGoodChoiceGrowthRecord) item).getFeedID()));
                        }
                    } else if (item instanceof SYWantEatGrowthRecord) {//想吃清单历程
                        if (FFUtils.isStringEmpty(((SYWantEatGrowthRecord) item).getMerchantId())) {
                            return;
                        }
                        WanEatDetailIntent data = new WanEatDetailIntent();
                        data.setBusinessId(((SYWantEatGrowthRecord) item).getMerchantId());
                        data.setRecordId(((SYWantEatGrowthRecord) item).getFeedID());
                        startActivity(WantEatDetailActivity.class, data);
                    }
                }
            });
            {
                tv_year.setText(TimeUtils.getTime("yyyy年", item.getTimeInterval()));
                tv_month.setText(TimeUtils.getTime("M月", item.getTimeInterval()));
                tv_date.setText(TimeUtils.getTime("d", item.getTimeInterval()));
            }

            tv_year.setVisibility(View.VISIBLE);
            tv_month.setVisibility(View.VISIBLE);
            tv_date.setVisibility(View.VISIBLE);

            if (position != 1) {
                BaseGrowthHistoryItem lastItem = adapter.getItem(position - 1);
                Calendar calender = Calendar.getInstance();
                Calendar lastcalender = Calendar.getInstance();
                calender.setTimeInMillis(item.getTimeInterval());
                lastcalender.setTimeInMillis(lastItem.getTimeInterval());
                if (calender.get(Calendar.YEAR) == lastcalender.get(Calendar.YEAR)) {
                    tv_year.setVisibility(View.GONE);
                }
                if (calender.get(Calendar.YEAR) == lastcalender.get(Calendar.YEAR) &&
                        calender.get(Calendar.MONTH) == lastcalender.get(Calendar.MONTH) &&
                        calender.get(Calendar.DAY_OF_MONTH) == lastcalender.get(Calendar.DAY_OF_MONTH)) {//年月日只要有一个不等
                    tv_month.setVisibility(View.INVISIBLE);
                    tv_date.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * 基础类
     * 包含各种达成
     */
    private class BaseCompleteHolder extends BaseHolder {
        protected LinearLayout ll_complete_container;

        public BaseCompleteHolder(int id) {
            super(id);
            ll_complete_container = (LinearLayout) fl_content_container.findViewById(R.id.ll_complete_container);
        }

        @Override
        public void onBind(int position, BaseGrowthHistoryItem item) {
            super.onBind(position, item);


            SYCompleteRecord record = (SYCompleteRecord) item;
            refreshComplateContainer(ll_complete_container);

            int i = 0;
            for (SYCompleteCount complete : record.getCompleteCountList()) {
                View view = getComplateView(ll_complete_container);
                ll_complete_container.addView(view);
                CompleteViewHolder holder = (CompleteViewHolder) view.getTag();

                holder.line_lower.setVisibility(View.VISIBLE);
                holder.line_upper.setVisibility(View.VISIBLE);

                if (i == 0) {
                    holder.line_upper.setVisibility(View.INVISIBLE);
                }

                if (i == record.getCompleteCountList().size() - 1) {
                    holder.line_lower.setVisibility(View.INVISIBLE);
                }

                if (complete.getType() == 4 || complete.getType() == 8) {
                    holder.tv_content.setBackgroundResource(R.drawable.growth_compalte_bg_yellow);
                    holder.iv_point.setImageResource(R.mipmap.growth_complate_point_yellow);
                } else {
                    holder.tv_content.setBackgroundResource(R.drawable.growth_compalte_bg_red);
                    holder.iv_point.setImageResource(R.mipmap.growth_complate_point);
                }

                holder.tv_content.setCompoundDrawablesWithIntrinsicBounds(complete.gotIcon(), 0, 0, 0);
                holder.tv_content.setText(complete.gotTypeString());

                i++;
            }
        }
    }

    /**
     * 被加入想吃或精选
     */
    private class WantedJingHolder extends BaseCompleteHolder {
        private LinearLayout ll_complete_container;
        private ImageView iv_img;
        private TextView tv_content;
        private TextView tv_score;
        private TextView tv_num;


        public WantedJingHolder() {
            super(R.layout.item_growth_jing);
            iv_img = (ImageView) fl_content_container.findViewById(R.id.iv_img);
            tv_content = (TextView) fl_content_container.findViewById(R.id.tv_content);
            tv_score = (TextView) fl_content_container.findViewById(R.id.tv_score);
            tv_num = (TextView) fl_content_container.findViewById(R.id.tv_num);
        }

        SYWantEatGrowthRecord lastRecord;

        @Override
        public void onBind(int position, BaseGrowthHistoryItem item) {
            super.onBind(position, item);
            if (item instanceof SYWantEatGrowthRecord) {//被加入想吃
                final SYWantEatGrowthRecord record = (SYWantEatGrowthRecord) item;
                lastRecord = record;
                tv_content.setText("");
                tv_content.append(getAvatarSpann(null));
                tv_content.append("  ");
                tv_content.append(getColorSpan(record.getUser().getNickName()));
                tv_content.append("  将  ");
                tv_content.append(getColorSpan(record.getTitle()));
                tv_content.append("  加入了想吃清单");
                tv_score.setVisibility(View.GONE);

                FFImageLoader.loadAvatar(context(), record.getUser().getHeadImage().getUrl(), null, new FFImageCallBack() {
                    @Override
                    public void imageLoaded(Bitmap bitmap, String imageUrl) {
                        if (record != lastRecord) {
                            return;
                        }
                        tv_content.setText(" ");
                        tv_content.append(getAvatarSpann(null));
                        tv_content.append(getColorSpan(record.getUser().getNickName()));
                        tv_content.append("  将  ");
                        tv_content.append(getColorSpan(record.getTitle()));
                        tv_content.append("  加入了想吃清单");
                        tv_score.setVisibility(View.GONE);
                    }

                    @Override
                    public void onDownLoadProgress(int downloaded, int contentLength) {

                    }
                });
                FFImageLoader.loadSmallImage(context(), record.getCoverImage().getUrl(), iv_img);

            } else if (item instanceof SYGoodChoiceGrowthRecord) {//被设置为精选
                final SYGoodChoiceGrowthRecord record = (SYGoodChoiceGrowthRecord) item;
                tv_content.setText(getContent(record));
                if (record.getIntegral() > 0) {
                    tv_score.setVisibility(View.VISIBLE);
                    tv_score.setText(" 奖励积分+" + getSubFloat(record.getIntegral()));
                } else {
                    tv_score.setVisibility(View.GONE);
                }
                FFImageLoader.loadSmallImage(context(), record.getCoverImage().getUrl(), iv_img);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (record.getType() == 1) {
                            startActivity(FastDetailActivity.class, new FastDetailIntent(record.getFeedID()));
                        } else if (record.getType() == 2) {
                            startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(record.getFeedID()));
                        }
                    }
                });
            }

        }

        private CharSequence getContent(SYGoodChoiceGrowthRecord record) {
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append("你分享的");
            String source = "《" + record.getTitle() + "》";
            SpannableString ss = getColorSpan(source);
            ssb.append(ss);
            ssb.append("被设为精选内容");
            return ssb;
        }
    }

    /**
     * 动态公共
     */
    private class FeedHolder extends BaseCompleteHolder {
        protected TextView tv_rest_name;
        protected TextView tv_score;
        protected FrameLayout fl_images_container;


        public FeedHolder() {
            super(R.layout.item_growth_feed);
            tv_rest_name = (TextView) fl_content_container.findViewById(R.id.tv_rest_name);
            tv_score = (TextView) fl_content_container.findViewById(R.id.tv_score);
            fl_images_container = (FrameLayout) fl_content_container.findViewById(R.id.fl_images_container);
        }

        public String getSubFloat(double f) {
            DecimalFormat fnum = new DecimalFormat("##0.0");
            String string = fnum.format(f);
            return string.equals("-0") ? "0" : string;
        }

        @Override
        public void onBind(int position, BaseGrowthHistoryItem item) {
            super.onBind(position, item);
            SYFoodGrowthRecord record = (SYFoodGrowthRecord) item;

            if (record.isbShowCompleteEat()) {
                iv_complete.setVisibility(View.VISIBLE);
            } else {
                iv_complete.setVisibility(View.GONE);
            }

            if (!FFUtils.isStringEmpty(record.getContent())) {
                tv_rest_name.setText(record.getContent());
                tv_rest_name.setVisibility(View.VISIBLE);
            } else {
                tv_rest_name.setVisibility(View.GONE);
            }

            if (record.getIntegral() > 0) {
                tv_score.setVisibility(View.VISIBLE);
                tv_score.setText(" 奖励积分+" + getSubFloat(record.getIntegral()));
            } else {
                tv_score.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 速记
     */
    private class FastHolder extends FeedHolder {
        protected ImageView iv_1;
        protected ImageView iv_2;
        protected ImageView iv_3;
        protected LinearLayout ll_sum;
        protected FrameLayout fl_img3;
        protected TextView tv_sum;


        public FastHolder() {
            super();
            fl_images_container.addView(getLayoutInflater().inflate(R.layout.view_growth_fast_imgs, fl_content_container, false));
            iv_1 = (ImageView) fl_images_container.findViewById(R.id.iv_1);
            iv_2 = (ImageView) fl_images_container.findViewById(R.id.iv_2);
            iv_3 = (ImageView) fl_images_container.findViewById(R.id.iv_3);
            ll_sum = (LinearLayout) fl_images_container.findViewById(R.id.ll_sum);
            fl_img3 = (FrameLayout) fl_images_container.findViewById(R.id.fl_img3);
            tv_sum = (TextView) fl_images_container.findViewById(R.id.tv_sum);

        }

        @Override
        public void onBind(int position, BaseGrowthHistoryItem item) {
            super.onBind(position, item);
            final SYFoodGrowthRecord record = (SYFoodGrowthRecord) item;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(FastDetailActivity.class, new FastDetailIntent(record.getFeedID()));
                }
            });
            iv_1.getLayoutParams().width = imgHeight;
            iv_1.getLayoutParams().height = imgHeight;
            iv_2.getLayoutParams().width = imgHeight;
            iv_2.getLayoutParams().height = imgHeight;
            fl_img3.getLayoutParams().width = imgHeight;
            fl_img3.getLayoutParams().height = imgHeight;
            iv_1.setVisibility(View.INVISIBLE);
            iv_2.setVisibility(View.INVISIBLE);
            fl_img3.setVisibility(View.INVISIBLE);
            if (!FFUtils.isListEmpty(record.getCoverImages())) {
                iv_1.setVisibility(View.VISIBLE);
                FFImageLoader.loadSmallImage(context(), record.getCoverImages().get(0).getUrl(), iv_1);
                if (record.getCoverImages().size() > 1) {
                    FFImageLoader.loadSmallImage(context(), record.getCoverImages().get(1).getUrl(), iv_2);
                    iv_2.setVisibility(View.VISIBLE);
                }
                if (record.getCoverImages().size() > 2) {
                    FFImageLoader.loadSmallImage(context(), record.getCoverImages().get(2).getUrl(), iv_3);
                    fl_img3.setVisibility(View.VISIBLE);
                    if (record.getCoverImages().size() > 3) {
                        ll_sum.setVisibility(View.VISIBLE);
                        tv_sum.setText(record.getCoverImages().size() + "");
                    } else {
                        ll_sum.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    /**
     * 富文本
     */
    private class RichHolder extends FeedHolder {
        protected ImageView iv_1;
        protected TextView tv_sum;


        public RichHolder() {
            super();
            fl_images_container.addView(getLayoutInflater().inflate(R.layout.view_growth_rich_img, fl_content_container, false));
            iv_1 = (ImageView) fl_images_container.findViewById(R.id.iv_1);
            tv_sum = (TextView) fl_images_container.findViewById(R.id.tv_sum);
        }

        @Override
        public void onBind(int position, BaseGrowthHistoryItem item) {
            super.onBind(position, item);
            final SYFoodGrowthRecord record = (SYFoodGrowthRecord) item;
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(record.getFeedID()));
//                }
//            });
            iv_1.getLayoutParams().height = imgHeight;
            if (!FFUtils.isListEmpty(record.getCoverImages())) {
                FFImageLoader.loadBigImage(context(), record.getCoverImages().get(0).getUrl(), iv_1);

                if (record.getCoverImages().size() > 1) {
                    tv_sum.setVisibility(View.VISIBLE);
                    tv_sum.setText(record.getCoverImages().size() + "张");
                } else {
                    tv_sum.setVisibility(View.GONE);
                }
            } else {
                iv_1.setImageResource(R.drawable.alpha);
                tv_sum.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 其他
     */
    private class ElseHolder extends BaseHolder {
        private ImageView iv_img;
        private View ll_img;
        private LinearLayout linearLayout6;
        private TextView tv_1;
        private TextView tv_2;
        private TextView tv_3;
        private View else_bg;

        public ElseHolder() {
            super(R.layout.item_growth_else);
            iv_img = (ImageView) fl_content_container.findViewById(R.id.iv_img);
            ll_img = fl_content_container.findViewById(R.id.ll_img);
            linearLayout6 = (LinearLayout) fl_content_container.findViewById(R.id.linearLayout6);
            tv_1 = (TextView) fl_content_container.findViewById(R.id.tv_1);
            tv_2 = (TextView) fl_content_container.findViewById(R.id.tv_2);
            tv_3 = (TextView) fl_content_container.findViewById(R.id.tv_3);
            else_bg = fl_content_container.findViewById(R.id.else_bg);
        }


        @Override
        public void onBind(int position, BaseGrowthHistoryItem item) {
            super.onBind(position, item);
            tv_1.setVisibility(View.GONE);
            ll_img.setBackgroundColor(0);
            tv_3.setMaxLines(2);
            tv_2.setVisibility(View.VISIBLE);
            if (item instanceof SYFulfillGrowthRecord) {//美食分享被加入想吃清单或被践行历程对象
                final SYFulfillGrowthRecord record = (SYFulfillGrowthRecord) item;
                refreshWantEat(record);
                else_bg.setBackgroundResource(R.mipmap.growth_else_bg_red);
            } else if (item instanceof SYConcernedFansGrowthRecord) {//关注或粉丝历程
                SYConcernedFansGrowthRecord record = (SYConcernedFansGrowthRecord) item;
                initFans(record);
                else_bg.setBackgroundResource(R.mipmap.growth_else_bg_yellow);
            } else if (item instanceof SYCommentGrowthRecord) {//赞或评论的历程
                SYCommentGrowthRecord record = (SYCommentGrowthRecord) item;
                initCommentAndPrise(record);
                else_bg.setBackgroundResource(R.mipmap.growth_else_bg_yellow);
            }
        }

        private void initCommentAndPrise(SYCommentGrowthRecord record) {
            switch (record.getType()) {
                case 1:
                case 2:
                case 3:
                    iv_img.setImageResource(R.mipmap.growth_prise);
                    break;
                case 4:
                case 5:
                case 6:
                    iv_img.setImageResource(R.mipmap.growth_comment_complete);
                    break;
            }
            switch (record.getType()) {
                case 2:
                case 5:
                    if (record.getCoverImage() != null && !FFUtils.isStringEmpty(record.getCoverImage().getUrl())) {
                        FFImageLoader.loadSmallImage(context(), record.getCoverImage().getUrl(), iv_img);
                        ll_img.setBackgroundResource(R.drawable.growth_else_img_bg);
                    }
                    break;
            }
            switch (record.getType()) {//
                case 1://收到的赞总数
                    tv_2.setText("你收到的赞总数达到  " + record.getCount());
                    if (record.getUser() != null) {
                        tv_3.setVisibility(View.VISIBLE);
                        String lastString;
                        if (!TextUtils.isEmpty(record.getTitle())) {
                            lastString = "  " + record.getUser().getNickName() + "  赞了你分享的  《" + record.getTitle() + "》";
                        } else if (!TextUtils.isEmpty(record.getBuinessName())) {
                            lastString = "  " + record.getUser().getNickName() + "  赞了你分享的  " + record.getBuinessName();
                        } else {
                            lastString = "  " + record.getUser().getNickName() + "  赞了你的分享";
                        }
                        initAvatarSpan(tv_3, "", null, record.getUser().getHeadImage().getUrl(), lastString, record);
                    } else {
                        tv_3.setVisibility(View.GONE);
                    }
                    break;
                case 2://某条文章被赞总数
                    tv_2.setText("被赞总数达到  " + record.getCount());

                    if (!TextUtils.isEmpty(record.getTitle())) {
                        tv_1.setText(record.getTitle());
                        tv_1.setVisibility(View.VISIBLE);
                    } else if (!TextUtils.isEmpty(record.getBuinessName())) {
                        tv_1.setText(record.getBuinessName());
                        tv_1.setVisibility(View.VISIBLE);
                    }

                    initAvatarSpan(tv_3, "", null, record.getUser().getHeadImage().getUrl(), "  " + record.getUser().getNickName() + "  赞了你的分享", record);
                    break;
                case 3://自己点赞的总数
                    tv_2.setText("你点赞的总数达到  " + record.getCount());
                    if (record.getUser() != null) {
                        tv_3.setVisibility(View.VISIBLE);
                        String lastString;
                        if (!TextUtils.isEmpty(record.getTitle())) {
                            lastString = "  " + record.getUser().getNickName() + "  分享的  《" + record.getTitle() + "》";
                        } else if (!TextUtils.isEmpty(record.getBuinessName())) {
                            lastString = "  " + record.getUser().getNickName() + "  分享的  " + record.getBuinessName();
                        } else {
                            lastString = "  " + record.getUser().getNickName() + "  的分享";
                        }
                        initAvatarSpan(tv_3, "你赞了", null, record.getUser().getHeadImage().getUrl(), lastString, record);
                    } else {
                        tv_3.setVisibility(View.GONE);
                    }
                    break;
                case 4://收到的总评论数
                    tv_2.setText("你收到的评论总数达到" + record.getCount());
                    tv_2.setMaxLines(2);
                    if (record.getUser() != null) {
                        tv_3.setVisibility(View.VISIBLE);
                        String lastString;
                        if (!TextUtils.isEmpty(record.getTitle())) {
                            lastString = "  " + record.getUser().getNickName() + "  评论了你分享的  《" + record.getTitle() + "》" + ":  " + record.getCommentContent();
                        } else if (!TextUtils.isEmpty(record.getBuinessName())) {
                            lastString = "  " + record.getUser().getNickName() + "  评论了你分享的  " + record.getBuinessName() + ":  " + record.getCommentContent();
                        } else {
                            lastString = "  " + record.getUser().getNickName() + "  评论了你的分享" + ":  " + record.getCommentContent();
                        }
                        initAvatarSpan(tv_3, "", null, record.getUser().getHeadImage().getUrl(), lastString, record);
                    } else {
                        tv_3.setVisibility(View.GONE);
                    }
                    break;
                case 5://某条文字被评论总数
                    tv_2.setMaxLines(1);
                    if (!TextUtils.isEmpty(record.getTitle())) {
                        tv_1.setText(record.getTitle());
                        tv_1.setVisibility(View.VISIBLE);
                    } else if (!TextUtils.isEmpty(record.getBuinessName())) {
                        tv_1.setText(record.getBuinessName());
                        tv_1.setVisibility(View.VISIBLE);
                    }

                    initAvatarSpan(tv_3, "", null, record.getUser().getHeadImage().getUrl(), "  " + record.getUser().getNickName() + ":  " + record.getCommentContent(), record);
                    tv_2.setText("被评论总数达到" + record.getCount());
                    break;
                case 6://评论总数达到
                    tv_2.setMaxLines(1);
//                    if (!TextUtils.isEmpty(record.getTitle())) {
//                        tv_1.setText(record.getTitle());
//                        tv_1.setVisibility(View.VISIBLE);
//                    } else if (!TextUtils.isEmpty(record.getBuinessName())) {
//                        tv_1.setText(record.getBuinessName());
//                        tv_1.setVisibility(View.VISIBLE);
//                    }
                    String title = record.getTitle();
                    if (FFUtils.isStringEmpty(title)) {
                        title = record.getBuinessName();
                    } else {
                        title = "《" + title + "》";
                    }
                    if (FFUtils.isStringEmpty(title)) {
                        title = "  的分享";
                    } else {
                        title = "  分享的  " + title;
                    }

                    initAvatarSpan(tv_3, "你评论了  ", null, record.getUser().getHeadImage().getUrl(), "  " + record.getUser().getNickName() + title + ":  " + record.getCommentContent(), record);
                    tv_2.setText("你评论的总数达到" + record.getCount());
                    break;
            }

        }

        private void initFans(SYConcernedFansGrowthRecord record) {
            switch (record.getType()) {
                case 1://已获得XX粉丝
                    iv_img.setImageResource(R.mipmap.growth_was_attented);
                    tv_2.setText("已获得  " + record.getCount() + "位  粉丝");
                    if (record.getUser() != null) {
                        tv_3.setVisibility(View.VISIBLE);
                        initAvatarSpan(tv_3, "", null, record.getUser().getHeadImage().getUrl(),
                                "  " + record.getUser().getNickName() + "  关注了你,  TA是你的第" + record.getCount() + "位粉丝"
                                , record);
                    } else {
                        tv_3.setVisibility(View.GONE);
                    }
                    break;
                case 2://已关注XX位用户
                    iv_img.setImageResource(R.mipmap.growth_attention);
                    tv_2.setText("已关注  " + record.getCount() + "位  用户");
                    if (record.getUser() != null) {
                        tv_3.setVisibility(View.VISIBLE);
                        initAvatarSpan(tv_3, "你关注了", null, record.getUser().getHeadImage().getUrl(),
                                "  " + record.getUser().getNickName() + ",TA是你关注的第" + record.getCount() + "位用户"
                                , record);
                    } else {
                        tv_3.setVisibility(View.GONE);
                    }
                    break;
            }

        }

        private void refreshWantEat(SYFulfillGrowthRecord record) {
            switch (record.getType()) {
                case 3:
                case 4:
                    iv_img.setImageResource(R.mipmap.growth_acted);
                    break;
                case 1:
                case 2:
                case 5:
                case 6:
                    iv_img.setImageResource(R.mipmap.growth_add_complete);
                    break;
            }
            switch (record.getType()) {
                case 2:
                case 4:
                    if (record.getCoverImage() != null && !FFUtils.isStringEmpty(record.getCoverImage().getUrl())) {
                        FFImageLoader.loadSmallImage(context(), record.getCoverImage().getUrl(), iv_img);
                        ll_img.setBackgroundResource(R.drawable.growth_else_img_bg);
                    }
                    break;
            }
            switch (record.getType()) {
                case 1://所有的美食分享总共被XX次加入想吃清单
                    if (record.getUser() != null && !FFUtils.isStringEmpty(record.getUser().getNickName()) && !FFUtils.isStringEmpty(record.getBuinessName())) {
                        tv_2.setText("美食分享被第" + record.getCount() + "次加入想吃清单");
                        tv_3.setVisibility(View.VISIBLE);
                        initAvatarSpan(tv_3, "", null, record.getUser().getHeadImage().getUrl(),
                                "  " + record.getUser().getNickName() + "  将  " + record.getBuinessName() + "  加入了想吃清单"
                                , record);
                    } else {
                        tv_3.setVisibility(View.GONE);
                        tv_2.setText("被加入想吃清单总数达到" + record.getCount() + "");
                    }
                    break;
                case 2://某条美食被加入想吃清单的总数XX次
                    if (record.getUser() != null) {
                        if (!FFUtils.isStringEmpty(record.getTitle())) {
                            tv_1.setVisibility(View.VISIBLE);
                            tv_1.setText("《" + record.getTitle() + "》");
                        }
                        tv_2.setText("被加入想吃清单总数达到" + record.getCount());
                        if (!FFUtils.isStringEmpty(record.getUser().getNickName()) && !FFUtils.isStringEmpty(record.getBuinessName())) {
                            tv_3.setVisibility(View.VISIBLE);
                            initAvatarSpan(tv_3, "", null, record.getUser().getHeadImage().getUrl(),
                                    "  " + record.getUser().getNickName() + "  将  " + record.getBuinessName() + "  加入了想吃清单"
                                    , record);
                        } else {
                            tv_3.setVisibility(View.GONE);
                        }
                    } else {
                        tv_3.setVisibility(View.GONE);
                        tv_2.setText("被加入想吃清单总数达到" + record.getCount() + "");
                    }
                    break;
                case 3://所有的美食分享总共被XX次践行
                    tv_2.setText("美食分享被践行总数达到" + record.getCount());
                    if (record.getUser() != null && !FFUtils.isStringEmpty(record.getUser().getNickName()) && !FFUtils.isStringEmpty(record.getBuinessName())) {
                        tv_3.setVisibility(View.VISIBLE);
                        initAvatarSpan(tv_3, "", null, record.getUser().getHeadImage().getUrl(),
                                "  " + record.getUser().getNickName() + "  品尝了你分享的  " + record.getBuinessName()
                                , record);
                    } else {
                        tv_3.setVisibility(View.GONE);
                    }
                    break;
                case 4://某条美食被践行总数达到XX次
                    tv_2.setText("被践行总数达到" + record.getCount());

                    if (!FFUtils.isStringEmpty(record.getTitle())) {
                        tv_1.setVisibility(View.VISIBLE);
                        tv_1.setText("《" + record.getTitle() + "》");
                    }
                    if (record.getUser() != null && !FFUtils.isStringEmpty(record.getUser().getNickName()) && !FFUtils.isStringEmpty(record.getBuinessName())) {
                        tv_3.setVisibility(View.VISIBLE);
                        initAvatarSpan(tv_3, "", null, record.getUser().getHeadImage().getUrl(),
                                "  " + record.getUser().getNickName() + "  品尝了你分享的  " + record.getBuinessName()
                                , record);
                    } else {
                        tv_3.setVisibility(View.GONE);
                    }
                    break;
                case 5://我加入的想吃清单次数达到XX次
                    tv_1.setVisibility(View.GONE);
                    tv_2.setVisibility(View.GONE);
                    switch (record.getSourceType()) {
                        case 1://从达人荐加入想吃清单
                            tv_1.setVisibility(View.VISIBLE);
                            tv_1.setText("从  达人荐  ");
                            tv_1.append("加入想吃清单总数达到" + record.getCount());
                            break;
                        case 2://从动态加入想吃清单的总数
                            tv_1.setVisibility(View.VISIBLE);
                            tv_1.setText("从  动态  ");
                            tv_1.append("加入想吃清单总数达到" + record.getCount());
                            break;
                        case 3://其他地方 暂时未知
                            tv_2.setVisibility(View.VISIBLE);
                            tv_2.setText("加入想吃清单总数达到" + record.getCount());
                            break;
                    }
//                    tv_2.append("加入想吃清单总数达到" + record.getCount());
                    if (record.getUser() != null && !FFUtils.isStringEmpty(record.getUser().getNickName()) && !FFUtils.isStringEmpty(record.getBuinessName())) {
                        tv_3.setVisibility(View.VISIBLE);
                        initAvatarSpan(tv_3, "你将  ", null, record.getUser().getHeadImage().getUrl(),
                                "  " + record.getUser().getNickName() + "  分享的  " + record.getBuinessName() + "  加入了想吃清单"
                                , record);
                    } else {
                        tv_3.setVisibility(View.GONE);
                    }
                    break;
                case 6:
                    tv_1.setVisibility(View.GONE);
                    tv_2.setVisibility(View.GONE);
                    switch (record.getSourceType()) {
                        case 1://从达人荐加入想吃清单
                            tv_1.setVisibility(View.VISIBLE);
                            tv_1.setText("从  达人荐  ");
                            tv_1.append("加入想吃清单总数达到" + record.getCount());
                            break;
                        case 2://从动态加入想吃清单的总数
                            tv_1.setVisibility(View.VISIBLE);
                            tv_1.setText("从  动态  ");
                            tv_1.append("加入想吃清单总数达到" + record.getCount());
                            break;
                        case 3://其他地方 暂时未知
                            tv_2.setVisibility(View.VISIBLE);
                            tv_2.setText("加入想吃清单总数达到" + record.getCount());
                            break;
                    }
                    tv_3.setVisibility(View.GONE);
                    break;
            }
        }
    }

    public void initAvatarSpan(final TextView textView, final CharSequence frountString, Drawable bitmap, String url, final CharSequence lastString, final Object item) {
        textView.setText(frountString);
        textView.append(getAvatarSpann(bitmap));
        textView.append(lastString);
        textView.setTag(item);
        if (bitmap == null && url != null) {
            FFImageLoader.loadAvatar(context(), url, null, new FFImageCallBack() {
                @Override
                public void imageLoaded(final Bitmap bitmap, String imageUrl) {
                    new Thread() {
                        @Override
                        public void run() {
                            final Drawable drawable = getAvatarDrawable(bitmap);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (item != textView.getTag()) {
                                        return;
                                    }
                                    initAvatarSpan(textView, frountString, drawable, null, lastString, item);
                                }
                            });
                        }
                    }.start();

                }

                @Override
                public void onDownLoadProgress(int downloaded, int contentLength) {

                }
            });
        }
    }

    /**
     * 告诉个人中心页面，是否显示美食历程的小红点
     */
    private void sendGrowthRedDotBroadcast(String hasNew) {
        Intent intent = new Intent(MyActions.ACTION_PROFILE);
        intent.putExtra("type", "type_has_new_growth");
        intent.putExtra("has_new", hasNew);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public ImageView getTempIv() {
        ImageView tempIv = new ImageView(this);
        tempIv.setLayoutParams(new ViewGroup.LayoutParams(avatarBounds.bottom - avatarBounds.top, avatarBounds.right - avatarBounds.left));
        int padding = FFUtils.getPx(1);
        tempIv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        tempIv.setPadding(padding, padding, padding, padding);
        tempIv.setBackgroundResource(R.mipmap.growth_circle_avatar);
        return tempIv;
    }


    private Drawable getAvatarDrawable(Bitmap bitmap) {
        if (bitmap == null) {
            return avatar;
        }
        ImageView tempIv = getTempIv();
        tempIv.setImageBitmap(bitmap);
        Bitmap bmp = FFUtils.getBitmapViewByMeasure1(tempIv, tempIv.getLayoutParams().height, tempIv.getLayoutParams().width);
        BitmapDrawable drawable = new BitmapDrawable(bmp);
        drawable.setBounds(avatarBounds);
        return drawable;
    }


    private SpannableString getAvatarSpann(Drawable bitmap) {
        SpannableString spann = new SpannableString(" ");
        if (bitmap != null) {
            spann.setSpan(new VerticalImageSpan(bitmap), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spann.setSpan(new VerticalImageSpan(avatar), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spann;
    }


    @NonNull
    private SpannableString getColorSpan(String source) {
        SpannableString ss = new SpannableString(source);
        ss.setSpan(new ForegroundColorSpan(0xffffb400), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static class VerticalImageSpan extends ImageSpan {

        public VerticalImageSpan(Drawable drawable) {
            super(drawable);
        }

        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {
            Drawable drawable = getDrawable();
            Rect rect = drawable.getBounds();
            if (fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                //对于这里我表示,我不知道为啥是这样。不应该是fontHeight/2?但是只有fontHeight/4才能对齐
                //难道是因为TextView的draw的时候top和bottom是大于实际的？具体请看下图
                //所以fontHeight/4是去除偏差?
                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fontMetricsInt.ascent = -bottom;
                fontMetricsInt.top = -bottom;
                fontMetricsInt.bottom = top;
                fontMetricsInt.descent = top;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {
            Drawable drawable = getDrawable();
            canvas.save();
            int transY = 0;
            //获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
            transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }
    }

    ArrayList<View> complateViews = new ArrayList<>();

    private void refreshComplateContainer(LinearLayout container) {
        while (container.getChildCount() > 0) {
            complateViews.add(container.getChildAt(0));
            container.removeViewAt(0);
        }
    }

    class CompleteViewHolder {
        private ImageView iv_point;
        private TextView tv_content;
        private ImageView line_upper;
        private ImageView line_lower;

        public CompleteViewHolder(View view) {
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            line_upper = (ImageView) view.findViewById(R.id.line_upper);
            line_lower = (ImageView) view.findViewById(R.id.line_lower);
            iv_point = (ImageView) view.findViewById(R.id.iv_point);
        }

    }

    private View getComplateView(LinearLayout container) {
        if (complateViews.size() > 0) {
            return complateViews.remove(0);
        } else {
            View view = getLayoutInflater().inflate(R.layout.item_growth_complate, container, false);
            CompleteViewHolder holder = new CompleteViewHolder(view);
            view.setTag(holder);
            return view;
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 0: {
                    return new ElseHolder();
                }
                case 1: {//速记
                    return new FastHolder();
                }
                case 2: {
                    return new RichHolder();
                }
                case 3: {
                    return new WantedJingHolder();
                }
            }
            ImageView view = new ImageView(context());
            view.setImageResource(R.mipmap.foodhistory_header);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FFUtils.getDisWidth() * 379 / 750));
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return 4;
            }
            BaseGrowthHistoryItem item = getItem(position);
            if (item instanceof SYCommentGrowthRecord) {//赞或评论的历程对象
                return 0;
            } else if (item instanceof SYConcernedFansGrowthRecord) {//关注或粉丝历程对象
                return 0;
            } else if (item instanceof SYFoodGrowthRecord) {//富文本美食或速记美食历程
                SYFoodGrowthRecord record = (SYFoodGrowthRecord) item;
                if (record.getType() == 1) {//速记
                    return 1;
                }
                if (record.getType() == 2) {
                    return 2;
                }
                return 1;
            } else if (item instanceof SYFulfillGrowthRecord) {//美食分享被加入想吃清单或被践行历程
                return 0;
            } else if (item instanceof SYGoodChoiceGrowthRecord) {//被设置为精选的历程
                return 3;
            } else if (item instanceof SYWantEatGrowthRecord) {//想吃清单历程
                return 3;
            }
            return 0;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position == 0) {
                return;
            }
            ((BaseHolder) holder).onBind(position, getItem(position));
        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : (items.size() + 1);
        }

        private BaseGrowthHistoryItem getItem(int position) {
            return items.get(position - 1);
        }

    }

}
