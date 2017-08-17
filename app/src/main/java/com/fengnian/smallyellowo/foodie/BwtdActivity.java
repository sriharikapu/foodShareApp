package com.fengnian.smallyellowo.foodie;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.base.MyBaseAdapter;
import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.widgets.PullToRefreshLayout;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseHolder;
import com.fengnian.smallyellowo.foodie.appbase.PullToRefreshFragment;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BwtdResult;
import com.fengnian.smallyellowo.foodie.bean.results.NoticeResult;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.NoticeDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.RestInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.WebInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BwtdActivity extends BaseActivity<IntentData> {
//
//
//    @Bind(R.id.listView)
//    ListView listView;
//    private MyBaseAdapter<HolderBwtd, BwtdResult.SYMillionFindMerchantsGroupModel> adapter;
//    private PullToRefreshLayout prl;

    public static class HolderBwtd {
        public TextView tv_title;
        public TextView tv_notice;
        public LinearLayout ll_item_container;
        public final static ArrayList<HolderBwtdItem> items = new ArrayList<>();

        public static class HolderBwtdItem extends MyBaseHolder {
            private ImageView iv_img;
            private TextView tv_item_title;
            private TextView tv_poi_title;
            private ImageView iv_avatar;
            private TextView tv_user_name;
            BaseActivity context;

            public HolderBwtdItem(BaseActivity context, LinearLayout recyclerView) {
                super(context.getLayoutInflater().inflate(R.layout.item_bwtd_item, recyclerView, false));
                this.context = context;
                iv_img = findViewById(R.id.iv_img);
                tv_item_title = findViewById(R.id.tv_item_title);
                tv_poi_title = findViewById(R.id.tv_poi_title);
                iv_avatar = findViewById(R.id.iv_avatar);
                tv_user_name = findViewById(R.id.tv_user_name);
                itemView.setTag(this);
            }

            public void onBind(int position) {
            }

            public void onBind(final BwtdResult.SYMillionFindMerchantModel item) {
                FFImageLoader.loadMiddleImage(context, item.getFoodImage().getUrl(), iv_img);
                tv_item_title.setText(item.getTitle());
                tv_poi_title.setText(item.getPoi().getTitle());
                tv_user_name.setText(item.getUser().getNickName());
                FFImageLoader.loadAvatar(context, item.getUser().getHeadImage().getUrl(), iv_avatar);
                View.OnClickListener l = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoIntent data = new UserInfoIntent();
                        data.setId(item.getUser().getId());
                        data.setUser(item.getUser());
                        context.startActivity(ClubUserInfoActivity.class, data);
                    }
                };
                tv_user_name.setOnClickListener(l);
                iv_avatar.setOnClickListener(l);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(DynamicDetailActivity.class, new DynamicDetailIntent(item.getFeedId()));
                    }
                });
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bwtd);
//        ButterKnife.bind(this);
        setTitle("百万探店");
        getSupportFragmentManager().beginTransaction().add(getContainer().getId(),new BwtdFregment()).commit();
//        adapter = new MyBaseAdapter<HolderBwtd, BwtdResult.SYMillionFindMerchantsGroupModel>(HolderBwtd.class, this, R.layout.item_bwtd) {
//            @Override
//            public void initView(View convertView, HolderBwtd holder, int position, final BwtdResult.SYMillionFindMerchantsGroupModel item_image_gallery) {
//                holder.tv_title.setText(item_image_gallery.getMillionFindMerchantsGrouptitle());
//                holder.tv_notice.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        WebInfo data = new WebInfo();
//                        data.setTitle(item_image_gallery.getActivityTitle());
//                        data.setUrl(item_image_gallery.getActivityHtmlUrl());
//                        startActivity(CommonWebviewUtilActivity.class, data);
//                    }
//                });
//                while (holder.ll_item_container.getChildCount() > 0) {
//                    HolderBwtd.items.add((HolderBwtd.HolderBwtdItem) holder.ll_item_container.getChildAt(0).getTag());
//                    holder.ll_item_container.removeViewAt(0);
//                }
//                if (item_image_gallery.getMillionFindMerchantsModelList() != null)
//                    for (int i = 0; i < item_image_gallery.getMillionFindMerchantsModelList().size(); i++) {
//                        HolderBwtd.HolderBwtdItem holder1;
//                        if (HolderBwtd.items.isEmpty()) {
//                            holder1 = new HolderBwtd.HolderBwtdItem(context(), holder.ll_item_container);
//                        } else {
//                            holder1 = HolderBwtd.items.remove(0);
//                        }
//                        holder1.onBind(item_image_gallery.getMillionFindMerchantsModelList().get(i));
//                        holder.ll_item_container.addView(holder1.itemView);
//                    }
//            }
//        };
//        listView.setAdapter(adapter);
//        prl = PullToRefreshLayout.supportPull(listView, new PullToRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
//                getData(true);
//            }
//
//            @Override
//            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
//                getData(false);
//            }
//        });
//        prl.setDoPullDown(true);
//        prl.setDoPullUp(true);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                prl.autoRefresh();
//            }
//        }, 500);
    }

//    private void getData(final boolean isRefresh) {
//        post(Constants.shareConstants().getNetHeaderAdress() + "/page/getMillionMerchant.do", isRefresh ? null : "", new FFExtraParams().setInitPage(isRefresh && adapter.getCount() == 0), new FFNetWorkCallBack<BwtdResult>() {
//            @Override
//            public void onSuccess(BwtdResult response, FFExtraParams extra) {
//                prl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                if (FFUtils.isListEmpty(response.getData())) {
//                    prl.loadmoreFinish(PullToRefreshLayout.NO_DATA_SUCCEED);
//                } else {
//                    prl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                }
//                if (isRefresh) {
//                    adapter.setData(response.getData());
//                } else {
//                    adapter.addData(response.getData());
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public boolean onFail(FFExtraParams extra) {
//                prl.loadmoreFinish(PullToRefreshLayout.FAIL);
//                prl.refreshFinish(PullToRefreshLayout.FAIL);
//                return false;
//            }
//        }, "pageSize", 20, "lastId", isRefresh ? 0 : (adapter.isEmpty() ? "0" : adapter.getItem(adapter.getCount() - 1).getId()));
//    }
}
