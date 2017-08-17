package com.fengnian.smallyellowo.foodie.usercenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseHolder;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFoodPhotoModel;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.dialogs.PopRest;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.FastDetailActivity;
import com.fengnian.smallyellowo.foodie.fragments.MainHomeUGCFragment;
import com.fengnian.smallyellowo.foodie.intentdatas.DynamicDetailIntent;
import com.fengnian.smallyellowo.foodie.intentdatas.FastDetailIntent;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModelManager;
import com.fengnian.smallyellowo.foodie.utils.ContextUtils;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/6/12.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private List<SYFeed> items;
    private BaseActivity mContext;

    public UserAdapter(Context context, List<SYFeed> items) {
        this.items = items;
        this.mContext = (BaseActivity) context;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserHolder(View.inflate(mContext, R.layout.item_user_adapter, null));
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (FFUtils.isListEmpty(items)) {
            return 0;
        }
        return items.size();
    }

    public class UserHolder extends MyBaseHolder {
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.single_image)
        ImageView singleImage;
        @Bind(R.id.mutil_image_ll)
        LinearLayout mutilImageLl;
        @Bind(R.id.star)
        RatingBar star;
        @Bind(R.id.recommend)
        TextView recommend;
        @Bind(R.id.tv_rest_name)
        TextView tvRestName;
        @Bind(R.id.food_title)
        TextView foodTitle;
        @Bind(R.id.star_ll)
        LinearLayout starLl;

        private View adapterView;

        public UserHolder(View itemView) {
            super(itemView);
            adapterView = itemView;
            ButterKnife.bind(this, itemView);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);

            int height = (int) (FFUtils.getDisWidth() * 0.33);
            FFLogUtil.e("useradapter", "height = " + height);
            RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) singleImage.getLayoutParams();
            imageParams.height = height;
            singleImage.setLayoutParams(imageParams);

            RelativeLayout.LayoutParams llParams = (RelativeLayout.LayoutParams) mutilImageLl.getLayoutParams();
            llParams.height = height;
            mutilImageLl.setLayoutParams(llParams);
        }

        @Override
        public void onBind(int position) {
            final SYFeed item = items.get(position);
            String rest_name = item.getFood() == null || item.getFood().getPoi() == null ? null : item.getFood().getPoi().getTitle();
            time.setText(ContextUtils.getUserDynamicTime(item.getTimeStamp()));
            FFUtils.setText(foodTitle, item.getFood().getFrontCoverModel().getFrontCoverContent().getContent());
            FFUtils.setText(tvRestName, rest_name);
            if (item.getStarLevel() <= 0) {
                starLl.setVisibility(View.GONE);
            } else {
                starLl.setVisibility(View.VISIBLE);
                star.setRating(item.getStarLevel());
                recommend.setText(item.pullStartLevelString());
            }

            initPics(item);

            tvRestName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SYPoi poi = item.getFood().getPoi();
                    if (poi.getIsCustom() == 1) {
                        mContext.showToast("该商户为自定义创建，暂无地址信息");
                        return;
                    }

                    showShopInfo(poi);

                }
            });

            adapterView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PublishModelManager.isNative(item) && ((NativeRichTextFood) item.getFood()).getTask().getTaskExecutState() == XTask.XTaskExecutStateExecing) {
                        return;
                    }
                    if (item.getFood().wasMeishiBianji()) {
                        DynamicDetailIntent intent = new DynamicDetailIntent(item, false, false);
                        intent.setRequestCode(123);
                        mContext.startActivity(DynamicDetailActivity.class, intent);
                    } else {
                        FastDetailIntent intent = new FastDetailIntent(item, false);
                        intent.setRequestCode(123);
                        mContext.startActivity(FastDetailActivity.class, intent);
                    }
                }
            });
        }

        public void initPics(SYFeed item) {
            //TODO zhangfan 加载动态图片
            if (item.getFood().wasMeishiBianji()) {//美食编辑
                singleImage.setVisibility(View.VISIBLE);
                mutilImageLl.setVisibility(View.GONE);
                String imgUrl = item.getFood().pullCoverImage();
                FFImageLoader.loadBigImage(mContext, imgUrl, singleImage);
            } else {//美食速记
                mutilImageLl.setVisibility(View.VISIBLE);
                singleImage.setVisibility(View.GONE);
                for (int i = 0; i < item.getFood().getRichTextLists().size(); i++) {
                    if (i >= 3) {
                        break;
                    }
                    SYFoodPhotoModel photo = item.getFood().getRichTextLists().get(i).getPhoto();
                    ImageView imageView = (ImageView) mutilImageLl.getChildAt(i);
                    imageView.setVisibility(View.VISIBLE);

                    FFImageLoader.loadMiddleImage(mContext, photo.getImageAsset().pullProcessedImageUrl(), imageView);
                }

            }
        }
    }

    private void showShopInfo(final SYPoi poi) {
//        mContext.post(Constants.shareConstants().getNetHeaderAdress() + "/shop/queryShopDrawerInfoV250.do", "", null, new FFNetWorkCallBack<MainHomeUGCFragment.RestInfoDrawerResult>() {
        mContext.post(IUrlUtils.Search.queryShopDrawerInfoV250, "", null, new FFNetWorkCallBack<MainHomeUGCFragment.RestInfoDrawerResult>() {
            @Override
            public void onSuccess(MainHomeUGCFragment.RestInfoDrawerResult response, FFExtraParams extra) {
                new PopRest(mContext, null,
                        response.getBuinessDetail().getMerchantPhone(),
                        poi.getAddress(),
                        poi.getTitle(),
                        poi.getLatitude(),
                        poi.getLongitude(),
                        poi.getId()).showAtLocation((View) mContext.getContainer().getParent(),
                        Gravity.CENTER, 0, 0);
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, "merchantId", poi.getId());
    }

}
