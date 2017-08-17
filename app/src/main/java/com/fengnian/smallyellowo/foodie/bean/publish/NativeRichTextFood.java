package com.fengnian.smallyellowo.foodie.bean.publish;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextFood;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-10-18.
 */

public class NativeRichTextFood  extends SYRichTextFood implements Parcelable,Serializable {

    public PublishModel getTask() {
        return task;
    }

    public void setTask(PublishModel task) {
        this.task = task;
    }

    @JSONField(serialize = false, deserialize = false)
    private transient PublishModel task;

    public NativeRichTextFood() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected NativeRichTextFood(Parcel in) {
        super(in);
    }

    public static final Creator<NativeRichTextFood> CREATOR = new Creator<NativeRichTextFood>() {
        @Override
        public NativeRichTextFood createFromParcel(Parcel source) {
            return new NativeRichTextFood(source);
        }

        @Override
        public NativeRichTextFood[] newArray(int size) {
            return new NativeRichTextFood[size];
        }
    };
}
