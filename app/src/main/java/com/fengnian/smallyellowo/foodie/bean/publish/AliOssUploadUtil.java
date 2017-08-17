package com.fengnian.smallyellowo.foodie.bean.publish;

import android.graphics.Bitmap;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.fan.framework.imageloader.NativeAndNetImageMapManager;
import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.FileUitl;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.publics.SYUploadImage;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016-10-20.
 */

public class AliOssUploadUtil {

    private OSSClient oss;

    private static AliOssUploadUtil instance;

    ExecutorService es = Executors.newScheduledThreadPool(3);

    public static AliOssUploadUtil getInstance() {
        if (instance == null) {
            instance = new AliOssUploadUtil();
        }
        return instance;
    }

    private AliOssUploadUtil()

    {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constants.shareConstants().accessKey, Constants.shareConstants().secretKey);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        // oss为全局变量，OSS_ENDPOINT是一个OSS区域地址
        oss = new OSSClient(APP.app, Constants.shareConstants().endPoint, credentialProvider, conf);
    }


    public void ossUpload(final SYUploadImage img) {
        es.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    syncUpload(img);
                } catch (Exception e) {
                    FFLogUtil.e("lll", e);
                }
            }
        });
    }

    private void syncUpload(final SYUploadImage img) {
        if (img == null || img.getImage() == null || img.getImage().getUrl() == null) {
            img.uploadFail();
            return;
        }

        if (img.getImage().getUrl().startsWith("http")) {
            img.getImage().setThumbUrl(img.getImage().getUrl());
            img.uploadSuccess();
            return;
        }
        synchronized (img) {
            if (img.getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOADING) {
                return;
            } else if (img.getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                img.uploadSuccess();
                return;
            }
        }
//        FFLogUtil.e("上传1", img.getImage().getUrl() + "   " + img.hashCode());
        img.uploadStart();

        // 判断图片是否全部上传
        // 如果已经是最后一张图片上传成功，则跳出

        // 指定数据类型，没有指定会自动根据后缀名判断
        ObjectMetadata objectMeta = new ObjectMetadata();


        String url = img.getImage().getUrl();
        final String path = FileUitl.getTempFileWithCheck(url) + ".jpg";

        Bitmap bitmap = FFImageUtil.bitmapFromPath(url, FFUtils.getDisWidth(), FFUtils.getDisHight());
        try {
            FFImageUtil.saveBitmap(path, bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.recycle();

        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(Constants.shareConstants().bucketName, img.getImage().getId() + ".jpg", path);
        put.setMetadata(objectMeta);

//        FFLogUtil.e("上传2", img.getImage().getUrl() + "   " + img.hashCode());
//        // 异步上传时可以设置进度回调
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//            }
//        });

        synchronized (img) {
            OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    synchronized (img) {
                        //                FFLogUtil.e("发布", "成功了一个图片" + img.getImage().getUrl() + "   " + img.hashCode());
                        if (img.getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
//                            img.uploadFail();
                            return;
                        }
                        String url = Constants.shareConstants().header_image + request.getObjectKey();
                        NativeAndNetImageMapManager.onImageUploadSuccessed(img.getImage().getUrl(), url);
                        img.getImage().setUrl(url);
                        img.getImage().setThumbUrl(img.getImage().getUrl());
                        img.getImage().setPreviewUrl(img.getImage().getUrl());
                        img.uploadSuccess();
                        img.notify();
                    }
                    new File(path).delete();
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                    synchronized (img) {
                        if (img.getUploadStatus() == SYUploadImage.UPLOAD_STATUS_UPLOAD_FAIL) {
//                            img.uploadFail();
                            return;
                        }
                        img.uploadFail();
                        img.notify();
                        new File(path).delete();

//                FFLogUtil.e("发布", "失败了一个图片" + img.getImage().getUrl() + "   " + img.hashCode());
                        return;
                    }
                }
            });

            try {
//                FFLogUtil.d("阻塞","阻塞前");
                img.wait(120 * 1000);
                if (img.getUploadStatus() != SYUploadImage.UPLOAD_STATUS_UPLOAD_SUCCESS) {
                    img.uploadFail();
                    task.cancel();
                }
//                FFLogUtil.d("阻塞","阻塞后");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            FFLogUtil.d("图片上传","传完了一个图片");
        }

    }


}
