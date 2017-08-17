//package com.fan.framework.utils.select_picture;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//import com.fan.framework.imageloader.FFImageLoader;
//import com.fan.framework.select_picture.AllFolderImagesActivity;
//import com.fan.framework.select_picture.CutImageActivity;
//import com.fan.framework.utils.FFUtils;
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
//
//import java.util.ArrayList;
//
///**
// * An example full-screen activity that shows and hides the system UI (i.e.
// * status bar and navigation/system bar) with user interaction.
// */
//public class SingleFolderImagesActivity extends BaseActivity implements OnClickListener {
//
//    private static final String INTENT_IMAGES = "images";
//    private static final String INTENT_TITLE = "title";
//    private static final String INTENT_DO_CUT = "cut";
//    static ArrayList<NativeImage> images;
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (isFinishing()) {
//            images = null;
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ff_listview_apply_all);
//        final LayoutInflater inflater = getLayoutInflater();
//        ListView listView = (ListView) findViewById(R.id.listView);
//        listView.setDivider(null);
//        listView.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//            }
//        });
//        setTitle(getIntent().getStringExtra(INTENT_TITLE));
//        final int height = (int) ((FFUtils.getDisWidth() - FFUtils.getPx(8)) / 3);
//        listView.setAdapter(
//                new BaseAdapter() {
//                    class Holder {
//                        ImageView image1;
//                        ImageView image2;
//                        ImageView image3;
//                        View ll1;
//                        View ll2;
//                        View ll3;
//                    }
//
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        Holder holder;
//                        if (convertView == null) {
//                            holder = new Holder();
//                            convertView = inflater.inflate(R.layout.ff_item_selectpic_images_single_folder, null);
//                            holder.image1 = (ImageView) convertView.findViewById(R.id.iv1);
//                            holder.image2 = (ImageView) convertView.findViewById(R.id.iv2);
//                            holder.image3 = (ImageView) convertView.findViewById(R.id.iv3);
//                            holder.ll1 = convertView.findViewById(R.id.ll1);
//                            holder.ll2 = convertView.findViewById(R.id.ll2);
//                            holder.ll3 = convertView.findViewById(R.id.ll3);
//
//                            holder.ll1.getLayoutParams().height = height;
//                            holder.ll2.getLayoutParams().height = height;
//                            holder.ll3.getLayoutParams().height = height;
//
//                            holder.ll1.setOnClickListener(SingleFolderImagesActivity.this);
//                            holder.ll2.setOnClickListener(SingleFolderImagesActivity.this);
//                            holder.ll3.setOnClickListener(SingleFolderImagesActivity.this);
//
//                            convertView.setTag(holder);
//                        } else {
//                            holder = (Holder) convertView.getTag();
//                        }
//                        holder.ll1.setTag(position * 3);
//                        holder.ll2.setTag(position * 3 + 1);
//                        holder.ll3.setTag(position * 3 + 2);
//
//                        holder.ll2.setVisibility(View.INVISIBLE);
//                        holder.ll3.setVisibility(View.INVISIBLE);
//
//                        String path = images.get(position * 3).getThumbnails();
//                        FFImageLoader.loadNativeImage(context(), path, holder.image1, height, height, R.drawable.alpha);
////                        NativeImageLoader.loadImage(holder.image1, path, height, height);
//                        if (position * 3 + 2 > images.size()) {
//                            holder.ll2.setTag(-1);
//                            holder.ll3.setTag(-1);
//                            holder.image2.setTag("");
//                            holder.image3.setTag("");
//                            holder.image2.setImageBitmap(null);
//                            holder.image3.setImageBitmap(null);
//                            return convertView;
//                        }
//                        holder.ll2.setVisibility(View.VISIBLE);
//                        path = images.get(position * 3 + 1).getThumbnails();
//                        FFImageLoader.loadNativeImage(context(), path, holder.image2, height, height, R.drawable.alpha);
////                        NativeImageLoader.loadImage(holder.image2, path, height, height);
//                        if (position * 3 + 3 > images.size()) {
//                            holder.ll3.setTag(-1);
//                            holder.image3.setTag("");
//                            holder.image3.setImageBitmap(null);
//                            return convertView;
//                        }
//                        holder.ll3.setVisibility(View.VISIBLE);
//                        path = images.get(position * 3 + 2).getThumbnails();
//                        FFImageLoader.loadNativeImage(context(), path, holder.image3, height, height, R.drawable.alpha);
//                        return convertView;
//                    }
//
//                    @Override
//                    public long getItemId(int position) {
//                        return position;
//                    }
//
//                    @Override
//                    public Object getItem(int position) {
//                        return images.get(position);
//                    }
//
//                    @Override
//                    public int getCount() {
//                        return images.size() / 3 + (images.size() % 3 > 0 ? 1 : 0);
//                    }
//                });
//    }
//
//    @Override
//    public void onClick(View v) {
//        int tag = (Integer) v.getTag();
//        if (tag < 0) {
//            return;
//        }
//
//        if (getIntent().getBooleanExtra(INTENT_DO_CUT, false)) {
//            CutImageActivity.skipToForResult(this, images.get(tag).getPath(), getIntent().getBooleanExtra(AllFolderImagesActivity.INTENT_ISAFTERLOGIN, false), 0);
//        } else {
//            ImageGalleryActivity.skipToForResult(this, images, tag, getIntent().getBooleanExtra(AllFolderImagesActivity.INTENT_ISAFTERLOGIN, false), 0);
//        }
//    }
//
//    public static void skipTo(Activity activity, ArrayList<NativeImage> images, String title, boolean cut, boolean isAfterLogin, int intentCode) {
//        Intent intent = new Intent(activity, SingleFolderImagesActivity.class);
////        intent.putParcelableArrayListExtra(INTENT_IMAGES, images);
//        SingleFolderImagesActivity.images = images;
//        intent.putExtra(INTENT_TITLE, title);
//        intent.putExtra(INTENT_DO_CUT, cut);
//        intent.putExtra(AllFolderImagesActivity.INTENT_ISAFTERLOGIN, isAfterLogin);
//        activity.startActivityForResult(intent, intentCode);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            setResult(RESULT_OK, data);
//            finish();
//        }
//    }
//
//}
