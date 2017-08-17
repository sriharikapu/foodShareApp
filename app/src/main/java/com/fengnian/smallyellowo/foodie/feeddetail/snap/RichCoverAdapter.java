package com.fengnian.smallyellowo.foodie.feeddetail.snap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.base.FFContext;
import com.fan.framework.imageloader.FFImageCallBack;
import com.fan.framework.utils.TimeUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.zxing.CreateQRImage;

import static com.fan.framework.imageloader.FFImageLoader.TYPE_NONE;
import static com.fan.framework.imageloader.FFImageLoader.TYPE_ROUND;
import static com.fan.framework.imageloader.FFImageLoader.load_base;

/**
 * Created by Administrator on 2017-1-9.  动态详情封面快照
 */
public class RichCoverAdapter extends SnapBaseAdapter {

    private FFContext mcontext;

    public RichCoverAdapter(SYFeed feed, FFContext context, String url) {
        super((Activity) context, feed, url);
        this.mcontext = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void getView(int position, OnSnapViewCreatedListener listener) {

    }


    @Override
    public void getBottom(final OnSnapViewCreatedListener listener) {
        final View view = View.inflate((Context) mcontext, R.layout.view_dyn_detatil_cover, null);

        ImageView iv_head = (ImageView) view.findViewById(R.id.iv_head);
        TextView tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
        ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        RelativeLayout ll_level_container = (RelativeLayout) view.findViewById(R.id.ll_level_container);
        RatingBar rb_level = (RatingBar) view.findViewById(R.id.rb_level);
        TextView tv_level = (TextView) view.findViewById(R.id.tv_level);
        TextView tv_rest_name = (TextView) view.findViewById(R.id.tv_rest_name);

        ImageView iv_qr_code = (ImageView) view.findViewById(R.id.iv_qr_code);

        iv_qr_code.setImageBitmap(CreateQRImage.createQRImage(url));


//        listener.OnSnapViewCreated(view);
        final SnapItemInfo info = new SnapItemInfo();
        info.count = 2;
        info.sum = 0;
//
        SYUser user = data.getUser();
        load_base(mcontext, user.getHeadImage().getUrl(), iv_head, true, Constants.AvatarImage, Constants.AvatarImage, R.mipmap.moren_head_img, TYPE_ROUND, new FFImageCallBack() {
            @Override
            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                info.sum++;
                if (info.count == info.sum)
                    listener.OnSnapViewCreated(view);
            }

            @Override
            public void onDownLoadProgress(int downloaded, int contentLength) {

            }
        });

        tv_nickname.setText(user.getNickName());
        tv_time.setText(TimeUtils.getTime("yyyy-MM-dd    HH:mm", data.getTimeStamp()));

        if (data.getFood().getPoi() != null && !TextUtils.isEmpty(data.getUser().getNickName())) {//
            tv_title.setText(data.getUser().getNickName());
        }else{
            tv_title.setText("");
        }

        if (data.getStarLevel() == 0) {
            ll_level_container.setVisibility(View.GONE);
        } else {
            ll_level_container.setVisibility(View.VISIBLE);
            rb_level.setRating(data.getStarLevel());
            String ss = data.pullStartLevelString();
            tv_level.setText(ss);
        }

        if (data.getFood().getPoi() != null && !TextUtils.isEmpty(data.getFood().getPoi().getTitle())) {//商户名称
            tv_rest_name.setVisibility(View.VISIBLE);
            String str1 = data.getFood().getPoi().getTitle();
            tv_rest_name.setText(str1);
        } else {
            tv_rest_name.setVisibility(View.GONE);
        }

        load_base(mcontext, data.getFood().pullCoverImage(), iv_img, true, 0, 0, R.mipmap.moren_head_img, TYPE_NONE, new FFImageCallBack() {
            @Override
            public void imageLoaded(Bitmap bitmap, String imageUrl) {
                info.sum++;
                if (info.count == info.sum)
                    listener.OnSnapViewCreated(view);
            }

            @Override
            public void onDownLoadProgress(int downloaded, int contentLength) {

            }
        });

    }
}
