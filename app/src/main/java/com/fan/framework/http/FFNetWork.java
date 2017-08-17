package com.fan.framework.http;

import android.net.TrafficStats;
import android.os.Process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.fan.framework.base.FFApplication;
import com.fan.framework.base.FFContext;
import com.fan.framework.base.FFHttpCache;
import com.fan.framework.config.FFConfig;
import com.fan.framework.utils.FFLogUtil;
import com.fan.framework.utils.FFUtils;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.BasicHttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class FFNetWork {
    protected ExecutorService es1 = null;
    private String TAG = "FFNetWork";

    //当前请求的主体   activity或者fragment
    private final FFContext ffContext;


    public FFNetWork(FFContext ffContext) {
        this.ffContext = ffContext;
        es1 = Executors.newFixedThreadPool(3);
        TAG = ffContext == null ? TAG
                : (ffContext.getClass().getSimpleName() + TAG);
    }

    public static String instreamToString(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write(input, output);
        output.close();
        return new String(output.toByteArray());
    }

    public static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
        int len;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer)) != -1)
            outputStream.write(buffer, 0, len);
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 停止所有请求
     */
    public void stopAll() {
        es1.shutdown();
    }

    /**
     * 当前对象
     */
    public void onDestory() {
        stopAll();
    }

    /**
     * 本实例所对应的Activity是否已经finished
     *
     * @return
     */
    public boolean getDestroyed() {
        if (ffContext == null) {
            return false;
        }
        return ffContext.getDestroyed();
    }

    public <T extends FFBaseBean> FFNetWorkRequest<T> get(String url,
                                                          String words, FFExtraParams extra, FFNetWorkCallBack<T> callBack,
                                                          Object... params) {
        if (params != null && params.length % 2 == 1) {
            throw new RuntimeException("网络请求传入了单数个参数");
        }
        Object[] params1 = FFConfig.getParams(url);
        TreeMap<Object, Object> map = new TreeMap<>();
        params = paramAddPublicParams(params1, params, map);
        url = url + FFNetWorkUtils.getGetString(params);
        final FFNetWorkRequest<T> request = new FFNetWorkRequest<T>(this, words, url,
                callBack, null, null, extra, map);
        excute(request);
        return request;
    }

    public static Object[] paramAddPublicParams(Object[] params1, Object[] params, TreeMap<Object, Object> map) {
        //TreeMap可同时为参数排序
        for (int i = 0; i < params1.length; i += 2) {
            map.put(params1[i], params1[i + 1]);
        }
        for (int i = 0; i < params.length; i += 2) {//手动填写的优先参数覆盖公共参数
            map.put(params[i], params[i + 1]);
        }
        Object[] temp = new Object[map.size() * 2];
        Iterator iter = map.entrySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            temp[i++] = key;
            temp[i++] = val;
        }
        return temp;
    }

    /**
     * 网络请求方法<br/>
     * 基于 URLConnection 为方便使用处理了一些常见的网络异常代码<br/>
     * 但由于精力有限，只做了几个，包括400-499和500-599两个区间的错误码<br/>
     * 本框架将会统一处理网络错误代码（以吐司的形式，也可以自行修改，只需要实现
     * {@link FFNetWorkCallBack#onFail(FFExtraParams)}并return true）
     *
     * @param url
     * @param words
     * @param extra
     * @param callBack
     * @param params
     * @return
     */
    public <T extends FFBaseBean> FFNetWorkRequest<T> post(String url,
                                                           String words, FFExtraParams extra, FFNetWorkCallBack<T> callBack,
                                                           Object... params) {
        if (params != null && params.length % 2 == 1) {
            throw new RuntimeException("网络请求传入了单数个参数");
        }
        Object[] params1 = FFConfig.getParams(url);
        TreeMap<Object, Object> map = new TreeMap<>();
        params = paramAddPublicParams(params1, params, map);
        final FFNetWorkRequest<T> request = new FFNetWorkRequest<T>(this, words, url,
                callBack, null, params, extra, map);
        excute(request);
        return request;
    }

    public <T extends FFBaseBean> FFNetWorkRequest<T> upload(String url,
                                                             String words, FFExtraParams extra, FFNetWorkCallBack<T> callBack,
                                                             HashMap<String, File> map, Object... params) {
        if (params != null && params.length % 2 == 1) {
            throw new RuntimeException("网络请求传入了单数个参数");
        }
        Object[] params1 = FFConfig.getParams(url);
        TreeMap<Object, Object> map1 = new TreeMap<>();
        params = paramAddPublicParams(params1, params, map1);
        final FFNetWorkRequest<T> request = new FFNetWorkRequest<T>(this, words, url,
                callBack, map, params, extra, map1);
        excute(request);
        return request;
    }

    private <T extends FFBaseBean> void excuteHttp(FFNetWorkRequest<T> request)
            throws SocketTimeoutException,
            ConnectException, UnsupportedEncodingException, IOException {
        FFLogUtil.e("请求网址" + request.hashCode(), request.getUrl());
        if (!FFUtils.checkNet()) {
            request.setStatus(FFResponseCode.ERROR_NATIVE_NET_CLOST, "网络未连");
            return;
        }
        long downloadBytesOriginal = TrafficStats.getUidRxBytes(Process.myUid());
        long uploadBytesOriginal = TrafficStats.getUidTxBytes(Process.myUid());
        URL url = new URL(request.getUrl());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        setPublicRequestHeaders(conn, request);
        if (null == request.getParams() && request.getFileMaps().size() <= 0) {
            makeGetRequest(conn);
        } else if (request.getFileMaps().size() > 0) {
            makeUploadReuest(request, conn);
        } else {
            makePostRequest(request, conn);
        }
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            performResponse(request, downloadBytesOriginal,
                    uploadBytesOriginal, conn);
        } else if (responseCode == 404) {
            request.setStatus(FFResponseCode.ERROR_NET_404, "状态码404");
        } else if (responseCode == 505) {
            request.setStatus(FFResponseCode.ERROR_SITE_505, "状态码505");
        } else {
            request.setStatus(FFResponseCode.ERROR_SITE_XXX, "状态码"
                    + responseCode);
        }
    }

    private <T extends FFBaseBean> void performResponse(
            FFNetWorkRequest<T> request, long downloadBytesOriginal,
            long uploadBytesOriginal, HttpURLConnection conn)
            throws IOException {
        String responseString = getResponse(conn);
        request.getExtraParams().setReponseString(responseString);
        FFLogUtil.e("返回数据" + request.hashCode(), responseString);
        showRequestMessage(downloadBytesOriginal, uploadBytesOriginal,
                responseString);
        T response;
        try {
            response = JSON.parseObject(responseString, request.getClazz());
        } catch (Exception e) {
            throw new JSONException(e.getMessage());
        }
        FFLogUtil.e("返回数据" + request.hashCode(), "json解析完成");
        if (!((FFBaseBean) response).judge()) {
            request.setStatus(FFResponseCode.ERROR_CONTEXT,
                    ((FFBaseBean) response).getErrorMessage());
            request.setEntity(response, responseString);
        } else {
            request.setStatus(FFResponseCode.SUCCESS, "请求成功");
            request.setEntity(response, responseString);
        }
    }

    private String getResponse(HttpURLConnection conn) throws IOException {
        String contentEncoding = conn.getContentEncoding();
        InputStream inputStream = null;
        try {
            inputStream = conn.getInputStream();
        } catch (IOException ioe) {
            inputStream = conn.getErrorStream();
        }
        if (contentEncoding != null && contentEncoding.equals("gzip")) {
            inputStream = new GZIPInputStream(inputStream);
        }
        String responseString = instreamToString(inputStream);
        return responseString;
    }

    private void showRequestMessage(long downloadBytesOriginal,
                                    long uploadBytesOriginal, String responseString) {
//        FFLogUtil.e("返回数据"+, responseString);
        // long downloadBytes = TrafficStats.getUidRxBytes(Process.myUid())
        // - downloadBytesOriginal;
        // long uploadBytes = TrafficStats.getUidTxBytes(Process.myUid()) -
        // uploadBytesOriginal;
        // String msg =
        // "本次请求使用流量使用情况\n上传:"+FFNetWorkUtils.getBytes(uploadBytes)+"\n下载:"+FFNetWorkUtils.getBytes(downloadBytes)+"\n总计:"+FFNetWorkUtils.getBytes(downloadBytes+uploadBytes);
        // FFLogUtil.i(TAG, msg);
        // FFApplication.showToast(null, msg);
    }

    private BasicHttpEntity getRequestEntity(HttpURLConnection conn) {
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream;
        try {
            inputStream = conn.getInputStream();
        } catch (IOException ioe) {
            inputStream = conn.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(conn.getContentLength());
        entity.setContentEncoding(conn.getContentEncoding());
        entity.setContentType(conn.getContentType());
        return entity;
    }

    private <T extends FFBaseBean> void setPublicRequestHeaders(
            HttpURLConnection conn, FFNetWorkRequest<T> request) {
        conn.setConnectTimeout(FFConfig.getNetTimeOut());
        conn.setReadTimeout(FFConfig.getNetTimeOut());
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("User-Agen", "android");
        conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
    }

    private void setRequestProperty(HttpURLConnection conn, String attr,
                                    String value) {
        try {
            conn.setRequestProperty(URLEncoder.encode(attr, "utf-8"),
                    URLEncoder.encode(value, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String sha256Hex(String signingKey, String stringToSign) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(signingKey.getBytes("utf-8"),
                    "HmacSHA256"));
            return new String(bytesToHexString(mac.doFinal(stringToSign
                    .getBytes("utf-8"))));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private <T extends FFBaseBean> void makePostRequest(
            FFNetWorkRequest<T> request, HttpURLConnection conn)
            throws ProtocolException, IOException, UnsupportedEncodingException {
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded; charset=utf-8");
        conn.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        StringBuilder encodedParams = new StringBuilder();
        for (int i = 0; i < request.getParams().length; i++) {
            String key = request.getParams()[i].toString();
            Object value = request.getParams()[i + 1];
            if (value != null && value.getClass().isArray()) {
                for (Object obj : (Object[]) value) {
                    appendKVPair(encodedParams, key, obj);
                }
            }
            if (value != null && value instanceof Collection) {
                for (Object obj : (Collection) value) {
                    appendKVPair(encodedParams, key, obj);
                }
            } else {
                appendKVPair(encodedParams, key, value);
            }
            i++;
        }
        out.write(encodedParams.toString().getBytes("utf-8"));
        // encodedParams.append(URLEncoder.encode("version", "utf-8"));
        // encodedParams.append('=');
        // encodedParams.append(URLEncoder.encode(FFConfig.VERSION_PAIR,
        // "utf-8"));
        // encodedParams.append('&');
        if (FFConfig.LOG_ENABLE) {
            String message = JSON.toJSONString(request.map1).replaceAll(",\"", "\r\n\"");
            FFLogUtil.e("请求参数" + request.hashCode(), message.substring(1, message.length() - 1));
        }
        out.flush();
        out.close();
    }

    private void appendKVPair(StringBuilder encodedParams, String key, Object value) throws UnsupportedEncodingException {
        encodedParams.append(URLEncoder.encode(
                key, "utf-8"));
        encodedParams.append('=');
        encodedParams.append(URLEncoder.encode(
                value == null ? ""
                        : value.toString(), "utf-8"));
        encodedParams.append('&');
    }

    private <T extends FFBaseBean> void makeUploadReuest(
            FFNetWorkRequest<T> request, HttpURLConnection conn)
            throws ProtocolException, IOException, FileNotFoundException,
            UnsupportedEncodingException {

        String separator = "\r\n";
        String boundary = "---------7d4a6d158c9";

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String contentType = String.format("multipart/form-data; boundary=%s", boundary);
        conn.setRequestProperty("Content-Type", contentType);

        DataOutputStream outPutSteam = new DataOutputStream(conn.getOutputStream());
        int paramCount = request.getParams().length;

        Set<String> set = request.getFileMaps().keySet();
        for (String key : set) {
            outPutSteam.write(separator.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--").append(boundary).append(separator);

            File file = request.getFileMaps().get(key);
            String contentDisposition = String.format("Content-Disposition: form-data;name=\"%s\";filename=\"%s\"%s", key, file.getName(), separator);
            stringBuilder.append(contentDisposition);
            String fileContentType = String.format("Content-Type:%s%s%s", FFNetWorkUtils.getImageMimeType(file), separator, separator);
            stringBuilder.append(fileContentType);
            byte[] data = stringBuilder.toString().getBytes();
            outPutSteam.write(data);
            DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
            int totalByte = 0;
            byte[] buffer = new byte[1024];
            while ((totalByte = inputStream.read(buffer)) != -1) {
                outPutSteam.write(buffer, 0, totalByte);
            }
            inputStream.close();
        }


        for (int i = 0; i < paramCount; i++) {
            outPutSteam.write(separator.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--").append(boundary).append(separator);

            String contentDisposition = String.format("Content-Disposition: form-data;name=\"%s\"%s", request.getParams()[i], separator);
            stringBuilder.append(contentDisposition);
            String paramContentType = String.format("Content-Type:application/x-www-form-urlencoded; charset=utf-8%s%s", separator, separator);
            stringBuilder.append(paramContentType);
            i++;
            Object value = request.getParams()[++i];
            if (value != null) {
                stringBuilder.append(URLEncoder.encode(value.toString(), "utf-8"));
            }
            byte[] data = stringBuilder.toString().getBytes();
            outPutSteam.write(data);
        }

        byte[] end_data = String.format("%s--%s--%s", separator, boundary, separator).getBytes();
        outPutSteam.write(end_data);
        outPutSteam.flush();
        outPutSteam.close();
    }

//    private <T extends FFBaseBean> void makeUploadReuest(
//            FFNetWorkRequest<T> request, HttpURLConnection conn)
//            throws ProtocolException, IOException, FileNotFoundException,
//            UnsupportedEncodingException {
//        conn.setRequestMethod("POST");
//        conn.setDoOutput(true);
//        byte[] end_data = ("\r\n-----------7d4a6d158c9--\r\n").getBytes();// 定义最后数据分隔线
//        conn.setRequestProperty("Content-Type",
//                "multipart/form-data; boundary=---------7d4a6d158c9");
//        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//
//        int fileLength = request.getFileMaps().size();
//        int paramsLength = request.getParams() == null ? 0 : request.getParams().length;
//        int leng = fileLength + paramsLength;
//        for (int i = 0; i < leng; i++) {
//            if (i != 0) {
//                out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
//            }
//            StringBuilder sb = new StringBuilder();
//            sb.append("--");
//            sb.append("---------7d4a6d158c9");
//            sb.append("\r\n");
//            if (i < fileLength) {
//                File file = request.getFiles()[i];
//                sb.append("Content-Disposition: form-data;name=\"pad_log"
//                        + "\";filename=\"" + file.getName() + "\"\r\n");
//                sb.append("Content-Type:"
//                        + FFNetWorkUtils.getImageMimeType(file) + "\r\n\r\n");
//                byte[] data = sb.toString().getBytes();
//                out.write(data);
//                DataInputStream in = new DataInputStream(new FileInputStream(
//                        file));
//                int bytes = 0;
//                byte[] bufferOut = new byte[1024];
//                while ((bytes = in.read(bufferOut)) != -1) {
//                    out.write(bufferOut, 0, bytes);
//                }
//                in.close();
//            } else {
//                sb.append("Content-Disposition: form-data;name=\""
//                        + request.getParams()[i - fileLength] + "\"\r\n");
//                sb.append("Content-Type:application/x-www-form-urlencoded; charset=utf-8\r\n\r\n");
//                i++;
//                if (request.getParams()[i - fileLength] != null) {
//                    sb.append(URLEncoder.encode(request.getParams()[i
//                            - fileLength].toString(), "utf-8"));
//                }
//                byte[] data = sb.toString().getBytes();
//                out.write(data);
//            }
//        }
//        out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
//        StringBuilder sb = new StringBuilder();
//        sb.append("--");
//        sb.append("---------7d4a6d158c9");
//        sb.append("\r\n");
//        sb.append("Content-Disposition: form-data;name=\"version\"\r\n");
//        sb.append("Content-Type:application/x-www-form-urlencoded; charset=utf-8\r\n\r\n");
//        // sb.append(URLEncoder.encode(FFConfig.VERSION_PAIR, "utf-8"));
//        byte[] data = sb.toString().getBytes();
//        out.write(data);
//        out.write(end_data);
//        out.flush();
//        out.close();
//    }

    private void makeGetRequest(HttpURLConnection conn)
            throws ProtocolException {
        conn.setRequestMethod("GET");
    }

    public <T extends FFBaseBean> void excute(final FFNetWorkRequest<T> request) {
        if (getDestroyed()
                && !request.getExtraParams().isKeepWhenActivityFinished()) {
            request.setStatus(FFResponseCode.ACTIVITY_FINISHED,
                    "请求失败，activity已关闭。");
            resultCame(request);
            return;
        }
        if (request.getExtraParams().isUseCache()) {
            String cache = FFHttpCache.getString(request.getCacheKey(), null,
                    request.getExtraParams().getUseValidTime());

            if (cache != null) {
                request.setEntity(JSON.parseObject(cache, request.getClazz()),
                        cache);
                request.setStatus(FFResponseCode.SUCCESS, "请求成功：缓存");
                resultCame(request);
                return;
            }
        }
        if (request.getExtraParams().isIsSynchronized()) {
            startLoad(request);
            requestNet(request);
            FFApplication.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    endLoad(request);
                }
            });
            resultCame(request);
            return;
        }
        es1.submit(new Runnable() {
            public void run() {
                if (getDestroyed()
                        && !request.getExtraParams()
                        .isKeepWhenActivityFinished()) {
                    request.setStatus(FFResponseCode.ACTIVITY_FINISHED,
                            "请求失败，activity已关闭。");
                    resultCame(request);
                    return;
                }
                startLoad(request);
                requestNet(request);
                FFApplication.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        endLoad(request);
                        resultCame(request);
                    }

                });
            }

        });
    }

    private <T extends FFBaseBean> void startLoad(
            final FFNetWorkRequest<T> request) {
        FFApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ffContext != null) {
                    if (request.getWord() != null && !getDestroyed()) {
                        request.setDialogId(
                                ffContext.showProgressDialog(request.getWord(), request.getExtraParams().isProgressDialogcancelAble())
                        );
                    }
                }
            }
        });
    }

    private <T extends FFBaseBean> void endLoad(FFNetWorkRequest<T> request) {
        if (ffContext != null) {
            if (request.getWord() != null) {
                ffContext.dismissProgressDialog(request.getDialogId());
            }
        }
    }

    private <T extends FFBaseBean> void requestNet(FFNetWorkRequest<T> request) {
        int retryTime = 0;
        boolean success = false;
        boolean needRetry = true;
        while (retryTime < 3 && !success && needRetry) {
            retryTime++;
            try {
                excuteHttp(request);
                if (request.getStatus() == FFResponseCode.SUCCESS
                        || request.getStatus() == FFResponseCode.ERROR_CONTEXT) {
                    success = true;
                    needRetry = false;
                }
            } catch (SocketTimeoutException e) {
                FFLogUtil.e(getTag(request), e);
                request.setStatus(FFResponseCode.ERROR_NET_TIMEOUT_S, "响应超时");
                needRetry = true;
            } catch (ConnectTimeoutException e) {
                FFLogUtil.e(getTag(request), e);
                request.setStatus(FFResponseCode.ERROR_NET_TIMEOUT_R, "请求超时");
                FFLogUtil.e(getTag(request), "ConnectTimeoutException超时重试");
                needRetry = true;
            } catch (UnsupportedEncodingException e) {
                FFLogUtil.e(getTag(request), e);
                needRetry = false;
                FFLogUtil.e(getTag(request), "UnsupportedEncodingException 稀奇错误 不重试");
            } catch (FFUnsupportedImageTypeException e) {
                FFLogUtil.e(getTag(request), e);
                needRetry = false;
                FFLogUtil.e(getTag(request), "图片类型错误");
                request.setStatus(FFResponseCode.ERROR_IMAGE_TYPE_NOSUPPORT,
                        "IO异常" + e.getMessage());
                needRetry = true;
            } catch (IOException e) {
                FFLogUtil.e(getTag(request), e);
                request.setStatus(FFResponseCode.ERROR_IO,
                        "IO异常" + e.getMessage());
                needRetry = true;
            } catch (JSONException e) {
                request.setStatus(FFResponseCode.ERROR_ANALYSIS, "服务器返回数据解析失败");
                FFLogUtil.e(getTag(request), e);
                FFLogUtil.e(getTag(request), "JSONException 重试");
                needRetry = true;
            } catch (Exception e) {
                request.setStatus(FFResponseCode.UNSET, "初始化请求");
                FFLogUtil.e(getTag(request), e);
                FFLogUtil.e(getTag(request), "Exception 不重试");
                needRetry = true;
            }
        }
    }

    private <T extends FFBaseBean> String getTag(FFNetWorkRequest<T> request) {
        return TAG + request.hashCode();
    }

    private <T extends FFBaseBean> void resultCame(FFNetWorkRequest<T> request) {
        try {
            request.getCallBack().onBack(request.getExtraParams());
            if (request.getStatus() == FFResponseCode.SUCCESS) {
                if (request.getExtraParams().isDoCache()) {
                    FFHttpCache.storeCache(request.getCacheKey(), request.getEntityString());
                }

                FFLogUtil.e("返回数据" + request.hashCode(), "开始成功回调");
                request.getCallBack().onSuccess(request.getEntity(),
                        request.getExtraParams());
                FFLogUtil.e("返回数据" + request.hashCode(), "结束成功回调");
                if (ffContext != null) {
                    if (request.getEntity().isNoData() && request.getExtraParams().isInitPage()) {
                        ffContext.onPageInitNoData(request);
                    } else {
                        ffContext.onPageInitHasData(request);
                    }
                }
            } else {
                String msg = request.getCallBack().fail(request);
                if (!getDestroyed() && !request.getExtraParams().isQuiet()) {
                    FFApplication.showToast(msg, request.getErrMessage());
                }

                if (request.getExtraParams().isInitPage() && ffContext != null && !getDestroyed()) {
                    switch (request.getStatus()) {
                        case ERROR_NATIVE_NET_CLOST:// 网络未连接
                            ffContext.onPageInitNoNet(request);
                            return;
                        case ERROR_IMAGE_TYPE_NOSUPPORT:// 图片类型不支持
                        case ERROR_NET_TIMEOUT_R:// 请求超时
                        case ERROR_NET_TIMEOUT_S:// 连接超时
                        case ERROR_ANALYSIS:// 数据解析
                        case ERROR_NET_404:// 404
                        case ERROR_SITE_505:// 505
                        case ERROR_IO:// IO异常
                        case ERROR_SITE_XXX:
                        case UNSET:// 未处理
                        case ACTIVITY_FINISHED:
                        case ERROR_CONTEXT:
                        case SUCCESS:
                            ffContext.onPageInitFail(request);
                    }
                }
            }
        } catch (Exception e) {
            FFLogUtil.e(getTag(request), e);
            FFApplication.showToast(null, "数据处理错误");
        }
    }

}
