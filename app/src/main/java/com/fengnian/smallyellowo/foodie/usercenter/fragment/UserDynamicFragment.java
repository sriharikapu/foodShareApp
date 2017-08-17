package com.fengnian.smallyellowo.foodie.usercenter.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.UserDynamicActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseFragment;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.BrodcastActions;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.results.UserDynamicResult;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.PopRest;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.FastDetailActivity;
import com.fengnian.smallyellowo.foodie.fragments.HomeChildDynamicFrag;
import com.fengnian.smallyellowo.foodie.fragments.MainHomeUGCFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastDetailIntent;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModelManager;
import com.fengnian.smallyellowo.foodie.utils.ContextUtils;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/6/9.
 */

public class UserDynamicFragment extends BaseFragment {

    @Bind(R.id.listView)
    public ListView listView;
    private MyBaseAdapter<Holder, SYFeed> adapter;

    private final ArrayList<SYFeed> list_net = new ArrayList<>();
    private final ArrayList<SYFeed> list_native = new ArrayList<>();

    private int flag = 1;

    private String userId;

    private BrodcastActions.FeedChangeListener listener = new BrodcastActions.FeedChangeListener() {
        @Override
        public void publishFailed(PublishModel task) {
//                setNativeData();
            //TODO zhangfan 发布失败展示条目
        }

        @Override
        public void publishSuccessed(PublishModel task, SYFeed feed) {
            if (task.isBshareToSmallYellowO()) {
                list_net.add(feed);
                initNativeData();
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void startTask(PublishModel task) {
            if (task.isBshareToSmallYellowO()) {
                list_native.add(task.getFeed());
                initNativeData();
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void foodDeleted(SYFeed task, boolean isUserCneter) {
            if (!isUserCneter) {
                remove(task, list_native);
                remove(task, list_net);
                syncAdapter();
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void newPubSuccess(SYFeed feed) {

        }

        private void remove(SYFeed task, ArrayList<SYFeed> list) {
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
    private PullToRefreshLayout prl;

    @Override
    public void onFindView() {

    }

    @Override
    public View getView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_dynamic, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userId = getArguments().getString("user_id");
        prl = PullToRefreshLayout.supportPull(listView, new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                getData();
            }
        });
        prl.setDoPullUp(true);

        getData();

        adapter = new MyBaseAdapter<Holder, SYFeed>(Holder.class, context(), R.layout.item_user_dynamic) {
            private View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.tv_comment: {
                            SYFeed feed = ((SYFeed) view.getTag());
                            visCommentInput(feed);
                        }
                        break;
                        case R.id.tv_vis_all_content: {
                            SYFeed feed = ((SYFeed) view.getTag());
                            feed.setExpaned(!feed.isExpaned());
                            notifyDataSetChanged();
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
            public void initView(View convertView, Holder holder, int position, final SYFeed item) {
                if (item.isHandPick()) {
                    holder.iv_jing.setVisibility(View.VISIBLE);
                } else {
                    holder.iv_jing.setVisibility(View.GONE);
                }
                initPics(holder, item, context());
                holder.tv_vis_all_content.setTag(item);
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
                holder.tv_time.setText(ContextUtils.getUserDynamicTime(item.getTimeStamp()));
                if (position == 0) {
                    holder.tv_time.setVisibility(View.VISIBLE);
                } else if (ContextUtils.getUserDynamicTime(item.getTimeStamp()).equals(ContextUtils.getUserDynamicTime((getItem(position - 1)).getTimeStamp()))) {
                    holder.tv_time.setVisibility(View.GONE);
                } else {
                    holder.tv_time.setVisibility(View.VISIBLE);
                }
                FFUtils.setText(holder.tv_title, item.getFood().getFrontCoverModel().getFrontCoverContent().getContent());
                String rest_name = item.getFood() == null || item.getFood().getPoi() == null ? null : item.getFood().getPoi().getTitle();
                FFUtils.setText(holder.tv_rest_name, rest_name);
                holder.tv_rest_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final SYPoi poi = item.getFood().getPoi();
                        if (poi.getIsCustom() == 1) {
                            showToast("该商户为自定义创建，暂无地址信息");
                            return;
                        }
//                        post(Constants.shareConstants().getNetHeaderAdress() + "/shop/queryShopDrawerInfoV250.do", "", null, new FFNetWorkCallBack<MainHomeUGCFragment.RestInfoDrawerResult>() {
                        post(IUrlUtils.Search.queryShopDrawerInfoV250, "", null, new FFNetWorkCallBack<MainHomeUGCFragment.RestInfoDrawerResult>() {
                            @Override
                            public void onSuccess(MainHomeUGCFragment.RestInfoDrawerResult response, FFExtraParams extra) {
                                new PopRest(context(), null,
                                        response.getBuinessDetail().getMerchantPhone(),
                                        poi.getAddress(),
                                        poi.getTitle(),
                                        poi.getLatitude(),
                                        poi.getLongitude(),
                                        poi.getId()).showAtLocation((View) ((BaseActivity)getActivity()).getContainer().getParent(),
                                                                    Gravity.CENTER, 0, 0);
                            }

                            @Override
                            public boolean onFail(FFExtraParams extra) {
                                return false;
                            }
                        }, "merchantId", poi.getId());
                    }
                });
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (PublishModelManager.isNative(item) && ((NativeRichTextFood) item.getFood()).getTask().getTaskExecutState() == XTask.XTaskExecutStateExecing) {
                            return;
                        }
                        if (item.getFood().wasMeishiBianji()) {
                            DynamicDetailIntent intent = new DynamicDetailIntent(item, false, false);
                            intent.setRequestCode(123);
                            startActivity(DynamicDetailActivity.class, intent);
                        } else {
                            FastDetailIntent intent = new FastDetailIntent(item, false);
                            intent.setRequestCode(123);
                            startActivity(FastDetailActivity.class, intent);
                        }
                    }
                });
            }


            @Override
            public void onGetView(Holder holder) {
                holder.tv_rest_name.setOnClickListener(listener);
                holder.tv_vis_all_content.setOnClickListener(listener);
                holder.tv_content.setOnLongClickListener(longListener);
                holder.tv_dynamic_moreImage.setOnClickListener(listener);

                holder.ivs[0] = holder.iv_dynamic_1;
                holder.ivs[1] = holder.iv_dynamic_2;
                holder.ivs[2] = holder.iv_dynamic_3;
                holder.ivs[3] = holder.iv_dynamic_4;
                holder.ivs[4] = holder.iv_dynamic_5;
                holder.ivs[5] = holder.iv_dynamic_6;
                holder.ivs[6] = holder.iv_dynamic_7;
                holder.ivs[7] = holder.iv_dynamic_8;

                holder.iv_dynamic_mssj_only.getLayoutParams().height = HomeChildDynamicFrag.onlyImageHight;
                holder.iv_dynamic_msbj_only.getLayoutParams().height = HomeChildDynamicFrag.msbjImageHight;
                holder.iv_dynamic_mssj_only.getLayoutParams().width = HomeChildDynamicFrag.mssjImageWidth;
                holder.iv_dynamic_msbj_only.getLayoutParams().width = FFUtils.getDisWidth();
                for (ImageView iv : holder.ivs) {
                    iv.getLayoutParams().height = HomeChildDynamicFrag.nineImageHight;
                    iv.getLayoutParams().width = HomeChildDynamicFrag.nineImageHight;
                }
                holder.fl_dynamic_lastImage_container.getLayoutParams().height = HomeChildDynamicFrag.nineImageHight;
                holder.fl_dynamic_lastImage_container.getLayoutParams().width = HomeChildDynamicFrag.nineImageHight;
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag != 1) {
            list_net.clear();
            getData();
        }
        initNativeData();
        adapter.notifyDataSetChanged();
        flag = 2;
    }

    private void initNativeData() {
        if (!userId.equals(SP.getUid())) {
            return;
        }
        ArrayList<PublishModel> list = SYDataManager.shareDataManager().allTasks();
        ArrayList<SYFeed> list1 = new ArrayList<>();
        for (PublishModel pm : list) {
            if (!pm.isbCanceled())
                list1.add(pm.getFeed());
        }
        setNativeData(list1);
    }

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
        addNativeData(list);
    }

    private void addNetData(ArrayList<SYFeed> list) {
        list_net.addAll(list);
        syncAdapter();
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

    private void getData() {
        post(Constants.shareConstants().getNetHeaderAdress() + "/dynamic/queryMyFoodRecordsV250.do", FFUtils.isListEmpty(list_net) ? "" : null, null, new FFNetWorkCallBack<UserDynamicResult>() {
                    @Override
                    public void onSuccess(UserDynamicResult response, FFExtraParams extra) {

                        if (userId.equals(SP.getUid())) {
                            addNetData(response.getFeeds());
                        } else {
                            addNetData(response.getFeeds());
//                            adapter.addData(response.getFeeds());
                        }
                        adapter.notifyDataSetChanged();
                        if (FFUtils.isListEmpty(response.getFeeds())) {
                            prl.setDoPullUp(false);
                            adapter.setHasMore(false);
                            adapter.setLoadMore(true);
                        }
                        prl.loadmoreFinish(prl.SUCCEED);
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        prl.loadmoreFinish(prl.FAIL);
                        return false;
                    }
                }, "userId", userId, "lastFoodRecordId", FFUtils.isListEmpty(list_net) ? 0 : list_net.get(list_net.size() - 1).getId(),
                "lastTimeStamp", FFUtils.isListEmpty(list_net) ? 0 : list_net.get(list_net.size() - 1).getTimeStamp());

    }

    private void visCommentInput(SYFeed feed) {
        //TODO zhangfan 显示评论输入框
    }


    public static void initPics(Holder holder, SYFeed item, FFContext context) {
        //TODO zhangfan 加载动态图片
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
            for (; (i < 9) && (j < item.getFood().getRichTextLists().size()); j++) {
                SYFoodPhotoModel photo = item.getFood().getRichTextLists().get(j).getPhoto();
                if (item.getFood().getRichTextLists().get(j).getPhoto() != null) {
                    i++;
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
            } else {
                i = 0;
                j = 0;
                holder.iv_dynamic_mssj_only.setVisibility(View.GONE);
                holder.iv_dynamic_msbj_only.setVisibility(View.GONE);
                holder.ll_nine_pic_container.setVisibility(View.VISIBLE);
                for (ImageView iv : holder.ivs) {
                    iv.setVisibility(View.GONE);
                }
                holder.fl_dynamic_lastImage_container.setVisibility(View.GONE);
                holder.tv_dynamic_moreImage.setVisibility(View.GONE);

                for (; (i < 10) && (j < item.getFood().getRichTextLists().size()); j++) {
                    SYFoodPhotoModel photo = item.getFood().getRichTextLists().get(j).getPhoto();
                    if (item.getFood().getRichTextLists().get(j).getPhoto() != null) {
                        i++;
                        if (i == 9) {
                            holder.fl_dynamic_lastImage_container.setVisibility(View.VISIBLE);
                            FFImageLoader.loadMiddleImage(context, photo1.getImageAsset().pullProcessedImageUrl(), holder.iv_dynamic_9);
                        } else if (i == 10) {
                            holder.tv_dynamic_moreImage.setVisibility(View.VISIBLE);
                            break;
                        } else {
                            holder.ivs[i - 1].setVisibility(View.VISIBLE);
                            FFImageLoader.loadMiddleImage(context, item.getFood().getRichTextLists().get(j).getPhoto().getImageAsset().pullProcessedImageUrl(), holder.ivs[i - 1]);
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
    }

    public static class Holder {
        TextView tv_time;
        TextView tv_title;
        TextView tv_content;
        TextView tv_vis_all_content;
        TextView tv_rest_name;

        LinearLayout ll_nine_pic_container;
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
        ImageView iv_dynamic_mssj_only;
        ImageView iv_dynamic_msbj_only;
        public ImageView[] ivs = new ImageView[8];

        private ImageView iv_jing;
    }
}
