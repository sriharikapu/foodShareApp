package com.fengnian.smallyellowo.foodie.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.DialogItem;
import com.fengnian.smallyellowo.foodie.bean.publics.OnItemClick;

import java.util.List;


public class UIUtil {
	
	private  ProgressDialog waittingDialog;
	public static Dialog showBottomDialog(Context context,
			List<DialogItem> items) {
		LinearLayout dialogView = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.bottom_dialog_layout, null);
		
		final Dialog customDialog = new Dialog(context, R.style.bottom_dialog_style);
		LinearLayout itemView;
		TextView textView;
		for (DialogItem item : items) {
			itemView = (LinearLayout) LayoutInflater.from(context).inflate(
					item.getViewId(), null);
			textView = (TextView) itemView.findViewById(R.id.bottom_item_text);
			textView.setText(item.getText());
			textView.setOnClickListener(new OnItemClick(item, customDialog));
			dialogView.addView(itemView);
		}

		WindowManager.LayoutParams localLayoutParams = customDialog.getWindow()
				.getAttributes();
		localLayoutParams.x = 0;
		localLayoutParams.y = -1000;
		localLayoutParams.gravity = 80;
		dialogView.setMinimumWidth(10000);

		customDialog.onWindowAttributesChanged(localLayoutParams);
		customDialog.setCanceledOnTouchOutside(true);
		customDialog.setCancelable(true);
		customDialog.setContentView(dialogView);

		if (context instanceof Activity) {
			Activity activity = (Activity) context;
			if (!activity.isFinishing()) {
				customDialog.show();
			}
		}
		customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));//new ColorDrawable(0)

		return customDialog;
	}


	public static void showToast(Context context,String message){
		Toast t = Toast.makeText(context, message,
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	  public static void DissDialog(Context context,ProgressDialog waittingDialog){
		  if(waittingDialog!=null&&waittingDialog.isShowing())
				 waittingDialog.dismiss();
			
	  }

}
