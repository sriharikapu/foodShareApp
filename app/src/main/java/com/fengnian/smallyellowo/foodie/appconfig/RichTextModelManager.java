package com.fengnian.smallyellowo.foodie.appconfig;

import java.util.ArrayList;

import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateCategore.SYReleaseTemplateCategore_None;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateCategore.SYReleaseTemplateCategore_classic;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateCategore.SYReleaseTemplateCategore_modern;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateCategore.SYReleaseTemplateCategore_simple;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplatePictureRate.SYReleaseTemplatePictureRate_Equal_1x1;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplatePictureRate.SYReleaseTemplatePictureRate_Less_1x1;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplatePictureRate.SYReleaseTemplatePictureRate_None;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateSort.SYReleaseTemplateSort_Allow;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateSort.SYReleaseTemplateSort_HalfAllow;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Brief2_2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Chinese2;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Modern;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Pomo;
import static com.fengnian.smallyellowo.foodie.appconfig.RichTextModelConfig.SYReleaseTemplateType.SYReleaseTemplateType_Standard;


public class RichTextModelManager {
    private static ArrayList<RichTextModel> models = null;
    private static final RichTextModel tempLatePomo = new RichTextModel();

    public static ArrayList<RichTextModel> getModels() {
        if (models == null || models.isEmpty()) {
            boolean is = models == null;
            models = new ArrayList<>();
            if (is) {
                init();
            }
        }
        return models;
    }


    private static void init() {
        //标准
        RichTextModel tempLate1 = new RichTextModel();
        tempLate1.setReleaseTemplateType(SYReleaseTemplateType_Standard);
        getModels().add(tempLate1);
        //现代
        RichTextModel tempLate2 = new RichTextModel();
        tempLate2.setReleaseTemplateType(SYReleaseTemplateType_Modern);
        getModels().add(tempLate2);
        //泼墨
//        RichTextModel tempLate3 = new RichTextModel();
        tempLatePomo.setReleaseTemplateType(SYReleaseTemplateType_Pomo);
//        getModels().add(tempLate3);
        //中式
        RichTextModel tempLate4 = new RichTextModel();
        tempLate4.setReleaseTemplateType(SYReleaseTemplateType_Chinese);
        getModels().add(tempLate4);
        //中式2
        RichTextModel tempLate5 = new RichTextModel();
        tempLate5.setReleaseTemplateType(SYReleaseTemplateType_Chinese2);
        getModels().add(tempLate5);
        //简短
        RichTextModel tempLate6 = new RichTextModel();
        tempLate6.setReleaseTemplateType(SYReleaseTemplateType_Brief);
        getModels().add(tempLate6);
        //简短2
        RichTextModel tempLate7 = new RichTextModel();
        tempLate7.setReleaseTemplateType(SYReleaseTemplateType_Brief2);
        getModels().add(tempLate7);
        //简短2-2
        RichTextModel tempLate8 = new RichTextModel();
        tempLate8.setReleaseTemplateType(SYReleaseTemplateType_Brief2_2);
        getModels().add(tempLate8);
    }

    public static void initShort2_2(RichTextModelConfig.SYBaseReleaseTemplate tempLate) {
        tempLate.indexCode = 8;
        tempLate.releaseTemplateCategory = releaseLateCategoryWithType(tempLate.getReleaseTemplateType());
        tempLate.picture.pictureCount = -1;
        tempLate.picture.pictureRate = SYReleaseTemplatePictureRate_Equal_1x1;
        tempLate.pictureText.bHaveText = true;
        tempLate.pictureText.textWordCount = 167;
        tempLate.textWindow.textWindowCount = 1;
        tempLate.textWindow.textWindowWordCount = 500;
        tempLate.bHaveHeadImage = true;
        tempLate.sort = SYReleaseTemplateSort_Allow;
        tempLate.title.bHaveTitle = true;
        tempLate.title.titleWordCount = 18;
        tempLate.dishes.bHaveDishes = true;
        tempLate.dishes.dishesNameWordCount = 20;
        tempLate.templateName = releaseLatesTitleWith(tempLate.getReleaseTemplateType());
    }

    public static void initShort2(RichTextModelConfig.SYBaseReleaseTemplate tempLate) {
        tempLate.indexCode = 7;
        tempLate.releaseTemplateCategory = releaseLateCategoryWithType(tempLate.getReleaseTemplateType());
        tempLate.picture.pictureCount = -1;
        tempLate.picture.pictureRate = SYReleaseTemplatePictureRate_None;
        tempLate.pictureText.bHaveText = true;
        tempLate.pictureText.textWordCount = 167;
        tempLate.textWindow.textWindowCount = -1;
        tempLate.textWindow.textWindowWordCount = 500;
        tempLate.bHaveHeadImage = true;
        tempLate.sort = SYReleaseTemplateSort_Allow;
        tempLate.title.bHaveTitle = true;
        tempLate.title.titleWordCount = 18;
        tempLate.dishes.bHaveDishes = true;
        tempLate.dishes.dishesNameWordCount = 20;
        tempLate.templateName = releaseLatesTitleWith(tempLate.getReleaseTemplateType());
    }

    public static void initShort(RichTextModelConfig.SYBaseReleaseTemplate tempLate) {
        tempLate.indexCode = 6;
        tempLate.releaseTemplateCategory = releaseLateCategoryWithType(tempLate.getReleaseTemplateType());
        tempLate.picture.pictureCount = -1;
        tempLate.picture.pictureRate = SYReleaseTemplatePictureRate_None;
        tempLate.pictureText.bHaveText = true;
        tempLate.pictureText.textWordCount = 167;
        tempLate.textWindow.textWindowCount = -1;
        tempLate.textWindow.textWindowWordCount = 500;
        tempLate.bHaveHeadImage = false;
        tempLate.sort = SYReleaseTemplateSort_Allow;
        tempLate.title.bHaveTitle = true;
        tempLate.title.titleWordCount = 18;
        tempLate.dishes.bHaveDishes = true;
        tempLate.dishes.dishesNameWordCount = 20;
        tempLate.templateName = releaseLatesTitleWith(tempLate.getReleaseTemplateType());
    }

    public static void initChinese2(RichTextModelConfig.SYBaseReleaseTemplate tempLate) {
        tempLate.indexCode = 5;
        tempLate.releaseTemplateCategory = releaseLateCategoryWithType(tempLate.getReleaseTemplateType());
        tempLate.picture.pictureCount = -1;
        tempLate.picture.pictureRate = SYReleaseTemplatePictureRate_Less_1x1;
        tempLate.pictureText.bHaveText = true;
        tempLate.pictureText.textWordCount = 167;
        tempLate.textWindow.textWindowCount = -1;
        tempLate.textWindow.textWindowWordCount = 500;
        tempLate.bHaveHeadImage = true;
        tempLate.sort = SYReleaseTemplateSort_Allow;
        tempLate.title.bHaveTitle = true;
        tempLate.title.titleWordCount = 18;
        tempLate.dishes.bHaveDishes = true;
        tempLate.dishes.dishesNameWordCount = 20;
        tempLate.templateName = releaseLatesTitleWith(tempLate.getReleaseTemplateType());
    }

    public static void initChinese(RichTextModelConfig.SYBaseReleaseTemplate tempLate) {
        tempLate.indexCode = 4;
        tempLate.releaseTemplateCategory = releaseLateCategoryWithType(tempLate.getReleaseTemplateType());
        tempLate.picture.pictureCount = -1;
        tempLate.picture.pictureRate = SYReleaseTemplatePictureRate_None;
        tempLate.pictureText.bHaveText = true;
        tempLate.pictureText.textWordCount = 167;
        tempLate.textWindow.textWindowCount = -1;
        tempLate.textWindow.textWindowWordCount = 500;
        tempLate.bHaveHeadImage = true;
        tempLate.sort = SYReleaseTemplateSort_Allow;
        tempLate.title.bHaveTitle = true;
        tempLate.title.titleWordCount = 18;
        tempLate.dishes.bHaveDishes = true;
        tempLate.dishes.dishesNameWordCount = 20;
        tempLate.templateName = releaseLatesTitleWith(tempLate.getReleaseTemplateType());
    }

    public static void initPM(RichTextModelConfig.SYBaseReleaseTemplate tempLate) {
        tempLate.indexCode = 3;
        tempLate.releaseTemplateCategory = releaseLateCategoryWithType(tempLate.getReleaseTemplateType());
        tempLate.picture.pictureCount = -1;
        tempLate.picture.pictureRate = SYReleaseTemplatePictureRate_None;
        tempLate.pictureText.bHaveText = true;
        tempLate.pictureText.textWordCount = 167;
        tempLate.textWindow.textWindowCount = -1;
        tempLate.textWindow.textWindowWordCount = 500;
        tempLate.bHaveHeadImage = true;
        tempLate.sort = SYReleaseTemplateSort_Allow;
        tempLate.title.bHaveTitle = true;
        tempLate.title.titleWordCount = 18;
        tempLate.dishes.bHaveDishes = true;
        tempLate.dishes.dishesNameWordCount = 20;
        tempLate.templateName = releaseLatesTitleWith(tempLate.getReleaseTemplateType());
    }

    public static void initNow(RichTextModelConfig.SYBaseReleaseTemplate tempLate) {
        tempLate.indexCode = 2;
        tempLate.releaseTemplateCategory = releaseLateCategoryWithType(tempLate.getReleaseTemplateType());
        tempLate.picture.pictureCount = -1;
        tempLate.picture.pictureRate = SYReleaseTemplatePictureRate_None;
        tempLate.pictureText.bHaveText = true;
        tempLate.pictureText.textWordCount = 167;
        tempLate.textWindow.textWindowCount = -1;
        tempLate.textWindow.textWindowWordCount = 500;
        tempLate.bHaveHeadImage = true;
        tempLate.sort = SYReleaseTemplateSort_Allow;
        tempLate.title.bHaveTitle = true;
        tempLate.title.titleWordCount = 18;
        tempLate.dishes.bHaveDishes = true;
        tempLate.dishes.dishesNameWordCount = 20;
        tempLate.templateName = releaseLatesTitleWith(tempLate.getReleaseTemplateType());
    }

    public static void initStandard(RichTextModelConfig.SYBaseReleaseTemplate tempLate) {
        tempLate.indexCode = 1;
        tempLate.releaseTemplateCategory = releaseLateCategoryWithType(tempLate.getReleaseTemplateType());
        tempLate.picture.pictureCount = -1;
        tempLate.picture.pictureRate = SYReleaseTemplatePictureRate_None;
        tempLate.pictureText.bHaveText = true;
        tempLate.pictureText.textWordCount = 167;
        tempLate.textWindow.textWindowCount = -1;
        tempLate.textWindow.textWindowWordCount = 500;
        tempLate.bHaveHeadImage = true;
        tempLate.sort = SYReleaseTemplateSort_Allow;
        tempLate.title.bHaveTitle = true;
        tempLate.title.titleWordCount = 18;
        tempLate.dishes.bHaveDishes = true;
        tempLate.dishes.dishesNameWordCount = 20;
        tempLate.templateName = releaseLatesTitleWith(tempLate.getReleaseTemplateType());
    }

    public static int releaseLateCategoryWithType(int type) {
        int category = SYReleaseTemplateCategore_None;
        if (type == SYReleaseTemplateType_Standard) {
            category = SYReleaseTemplateCategore_modern;
        }
        if (type == SYReleaseTemplateType_Modern) {
            category = SYReleaseTemplateCategore_modern;
        }
        if (type == SYReleaseTemplateType_Pomo) {
            category = SYReleaseTemplateCategore_classic;
        }
        if (type == SYReleaseTemplateType_Chinese) {
            category = SYReleaseTemplateCategore_classic;
        }
        if (type == SYReleaseTemplateType_Chinese2) {
            category = SYReleaseTemplateCategore_classic;
        }
        if (type == SYReleaseTemplateType_Brief) {
            category = SYReleaseTemplateCategore_simple;
        }
        if (type == SYReleaseTemplateType_Brief2) {
            category = SYReleaseTemplateCategore_simple;
        }
        if (type == SYReleaseTemplateType_Brief2_2) {
            category = SYReleaseTemplateCategore_simple;
        }
        category |= SYReleaseTemplateCategore_None;
        return category;
    }

    public static RichTextModel getConfigByIndex(int index) {
        for (int i = 0; i < getModels().size(); i++) {
            if (getModels().get(i).indexCode == index) {
                return getModels().get(i);
            }
        }
        if (index == tempLatePomo.indexCode) {
            return tempLatePomo;
        }
        return null;
    }


    //根据模板类型获取模板标题
    public static String releaseLatesTitleWith(int type) {
        String title = "";
        if (type == SYReleaseTemplateType_Standard) {
            title = "标准";
        }
        if (type == SYReleaseTemplateType_Modern) {
            title = "现代";
        }
        if (type == SYReleaseTemplateType_Pomo) {
            title = "泼墨";
        }
        if (type == SYReleaseTemplateType_Chinese) {
            title = "中式";
        }
        if (type == SYReleaseTemplateType_Chinese2) {
            title = "中式2";
        }
        if (type == SYReleaseTemplateType_Brief) {
            title = "简短";
        }
        if (type == SYReleaseTemplateType_Brief2) {
            title = "简短2";
        }
        if (type == SYReleaseTemplateType_Brief2_2) {
            title = "简短2-2";
        }
        return title;
    }

}
