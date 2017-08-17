package com.fan.framework.select_picture;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.imageloader.FFImageLoader;
import com.fan.framework.select_picture.NativeImage;
import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 返回路径 "path" String 类型
 */
public class AllFolderImagesActivity extends BaseActivity<IntentData> {

    ArrayList<NativeImage> images;
    ArrayList<String> paths;
    @Bind(R.id.tv_middle_title)
    TextView tvMiddleTitle;
    @Bind(R.id.listView)
    ListView listView;
    private HashMap<String, ArrayList<NativeImage>> folderFile;
    private AllFolderAdapter adapterAll;
    private SingleFolderAdapter adapterSingle;

    public void setLastFolder(String lastFolder) {
        this.lastFolder = lastFolder;
        tvMiddleTitle.setText(lastFolder);
    }

    private String lastFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = getImages();
        setNotitle(true);
        if (images.size() == 0) {
            showToast("没有图片", null);
            TextView view = new TextView(this);
            view.setText("没有图片");
            setContentView(view);
            return;
        }
        setContentView(R.layout.ff_listview_apply_all);
        ButterKnife.bind(this);
        paths = new ArrayList<String>();
        final LayoutInflater inflater = getLayoutInflater();
        listView = ((ListView) findViewById(R.id.listView));
        listView.setDivider(null);
        adapterAll = new AllFolderAdapter();
        adapterSingle = new SingleFolderAdapter();
        getPaths();
        listView.setAdapter(adapterSingle);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                listView.setAdapter(adapterSingle);
                adapterSingle.setImages(folderFile.get(paths.get(position)), new File(paths.get(position)).getName());
                if (adapterSingle.getCount() > 0)
                    listView.setSelection(0);
            }
        });
        tvMiddleTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.photo_arrow_top, 0);
        tvMiddleTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listView.getAdapter() == adapterAll) {
                    listView.setAdapter(adapterSingle);
                    setLastFolder(lastFolder);
                    tvMiddleTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.photo_arrow_top, 0);
                } else {
                    listView.setAdapter(adapterAll);
                    tvMiddleTitle.setText("相册");
                    tvMiddleTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.photo_arrow_bottom, 0);
                }
            }
        });

    }

    private void getPaths() {
        folderFile = new HashMap<>();
        paths.add("相机胶卷");
        folderFile.put("相机胶卷", images);
        adapterSingle.setImages(images, "相机胶卷");
        for (NativeImage item : images) {
            String path = item.getPath();
            File file = new File(path);
            String parentPath = file.getParent();
            if (!folderFile.containsKey(parentPath)) {
                folderFile.put(parentPath,
                        new ArrayList<NativeImage>());
                paths.add(parentPath);
            }
            folderFile.get(parentPath).add(item);
        }
    }

    /**
     * @description:通过contentprovider获得sd卡上的图片
     * @author:hui-ye
     * @return:void
     */

    public ArrayList<NativeImage> getImages() {
        // 指定要查询的uri资源
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 获取ContentResolver
        ContentResolver contentResolver = getContentResolver();
        // 查询的字段
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        // 条件
        String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
        // 条件值(這裡的参数不是图片的格式，而是标准，所有不要改动)
//		String[] selectionArgs = { "image/jpeg","image/gif", "image/bmp", "image/png" };
//		String[] selectionArgs = { "image/gif"};
        // 排序
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        // 查询sd卡上的图片
        ArrayList<NativeImage> imageList = new ArrayList<NativeImage>();
        query(imageList, contentResolver, uri, projection, selection, new String[]{"image/jpeg"}, sortOrder);
//        query(imageList, contentResolver, uri, projection, selection, new String[]{"image/gif"}, sortOrder);
        query(imageList, contentResolver, uri, projection, selection, new String[]{"image/bmp"}, sortOrder);
        query(imageList, contentResolver, uri, projection, selection, new String[]{"image/png"}, sortOrder);
        initList(contentResolver, imageList);
        return imageList;
    }

    /**
     * 得到本地图片文件
     *
     * @param context
     * @return
     */
    public static ArrayList<HashMap<String, String>> getAllPictures(Context context) {
        ArrayList<HashMap<String, String>> picturemaps = new ArrayList<>();
        HashMap<String, String> picturemap;
        ContentResolver cr = context.getContentResolver();
        //先得到缩略图的URL和对应的图片id
        Cursor cursor = cr.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Thumbnails.IMAGE_ID,
                        MediaStore.Images.Thumbnails.DATA
                },
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            do {
                picturemap = new HashMap<>();
                picturemap.put("image_id_path", cursor.getInt(0) + "");
                picturemap.put("thumbnail_path", cursor.getString(1));
                picturemaps.add(picturemap);
            } while (cursor.moveToNext());
            cursor.close();
        }
        //再得到正常图片的path
        for (int i = 0; i < picturemaps.size(); i++) {
            picturemap = picturemaps.get(i);
            String media_id = picturemap.get("image_id_path");
            cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media.DATA
                    },
                    MediaStore.Audio.Media._ID + "=" + media_id,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                do {
                    picturemap.put("image_id", cursor.getString(0));
                    picturemaps.set(i, picturemap);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return picturemaps;
    }

    private void initList(final ContentResolver contentResolver, final List<NativeImage> imageList) {
        new Thread() {
            @Override
            public void run() {

                for (NativeImage imageMap : imageList) {

                    Cursor cursor1 = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                            new String[]{MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA},
                            MediaStore.Images.Thumbnails.IMAGE_ID + "=" + imageMap.getId(),
                            null, null);
                    if (cursor1.moveToFirst()) {
                        String path1 = cursor1.getString(cursor1
                                .getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                        File file1 = new File(path1);
                        if (!file1.exists() || file1.length() < 10) {
                            imageMap.setThumbnails(imageMap.getPath());

                        } else {
                            imageMap.setThumbnails(path1);
                        }
                    } else {
                        imageMap.setThumbnails(imageMap.getPath());
                    }
                    cursor1.close();
                }
            }
        }.start();
    }

    public static void query(List<NativeImage> imageList, ContentResolver contentResolver, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = contentResolver.query(uri, projection, selection,
                selectionArgs, sortOrder);
        if (cursor != null) {
            NativeImage imageMap = null;
            if (cursor.moveToFirst()) {
                do {
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    File file = new File(path);
                    if (!file.exists() && file.length() < 10240) {
                        continue;
                    }

                    imageMap = new NativeImage();
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                    imageMap.setId(id);
                    imageMap.setPath(path);
                    imageList.add(imageMap);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }


    }

    private class AllFolderAdapter extends BaseAdapter {
        class Holder {
            TextView image_num;
            TextView folder_name;
            ImageView image;
//				int position;
        }

        final int height = (int) ((FFUtils.getDisWidth() - FFUtils.getPx(8)) / 3);

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = getLayoutInflater().inflate(R.layout.ff_item_selectpic_images_folder,
                        parent, false);
                holder.image = (ImageView) convertView
                        .findViewById(R.id.image);
                holder.image_num = (TextView) convertView
                        .findViewById(R.id.tv_num);
                holder.folder_name = (TextView) convertView
                        .findViewById(R.id.tv_folder);
                convertView.setTag(holder);
//					convertView.setOnClickListener(new OnClickListener() {
//
//						@Override
//						public void onClick(View v) {
//							int position = ((Holder) v.getTag()).position;
//							SingleFolderImagesActivity.skipTo(AllFolderImagesActivity.this,
//							folderFile.get(paths.get(position)),
//							new File(paths.get(position)).getName(), getIntent()
//									.getBooleanExtra(INTENT_DO_CUT, false), 0);
//						}
//					});
            } else {
                holder = (Holder) convertView.getTag();
            }
//				holder.position = position;
            String path = folderFile.get(paths.get(position)).get(0)
                    .getThumbnails();
            holder.image_num.setText(folderFile.get(paths.get(position))
                    .size() + "张");
            if (position == 0) {

                holder.folder_name.setText("相机胶卷");
            } else {

                holder.folder_name.setText(new File(path).getParentFile().getName());
            }
//                FFImageLoader.loadMiddleImage(context(),path,holder.image);
            FFImageLoader.loadNativeImage(context(), path, holder.image, height, height, R.drawable.alpha);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return paths.get(position);
        }

        @Override
        public int getCount() {
            return paths.size();
        }
    }

    public class SingleFolderAdapter extends BaseAdapter implements View.OnClickListener {
        ArrayList<NativeImage> images;
        int height = (int) ((FFUtils.getDisWidth() - FFUtils.getPx(8)) / 3);

        public void setImages(ArrayList<NativeImage> images, String title) {
            this.images = images;
            setLastFolder(title);
        }

        class Holder {
            ImageView image1;
            ImageView image2;
            ImageView image3;
            View ll1;
            View ll2;
            View ll3;
        }


        @Override
        public void onClick(View v) {
            int tag = (Integer) v.getTag();
            if (tag < 0) {
                return;
            }

            setResult(RESULT_OK, new Intent().putExtra("path", images.get(tag).getPath()));
            finish();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = getLayoutInflater().inflate(R.layout.ff_item_selectpic_images_single_folder, null);
                holder.image1 = (ImageView) convertView.findViewById(R.id.iv1);
                holder.image2 = (ImageView) convertView.findViewById(R.id.iv2);
                holder.image3 = (ImageView) convertView.findViewById(R.id.iv3);
                holder.ll1 = convertView.findViewById(R.id.ll1);
                holder.ll2 = convertView.findViewById(R.id.ll2);
                holder.ll3 = convertView.findViewById(R.id.ll3);

                holder.ll1.getLayoutParams().height = height;
                holder.ll2.getLayoutParams().height = height;
                holder.ll3.getLayoutParams().height = height;

                holder.ll1.setOnClickListener(this);
                holder.ll2.setOnClickListener(this);
                holder.ll3.setOnClickListener(this);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.ll1.setTag(position * 3);
            holder.ll2.setTag(position * 3 + 1);
            holder.ll3.setTag(position * 3 + 2);

            holder.ll2.setVisibility(View.INVISIBLE);
            holder.ll3.setVisibility(View.INVISIBLE);

            String path = images.get(position * 3).getThumbnails();
            FFImageLoader.loadNativeImage(context(), path, holder.image1, height, height, R.drawable.alpha);
//                        NativeImageLoader.loadImage(holder.image1, path, height, height);
            if (position * 3 + 2 > images.size()) {
                holder.ll2.setTag(-1);
                holder.ll3.setTag(-1);
                holder.image2.setTag("");
                holder.image3.setTag("");
                holder.image2.setImageBitmap(null);
                holder.image3.setImageBitmap(null);
                return convertView;
            }
            holder.ll2.setVisibility(View.VISIBLE);
            path = images.get(position * 3 + 1).getThumbnails();
            FFImageLoader.loadNativeImage(context(), path, holder.image2, height, height, R.drawable.alpha);
//                        NativeImageLoader.loadImage(holder.image2, path, height, height);
            if (position * 3 + 3 > images.size()) {
                holder.ll3.setTag(-1);
                holder.image3.setTag("");
                holder.image3.setImageBitmap(null);
                return convertView;
            }
            holder.ll3.setVisibility(View.VISIBLE);
            path = images.get(position * 3 + 2).getThumbnails();
            FFImageLoader.loadNativeImage(context(), path, holder.image3, height, height, R.drawable.alpha);
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public int getCount() {
            return images.size() / 3 + (images.size() % 3 > 0 ? 1 : 0);
        }
    }
}
