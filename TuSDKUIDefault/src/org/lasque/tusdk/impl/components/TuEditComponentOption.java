/** 
 * TuSDKCore
 * TuEditComponentOption.java
 *
 * @author 		Clear
 * @Date 		2014-12-24 下午3:14:51 
 * @Copyright 	(c) 2014 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.impl.components.edit.TuEditCuterOption;
import org.lasque.tusdk.impl.components.edit.TuEditEntryOption;
import org.lasque.tusdk.impl.components.filter.TuEditFilterOption;
import org.lasque.tusdk.impl.components.sticker.TuStickerChooseOption;

/**
 * 图片编辑组件配置
 * 
 * @author Clear
 */
public class TuEditComponentOption
{
	/**
	 * 图片编辑入口配置选项
	 */
	private TuEditEntryOption mEditEntryOption;

	/**
	 * 图片编辑滤镜控制器配置选项
	 */
	private TuEditFilterOption mEditFilterOption;

	/**
	 * 图片编辑裁切旋转控制器配置选项
	 */
	private TuEditCuterOption mEditCuterOption;

	/**
	 * 贴纸选择控制器配置选项
	 */
	private TuStickerChooseOption mEditStickerOption;

	/**
	 * 图片编辑入口配置选项<br>
	 * 默认配置：<br>
	 * // 默认: true, 开启裁剪旋转功能<br>
	 * mEditEntryOption.setEnableCuter(true);<br>
	 * // 默认: true, 开启滤镜功能<br>
	 * mEditEntryOption.setEnableFilter(true);<br>
	 * // 默认: true, 开启贴纸功能<br>
	 * mEditEntryOption.setEnableSticker(true);<br>
	 * // 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen)<br>
	 * mEditEntryOption.setLimitForScreen(true);<br>
	 * // 保存到系统相册<br>
	 * mEditEntryOption.setSaveToAlbum(true);<br>
	 * //是否在控制器结束后自动删除临时文件<br>
	 * mEditEntryOption.setAutoRemoveTemp(true);<br>
	 * 
	 * @return the mAlbumListOption
	 */
	public TuEditEntryOption editEntryOption()
	{
		if (mEditEntryOption == null)
		{
			mEditEntryOption = new TuEditEntryOption();
			// 默认: true, 开启裁剪旋转功能
			mEditEntryOption.setEnableCuter(true);
			// 默认: true, 开启滤镜功能
			mEditEntryOption.setEnableFilter(true);
			// 默认: true, 开启贴纸功能
			mEditEntryOption.setEnableSticker(true);
			// 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen)
			mEditEntryOption.setLimitForScreen(true);
			// 保存到系统相册
			mEditEntryOption.setSaveToAlbum(true);
			// 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
			// mEditEntryOption.setLimitSideSize(1024);
			// mEditEntryOption.setShowResultPreview(true);
			// 是否在控制器结束后自动删除临时文件
			mEditEntryOption.setAutoRemoveTemp(true);
		}
		return mEditEntryOption;
	}

	/**
	 * 图片编辑滤镜控制器配置选项<br>
	 * // 默认: true, 开启滤镜配置选项<br>
	 * mEditFilterOption.setEnableFilterConfig(true);<br>
	 * // 是否仅返回滤镜，不返回处理图片(默认：true)<br>
	 * mEditFilterOption.setOnlyReturnFilter(true);<br>
	 * // 开启用户滤镜历史记录<br>
	 * mEditFilterOption.setEnableFiltersHistory(true);<br>
	 * // 开启在线滤镜<br>
	 * mEditFilterOption.setEnableOnlineFilter(true);<br>
	 * // 显示滤镜标题视图<br>
	 * mEditFilterOption.setDisplayFiltersSubtitles(true);<br>
	 * 
	 * @return
	 */
	public TuEditFilterOption editFilterOption()
	{
		if (mEditFilterOption == null)
		{
			mEditFilterOption = new TuEditFilterOption();
			// 默认: true, 开启滤镜配置选项
			mEditFilterOption.setEnableFilterConfig(true);
			// 是否仅返回滤镜，不返回处理图片(默认：false)
			mEditFilterOption.setOnlyReturnFilter(true);
			// 开启用户滤镜历史记录
			mEditFilterOption.setEnableFiltersHistory(true);
			// 开启在线滤镜
			mEditFilterOption.setEnableOnlineFilter(true);
			// 显示滤镜标题视图
			mEditFilterOption.setDisplayFiltersSubtitles(true);
		}
		return mEditFilterOption;
	}

	/**
	 * 图片编辑裁切旋转控制器配置选项<br>
	 * // 是否开启图片旋转(默认: true)<br>
	 * mEditCuterOption.setEnableTrun(true);<br>
	 * // 是否开启图片镜像(默认: true)<br>
	 * mEditCuterOption.setEnableMirror(true);<br>
	 * // 裁剪比例 (默认:RatioType.ratio_all)<br>
	 * mEditCuterOption.setRatioType(RatioType.ratio_all);<br>
	 * // 是否仅返回裁切参数，不返回处理图片<br>
	 * mEditCuterOption.setOnlyReturnCuter(true);
	 * 
	 * @return the mEditCuterOption
	 */
	public TuEditCuterOption editCuterOption()
	{
		if (mEditCuterOption == null)
		{
			mEditCuterOption = new TuEditCuterOption();
			// 是否开启图片旋转(默认: false)
			mEditCuterOption.setEnableTrun(true);
			// 是否开启图片镜像(默认: false)
			mEditCuterOption.setEnableMirror(true);
			// 裁剪比例 (默认:RatioType.ratio_default)
			mEditCuterOption.setRatioType(RatioType.ratio_default);
			/**
			 *  裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.defaultRatioTypes)  
			 *  
			 *  设置 int 型数组来控制显示的按钮顺序， 例如:
			 *	new int[] {RatioType.ratio_orgin, RatioType.ratio_1_1, RatioType.ratio_2_3, RatioType.ratio_3_4}
			 *
			 */
			mEditCuterOption.setRatioTypeList(RatioType.defaultRatioTypes);
			// 是否仅返回裁切参数，不返回处理图片
			mEditCuterOption.setOnlyReturnCuter(true);
		}
		return mEditCuterOption;
	}

	/**
	 * 贴纸选择控制器配置选项
	 * 
	 * @return the mEditStickerOption
	 */
	public TuStickerChooseOption editStickerOption()
	{
		if (mEditStickerOption == null)
		{
			mEditStickerOption = new TuStickerChooseOption();
		}
		return mEditStickerOption;
	}

	/** 图片编辑组件配置 */
	public TuEditComponentOption()
	{

	}
}