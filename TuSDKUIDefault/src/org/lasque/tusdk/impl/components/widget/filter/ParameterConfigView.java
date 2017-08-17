/** 
 * TuSDKCore
 * ParameterConfigView.java
 *
 * @author 		Clear
 * @Date 		2015-2-15 下午6:18:04 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.widget.filter;

import java.util.ArrayList;
import java.util.List;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.view.TuSdkLinearLayout;
import org.lasque.tusdk.core.view.TuSdkViewHelper.OnSafeClickListener;
import org.lasque.tusdk.core.view.widget.button.TuSdkTextButton;
import org.lasque.tusdk.impl.view.widget.ParameterConfigViewInterface;
import org.lasque.tusdk.impl.view.widget.TuSeekBar;
import org.lasque.tusdk.impl.view.widget.TuSeekBar.TuSeekBarDelegate;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 参数配置视图
 * 
 * @author Clear
 */
public class ParameterConfigView extends TuSdkLinearLayout implements ParameterConfigViewInterface
{
	/** 布局ID */
	public static int getLayoutId()
	{
		return TuSdkContext.getLayoutResId("tusdk_impl_component_widget_parameter_config_view");
	}

	public ParameterConfigView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public ParameterConfigView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ParameterConfigView(Context context)
	{
		super(context);
	}

	/** 参数配置视图委托 */
	private ParameterConfigViewDelegate mDelegate;
	/** 参数视图列表 */
	private List<TuSdkTextButton> mParamViews;
	/** 当前选中的索引 */
	private int mCurrentIndex;

	/** 参数配置视图委托 */
	public ParameterConfigViewDelegate getDelegate()
	{
		return mDelegate;
	}

	/** 参数配置视图委托 */
	@Override
	public void setDelegate(ParameterConfigViewDelegate mDelegate)
	{
		this.mDelegate = mDelegate;
	}

	/**************** view ***************/
	/** 参数选项视图 */
	private LinearLayout mParamsView;
	/** 重置按钮 */
	private TextView mRestButton;
	/** 数字显示视图 */
	private TextView mNumberView;
	/** 百分比控制条 */
	private TuSeekBar mSeekView;

	/** 参数选项视图 */
	public LinearLayout getParamsView()
	{
		if (mParamsView == null)
		{
			mParamsView = this.getViewById("lsq_params_view");
			if (mParamsView != null)
			{
				mParamsView.removeAllViews();
			}
		}
		return mParamsView;
	}

	/** 重置按钮 */
	public TextView getRestButton()
	{
		if (mRestButton == null)
		{
			mRestButton = this.getViewById("lsq_rest_button");
			if (mRestButton != null)
			{
				mRestButton.setOnClickListener(mOnClickListener);
			}
		}
		return mRestButton;
	}

	/** 数字显示视图 */
	public TextView getNumberView()
	{
		if (mNumberView == null)
		{
			mNumberView = this.getViewById("lsq_number_view");
		}
		return mNumberView;
	}

	/** 百分比控制条 */
	public TuSeekBar getSeekView()
	{
		if (mSeekView == null)
		{
			mSeekView = this.getViewById("lsq_seek_view");
			if (mSeekView != null)
			{
				mSeekView.setDelegate(mTuSeekBarDelegate);
			}
		}
		return mSeekView;
	}

	@Override
	public void loadView()
	{
		super.loadView();
		getParamsView();
		getRestButton();
		getNumberView();
		getSeekView();
	}

	/** 按钮点击事件 */
	protected OnSafeClickListener mOnClickListener = new OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			if (equalViewIds(v, getRestButton()))
			{
				handleRestAction();
			}
			else if (v instanceof TuSdkTextButton)
			{
				selectedIndex(((TuSdkTextButton) v).index);
			}
		}
	};

	/** 百分比控制条委托 */
	private TuSeekBarDelegate mTuSeekBarDelegate = new TuSeekBarDelegate()
	{
		@Override
		public void onTuSeekBarChanged(TuSeekBar seekBar, float progress)
		{
			onSeekbarDataChanged(progress);
		}
	};

	/** 百分比控制条数据改变 */
	protected void onSeekbarDataChanged(float progress)
	{
		this.setProgressTitle(progress);
		if (mDelegate != null)
		{
			mDelegate.onParameterConfigDataChanged(this, mCurrentIndex, progress);
		}
	}

	/** 跳到指定百分比 */
	public void seekTo(float progress)
	{
		if (this.getSeekView() != null)
		{
			this.getSeekView().setProgress(progress);
		}
		this.setProgressTitle(progress);
	}

	/**
	 * 进度
	 * 
	 * @param mProgress
	 *            进度百分比(0-1)
	 */
	private void setProgressTitle(float mProgress)
	{
		if (this.getNumberView() != null)
		{
			this.getNumberView().setText(String.format("+%01d", (int) (mProgress * 100)));
		}
	}

	/** 重置参数 */
	protected void handleRestAction()
	{
		if (mDelegate != null)
		{
			mDelegate.onParameterConfigRest(this, mCurrentIndex);
		}
	}

	/** 设置参数列表 */
	public void setParams(List<String> argKeys, int selectedIndex)
	{
		if (argKeys == null || selectedIndex >= argKeys.size() || argKeys.isEmpty()) return;

		LinearLayout view = this.getParamsView();
		if (view == null) return;

		view.removeAllViews();

		mParamViews = new ArrayList<TuSdkTextButton>(argKeys.size());

		LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);

		for (String key : argKeys)
		{
			TuSdkTextButton textView = this.createParamView(key);
			textView.index = mParamViews.size();
			view.addView(textView, params);
			mParamViews.add(textView);
		}
		mCurrentIndex = -1;
		this.selectedIndex(selectedIndex);
	}

	/** 创建参数选项视图 */
	private TuSdkTextButton createParamView(String key)
	{
		TuSdkTextButton textView = new TuSdkTextButton(this.getContext());
		int[] colors = new int[] {
				TuSdkContext.getColor("lsq_filter_config_highlight"), TuSdkContext.getColor("lsq_color_white") };
		int[][] states = new int[][] {
				new int[] { android.R.attr.state_selected }, new int[] {} };

		ColorStateList colorStateList = new ColorStateList(states, colors);

		textView.setTextColor(colorStateList);
		textView.setGravity(Gravity.CENTER);
		textView.setText(TuSdkContext.getString("lsq_filter_set_" + key));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		textView.setEllipsize(TruncateAt.END);
		textView.setLines(1);
		textView.setOnClickListener(mOnClickListener);
		return textView;
	}

	/** 选中索引 */
	private void selectedIndex(int index)
	{
		if (mCurrentIndex == index || mParamViews == null) return;
		mCurrentIndex = index;
		for (TuSdkTextButton btn : mParamViews)
		{
			btn.setSelected(btn.index == index && mParamViews.size() > 1);
		}
		if (mDelegate != null)
		{
			this.seekTo(mDelegate.readParameterValue(this, index));
		}
	}
}