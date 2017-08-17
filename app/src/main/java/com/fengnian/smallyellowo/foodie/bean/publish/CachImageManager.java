package com.fengnian.smallyellowo.foodie.bean.publish;

import android.graphics.Bitmap;

import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FFUtils;
import com.fan.framework.utils.FileUitl;

import java.io.File;

/**
 * Created by Administrator on 2016-10-18.
 */

/**
 * 发布本地缓存管理
 */
public class CachImageManager {

    public static final int IMG_TYPE_ORIGINAL = 0;
    public static final int IMG_TYPE_ORIGINAL_SCALED = 1;
    public static final int IMG_TYPE_ORIGINAL_PREVIEW = 2;
    public static final int IMG_TYPE_PROCESS = 3;
    public static final int IMG_TYPE_PROCESS_SCALED = 4;
    public static final int IMG_TYPE_PROCESS_PREVIEW = 5;
    public static final int IMG_TYPE_HEAD = 6;
    public static final int IMG_TYPE_COVER = 7;
    public static final int IMG_TYPE_ORIGINAL_CUT = 8;
    public static final int IMG_TYPE_PROCESS_TEMP = 9;
    public static final int IMG_TYPE_ORIGINAL_CUT_TEMP = 10;

    /**
     * @param id             SYFood 的 id
     * @param imageAssetID   图片id
     * @param imageAssetType 图片类型 IMG_TYPE_PROCESS：处理过的图
     * @param bitmap         要保存的图片bitmap
     * @return
     */
    public static String saveAssetImageWithID(String id, String imageAssetID, int imageAssetType, Bitmap bitmap) {
        if (FFUtils.isStringEmpty(id))
            return null;
        if (FFUtils.isStringEmpty(imageAssetID))
            return null;
        if (bitmap == null || bitmap.isRecycled())
            return null;
        String path = getImagePathWithId(id, imageAssetID, imageAssetType);
        FFImageUtil.saveBitmap(path, bitmap);

        return path;
    }

    /**
     * @param id                SYFood 的 id
     * @param imageAssetID      图片id
     * @param imageAssetType    图片类型 IMG_TYPE_PROCESS：处理过的图
     * @param originalImagePath 图片原来的的路径
     * @param isCut             是否剪切 如果不是就copy
     * @return
     */
    public static boolean saveAssetImageWithID(String id, String imageAssetID, int imageAssetType, String originalImagePath, boolean isCut) {
        if (FFUtils.isStringEmpty(id))
            return false;
        if (FFUtils.isStringEmpty(imageAssetID))
            return false;
        File file = new File(originalImagePath);
        if (!file.exists())
            return false;
        String path = getImagePathWithId(id, imageAssetID, imageAssetType);
        if (originalImagePath.equals(path)) {//原图片和目标路径相同不需要任何操作
            return true;
        }
        File targetFile = new File(path);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        if (isCut) {
            file.renameTo(new File(path));
        } else {
            FileUitl.copyFile(originalImagePath, path);
        }
        return true;
    }

    public static String getImagePathWithId(String foodId, String imageAssetID, int imageAssetType) {
        String superParent = FileUitl.getCacheFileDir() + "/" + foodId;
        mkdir(superParent);
        String parent = superParent + "/" + imageAssetID;
        mkdir(parent);
        return parent + "/" + imageAssetType+".jpg";
    }

    private static void mkdirs(String parent) {
        File parentFile = new File(parent);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
    }

    private static void mkdir(String parent) {
        File parentFile = new File(parent);
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
    }

    public static String getImageDirWithId(String foodId, String imageAssetID) {
        return getImageDirWithId(foodId, imageAssetID, true);
    }

    public static String getImageDirWithId(String foodId, String imageAssetID, boolean mkDir) {
        String parent = FileUitl.getCacheFileDir() + "/" + foodId + "/" + imageAssetID;
        if (mkDir) {
            mkdirs(parent);
        }
        return parent;
    }

    public static String getImagePathWithId(String foodId) {
        String parent = FileUitl.getCacheFileDir() + "/" + foodId;
        mkdirs(parent);
        return parent;
    }


}
