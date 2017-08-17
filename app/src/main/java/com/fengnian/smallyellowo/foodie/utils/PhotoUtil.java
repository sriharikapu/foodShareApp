package com.fengnian.smallyellowo.foodie.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.fan.framework.utils.FileUitl;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;

import java.io.File;


public class PhotoUtil {
    private static String tag = PhotoUtil.class.getSimpleName();

    public static File getPhotoByTaking(Activity activity) {

        File photo = FileUitl.generateFile("cir");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(MediaStore.Images.Media.DATA, photo.getAbsolutePath());
        Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, Constants.PHOTO_GRAPH);
        return photo;
    }

    public static void getPhotoFromAlbum(Activity activity, int tag) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        activity.startActivityForResult(intent, tag);
    }


    public static String getPathByUri(Activity activity, Uri uri) {
        String path = null;
        if (uri == null) {
            return path;
        }
        if ("content".equals(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.managedQuery(uri, projection,  null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } else if ("file".equals(uri.getScheme())) {
            path = uri.getPath();
        }
        return path;
    }

}
