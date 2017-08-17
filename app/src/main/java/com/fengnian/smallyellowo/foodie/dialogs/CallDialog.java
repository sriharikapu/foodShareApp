package com.fengnian.smallyellowo.foodie.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengnian.smallyellowo.foodie.R;

/**
 * Created by Administrator on 2016-9-14.
 */
public class CallDialog extends Dialog implements View.OnClickListener {
    private final View inflate;
    private final TextView tv_cancel;
    private final TextView tv_phone;
    private final Context context;

    public CallDialog(final Context context, int themeResId, final String phone) {
        super(context, themeResId);
        this.context = context;
        inflate = LayoutInflater.from(context).inflate(R.layout.dialog_call, null);


        tv_phone = (TextView) inflate.findViewById(R.id.tv_phone);
        tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tel = ((TextView) view).getText().toString();
                dismiss();
                EnsureDialog.showEnsureDialog(context, true, "是否拨打" + tel, "确定",null, "取消", new EnsureDialog.EnsureDialogListener() {
                    @Override
                    public void onOk(DialogInterface dialog) {
                        dialog.dismiss();
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + tel);
                            intent.setData(data);
                            context.startActivity(intent);
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        };

        if (phone.contains(",")) {
            String[] phones = phone.split(",");

            tv_phone.setText(phones[0]);
            tv_phone.setOnClickListener(l);
            for (int i = 1; i < phones.length; i++) {
                if (phones[i] != null && phones[i].length() != 0) {
                    View view = getLayoutInflater().inflate(R.layout.item_phone, null);
                    ((LinearLayout) tv_phone.getParent()).addView(view);
                    TextView tvPhone = (TextView) view.findViewById(R.id.tv_phone);
                    tvPhone.setText(phones[i]);
                    tvPhone.setOnClickListener(l);
                }
            }
        } else if (phone.contains(" ")) {
            String[] phones = phone.split(" ");

            tv_phone.setText(phones[0]);
            tv_phone.setOnClickListener(l);
            for (int i = 1; i < phones.length; i++) {
                if (phones[i] != null && phones[i].length() != 0) {
                    View view = getLayoutInflater().inflate(R.layout.item_phone, null);
                    ((LinearLayout) tv_phone.getParent()).addView(view);
                    TextView tvPhone = (TextView) view.findViewById(R.id.tv_phone);
                    tvPhone.setText(phones[i]);
                    tvPhone.setOnClickListener(l);
                }
            }
        } else {
            tv_phone.setText(phone);
            tv_phone.setOnClickListener(l);
        }
        setContentView(inflate);
        setCanceledOnTouchOutside(true);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}
