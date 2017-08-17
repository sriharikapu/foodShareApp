package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fan.framework.base.XData;

import java.io.Serializable;

public class SYImage extends XData implements Parcelable, Serializable {
    private String url;// 高清图链接地址 是
    private String thumbUrl;// 缩略图链接地址 是
    private float height;// 图片的高度(高清图) 否
    private float width;// 图片的宽度(高清图) 否

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    @JSONField(serialize = false, deserialize = false)
    private String previewUrl;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public SYImage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.url);
        dest.writeString(this.thumbUrl);
        dest.writeFloat(this.height);
        dest.writeFloat(this.width);
    }

    protected SYImage(Parcel in) {
        super(in);
        this.url = in.readString();
        this.thumbUrl = in.readString();
        this.height = in.readFloat();
        this.width = in.readFloat();
    }

    public static final Creator<SYImage> CREATOR = new Creator<SYImage>() {
        @Override
        public SYImage createFromParcel(Parcel source) {
            return new SYImage(source);
        }

        @Override
        public SYImage[] newArray(int size) {
            return new SYImage[size];
        }
    };


    public static final SYImage createOrUpdateWithJsonObject(SYImage image,JSONObject jsonObject){
        if(jsonObject != null) {
            if (image == null) {
                image = new SYImage();
            }

            if(jsonObject.containsKey("id")){
                image.setId(jsonObject.getString("id"));
            }else {
                image.setId(null);
            }

            if(jsonObject.containsKey("url")){
                image.setUrl(jsonObject.getString("url"));
            }else {
                image.setUrl(null);
            }

            if(jsonObject.containsKey("thumbUrl")){
                image.setThumbUrl(jsonObject.getString("thumbUrl"));
            }else {
                image.setThumbUrl(null);
            }

            if(jsonObject.containsKey("width")){
                Float width = jsonObject.getFloat("width");
                if(width != null){
                    width = jsonObject.getFloatValue("width");
                    image.setWidth(width);
                }else {
                    image.setWidth(-1);
                }
            }else {
                image.setWidth(-1);
            }

            if(jsonObject.containsKey("height")){
                Float height = jsonObject.getFloat("height");
                if(height != null){
                    height = jsonObject.getFloatValue("height");
                    image.setHeight(height);
                }else {
                    image.setHeight(-1);
                }
            }else {
                image.setHeight(-1);
            }
        }
        return image;
    }
}
