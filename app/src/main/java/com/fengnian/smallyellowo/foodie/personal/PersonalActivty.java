package com.fengnian.smallyellowo.foodie.personal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFLogUtil;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appbase.SP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.DialogItem;
import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUploadImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUser;
import com.fengnian.smallyellowo.foodie.bean.publish.AliOssUploadUtil;
import com.fengnian.smallyellowo.foodie.bean.results.NewSetNicknameResult;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;
import com.fengnian.smallyellowo.foodie.utils.PhotoUtil;
import com.fengnian.smallyellowo.foodie.utils.UIUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.fengnian.smallyellowo.foodie.appbase.SP.getUser;
import static com.fengnian.smallyellowo.foodie.contact.AddFriendsActivty.MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD;

/**
 * Created by Administrator on 2016-9-2.
 */

public class PersonalActivty extends BaseActivity<UserInfoIntent> implements View.OnClickListener {
    private RelativeLayout rl_head, rl_bg, rl_nickname, rl_sex, rl_personal_declaration;
    private ImageView iv_header, iv_bg;
    private TextView tv_nickname, tv_sex, tv_personal_declaration;
    private SYUser user;
    private String backgrounurl;

    public static final int changebg = 1000;
    public static final int headimg = 10003;
    public static final int jianqibg = 1001;
    public static final int headimgCommit = 1004;
    public static final int carmer = 10002;

    public static int deviceWidth = 0;
    public static int deviceHeight = 0;
    File pohot_image;

    private SYUser info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        e("PersonalActivty 执行 onCreate ... ");
        Display display = getWindowManager().getDefaultDisplay();
        if (deviceWidth == 0)
            deviceWidth = display.getWidth();
        if (deviceHeight == 0)
            deviceHeight = display.getHeight();

        user = getIntentData().getUser();

        SYUser loginuser = SP.getUser();
        loginuser.setSex(user.getSex());
        loginuser.setPersonalDeclaration(user.getPersonalDeclaration());
        SP.setUser(loginuser);


        setContentView(R.layout.activity_personal);
        rl_head = findView(R.id.rl_head);
        iv_header = findView(R.id.iv_header);

        rl_bg = findView(R.id.rl_bg);
        iv_bg = findView(R.id.iv_bg);

        rl_nickname = findView(R.id.rl_nickname);
        tv_nickname = findView(R.id.tv_nickname);

        rl_sex = findView(R.id.rl_sex);
        tv_sex = findView(R.id.tv_sex);

        rl_personal_declaration = findView(R.id.rl_personal_declaration);
        tv_personal_declaration = findView(R.id.tv_personal_declaration);


        rl_head.setOnClickListener(this);
        rl_bg.setOnClickListener(this);
        rl_nickname.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_personal_declaration.setOnClickListener(this);

        if (loginuser != null && loginuser.getHeadImage() != null) {
            FFImageLoader.loadAvatar(this, loginuser.getHeadImage().getUrl(), iv_header);
        } else {
            FFImageLoader.loadAvatar(this, "", iv_header);
        }
        if (loginuser != null && loginuser.getBgImage() != null) {
            FFImageLoader.loadSmallImage(this, loginuser.getBgImage().getUrl(), iv_bg);
        } else {
            FFImageLoader.loadSmallImage(this, "", iv_bg);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        info = getUser();
        tv_nickname.setText(info.getNickName());
        setSex();
        if (info.getPersonalDeclaration() == null || info.getPersonalDeclaration().length() == 0)
            tv_personal_declaration.setText("未填写");
        else {
            tv_personal_declaration.setText(info.getPersonalDeclaration());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            pohot_image = (File) savedInstanceState.getSerializable("file_path");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("file_path", pohot_image);
        super.onSaveInstanceState(outState);
    }

    private void setSex() {
        if (info.getSex() == 0) {
            tv_sex.setText("男");
        } else if (info.getSex() == 1) {
            tv_sex.setText("女");
        }
        if (info.getSex() == -1) {
            tv_sex.setText("未设置");
        }
    }

    private void checkPersionalInfo(final String tag, final String tag_value) {
        FFExtraParams extra = new FFExtraParams();
        extra.setDoCache(true);
        extra.setSynchronized(false);
        extra.setKeepWhenActivityFinished(false);
        extra.setProgressDialogcancelAble(true);
//        post(Constants.shareConstants().getNetHeaderAdress() + "/user/updateUserInfoNew.do", "", extra, new FFNetWorkCallBack<NewSetNicknameResult>() {
        post(IUrlUtils.Search.updateUserInfoNew, "", extra, new FFNetWorkCallBack<NewSetNicknameResult>() {
            @Override
            public void onSuccess(NewSetNicknameResult response, FFExtraParams extra) {

                if (response.getErrorCode() == 0) {
                    SYUser pinfo = response.getUser();
//                    if(pinfo.getSex()==0){
//                        info.setSex(-1);
//                    }else{
//                        if("男".equals(pinfo.getSex())){

//                        }else{
//                            info.setSex(1);
//                        }
//                    SYImage image_head = new SYImage();
//                    image_head.setUrl(pinfo.getHeadImage());
//                    info.setHeadImage(image_head);
//                    SYImage image = new SYImage();
//                    image.setUrl(pinfo.getBgImage());


                    if ("sex".equals(tag)) {
//                        tv_sex.setText(info.getSex() == 0 ? "男" : "女");
                        info.setSex(pinfo.getSex());
                        setSex();
                    }
                    if ("headImage".equals(tag)) {
                        //头像上传
                        info.setHeadImage(pinfo.getHeadImage());
                        FFImageLoader.loadAvatar(PersonalActivty.this, pinfo.getHeadImage().getUrl(), iv_header);
                    }
                    if ("bgImage".equals(tag)) {
                        //背景上传
                        info.setBgImage(pinfo.getBgImage());

                        FFImageLoader.loadSmallImage(PersonalActivty.this, pinfo.getBgImage().getUrl(), iv_bg);
                    }
                    SP.setUser(info);// 将信息 转化为 json

                    Intent intent = new Intent(MyActions.ACTION_UPDATE_USER_INFO);
                    LocalBroadcastManager.getInstance(PersonalActivty.this).sendBroadcast(intent);
                }
//                showToast(response.getServerMsg());
            }

            @Override
            public boolean onFail(FFExtraParams extra) {
                return false;
            }
        }, tag, tag_value, "type", "0");

    }

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 10256;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head:

                ArrayList<DialogItem> items = new ArrayList<DialogItem>();

                items.add(new DialogItem("拍照", R.layout.bottom_dialog_btn) {
                    @Override
                    public void onClick() {
                        super.onClick();

                        if (android.os.Build.VERSION.SDK_INT >= 23 && !hasPermission(android.Manifest.permission.CAMERA)) {
                            ActivityCompat.requestPermissions(PersonalActivty.this, new String[]{android.Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        } else {
                            String sdStatus = Environment.getExternalStorageState();
                            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                                showToast("未检测到内存卡");
                                return;
                            }
                            pohot_image = PhotoUtil.getPhotoByTaking(PersonalActivty.this);
                        }

                    }
                });
                items.add(new DialogItem("从相册选择", R.layout.bottom_dialog_btn_gallery) {
                    @Override
                    public void onClick() {
                        super.onClick();

                        if (android.os.Build.VERSION.SDK_INT >= 23&&!hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                            ActivityCompat.requestPermissions(PersonalActivty.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD);
                        else {
                            Intent bg_intent = new Intent(Intent.ACTION_PICK, null);
                            bg_intent.setDataAndType(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(bg_intent, headimg);
                        }


                    }
                });
                items.add(new DialogItem("取消", R.layout.bottom_dialog_cancel));
                UIUtil.showBottomDialog(this, items);
                break;
            case R.id.rl_bg:
                Intent bg_intent = new Intent(Intent.ACTION_PICK, null);
                bg_intent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(bg_intent, changebg);

                break;
            case R.id.rl_nickname:
                Intent intent = new Intent(this, NicknameActivty.class);
                intent.putExtra("nickname", info.getNickName());
                startActivity(intent);
                break;
            case R.id.rl_sex:

                ArrayList<DialogItem> items_sex = new ArrayList<DialogItem>();

                items_sex.add(new DialogItem("男", R.layout.bottom_dialog_btn) {
                    @Override
                    public void onClick() {
                        super.onClick();
                        checkPersionalInfo("sex", "男");

                    }
                });
                items_sex.add(new DialogItem("女", R.layout.bottom_dialog_btn_gallery) {
                    @Override
                    public void onClick() {
                        super.onClick();
                        checkPersionalInfo("sex", "女");
                    }
                });
                items_sex.add(new DialogItem("取消", R.layout.bottom_dialog_cancel));
                UIUtil.showBottomDialog(this, items_sex);
                break;
            case R.id.rl_personal_declaration:
                Intent intent1 = new Intent(this, UpdatePersonalDeclarationActivty.class);
                intent1.putExtra("user", info.getPersonalDeclaration() == null ? user.getPersonalDeclaration() : info.getPersonalDeclaration());
                startActivity(intent1);

                break;

        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(String flag, Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
//        intent.putExtra("crop", "true");
//        // aspectX aspectY 是宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("return-data", true);
        String photoPath = PhotoUtil.getPathByUri(this,
                uri);// 相片的路径
        if (photoPath == null) {
            showToast("未获取图片");
            return;
        }
        if ("头像".equals(flag)) {
//          startActivityForResult(intent, headimgCommit);
//            startActivity(com.fan.framework.select_picture.CutImageActivity.class,new IntentData());
            com.fan.framework.select_picture.CutImageActivity.skipToForResult(PersonalActivty.this, photoPath, headimgCommit);

        } else {

//            startActivityForResult(intent, jianqibg);
            com.fan.framework.select_picture.CutImageActivity.skipToForResult(PersonalActivty.this, photoPath, jianqibg);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case changebg:
                    startPhotoZoom("背景", data.getData());
                    break;

                case headimg:
                    startPhotoZoom("头像", data.getData());

                    break;
                case jianqibg: //相册背景
//                    if (data != null) {
//                        setPicToView(data);
//                    }
//                    checkPersionalInfo("bgImage", Base_64_url);
//                    String url=     data.getParcelableExtra("path");
                    String url = data.getStringExtra("path");
                    ;

                    UploadImg("bgImage", url);

                    break;
                case headimgCommit: // 相册  头像
//                    if (data != null) {
//                        setPicToView(data);
//                    }
//                    checkPersionalInfo("headImage", Base_64_url);
//                    String url1=     data.getParcelableExtra("path");
                    String url11 = data.getStringExtra("path");
                    ;
                    UploadImg("headImage", url11);
                    break;
                case Constants.PHOTO_GRAPH: //拍照
//                    Bitmap rigitBitmap1 = ImageUtil.loadRightDirectioniBitmap(
//                            ImageUtil.compressImageFromFile(pohot_image.getAbsolutePath(), 500, 500), pohot_image.getAbsolutePath());// 相片方向矫正
//                    checkPersionalInfo("headImage", imgToBase64(rigitBitmap1));
                    if (pohot_image == null) {
                        showToast("创建的文件失败了");
                        return;
                    }
                    UploadImg("headImage", pohot_image.getAbsolutePath());
                    break;
            }
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private Bitmap bitmap;
    private String IMAGE_FILE_PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/bg_pic";

    private File iconFile;

    private String base_64_url, Base_64_url;


    private String UploadImg(final String flag, final String url) {
        final int id = showProgressDialog("", false, Integer.MAX_VALUE);
        SYUploadImage syUploadImage = new SYUploadImage() {
            @Override
            public void uploadFail() {
                super.uploadFail();
                dis();
                showToast("上传失败~~");
            }

            @Override
            public void uploadSuccess() {
                super.uploadSuccess();
                dis();
                String url = getImage().getUrl();
                System.out.println("获取的上传成功 的图片地址..." + url);
                //// TODO: 2017-1-5  上传背景跟图片
                checkPersionalInfo(flag, url);
            }

            private void dis() {
                dismissProgressDialog(id);
            }
        };

        SYImage syImage = new SYImage();
        syImage.setUrl(url);
        syUploadImage.setImage(syImage);
        AliOssUploadUtil.getInstance().ossUpload(syUploadImage);

        return "";
    }

    /**
     * 动态申请  权限后用用的选择
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS_ADD: {
                // If request is cancelled, the result arrays are empty.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //用户授权
//                initLoation();
                    Intent bg_intent = new Intent(Intent.ACTION_PICK, null);
                    bg_intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(bg_intent, headimg);
                } else {
                    showToast("未获得访问权限，不能访问！！");
                }

            }
            break;
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        showToast("未检测到内存卡");
                        return;
                    }
                    pohot_image = PhotoUtil.getPhotoByTaking(PersonalActivty.this);
                } else {
                    showToast("未获得访问权限，不能访问！！");
                }

            }


            break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
