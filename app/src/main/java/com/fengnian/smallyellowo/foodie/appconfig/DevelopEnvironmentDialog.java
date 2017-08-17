package com.fengnian.smallyellowo.foodie.appconfig;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.PreferencesData;

/**
 * Created by chenglin on 2017-3-22.
 */

public class DevelopEnvironmentDialog extends Dialog implements View.OnClickListener {
    private final String[] text = {"开发环境", "测试环境", "仿真环境", "官方发布环境"};
    private final int[] layoutIdArray = {R.id.item_0, R.id.item_1, R.id.item_2, R.id.item_3};

    public DevelopEnvironmentDialog(Context context) {
        this(context, R.style.dialog);
    }

    public DevelopEnvironmentDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCanceledOnTouchOutside(false);
        setContentView(R.layout.develop_environment_layout);

//        int developId = PreferencesData.getDevelopEnvironmentId();
        int developId = Constants.shareConstants().getAppEnvironment();

        for (int index = 0; index < layoutIdArray.length; index++) {
            View itemView = findViewById(layoutIdArray[index]);
            itemView.setTag(index);
            itemView.setOnClickListener(this);
            TextView textView = (TextView) itemView.findViewById(R.id.textView);
            RadioButton radio = (RadioButton) itemView.findViewById(R.id.radio);
            radio.setEnabled(false);
            textView.setText(text[index]);
            if (developId == index) {
                radio.setChecked(true);
            } else {
                radio.setChecked(false);
            }
        }

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();

        if (index == 0) {
            PreferencesData.setDevelopEnvironmentId(Constants.ENV_DEVELOP);
        } else if (index == 1) {
            PreferencesData.setDevelopEnvironmentId(Constants.ENV_TEST);
        } else if (index == 2) {
            PreferencesData.setDevelopEnvironmentId(Constants.ENV_EMULATE);
        } else if (index == 3) {
            PreferencesData.setDevelopEnvironmentId(Constants.ENV_RELEASE);
        }

        String str = "已切换到：" + text[index] + "\n" + "请杀掉APP重新进入";
        Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();
        dismiss();
    }
}
