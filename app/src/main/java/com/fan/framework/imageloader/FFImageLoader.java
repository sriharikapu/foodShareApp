package com.fan.framework.imageloader;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.fan.framework.base.FFContext;
import com.fan.framework.config.FFConfig;
import com.fan.framework.lru.DiskLruCache;
import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFThreadPool;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.FileUitl;
import com.fan.framework.widgets.ZoomImageView;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static com.fan.framework.base.FFApplication.runOnUiThread;

/**
 * 创建日期2013-1-21下午3:29:55<br/>
 * 作用描述：异步载入网络下发的图片
 */
@SuppressLint("NewApi")
public class FFImageLoader {

    /**
     * 无
     */
    public static final int TYPE_NONE = 0;
    public static final int TYPE_ROUND = 0xFF;
    public static final int TYPE_ROUND_CORNER = 0xF0;
    public static final int TYPE_BLACK_AND_WHITE = 0XF00;

    public final static LruCache<String, Bitmap> map = new LruCache<String, Bitmap>(FFConfig.IMAGE_CATCHS) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            if (bitmap != null) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            } else {
                return 0;
            }
        }
    };

    private static ExecutorService es_sd;

    private static ExecutorService es_net;
    private static boolean isDiskCacheUseable = false;

    private static DiskLruCache diskCache;

    public static void init(boolean hasSdPermission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            es_sd = new FFThreadPool(FFConfig.THREADS, FFConfig.THREADS, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
            es_net = new FFThreadPool(FFConfig.THREADS, FFConfig.THREADS, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
        } else {
            es_sd = Executors.newFixedThreadPool(FFConfig.THREADS);
            es_net = Executors.newFixedThreadPool(FFConfig.THREADS);
        }
        if (hasSdPermission) {
            try {
                diskCache = DiskLruCache.open(new File(FileUitl.getCacheFileDir()), FFUtils.getVerCode(), 1, FFConfig.maxSDCacheSize);
                isDiskCacheUseable = true;
            } catch (IOException e) {
                e.printStackTrace();
                isDiskCacheUseable = false;
            }
        } else {
            isDiskCacheUseable = false;
        }

    }

    public static long getCacheMemory() {
        return diskCache.size();
    }


    private static final FFImageRequests queue_sd = new FFImageRequests();
    private static final FFImageRequests queue_net = new FFImageRequests();
    private static final String TAG = "FFImageLoader";

    /**
     * @param activity  当前请求所在activity
     * @param imageUrl  图片网络地址
     * @param imageView 图片要显示的目标imageView
     */
    public static FFImageRequest loadAvatar(FFContext activity, Object imageUrl, ImageView imageView) {
        return load_base(activity, imageUrl, imageView, true, Constants.AvatarImage, Constants.AvatarImage, R.mipmap.moren_head_img, TYPE_ROUND, null);
    }


    /**
     * 多一个回调
     *
     * @param activity
     * @param imageUrl
     * @param imageView
     * @param callBack
     * @return
     */
    public static FFImageRequest loadAvatar(FFContext activity, Object imageUrl, ImageView imageView, FFImageCallBack callBack) {
        return load_base(activity, imageUrl, imageView, true, Constants.AvatarImage, Constants.AvatarImage, R.mipmap.moren_head_img, TYPE_ROUND, callBack);
    }

    /**
     * @param activity  当前请求所在activity
     * @param imageUrl  图片网络地址
     * @param imageView 图片要显示的目标imageView
     */
    public static FFImageRequest loadSmallImage(FFContext activity, Object imageUrl, ImageView imageView) {
        return load_base(activity, imageUrl, imageView, true, Constants.SmallImage, Constants.SmallImage, R.drawable.alpha, TYPE_NONE, null);
    }

    /**
     * @param activity  当前请求所在activity
     * @param imageUrl  图片网络地址
     * @param imageView 图片要显示的目标imageView
     */
    public static FFImageRequest loadMiddleImage(FFContext activity, Object imageUrl, ImageView imageView) {
        return load_base(activity, imageUrl, imageView, true, Constants.MiddleImage, Constants.MiddleImage, R.drawable.alpha, TYPE_NONE, null);
    }

    /**
     * @param activity  当前请求所在activity
     * @param imageUrl  图片网络地址
     * @param imageView 图片要显示的目标imageView
     */
    public static FFImageRequest loadNativeImage(FFContext activity, Object imageUrl, ImageView imageView, int width, int height, int defalut) {
        return load_base(activity, imageUrl, imageView, true, width, height, defalut, TYPE_NONE, null);
    }

    /**
     * @param activity  当前请求所在activity
     * @param imageUrl  图片网络地址
     * @param imageView 图片要显示的目标imageView
     */
    public static FFImageRequest loadBigImage(FFContext activity, Object imageUrl, ImageView imageView) {
        return load_base(activity, imageUrl, imageView, true, Constants.BigImage, Constants.BigImage, R.drawable.alpha, TYPE_NONE, null);
    }

    /**
     * @param activity  当前请求所在activity
     * @param imageUrl  图片网络地址
     * @param imageView 图片要显示的目标imageView
     */
    public static FFImageRequest loadOriginalImage(FFContext activity, Object imageUrl, ImageView imageView) {
        return load_base(activity, imageUrl, imageView, true, Constants.OriginalImage, Constants.OriginalImage, R.drawable.alpha, TYPE_NONE, null);
    }

    /**
     * @param activity  当前请求所在activity
     * @param imageUrl  图片网络地址
     * @param imageView 图片要显示的目标imageView
     */
    public static FFImageRequest loadBigImage(FFContext activity, Object imageUrl, ImageView imageView, int defaultResource) {
        return load_base(activity, imageUrl, imageView, true, Constants.BigImage, Constants.BigImage, defaultResource, TYPE_NONE, null);
    }
//
//    /**
//     * @param activity  当前请求所在activity
//     * @param imageUrl  图片网络地址
//     * @param imageView 图片要显示的目标imageView
//     */
//    public static FFImageRequest loadImage(FFContext activity, String imageUrl, ImageView imageView, int width, int height) {
//        return load_base(activity, imageUrl, imageView, true, width, height, R.drawable.alpha, TYPE_NONE, null);
//    }

//    /**
//     * @param activity  当前请求所在activity
//     * @param imageUrl  图片网络地址
//     * @param imageView 图片要显示的目标imageView
//     */
//    public static FFImageRequest loadImage(FFContext activity, String imageUrl, ImageView imageView, int width, int height, int defalutRes) {
//        return load_base(activity, imageUrl, imageView, true, width, height, defalutRes, TYPE_NONE, null);
//    }

    /**
     * @param activity     当前请求所在activity
     * @param imageUrl     图片网络地址
     * @param imageView    图片要显示的目标imageView
     * @param doCache      是否缓存当前图片到sd卡 如果设为false则仅从缓存中获取
     * @param width        图片的最大宽度 如果不需要限制 请设置为-1
     * @param height       图片的最大高度 如果不需要限制 请设置为-1
     * @param defaultResId 如果缓存中不存在该图片默认显示的图片id 如果不需要默认显示图片请设为0
     * @param type
     * @param callBack     回调方法
     */
    public static FFImageRequest load_base(FFContext activity, Object imageUrl, ImageView imageView, boolean doCache, int width, int height, int defaultResId, int type, FFImageCallBack callBack) {
        FFImageRequest request = new FFImageRequest(imageUrl, imageView, doCache, width, height, callBack, activity, type, defaultResId);
        loadDrawable(request);
        return request;
    }

    /**
     * 创建日期2012-12-26 下午12:47:57 描述：按照后进先出方式加载所有图片
     *
     * @param request
     * @return
     */
    private static void loadDrawable(final FFImageRequest request) {
//        FFLogUtil.e("-------", request.getImageUrl() + "开始加载");
        if (request.getImageUrl() == null) {
            request.setImageUrl("");
        }

        if (loadFromCache(request)) {
            return;
        }
        if (request.isNeedRefresh()) {
            if (request.getDefaultResId() != 0) {
                setDefultResource(request);
            } else {
                ImageView imageView = request.getImageView();
                if (imageView != null) {
                    imageView.setImageBitmap(null);
                }
            }
        }
        if (request.getImageUrl() instanceof Integer) {
            loadFromResurce(request);
        } else {
            if (request.isDoCache() && loadFromSDCard(request)) {
                return;
            }
            if (((String) request.getImageUrl()).startsWith("http")) {
                loadFromNet(request);
            } else {
                endLoad(request, null, false);
            }
        }
        return;
    }

    private static boolean loadFromResurce(final FFImageRequest request) {
        final int imageUrl = (Integer) request.getImageUrl();
        synchronized (queue_sd) {
            if (queue_sd.containsKey(imageUrl)) {
                queue_sd.add(imageUrl, request);
                return true;
            }
            queue_sd.add(imageUrl, request);
        }
        getEsSD().submit(new Runnable() {
            public void run() {
                synchronized (queue_sd) {
                    if (FFImageLoader.isAllFinished(request, queue_sd)) {
                        return;
                    }
                }
                final Bitmap bitmap_normal = FFImageUtil.bitmapFromResource(imageUrl, request.getWidth(), request.getHeight());
                originalBitmapLoaded(request, queue_sd, bitmap_normal);
            }
        });
        return true;
    }

    public static void delCache(Bitmap bitmap) {
    }

    public static ExecutorService getEsNet() {
        if (es_net == null) {
            init(true);
        }
        return es_net;
    }

    public static ExecutorService getEsSD() {
        if (es_sd == null) {
            init(true);
        }
        return es_sd;
    }

    private static class ProgressCallback {
        private ArrayList<FFImageRequest> list;

        public ProgressCallback(ArrayList<FFImageRequest> list) {
            this.list = list;

        }


        public void onProgress(int progress, int totail) {
            for (FFImageRequest request : list) {
                if (request.getCallBack() != null) {
                    request.getCallBack().onProgress(progress, totail);
                }
                if (request.getImageView() != null && request.getImageView() instanceof PercentImageView) {
                    ((PercentImageView) request.getImageView()).onProgress(progress, totail);
                }
            }
        }
    }


    /**
     * 从网络加载图片
     *
     * @param request
     * @return 返回图片
     */
    public static Bitmap loadImageFromUrl(FFImageRequest request, String url, int width, int height, ProgressCallback callback) {
        final String path = getFile(url);
        boolean sdFreeEnough = FileUitl.isSdFreeEnough();
        Bitmap bitmap = null;
        int retryTimes = 0;
        while ((bitmap == null && retryTimes < 3)) {
            retryTimes++;
            DiskLruCache.Editor editor = null;
            if (isDiskCacheUseable) {
                try {
                    editor = diskCache.edit(FileUitl.fileNameFromURL(url));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                URL u = new URL(url);
                URLConnection conn = u.openConnection();
                conn.setConnectTimeout(10000);
                conn.connect();
                if (callback != null) {
                    callback.onProgress(0, 10);
                }
                InputStream is = conn.getInputStream();
                int fileSize = conn.getContentLength();
                int downloadSize = 0;
                if (fileSize < 1 || is == null) {
                    request.setStatus(404);
                } else {
                    if (callback != null) {
                        callback.onProgress(0, fileSize);
                    }
                    if (FileUitl.isSDCardAvailable() && sdFreeEnough) {
                        OutputStream fos = editor.newOutputStream(0);
                        byte[] bytes = new byte[4 * 1024];
                        int len;
                        while ((len = is.read(bytes)) != -1) {
                            fos.write(bytes, 0, len);
                            downloadSize += len;
                            if (callback != null) {
                                callback.onProgress(downloadSize, fileSize);
                            }
                        }
                        is.close();
                        fos.close();
                        if (editor != null) {
                            try {
                                editor.commit();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        bitmap = FFImageUtil.bitmapFromPath(path + ".0", width, height);
                    } else {
                        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                        byte[] buff = new byte[4 * 1024];
                        int rc = 0;
                        while ((rc = is.read(buff)) > 0) {
                            swapStream.write(buff, 0, rc);
                            downloadSize += rc;
                            if (callback != null) {
                                callback.onProgress(downloadSize, fileSize);
                            }
                        }
                        is.close();
                        swapStream.close();
                        bitmap = FFImageUtil.bitmapFromByte(swapStream.toByteArray(), width, height);
                        if (callback != null) {
                            callback.onProgress(100, 100);
                        }
                    }

                    if (bitmap != null) {
                        request.setStatus(200);
                    }
                }

            } catch (Exception e) {
                if (e instanceof FileNotFoundException) {
                    request.setStatus(404);
                    break;
                }
                e.printStackTrace();
                if (editor != null) {
                    try {
                        editor.abort();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (retryTimes > 3) {
                    return bitmap;
                }
            }
        }
        return bitmap;
    }

    /**
     * 根据图片url获取文件
     *
     * @param url
     * @return
     */
    public static String getFile(String url) {
        return FileUitl.getCacheFileWithCheck(url);
    }

    /**
     * 根据图片url获取文件
     *
     * @param imageUrl
     * @return
     */
    public static String findFile(String imageUrl) {
        if (imageUrl.startsWith("http")) {
            String path = NativeAndNetImageMapManager.getUrl((String) imageUrl, Constants.BigImage, Constants.BigImage);//如果有本地映射
            if (!path.startsWith("http") && new File(path).exists()) {//返回映射
                return path;
            } else {//没有映射
                int width = Constants.BigImage;
                int height = Constants.BigImage;
                if (width <= 0) {
                    width = FFUtils.getDisWidth();
                }
                if (height <= 0) {
                    height = FFUtils.getDisHight();
                }
                if (imageUrl != null && (((String) imageUrl).startsWith(Constants.shareConstants().publishEnvironmentHeaderOriginal) || ((String) imageUrl).startsWith(Constants.shareConstants().testEnvironmentHeaderOriginal))) {
                    imageUrl = imageUrl + "?x-oss-process=image/resize,m_mfit,h_" + height + ",w_" + width + ",limit_1/format,jpg/auto-orient,1/quality,Q_80";
                }
                DiskLruCache.Snapshot snapShot;
                try {
                    snapShot = diskCache.get(FileUitl.fileNameFromURL(imageUrl));
                    if (snapShot == null) {
                        return null;
                    }
                } catch (IOException e) {
                    return null;
                }
                return snapShot.getPaths()[0].getPath();
            }
        } else {
            return imageUrl;
        }
    }

    /**
     * 根据图片url获取文件
     *
     * @param url
     * @return
     */
    public static String getTempFile(String url) {
        return FileUitl.getTempFileWithCheck(url);
    }

    /**
     * 结束加载
     *
     * @param request
     * @param isCallback
     */
    private static void endLoad(final FFImageRequest request, final Bitmap s, final boolean isCallback) {
//        if (checkTag(request)) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    endLoad_onUiThread(request, s, isCallback);
                }
            });
        } else {
            endLoad_onUiThread(request, s, isCallback);
        }
//        }
    }

    /**
     * 结束加载
     *
     * @param request
     * @param bitmap
     * @param isCallback
     */
    private static void endLoad_onUiThread(final FFImageRequest request, final Bitmap bitmap, boolean isCallback) {
        if (!checkTag(request)) {
            return;
        }
        ImageView imageView = request.getImageView();
        if (imageView != null) {
            if (bitmap == null) {
                int failedResId = request.getFailedResId(request.getStatus());
                if (failedResId != 0) {
                    imageView.setImageResource(failedResId);
                } else if (request.getDefaultResId() != 0) {
                    imageView.setImageResource(request.getDefaultResId());
                } else {
                    imageView.setImageBitmap(null);
                }
                if (imageView instanceof PercentImageView) {
                    ((PercentImageView) imageView).onFaile();
                }
            } else {
                if (isCallback) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
        if (request.getCallBack() != null) {
            request.getCallBack().imageLoaded(bitmap, request.getImageUrl().toString());
        }
    }

    /**
     * 结束加载
     *
     * @param request
     */
    private static void setDefultResource(final FFImageRequest request) {
        if (request.getImageView() != null) {
            request.getImageView().setImageResource(request.getDefaultResId());
        }
    }

    /**
     * 检查当前请求的图片是否已经改变目标图片
     *
     * @param request
     * @return
     */
    private static boolean checkTag(FFImageRequest request) {
        if (request.getImageView() != null) {
            return request.getImageUrl().equals(request.getImageView().getTag(R.id.ff_tag_imageLoader));
        }

        if (request.getCallBack() != null) {
            return request.getImageUrl().equals(request.getCallBack().getUrl());
        }
        return true;
    }

    /**
     * 尝试从缓存中加载
     *
     * @param request
     * @return
     */
    private static boolean loadFromCache(FFImageRequest request) {
        Bitmap s = map.get(request.getCacheKey());
        if (s != null) {
            endLoad(request, s, false);
//            FFLogUtil.i(TAG, "从缓存中加载到图片");
            return true;
        }
//        FFLogUtil.i(TAG, "缓存中没有图片");
        return false;
    }

    /**
     * 添加一个从sd卡加载图片的任务
     *
     * @param request
     * @return 是否有此文件
     */
    private static boolean loadFromSDCard(final FFImageRequest request) {
        File tempFile;
        final String imageUrl = (String) request.getImageUrl();
        if (!imageUrl.startsWith("http")) {
            tempFile = new File(imageUrl);
        } else if (isDiskCacheUseable) {
            DiskLruCache.Snapshot snapShot;
            try {
                snapShot = diskCache.get(FileUitl.fileNameFromURL(imageUrl));
                if (snapShot == null) {
                    return false;
                }
            } catch (IOException e) {
                return false;
            }
            tempFile = snapShot.getPaths()[0];
        } else {
            return false;
        }
        final File file = tempFile;
        boolean exists = file.exists();
        if (exists && !queue_net.containsKey(imageUrl)) {
            synchronized (queue_sd) {
                if (queue_sd.containsKey(imageUrl)) {
                    queue_sd.add(imageUrl, request);
                    return true;
                }
                queue_sd.add(imageUrl, request);
            }
            getEsSD().submit(new Runnable() {
                public void run() {
                    synchronized (queue_sd) {
                        if (FFImageLoader.isAllFinished(request, queue_sd)) {
                            return;
                        }
                    }
                    final Bitmap bitmap_normal = FFImageUtil.bitmapFromPath(file.getPath(), request.getWidth(), request.getHeight());
                    if (bitmap_normal == null && imageUrl.startsWith("http")) {
                        file.delete();
                        APP.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                FFImageLoader.loadFromNet(request);
                            }
                        });
                        return;
                    }
                    originalBitmapLoaded(request, queue_sd, bitmap_normal);
                }
            });
            return true;
        }
        // }
        return false;
    }

    /**
     * 添加一个从网络加载图片的任务
     *
     * @param request
     */
    private static void loadFromNet(final FFImageRequest request) {
//        FFLogUtil.i(TAG, "从网络加载图片");
        final String imageUrl = (String) request.getImageUrl();
        synchronized (queue_net) {
            if (request.getCallBack() != null) {
                request.getCallBack().onProgress(0, 100);
            }
            if (queue_net.containsKey(imageUrl)) {
//                FFLogUtil.i(TAG, "已包含，不再重新加载"+request.getImageUrl());
                queue_net.add(imageUrl, request);
                return;
            }
            queue_net.add(imageUrl, request);
        }
        getEsNet().submit(new Runnable() {
            public void run() {
                int width;
                int height;
                synchronized (queue_net) {
                    if (FFImageLoader.isAllFinished(request, queue_net)) {
//                        FFLogUtil.i(TAG, "都结束了不再加载"+request.getImageUrl());
                        return;
                    }
                    width = queue_net.getWidth(request);
                    height = queue_net.getHeight(request);
                }
                final Bitmap bitmap = loadImageFromUrl(request, imageUrl, width, height, new ProgressCallback(queue_net.getQueue(imageUrl)));
                originalBitmapLoaded(request, queue_net, bitmap);
            }
        });
    }


    /**
     * 原图加载成功
     */
    private static void originalBitmapLoaded(FFImageRequest request, FFImageRequests queue, Bitmap originalBitmap) {
        synchronized (queue) {
            ArrayList<FFImageRequest> list = queue.getQueue(request.getImageUrl());
            for (FFImageRequest ffImageRequest : list) {
                if (map.get(ffImageRequest.getCacheKey()) == null) {
                    Bitmap bMap = getEffectsBitmap(originalBitmap, ffImageRequest);
                    if (bMap != null) {
                        map.put(ffImageRequest.getCacheKey(), bMap);
                    }
                }
                ffImageRequest.setStatus(request.getStatus());
                if (ffImageRequest.getActivity() != null && ffImageRequest.getActivity().getDestroyed()) {
                    continue;
                }
                endLoad(ffImageRequest, map.get(ffImageRequest.getCacheKey()), true);
            }
            queue.remove(request.getImageUrl());
        }
    }

    private static Bitmap getEffectsBitmap(Bitmap originalBitmap, FFImageRequest request) {
        if (originalBitmap == null) {
            return null;
        }
        switch (request.getType()) {
            case TYPE_NONE:
                return originalBitmap;
            case TYPE_ROUND:
                return FFImageUtil.getRoundedBitmap(originalBitmap);
            case TYPE_ROUND_CORNER:
                return FFImageUtil.getCornerBitmap(originalBitmap);
            case TYPE_BLACK_AND_WHITE:
                return FFImageUtil.getGrayscale(originalBitmap);
        }
        return null;
    }


    /**
     * 清除内存中的图片缓存
     */
    public static void cleanCache() {
        map.trimToSize(0);
        if (isDiskCacheUseable) {
            try {
                diskCache.delete();
                diskCache = DiskLruCache.open(new File(FileUitl.getCacheFileDir()), FFUtils.getVerCode(), 1, FFConfig.maxSDCacheSize);
                isDiskCacheUseable = true;
            } catch (IOException e) {
                e.printStackTrace();
                isDiskCacheUseable = false;
            }
        }
    }

    private static boolean isAllFinished(FFImageRequest request, FFImageRequests queue) {
        ArrayList<FFImageRequest> list = queue.getQueue(request.getImageUrl());
        for (FFImageRequest ffImageRequest : list) {
            boolean b = ffImageRequest.getActivity() == null || !ffImageRequest.getActivity().getDestroyed();
            boolean b1 = checkTag(ffImageRequest);
            if (b && b1) {
                return false;
            }
        }
        queue.remove(request.getImageUrl());
        return true;
    }

    private static boolean hasType(FFImageRequest request, FFImageRequests queue, int type) {
        ArrayList<FFImageRequest> list = queue.getQueue(request.getImageUrl());
        for (FFImageRequest ffImageRequest : list) {
            if ((ffImageRequest.getType() & type) == ffImageRequest.getType()) {
                if (type == TYPE_ROUND_CORNER && ((ffImageRequest.getType() & type) == ffImageRequest.getType())) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
//
//    private static boolean isTrimimg = false;
//
//    private static int cleanCache() {
//        if (isTrimimg) {
//            return 0;
//        }
//        File file_parent = new File(FileUitl.getCacheFileDir());
//        File[] f = file_parent.listFiles();
//        if (f == null) {
//            return 0;
//        }
//        isTrimimg = true;
//        long[] time = new long[f.length];
//        int sum = 0;
//        for (int i = 0; i < f.length; i++) {
//            sum += f[i].length();
//            time[i] = f[i].lastModified();
//        }
//        if (sum == 0) {
//            isTrimimg = false;
//            return sum;
//        }
//        if (maxSize != 0) {
//            for (int i = 0; i < f.length; i++) {
//                for (int j = 0; j < f.length; j++) {
//                    if (time[i] < time[j]) {
//                        File file = f[i];
//                        f[i] = f[j];
//                        f[j] = file;
//
//                        long temp = time[i];
//                        time[i] = time[j];
//                        time[j] = temp;
//                    }
//                }
//            }
//        }
//        for (File file : f) {
//            if (sum < maxSize) {
//                break;
//            }
//            if (file.isDirectory()) {
//                continue;
//            }
//            sum -= file.length();
//            file.delete();
//        }
//        isTrimimg = false;
//        return sum;
//    }
}
