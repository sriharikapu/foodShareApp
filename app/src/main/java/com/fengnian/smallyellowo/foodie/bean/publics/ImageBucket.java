package com.fengnian.smallyellowo.foodie.bean.publics;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 一个目录的相册对象
 * 
 * @author Administrator
 * 
 */
public class ImageBucket  implements Parcelable {
	public int count = 0;
	public String bucketName;
	public List<ImageItem> imageList;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public List<ImageItem> getImageList() {
		return imageList;
	}

	public void setImageList(List<ImageItem> imageList) {
		this.imageList = imageList;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.count);
		dest.writeString(this.bucketName);
		dest.writeTypedList(this.imageList);
	}

	public ImageBucket() {
	}

	protected ImageBucket(Parcel in) {
		this.count = in.readInt();
		this.bucketName = in.readString();
		this.imageList = in.createTypedArrayList(ImageItem.CREATOR);
	}

	public static final Creator<ImageBucket> CREATOR = new Creator<ImageBucket>() {
		@Override
		public ImageBucket createFromParcel(Parcel source) {
			return new ImageBucket(source);
		}

		@Override
		public ImageBucket[] newArray(int size) {
			return new ImageBucket[size];
		}
	};
}
