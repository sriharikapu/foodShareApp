package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.fan.framework.base.XData;
import com.fan.framework.xtaskmanager.xtask.XTask;
import com.fengnian.smallyellowo.foodie.FastEditActivity;
import com.fengnian.smallyellowo.foodie.RichTextEditActivity;
import com.fengnian.smallyellowo.foodie.appbase.APP;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.feeddetail.DynamicDetailActivity;
import com.fengnian.smallyellowo.foodie.taskmanager.task.PublishModel;

import java.io.Serializable;

public class SYUploadImage extends XData implements Parcelable, Serializable {
    public static final int UPLOAD_STATUS_INIT = 0;
    public static final int UPLOAD_STATUS_UPLOADING = 1;
    public static final int UPLOAD_STATUS_UPLOAD_SUCCESS = 2;
    public static final int UPLOAD_STATUS_UPLOAD_FAIL = 3;


    private SYImage image;// 有可能是本地图片有可能是网络图片 否

    @JSONField(serialize = false)
    public transient Object LOCK1 = new Object();

    public final Object LOCK() {
        if (LOCK1 == null) {
            LOCK1 = new Object();
        }
        return LOCK1;
    }


    /**
     * 上传状态
     */
    @JSONField(serialize = false)
    private int uploadStatus = UPLOAD_STATUS_INIT;

    private String assetId;

    public SYImage getImage() {
        return image;
    }

    public void setImage(SYImage image) {
        this.image = image;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public SYUploadImage() {
        setImage(new SYImage());
    }

    public int getUploadStatus() {
        if(getImage() != null){
            if(getImage().getUrl() != null){
                if (getImage().getUrl().startsWith("http")) {
                    return UPLOAD_STATUS_UPLOAD_SUCCESS;
                }
            }
        }

        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public void uploadStart() {
        synchronized (LOCK()) {
            setUploadStatus(UPLOAD_STATUS_UPLOADING);
        }
    }

    public void uploadSuccess() {
        synchronized (LOCK()) {
            setUploadStatus(UPLOAD_STATUS_UPLOAD_SUCCESS);
            XTask task = SYDataManager.shareDataManager().taskWithID(getId());
            boolean isIn = task != null;
            if(task == null && FastEditActivity.task != null){
                if(getId().equals(FastEditActivity.task.getId())){
                    task = FastEditActivity.task;
                }
            }
            if(task == null && RichTextEditActivity.task != null){
                if(getId().equals(RichTextEditActivity.task.getId())){
                    task = RichTextEditActivity.task;
                }
            }
            if(task == null && DynamicDetailActivity.task != null){
                if(getId().equals(DynamicDetailActivity.task.getId())){
                    task = DynamicDetailActivity.task;
                }
            }
            if (task instanceof PublishModel) {
                PublishModel publishModel = (PublishModel) task;
//                publishModel.setTaskExecutState(XTask.XTaskExecutStateComplete);
                if(isIn) {
                    SYDataManager.shareDataManager().addTask(publishModel);
                }
                publishModel.uploadImageComplete();
            }
        }
        APP.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(RichTextEditActivity.instance != null){
                    RichTextEditActivity.instance.onOnePictureUploadStatusChange(SYUploadImage.this);
                }
                if(FastEditActivity.instance != null){
                    FastEditActivity.instance.onOnePictureUploadStatusChange(SYUploadImage.this);
                }
            }
        });
    }

    public void uploadFail() {
        synchronized (LOCK()) {
            setUploadStatus(UPLOAD_STATUS_UPLOAD_FAIL);
            XTask task = SYDataManager.shareDataManager().taskWithID(getId());
            boolean isin = task != null;
            if(task == null && FastEditActivity.task != null){
                if(getId().equals(FastEditActivity.task.getId())){
                    task = FastEditActivity.task;
                }
            }
            if(task == null && RichTextEditActivity.task != null){
                if(getId().equals(RichTextEditActivity.task.getId())){
                    task = RichTextEditActivity.task;
                }
            }
            if(task == null && DynamicDetailActivity.task != null){
                if(getId().equals(DynamicDetailActivity.task.getId())){
                    task = DynamicDetailActivity.task;
                }
            }
            if (task instanceof PublishModel) {
                PublishModel publishModel = (PublishModel) task;
                publishModel.setTaskExecutState(XTask.XTaskExecutStateFail);
                if(isin) {
                    SYDataManager.shareDataManager().addTask(publishModel);
                }
                publishModel.uploadImageFail();
            }
        }
        APP.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(RichTextEditActivity.instance != null){
                    RichTextEditActivity.instance.onOnePictureUploadStatusChange(SYUploadImage.this);
                }
                if(FastEditActivity.instance != null){
                    FastEditActivity.instance.onOnePictureUploadStatusChange(SYUploadImage.this);
                }
            }
        });
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.image, flags);
        dest.writeInt(this.uploadStatus);
        dest.writeString(this.assetId);
    }

    protected SYUploadImage(Parcel in) {
        super(in);
        this.image = in.readParcelable(SYImage.class.getClassLoader());
        this.uploadStatus = in.readInt();
        this.assetId = in.readString();
    }

    public static final Creator<SYUploadImage> CREATOR = new Creator<SYUploadImage>() {
        @Override
        public SYUploadImage createFromParcel(Parcel source) {
            return new SYUploadImage(source);
        }

        @Override
        public SYUploadImage[] newArray(int size) {
            return new SYUploadImage[size];
        }
    };
}
