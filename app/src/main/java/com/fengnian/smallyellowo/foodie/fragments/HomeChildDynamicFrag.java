package com.fengnian.smallyellowo.foodie.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.http.FFNetWorkRequest;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.DynamicMessagesActivity;
import com.fengnian.smallyellowo.foodie.MainActivity;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.RestInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.CityPref;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.BrodcastActions;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYComment;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.bean.results.DynamicResult;
import com.fengnian.smallyellowo.foodie.bean.results.RemoveDynamicResult;
import com.fengnian.smallyellowo.foodie.bigpicture.BitPictureIntent;
import com.fengnian.smallyellowo.foodie.bigpicture.ChatBigPictureActivity;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.dialogs.PopRest;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.LoginIntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.login.LoginOneActivity;
import com.fengnian.smallyellowo.foodie.receivers.PushManager;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModelManager;
import com.fengnian.smallyellowo.foodie.utils.CommentUtil;
import com.fengnian.smallyellowo.foodie.utils.ContextUtils;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;
import com.fengnian.smallyellowo.foodie.utils.ReplaceViewHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.fengnian.smallyellowo.foodie.datamanager.SYDataManager.shareDataManager;

/**
 * @author
 * @version 1.0
 * @date 2016-7-19
 */
public class HomeChildDynamicFrag extends BaseFragment {
    private ReplaceViewHelper mReplaceViewHelper;
    private MyBaseAdapter<MainDynamicHolder, SYFeed> adapter;

    private final ArrayList<SYFeed> list_net = new ArrayList<>();
    private final ArrayList<SYFeed> list_native = new ArrayList<>();
    public ArrayList<String> menus = new ArrayList<>();

    public static final int mssjImageWidth = FFUtils.getDisWidth() - FFUtils.getPx(15 + 15);
    public static final int onlyImageHight = mssjImageWidth * 536 / 660;
    public static final int msbjImageHight = FFUtils.getDisWidth() * 536 / 660;
    public static final int nineImageHight = (FFUtils.getDisWidth() - FFUtils.getPx(15 + 4 + 4 + 15)) / 3;
    private PullToRefreshLayout prl;
    private LinearLayout ll_noticeContainer;
    private BrodcastActions.FeedChangeListener listener = new BrodcastActions.FeedChangeListener() {
        @Override
        public void publishFailed(PublishModel task) {
//                setNativeData();
            //TODO zhangfan 发布失败展示条目
            PushManager.refreshMessage();
        }

        @Override
        public void publishSuccessed(PublishModel task, SYFeed feed) {
            if (task.isBshareToSmallYellowO()) {
                list_net.add(feed);
                initNativeData();
                adapter.notifyDataSetChanged();
            }
            PushManager.refreshMessage();

        }

        @Override
        public void startTask(PublishModel task) {
            if (task.isBshareToSmallYellowO()) {
                list_native.add(task.getFeed());
                initNativeData();
                adapter.notifyDataSetChanged();
            }
        }

        private ArrayList<SYFeed> getAllNativeFeeds() {
            ArrayList<PublishModel> publishModes = SYDataManager.shareDataManager().allTasks();
            ArrayList<SYFeed> publishFeedList = new ArrayList<>();
            for (PublishModel publishModel : publishModes) {
                if (publishModel.isBshareToSmallYellowO()) {
                    publishFeedList.add(publishModel.getFeed());
                }
            }
            return publishFeedList;
        }

        @Override
        public void foodDeleted(SYFeed task, boolean isUserCneter) {
            if (isUserCneter) {
                //来源于个人中心
                if (task != null) {
                    if (task.getFood() instanceof NativeRichTextFood) {
                        //删除的本地数据，动态中对应的本地数据也需要删除
                        setNativeData(getAllNativeFeeds());
                        adapter.notifyDataSetChanged();
                    }
                }
            } else {
                //来源于非个人中心
                if (task != null) {
                    if (task.getFood() instanceof NativeRichTextFood) {
                        //删除的本地数据，动态中对应的本地数据也需要删除
                        setNativeData(getAllNativeFeeds());
                        adapter.notifyDataSetChanged();
                    } else {
                        remove(task, list_net);
                        syncAdapter();
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            PushManager.refreshMessage();
        }

        @Override
        public void newPubSuccess(SYFeed feed) {
            list_net.add(feed);
            syncAdapter();
            adapter.notifyDataSetChanged();
        }

        private void remove(SYFeed task, ArrayList<SYFeed> list) {
            PushManager.refreshMessage();
            SYFeed f = null;
            for (SYFeed feed : list) {
                if (task.getId().equals(feed.getId())) {
                    f = feed;
                }
            }
            if (f != null) {
                list.remove(f);
            }
        }

    };

    private void setNativeData(ArrayList<SYFeed> list) {
        list_native.clear();
        addNativeData(list);
    }

    private void addNativeData(ArrayList<SYFeed> list) {
        list_native.addAll(list);
        syncAdapter();
    }

    private void setNetData(ArrayList<SYFeed> list) {
        list_net.clear();
        addNetData(list);
    }

    private void addNetData(ArrayList<SYFeed> list) {
        if (list != null) {
            list_net.addAll(list);
        }
        syncAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReplaceViewHelper = new ReplaceViewHelper(getActivity());
    }

    @Override
    protected void refreshAfterLogin() {
        super.refreshAfterLogin();
        if (!isLoading) {
            if (lv_dynamic != null)
            lv_dynamic.smoothScrollToPosition(0);
            prl.autoRefresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initNativeData();
        PushManager.refreshMessage();
        adapter.notifyDataSetChanged();
    }

    public void setAutoRefresh(){
        lv_dynamic.setSelection(0);
        prl.autoRefresh();
    }

    private void syncAdapter() {
        ArrayList<SYFeed> listTemp = new ArrayList<SYFeed>();
        listTemp.addAll(list_native);
        listTemp.addAll(list_net);
        sortData(listTemp);
        adapter.setData(listTemp);
    }

    private void sortData(ArrayList<SYFeed> listTemp) {
        Collections.sort(listTemp, new Comparator<SYFeed>() {
            @Override
            public int compare(SYFeed syFeed, SYFeed t1) {
                if (syFeed.getTimeStamp() > t1.getTimeStamp()) {
                    return -1;
                }
                if (syFeed.getTimeStamp() < t1.getTimeStamp()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    private void resetList() {
        list_net.clear();
        list_native.clear();
    }

    boolean isLoading = false;

    public void onTitleClicked(boolean showProgressDialog) {
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!isLoading) {
                    toTop();
                    prl.autoRefresh();
                }
            }
        }, 500);
    }

    public void toTop() {
        if (lv_dynamic != null)
            lv_dynamic.smoothScrollToPosition(0);
    }

    private View newMessageView;
    private ArrayList<View> failView = new ArrayList<>();

    public void setMessageNum(int messageNum) {
        if (ll_noticeContainer == null) {
            return;
        }
//        messageNum-=SYDataManager.shareDataManager().getAllFailDynamic().size();
        List<PublishModel> list = shareDataManager().getAllFailDynamic();
        if (messageNum == 0 && list.size() == 0) {
            if (newMessageView != null && ll_noticeContainer.getChildCount() != 0 && ll_noticeContainer.getChildAt(ll_noticeContainer.getChildCount() - 1) == newMessageView) {
                ll_noticeContainer.removeViewAt(ll_noticeContainer.getChildCount() - 1);
            }
            ll_noticeContainer.setPadding(0, 0, 0, 0);
        } else {
            ll_noticeContainer.setPadding(0, 0, 0, FFUtils.getPx(8));
        }

        if (messageNum != 0) {
            if (newMessageView == null) {
                newMessageView = activity.getLayoutInflater().inflate(R.layout.item_new_message_dynamic, ll_noticeContainer, false);
                newMessageView.setTag(newMessageView.findViewById(R.id.tv_num));
                newMessageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!FFUtils.checkNet()){
                            APP.app.showToast("网络连接失败，请检查网络设置",null);
                            return;
                        }
                        startActivity(DynamicMessagesActivity.class, new IntentData());
                    }
                });
            }

            int count = ll_noticeContainer.getChildCount();
            if (count == 0 || (count > 0 && ll_noticeContainer.getChildAt(0) != newMessageView)){
                ll_noticeContainer.removeView(newMessageView);
                //不知为什么，我明明移除了此view ,但是在某种未知情况下还是报错说has parent。所以再加一层保险：
                if (newMessageView.getParent() != null && newMessageView.getParent() instanceof ViewGroup){
                    ViewGroup viewGroup = (ViewGroup) newMessageView.getParent();
                    viewGroup.removeView(newMessageView);
                }
                ll_noticeContainer.addView(newMessageView);
            }
            ((TextView) newMessageView.getTag()).setText(String.valueOf(messageNum));
        }


        int index = 0;
        while (ll_noticeContainer.getChildCount() > index) {
            if (ll_noticeContainer.getChildAt(0) != newMessageView) {
                failView.add(ll_noticeContainer.getChildAt(0));
                ll_noticeContainer.removeViewAt(0);
            } else {
                index++;
            }
        }

        for (PublishModel pm : list) {
            View view;
            if (failView.size() == 0) {
                view = activity.getLayoutInflater().inflate(R.layout.view_publishfail, ll_noticeContainer, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!FFUtils.checkNet()){
                            APP.app.showToast("网络连接失败，请检查网络设置",null);
                            return;
                        }
                        PublishModel pm = (PublishModel) view.getTag();
                        DynamicDetailIntent data = new DynamicDetailIntent(pm.getFeed(), false, false);
                        startActivity(DynamicDetailActivity.class, data);
                    }
                });
            } else {
                view = failView.remove(0);
            }

            view.setTag(pm);

            ll_noticeContainer.addView(view, 0);
        }
    }


    public static class MainDynamicHolder {
        ImageView iv_avatar, iv_add_crown;
        TextView tv_name;
        //        TextView tv_eat_num;
        TextView tv_level;
        RatingBar rb_level;
        View v_line_level;
        TextView tv_title;
        TextView tv_content;
        TextView tv_vis_all_content;
        TextView tv_rest_name;
        TextView tv_time;
        TextView tv_del;
        TextView tv_comment;
        TextView tv_prise;
        TextView tv_eaten;
        LinearLayout ll_dynamic_comment_container;
        LinearLayout tv_dynamic_commentContent;
        LinearLayout ll_nine_pic_container;
        View iv_jing;
        ImageView iv_dynamic_1;
        ImageView iv_dynamic_2;
        ImageView iv_dynamic_3;
        ImageView iv_dynamic_4;
        ImageView iv_dynamic_5;
        ImageView iv_dynamic_6;
        ImageView iv_dynamic_7;
        ImageView iv_dynamic_8;
        FrameLayout fl_dynamic_lastImage_container;
        ImageView iv_dynamic_9;
        TextView tv_dynamic_moreImage;
        ImageView iv_dynamic_msbj_only;
        ImageView iv_dynamic_mssj_only;
        final ImageView[] ivs = new ImageView[8];//前八张图片  第九张特殊处理
    }

    private ListView lv_dynamic;

    public HomeChildDynamicFrag() {
        super(null);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        BrodcastActions.removeListtener(listener);
    }

    @Override
    public void onFindView() {
        BrodcastActions.addListener(listener);
        lv_dynamic = (ListView) findViewById(R.id.lv_dynamic);
        ll_noticeContainer = (LinearLayout) findViewById(R.id.ll_noticeContainer);
        adapter = new MyBaseAdapter<MainDynamicHolder, SYFeed>(MainDynamicHolder.class, activity, R.layout.item_main_dynamic) {
            private View.OnClickListener listener = new View.OnClickListener() {

                @Override
                public void onClick(final View view) {
                    switch (view.getId()) {
                        case R.id.tv_comment: {
                            if(!FFUtils.checkNet()){
                                showToast("网络连接失败，请检查网络设置");
                                return;
                            }
                            SYFeed feed = ((SYFeed) view.getTag());
                            visCommentInput(feed, null, new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        break;
                        case R.id.tv_vis_all_content: {
                            SYFeed feed = ((SYFeed) view.getTag());
                            feed.setExpaned(!feed.isExpaned());
                            notifyDataSetChanged();
                        }
                        case R.id.tv_rest_name: {
                            final SYFeed feed = ((SYFeed) view.getTag());
                            if (feed.getFood().getPoi().getIsCustom() == 1) {
                                showToast("该商户为自定义创建，暂无地址信息");
                                return;
                            }
//                            post(Constants.shareConstants().getNetHeaderAdress() + "/shop/queryShopDrawerInfoV250.do", "", null, new FFNetWorkCallBack<MainHomeUGCFragment.RestInfoDrawerResult>() {
                            post(IUrlUtils.Search.queryShopDrawerInfoV250, "", null, new FFNetWorkCallBack<MainHomeUGCFragment.RestInfoDrawerResult>() {
                                @Override
                                public void onSuccess(MainHomeUGCFragment.RestInfoDrawerResult response, FFExtraParams extra) {
                                    new PopRest(getActivity(), HomeChildDynamicFrag.this, response.getBuinessDetail().getMerchantPhone(),
                                            response.getBuinessDetail().getMerchantAddress(), feed.getFood().getPoi().getTitle(), response.getBuinessDetail().getMerchantPoi().getLatitude(),
                                            response.getBuinessDetail().getMerchantPoi().getLongitude(), feed.getFood().getPoi().getId()).showAtLocation((View) getBaseActivity().getContainer().getParent(),
                                            Gravity.CENTER, 0, 0);
                                }

                                @Override
                                public boolean onFail(FFExtraParams extra) {
                                    return false;
                                }
                            }, "merchantId", feed.getFood().getPoi().getId());
                        }
                        break;
                        case R.id.tv_del: {
                            EnsureDialog.showEnsureDialog(activity, true, "是否要删除？", "确定",null, "取消", new EnsureDialog.EnsureDialogListener() {
                                @Override
                                public void onOk(DialogInterface dialog) {
                                    dialog.dismiss();
                                    SYFeed feed = ((SYFeed) view.getTag());
                                    if (list_native.contains(feed)) {
                                        list_native.remove(feed);
                                        shareDataManager().removeTask(PublishModelManager.getTask(feed));
                                        BrodcastActions.foodDeleted(feed, false);
                                    } else {
                                        list_net.remove(feed);
                                        BrodcastActions.foodDeleted(feed, false);
//                                        post(Constants.shareConstants().getNetHeaderAdress() + "/food/removeFoodRecordByTypeV250.do", null, null, new FFNetWorkCallBack<RemoveDynamicResult>() {
                                        post(IUrlUtils.Search.removeFoodRecordByTypeV250, null, null, new FFNetWorkCallBack<RemoveDynamicResult>() {
                                            @Override
                                            public void onSuccess(RemoveDynamicResult response, FFExtraParams extra) {

                                            }

                                            @Override
                                            public boolean onFail(FFExtraParams extra) {
                                                return false;
                                            }
                                            //TODO zhangfan type int 1:ugc 2:pugc
                                        }, "recordId", feed.getId(), "type", feed.getFood().wasMeishiBianji() ? 2 : 1);
                                    }
                                    syncAdapter();
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        break;
                    }
                }

            };


            private View.OnLongClickListener longListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            };

            @Override
            public void initView(View convertView, final MainDynamicHolder holder, int position, final SYFeed item) {
                holder.tv_comment.setTag(item);
                holder.tv_eaten.setTag(item);
                holder.tv_prise.setTag(item);
                holder.tv_del.setTag(item);
                holder.tv_rest_name.setTag(item);
                holder.tv_vis_all_content.setTag(item);
                holder.tv_content.setTag(item);
                holder.tv_dynamic_moreImage.setTag(item);
                //头像
                if (item.getUser() != null) {
                    IsAddCrownUtils.checkIsAddCrow(item.getUser(), holder.iv_add_crown);
                    FFImageLoader.loadAvatar(getBaseActivity(), item.getUser().getHeadImage().getUrl(), holder.iv_avatar);
                    //名字
                    holder.tv_name.setText(item.getUser().getNickName());
                    holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!FFUtils.checkNet()){
                                APP.app.showToast("网络连接失败，请检查网络设置",null);
                                return;
                            }
                            if (!item.getUser().getId().equals(SP.getUid())) {
                                UserInfoIntent intent = new UserInfoIntent();
                                intent.setUser(item.getUser());
//                                startActivity(UserInfoActivity.class, intent);
                                IsAddCrownUtils.FragmentActivtyStartAct(item.getUser(), intent, activity);
                            } else {
//                                ((MainActivity) activity).rb_user.setChecked(true);
                            }
                        }
                    });
                }
                if (item.getStarLevel() == 0) {
                    holder.tv_level.setVisibility(View.INVISIBLE);
                    holder.rb_level.setVisibility(View.INVISIBLE);
                    holder.v_line_level.setVisibility(View.INVISIBLE);
                } else {
                    holder.rb_level.setRating(item.getStarLevel());
                    holder.tv_level.setText(item.pullStartLevelString());
                    holder.tv_level.setVisibility(View.VISIBLE);
                    holder.rb_level.setVisibility(View.VISIBLE);
                    holder.v_line_level.setVisibility(View.VISIBLE);
                }
                //内容
                if (!item.isExpaned() && FFUtils.getTextLine(holder.tv_content, item.getFood().getContent(), FFUtils.getDisWidth() - FFUtils.getPx(30)) > 5) {
                    holder.tv_vis_all_content.setVisibility(View.VISIBLE);
                    holder.tv_content.setMaxLines(5);
                } else {
                    holder.tv_vis_all_content.setVisibility(View.GONE);
                    if (item.isExpaned()) {
                        holder.tv_content.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        holder.tv_content.setMaxLines(5);
                    }
                }
                FFUtils.setText(holder.tv_content, item.getFood().getContent());
                holder.tv_time.setText(ContextUtils.getFriendlyTime(item.getTimeStamp(), false));
                //图片
                initPics(holder, item, getBaseActivity());
                //餐厅名称
                String rest_name = item.getFood() == null || item.getFood().getPoi() == null ? null : item.getFood().getPoi().getTitle();
                FFUtils.setText(holder.tv_rest_name, rest_name);

                //是否可以删除
                if (SP.getUid().equals(item.getUser().getId())) {
                    holder.tv_del.setVisibility(View.VISIBLE);
                } else {
                    holder.tv_del.setVisibility(View.GONE);
                }
                String title = item.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
                if (title == null) {
                    try {
                        title = item.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
                    } catch (Exception e) {

                    }
                }

                boolean bo = title != null && title.matches("[0-9]{8}");
                if (bo) {
                    int a = Integer.parseInt(title);
                    bo = a < 21000000 && a > 19700000;
                }
                if (FFUtils.isStringEmpty(title) || bo) {
                    holder.tv_title.setVisibility(View.GONE);
                } else {
                    holder.tv_title.setVisibility(View.VISIBLE);
                    holder.tv_title.setText(title);
                }


                if (item.getFood().getPoi() != null && !TextUtils.isEmpty(item.getFood().getPoi().getTitle())) {
                    if (item.getEatState() == 1 || item.getUser().getId().equals(SP.getUid())) {
                        holder.tv_eaten.setText("吃过");
                        holder.tv_eaten.setTextColor(getResources().getColor(R.color.ff_text_gray));
                        holder.tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_eaten_normal, 0, 0, 0);
                        holder.tv_eaten.setOnClickListener(null);
                    } else if (item.getEatState() == 0) {
                        holder.tv_eaten.setText("想吃");
                        holder.tv_eaten.setTextColor(getResources().getColor(R.color.colorPrimary));
                        holder.tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_want_eat_selected, 0, 0, 0);
                    } else if (item.getEatState() == 2) {
                        holder.tv_eaten.setText("想吃");
                        holder.tv_eaten.setTextColor(getResources().getColor(R.color.ff_text_black));
                        holder.tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_want_eat, 0, 0, 0);
                    }

                    holder.tv_eaten.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!FFUtils.checkNet()){
                                showToast("网络连接失败，请检查网络设置");
                                return;
                            }
                            if (FFUtils.isStringEmpty(SP.getUid())) {
                                LoginIntentData loginIntentData = new LoginIntentData();
                                loginIntentData.setCode("");
                                startActivity(LoginOneActivity.class, loginIntentData);
                                return;
                            }
                            if (item.getEatState() == 0) {
                                item.setEatState(2);
                            } else if (item.getEatState() == 1) {
                                return;
                            } else if (item.getEatState() == 2) {
                                item.setEatState(0);
                            }
                            if (item.getEatState() == 0) {
                                showToast("已将该商户加入您的想吃清单中");
                                holder.tv_eaten.setTextColor(getResources().getColor(R.color.colorPrimary));
                                holder.tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_want_eat_selected, 0, 0, 0);
                                post(IUrlUtils.Search.addFoodEatV250, null, null, new FFNetWorkCallBack<BaseResult>() {
                                            @Override
                                            public void onSuccess(BaseResult response, FFExtraParams extra) {
                                                HashMap<String, String> event = new HashMap<String, String>();
                                                event.put("account", SP.getUid());
                                                event.put("shop_id", item.getFood().getPoi().getId());
                                                event.put("shop_name", item.getFood().getPoi().getTitle());
                                                event.put("record_id", item.getId()); // 1:微信; 2:QQ; 3:微博; 4:手机号码; 5:验证码登录
                                                event.put("from", HomeChildDynamicFrag.class.getName());
                                                getBaseActivity().pushEventAction("Yellow_051", event);
                                            }

                                            @Override
                                            public boolean onFail(FFExtraParams extra) {
                                                return true;
                                            }
                                        }, "recordId", item.getId(),
                                        "id", item.getFood().getPoi().getId(),
                                        "isResource", "2",
                                        "shopImage", item.getFood().pullCoverImage(),
                                        "shopType", item.getFood().getPoi().getType());
                            } else {
                                holder.tv_eaten.setTextColor(getResources().getColor(R.color.ff_text_black));
                                holder.tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_want_eat, 0, 0, 0);
                                showToast("已将该商户从想吃清单移除");
//                                post(Constants.shareConstants().getNetHeaderAdress() + "/eat/delFoodEatV250.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                                post(IUrlUtils.Search.delFoodEatV250, null, null, new FFNetWorkCallBack<BaseResult>() {
                                    @Override
                                    public void onSuccess(BaseResult response, FFExtraParams extra) {

                                    }

                                    @Override
                                    public boolean onFail(FFExtraParams extra) {
                                        return true;
                                    }
                                }, "recordId", item.getId(), "merchantId", item.getFood().getPoi().getId(), "eatStatus", 0);
                            }
                        }
                    });
                } else {
                    holder.tv_eaten.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_eaten_normal, 0, 0, 0);
                    holder.tv_eaten.setTextColor(getResources().getColor(R.color.ff_text_gray));
                    holder.tv_eaten.setText("想吃");
                    holder.tv_eaten.setOnClickListener(null);
                }
                if (item.getThumbsUpCount() == 0) {
                    holder.tv_prise.setText("赞");
                } else {
                    holder.tv_prise.setText(item.getThumbsUpCount() + "");
                }
                holder.tv_prise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!FFUtils.checkNet()){
                            showToast("网络连接失败，请检查网络设置");
                            return;
                        }
                        if (FFUtils.isStringEmpty(SP.getUid())) {
                            LoginIntentData loginIntentData = new LoginIntentData();
                            loginIntentData.setCode("");
                            startActivity(LoginOneActivity.class, loginIntentData);
                            return;
                        }
                        item.setbThumbsUp(!item.isbThumbsUp());
                        //是否赞过
                        if (item.isbThumbsUp()) {
                            holder.tv_prise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_prise, 0, 0, 0);
                            item.addThumbsUp(SP.getUser());
                            item.setThumbsUpCount(item.getThumbsUpCount() + 1);
                        } else {
                            holder.tv_prise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_prise_no, 0, 0, 0);
                            item.removeThumbsUp(SP.getUser());
                            item.setThumbsUpCount(item.getThumbsUpCount() - 1);
                        }
                        if (item.getThumbsUpCount() == 0) {
                            holder.tv_prise.setText("赞");
                        } else {
                            holder.tv_prise.setText(item.getThumbsUpCount() + "");
                        }
//                        post(Constants.shareConstants().getNetHeaderAdress() + "/praise/praiseOrUnpraiseV250.do", null, null, new FFNetWorkCallBack<BaseResult>() {
                        post(IUrlUtils.Search.praiseOrUnpraiseV250, null, null, new FFNetWorkCallBack<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult response, FFExtraParams extra) {

                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, "foodRecordId", item.getId(), "flag", item.isbThumbsUp() ? 1 : 2);
                    }
                });

                //是否赞过
                if (item.isbThumbsUp()) {
                    holder.tv_prise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_prise, 0, 0, 0);
                } else {
                    holder.tv_prise.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.dynamic_prise_no, 0, 0, 0);
                }

                //评论内容
                initComments(holder, item);

            }

            @Override
            public void onGetView(MainDynamicHolder holder) {
                holder.tv_comment.setOnClickListener(listener);
                holder.tv_eaten.setOnClickListener(listener);
                holder.tv_prise.setOnClickListener(listener);
                holder.tv_del.setOnClickListener(listener);
                holder.tv_rest_name.setOnClickListener(listener);
                holder.tv_vis_all_content.setOnClickListener(listener);
                holder.tv_content.setOnLongClickListener(longListener);
                holder.tv_dynamic_moreImage.setOnClickListener(listener);
                holder.tv_comment.setMovementMethod(new LinkTouchMovementMethod());

                holder.ivs[0] = holder.iv_dynamic_1;
                holder.ivs[1] = holder.iv_dynamic_2;
                holder.ivs[2] = holder.iv_dynamic_3;
                holder.ivs[3] = holder.iv_dynamic_4;
                holder.ivs[4] = holder.iv_dynamic_5;
                holder.ivs[5] = holder.iv_dynamic_6;
                holder.ivs[6] = holder.iv_dynamic_7;
                holder.ivs[7] = holder.iv_dynamic_8;

                holder.iv_dynamic_mssj_only.getLayoutParams().height = onlyImageHight;
                holder.iv_dynamic_msbj_only.getLayoutParams().height = msbjImageHight;
                holder.iv_dynamic_mssj_only.getLayoutParams().width = mssjImageWidth;
                holder.iv_dynamic_msbj_only.getLayoutParams().width = FFUtils.getDisWidth();
                for (ImageView iv : holder.ivs) {
                    iv.getLayoutParams().height = nineImageHight;
                    iv.getLayoutParams().width = nineImageHight;
                }
                holder.fl_dynamic_lastImage_container.getLayoutParams().height = nineImageHight;
                holder.fl_dynamic_lastImage_container.getLayoutParams().width = nineImageHight;
            }
        };
        lv_dynamic.setAdapter(adapter);

        lv_dynamic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SYFeed item = adapter.getItem(position);
                if (item.getFood().wasMeishiBianji()) {
                    if (PublishModelManager.isNative(item) && item.getFood() instanceof NativeRichTextFood && ((NativeRichTextFood) item.getFood()).getTask().getTaskExecutState() == XTask.XTaskExecutStateExecing) {
                        return;
                    }
                    if(!FFUtils.checkNet()){
                        showToast("网络连接失败，请检查网络设置");
                        return;
                    }
                    DynamicDetailIntent intent = new DynamicDetailIntent(item, false, false);
                    intent.setRequestCode(123);
                    startActivity(DynamicDetailActivity.class, intent);
                }
            }
        });

        prl = PullToRefreshLayout.supportPull(lv_dynamic, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                refresh(true);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                refresh(false);
            }
        });
        FFUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prl.autoRefresh();
            }
        }, 500);

        prl.setDoPullUp(true);
        prl.setDoPullDown(true);

        initNativeData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
//            SYFeed feed = data.getParcelableExtra("feed");
//            if (feed == null) {
//                return;
//            }
//            for (int i = 0; i < list_net.size(); i++) {
//                if (list_net.get(i).getFood().getId().equals(feed.getFood().getId())) {
//                    list_net.remove(i);
//                    list_net.add(i, feed);
//                    break;
//                }
//            }
//            List<SYFeed> list_all = adapter.getData();
//            for (int i = 0; i < list_all.size(); i++) {
//                if (list_all.get(i).getFood().getId().equals(feed.getFood().getId())) {
//                    list_all.remove(i);
//                    list_all.add(i, feed);
//                    break;
//                }
//            }
            adapter.notifyDataSetChanged();
        }
    }

    private void initNativeData() {
        ArrayList<PublishModel> list = shareDataManager().allTasks();
        ArrayList<SYFeed> list1 = new ArrayList<>();
        for (PublishModel pm : list) {
            if (!pm.isbCanceled() && pm.isBshareToSmallYellowO())
                list1.add(pm.getFeed());
        }
        setNativeData(list1);
    }

    private void visCommentInput(SYFeed feed, SYComment comment, Runnable runn) {
        ((MainActivity) getActivity()).comment(feed, comment, runn);
    }

    private void initComments(final MainDynamicHolder holder, final SYFeed data) {
        if (data.getSecondLevelcomments() == null || data.getSecondLevelcomments().size() == 0) {
            holder.ll_dynamic_comment_container.setVisibility(View.GONE);
            return;
        } else {
            holder.ll_dynamic_comment_container.setVisibility(View.VISIBLE);
        }

        LinearLayout tv = holder.tv_dynamic_commentContent;
        final Runnable runn = new Runnable() {
            @Override
            public void run() {
                initComments(holder, data);
            }
        };
        CommentUtil.dooComment(data, tv, runn, context(), this);
    }

    private static void addImageMap(final ArrayList<BitPictureIntent.ImageMap> list,String dishName, String url, ImageView iv, final int position, final FFContext context) {
        BitPictureIntent.ImageMap map = new BitPictureIntent.ImageMap();
        map.setPath(url);
        map.setDishName(dishName);
        list.add(map);
        if (iv != null)
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!FFUtils.checkNet()){
                        APP.showToast("网络连接失败，请检查网络设置",null);
                        return;
                    }
                    BitPictureIntent intent = new BitPictureIntent();
                    intent.setImages(list);
                    intent.setIndex(position);
                    ((BaseActivity) context).startActivity(ChatBigPictureActivity.class, intent);
                }
            });
    }


    public static void initPics(MainDynamicHolder holder, SYFeed item, final FFContext context) {
        final ArrayList<BitPictureIntent.ImageMap> list = new ArrayList<>();
        if (item.isHandPick()) {
            holder.iv_jing.setVisibility(View.VISIBLE);
        } else {
            holder.iv_jing.setVisibility(View.GONE);
        }
        if (item.getFood().wasMeishiBianji()) {//美食编辑
            holder.iv_dynamic_msbj_only.setVisibility(View.VISIBLE);
            holder.ll_nine_pic_container.setVisibility(View.GONE);
            holder.iv_dynamic_mssj_only.setVisibility(View.GONE);
            String imgUrl = item.getFood().pullCoverImage();
            FFImageLoader.loadBigImage(context, imgUrl, holder.iv_dynamic_msbj_only);
        } else {//美食速记
            int i = 0;
            int j = 0;
            SYFoodPhotoModel photo1 = null;
            String dishName = null;
            for (; (i < 9) && (j < item.getFood().getRichTextLists().size()); j++) {
                SYFoodPhotoModel photo = item.getFood().getRichTextLists().get(j).getPhoto();
                if (photo != null) {
                    i++;
                    dishName = item.getFood().getRichTextLists().get(j).getDishesName();
                    photo1 = photo;
                } else {
                    continue;
                }
            }
            if (i == 1) {//只有一张图片
                holder.iv_dynamic_msbj_only.setVisibility(View.GONE);
                holder.ll_nine_pic_container.setVisibility(View.GONE);
                holder.iv_dynamic_mssj_only.setVisibility(View.VISIBLE);
                FFImageLoader.loadBigImage(context, photo1.getImageAsset().pullProcessedImageUrl(), holder.iv_dynamic_mssj_only);
                addImageMap(list,dishName, photo1.getImageAsset().pullProcessedImageUrl(), holder.iv_dynamic_mssj_only, 0, context);
            } else {
                j = 0;
                holder.iv_dynamic_mssj_only.setVisibility(View.GONE);
                holder.iv_dynamic_msbj_only.setVisibility(View.GONE);
                holder.ll_nine_pic_container.setVisibility(View.VISIBLE);
                for (ImageView iv : holder.ivs) {
                    iv.setVisibility(View.GONE);
                }
                holder.fl_dynamic_lastImage_container.setVisibility(View.GONE);
                holder.tv_dynamic_moreImage.setVisibility(View.GONE);

                for (i = 0; (i < 10) && (j < item.getFood().getRichTextLists().size()); j++) {
                    SYRichTextPhotoModel syRichTextPhotoModel = item.getFood().getRichTextLists().get(j);
                    final SYFoodPhotoModel photo = syRichTextPhotoModel.getPhoto();
                    if (item.getFood().getRichTextLists().get(j).getPhoto() != null) {
                        i++;
                        if (i == 9) {
                            holder.fl_dynamic_lastImage_container.setVisibility(View.VISIBLE);
                            FFImageLoader.loadMiddleImage(context, photo1.getImageAsset().pullProcessedImageUrl(), holder.iv_dynamic_9);
                            addImageMap(list,syRichTextPhotoModel.getDishesName(), photo.getImageAsset().pullProcessedImageUrl(), holder.iv_dynamic_9, i - 1, context);
                        } else if (i >= 10) {
                            holder.tv_dynamic_moreImage.setVisibility(View.VISIBLE);
                            addImageMap(list,syRichTextPhotoModel.getDishesName(), photo.getImageAsset().pullProcessedImageUrl(), null, i - 1, context);
                            holder.tv_dynamic_moreImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(!FFUtils.checkNet()){
                                        APP.app.showToast("网络连接失败，请检查网络设置",null);
                                        return;
                                    }
                                    BitPictureIntent intent = new BitPictureIntent();
                                    intent.setImages(list);
                                    intent.setIndex(8);
                                        ((BaseActivity) context).startActivity(ChatBigPictureActivity.class, intent);
                                }
                            });
                        } else {
                            holder.ivs[i - 1].setVisibility(View.VISIBLE);
                            FFImageLoader.loadMiddleImage(context, item.getFood().getRichTextLists().get(j).getPhoto().getImageAsset().pullProcessedImageUrl(), holder.ivs[i - 1]);
                            addImageMap(list,syRichTextPhotoModel.getDishesName(), photo.getImageAsset().pullProcessedImageUrl(), holder.ivs[i - 1], i - 1, context);
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }


    private class LinkTouchMovementMethod extends LinkMovementMethod {
        private TouchableSpan mPressedSpan;

        @Override
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mPressedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(true);
                    Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                            spannable.getSpanEnd(mPressedSpan));
                }
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan.setPressed(false);
                    mPressedSpan = null;
                    Selection.removeSelection(spannable);
                }
            } else {
                if (mPressedSpan != null) {
                    mPressedSpan.setPressed(false);
                    super.onTouchEvent(textView, spannable, event);
                }
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
            return true;
        }

        private TouchableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();

            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
            TouchableSpan touchedSpan = null;
            if (link.length > 0) {
                touchedSpan = link[0];
            }
            return touchedSpan;
        }

    }

    public static abstract class TouchableSpan extends ClickableSpan {
        private boolean mIsPressed;

        public void setPressed(boolean isSelected) {
            mIsPressed = isSelected;
        }

        public boolean isPressed() {
            return mIsPressed;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_dynamic, container, false);
    }

    @Override
    public void onPageInitFail(FFNetWorkRequest request) {
        super.onPageInitFail(request);

    }

    @Override
    public void onPageInitNoData(FFNetWorkRequest request) {
        super.onPageInitNoData(request);
    }

    @Override
    public void onPageInitNoNet(FFNetWorkRequest request) {
        super.onPageInitNoNet(request);

    }

    @Override
    public void onPageInitHasData(FFNetWorkRequest request) {
        super.onPageInitHasData(request);
    }

    String lastRecordId;

    public void refresh(final boolean isInit) {
        refresh(isInit, false);
    }

    public void refresh(final boolean isInit, boolean isShowProgressDialog) {
        if (!FFUtils.checkNet()){
            mReplaceViewHelper.toReplaceView(prl,R.layout.ff_nonet_layout);
            mReplaceViewHelper.getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refresh(true,true);
                }
            });
            return;
        }

        if (isInit) {
            lastRecordId = "";
        }
        ArrayList<Object> list = new ArrayList<>();
        list.add("pageSize");
        list.add("10");
        if (!FFUtils.isStringEmpty(lastRecordId)) {
            list.add("lastRecordId");
            list.add(lastRecordId);
        }
//        post(Constants.shareConstants().getNetHeaderAdress() + "/dynamic/queryDynamicFeedsV250.do", isShowProgressDialog ? "" : null, new FFExtraParams().setInitPage(isInit && adapter.getCount() == 0), new FFNetWorkCallBack<DynamicResult>() {
        post(IUrlUtils.Search.queryDynamicFeedsV250, isShowProgressDialog ? "" : null, new FFExtraParams(), new FFNetWorkCallBack<DynamicResult>() {
            @Override
            public void onSuccess(DynamicResult response, FFExtraParams extra) {
                mReplaceViewHelper.removeView();
                if (isInit) {
//                    resetList();
                    PushManager.onMessageClick();
                    setNetData(response.getFeeds());
                } else {
                    addNetData(response.getFeeds());
                }
                prl.refreshFinish(prl.SUCCEED);
                prl.loadmoreFinish(prl.SUCCEED);
                if (response.getFeeds() == null || response.getFeeds().size() < 10) {
                    adapter.setLoadMore(true);
                    adapter.setHasMore(false);
                    prl.setDoPullUp(false);
                } else {
                    prl.setDoPullUp(true);
                    adapter.setLoadMore(false);
                    adapter.setHasMore(false);
                }
                if (!FFUtils.isListEmpty(response.getFeeds())) {
                    lastRecordId = response.getFeeds().get(response.getFeeds().size() - 1).getId();
                }
                adapter.notifyDataSetChanged();

                if (adapter.getCount() <= 0) {
                    setEmptyView();
                }
            }
            @Override
            public boolean onFail(FFExtraParams extra) {
                prl.refreshFinish(prl.FAIL);
                prl.loadmoreFinish(prl.FAIL);
                return false;
            }
        }, list.toArray());
    }

    private void setEmptyView(){
        mReplaceViewHelper.toReplaceView(prl, R.layout.ff_empty_layout);
        TextView textView = (TextView) mReplaceViewHelper.getView().findViewById(R.id.text);
        textView.setText(R.string.empty_data_text);
        mReplaceViewHelper.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(true,true);
            }
        });
    }

    @Override
    public void onPageInitRetry(FFNetWorkRequest request) {
//        super.onPageInitRetry(request);
        prl.autoRefresh();
    }
}