package com.fengnian.smallyellowo.foodie.bean.publics;

import android.widget.ImageView;

public class DialogItem {
	private String text;
	private int mViewId;


	public  DialogItem(String text, int viewId){
		this.setText(text);
		this.mViewId = viewId;
	}
	//点击事件
	public void onClick(){
		
	}


	public int getViewId() {
		return mViewId;
	}

	public void setViewId(int mViewId) {
		this.mViewId = mViewId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}

