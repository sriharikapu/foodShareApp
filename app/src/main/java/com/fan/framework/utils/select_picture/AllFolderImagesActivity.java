//package com.fan.framework.utils.select_picture;
//
//import android.app.Activity;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.fan.framework.imageloader.FFImageLoader;
//import com.fan.framework.utils.FFUtils;
//import com.fan.framework.utils.FileUitl;
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * An example full-screen activity that shows and hides the system UI (i.e.
// * status bar and navigation/system bar) with user interaction.
// */
//public class AllFolderImagesActivity extends BaseActivity {
//
//    ArrayList<NativeImage> images;
//    ArrayList<String> paths;
//    private HashMap<String, ArrayList<NativeImage>> folderFile;
//    public static final String INTENT_DO_CUT = "cut";
//    public static final String INTENT_ISAFTERLOGIN = "afterlogin";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getIntent().getBooleanExtra(INTENT_DO_CUT, false) && !FileUitl.isSdFreeEnough()) {
//            showToast("内存卡剩余空间不足", null);
//            return;
//        }
//        images = getImages();
//        if (images.size() == 0) {
//            showToast("没有图片", null);
//            TextView view = new TextView(this);
//            view.setText("没有图片");
//            setContentView(view);
//            return;
//        }
//        setContentView(R.layout.ff_listview_apply_all);
//        paths = new ArrayList<String>();
//        getPaths();
//        final LayoutInflater inflater = getLayoutInflater();
//        final int height = (int) ((FFUtils.getDisWidth() - FFUtils.getPx(8)) / 3);
//        ListView listView = ((ListView) findViewById(R.id.listView));
//        listView.setDivider(null);
//        listView.setAdapter(new BaseAdapter() {
//            class Holder {
//                TextView image_num;
//                TextView folder_name;
//                ImageView image;
////				int position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                Holder holder;
//                if (convertView == null) {
//                    holder = new Holder();
//                    convertView = inflater.inflate(R.layout.ff_item_selectpic_images_folder,
//                            parent, false);
//                    holder.image = (ImageView) convertView
//                            .findViewById(R.id.image);
//                    holder.image_num = (TextView) convertView
//                            .findViewById(R.id.tv_num);
//                    holder.folder_name = (TextView) convertView
//                            .findViewById(R.id.tv_folder);
//                    convertView.setTag(holder);
////					convertView.setOnClickListener(new OnClickListener() {
////
////						@Override
////						public void onClick(View v) {
////							int position = ((Holder) v.getTag()).position;
////							SingleFolderImagesActivity.skipTo(AllFolderImagesActivity.this,
////							folderFile.get(paths.get(position)),
////							new File(paths.get(position)).getName(), getIntent()
////									.getBooleanExtra(INTENT_DO_CUT, false), 0);
////						}
////					});
//                } else {
//                    holder = (Holder) convertView.getTag();
//                }
////				holder.position = position;
//                String path = folderFile.get(paths.get(position)).get(0)
//                        .getThumbnails();
//                holder.image_num.setText(folderFile.get(paths.get(position))
//                        .size() + "张");
//                holder.folder_name.setText(new File(path).getParentFile()
//                        .getName());
////                FFImageLoader.loadMiddleImage(context(),path,holder.image);
//                FFImageLoader.loadNativeImage(context(), path, holder.image, height, height, R.drawable.alpha);
//                return convertView;
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return paths.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return paths.size();
//            }
//        });
//        listView.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                SingleFolderImagesActivity.skipTo(AllFolderImagesActivity.this,
//                        folderFile.get(paths.get(position)),
//                        new File(paths.get(position)).getName(), getIntent()
//                                .getBooleanExtra(INTENT_DO_CUT, false), getIntent().getBooleanExtra(INTENT_ISAFTERLOGIN, false), 0);
//            }
//        });
//    }
//
//    private void getPaths() {
//        folderFile = new HashMap<>();
//        for (NativeImage item_image_gallery : images) {
//            String path = item_image_gallery.getPath();
//            File file = new File(path);
//            String parentPath = file.getParent();
//            if (!folderFile.containsKey(parentPath)) {
//                folderFile.put(parentPath,
//                        new ArrayList<NativeImage>());
//                paths.add(parentPath);
//            }
//            folderFile.get(parentPath).add(item_image_gallery);
//        }
//    }
//
//    /**
//     * @description:通过contentprovider获得sd卡上的图片
//     * @author:hui-ye
//     * @return:void
//     */
//
//    public ArrayList<NativeImage> getImages() {
//        // 指定要查询的uri资源
//        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        // 获取ContentResolver
//        ContentResolver contentResolver = getContentResolver();
//        // 查询的字段
//        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
//        // 条件
//        String selection = MediaStore.Images.Media.MIME_TYPE + "=?";
//        // 条件值(這裡的参数不是图片的格式，而是标准，所有不要改动)
////		String[] selectionArgs = { "image/jpeg","image/gif", "image/bmp", "image/png" };
////		String[] selectionArgs = { "image/gif"};
//        // 排序
//        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
//        // 查询sd卡上的图片
//        ArrayList<NativeImage> imageList = new ArrayList<NativeImage>();
//        query(imageList, contentResolver, uri, projection, selection, new String[]{"image/jpeg"}, sortOrder);
//        query(imageList, contentResolver, uri, projection, selection, new String[]{"image/gif"}, sortOrder);
//        query(imageList, contentResolver, uri, projection, selection, new String[]{"image/bmp"}, sortOrder);
//        query(imageList, contentResolver, uri, projection, selection, new String[]{"image/png"}, sortOrder);
//        initList(contentResolver, imageList);
//        return imageList;
//    }
//
//    /**
//     * 得到本地图片文件
//     *
//     * @param context
//     * @return
//     */
//    public static ArrayList<HashMap<String, String>> getAllPictures(Context context) {
//        ArrayList<HashMap<String, String>> picturemaps = new ArrayList<>();
//        HashMap<String, String> picturemap;
//        ContentResolver cr = context.getContentResolver();
//        //先得到缩略图的URL和对应的图片id
//        Cursor cursor = cr.query(
//                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
//                new String[]{
//                        MediaStore.Images.Thumbnails.IMAGE_ID,
//                        MediaStore.Images.Thumbnails.DATA
//                },
//                null,
//                null,
//                null);
//        if (cursor.moveToFirst()) {
//            do {
//                picturemap = new HashMap<>();
//                picturemap.put("image_id_path", cursor.getInt(0) + "");
//                picturemap.put("thumbnail_path", cursor.getString(1));
//                picturemaps.add(picturemap);
//            } while (cursor.moveToNext());
//            cursor.close();
//        }
//        //再得到正常图片的path
//        for (int i = 0; i < picturemaps.size(); i++) {
//            picturemap = picturemaps.get(i);
//            String media_id = picturemap.get("image_id_path");
//            cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    new String[]{
//                            MediaStore.Images.Media.DATA
//                    },
//                    MediaStore.Audio.Media._ID + "=" + media_id,
//                    null,
//                    null
//            );
//            if (cursor.moveToFirst()) {
//                do {
//                    picturemap.put("image_id", cursor.getString(0));
//                    picturemaps.set(i, picturemap);
//                } while (cursor.moveToNext());
//                cursor.close();
//            }
//        }
//        return picturemaps;
//    }
//
//    private void initList(final ContentResolver contentResolver, final List<NativeImage> imageList) {
//        new Thread() {
//            @Override
//            public void run() {
//
//                for (NativeImage imageMap : imageList) {
//
//                    Cursor cursor1 = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
//                            new String[]{MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.DATA},
//                            MediaStore.Images.Thumbnails.IMAGE_ID + "=" + imageMap.getId(),
//                            null, null);
//                    if (cursor1.moveToFirst()) {
//                        String path1 = cursor1.getString(cursor1
//                                .getColumnIndex(MediaStore.Images.Thumbnails.DATA));
//                        File file1 = new File(path1);
//                        if (!file1.exists() || file1.length() < 10) {
//                            imageMap.setThumbnails(imageMap.getPath());
//
//                        } else {
//                            imageMap.setThumbnails(path1);
//                        }
//                    } else {
//                        imageMap.setThumbnails(imageMap.getPath());
//                    }
//                    cursor1.close();
//                }
//            }
//        }.start();
//    }
//
//    public void query(List<NativeImage> imageList, ContentResolver contentResolver, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
//        Cursor cursor = contentResolver.query(uri, projection, selection,
//                selectionArgs, sortOrder);
//        if (cursor != null) {
//            NativeImage imageMap = null;
//            if (cursor.moveToFirst()) {
//                do {
//                    String path = cursor.getString(cursor
//                            .getColumnIndex(MediaStore.Images.Media.DATA));
//                    File file = new File(path);
//                    if (!file.exists() && file.length() < 10240) {
//                        continue;
//                    }
//
//                    imageMap = new NativeImage();
//
//                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
//                    imageMap.setId(id);
//                    imageMap.setPath(path);
//                    imageList.add(imageMap);
//                }
//                while (cursor.moveToNext());
//            }
//            cursor.close();
//        }
//
//
//    }
//
//    public static void skipToForResult(Activity activity, boolean cut, boolean isAfterLogin,
//                                       int requestCode) {
//        Intent intent = new Intent(activity, AllFolderImagesActivity.class);
//        intent.putExtra(INTENT_DO_CUT, cut);
//        intent.putExtra(INTENT_ISAFTERLOGIN, isAfterLogin);
//        activity.startActivityForResult(intent, requestCode);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            setResult(RESULT_OK, data);
//            finish();
//        }
//    }
//}
