package com.fengnian.smallyellowo.foodie.intentdatas;

import android.os.Parcel;
import android.os.Parcelable;

import com.fengnian.smallyellowo.foodie.bean.publics.FoodListBean;

/**
 * Created by Administrator on 2016-9-13.
 */

public class FoodListIntent extends IntentData  implements Parcelable{
    private FoodListBean   foodbean;

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public FoodListIntent() {

    }

    public FoodListBean getFoodbean() {
        return foodbean;
    }

    public void setFoodbean(FoodListBean foodbean) {
        this.foodbean = foodbean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.foodbean, flags);
        dest.writeString(this.type);
    }

    protected FoodListIntent(Parcel in) {
        super(in);
        this.foodbean = in.readParcelable(FoodListBean.class.getClassLoader());
        this.type = in.readString();
    }

    public static final Creator<FoodListIntent> CREATOR = new Creator<FoodListIntent>() {
        @Override
        public FoodListIntent createFromParcel(Parcel source) {
            return new FoodListIntent(source);
        }

        @Override
        public FoodListIntent[] newArray(int size) {
            return new FoodListIntent[size];
        }
    };
}
