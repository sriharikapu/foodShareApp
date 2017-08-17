package com.fengnian.smallyellowo.foodie.appconfig;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplatePictureRate.SYReleaseTemplatePictureRate_None;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateSort.SYReleaseTemplateSort_Allow;

public class RichTextModel extends RichTextModelConfig.SYBaseReleaseTemplate implements Parcelable, Serializable {
    //
    public static RichTextModelConfig.SYBaseReleaseTemplatePicture initSYBaseReleaseTemplatePicture() {
        RichTextModelConfig.SYBaseReleaseTemplatePicture instance = new RichTextModelConfig.SYBaseReleaseTemplatePicture();
        instance.pictureCount = -1;
        instance.pictureRate = SYReleaseTemplatePictureRate_None;
        return instance;
    }

    //
    public static RichTextModelConfig.SYBaseReleaseTemplatePictureAndText initSYBaseReleaseTemplatePictureAndText() {
        RichTextModelConfig.SYBaseReleaseTemplatePictureAndText instance = new RichTextModelConfig.SYBaseReleaseTemplatePictureAndText();
        instance.bHaveText = true;
        instance.textWordCount = 110;
        return instance;
    }


    //
    public static RichTextModelConfig.SYBaseReleaseTemplateTextWindow initSYBaseReleaseTemplateTextWindow() {
        RichTextModelConfig.SYBaseReleaseTemplateTextWindow instance = new RichTextModelConfig.SYBaseReleaseTemplateTextWindow();
        instance.textWindowCount = -1;
        instance.textWindowWordCount = 500;
        return instance;
    }

    //
    public static RichTextModelConfig.SYBaseReleaseTemplateTitle initSYBaseReleaseTemplateTitle() {
        RichTextModelConfig.SYBaseReleaseTemplateTitle instance = new RichTextModelConfig.SYBaseReleaseTemplateTitle();
        instance.bHaveTitle = true;
        instance.titleWordCount = 18;
        return instance;
    }

    //
    RichTextModelConfig.SYBaseReleaseTemplateDishesName initSYBaseReleaseTemplateDishesName() {
        RichTextModelConfig.SYBaseReleaseTemplateDishesName instance = new RichTextModelConfig.SYBaseReleaseTemplateDishesName();
        instance.bHaveDishes = true;
        instance.dishesNameWordCount = 20;
        return instance;
    }


    // SYBaseReleaseTemplate
    public RichTextModel() {
        picture = initSYBaseReleaseTemplatePicture();
        pictureText = initSYBaseReleaseTemplatePictureAndText();
        textWindow = initSYBaseReleaseTemplateTextWindow();
        title = initSYBaseReleaseTemplateTitle();
        dishes = initSYBaseReleaseTemplateDishesName();
        bHaveHeadImage = true;
        sort = SYReleaseTemplateSort_Allow;
    }
//
//    /**
//     * 是否多图
//     *
//     * @return
//     */
//    @Override
//    public boolean isMultiJPGCategory() {
//        return (releaseTemplateCategory & SYReleaseTemplateCategore_MultiJPG) != 0;
//    }
//
//    /**
//     * 是否少图
//     *
//     * @return
//     */
//    @Override
//    public boolean isLeeJPGCategory() {
//        return (releaseTemplateCategory & SYReleaseTemplateCategore_LessJPG) != 0;
//    }
//
//    /**
//     * 是否需中式
//     *
//     * @return
//     */
//    @Override
//    public boolean isChineseCategory() {
//        return (releaseTemplateCategory & SYReleaseTemplateCategore_ChineseFood) != 0;
//    }
//
//    /**
//     * 是否是西式
//     *
//     * @return
//     */
//    @Override
//    public boolean isWesternCategory() {
//        return (releaseTemplateCategory & SYReleaseTemplateCategore_WesternFood) != 0;
//    }

    @Override
    public boolean isSupportScrool() {
//        return getReleaseTemplateType() != SYReleaseTemplateType_Brief2 && getReleaseTemplateType() != SYReleaseTemplateType_Brief2_2;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected RichTextModel(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<RichTextModel> CREATOR = new Parcelable.Creator<RichTextModel>() {
        @Override
        public RichTextModel createFromParcel(Parcel source) {
            return new RichTextModel(source);
        }

        @Override
        public RichTextModel[] newArray(int size) {
            return new RichTextModel[size];
        }
    };
}