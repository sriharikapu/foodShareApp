package com.fengnian.smallyellowo.foodie.appconfig;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2_2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Modern;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Pomo;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Standard;

/**
 * Created by Administrator on 2017-2-16.
 */

public class RichTextModelConfig {

    /**
     * 模板样式
     */
    public interface SYReleaseTemplateType {
        /**
         * 标准
         */
        int SYReleaseTemplateType_Standard = 0;

        /**
         * 现代
         */
        int SYReleaseTemplateType_Modern = 1;

        /**
         * 泼墨
         */
        int SYReleaseTemplateType_Pomo = 2;

        /**
         * 中式
         */
        int SYReleaseTemplateType_Chinese = 3;

        /**
         * 中式2
         */
        int SYReleaseTemplateType_Chinese2 = 4;

        /**
         * 简短
         */
        int SYReleaseTemplateType_Brief = 5;

        /**
         * 简短2
         */
        int SYReleaseTemplateType_Brief2 = 6;

        /**
         * 简短2-2
         */
        int SYReleaseTemplateType_Brief2_2 = 7;
    }

    /**
     * 模板分类
     */
    public interface SYReleaseTemplateCategore {
        int SYReleaseTemplateCategore_None = 0;
        /**
         * 现代
         */
        int SYReleaseTemplateCategore_modern = 1 << 0;

        /**
         * 古风
         */
        int SYReleaseTemplateCategore_classic = 1 << 1;

        /**
         * 简约
         */
        int SYReleaseTemplateCategore_simple = 1 << 2;
    }

    /**
     * 图片比率
     */
    public interface SYReleaseTemplatePictureRate {
        /**
         * 不限
         */
        int SYReleaseTemplatePictureRate_None = 0;

        /**
         * 小于1:1
         */
        int SYReleaseTemplatePictureRate_Less_1x1 = 1;

        /**
         * 等于1:1
         */
        int SYReleaseTemplatePictureRate_Equal_1x1 = 2;

        /**
         * 大于1:1
         */
        int SYReleaseTemplatePictureRate_Greater_1x1 = 3;
    }

    /**
     * 是否允许排序
     */
    public interface SYReleaseTemplateSort {
        /**
         * 允许
         */
        int SYReleaseTemplateSort_Allow = 0;

        /**
         * 部分允许
         */
        int SYReleaseTemplateSort_HalfAllow = 1;

        /**
         * 不允许
         */
        int SYReleaseTemplateSort_NotAllow = 2;
    }

    ;

    /**
     * 图片约束
     */
    public static class SYBaseReleaseTemplatePicture implements Parcelable, Serializable {

        /**
         * 图片张数;-1为不限
         */
        public int pictureCount;

        /**
         * 图片比例
         * SYReleaseTemplatePictureRate
         */
        public int pictureRate;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.pictureCount);
            dest.writeInt(this.pictureRate);
        }

        public SYBaseReleaseTemplatePicture() {
        }

        protected SYBaseReleaseTemplatePicture(Parcel in) {
            this.pictureCount = in.readInt();
            this.pictureRate = in.readInt();
        }

        public static final Parcelable.Creator<SYBaseReleaseTemplatePicture> CREATOR = new Parcelable.Creator<SYBaseReleaseTemplatePicture>() {
            @Override
            public SYBaseReleaseTemplatePicture createFromParcel(Parcel source) {
                return new SYBaseReleaseTemplatePicture(source);
            }

            @Override
            public SYBaseReleaseTemplatePicture[] newArray(int size) {
                return new SYBaseReleaseTemplatePicture[size];
            }
        };
    }

    /**
     * 图配文约束
     */
    public static class SYBaseReleaseTemplatePictureAndText implements Parcelable, Serializable {

        /**
         * 图片是否配文字
         */
        public boolean bHaveText;

        /**
         * 图片配文字的最大文字个数，-1为不限
         */
        public int textWordCount;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.bHaveText ? (byte) 1 : (byte) 0);
            dest.writeInt(this.textWordCount);
        }

        public SYBaseReleaseTemplatePictureAndText() {
        }

        protected SYBaseReleaseTemplatePictureAndText(Parcel in) {
            this.bHaveText = in.readByte() != 0;
            this.textWordCount = in.readInt();
        }

        public static final Parcelable.Creator<SYBaseReleaseTemplatePictureAndText> CREATOR = new Parcelable.Creator<SYBaseReleaseTemplatePictureAndText>() {
            @Override
            public SYBaseReleaseTemplatePictureAndText createFromParcel(Parcel source) {
                return new SYBaseReleaseTemplatePictureAndText(source);
            }

            @Override
            public SYBaseReleaseTemplatePictureAndText[] newArray(int size) {
                return new SYBaseReleaseTemplatePictureAndText[size];
            }
        };
    }

    /**
     * 文字框约束
     */
    public static class SYBaseReleaseTemplateTextWindow implements Parcelable, Serializable {
        /**
         * 文字框个数限制 -1为不限
         */
        public int textWindowCount;

        /**
         * 文字框字数约束 -1为不限
         */
        public int textWindowWordCount;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.textWindowCount);
            dest.writeInt(this.textWindowWordCount);
        }

        public SYBaseReleaseTemplateTextWindow() {
        }

        protected SYBaseReleaseTemplateTextWindow(Parcel in) {
            this.textWindowCount = in.readInt();
            this.textWindowWordCount = in.readInt();
        }

        public static final Parcelable.Creator<SYBaseReleaseTemplateTextWindow> CREATOR = new Parcelable.Creator<SYBaseReleaseTemplateTextWindow>() {
            @Override
            public SYBaseReleaseTemplateTextWindow createFromParcel(Parcel source) {
                return new SYBaseReleaseTemplateTextWindow(source);
            }

            @Override
            public SYBaseReleaseTemplateTextWindow[] newArray(int size) {
                return new SYBaseReleaseTemplateTextWindow[size];
            }
        };
    }

    /**
     * 标题约束
     */
    public static class SYBaseReleaseTemplateTitle implements Parcelable, Serializable {

        /**
         * 是否存在标题
         */
        boolean bHaveTitle;

        /**
         * 标题的字数约束 <0为不限
         */
        int titleWordCount;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.bHaveTitle ? (byte) 1 : (byte) 0);
            dest.writeInt(this.titleWordCount);
        }

        public SYBaseReleaseTemplateTitle() {
        }

        protected SYBaseReleaseTemplateTitle(Parcel in) {
            this.bHaveTitle = in.readByte() != 0;
            this.titleWordCount = in.readInt();
        }

        public static final Parcelable.Creator<SYBaseReleaseTemplateTitle> CREATOR = new Parcelable.Creator<SYBaseReleaseTemplateTitle>() {
            @Override
            public SYBaseReleaseTemplateTitle createFromParcel(Parcel source) {
                return new SYBaseReleaseTemplateTitle(source);
            }

            @Override
            public SYBaseReleaseTemplateTitle[] newArray(int size) {
                return new SYBaseReleaseTemplateTitle[size];
            }
        };
    }

    /**
     * 菜品名称约束
     */
    public static class SYBaseReleaseTemplateDishesName implements Parcelable, Serializable {
        /**
         * 是否存在菜品
         */
        public boolean bHaveDishes;

        /**
         * 菜品的最大字数; <0没有约束
         */
        public int dishesNameWordCount;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.bHaveDishes ? (byte) 1 : (byte) 0);
            dest.writeInt(this.dishesNameWordCount);
        }

        public SYBaseReleaseTemplateDishesName() {
        }

        protected SYBaseReleaseTemplateDishesName(Parcel in) {
            this.bHaveDishes = in.readByte() != 0;
            this.dishesNameWordCount = in.readInt();
        }

        public static final Parcelable.Creator<SYBaseReleaseTemplateDishesName> CREATOR = new Parcelable.Creator<SYBaseReleaseTemplateDishesName>() {
            @Override
            public SYBaseReleaseTemplateDishesName createFromParcel(Parcel source) {
                return new SYBaseReleaseTemplateDishesName(source);
            }

            @Override
            public SYBaseReleaseTemplateDishesName[] newArray(int size) {
                return new SYBaseReleaseTemplateDishesName[size];
            }
        };
    }


    /**
     * 基础模板
     */
    public abstract static class SYBaseReleaseTemplate implements Parcelable, Serializable {

        /**
         * 序号
         */
        public int indexCode;

        /**
         * 模板名称
         */
        public String templateName;

        public int getReleaseTemplateType() {
            return releaseTemplateType;
        }

        public void setReleaseTemplateType(int releaseTemplateType) {
            this.releaseTemplateType = releaseTemplateType;
            switch (releaseTemplateType) {
                case SYReleaseTemplateType_Standard:
                    RichTextModelManager.initStandard(this);
                    break;
                case SYReleaseTemplateType_Brief:
                    RichTextModelManager.initShort(this);
                    break;
                case SYReleaseTemplateType_Brief2:
                    RichTextModelManager.initShort2(this);
                    break;
                case SYReleaseTemplateType_Brief2_2:
                    RichTextModelManager.initShort2_2(this);
                    break;
                case SYReleaseTemplateType_Chinese:
                    RichTextModelManager.initChinese(this);
                    break;
                case SYReleaseTemplateType_Chinese2:
                    RichTextModelManager.initChinese2(this);
                    break;
                case SYReleaseTemplateType_Modern:
                    RichTextModelManager.initNow(this);
                    break;
                case SYReleaseTemplateType_Pomo:
                    RichTextModelManager.initPM(this);
                    break;
            }


        }

        /**
         * 模板样式
         * SYReleaseTemplateType
         */
        private int releaseTemplateType;

        /**
         * 模板分类
         * SYReleaseTemplateCategore
         */
        public int releaseTemplateCategory;

        /**
         * 图片约束
         */
        public SYBaseReleaseTemplatePicture picture;

        /**
         * 图文约束
         */
        public SYBaseReleaseTemplatePictureAndText pictureText;

        /**
         * 文字框约束
         */
        public SYBaseReleaseTemplateTextWindow textWindow;

        /**
         * 是否存在头图约束
         */
        public boolean bHaveHeadImage;

        /**
         * 排序约束
         * SYReleaseTemplateSort
         */
        public int sort;

        /**
         * 标题约束
         */
        public SYBaseReleaseTemplateTitle title;

        /**
         * 菜品约束
         */
        public SYBaseReleaseTemplateDishesName dishes;

//        /**
//         * 是否是多图分类
//         * return YES 是 否则不是
//         */
//        public abstract boolean isMultiJPGCategory();
//
//        /**
//         * 是否是少图分类
//         * return YES 是 否则不是
//         */
//        public abstract boolean isLeeJPGCategory();
//
//        /**
//         * 是否是中式分类
//         * return YES 是 否则不是
//         */
//        public abstract boolean isChineseCategory();
//
//        /**
//         * 是否是西式分类
//         * return YES 是 否则不是
//         */
//        public abstract boolean isWesternCategory();

        public abstract boolean isSupportScrool();

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.indexCode);
            dest.writeString(this.templateName);
            dest.writeInt(this.releaseTemplateType);
            dest.writeInt(this.releaseTemplateCategory);
            dest.writeParcelable(this.picture, flags);
            dest.writeParcelable(this.pictureText, flags);
            dest.writeParcelable(this.textWindow, flags);
            dest.writeByte(this.bHaveHeadImage ? (byte) 1 : (byte) 0);
            dest.writeInt(this.sort);
            dest.writeParcelable(this.title, flags);
            dest.writeParcelable(this.dishes, flags);
        }

        public SYBaseReleaseTemplate() {
        }

        protected SYBaseReleaseTemplate(Parcel in) {
            this.indexCode = in.readInt();
            this.templateName = in.readString();
            this.releaseTemplateType = in.readInt();
            this.releaseTemplateCategory = in.readInt();
            this.picture = in.readParcelable(SYBaseReleaseTemplatePicture.class.getClassLoader());
            this.pictureText = in.readParcelable(SYBaseReleaseTemplatePictureAndText.class.getClassLoader());
            this.textWindow = in.readParcelable(SYBaseReleaseTemplateTextWindow.class.getClassLoader());
            this.bHaveHeadImage = in.readByte() != 0;
            this.sort = in.readInt();
            this.title = in.readParcelable(SYBaseReleaseTemplateTitle.class.getClassLoader());
            this.dishes = in.readParcelable(SYBaseReleaseTemplateDishesName.class.getClassLoader());
        }
    }
}