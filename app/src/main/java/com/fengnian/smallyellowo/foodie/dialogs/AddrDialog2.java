//package com.fengnian.smallyellowo.foodie.dialogs;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.WindowManager;
//import android.view.WindowManager.LayoutParams;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.fengnian.smallyellowo.foodie.R;
//
//
//public class AddrDialog2 extends Dialog {
//
//	private ListView lvStyle1;
//	TextView dialog3;
//
//	public ListView getLvStyle1() {
//		return lvStyle1;
//	}
//
//	public void setLvStyle1(ListView lvStyle1) {
//		this.lvStyle1 = lvStyle1;
//	}
//
//	public TextView getDialog3() {
//		return dialog3;
//	}
//
//	public void setDialog3(TextView dialog3) {
//		this.dialog3 = dialog3;
//	}
//
//	public AddrDialog2(Context context) {
//
//		super(context, R.style.AddrDialogStyle);
//
//
//
//
//
//		getWindow().setGravity(Gravity.BOTTOM);
//
//		WindowManager m = getWindow().getWindowManager();
//		Display d = m.getDefaultDisplay();
//		LayoutParams p = getWindow().getAttributes();
//		p.width = d.getWidth();
//		getWindow().setAttributes(p);
//	}
//
//	public AddrDialog2(Context context, int style) {
//
//		super(context, style);
//		getWindow().setGravity(Gravity.BOTTOM);
//		WindowManager m = getWindow().getWindowManager();
//		Display d = m.getDefaultDisplay();
//		LayoutParams p = getWindow().getAttributes();
//		p.width = d.getWidth();
//		getWindow().setAttributes(p);
//
//	}
//
//
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.dialog_addr2);
//		lvStyle1 = (ListView) findViewById(R.id.lv_addr_style1);
//		dialog3 = (TextView) findViewById(R.id.dialog3);
//
//
//
//		// 单条点击
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
//	}
//
//
//
//}
