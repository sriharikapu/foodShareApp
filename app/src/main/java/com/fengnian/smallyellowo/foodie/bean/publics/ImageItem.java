package com.fengnian.smallyellowo.foodie.bean.publics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Parcelable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public long modify;
	public boolean isSelected = false;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public ImageItem() {

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.imageId);
		dest.writeString(this.thumbnailPath);
		dest.writeString(this.imagePath);
		dest.writeLong(this.modify);
		dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
	}

	protected ImageItem(Parcel in) {
		this.imageId = in.readString();
		this.thumbnailPath = in.readString();
		this.imagePath = in.readString();
		this.modify = in.readLong();
		this.isSelected = in.readByte() != 0;
	}

	public static final Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
		@Override
		public ImageItem createFromParcel(Parcel source) {
			return new ImageItem(source);
		}

		@Override
		public ImageItem[] newArray(int size) {
			return new ImageItem[size];
		}
	};
}
