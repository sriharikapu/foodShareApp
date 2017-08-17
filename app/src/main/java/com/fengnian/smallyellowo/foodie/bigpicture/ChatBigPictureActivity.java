package com.fengnian.smallyellowo.foodie.bigpicture;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.widgets.MyPercenImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ChatBigPictureActivity extends BaseActivity<BitPictureIntent> implements ImageGalleryAdapter.LoadImagePercentListener {

    private ImageGallery gallery;
    private int width;
    private ImageGalleryAdapter adapter;
    int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic_gallery);
        setNotitle(true);
        findView();
        afterCreate();
    }

    private TextView tv_number;

    @Override
    public void onLoadImagePercent(int nowPercent, int total, int position,
                                   boolean fail) {
        if (position != gallery.getSelectedItemPosition()) {
            return;
        }

    }


    public void findView() {
        tv_number = (TextView) findViewById(R.id.tv_number);
        gallery = (ImageGallery) findViewById(R.id.gallery);

    }


    @SuppressLint("NewApi")
    public void afterCreate() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR2) {
            findViewById(R.id.ll_actionBar).setAlpha(0.75f);
        }
        this.width = FFUtils.getDisWidth() - FFUtils.getPx(160);
        @SuppressWarnings("unchecked")
        ArrayList<BitPictureIntent.ImageMap> serializableExtra = getIntentData().getImages();
        if (serializableExtra != null) {
            sum = serializableExtra.size();
        }
        int intExtra = getIntentData().getIndex();
//		setTitle("餐厅详情");
        tv_number.setText((intExtra + 1) + "/" + sum);
        gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
        gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
        adapter = new ImageGalleryAdapter(this, serializableExtra);
        gallery.setAdapter(adapter);
        gallery.setSelection(intExtra);
        gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                tv_number.setText((position + 1) + "/" + sum);
                BitPictureIntent.ImageMap item = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        gallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Bitmap image = ((MyPercenImageView) gallery.getSelectedView()).getImage();
                if (image == null) {
                    return false;
                }
                final String path = FFImageLoader.findFile(adapter.getItem(position).getPath());
                if (path != null && new File(path).exists()) {
                    new EnsureDialog.Builder(context()).setNegativeButton("保存到相册", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveCurrentImage(path);
                        }
                    }).show();
                }
                return true;
            }
        });
    }

    private void saveCurrentImage(final String path) {
        new Thread() {
            @Override
            public void run() {
                File appDir = new File(Environment.getExternalStorageDirectory(), "smallYellowO");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String fileName = System.currentTimeMillis() + ".jpg";
                final File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    FileInputStream fis = new FileInputStream(new File(path));
                    byte[] temp = new byte[1024];
                    int count = 0;
                    while ((count = fis.read(temp)) > 0) {
                        fos.write(temp, 0, count);
                    }
                    fis.close();
                    fos.flush();
                    fos.close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                        }
                    });
                    showToast("已保存到相册");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
