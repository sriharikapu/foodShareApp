package com.fan.framework.http;

import com.fan.framework.utils.FFUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FFNetWorkUtils {
    /**
     * 获取get方法字符
     *
     * @param paramArray
     * @return
     */
    public static String getGetString(Object... paramArray) {
        if (paramArray != null && paramArray.length != 0) {
            StringBuilder params = new StringBuilder("?");
            int max = paramArray.length;
            for (int i = 0; i < max; i++) {
                Object str = paramArray[++i] == null ? "" : paramArray[i];
                try {
                    params.append(paramArray[i - 1] + "=" + URLEncoder.encode(str.toString(), "utf-8") + "&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return params.toString().substring(0, params.length() - 1);
        }
        return "";
    }

    /**
     * 得到文件的 MIME type
     *
     * @param file
     * @return
     */
    public static int getFileType(File file) {
        if (!file.exists()) {
            return 0;
        }
        InputStream is = null;
        int fileType = 0;
        try {
            is = new FileInputStream(file);
            byte[] buffer = new byte[2];
            String fileCode = "";
            if (is.read(buffer) != -1) {
                for (int i = 0; i < buffer.length; i++) {
                    fileCode += Integer.toString((buffer[i] & 0xFF));
                }
                fileType = Integer.parseInt(fileCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return fileType;
    }

//    public static String unGZip(InputStream in) {
//        StringBuilder sb = new StringBuilder();
//        try {
//            GZIPInputStream pIn = new GZIPInputStream(in);
//            int l;
//            byte[] tmp = new byte[1024];
//            while ((l = pIn.read(tmp)) != -1) {
//                sb.append(new String(tmp, 0, l, "utf-8"));
//            }
//            return sb.toString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static String getBytes(long byteSum) {
        if (byteSum < 1024) {
            return byteSum + "字节";
        }
        if (byteSum < 1024 * 1024) {
            return FFUtils.getSubFloat(byteSum / 1024f) + "K";
        }
        if (byteSum < 1024 * 1024 * 1024) {
            return FFUtils.getSubFloat(byteSum / 1024f / 1024f) + "M";
        }
        return byteSum + "字节";
    }

    public static String getImageMimeType(File file) {
        String type = null;
        int fileType = FFNetWorkUtils.getFileType(file);
        switch (fileType) {
            case 255216:
                type = "image/jpeg";
                break;
            case 7173:
                type = "image/gif";
                break;
            case 6677:
                type = "image/bmp";
                break;
            case 13780:
                type = "image/png";
                break;
            case 4855:
                type = "text/plain";
                break;
            default:
                throw new FFUnsupportedImageTypeException(fileType);
        }
        return type;
    }

}
