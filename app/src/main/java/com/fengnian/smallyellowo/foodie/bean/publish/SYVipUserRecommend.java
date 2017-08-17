package com.fengnian.smallyellowo.foodie.bean.publish;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017-2-27.
 */

public class SYVipUserRecommend implements Parcelable {

    private  String  foodImage ;//美食记录图片
    private  String   foodConment="";//美食描述
    private  String   foodTime;//美食发布时间
    private  String  foodRecordId ;//美食ID
    private  String   releaseTemplate;//0标准 1现代 2泼墨 3中式 4中式2 5简短 6简短2 7简短2-2

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodConment() {
        return foodConment;
    }

    public void setFoodConment(String foodConment) {
        this.foodConment = foodConment;
    }

    public String getFoodTime() {
        return foodTime;
    }

    public void setFoodTime(String foodTime) {
        this.foodTime = foodTime;
    }

    public String getFoodRecordId() {
        return foodRecordId;
    }

    public void setFoodRecordId(String foodRecordId) {
        this.foodRecordId = foodRecordId;
    }

    public String getReleaseTemplate() {
        return releaseTemplate;
    }

    public void setReleaseTemplate(String releaseTemplate) {
        this.releaseTemplate = releaseTemplate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.foodImage);
        dest.writeString(this.foodConment);
        dest.writeString(this.foodTime);
        dest.writeString(this.foodRecordId);
        dest.writeString(this.releaseTemplate);
    }

    public SYVipUserRecommend() {
    }

    protected SYVipUserRecommend(Parcel in) {
        this.foodImage = in.readString();
        this.foodConment = in.readString();
        this.foodTime = in.readString();
        this.foodRecordId = in.readString();
        this.releaseTemplate = in.readString();
    }

    public static final Parcelable.Creator<SYVipUserRecommend> CREATOR = new Parcelable.Creator<SYVipUserRecommend>() {
        @Override
        public SYVipUserRecommend createFromParcel(Parcel source) {
            return new SYVipUserRecommend(source);
        }

        @Override
        public SYVipUserRecommend[] newArray(int size) {
            return new SYVipUserRecommend[size];
        }
    };
}
