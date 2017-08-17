package com.fengnian.smallyellowo.foodie.feeddetail;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fan.framework.base.XData;
import com.fan.framework.config.Tool;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.xtaskmanager.XTaskManager;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.CommentActivity;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.PublishedSuccessActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestInfoActivity;
import com.fengnian.smallyellowo.foodie.RichTextEditActivity;
import com.fengnian.smallyellowo.foodie.SwitchMubanActivity;
import com.fengnian.smallyellowo.foodie.View.UpDownListView;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.ActivityTags;
import com.fengnian.smallyellowo.foodie.appconfig.BrodcastActions;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.bean.publics.SYComment;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.results.AddCommentResult;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.DynamicDetailResult;
import com.fengnian.smallyellowo.foodie.contact.ShareUrlTools;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.BaseDetailAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.Brief2StyleAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.Brief2_2_StyleAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.BriefStyleAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.China2StyleAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.ChinaStyleAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.ModernStyleAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.PomoAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.detail.StandardAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.OnSnapBitmapCreatedListener;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentBrief2SnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentBrief2_2_SnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentBriefSnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentChina2_SnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentChinaSnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentModernSnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentPomoSnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentStandardSnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichCoverAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.SnapBaseAdapter;
import com.fengnian.smallyellowo.foodie.homepage.HomeChildSelectedFrag;
import com.fengnian.smallyellowo.foodie.intentdatas.CommentIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.PublishedSuccesskIndata;
import com.fengnian.smallyellowo.foodie.intentdatas.RichTextEditIntent;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.personal.MyActions;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModelManager;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.ProductImgUtils;
import com.fengnian.smallyellowo.foodie.widgets.DynamicReviewListView;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static com.fengnian.smallyellowo.foodie.appbase.SP.getUser;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2_2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Modern;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Pomo;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Standard;

/**
 * Created by Administrator on 2016-8-9.
 */
public class DynamicDetailActivity extends BaseActivity<DynamicDetailIntent> {

    public static final int INTENT_USER = 121;
    public static final int INTENT_SWITCH_MUBAN = 123;

    private DynamicDetailHelper mHelper;
    public ListView listView1;
    protected LinearLayout ll_dynamic_title;
    protected ImageView iv_back;
    protected ImageView iv_close;
    protected ImageView iv_close_actionbar;
    protected TextView tv_title;
    protected TextView tv_publish;
    public View s_status_bar;
    protected TextView textView;
    protected FrameLayout fl_action_container;
    protected View v_title_bottom;
    protected View btn_republish;
    public ImageView iv_menu_attent;
    public ImageView iv_switch_muban;
    protected ImageView iv_three_point;

    public void setData(SYFeed data) {
        boolean isNull = this.data == null;
        this.data = data;
        if (isNull) {
            initPage();
        }
    }

    public SYFeed data;
    public ActionViewHolderParent actionViewHolder1;
    private ActionViewHolderParent actionViewHolder2;
    public BaseDetailAdapter adapter;
    public static PublishModel task;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        task = null;
        if (shareReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(shareReceiver);
        }
    }

    private BrodcastActions.FeedChangeListener listener = new BrodcastActions.FeedChangeListener() {
        @Override
        public void publishFailed(PublishModel task) {

        }

        @Override
        public void publishSuccessed(PublishModel task, SYFeed feed) {
            if (task.getFeed().getId().equals(data.getId())) {
                data = feed;
            }
        }

        @Override
        public void startTask(PublishModel task) {
        }

        @Override
        public void foodDeleted(SYFeed task, boolean isUserCneter) {
        }

        @Override
        public void newPubSuccess(SYFeed feed) {

        }

    };
    private SYComment comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setEmojiEnable(true);
        super.onCreate(savedInstanceState);
        mHelper = new DynamicDetailHelper(this);
        setNotitle(true);
        initData();
    }

    private void initPage() {
        if (data == null) {
            return;
        }
        setContentView(getContentViewId());
        findViews();

        if (btn_republish != null) {
            btn_republish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //在某种情况下会报data.getFood() 为空 by chenglin 2017年7月25日21:08:09
                    if (data.getFood() != null && data.getFood() instanceof NativeRichTextFood) {
                        FFLogUtil.e("重新发布", data.getId() + "");
                        XTaskManager.taskManagerWithTask(((NativeRichTextFood) data.getFood()).getTask());
                        finish();
                    }
                }
            });
        }

        initActionViews();
        adapter = getAdapter(data.getReleaseTemplate().getReleaseTemplateType());
        if (data != null) {
            if (actionViewHolder1 != null) {
                actionViewHolder1.refresh();
                actionViewHolder2.refresh();
            }
            if (adapter.getHeadView() != null)
                adapter.getHeadView().refresh();
        }
        adapter.notifyDataSetChanged();
        listView1.setAdapter(adapter);
        s_status_bar.getLayoutParams().height = FFUtils.getStatusbarHight(this);
        if (getIntentData().isPreview()) {
            s_status_bar.setBackgroundColor(0xff000000);
            tv_publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    task = PublishModelManager.publish(context(), new FFNetWorkCallBack<com.fengnian.smallyellowo.foodie.bean.results.PublishResult>() {
                        @Override
                        public void onSuccess(com.fengnian.smallyellowo.foodie.bean.results.PublishResult response, FFExtraParams extra) {
                            PublishedSuccessActivity.model = (PublishModel) extra.getObj();
                            startActivity(PublishedSuccessActivity.class, new PublishedSuccesskIndata());
                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            return false;
                        }
                    }, SYDataManager.shareDataManager().draftsOfFirst(), false);
                }
            });
        }
        if (!getIntentData().isPreview() && !isNativeFail()) {
            if (listView1 instanceof UpDownListView) {
                UpDownListView upDownListView = (UpDownListView) listView1;
                upDownListView.setOnListViewUpDownListener(new UpDownListView.onListViewUpDownListener() {
                    @Override
                    public void onDownUp(boolean isUp) {
                        if (isUp) {//"上滑"
                            ll_dynamic_title.animate().setDuration(300).translationY(-ll_dynamic_title.getHeight());
                        } else {//"下滑"
                            ll_dynamic_title.animate().setDuration(300).translationY(0);
                        }

                    }
                });
            }

            listView1.setOnScrollListener(new AbsListView.OnScrollListener() {
                int scrollOffset = s_status_bar.getLayoutParams().height + FFUtils.getPx(48);

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem == 0 && view.getChildCount() > 0) {
                        if (data.getReleaseTemplate().isSupportScrool()) {
                            if (scrollOffset > Math.abs(view.getChildAt(0).getTop())) {
                                int alpha = 0xff * Math.abs(view.getChildAt(0).getTop()) / scrollOffset;
                                int color = alpha << 24 | 0x00ffffff;
                                ll_dynamic_title.setBackgroundColor(color);
                                s_status_bar.setBackgroundColor(0);
                                iv_back.setImageResource(R.mipmap.ff_ic_back_normal);
                                if (iv_close != null) {
                                    iv_close.setImageResource(R.mipmap.ic_close_three_level_page_white);
                                }
                                iv_close_actionbar.setImageResource(R.mipmap.ic_close_three_level_page_white);
                                tv_title.setTextColor(0xffffffff);
                                v_title_bottom.setVisibility(View.INVISIBLE);
                                iv_three_point.setImageResource(R.mipmap.ic_three_point_white);
                                iv_menu_attent.setVisibility(View.GONE);
//                                Log.v("tag_2","11111");
                            } else {//100 %
                                ll_dynamic_title.setBackgroundColor(0xffffffff);
                                s_status_bar.setBackgroundColor(0xff000000);
                                iv_back.setImageResource(R.mipmap.ff_ic_back_pressed);
                                if (iv_close != null) {
                                    iv_close.setImageResource(R.mipmap.ic_close_three_level_page_black);
                                }
                                iv_close_actionbar.setImageResource(R.mipmap.ic_close_three_level_page_black);
                                tv_title.setTextColor(0xff000000);

                                v_title_bottom.setVisibility(View.VISIBLE);
                                iv_three_point.setImageResource(R.mipmap.ic_three_point_black);
                                iv_menu_attent.setVisibility(View.GONE);
//                                Log.v("tag_2","22222");
                            }
                        }

                        if (adapter.getMAttentionBottomY() > Math.abs(view.getChildAt(0).getTop())) {
                            iv_menu_attent.setVisibility(View.GONE);
//                            Log.v("tag_2","33333");
                            tv_title.setText("详情");
                            if (adapter.getHeadView() != null && data.getUser() != null && !data.getUser().getId().equals(SP.getUid())) {
                                adapter.getHeadView().getTv_attention().setVisibility(View.VISIBLE);
                            }
                        } else {
//                            Log.v("tag_2","44444");
                            tv_title.setText(data.getUser().getNickName());
                            if (!(data.getUser().getId().equals(SP.getUid()))) {
                                iv_menu_attent.setVisibility(View.VISIBLE);
                            } else {
                                iv_menu_attent.setVisibility(View.GONE);
                            }
                            if (adapter.getHeadView() != null) {
                                adapter.getHeadView().getTv_attention().setVisibility(View.GONE);
                            }
                        }
                    } else {//100 %
                        ll_dynamic_title.setBackgroundColor(0xffffffff);
                        s_status_bar.setBackgroundColor(0xff000000);
                        iv_back.setImageResource(R.mipmap.ff_ic_back_pressed);
                        if (iv_close != null) {
                            iv_close.setImageResource(R.mipmap.ic_close_three_level_page_black);
                        }
                        iv_close_actionbar.setImageResource(R.mipmap.ic_close_three_level_page_black);
                        tv_title.setTextColor(0xff000000);
                        v_title_bottom.setVisibility(View.VISIBLE);
                        iv_three_point.setImageResource(R.mipmap.ic_three_point_black);
                        if (data != null && !(data.getUser().getId().equals(SP.getUid()))) {
//                            Log.v("tag_2","55555");
                            iv_menu_attent.setVisibility(View.VISIBLE);
                        } else {
                            iv_menu_attent.setVisibility(View.GONE);
                        }
                        if (adapter.getHeadView() != null) {
                            adapter.getHeadView().getTv_attention().setVisibility(View.GONE);
                        }
                    }
                    if (!getIntentData().isPreview()) {
                        int actionIndex = adapter.getActionIndex();
                        if (firstVisibleItem + visibleItemCount - 1 == actionIndex) {
                            int top = view.getChildAt(visibleItemCount - 1).getTop();
                            if (top > view.getHeight() - FFUtils.getPx(48)) {//列表中view 滑到固定view下面了
                                actionViewHolder2.getActionView().setVisibility(View.VISIBLE);
                            } else {//列表中view 滑到固定view上面了
                                actionViewHolder2.getActionView().setVisibility(View.GONE);
                            }
                        } else if (firstVisibleItem + visibleItemCount - 1 > actionIndex) {//
                            actionViewHolder2.getActionView().setVisibility(View.GONE);
                        } else {
                            actionViewHolder2.getActionView().setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } else if (isNativeFail()) {
            listView1.setOnScrollListener(new AbsListView.OnScrollListener() {
                int scrollOffset = s_status_bar.getLayoutParams().height + FFUtils.getPx(48);

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (!getIntentData().isPreview()) {
                        int actionIndex = data == null ? 1 : data.getFood().getRichTextLists().size() + 1;
                        if (firstVisibleItem + visibleItemCount - 1 == actionIndex) {
                            int top = view.getChildAt(visibleItemCount - 1).getTop();
                            if (top > view.getHeight() - FFUtils.getPx(48)) {//列表中view 滑到固定view下面了
                                actionViewHolder2.getActionView().setVisibility(View.VISIBLE);
                            } else {//列表中view 滑到固定view上面了
                                actionViewHolder2.getActionView().setVisibility(View.GONE);
                            }
                        } else if (firstVisibleItem + visibleItemCount - 1 > actionIndex) {//
                            actionViewHolder2.getActionView().setVisibility(View.GONE);
                        } else {
                            actionViewHolder2.getActionView().setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
        if (!getIntentData().isPreview()) {
            iv_three_point.setOnClickListener(new View.OnClickListener() {
                private String getText() {
                    StringBuilder title = new StringBuilder();
//                    if (data.getFood().getFrontCoverModel() != null && data.getFood().getFrontCoverModel().getFrontCoverContent() != null) {
//                        title = data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
//                    }
                    SYRichTextFood food = data.getFood();
                    SYPoi poi = food.getPoi();
                    if (poi != null && !FFUtils.isStringEmpty(poi.getTitle())) {
                        title.append(poi.getTitle());
                        title.append("|");
                    }
                    String foodTypeString = food.getFoodTypeString();
                    if (foodTypeString.length() != 0) {
                        title.append(foodTypeString);
                        title.append("|");
                    }

                    double totalPrice = food.getTotalPrice();
                    int numberOfPeople = food.getNumberOfPeople();
                    if (numberOfPeople != 0 && totalPrice != 0) {
                        title.append("￥" + FFUtils.getSubFloat(totalPrice) + "/" + numberOfPeople);
                        title.append("|");
                    }

                    if (poi != null && !FFUtils.isStringEmpty(poi.getAddress())) {
                        title.append(poi.getAddress());
                        title.append("|");
                    }

                    String result = title.toString();
                    if (result.endsWith("|")) {
                        result = result.substring(0, result.length() - 1);
                    }
                    return result;
                }

                @Override
                public void onClick(View view) {
                    if (data == null) {
                        return;
                    }
                    View contentView = LayoutInflater.from(context()).inflate(
                            R.layout.pop_share, null);
                    final PopupWindow popupWindow = new PopupWindow(contentView,
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

                    popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    // 设置按钮的点击事件
                    TextView tv_pyq = (TextView) contentView.findViewById(R.id.tv_pyq);
                    TextView tv_py = (TextView) contentView.findViewById(R.id.tv_py);
                    TextView tv_sina = (TextView) contentView.findViewById(R.id.tv_sina);

                    //封面快照
                    TextView tv_cover = (TextView) contentView.findViewById(R.id.tv_cover);
                    //内容快照
                    TextView product_tv_content = (TextView) contentView.findViewById(R.id.tv_content);


                    TextView tv_del = (TextView) contentView.findViewById(R.id.tv_del);
                    contentView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });
                    tv_pyq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.getFood() instanceof NativeRichTextFood) {
                                showToast("未保存成功的数据不能分享哦!");
                                return;
                            }

                            platform = "朋友圈";
                            registerShareReceiver();

                            final int id = showProgressDialog("");
                            FFImageLoader.load_base(DynamicDetailActivity.this, data.getFood().pullCoverImage(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                                @Override
                                public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                    dismissProgressDialog(id);
                                    popupWindow.dismiss();
                                    if (bitmap == null) {
                                        return;
                                    }
                                    WeixinOpen.getInstance().share2weixin_pyq(getUrl1(), getText(), getShareTitle(), bitmap);
                                }


                                @Override
                                public void onDownLoadProgress(int downloaded, int contentLength) {

                                }
                            });


                        }
                    });

                    tv_py.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.getFood() instanceof NativeRichTextFood) {
                                showToast("未保存成功的数据不能分享哦!");
                                return;
                            }

                            platform = "微信好友";
                            registerShareReceiver();

                            final int id = showProgressDialog("");
                            FFImageLoader.load_base(DynamicDetailActivity.this, data.getFood().pullCoverImage(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                                @Override
                                public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                    dismissProgressDialog(id);
                                    popupWindow.dismiss();
                                    if (bitmap == null) {
                                        return;
                                    }
                                    WeixinOpen.getInstance().share2weixin(getUrl1(), getText(), getShareTitle(), bitmap);
                                }

                                @Override
                                public void onDownLoadProgress(int downloaded, int contentLength) {

                                }
                            });
                        }
                    });

                    tv_sina.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.getFood() instanceof NativeRichTextFood) {
                                showToast("未保存成功的数据不能分享哦!");
                                return;
                            }

                            platform = "新浪微博";
                            registerShareReceiver();

                            final int id = showProgressDialog("");
                            FFImageLoader.load_base(DynamicDetailActivity.this, data.getFood().pullCoverImage(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                                @Override
                                public void imageLoaded(Bitmap bitmap, String imageUrl) {
                                    dismissProgressDialog(id);
                                    popupWindow.dismiss();
                                    if (bitmap == null) {
                                        return;
                                    }
//                                    SinaOpen.share(context(), bitmap, getShareTitle(), getText(), getUrl1());
                                    SinaOpen.shareUGC(context(), bitmap, getShareTitle(), getText(), getUrl1());
                                }

                                @Override
                                public void onDownLoadProgress(int downloaded, int contentLength) {
                                }
                            });
                        }
                    });

                    product_tv_content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            platform = "内容快照";
                            registerShareReceiver();

                            final int id = showProgressDialog("");
                            new Thread() {
                                @Override
                                public void run() {
                                    int templateType = data.getReleaseTemplate().getReleaseTemplateType();
                                    ProductImgUtils.startSnap(getSnapAdapter(templateType), new OnSnapBitmapCreatedListener() {
                                        @Override
                                        public void OnSnapBitmapCreatedListener(final String bitmap) {
                                            dismissProgressDialog(id);
                                            if (bitmap != null) {
                                                WeixinOpen.getInstance().share2weixin(context(), bitmap);
                                            } else {
                                                showToast("快照生成失败");
                                            }
                                        }
                                    });
                                }
                            }.start();

                        }
                    });

                    tv_cover.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.getFood() instanceof NativeRichTextFood) {
                                showToast("未保存成功的数据不能分享哦!");
                                return;
                            }

                            platform = "封面快照";
                            registerShareReceiver();

                            final int id = showProgressDialog("");
                            new Thread() {
                                @Override
                                public void run() {
                                    ProductImgUtils.startSnap(new RichCoverAdapter(data, DynamicDetailActivity.this, getUrl1()), new OnSnapBitmapCreatedListener() {
                                        @Override
                                        public void OnSnapBitmapCreatedListener(final String bitmap) {
                                            dismissProgressDialog(id);
                                            if (bitmap != null) {
                                                WeixinOpen.getInstance().share2weixin(context(), bitmap);
                                            } else {
                                                showToast("快照生成失败");
                                            }
                                        }
                                    });
                                }
                            }.start();

                        }
                    });


                    if (data.getUser().getId().equals(SP.getUid())) {
                        tv_del.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_share_del, 0, 0);
                        tv_del.setText("删除");
                        tv_del.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                if (PublishModelManager.isNative(data)) {
                                    if (!PublishModelManager.isPublishFail(data)) {
                                        showToast("发布中动态不可删除");
                                        return;
                                    }
                                    SYDataManager.shareDataManager().removeTask(((NativeRichTextFood) data.getFood()).getTask());
                                    finish();
                                    return;
                                }
                                EnsureDialog.showEnsureDialog(context(), true, "确定删除这条记录?", "删除", null, "取消", new EnsureDialog.EnsureDialogListener() {
                                    @Override
                                    public void onOk(DialogInterface dialog) {
                                        dialog.dismiss();
                                        del();
                                    }

                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        dialog.dismiss();

                                    }
                                });
                            }

                            private void del() {
                                if (!getIntentData().isMineMode()) {
//                                    post(Constants.shareConstants().getNetHeaderAdress() + "/food/removeFoodRecordByTypeV250.do", "", null, new FFNetWorkCallBack<BaseResult>() {
                                    post(IUrlUtils.Search.removeFoodRecordByTypeV250, "", null, new FFNetWorkCallBack<BaseResult>() {
                                        @Override
                                        public void onSuccess(BaseResult response, FFExtraParams extra) {
                                            BrodcastActions.foodDeleted(data, getIntentData().isMineMode());
                                            setResult(RESULT_OK);
                                            finish();
                                        }

                                        @Override
                                        public boolean onFail(FFExtraParams extra) {
                                            return false;
                                        }
                                    }, "recordId", data.getId(), "type", "2");
                                } else {
//                                    post(Constants.shareConstants().getNetHeaderAdress() + "/notes/removeFoodNoteV250.do", "", null, new FFNetWorkCallBack<BaseResult>() {
                                    post(IUrlUtils.Search.removeFoodNoteV250, "", null, new FFNetWorkCallBack<BaseResult>() {
                                        @Override
                                        public void onSuccess(BaseResult response, FFExtraParams extra) {
                                            BrodcastActions.foodDeleted(data, getIntentData().isMineMode());
                                            setResult(RESULT_OK);
                                            finish();
                                        }

                                        @Override
                                        public boolean onFail(FFExtraParams extra) {
                                            return false;
                                        }
                                    }, "cusId", data.getFoodNoteId(), "recordId", data.getFoodNoteId());
                                }
                            }
                        });
                    } else {
                        tv_del.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_share_jubao, 0, 0);
                        tv_del.setText("举报");
                        tv_del.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                setDialog();
                            }
                        });
                    }


                    popupWindow.setTouchable(true);

                    popupWindow.setTouchInterceptor(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            Log.i("mengdd", "onTouch : ");

                            return false;
                            // 这里如果返回true的话，touch事件将被拦截
                            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                        }
                    });

                    // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
                    // 我觉得这里是API的一个bug
                    popupWindow.setBackgroundDrawable(new ColorDrawable(0x45000000));
                    popupWindow.showAtLocation((View) getContainer().getParent(), Gravity.CENTER, 0, 0);
                }
            });
        }
    }


    /**
     * 根据模板类型获取adapter
     *
     * @param
     * @return
     */
    private BaseDetailAdapter getAdapter(int type) {
        switch (type) {
            case SYReleaseTemplateType_Standard:
                return new StandardAdapter(this);
            case SYReleaseTemplateType_Chinese:
                return new ChinaStyleAdapter(this);
            case SYReleaseTemplateType_Brief2:
                return new Brief2StyleAdapter(this);
            case SYReleaseTemplateType_Brief2_2:
                return new Brief2_2_StyleAdapter(this);
            case SYReleaseTemplateType_Brief:
                if (listView1 instanceof DynamicReviewListView) {
                    ((DynamicReviewListView) listView1).setIntercept(false);
                }
                return new BriefStyleAdapter(this);
            case SYReleaseTemplateType_Chinese2:
                return new China2StyleAdapter(this);
            case SYReleaseTemplateType_Pomo:
                return new PomoAdapter(this);
            case SYReleaseTemplateType_Modern:
                return new ModernStyleAdapter(this);
        }
        return new StandardAdapter(this);
    }

    private SnapBaseAdapter getSnapAdapter(int type) {
        switch (type) {
            case SYReleaseTemplateType_Standard:
                return new RichContentStandardSnapAdapter(this, data, getUrl1());
            case SYReleaseTemplateType_Chinese:
                return new RichContentChinaSnapAdapter(this, data, getUrl1());
            case SYReleaseTemplateType_Brief2:
                return new RichContentBrief2SnapAdapter(this, data, getUrl1());
            case SYReleaseTemplateType_Brief:
                return new RichContentBriefSnapAdapter(this, data, getUrl1());
            case SYReleaseTemplateType_Brief2_2:
                return new RichContentBrief2_2_SnapAdapter(this, data, getUrl1());
            case SYReleaseTemplateType_Pomo:
                return new RichContentPomoSnapAdapter(this, data, getUrl1());
            case SYReleaseTemplateType_Chinese2:
                return new RichContentChina2_SnapAdapter(this, data, getUrl1());
            case SYReleaseTemplateType_Modern:
                return new RichContentModernSnapAdapter(this, data, getUrl1());
        }
        return new RichContentStandardSnapAdapter(this, data, getUrl1());
    }

    private String getUrl1() {
        if (getIntentData().isMineMode()) {
            return ShareUrlTools.getRichNoteUrl(data);
        } else {
            return ShareUrlTools.getDynamicRichUrl(data);
        }
    }

    private String getShareTitle() {
        return data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
    }

    private void setDialog() {
        new EnsureDialog.Builder(DynamicDetailActivity.this)
//                        .setTitle("系统提示")//设置对话框标题

                .setMessage("确定要举报该内容为不良信息吗!")//设置显示的内容

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮

                    @Override

                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
//                        post(Constants.shareConstants().getNetHeaderAdress() + "/report.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                        post(IUrlUtils.Search.report, null, null, new FFNetWorkCallBack<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult response, FFExtraParams extra) {
//                                    setResult(RESULT_OK);
//                                    finish();
                                showToast("举报成功！");
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, "recordId", data.getId(), "defendant", data.getUser().getId());
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件

            }

        }).show();//在按键响应事件中显示此对话框
    }

    private void initData() {
        String detailUrl;
        String resource = "0";//此字段只有非计划性推荐进入详情页的时候传1 其他来源传0

        if (getIntentData().isMineMode()) {
            detailUrl = IUrlUtils.Search.getFoodNoteInfoV250;
        } else {
            // 9是非计划性推荐，只有非计划性推荐才会调用这个V320动态详情接口
            if (getIntentData().getResource().equals("9")) {
                detailUrl = IUrlUtils.Search.queryDynamicDetailV320;
                resource = "1";
            } else {
                detailUrl = IUrlUtils.Search.queryDynamicDetailV250;
            }
        }

        if (!getIntentData().isPreview()) {
            if (getIntentData().getId() != null) {
                //只有ID的网络数据
                setData(getIntentData().getFeed());

                post(detailUrl, "", null, new FFNetWorkCallBack<DynamicDetailResult>() {
                            @Override
                            public void onSuccess(DynamicDetailResult response, FFExtraParams extra) {
                                setData(response.getFeed());
                                adapter.getHeadView().refresh();
                                actionViewHolder1.refresh();
                                actionViewHolder2.refresh();
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, (getIntentData().isMineMode() ? "foodNoteId" : "recordId"), getIntentData().getId()
                        , "resource", resource);

            } else if (!(getIntentData().getFeed().getFood() instanceof NativeRichTextFood)) {//网络数据
                setData(getIntentData().getFeed());
                post(detailUrl, "", null, new FFNetWorkCallBack<DynamicDetailResult>() {
                            @Override
                            public void onSuccess(DynamicDetailResult response, FFExtraParams extra) {
                                setData(response.getFeed());
                                adapter.getHeadView().refresh();
                                actionViewHolder1.refresh();
                                actionViewHolder2.refresh();
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, (getIntentData().isMineMode() ? "foodNoteId" : "recordId"), getIntentData().isMineMode() ? getIntentData().getFeed().getFoodNoteId() : getIntentData().getFeed().getId()
                        , "resource", resource);
            } else {//本地数据
                SYFeed feed = getIntentData().getFeed();
                for (PublishModel task : SYDataManager.shareDataManager().allTasks()) {
                    if (task.getFeed().getFood().getId().equals(feed.getFood().getId())) {
                        setData(task.getFeed());
                    }
                }
                if (data == null) {
                    setData(feed);
                }

            }
        } else {
            DraftModel draftModel = SYDataManager.shareDataManager().draftsOfFirst();
            if (draftModel == null) {
                finish();
                return;
            }
            setData(draftModel.getFeed());
            data.setTimeStamp(System.currentTimeMillis());
        }
    }

    private void initActionViews() {
        if (getIntentData().isPreview()) {
            return;
        }
        if (getIntentData().isMineMode() || isNativeFail()) {
            actionViewHolder1 = new ActionViewHolder_mine();
            actionViewHolder2 = new ActionViewHolder_mine();
        } else {
            actionViewHolder1 = new ActionViewHolder_else();
            actionViewHolder2 = new ActionViewHolder_else();
        }
        actionViewHolder1.init();
        actionViewHolder2.init();
        actionViewHolder1.setOtherActionViewHolder(actionViewHolder2);
        actionViewHolder2.setOtherActionViewHolder(actionViewHolder1);
        fl_action_container.addView(actionViewHolder2.getActionView());
    }


    @Override
    public void finish() {
//        if (isFrom(MainActivity.class, HomeChildDynamicFrag.class) && !(getIntentData().getFeed().getFood() instanceof NativeRichTextFood))
//            setResult(RESULT_OK, new Intent().putExtra("feed", (Parcelable) data));
        super.finish();
    }


    public String getHeadImage() {
        if (getIntentData().isPreview()) {
            SYRichTextPhotoModel syRichTextPhotoModel = PublishModelManager.getHeadImage(data);
            if (syRichTextPhotoModel != null) {
                return syRichTextPhotoModel.getPhoto().getImageAsset().pullProcessedImageUrl();
            }
        }
        return data.getFood().pullHeadImage();
    }

    @Override
    public void refreshAfterLogin() {
        super.refreshAfterLogin();

        if (adapter != null && adapter.getHeadView() != null) {
            adapter.getHeadView().initAttention();
        }
    }

    public abstract class ActionViewHolderParent {
        private View actionView;
        private ActionViewHolderParent otherActionViewHolder;

        public void setActionView(View actionView) {
            this.actionView = actionView;
        }

        public View getActionView() {
            return actionView;
        }

        public void setOtherActionViewHolder(ActionViewHolderParent otherActionViewHolder) {
            this.otherActionViewHolder = otherActionViewHolder;
        }

        public View findViewById(int id) {
            return getActionView().findViewById(id);
        }

        public abstract void refresh();

        public abstract void init();

    }

    private class ActionViewHolder_mine extends ActionViewHolderParent {
        private LinearLayout ll_edit;
        private ImageView iv_edit;
        private TextView tv_edit;
        private LinearLayout ll_publish;
        private ImageView iv_publish;


        public void findViews() {
            ll_edit = (LinearLayout) findViewById(R.id.ll_edit);
            iv_edit = (ImageView) findViewById(R.id.iv_edit);
            tv_edit = (TextView) findViewById(R.id.tv_edit);
            ll_publish = (LinearLayout) findViewById(R.id.ll_publish);
            iv_publish = (ImageView) findViewById(R.id.iv_publish);
        }

        public void init() {
            setActionView(getLayoutInflater().inflate(R.layout.view_dynamic_detail_actions_mine, null));
            findViews();

            ll_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (data == null) {
                        return;
                    }

                    if (!FFUtils.checkNet()) {
                        showToast("似乎没网络，咱不能编辑.");
                        return;
                    }

                    if (DraftModelManager.hasDraft()) {
                        new EnsureDialog.Builder(context()).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(RichTextEditActivity.class, new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_CONTINUE));
                            }
                        }).setNeutralButton("创建新编辑", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                edit();
                            }
                        }).create().show();
                    } else {
                        edit();
                    }
                }
            });

            ll_publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (data == null) {
                        return;
                    }
                    if (data.getFood().getPoi() == null || FFUtils.isStringEmpty(data.getFood().getPoi().getTitle())) {
                        showToast("请编辑选择餐厅后再发布。");
                        return;
                    }
                    if (data.getFood() instanceof NativeRichTextFood) {
//                        if (PublishModelManager.isPublishFail(data)) {
//                            showToast("已发布");
//                            PublishModelManager.publish(data);
//                        }
                        showToast("本地数据不支持分享到动态，请先保存数据。");
                        LinkedList list = null;
                    } else {
//                        post(Constants.shareConstants().getNetHeaderAdress() + "/notes/publishToDynamicV250.do", "", null, new FFNetWorkCallBack<PublishResult>() {
                        post(IUrlUtils.Search.publishToDynamicV250, "", null, new FFNetWorkCallBack<PublishResult>() {
                            @Override
                            public void onSuccess(PublishResult response, FFExtraParams extra) {
                                if (response.judge()) {
                                    BrodcastActions.netDataPublishSuccess(response.getFeed());
                                    finishAllActivitysByTag(ActivityTags.main);
                                    MainActivity.toDynamic();
                                } else {
                                    showToast("发布到动态失败.");
                                }
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                showToast("发布到动态失败.");
                                return false;
                            }
                        }, "cusId", XData.uuid(), "recordId", data.getFoodNoteId());
                    }
                }
            });
        }


        public void refresh() {
            if (data == null) {
                return;
            }
        }
    }

    public static class PublishResult extends BaseResult {
        public SYFeed getFeed() {
            return feed;
        }

        public void setFeed(SYFeed feed) {
            this.feed = feed;
        }

        SYFeed feed;
    }


    private void edit() {
        final int id = showProgressDialog("", false);
        new Thread() {
            @Override
            public void run() {
                if (getDestroyed()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (id > 0)
                                dismissProgressDialog(id);
                        }
                    });
                    return;
                }
                if (PublishModelManager.isNative(data)) {//本地
                    if (data.getFood().wasMeishiBianji()) {//富文本
                        if (isNativeFail()) {//本地失败的富文本
                            //copy一份
                            PublishModel oldTask = ((NativeRichTextFood) data.getFood()).getTask();
                            String oldCustomId = oldTask.getFirstCustomId();
                            final DraftModel feed = DraftModelManager.edit(data);
                            feed.setFirstComtomId(oldCustomId);
                            //copy资源
                            SYDataManager.shareDataManager().removeAllDrafts();
                            SYDataManager.shareDataManager().addDraft(feed);
                            SYDataManager.shareDataManager().removeTask(oldTask);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (id > 0)
                                        dismissProgressDialog(id);
                                    startActivity(RichTextEditActivity.class, new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_CONTINUE));
                                    finish();
                                }
                            });
                        } else {//本地正在上传的
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (id > 0)
                                        dismissProgressDialog(id);
                                }
                            });
                        }
                    } else {//本地速记
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0)
                                    dismissProgressDialog(id);
                            }
                        });
                    }
                } else {//网络
                    if (data.getFood().wasMeishiBianji()) {//富文本
                        final DraftModel feed = DraftModelManager.edit(data);
                        SYDataManager.shareDataManager().removeAllDrafts();
                        SYDataManager.shareDataManager().addDraft(feed);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0)
                                    dismissProgressDialog(id);
                                startActivity(RichTextEditActivity.class, new RichTextEditIntent(RichTextEditIntent.TYPE_EDIT_CONTINUE));
                            }
                        });
                    } else {//网络速记
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (id > 0)
                                    dismissProgressDialog(id);
                            }
                        });
                    }
                }
            }
        }.start();
    }


    private class ActionViewHolder_else extends ActionViewHolderParent {
        private LinearLayout ll_want_eat;
        private ImageView iv_wanteat;
        private LinearLayout ll_prise;
        private ImageView iv_prise;
        private TextView tv_prise;
        private TextView tv_wanteat;
        private LinearLayout ll_comment;
        private ImageView iv_comment;


        public void init() {
            setActionView(getLayoutInflater().inflate(R.layout.view_dynamic_detail_actions, null));
            ll_want_eat = (LinearLayout) findViewById(R.id.ll_want_eat);
            iv_wanteat = (ImageView) findViewById(R.id.iv_wanteat);
            ll_prise = (LinearLayout) findViewById(R.id.ll_prise);
            iv_prise = (ImageView) findViewById(R.id.iv_prise);
            tv_prise = (TextView) findViewById(R.id.tv_prise);
            ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
            iv_comment = (ImageView) findViewById(R.id.iv_comment);
            tv_wanteat = (TextView) findViewById(R.id.tv_wanteat);

            ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FFUtils.isStringEmpty(SP.getUid())) {
                        LoginIntentData loginIntentData = new LoginIntentData();
                        loginIntentData.setCode("");
                        startActivity(LoginOneActivity.class, loginIntentData);
                        return;
                    }
                    if (data == null) {
                        return;
                    }
                    if (FFUtils.isStringEmpty(SP.getUid())) {
                        LoginIntentData loginIntentData = new LoginIntentData();
                        loginIntentData.setCode("");
                        startActivity(LoginOneActivity.class, loginIntentData);
                    } else {
                        startActivity(CommentActivity.class, (CommentIntent) new CommentIntent().setFeed(data).setRequestCode(517));
                    }
                }
            });

            ll_want_eat.setOnClickListener(new View.OnClickListener() {

                boolean isdoing = false;

                @Override
                public void onClick(View view) {
                    if (FFUtils.isStringEmpty(SP.getUid())) {
                        LoginIntentData loginIntentData = new LoginIntentData();
                        loginIntentData.setCode("");
                        startActivity(LoginOneActivity.class, loginIntentData);
                        return;
                    }
                    if (data == null) {
                        return;
                    }
                    if (data.getFood().getPoi() == null || TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {
                        return;
                    }
                    if (data.getEatState() == 1) {//想吃吃过
                        return;
                    }
                    if (data.getEatState() == 2) {
                        tv_wanteat.setTextColor(getResources().getColor(R.color.colorPrimary));
                        iv_wanteat.setImageResource(R.mipmap.dynamic_want_eat_selected);
                        showToast("已将该商户加入您的想吃清单中");
                        post(IUrlUtils.Search.addFoodEatV250, "", null, new FFNetWorkCallBack<BaseResult>() {
                                    @Override
                                    public void onSuccess(BaseResult response, FFExtraParams extra) {
                                        data.setEatState(0);
                                        actionViewHolder1.refresh();
                                        actionViewHolder2.refresh();
                                        adapter.notifyDataSetChanged();

                                        //事件统计
                                        HashMap<String, String> event = new HashMap<String, String>();
                                        event.put("account", SP.getUid());
                                        event.put("shop_id", data.getFood().getPoi().getId());
                                        event.put("shop_name", data.getFood().getPoi().getTitle());
                                        event.put("record_id", data.getId()); // 1:微信; 2:QQ; 3:微博; 4:手机号码; 5:验证码登录
                                        event.put("from", DynamicDetailActivity.class.getName());
                                        pushEventAction("Yellow_051", event);

                                        //9是非计划性推荐
                                        if ("9".equals(getIntentData().getResource())) {
                                            Intent intent = new Intent(MyActions.ACTION_HOME_RECOMMEND);
                                            LocalBroadcastManager.getInstance(context()).sendBroadcast(intent);
                                        }

                                    }

                                    @Override
                                    public boolean onFail(FFExtraParams extra) {
                                        return false;
                                    }
                                }, "recordId", data.getId(),
                                "id", data.getFood().getPoi().getId(),
//                                "isResource", isFrom(MainActivity.class, HomeChildSelectedFrag.class) ? "1" : "3",
                                "isResource", getIntentData().getResource(),
                                "shopImage", data.getFood().pullCoverImage(),
                                "shopType", data.getFood().getPoi().getType());
                    } else {
                        showToast("已将该商户从想吃清单移除");
//                        post(Constants.shareConstants().getNetHeaderAdress() + "/eat/delFoodEatV250.do", "", null, new FFNetWorkCallBack<BaseResult>() {
                        post(IUrlUtils.Search.delFoodEatV250, "", null, new FFNetWorkCallBack<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult response, FFExtraParams extra) {
                                data.setEatState(2);
                                actionViewHolder1.refresh();
                                actionViewHolder2.refresh();
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, "recordId", data.getId(), "merchantId", data.getFood().getPoi().getId(), "eatStatus", 0);
                    }
                }
            });

            ll_prise.setOnClickListener(new View.OnClickListener() {
                boolean isRequesting = false;

                @Override
                public void onClick(View view) {
                    if (FFUtils.isStringEmpty(SP.getUid())) {
                        LoginIntentData loginIntentData = new LoginIntentData();
                        loginIntentData.setCode("");
                        startActivity(LoginOneActivity.class, loginIntentData);
                        return;
                    }
                    if (data == null) {
                        return;
                    }
                    if (isRequesting) {
                        return;
                    }
                    isRequesting = true;
//                    post(Constants.shareConstants().getNetHeaderAdress() + "/praise/praiseOrUnpraiseV250.do", "", null, new FFNetWorkCallBack<BaseResult>() {
                    post(IUrlUtils.Search.praiseOrUnpraiseV250, "", null, new FFNetWorkCallBack<BaseResult>() {
                        @Override
                        public void onBack(FFExtraParams extra) {
                            super.onBack(extra);
                            isRequesting = false;
                        }

                        @Override
                        public void onSuccess(BaseResult response, FFExtraParams extra) {
                            //点赞
                            data.setbThumbsUp(!data.isbThumbsUp());
                            if (data.isbThumbsUp()) {
                                data.addThumbsUp(getUser());
                            } else {
                                data.removeThumbsUp(getUser());
                            }
                            if (data.isbThumbsUp()) {
                                data.setThumbsUpCount(data.getThumbsUpCount() + 1);
                            } else {
                                data.setThumbsUpCount(data.getThumbsUpCount() - 1);
                            }
                            actionViewHolder1.refresh();
                            actionViewHolder2.refresh();
//                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public boolean onFail(FFExtraParams extra) {
                            return true;
                        }
                    }, "foodRecordId", data.getId(), "flag", !data.isbThumbsUp() ? 1 : 2);
                }
            });
        }


        public void refresh() {
            if (data == null) {
                return;
            }
            if (data.isbThumbsUp()) {
                iv_prise.setImageResource(R.mipmap.dynamic_prise);
                tv_prise.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                iv_prise.setImageResource(R.mipmap.dynamic_prise_no);
                tv_prise.setTextColor(getResources().getColor(R.color.ff_text_black));
            }

            if (data.getFood().getPoi() != null && !TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {
                if (data.getEatState() == 1 || data.getUser().getId().equals(SP.getUid())) {
                    tv_wanteat.setText("吃过");
                    tv_wanteat.setTextColor(getResources().getColor(R.color.ff_text_gray));
                    iv_wanteat.setImageResource(R.mipmap.dynamic_eaten_normal);
                } else if (data.getEatState() == 0) {
                    tv_wanteat.setText("想吃");
                    tv_wanteat.setTextColor(getResources().getColor(R.color.colorPrimary));
                    iv_wanteat.setImageResource(R.mipmap.dynamic_want_eat_selected);
                } else {
                    tv_wanteat.setText("想吃");
                    tv_wanteat.setTextColor(getResources().getColor(R.color.ff_text_black));
                    iv_wanteat.setImageResource(R.mipmap.dynamic_want_eat);
                }
            } else {
                iv_wanteat.setImageResource(R.mipmap.dynamic_eaten_normal);
                tv_wanteat.setTextColor(getResources().getColor(R.color.ff_text_gray));
                tv_wanteat.setText("想吃");
                ((View) tv_wanteat.getParent()).setOnClickListener(null);
            }
        }
    }

    public void comment(SYComment comment) {
        if (FFUtils.isStringEmpty(SP.getUid())) {
            LoginIntentData loginIntentData = new LoginIntentData();
            loginIntentData.setCode("");
            startActivity(LoginOneActivity.class, loginIntentData);
            return;
        }
        this.comment = comment;
        CommentIntent intent = (CommentIntent) new CommentIntent();
        intent.setFeed(data);
        intent.setComment(comment);
        startActivity(CommentActivity.class, (CommentIntent) intent.setRequestCode(517));
    }


    public String getDishString() {
        if (data == null) {
            return "";
        }
        HashSet<String> dishs = new HashSet<>();
        for (SYRichTextPhotoModel rt : data.getFood().getRichTextLists()) {
            if (!FFUtils.isStringEmpty(rt.getDishesName())) {
                dishs.add(rt.getDishesName());
            }
        }
//        if (data.getFood().getDishesNameList() != null) {
//            for (String dishName : data.getFood().getDishesNameList()) {
//                if (!FFUtils.isStringEmpty(dishName)) {
//                    dishs.add(dishName);
//                }
//            }
//        }
        StringBuilder sb = new StringBuilder();
        for (String dishName : dishs) {
            sb.append(dishName);
            sb.append(",");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }


    private void findViews() {
        listView1 = (ListView) findViewById(R.id.listView1);
        ll_dynamic_title = (LinearLayout) findViewById(R.id.ll_dynamic_title);
        ll_dynamic_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_close = (ImageView) findViewById(R.id.iv_close);
        iv_close_actionbar = (ImageView) findViewById(R.id.iv_actionbar_close);
        iv_menu_attent = (ImageView) findViewById(R.id.iv_menu_attent);
        iv_three_point = (ImageView) findViewById(R.id.iv_three_point);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_publish = (TextView) findViewById(R.id.tv_publish);
        s_status_bar = findViewById(R.id.s_status_bar);
        fl_action_container = (FrameLayout) findViewById(R.id.fl_action_container);
        v_title_bottom = findViewById(R.id.v_title_bottom);
        btn_republish = findViewById(R.id.btn_republish);
        iv_switch_muban = (ImageView) findViewById(R.id.iv_switch_muban);
        if (iv_switch_muban != null) {
            iv_switch_muban.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(SwitchMubanActivity.class, new IntentData().setRequestCode(INTENT_SWITCH_MUBAN));
                    overridePendingTransition(R.anim.switch_muban_activity_in, 0);
                }
            });
        }
        initBottom();
    }

    protected int getContentViewId() {
        if (isNativeFail()) {
            return R.layout.activity_dynamic_fail;
        } else if (getIntentData().isPreview()) {
            return R.layout.activity_dynamic_review;
        } else if (!data.getReleaseTemplate().isSupportScrool()) {
            return R.layout.activity_dynamic_noscroll;
        }
        return R.layout.activity_dynamic_detail;
    }

    private boolean isNativeFail() {
        return data != null && data.getFood() instanceof NativeRichTextFood && ((NativeRichTextFood) data.getFood()).getTask() != null && ((NativeRichTextFood) data.getFood()).getTask().getTaskExecutState() == XTask.XTaskExecutStateFail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 517 && resultCode == RESULT_OK) {
            if (this.data.getUser() != null)
//                post(Constants.shareConstants().getNetHeaderAdress() + "/comment/addCommentV250.do", null, null, new FFNetWorkCallBack<AddCommentResult>() {
                post(IUrlUtils.Search.addCommentV250, null, null, new FFNetWorkCallBack<AddCommentResult>() {
                    @Override
                    public void onSuccess(AddCommentResult response, FFExtraParams extra) {
                        DynamicDetailActivity.this.data.setSecondLevelcomments(response.getSySecondLevelComments());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "ugcId", this.data.getId(), "content", data.getStringExtra("comment"), "toUserId", comment == null ? this.data.getUser().getId() : comment.getCommentUser().getId(), "toCommentId", comment == null ? 0 : comment.getId());
            comment = null;
        } else if (requestCode == DynamicDetailActivity.INTENT_USER && resultCode == RESULT_OK) {
            this.data.getUser().setByFollowMe(data.getBooleanExtra("is", false));
//            adapter.notifyDataSetChanged();
            adapter.getHeadView().refresh();
        } else if (requestCode == DynamicDetailActivity.INTENT_SWITCH_MUBAN && resultCode == RESULT_OK) {
            this.data.setReleaseTemplate(RichTextModelManager.getConfigByIndex(data.getIntExtra("indexCode", 1)));
            adapter = getAdapter(this.data.getReleaseTemplate().getReleaseTemplateType());
            if (adapter.getHeadView() != null)
                adapter.getHeadView().refresh();
            adapter.notifyDataSetChanged();
            listView1.setAdapter(adapter);
        }
    }

    public static void waitingDialog(Context context, String message) {
        if (waittingDialog == null)
            waittingDialog = new ProgressDialog(context);
        waittingDialog.setCanceledOnTouchOutside(false);//
        waittingDialog.setMessage(message);
        waittingDialog.show();
    }

    public static ProgressDialog waittingDialog;

    public static void DissDialog(Context context, ProgressDialog waittingDialog) {
        if (waittingDialog != null && waittingDialog.isShowing())
            waittingDialog.dismiss();
    }

//    Handler handler = new Handler();

//    @Override
//    protected void onResume() {
//        super.onResume();
//        adapter.notifyDataSetChanged();
//    }

    private ShareReceiver shareReceiver;

    public void registerShareReceiver() {
        if (shareReceiver != null) {
            return;
        }
        shareReceiver = new ShareReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(IUrlUtils.Constans.SHARE_MESSAGE_WECHAT);
        LocalBroadcastManager.getInstance(this).registerReceiver(shareReceiver, filter);
    }

    private String platform;

    public class ShareReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (IUrlUtils.Constans.SHARE_MESSAGE_WECHAT.equals(intent.getAction())) { // 微信登录回调

                HashMap<String, String> event = new HashMap<String, String>();
                event.put("account", SP.getUid());
                event.put("platform", platform);
                if (data != null) {
                    event.put("ugcid", data.getId());
                }
                if (data != null && data.getUser() != null) {
                    event.put("author_name", data.getUser().getNickName());
                    event.put("author_id", data.getUser().getId());
                }

                if (data != null && data.getFood() != null && data.getFood().getPoi() != null) {
                    event.put("shop_name", data.getFood().getPoi().getTitle());
                    event.put("shop_id", data.getFood().getPoi().getId());
                }
                pushEventAction("Yellow_071", event);
            }
        }
    }

}
