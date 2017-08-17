package com.fengnian.smallyellowo.foodie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.intentdatas.IntentData;
import com.fengnian.smallyellowo.foodie.utils.Config;

/**
 * Created by Administrator on 2016-10-9.
 */

public class AddFoodNameActivty extends BaseActivity<IntentData> implements View.OnClickListener {
    private EditText ed_food_name;
    private ImageView iv_delete_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actvity_add_food_name);
        ed_food_name = findView(R.id.ed_food_name);
        ed_food_name.setFocusable(true);
        ed_food_name.requestFocus();
        show();

        ed_food_name.addTextChangedListener(watcher);
        iv_delete_img = findView(R.id.iv_delete_img);
        iv_delete_img.setVisibility(View.GONE);
        iv_delete_img.setOnClickListener(this);

        addMenu("完成", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                DraftModel model= SYDataManager.shareDataManager().draftsOfFirst();
//                Config.memo_list.add(ed_food_name.getText().toString().trim());
//               List<String> list= model.getFeed().getFood().getDishesNameList();
//                if(list==null) list=new ArrayList<>();
//                list.addAll(Config.memo_list);
//                model.getFeed().getFood().setDishesNameList(list);
//                SYDataManager.shareDataManager().updateDraft(model);
//                finish();
                String str = ed_food_name.getText().toString().trim();
                if (str.length() > 0) {
                    Config.memo_list.add(str);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showToast("菜品不能为空");
                }
            }
        });


    }


    void show() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0) {
                iv_delete_img.setVisibility(View.VISIBLE);
            } else {
                iv_delete_img.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onBackPressed(View v) {
        getpopWindow();

    }

    private void getpopWindow() {

        hideKey();
        new AlertDialog.Builder(AddFoodNameActivty.this)
                .setTitle("提示")//设置对话框标题
                .setMessage("是否放弃本次修改!")//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing
                        AddFoodNameActivty.this.finish();
                    }

                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
            @Override

            public void onClick(DialogInterface dialog, int which) {//响应事件
                //nothing
            }

        }).show();//在按键响应事件中显示此对话框
    }
    public void hideKey() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_delete_img:
                ed_food_name.setText("");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Config.memo_list.clear();
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
