/** 
 * TuSDKCore
 * TuAlbumGroupListView.java
 *
 * @author 		Clear
 * @Date 		2014-11-26 下午5:09:44 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import java.util.ArrayList;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.anim.AccelerateDecelerateInterpolator;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdk.impl.view.widget.listview.TuArrayListView;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 相册列表视图
 * 
 * @author Clear
 */
public class TuAlbumPopList extends
		TuArrayListView<AlbumSqlInfo, TuAlbumPopListCell>
{
	// 设置弹出相册列表每一行的高度，默认是64
	private int mRowHeight;
	
	/**
	 * 当前显示状态
	 */
	private Boolean mStateHidden = true;
	
	public TuAlbumPopList(Context context)
	{
		super(context);
	}

	public TuAlbumPopList(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuAlbumPopList(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onArrayListViewCreated(TuAlbumPopListCell view,
			TuSdkIndexPath indexPath)
	{
		view.setHeight(TuSdkContext.dip2px(this.getPopListRowHeight()));
	}

	@Override
	protected void onArrayListViewBinded(TuAlbumPopListCell view,
			TuSdkIndexPath indexPath)
	{

	}
	
	@Override
	protected void initView()
	{
		super.initView();
		
		mStateHidden = true;
		setVisibility(View.GONE);
	}
	
	/**
	 * 设置每一行的高度，默认是64
	 * 
	 * @param rowHeight
	 */
	public void setPopListRowHeight(int rowHeight) 
	{
		this.mRowHeight = rowHeight;
	}
	
	/**
	 * 获取每一行的高度，默认是64
	 * 
	 * @return
	 */
	public int getPopListRowHeight() 
	{
		return this.mRowHeight;
	}
	
	/**
	 * 设置相册组数据
	 * 
	 * @param modeList
	 */
	public void setGroups(ArrayList<AlbumSqlInfo> modeList)
	{
		if (modeList == null) return;
		
		setModeList(modeList);
		
		TuSdkSize screenSize = ContextUtils.getDisplaySize(this.getContext());
		
		int maxHeight = screenSize.height - 240;
		
		ViewGroup.LayoutParams params = getLayoutParams();
		
		int dpRowHeight = TuSdkContext.dip2px(this.getPopListRowHeight());
		
		if (modeList.size() * dpRowHeight > maxHeight) {
			params.height = maxHeight;
	    }
	    else {
	    	params.height = modeList.size() * dpRowHeight;
	    }
		setLayoutParams(params); 
		
		this.setY(-params.height);
	}
	
	/**
	 *  切换相册选择列表显示状态: 显示 | 隐藏
	 */
	public void toggleAlbumListViewState()
	{
		mStateHidden = !mStateHidden;
		
		int current = getVisibility();
		
		setVisibility(View.VISIBLE);

		int posY, alpha;
		if (current == View.VISIBLE)
		{
			posY = -this.getHeight();
			alpha = 0;
		}
		else
		{
			posY = 0;
			alpha = 255;
		}
		ViewCompat.animate(this).translationY(posY)
				.setDuration(220)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.setListener(new ViewPropertyAnimatorListenerAdapter()
				{
					@Override
					public void onAnimationEnd(View view)
					{
						if(getStateHidden())
						{
							setVisibility(View.GONE);
						}
					}
				});
		
		ViewCompat.animate(this).alpha(alpha).setDuration(150);
	}
	
	public Boolean getStateHidden()
	{
		return mStateHidden;
	}
}
