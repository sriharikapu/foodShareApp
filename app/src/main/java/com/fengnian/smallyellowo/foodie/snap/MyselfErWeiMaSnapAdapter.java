package com.fengnian.smallyellowo.foodie.snap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.OnSnapViewCreatedListener;
import com.fengnian.smallyellowo.foodie.zxing.CreateQRImage;

/**
 * Created by Administrator on 2017-2-22.
 */

public class MyselfErWeiMaSnapAdapter implements ERWeiMaAdapter {
    private String murl;//生成 二维码的url
    private final Activity activity;

    private int   count=0;

    public MyselfErWeiMaSnapAdapter(Activity activity, String url) {
        this.murl = url;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void getView(int position, final OnSnapViewCreatedListener listener) {
        final View convertView = activity.getLayoutInflater().inflate(R.layout.activity_myself_erweima, null);
        final ImageView iv_avator,iv_erweima;
        TextView tv_name   ;
        iv_avator = (ImageView) convertView.findViewById(R.id.iv_avator);
        iv_erweima = (ImageView) convertView.findViewById(R.id.iv_erweima);
        tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        SYUser user=SP.getUser();
        new Thread() {
            @Override
            public void run() {
                final Bitmap bitmap = CreateQRImage.createQRImage(murl);
                APP.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iv_erweima.setImageBitmap(bitmap);
                        count++;
                        if(count==2)
                        listener.OnSnapViewCreated(convertView);
                    }
                });
            }
        }.start();


        FFImageCallBack lis = new FFImageCallBack() {
            @Override
            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                count++;
                if(count==2)
                listener.OnSnapViewCreated(convertView);
            }

            @Override
            public void onDownLoadProgress(int downloaded, int contentLength) {

            }
        };
        FFImageLoader.loadAvatar((FFContext) activity,user.getHeadImage().getUrl(),iv_avator,lis);
        FFUtils.setText(tv_name,user.getNickName());

    }

//       private class HeadViewHolder {
//        private ImageView iv_avator,iv_erweima;
//
//        private TextView tv_name   ;
//
//         private View headView;
//
//         public void setHeadView(View headView) {
//               this.headView = headView;
//               iv_avator = (ImageView) findViewById(R.id.iv_avator);
//               iv_erweima = (ImageView) findViewById(R.id.iv_erweima);
//               tv_name = (TextView) findViewById(R.id.tv_name);
//
//           }
//           public View findViewById(int id) {
//               return headView.findViewById(id);
//           }
//    }

}
