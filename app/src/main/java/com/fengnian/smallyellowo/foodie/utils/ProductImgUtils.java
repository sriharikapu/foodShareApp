package com.fengnian.smallyellowo.foodie.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.fan.framework.base.XData;
import com.fan.framework.config.FFConfig;
import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.FileUitl;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.OnSnapBitmapCreatedListener;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.OnSnapViewCreatedListener;
import com.fengnian.smallyellowo.foodie.feeddetail.snap.SnapBaseAdapter;
import com.fengnian.smallyellowo.foodie.snap.ERWeiMaAdapter;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Administrator on 2016-12-27.
 */

public class ProductImgUtils {

    public static float max_bitmap_memory = Math.min(40*1024*1024,FFConfig.calculateMemoryCacheSize()*7/2);//生成图片的大小(目前为9M)
    /**
     * 生成二维码的  专用
     *
     * @param parent_view
     * @param index
     * @param listener
     */
    public static void createQrcodeSnap(final ERWeiMaAdapter parent_view, final int index, final OnSnapBitmapCreatedListener listener) {
        if (index == parent_view.getCount()) {
            parent_view.getView(index, new OnSnapViewCreatedListener() {
                @Override
                public void OnSnapViewCreated(View v) {
                    try {
                        Bitmap b = FFUtils.getBitmapViewByMeasure(v, FFUtils.getDisWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                        final String path = FileUitl.getCacheFileDir() + "/share";
//                                                    FFImageUtil.saveBitmap(path, bitmap);
                        FFImageUtil.saveBitmap(path, b);
                        if(new File(path).length() == 0){
                            ProductImgUtils.max_bitmap_memory-=5*1024*1024;
                        }
                        b.recycle();
                        listener.OnSnapBitmapCreatedListener(path);
                    } catch (Throwable e) {
                        FFLogUtil.e("snapException", e);
                        listener.OnSnapBitmapCreatedListener(null);
                    }
                }
            });
            return;
        }
    }

    private static class BitmapInfo {
        private Bitmap bitmap;
        int size;
        String path;
        private int height;
        private int width;

        public BitmapInfo(Bitmap b, boolean isLow) {
            bitmap = b;
            if (b != null) {
                height = bitmap.getHeight();
                width = bitmap.getWidth();
                size = bitmap.getWidth() * bitmap.getHeight();
            }
            if (isLow) {
                low();
            }
        }

        public void low() {
            if(bitmap == null){
                return;
            }
            save();
            bitmap.recycle();
            bitmap = null;
        }

        private void save() {
            String superParent = getTempDir();
            path = superParent + "/" + XData.uuid();
            FFImageUtil.saveBitmap(path, bitmap);
        }

        public Bitmap getBitmap() {
            if(bitmap == null || bitmap.isRecycled()){
                BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
                decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                return BitmapFactory.decodeFile(path, decodeOptions);
            }
            return bitmap;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    private static void mkdir(String parent) {
        File parentFile = new File(parent);
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
    }

    @NonNull
    private static String getTempDir() {
        String superParent = FileUitl.getCacheFileDir() + "/snap";
        mkdir(superParent);
        return superParent;
    }

    public static void startSnap(final SnapBaseAdapter parent_view, final OnSnapBitmapCreatedListener listener) {
//        FFUtils.isLowMem();
        if (Looper.getMainLooper() == Looper.myLooper()) {
            new Thread() {
                @Override
                public void run() {
                    startSnap(parent_view, listener);
                }
            }.start();
            return;
        }
        FileUitl.removeAll(new File(getTempDir()));
        snap(false, new ArrayList<BitmapInfo>(), parent_view, 0, listener);
    }

    private static void snap(final boolean lowMode, final ArrayList<BitmapInfo> bitmap, final SnapBaseAdapter parent_view, final int index, final OnSnapBitmapCreatedListener listener) {
        if (parent_view != null && parent_view.activity != null) {
            if (parent_view.activity.isFinishing()) {
                return;
            }
        }
        if (index == parent_view.getCount()) {
            parent_view.getBottom(new OnSnapViewCreatedListener() {

                boolean isLow = lowMode;

                @Override
                public void OnSnapViewCreated(final View v) {
                    if (Looper.getMainLooper() == Looper.myLooper()) {
                        new Thread() {
                            @Override
                            public void run() {
                                OnSnapViewCreated(v);
                            }
                        }.start();
                        return;
                    }
                    try {
                        Bitmap b;
                        try {
                            b = FFUtils.getBitmapViewByMeasure(v, FFUtils.getDisWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                        } catch (OutOfMemoryError e) {
                            isLow = true;
                            for (BitmapInfo bmp : bitmap) {
                                bmp.low();
                            }
                            OnSnapViewCreated(v);
                            return;
                        }
                        bitmap.add(new BitmapInfo(b, isLow));

                        int size = getTotalSize(bitmap);
                        if(size > FFConfig.IMAGE_CATCHS*7/4){
                            isLow = true;
                            for (BitmapInfo bmp : bitmap) {
                                bmp.low();
                            }
                        }
                        Bitmap bitmap1 = product_together_img(bitmap);
                        final String path = FileUitl.getCacheFileDir() + "/share";
//                                                    FFImageUtil.saveBitmap(path, bitmap);
                        FFImageUtil.saveBitmap(path, bitmap1);
                        if(new File(path).length() == 0){
                            ProductImgUtils.max_bitmap_memory-=5*1024*1024;
                        }
                        bitmap1.recycle();
                        listener.OnSnapBitmapCreatedListener(path);
                    } catch (Throwable e) {
                        FFLogUtil.e("snapException", e);
                        listener.OnSnapBitmapCreatedListener(null);
                    }
                }
            });
            return;
        }
        parent_view.getView(index, new OnSnapViewCreatedListener() {
            boolean isLow = lowMode;

            public void OnSnapViewCreated(final View v) {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    new Thread() {
                        @Override
                        public void run() {
                            OnSnapViewCreated(v);
                        }
                    }.start();
                    return;
                }
                try {
                    Bitmap b;
                    try {
                        b = FFUtils.getBitmapViewByMeasure(v, FFUtils.getDisWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                        if(b == null){
                            listener.OnSnapBitmapCreatedListener(null);
                            return;
                        }
                    } catch (OutOfMemoryError e) {
                        isLow = true;
                        for (BitmapInfo bmp : bitmap) {
                            bmp.low();
                        }
                        OnSnapViewCreated(v);
                        return;
                    }
                    bitmap.add(new BitmapInfo(b, isLow));

                    int size = getTotalSize(bitmap);
                    if(size > FFConfig.IMAGE_CATCHS*7/3){
                        isLow = true;
                        for (BitmapInfo bmp : bitmap) {
                            bmp.low();
                        }
                    }
                    snap(isLow, bitmap, parent_view, index + 1, listener);

                } catch (Throwable e) {
                    FFLogUtil.e("snapException", e);
                    listener.OnSnapBitmapCreatedListener(null);
                }
            }
        });
    }

    private static int getTotalSize(ArrayList<BitmapInfo> bitmap) {
        int size_temp = 0;
        if (bitmap != null) {
            for (BitmapInfo bmp : bitmap) {
                size_temp += bmp.size;
            }
        }
        return size_temp;
    }


//    /**
//     * 质量压缩方法
//     *
//     * @param image
//     * @return
//     */
//    public static Bitmap compressImage(Bitmap image) {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 30, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, opts);// 把ByteArrayInputStream数据生成图片
//        image.recycle();
//        return bitmap;
//    }

    /**
     * 合并图片
     *
     * @param head
     * @return
     */
    public static Bitmap product_together_img(ArrayList<BitmapInfo> head) {
        int width = 0;
        int totaleight = 0;
        int[] heights = new int[head.size()];
        FileUitl.getTempFileWithCheck("");
        for (int i = 0; i < heights.length; i++) {
            totaleight += head.get(i).getHeight();
            heights[i] = head.get(i).getHeight();
        }

        if (head != null) {
            width = head.get(0).getWidth();
        }
        if (totaleight == 0) return null;
        //生成三个图片合并大小的Bitmap
        Bitmap newbmp = null;
        int size = totaleight*width;
        float scale = size*1f/max_bitmap_memory;
        if(scale < 1){
            scale = 1;
        }
        scale = (float) Math.sqrt(scale);
        boolean islow = false;
        while (newbmp == null) {
            try {
                newbmp = Bitmap.createBitmap((int)(width/scale), (int)(totaleight/scale), Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError e) {
                if (!islow) {
                    islow = true;
                    for (BitmapInfo bmp : head) {
                        bmp.low();
                    }
                } else {
                    scale*=1.1;
                }
            }
        }

        FFLogUtil.e("scale",scale+"");

        Canvas cv = new Canvas(newbmp);

        int nowHeight = 0;
        Matrix matrix = new Matrix();
        matrix.setScale(1/scale,1/scale);
        cv.setMatrix(matrix);
        for (int i = 0; i < heights.length; i++) {
            Bitmap bitmap = head.get(i).getBitmap();
            cv.drawBitmap(bitmap, 0, nowHeight, null);
            nowHeight += heights[i];
            bitmap.recycle();
            head.get(i).bitmap = null;
            if(head.get(i).path != null){
                new File(head.get(i).path).delete();
            }
        }
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        return newbmp;

    }

}
