/** 
 * TuSDKCore
 * TuEditMultipleComponentOption.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午6:43:41 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components;

import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.impl.components.edit.TuEditCuterOption;
import org.lasque.tusdk.impl.components.edit.TuEditMultipleOption;
import org.lasque.tusdk.impl.components.filter.TuEditAdjustOption;
import org.lasque.tusdk.impl.components.filter.TuEditApertureOption;
import org.lasque.tusdk.impl.components.filter.TuEditFilterOption;
import org.lasque.tusdk.impl.components.filter.TuEditHDROption;
import org.lasque.tusdk.impl.components.filter.TuEditHolyLightOption;
import org.lasque.tusdk.impl.components.filter.TuEditSharpnessOption;
import org.lasque.tusdk.impl.components.filter.TuEditSkinOption;
import org.lasque.tusdk.impl.components.filter.TuEditVignetteOption;
import org.lasque.tusdk.impl.components.filter.TuEditWipeAndFilterOption;
import org.lasque.tusdk.impl.components.smudge.TuEditSmudgeOption;
import org.lasque.tusdk.impl.components.sticker.TuEditStickerOption;

/**
 * 多功能图像编辑组件选项
 * 
 * @author Clear
 */
public class TuEditMultipleComponentOption
{
	// 多功能图像编辑控制器配置选项
	private TuEditMultipleOption mEditMultipleOption;

	// 图片编辑滤镜控制器配置选项
	private TuEditFilterOption mEditFilterOption;

	// 图片编辑裁切旋转控制器配置选项
	private TuEditCuterOption mEditCuterOption;

	// 美肤编辑功能控制器配置选项
	private TuEditSkinOption mEditSkinOption;

	// 图片编辑贴纸控制器配置选项
	private TuEditStickerOption mEditStickerOption;

	// 颜色调整控制器配置选项
	private TuEditAdjustOption mEditAdjustOption;
	
	// 涂抹控制器配置选项
	private TuEditSmudgeOption mEditSmudgeOption;
	
	// 模糊控制器配置选项
	private TuEditWipeAndFilterOption mEditWipeAndFilterOption;

	// 锐化控制器配置选项
	private TuEditSharpnessOption mEditSharpnessOption;

	// 大光圈控制器配置选项
	private TuEditApertureOption mEditApertureOption;

	// 暗角控制器配置选项
	private TuEditVignetteOption mEditVignetteOption;
	
	// 圣光控制器配置选项
	private TuEditHolyLightOption mEditHolyLightOption;
	
	// HDR 控制器配置选项
	private TuEditHDROption mEditHDROption;

	/**
	 * 多功能图像编辑控制器配置选项<br>
	 * 默认配置：<br>
	 * // 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen)<br>
	 * mEditEntryOption.setLimitForScreen(true);<br>
	 * // 保存到系统相册<br>
	 * mEditEntryOption.setSaveToAlbum(true);<br>
	 * //是否在控制器结束后自动删除临时文件<br>
	 * mEditEntryOption.setAutoRemoveTemp(true);<br>
	 * 
	 * @return the mEditMultipleOption
	 */
	public TuEditMultipleOption editMultipleOption()
	{
		if (mEditMultipleOption == null)
		{
			mEditMultipleOption = new TuEditMultipleOption();
			// 最大输出图片按照设备屏幕 (默认:true, 如果设置了LimitSideSize, 将忽略LimitForScreen)
			mEditMultipleOption.setLimitForScreen(true);
			// 保存到系统相册
			mEditMultipleOption.setSaveToAlbum(true);
			// 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
			// mEditEntryOption.setLimitSideSize(1024);
			// mEditEntryOption.setShowResultPreview(true);
			// 是否在控制器结束后自动删除临时文件
			mEditMultipleOption.setAutoRemoveTemp(true);
		}
		return mEditMultipleOption;
	}

	/**
	 * 图片编辑滤镜控制器配置选项<br>
	 * // 默认: true, 开启滤镜配置选项<br>
	 * mEditFilterOption.setEnableFilterConfig(true);<br>
	 * // 保存到临时文件<br>
	 * mEditFilterOption.setSaveToTemp(true);<br>
	 * // 开启用户滤镜历史记录<br>
	 * mEditFilterOption.setEnableFiltersHistory(true);<br>
	 * // 开启在线滤镜<br>
	 * mEditFilterOption.setEnableOnlineFilter(true);<br>
	 * // 显示滤镜标题视图<br>
	 * mEditFilterOption.setDisplayFiltersSubtitles(true);<br>
	 * // 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)<br>
	 * mEditFilterOption.setRenderFilterThumb(true);<br>
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
			// 保存到临时文件
			mEditFilterOption.setSaveToTemp(true);
			// 开启用户滤镜历史记录
			mEditFilterOption.setEnableFiltersHistory(true);
			// 开启在线滤镜
			mEditFilterOption.setEnableOnlineFilter(true);
			// 显示滤镜标题视图
			mEditFilterOption.setDisplayFiltersSubtitles(true);
			// 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
			mEditFilterOption.setRenderFilterThumb(true);
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
	 * // 保存到临时文件<br>
	 * mEditCuterOption.setSaveToTemp(true);<br>
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
			// 保存到临时文件
			mEditCuterOption.setSaveToTemp(true);
		}
		return mEditCuterOption;
	}

	/**
	 * 美肤编辑功能控制器配置选项
	 * // 保存到临时文件<br>
	 * mEditCuterOption.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditSkinOption
	 */
	public TuEditSkinOption editSkinOption()
	{
		if (mEditSkinOption == null)
		{
			mEditSkinOption = new TuEditSkinOption();
			// 保存到临时文件
			mEditSkinOption.setSaveToTemp(true);
		}
		return mEditSkinOption;
	}

	/**
	 * 图片编辑贴纸控制器配置选项
	 * // 单元格间距 (单位：DP)
	 * mEditStickerOption.setGridPaddingDP(2);
	 * // 保存到临时文件<br>
	 * mEditStickerOption.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditStickerOption
	 */
	public TuEditStickerOption editStickerOption()
	{
		if (mEditStickerOption == null)
		{
			mEditStickerOption = new TuEditStickerOption();
			// 单元格间距 (单位：DP)
			mEditStickerOption.setGridPaddingDP(2);
			// 保存到临时文件
			mEditStickerOption.setSaveToTemp(true);
		}
		return mEditStickerOption;
	}

	/**
	 * 涂抹控制器配置选项
	 * // 保存到临时文件<br>
	 * mEditSmudgeOption.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditSmudgeOption
	 */
	public TuEditSmudgeOption editSmudgeOption()
	{
		if (mEditSmudgeOption == null)
		{
			mEditSmudgeOption = new TuEditSmudgeOption();
			// 保存到临时文件
			mEditSmudgeOption.setSaveToTemp(true);
		}
		return mEditSmudgeOption;
	}
	
	/**
	 * 颜色调整控制器配置选项
	 * // 保存到临时文件<br>
	 * mEditAdjustOption.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditAdjustOption
	 */
	public TuEditAdjustOption editAdjustOption()
	{
		if (mEditAdjustOption == null)
		{
			mEditAdjustOption = new TuEditAdjustOption();
			// 保存到临时文件
			mEditAdjustOption.setSaveToTemp(true);
		}
		return mEditAdjustOption;
	}
	
	/**
	 * 模糊控制器配置选项
	 * // 保存到临时文件<br>
	 * mEditSmudgeOption.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditSmudgeOption
	 */
	public TuEditWipeAndFilterOption editWipeAndFilterOption()
	{
		if (mEditWipeAndFilterOption == null)
		{
			mEditWipeAndFilterOption = new TuEditWipeAndFilterOption();
			// 保存到临时文件
			mEditWipeAndFilterOption.setSaveToTemp(true);
		}
		return mEditWipeAndFilterOption;
	}

	/**
	 * 锐化控制器配置选项
	 * // 保存到临时文件<br>
	 * mEditSharpnessOption.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditSharpnessOption
	 */
	public TuEditSharpnessOption editSharpnessOption()
	{
		if (mEditSharpnessOption == null)
		{
			mEditSharpnessOption = new TuEditSharpnessOption();
			// 保存到临时文件
			mEditSharpnessOption.setSaveToTemp(true);
		}
		return mEditSharpnessOption;
	}

	/**
	 * 大光圈控制器配置选项
	 * // 保存到临时文件<br>
	 * mEditApertureOption.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditApertureOption
	 */
	public TuEditApertureOption editApertureOption()
	{
		if (mEditApertureOption == null)
		{
			mEditApertureOption = new TuEditApertureOption();
			// 保存到临时文件
			mEditApertureOption.setSaveToTemp(true);
		}
		return mEditApertureOption;
	}

	/**
	 * 暗角控制器配置选项
	 * // 保存到临时文件<br>
	 * mEditVignetteOptio.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditVignetteOption
	 */
	public TuEditVignetteOption editVignetteOption()
	{
		if (mEditVignetteOption == null)
		{
			mEditVignetteOption = new TuEditVignetteOption();
			// 保存到临时文件
			mEditVignetteOption.setSaveToTemp(true);
		}
		return mEditVignetteOption;
	}

	/**
	 * 圣光控制器配置选项
	 * // 保存到临时文件<br>
	 * mEditHolyLightOption.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditVignetteOption
	 */
	public TuEditHolyLightOption editHolyLightOption()
	{
		if (mEditHolyLightOption == null)
		{
			mEditHolyLightOption = new TuEditHolyLightOption();
			// 保存到临时文件
			mEditHolyLightOption.setSaveToTemp(true);
		}
		return mEditHolyLightOption;
	}
	
	/**
	 * HDR控制器配置选项
	 * // 保存到临时文件<br>
	 * mEditHDROption.setSaveToTemp(true);<br>
	 * 
	 * @return the mEditHDROption
	 */
	public TuEditHDROption editHDROption()
	{
		if (mEditHDROption == null)
		{
			mEditHDROption = new TuEditHDROption();
			// 保存到临时文件
			mEditHDROption.setSaveToTemp(true);
		}
		return mEditHDROption;
	}
}