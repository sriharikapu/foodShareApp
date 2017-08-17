//package com.fan.framework.utils.select_picture;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
///**
// * Created by Administrator on 2016-9-29.
// */
//public class NativeImage implements Parcelable {
//    String path;
//    String thumbnails;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    String id;
//
//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public String getThumbnails() {
//        if(thumbnails == null){
//            return path;
//        }
//        return thumbnails;
//    }
//
//    public void setThumbnails(String thumbnails) {
//        this.thumbnails = thumbnails;
//    }
//
//    public NativeImage() {
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.path);
//        dest.writeString(this.thumbnails);
//        dest.writeString(this.id);
//    }
//
//    protected NativeImage(Parcel in) {
//        this.path = in.readString();
//        this.thumbnails = in.readString();
//        this.id = in.readString();
//    }
//
//    public static final Creator<NativeImage> CREATOR = new Creator<NativeImage>() {
//        @Override
//        public NativeImage createFromParcel(Parcel source) {
//            return new NativeImage(source);
//        }
//
//        @Override
//        public NativeImage[] newArray(int size) {
//            return new NativeImage[size];
//        }
//    };
//}
