/**
 * TuSDKCore
 * TuEditCuterOption.java
 *
 * @author Clear
 * @Date 2014-12-25 上午11:17:26
 * @Copyright (c) 2014 tusdk.com. All rights reserved.
 */
package com.fengnian.smallyellowo.foodie.fragments;

import org.lasque.tusdk.core.utils.image.RatioType;
import org.lasque.tusdk.impl.activity.TuImageResultOption;

/**
 * 图片编辑裁切旋转控制器配置选项
 *
 * @author Clear
 */
public class MyEditCuterOption extends TuImageResultOption {
    /**
     * 图片编辑裁切旋转控制器配置选项
     */
    public MyEditCuterOption() {

    }

    public static MyEditCuterFragment editCuterOption() {
        MyEditCuterOption mEditCuterOption = new MyEditCuterOption();
        // 是否开启图片旋转(默认: false)
        mEditCuterOption.setEnableTrun(true);
        // 是否开启图片镜像(默认: false)
        mEditCuterOption.setEnableMirror(false);
        // 裁剪比例 (默认:RatioType.ratio_default)
        mEditCuterOption.setRatioType(RatioType.ratio_default);
        /**
         *  裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.defaultRatioTypes)
         *  设置 int 型数组来控制显示的按钮顺序， 例如:
         *	new int[] {RatioType.ratio_orgin, RatioType.ratio_1_1, RatioType.ratio_2_3, RatioType.ratio_3_4}
         */
        mEditCuterOption.setRatioTypeList(new int[]{RatioType.ratio_orgin,RatioType.ratio_1_1, RatioType.ratio_2_3, RatioType.ratio_3_4, RatioType.ratio_9_16});
        // 保存到临时文件
        mEditCuterOption.setSaveToTemp(true);
        return mEditCuterOption.fragment();
    }

    /**
     * 图片编辑裁切旋转控制器类
     *
     * @return 图片编辑裁切旋转控制器类 (默认: MyEditCuterFragment，如需自定义请继承自 MyEditCuterFragment)
     */
    @Override
    protected Class<?> getDefaultComponentClazz() {
        return MyEditCuterFragment.class;
    }

    /**
     * 获取默认根视图布局资源ID
     *
     * @return 根视图布局资源ID (默认: tusdk_impl_component_edit_cuter_fragment)
     * @see # {@link MyEditCuterFragment}
     */
    @Override
    protected int getDefaultRootViewLayoutId() {
        return MyEditCuterFragment.getLayoutId();
    }

    /********************************** Config ***********************************/
    /**
     * 是否开启图片旋转 (默认: false)
     */
    private boolean mEnableTrun;
    /**
     * 是否开启图片镜像(默认: false)
     */
    private boolean mEnableMirror;
    /**
     * 裁剪比例类型 (默认:RatioType.ratio_all)
     */
    private int mRatioType;
    /**
     * 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes)
     * <p>
     * 设置 int 型数组来控制显示的按钮顺序， 例如:
     * new int[] {RatioType.ratio_orgin, RatioType.ratio_1_1, RatioType.ratio_2_3, RatioType.ratio_3_4}
     */
    private int[] mRatioTypeList;

    /**
     * 是否仅返回裁切参数，不返回处理图片
     */
    private boolean mOnlyReturnCuter;

    /**
     * 是否开启图片旋转(默认: false)
     */
    public final boolean isEnableTrun() {
        return mEnableTrun;
    }

    /**
     * 是否开启图片旋转(默认: false)
     */
    public final void setEnableTrun(boolean mEnableTrun) {
        this.mEnableTrun = mEnableTrun;
    }

    /**
     * 是否开启图片镜像(默认: false)
     */
    public final boolean isEnableMirror() {
        return mEnableMirror;
    }

    /**
     * 是否开启图片镜像(默认: false)
     */
    public final void setEnableMirror(boolean mEnableMirror) {
        this.mEnableMirror = mEnableMirror;
    }

    /**
     * 裁剪比例类型 (默认:RatioType.ratio_all)
     */
    public final int getRatioType() {
        return mRatioType;
    }

    /**
     * 裁剪比例类型 (默认:RatioType.ratio_all)
     */
    public final void setRatioType(int mRatioType) {
        this.mRatioType = mRatioType;
    }

    /**
     * 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes)
     */
    public final int[] getRatioTypeList() {
        return mRatioTypeList;
    }

    /**
     * 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes)
     */
    public final void setRatioTypeList(int[] mRatioTypeList) {
        this.mRatioTypeList = mRatioTypeList;
    }

    /**
     * 是否仅返回裁切参数，不返回处理图片
     */
    public boolean isOnlyReturnCuter() {
        return mOnlyReturnCuter;
    }

    /**
     * 是否仅返回裁切参数，不返回处理图片
     */
    public void setOnlyReturnCuter(boolean mOnlyReturnCuter) {
        this.mOnlyReturnCuter = mOnlyReturnCuter;
    }

    /**
     * 创建图片编辑裁切旋转控制器对象
     */
    public MyEditCuterFragment fragment() {
        MyEditCuterFragment fragment = this.fragmentInstance();
        // 是否开启图片旋转(默认: false)
        fragment.setEnableTrun(this.isEnableTrun());

        fragment.setShowResultPreview(false);
        // 是否开启图片镜像(默认: false)
        fragment.setEnableMirror(this.isEnableMirror());
        // 裁剪比例 (默认:RatioType.ratio_all)
        fragment.setRatioType(this.getRatioType());
        // 裁剪比例类型列表 ( 优先级 RatioTypeList > RatioType, 默认：RatioType.ratioTypes)
        fragment.setRatioTypeList(this.getRatioTypeList());
        // 是否仅返回裁切参数，不返回处理图片
        fragment.setOnlyReturnCuter(this.isOnlyReturnCuter());
        return fragment;
    }
}