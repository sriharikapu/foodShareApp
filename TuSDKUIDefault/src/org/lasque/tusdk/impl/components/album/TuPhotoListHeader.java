/** 
 * TuSDKCore
 * TuPhotoListHeader.java
 *
 * @author 		Clear
 * @Date 		2014-11-28 下午7:46:31 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.TuSdkDate;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 相片列表分组头部视图
 * 
 * @author Clear
 */
public class TuPhotoListHeader extends TuSdkCellRelativeLayout<TuSdkDate>
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_album_photo_list_header");
	}

	public TuPhotoListHeader(Context context)
	{
		super(context);
	}

	public TuPhotoListHeader(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuPhotoListHeader(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/** 日期标签 */
	private TextView mDateLabel;

	/** 星期标签 */
	private TextView mWeekLabel;

	/** 日期标签 */
	public TextView getDateLabel()
	{
		if (mDateLabel == null)
		{
			mDateLabel = this.getViewById("lsq_dateLabel");
		}
		return mDateLabel;
	}

	/** 星期标签 */
	public TextView getWeekLabel()
	{
		if (mWeekLabel == null)
		{
			mWeekLabel = this.getViewById("lsq_weekLabel");
		}
		return mWeekLabel;
	}

	@Override
	protected void bindModel()
	{
		if (this.getModel() == null) return;

		this.setTextViewText(this.getDateLabel(), this.getModel().format(this.getResString("las_date_format")));
		this.setTextViewText(this.getWeekLabel(), TuSdkContext.getWeekdayName(this.getModel().weekday()));
	}
}