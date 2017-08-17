package com.fan.framework.utils;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

import com.fan.framework.base.FFApplication;
import com.fengnian.smallyellowo.foodie.utils.ProductImgUtils;

import org.lasque.tusdk.core.utils.image.BitmapHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.fan.framework.config.FFConfig.IMAGE_QUALITY;

public class FFImageUtil {
    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap getGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * 保存方法
     */
    public static int[] saveBitmap(String path, Bitmap bitmap) {

        if (!BitmapHelper.saveBitmap(new File(path), bitmap, IMAGE_QUALITY)) {
            File f = new File(path);
            if (f.exists()) {
                f.delete();
            }
            try {
                FileOutputStream out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new int[]{bitmap.getWidth(), bitmap.getHeight()};
    }

    /**
     * 获得形图
     * @param bitmap 原始图片
     * @return 处理过的图片
     */
    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 根据路径加载指定大小范围的图片
     *
     * @param path 图片路径
     * @return 返回Bitmap
     */
    public static Bitmap bitmapFromPath(final String path, int maxWidth, int maxHeight) {

        Bitmap tempBitmap = BitmapFactory.decodeFile(path, getOpt(maxHeight, maxWidth, new ImageFilter() {
            @Override
            public void filt(Options bmpFactoryOptions) {
                BitmapFactory.decodeFile(path, bmpFactoryOptions);
            }
        }));

        Bitmap bitmap;
        tempBitmap = loadBitmap(path, tempBitmap);
        if (tempBitmap != null && maxHeight > 0 && maxWidth > 0 && (tempBitmap.getWidth() > maxWidth || tempBitmap.getHeight() > maxHeight)) {
            bitmap = Bitmap.createScaledBitmap(tempBitmap, getResizedDimension(maxWidth, maxHeight, tempBitmap.getWidth(), tempBitmap.getHeight()),
                    getResizedDimension(maxHeight, maxWidth, tempBitmap.getHeight(), tempBitmap.getWidth()), true);
            tempBitmap.recycle();
        } else {
            bitmap = tempBitmap;
        }

        return bitmap;
    }

    /**
     * 获得形图
     *
     * @param bitmap 原始图片
     * @return 处理过的图片
     */
    public static Bitmap getCornerBitmap(Bitmap bitmap) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = FFUtils.getPx(6);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * @return 返回Bitmap
     * @throws IOException
     */
    public static Bitmap bitmapFromByte(final byte[] bt, int maxWidth, int maxHeight) throws IOException {
        if (bt.length == 0) {
            return null;
        }

        Bitmap tempBitmap = BitmapFactory.decodeByteArray(bt, 0, bt.length, getOpt(maxHeight, maxWidth, new ImageFilter() {

            @Override
            public void filt(Options bmpFactoryOptions) {
                BitmapFactory.decodeByteArray(bt, 0, bt.length, bmpFactoryOptions);
            }
        }));

        Bitmap bitmap;

        if (tempBitmap != null && maxHeight > 0 && maxWidth > 0 && (tempBitmap.getWidth() > maxWidth || tempBitmap.getHeight() > maxHeight)) {
            bitmap = Bitmap.createScaledBitmap(tempBitmap, getResizedDimension(maxWidth, maxHeight, tempBitmap.getWidth(), tempBitmap.getHeight()),
                    getResizedDimension(maxHeight, maxWidth, tempBitmap.getHeight(), tempBitmap.getWidth()), true);
            tempBitmap.recycle();
        } else {
            bitmap = tempBitmap;
        }

        return bitmap;
    }

    /**
     * 创建日期2013-1-11 下午5:27:00 描述：把流转换成byte
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static final byte[] inputStreamToByteArray(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }

    public static Bitmap bitmapFromResource(final int imageUrl, int maxWidth, int maxHeight) {
        Bitmap tempBitmap = BitmapFactory.decodeResource(FFApplication.app.getResources(),imageUrl, getOpt(maxHeight, maxWidth, new ImageFilter() {

            @Override
            public void filt(Options bmpFactoryOptions) {
                BitmapFactory.decodeResource(FFApplication.app.getResources(),imageUrl, bmpFactoryOptions);
            }
        }));

        Bitmap bitmap;
//        tempBitmap = loadBitmap(path, tempBitmap);
        if (tempBitmap != null && maxHeight > 0 && maxWidth > 0 && (tempBitmap.getWidth() > maxWidth || tempBitmap.getHeight() > maxHeight)) {
            bitmap = Bitmap.createScaledBitmap(tempBitmap, getResizedDimension(maxWidth, maxHeight, tempBitmap.getWidth(), tempBitmap.getHeight()),
                    getResizedDimension(maxHeight, maxWidth, tempBitmap.getHeight(), tempBitmap.getWidth()), true);
            tempBitmap.recycle();
        } else {
            bitmap = tempBitmap;
        }
        return bitmap;
    }

    interface ImageFilter {
        void filt(Options bmpFactoryOptions);
    }

    public static int[] getImageSize(String path) {
        Options bmpFactoryOptions = new Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmpFactoryOptions);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // 读取图片中相机方向
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return new int[]{bmpFactoryOptions.outHeight, bmpFactoryOptions.outWidth};
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return new int[]{bmpFactoryOptions.outWidth, bmpFactoryOptions.outHeight};
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return new int[]{bmpFactoryOptions.outHeight, bmpFactoryOptions.outWidth};
                default:
                    return new int[]{bmpFactoryOptions.outWidth, bmpFactoryOptions.outHeight};
            }
        }
        return new int[]{bmpFactoryOptions.outWidth, bmpFactoryOptions.outHeight};
    }

    private static Options getOpt(int maxHeight, int maxWidth, ImageFilter filter) {
        Options decodeOptions = new Options();
        decodeOptions.inJustDecodeBounds = true;
        filter.filt(decodeOptions);
        int actualWidth = decodeOptions.outWidth;
        int actualHeight = decodeOptions.outHeight;
        if (maxHeight <= 0) {
            maxHeight = FFUtils.getDisHight();
        }
        if (maxWidth <= 0) {
            maxWidth = FFUtils.getDisWidth();
        }
//        int desiredWidth = getResizedDimension(maxWidth, maxHeight, actualWidth, actualHeight);
//        int desiredHeight = getResizedDimension(maxHeight, maxWidth, actualHeight, actualWidth);

        decodeOptions.inJustDecodeBounds = false;
        decodeOptions.inPreferredConfig = Config.ARGB_8888;
        decodeOptions.inSampleSize = findBestSampleSize(actualWidth, actualHeight, maxWidth, maxHeight);
        return decodeOptions;
    }

    /**
     * 从给定的路径加载图片，并指定是否自动旋转方向
     */
    public static Bitmap loadBitmap(String imgpath, Bitmap bm) {
        int digree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imgpath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        if (exif != null) {
            // 读取图片中相机方向
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
                default:
                    digree = 0;
                    break;
            }
        }
        if (digree != 0) {
            // 旋转图片
            Matrix m = new Matrix();
            m.postRotate(digree);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
        }
        return bm;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary, int actualPrimary, int actualSecondary) {
        if (maxPrimary <= 0 && maxSecondary <= 0) {
            return actualPrimary;
        }

        if (maxPrimary <= 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary <= 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    //当期望宽高跟实际宽高比不一致取比例更小的一组  相当于centerCrop
    static int findBestSampleSize(int actualWidth, int actualHeight, int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);//centerCrop
//        double ratio = Math.max(wr, hr);//centerInside
        float n = 1.0f;
        while ((n + 1) <= ratio) {
            n += 1;
        }
        while((actualHeight*actualWidth)/n/n > (3*1024*1024)){//图片大小不得大于3M
            n+=1;
        }

        return (int) n;
    }
}