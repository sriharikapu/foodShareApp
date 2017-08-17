package com.fengnian.smallyellowo.foodie.bigpicture;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-9-12.
 */
public class BitPictureIntent extends IntentData {
    ArrayList<ImageMap> images;
    int index;

    public ArrayList<ImageMap> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageMap> images) {
        this.images = images;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeList(this.images);
        dest.writeInt(this.index);
    }

    public BitPictureIntent() {
    }

    protected BitPictureIntent(Parcel in) {
        super(in);
        this.images = new ArrayList<ImageMap>();
        in.readList(this.images, ImageMap.class.getClassLoader());
        this.index = in.readInt();
    }

    public static final Creator<BitPictureIntent> CREATOR = new Creator<BitPictureIntent>() {
        @Override
        public BitPictureIntent createFromParcel(Parcel source) {
            return new BitPictureIntent(source);
        }

        @Override
        public BitPictureIntent[] newArray(int size) {
            return new BitPictureIntent[size];
        }
    };

    public static class ImageMap implements Parcelable {

        public static Creator<ImageMap> getCREATOR() {
            return CREATOR;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        String path;

        public String getDishName() {
            return dishName;
        }

        public void setDishName(String dishName) {
            this.dishName = dishName;
        }

        String dishName;

        public ImageMap() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.path);
            dest.writeString(this.dishName);
        }

        protected ImageMap(Parcel in) {
            this.path = in.readString();
            this.dishName = in.readString();
        }

        public static final Creator<ImageMap> CREATOR = new Creator<ImageMap>() {
            @Override
            public ImageMap createFromParcel(Parcel source) {
                return new ImageMap(source);
            }

            @Override
            public ImageMap[] newArray(int size) {
                return new ImageMap[size];
            }
        };
    }

}
