//package com.fan.framework.utils.select_picture;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.FrameLayout;
//
//import com.fan.framework.select_picture.AllFolderImagesActivity;
//import com.fan.framework.select_picture.CutImageActivity;
//import com.fan.framework.select_picture.FullscreenActivity;
//import com.fengnian.smallyellowo.foodie.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ImageGalleryActivity extends FullscreenActivity {
//    private static final String INTENT_CUTTENT = "CURRENT";
//    private static final String INTENT_IMAGE = "IMAGE";
//    private static ArrayList<NativeImage> images;
//    private ImageGallery gallery;
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (isFinishing()) {
//            images = null;
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ff_activity_select_pic_gallery);
//        gallery = (ImageGallery) findViewById(R.id.gallery);
//        gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
//        gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
//        gallery.setAdapter(new ImageGalleryAdapter(this, images));
//        gallery.setSelection(getIntent().getIntExtra(INTENT_CUTTENT, 0));
//        gallery.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//            }
//        });
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
//            FrameLayout bottom = (FrameLayout) findViewById(R.id.fl_baseActivity_bottombar);
//            bottom.removeAllViews();
//            View inflate = getLayoutInflater().inflate(R.layout.ff_view_select_pic, null);
//            bottom.addView(inflate);
//            inflate.findViewById(R.id.btn_select).setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    ImageGalleryActivity.this.onClick(v);
//                }
//            });
//
//        } else {
//            addMenu("选择", new OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    ImageGalleryActivity.this.onClick(v);
//                }
//            });
//        }
//    }
//
//
//    @SuppressWarnings("unchecked")
//    public void onClick(View v) {
//        String path = ((List<NativeImage>) getIntent().getSerializableExtra(INTENT_IMAGE)).get(gallery.getSelectedItemPosition()).getPath();
//        Intent data = new Intent();
//        data.putExtra(CutImageActivity.RESULT_PATH, path);
//        setResult(RESULT_OK, data);
//        finish();
//    }
//
//
//    public static void skipToForResult(Activity activity, ArrayList<NativeImage> images, int tag, boolean isAfterLogin, int requestCode) {
//        Intent intent = new Intent(activity, ImageGalleryActivity.class);
//        ImageGalleryActivity.images = images;
//        intent.putExtra(INTENT_CUTTENT, tag);
//        intent.putExtra(AllFolderImagesActivity.INTENT_ISAFTERLOGIN, isAfterLogin);
//        activity.startActivityForResult(intent, requestCode);
//    }
//
//}