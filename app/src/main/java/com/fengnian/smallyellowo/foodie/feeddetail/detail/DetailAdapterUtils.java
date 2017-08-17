package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.content.Context;

import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextPhotoModel;

/**
 * Created by chenglin on 2017-3-19.
 */

public class DetailAdapterUtils {
    public static int getImageWidth(SYRichTextPhotoModel photoModel) {
        SYImage img = photoModel.getPhoto().getImageAsset().pullProcessedImage().getImage();
        float bitmapWidth = img.getWidth();

        if (bitmapWidth <= 0) {
            SYImage imgOriginal = photoModel.getPhoto().getImageAsset().getOriginalImage().getImage();
            bitmapWidth = imgOriginal.getWidth();
        }
        return (int) bitmapWidth;
    }

    public static int getImageHeight(SYRichTextPhotoModel photoModel) {
        SYImage img = photoModel.getPhoto().getImageAsset().pullProcessedImage().getImage();
        float bitmapHeight = img.getHeight();

        if (bitmapHeight <= 0) {
            SYImage imgOriginal = photoModel.getPhoto().getImageAsset().getOriginalImage().getImage();
            bitmapHeight = imgOriginal.getHeight();
        }

        return (int) bitmapHeight;
    }
}
