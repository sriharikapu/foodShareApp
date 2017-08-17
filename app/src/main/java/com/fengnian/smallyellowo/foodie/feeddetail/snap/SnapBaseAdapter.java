package com.fengnian.smallyellowo.foodie.feeddetail.snap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.utils.IsAddCrownUtils;
import com.fengnian.smallyellowo.foodie.zxing.CreateQRImage;

/**
 * Created by Administrator on 2017-1-9.
 */

public abstract class SnapBaseAdapter {

    public final Activity activity;
    protected final String url;
    protected SYFeed data;

    public SnapBaseAdapter(Activity activity, SYFeed data, String url) {
        this.data = data;
        this.activity = activity;
        this.url = url;
    }


        public SnapBaseAdapter(Activity activity,String url) {
        this.activity=activity;
        this.url=url;
    }
    public void getBottom(final OnSnapViewCreatedListener listener) {
        final View convertView = activity.getLayoutInflater().inflate(R.layout.snap_content_qrcode, null, false);
        final ImageView iv_qr_code = (ImageView) convertView.findViewById(R.id.iv_qr_code);
        new Thread() {
            @Override
            public void run() {
                final Bitmap bitmap = CreateQRImage.createQRImage(url);
                APP.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_qr_code.setImageBitmap(bitmap);
                        listener.OnSnapViewCreated(convertView);
                    }
                });
            }
        }.start();
    }

    /**
     * 是否显示头像皇冠
     */
    protected void setKingCrownInfo(View HeadView,SYFeed data) {
        if (HeadView != null && data != null && data.getUser() != null){
            ImageView iv_add_crown = (ImageView) HeadView.findViewById(R.id.iv_add_crown);
            if (iv_add_crown != null){
                IsAddCrownUtils.checkIsAddCrow(data.getUser(), iv_add_crown);
            }
        }
    }

    public abstract int getCount();

    public abstract void getView(int position, OnSnapViewCreatedListener listener);
}