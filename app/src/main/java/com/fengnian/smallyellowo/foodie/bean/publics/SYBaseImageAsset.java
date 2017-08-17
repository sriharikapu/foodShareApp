package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;
import com.fan.framework.utils.FFImageUtil;
import com.fan.framework.utils.FileUitl;
import com.fengnian.smallyellowo.foodie.bean.publish.AliOssUploadUtil;
import com.fengnian.smallyellowo.foodie.bean.publish.CachImageManager;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class SYBaseImageAsset extends XData implements Parcelable, Serializable {
    //    private String assetID;// assetID，用于服务端查找本地缓存用 是
    private String publishId;//
    private SYUploadImage originalImage;// 原始图(可能在不同的场景赋予不同的意义) 是
    private SYUploadImage processedImage;//  图片(可能在不同的场景赋予不同的意义) 否 
    private List<SYBaseControlImageModel> editControlModels;// 图片编辑操作集合 否

    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }

    public String getAssetID() {
        return getId();
    }

    public void setAssetID(String assetID) {
        setId(assetID);
    }

    public SYUploadImage getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(SYUploadImage originalImage) {
        this.originalImage = originalImage;
    }

    public SYUploadImage getProcessedImage() {
        return processedImage;
    }

    public String pullProcessedImageUrl() {
        if (processedImage == null) {
            return originalImage.getImage().getUrl();
        }
        return processedImage.getImage().getUrl();
    }

    public SYUploadImage pullProcessedImage() {
        if (processedImage == null) {
            return originalImage;
        }
        return processedImage;
    }

    public void pushProcessedImageUrl(String url) {
        if (processedImage == null) {
            setSYImage(url, true);
        } else {
            processedImage.getImage().setUrl(url);
            processedImage.getImage().setPreviewUrl(url);
            processedImage.getImage().setThumbUrl(url);
        }
    }

    public void setProcessedImage(SYUploadImage processedImage) {
        this.processedImage = processedImage;
    }

    public List<SYBaseControlImageModel> getEditControlModels() {
        return editControlModels;
    }

    public void setEditControlModels(List<SYBaseControlImageModel> editControlModels) {
        this.editControlModels = editControlModels;
    }

    public SYBaseImageAsset() {
        setOriginalImage(new SYUploadImage());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.publishId);
        dest.writeParcelable(this.originalImage, flags);
        dest.writeParcelable(this.processedImage, flags);
        dest.writeTypedList(this.editControlModels);
    }

    protected SYBaseImageAsset(Parcel in) {
        super(in);
        this.publishId = in.readString();
        this.originalImage = in.readParcelable(SYUploadImage.class.getClassLoader());
        this.processedImage = in.readParcelable(SYUploadImage.class.getClassLoader());
        this.editControlModels = in.createTypedArrayList(SYBaseControlImageModel.CREATOR);
    }

    public String pullOriginalCutImagePath() {
        return CachImageManager.getImagePathWithId(getPublishId(), getAssetID(), CachImageManager.IMG_TYPE_ORIGINAL_CUT);
    }

    public String pullPrecessedImagePath() {
        return CachImageManager.getImagePathWithId(getPublishId(), getAssetID(), CachImageManager.IMG_TYPE_PROCESS);
    }


    public void setSYImage(String path, boolean isOriginal) {
        {
            SYUploadImage image = isOriginal ? getOriginalImage() : getProcessedImage();
            if (image != null) {
                String oldPath = CachImageManager.getImagePathWithId(getPublishId(), getAssetID(), isOriginal ? CachImageManager.IMG_TYPE_ORIGINAL : CachImageManager.IMG_TYPE_PROCESS);
                if (!oldPath.equals(path)) {
                    FileUitl.delete(new File(oldPath));
                }
            }
        }
        SYUploadImage sui = new SYUploadImage();
        if (isOriginal) {
            setOriginalImage(sui);
        } else {
            setProcessedImage(sui);
        }

        SYImage image = new SYImage();
        sui.setImage(image);

        int[] wh = FFImageUtil.getImageSize(path);
//        FileUitl.copyFile(path, CachImageManager.getImagePathWithId(getPublishId(), ass.getPublishId(), isOriginal ? CachImageManager.IMG_TYPE_ORIGINAL : CachImageManager.IMG_TYPE_PROCESS));
////        int[] wh = saveAssetImageWithID(getPublishId(), ass.getPublishId(), isOriginal ? CachImageManager.IMG_TYPE_ORIGINAL : CachImageManager.IMG_TYPE_PROCESS, FFImageUtil.bitmapFromPath(path, 2000, 2000));//原图
////        saveAssetImageWithID(getPublishId(), ass.getPublishId(), isOriginal ? CachImageManager.IMG_TYPE_ORIGINAL_PREVIEW : CachImageManager.IMG_TYPE_PROCESS_PREVIEW, FFImageUtil.bitmapFromPath(path, Constants.BigImage, Constants.BigImage));//预览图
////        saveAssetImageWithID(getPublishId(), ass.getPublishId(), isOriginal ? CachImageManager.IMG_TYPE_ORIGINAL_SCALED : CachImageManager.IMG_TYPE_PROCESS_SCALED, FFImageUtil.bitmapFromPath(path, Constants.MiddleImage, Constants.MiddleImage));//缩略图

        sui.setAssetId(getPublishId());
        sui.setId(getPublishId());

        if (path.startsWith(FileUitl.getCacheFileDir()) && path.endsWith(".fast")) {
            String processPath = CachImageManager.getImagePathWithId(getPublishId(), getAssetID(), isOriginal ? CachImageManager.IMG_TYPE_ORIGINAL : CachImageManager.IMG_TYPE_PROCESS);
            new File(path).renameTo(new File(processPath));
            path = processPath;
        }

        image.setUrl(path);
//        image.setUrl(CachImageManager.getImagePathWithId(getPublishId(), ass.getPublishId(), isOriginal ? CachImageManager.IMG_TYPE_ORIGINAL : CachImageManager.IMG_TYPE_PROCESS));
////        image.setPreviewUrl(CachImageManager.getImagePathWithId(getPublishId(), ass.getPublishId(), isOriginal ? CachImageManager.IMG_TYPE_ORIGINAL_PREVIEW : CachImageManager.IMG_TYPE_PROCESS_PREVIEW));
////        image.setThumbUrl(CachImageManager.getImagePathWithId(getPublishId(), ass.getPublishId(), isOriginal ? CachImageManager.IMG_TYPE_ORIGINAL_SCALED : CachImageManager.IMG_TYPE_PROCESS_SCALED));
        image.setPreviewUrl(image.getUrl());
        image.setThumbUrl(image.getUrl());
        if (wh != null) {
            image.setWidth(wh[0]);
            image.setHeight(wh[1]);
        }
        if (isOriginal) {
            AliOssUploadUtil.getInstance().ossUpload(sui);
        }
    }

    public static final Creator<SYBaseImageAsset> CREATOR = new Creator<SYBaseImageAsset>() {
        @Override
        public SYBaseImageAsset createFromParcel(Parcel source) {
            return new SYBaseImageAsset(source);
        }

        @Override
        public SYBaseImageAsset[] newArray(int size) {
            return new SYBaseImageAsset[size];
        }
    };


}
