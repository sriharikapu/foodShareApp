package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;

import com.fengnian.smallyellowo.foodie.fragments.MyCameraFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-1-4.
 */

public class ReviewImagesIntent extends IntentData {
//    public ArrayList<MyCameraFragment.TempPic> getImages() {
//        return images;
//    }
//
//    public void setImages(ArrayList<MyCameraFragment.TempPic> images) {
//        this.images = images;
//    }
//
//    ArrayList<MyCameraFragment.TempPic> images;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int index;

    public ReviewImagesIntent() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
//        dest.writeTypedList(this.images);
        dest.writeInt(this.index);
    }

    protected ReviewImagesIntent(Parcel in) {
        super(in);
//        this.images = in.createTypedArrayList(MyCameraFragment.TempPic.CREATOR);
        this.index = in.readInt();
    }

    public static final Creator<ReviewImagesIntent> CREATOR = new Creator<ReviewImagesIntent>() {
        @Override
        public ReviewImagesIntent createFromParcel(Parcel source) {
            return new ReviewImagesIntent(source);
        }

        @Override
        public ReviewImagesIntent[] newArray(int size) {
            return new ReviewImagesIntent[size];
        }
    };
}
