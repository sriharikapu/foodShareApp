package com.fengnian.smallyellowo.foodie.release;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.RichTextModelManager;
import com.fengnian.smallyellowo.foodie.bean.publics.GalleryInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.ImageBucket;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModelManager;
import com.fengnian.smallyellowo.foodie.utils.AlbumHelper;
import com.fengnian.smallyellowo.foodie.utils.Config;

import java.util.ArrayList;
import java.util.List;

import static com.fengnian.smallyellowo.foodie.utils.Config.list;


/**
 * Created by Administrator on 2016-10-6.
 */

public class GalleryAndPhotoActvity extends BaseActivity<GalleryInfo> implements View.OnClickListener {
    private static int num;
    private FrameLayout fl_frame;
    private FragmentManager fm;
    private FragmentTransaction fragmentTransaction;

    private int flag = 1; //1缩略图 2 列表

    private TextView tv_middle_title, tv_ismemo_text;
    private List<ImageBucket> dataLists;
    public static TextView tv_right_title;
    private ImageView iv_colse;

    private String ismemo;
    private static int enableColor;
    public  static  int template_type;//，模板类型  5：简短  6：简短2  7：简短2-2



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(true);
        ismemo = getIntentData().getIsmemo();
        setContentView(R.layout.gallery_photo_activty);

        template_type=getIntentData().getTemplate_type();

        tv_ismemo_text = (TextView) findViewById(R.id.tv_ismemo_text);
        if ("1".equals(ismemo)) {
            tv_ismemo_text.setVisibility(View.VISIBLE);
        }
        fl_frame = (FrameLayout) findViewById(R.id.fl_frame);
        iv_colse = (ImageView) findViewById(R.id.iv_colse);

        iv_colse.setOnClickListener(this);
        tv_middle_title = (TextView) findViewById(R.id.tv_middle_title);
        tv_middle_title.setOnClickListener(this);
        tv_right_title = (TextView) findViewById(R.id.tv_right_title);

        if(RichTextModelManager.getConfigByIndex(getIntentData().getTemplate_type()).picture.pictureCount!=-1){
            tv_right_title.setText("下一步("+getIntentData().getNum()+"/9)");
        }else{
            tv_right_title.setText("下一步(0)");
        }
        tv_right_title.setOnClickListener(this);
        enableColor = tv_right_title.getCurrentTextColor();
        refreshCompleteUI();

        fm = getSupportFragmentManager();
        dataLists = new ArrayList<>();
        dataLists.clear();
        initdata();
        switchFragment(flag);

    }

    @Override
    protected void onResume() {
        super.onResume();
        num=getIntentData().getNum();
    }

    private AlbumHelper helper;

    private void initdata() {
        if (helper == null)
            helper = AlbumHelper.getHelper();
        helper.init(this);
        if (helper == null) {
            showToast("缩略图不存在");
            return;
        }

        dataLists = helper.getImagesBucketList(false);
//        Collections.reverse(dataLists);
        if (dataLists == null) {
            showToast("图不存在");
            return;
        }
    }

    private PicGalleryFragment pic;
    private AlbumPhonoFragment album;

    private void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();

        hideFragments(fragmentTransaction);
        switch (id) {
            case 1:
                if (pic == null) {
                    pic = new PicGalleryFragment();
                    pic.setPicGallery(dataLists, getIntentData());
                    fragmentTransaction.add(R.id.fl_frame, pic);
                } else {
                    fragmentTransaction.show(pic);
                }
                break;
            case 2:
                if (album == null) {
                    album = new AlbumPhonoFragment();
                    album.setAlbumPhono(dataLists, getIntentData());
                    fragmentTransaction.add(R.id.fl_frame, album);
                } else {
                    fragmentTransaction.show(album);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (pic != null) {
            ft.hide(pic);
        }
        if (album != null) {
            ft.hide(album);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right_title:
                DraftModelManager.onImagesPicked1(this, DraftModelManager.getCurrentDraft(), Config.getStringlist(), getIntentData().getIndex());
                list.clear();
                dataLists.clear();//取消一下
                Config.list.clear();
                initdata();
                break;
            case R.id.tv_middle_title:
                Drawable img_top = this.getResources().getDrawable(R.mipmap.photo_arrow_top);

                Drawable img_bottom = this.getResources().getDrawable(R.mipmap.photo_arrow_bottom);
                //调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                if (flag == 2) {
                    flag = 1;
                    tv_middle_title.setText("相机胶卷");
                    img_top.setBounds(0, 0, img_top.getMinimumWidth(), img_top.getMinimumHeight());
                    tv_middle_title.setCompoundDrawables(null, null, img_top, null); //设置左图标
                } else {
                    flag = 2;
                    tv_middle_title.setText("相册");
                    img_bottom.setBounds(0, 0, img_bottom.getMinimumWidth(), img_top.getMinimumHeight());
                    tv_middle_title.setCompoundDrawables(null, null, img_bottom, null); //设置左图标
                }

                switchFragment(flag);

                break;
            case R.id.iv_colse:

                finish();
                break;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Config.list.clear();
        list.clear();
        dataLists.clear();
    }

    public static void refreshCompleteUI() {
        boolean enable = false;
        if (Config.list.size() > 0) {
            enable = true;
        } else {
            enable = false;
        }
        if (tv_right_title != null) {
            tv_right_title.setEnabled(enable);
            if (enable) {
                tv_right_title.setTextColor(enableColor);
            } else {
                tv_right_title.setTextColor(Color.GRAY);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
           finish();
        }
    }

}
