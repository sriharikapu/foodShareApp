//package com.fan.framework.select_picture;
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
//import com.fan.framework.utils.FFUtils;
//import com.fengnian.smallyellowo.foodie.R;
//import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
//
//import java.util.ArrayList;
//
//import static com.baidu.location.b.g.v;
//
///**
// * An example full-screen activity that shows and hides the system UI (i.e.
// * status bar and navigation/system bar) with user interaction.
// */
//public class SingleFolderImagesActivity extends BaseActivity {
//
//    private static final String INTENT_IMAGES = "images";
//    private static final String INTENT_TITLE = "title";
//    private static final String INTENT_DO_CUT = "cut";
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
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
//        BaseAdapter adapter = new SingleFolderAdapter();
//        listView.setAdapter(
//                adapter);
//    }
//
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
