package com.fengnian.smallyellowo.foodie.bean.publics;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.fan.framework.base.XData;
import com.fengnian.smallyellowo.foodie.bean.publish.CachImageManager;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;

import java.io.Serializable;

public class SYFrontCoverModel extends XData implements Parcelable, Serializable {
    private String frontCoverAssetID;// 封面assetID 是
    private SYRichTextPhotoModel frontCoverContent;// 富文本对象 是

    public String getFrontCoverAssetID() {
        return frontCoverAssetID;
    }

    public void setFrontCoverAssetID(String frontCoverAssetID) {
        this.frontCoverAssetID = frontCoverAssetID;
    }

    public SYRichTextPhotoModel getFrontCoverContent() {
        if(frontCoverContent == null){
            frontCoverContent = new SYRichTextPhotoModel();
        }
        return frontCoverContent;
    }

    public String pullImage() {
        return getFrontCoverContent().getPhoto().getImageAsset().getOriginalImage().getImage().getUrl();
    }


    public void setFrontCoverContent(SYRichTextPhotoModel frontCoverContent) {
        this.frontCoverContent = frontCoverContent;
    }

    public SYFrontCoverModel() {
        setFrontCoverContent(new SYRichTextPhotoModel());
        frontCoverContent.setPhoto(new SYFoodPhotoModel());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.frontCoverAssetID);
        dest.writeParcelable(this.frontCoverContent, flags);
    }

    public void init(String frontCoverAssetID, Bitmap bitmap, String publishId) {
        this.frontCoverAssetID = frontCoverAssetID;
        if(this.frontCoverContent == null) {
            this.frontCoverContent = new SYRichTextPhotoModel();
        }
        this.frontCoverContent.setPhoto(new SYFoodPhotoModel());
        this.frontCoverContent.getPhoto().setImageAsset(new SYBaseImageAsset());
        this.frontCoverContent.getPhoto().getImageAsset().setPublishId(publishId);
        String path = CachImageManager.saveAssetImageWithID(SYDataManager.shareDataManager().draftsWithID(publishId).getFeed().getFood().getId(),
                this.frontCoverContent.getPhoto().getImageAsset().getId(),
                CachImageManager.IMG_TYPE_COVER, bitmap);
        this.frontCoverContent.getPhoto().getImageAsset().pushProcessedImageUrl(path);

    }

    protected SYFrontCoverModel(Parcel in) {
        super(in);
        this.frontCoverAssetID = in.readString();
        this.frontCoverContent = in.readParcelable(SYRichTextPhotoModel.class.getClassLoader());
    }

    public static final Creator<SYFrontCoverModel> CREATOR = new Creator<SYFrontCoverModel>() {
        @Override
        public SYFrontCoverModel createFromParcel(Parcel source) {
            return new SYFrontCoverModel(source);
        }

        @Override
        public SYFrontCoverModel[] newArray(int size) {
            return new SYFrontCoverModel[size];
        }
    };
}
