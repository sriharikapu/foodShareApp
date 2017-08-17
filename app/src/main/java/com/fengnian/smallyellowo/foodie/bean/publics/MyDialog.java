package com.fengnian.smallyellowo.foodie.bean.publics;

/*
 * Copyright © 1999-2014 byecity, Inc. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.fengnian.smallyellowo.foodie.R;


/**
 *
 */
public class MyDialog extends Dialog {

	public MyDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initDialog(context);
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
		initDialog(context);
	}

	public MyDialog(Context context) {
		super(context);
		initDialog(context);
	}

	private void initDialog(Context context) {
		setCanceledOnTouchOutside(false);
		Window window = getWindow();
		window.setWindowAnimations(R.style.dialogWindowAnim);
		WindowManager.LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(params);
	}
}
