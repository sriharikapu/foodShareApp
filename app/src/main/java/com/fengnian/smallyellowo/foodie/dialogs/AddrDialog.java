//package com.fengnian.smallyellowo.foodie.dialogs;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.WindowManager;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.fengnian.smallyellowo.foodie.R;
//
//
//public class AddrDialog extends Dialog {
//
//    private ListView lvStyle;
//    TextView dialog2;
//    private ProgressBar pb_bar;
//
//    public ListView getLvStyle() {
//        return lvStyle;
//    }
//
//    public void setLvStyle(ListView lvStyle) {
//        this.lvStyle = lvStyle;
//    }
//
//    public TextView getDialog2() {
//
//        return dialog2;
//    }
//
//    public void setDialog2(TextView dialog2) {
//        this.dialog2 = dialog2;
//    }
//
//
//    public AddrDialog(Context context) {
//
//        super(context, R.style.AddrDialogStyle);
//
//
//        getWindow().setGravity(Gravity.BOTTOM);
//
//        WindowManager m = getWindow().getWindowManager();
//        Display d = m.getDefaultDisplay();
//        WindowManager.LayoutParams p = getWindow().getAttributes();
//        p.width = d.getWidth();
//        getWindow().setAttributes(p);
//    }
//
//    public AddrDialog(Context context, int style) {
//
//        super(context, style);
//        getWindow().setGravity(Gravity.BOTTOM);
//        WindowManager m = getWindow().getWindowManager();
//        Display d = m.getDefaultDisplay();
//        WindowManager.LayoutParams p = getWindow().getAttributes();
//        p.width = d.getWidth();
//        getWindow().setAttributes(p);
//
//    }
//
//
//    public ProgressBar getPb_bar() {
//        return pb_bar;
//    }
//
//    public void setPb_bar(ProgressBar pb_bar) {
//        this.pb_bar = pb_bar;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_addr);
//        lvStyle = (ListView) findViewById(R.id.lv_addr_style);
//        dialog2 = (TextView) findViewById(R.id.dialog2);
//        pb_bar = (ProgressBar) findViewById(R.id.pb_bar);
//
//
//        // 单条点击
////		lvStyle.setOnItemClickListener(new OnItemClickListener() {
////
////			@Override
////			public void onItemClick(AdapterView<?> parent, View view, int position,
////					long id) {
////				// 保存选中的背景id
////				//SpUtils.putParam(getContext(), Constants.ADDR_STYLE_BG, mBgs[position]);
////				// dialog消失
////				dismiss();
////			}
////		});
//    }
//
//
//}
