package com.fengnian.smallyellowo.foodie;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.Adapter.MemoResultaAdapter;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.bean.publics.GalleryInfo;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFeed;
import com.fengnian.smallyellowo.foodie.bean.publics.SYRichTextFood;
import com.fengnian.smallyellowo.foodie.bean.publish.DraftModel;
import com.fengnian.smallyellowo.foodie.datamanager.SYDataManager;
import com.fengnian.smallyellowo.foodie.dialogs.EnsureDialog;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.release.GalleryAndPhotoActvity;
import com.fengnian.smallyellowo.foodie.utils.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-10-8.
 */

public class MemoReultActivty extends BaseActivity<IntentData> implements View.OnClickListener{
    private TextView tv_num_food;
//    private View view_bottom, view_top;
    private ListView list_view;
    private TextView view_bottom;

    private MemoResultaAdapter adapter;

    public final  static  int Add_Memo_name=100;
    public final  static  int Add_Memo_pic=101;

    private List list;//水单列表
    public static ImageView iv_memo_point;
    private TextView tv_add_memo,tv_add_food_name;

    private List<String> dishes_name_List;
    DraftModel model;
    private ImageView iv_back;

    public  static  TextView tv_right_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNotitle(true);
        setTitle("菜品");
        setContentView(R.layout.activity_memo_reult);
        iv_back=findView(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_right_title=findView(R.id.tv_right_title);



        iv_memo_point= findView(R.id.iv_memo_point);
        tv_add_memo=findView(R.id.tv_add_memo);
        tv_add_memo.setOnClickListener(this);
        tv_add_food_name=findView(R.id.tv_add_food_name);
        tv_add_food_name.setOnClickListener(this);
        tv_num_food= (TextView) findViewById(R.id.tv_num_food);
        view_bottom=findView(R.id.view_bottom);
        list_view=findView(R.id.list_view);


         model= SYDataManager.shareDataManager().draftsOfFirst();
         list=new ArrayList<>();
        SYFeed fed= model.getFeed();

        SYRichTextFood textFood=fed.getFood();

        List<String> list1=textFood.getDishesNameList();
        if(list1!=null){
         list.addAll(list1);
        }
         adapter =new MemoResultaAdapter(this,list,model,this);
         list_view.setAdapter(adapter);

        showAboutUI();

        if(list.size()==0){
            tv_right_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Config.memo_list.clear();
                    MemoReultActivty.this.finish();
                }
            });
        }

    }

    @Override
    public void onBackPressed(View v) {
        Config.memo_list.clear();
        super.onBackPressed(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if("yes".equals(Config.is_add_memo_pic_flag)){
            list.addAll(Config.memo_list);
            tv_num_food.setVisibility(View.VISIBLE);
            tv_num_food.setText("为你识别出"+Config.memo_list.size()+"道美食");
            iv_memo_point.setVisibility(View.GONE);
//            model.getFeed().getFood().setDishesNameList(list);
//            DraftModelManager.dbSaveOrUpDate(model);
            adapter.notifyDataSetChanged();
            Config.is_add_memo_pic_flag="no";
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                  getpopWindow();
                break;

            case  R.id.tv_add_memo:
                GalleryInfo info=new GalleryInfo();
                info.setIsmemo(GalleryInfo.Is_memo);
                info.setTemplate_type(1);
                startActivity(GalleryAndPhotoActvity.class, info);
                finish();

                break;
            case  R.id.tv_add_food_name:
                 Intent intent=new Intent(this,AddFoodNameActivty.class);
                 startActivityForResult(intent,  Add_Memo_name);
//                startActivity(AddFoodNameActivty.class,new IntentData());
//                 finish();
                break;
        }
    }

    private void  showAboutUI(){
        if(list.size()>0){
            tv_num_food.setVisibility(View.GONE);
            view_bottom.setVisibility(View.VISIBLE);
            iv_memo_point.setVisibility(View.GONE);
        }
        else{
            tv_num_food.setVisibility(View.GONE);
            view_bottom.setVisibility(View.GONE);
            iv_memo_point.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Config.memo_list.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case  Add_Memo_name:
                list.addAll(Config.memo_list);
//                model.getFeed().getFood().setDishesNameList(list);
//                DraftModelManager.dbSaveOrUpDate(model);
//                SYDataManager.shareDataManager().updateDraft(model);
//                Config.memo_list.clear();

                showAboutUI();
                adapter.notifyDataSetChanged();
                break;
        }

    }
    public void hideKey() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
    private void  getpopWindow(){

        hideKey();
        new EnsureDialog.Builder(MemoReultActivty.this)
                .setTitle("提示")//设置对话框标题
                .setMessage("是否放弃本次修改!")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                        dialog.dismiss();
                        MemoReultActivty.this.finish();
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
                //nothing
                dialog.dismiss();
            }

        }).show();//在按键响应事件中显示此对话框
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            getpopWindow();
        }
        return false;

    }
}
