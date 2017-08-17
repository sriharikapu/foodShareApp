package com.fengnian.smallyellowo.foodie;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.ActivityTags;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYPoi;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.publish.NativeRichTextFood;
import com.fengnian.smallyellowo.foodie.contact.ShareUrlTools;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.FastContentSnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.OnSnapBitmapCreatedListener;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichContentStandardSnapAdapter;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.RichCoverAdapter;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.intentdatas.PublishedSuccesskIndata;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;
import com.fengnian.smallyellowo.foodie.utils.ProductImgUtils;
import com.fengnian.smallyellowo.foodie.wxapi.SinaOpen;
import com.fengnian.smallyellowo.foodie.wxapi.WeixinOpen;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * 个人编辑发表
 */

public class PublishedSuccessActivity extends BaseActivity<PublishedSuccesskIndata> implements IWeiboHandler.Response, View.OnClickListener {

    private TextView tv_share_and_save, tv_1, tv_wechat_friend, tv_circle_friend, tv_sina, tv_cover, tv_content;

    public static PublishModel model;

    BroadcasrReciver receiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()) {
            model = null;
        }
    }

    public void finishAllActivitysByTag(String tag) {
        for (int i = allActivis.size() - 1; i >= 0; i--) {
            if (((FFContext) allActivis.get(i)).getTags().contains(tag) && this != allActivis.get(i)) {
                Activity remove = (Activity) allActivis.remove(i);
                if (allActivis.isEmpty()) {
                    ((BaseActivity) remove).startActivity(MainActivity.class, new IntentData());
                }
                remove.finish();
            }
        }
    }

    boolean has = false;

    @Override
    protected void onPostResume() {
        if (!has) {
            has = true;
            finishAllActivitysByTag(ActivityTags.main);
        }
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activty_published_sucess);
        tv_share_and_save = findView(R.id.tv_share_and_save);
        tv_1 = findView(R.id.tv_1);

        tv_wechat_friend = findView(R.id.tv_wechat_friend);
        tv_circle_friend = findView(R.id.tv_circle_friend);
        tv_sina = findView(R.id.tv_sina);

        tv_cover = findView(R.id.tv_cover);
        tv_content = findView(R.id.tv_content);


        tv_wechat_friend.setOnClickListener(this);
        tv_circle_friend.setOnClickListener(this);
        tv_sina.setOnClickListener(this);

        tv_cover.setOnClickListener(this);
        tv_content.setOnClickListener(this);

        setBackVisible(false);
        addMenu("完成", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        IntentFilter filter = new IntentFilter("share_sucessful");
        receiver = new BroadcasrReciver();
        registerReceiver(receiver, filter);
    }


    private void sharesucessfull() {


        tv_1.setVisibility(View.VISIBLE);

        Drawable img = this.getResources().getDrawable(R.mipmap.commit_sucess);
        //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        tv_share_and_save.setText("分享成功");
        img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
        tv_share_and_save.setCompoundDrawables(null, img, null, null); //设置左图标
    }

    private String getText() {
        StringBuilder title = new StringBuilder();
//                    if (data.getFood().getFrontCoverModel() != null && data.getFood().getFrontCoverModel().getFrontCoverContent() != null) {
//                        title = data.getFood().getFrontCoverModel().getFrontCoverContent().getContent();
//                    }
        SYRichTextFood food = model.getFeed().getFood();
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

    private String getUrl1() {
        if (model.isBshareToSmallYellowO()) {
            return ShareUrlTools.getDynamicRichUrl(model.getFeed());
        } else {
            return ShareUrlTools.getRichNoteUrl(model.getFeed());
        }
    }

    private String getShareTitle() {
        return model.getFeed().getFood().getFrontCoverModel().getFrontCoverContent().getContent();
    }

    @Override
    public void onClick(View view) {

        if (PublishModel.XTaskExecutStateComplete != model.getTaskExecutState()) {
            showToast("未发布成功不能分享!");
            return;
        }

        switch (view.getId()) {
            case R.id.tv_wechat_friend: {
                final int id = showProgressDialog("");
                FFImageLoader.load_base(context(), model.getFeed().getFood().pullCoverImage(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                    @Override
                    public void imageLoaded(Bitmap bitmap, String imageUrl) {
                        dismissProgressDialog(id);
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
            break;
            case R.id.tv_circle_friend: {
                final int id = showProgressDialog("");
                FFImageLoader.load_base(context(), model.getFeed().getFood().pullCoverImage(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                    @Override
                    public void imageLoaded(Bitmap bitmap, String imageUrl) {
                        dismissProgressDialog(id);
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
            break;
            case R.id.tv_sina:

            {
                if (model.getFeed().getFood() instanceof NativeRichTextFood) {
                    showToast("未保存成功的数据不能分享哦!");
                    return;
                }
                final int id = showProgressDialog("");
                FFImageLoader.load_base(context(), model.getFeed().getFood().pullCoverImage(), null, true, Constants.SmallImage, Constants.SmallImage, 0, FFImageLoader.TYPE_NONE, new FFImageCallBack() {
                    @Override
                    public void imageLoaded(Bitmap bitmap, String imageUrl) {
                        dismissProgressDialog(id);
                        if (bitmap == null) {
                            return;
                        }
                        SinaOpen.share(context(), bitmap, getShareTitle(), getText(), getUrl1());
                    }

                    @Override
                    public void onDownLoadProgress(int downloaded, int contentLength) {
                    }
                });
            }
                break;
            case R.id.tv_cover:
                //TODO: 2017-1-11  封面快照
            {
                final int id = showProgressDialog("");
                new Thread() {
                    @Override
                    public void run() {
                        ProductImgUtils.startSnap(new RichCoverAdapter(model.getFeed(), PublishedSuccessActivity.this, getUrl1()), new OnSnapBitmapCreatedListener() {
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

            break;
            case R.id.tv_content:
                //TODO: 2017-1-11  内容快照
            {
                final int id = showProgressDialog("");
                new Thread() {
                    @Override
                    public void run() {
                        OnSnapBitmapCreatedListener listener = new OnSnapBitmapCreatedListener() {
                            @Override
                            public void OnSnapBitmapCreatedListener(final String bitmap) {
                                dismissProgressDialog(id);
                                if (bitmap != null) {
                                    WeixinOpen.getInstance().share2weixin(context(), bitmap);
                                } else {
                                    showToast("快照生成失败");
                                }
                            }
                        };

                        if (model.getFeed().getFood().wasMeishiBianji()) {
                            ProductImgUtils.startSnap(new RichContentStandardSnapAdapter(PublishedSuccessActivity.this, model.getFeed(), getUrl1()), listener);
                        } else {
                            ProductImgUtils.startSnap(new FastContentSnapAdapter(PublishedSuccessActivity.this, model.getFeed(), getUrl1()), listener);
                        }
                    }
                }.start();
            }
            break;
        }
    }

    @Override
    public void onResponse(BaseResponse baseResponse) {
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                showToast("分享成功");
                Intent intent = new Intent("share_sucessful");
                sendBroadcast(intent);
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                showToast("取消分享");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                showToast("分享失败");
                break;
        }
//        finish();
    }

    @Override
    public void onBackPressed() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (model.isBshareToSmallYellowO()) {
            MainActivity.toDynamic();
        } else {
            MainActivity.toUser();
        }
        model = null;

        if (MainActivity.instance == null || MainActivity.instance.getDestroyed()) {
            if (MainActivity.instance != null) {
                MainActivity.instance.finish();
            }
            startActivity(MainActivity.class, new IntentData());
        }

        finish();
    }

    public class BroadcasrReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("share_sucessful".equals(intent.getAction())) {
                sharesucessfull();
            }
        }

    }
}
