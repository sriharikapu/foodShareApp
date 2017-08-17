package com.fengnian.smallyellowo.foodie.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;

/**
 * title
 * msg
 * 两个按钮
 */
public class EnsureDialog extends Dialog {
    public EnsureDialog(Context context) {
        this(context, R.style.dialog);
    }

    public EnsureDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String message;

        public boolean isCancelable() {
            return cancelable;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        private boolean cancelable = true;
        private String positiveButtonText;
        private String negativeButtonText;
        private String neutralButtonText;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;
        private OnClickListener neutralButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(int neutralButtonText,
                                        OnClickListener listener) {
            this.neutralButtonText = (String) context
                    .getText(neutralButtonText);
            this.neutralButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(String neutralButtonText,
                                        OnClickListener listener) {
            this.neutralButtonText = neutralButtonText;
            this.neutralButtonClickListener = listener;
            return this;
        }

        public EnsureDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final EnsureDialog dialog = new EnsureDialog(context, R.style.dialog);
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(cancelable ? true : false);
            View layout = inflater.inflate(R.layout.dialog_ensure, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_right))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_right))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                    if (dialog.isShowing() && cancelable) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_right).setVisibility(
                        View.GONE);
                layout.findViewById(R.id.line2).setVisibility(View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_left))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_left))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                    if (dialog.isShowing() && cancelable) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_left).setVisibility(
                        View.GONE);
                layout.findViewById(R.id.line1).setVisibility(View.GONE);
            }
            // set the cancel button
            if (neutralButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_center))
                        .setText(neutralButtonText);
                if (neutralButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_center))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    neutralButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                    if (dialog.isShowing() && cancelable) {
                                            dialog.dismiss();
                                    }
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.btn_center).setVisibility(
                        View.GONE);
                layout.findViewById(R.id.line2).setVisibility(View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.tv_message)).setText(message);
            } else {
                layout.findViewById(R.id.line3).setVisibility(View.INVISIBLE);
                ((TextView) layout.findViewById(R.id.tv_message)).setVisibility(View.GONE);
            }
            return dialog;
        }

        public void show() {
            create().show();
        }


        public Builder setTitle(String title) {
            return this;
        }
    }

    public static Dialog showEnsureDialog(Context context, boolean cancelAble, String msg, String okWord, String centerWord, String cancelWord, final EnsureDialogListener listener) {
        return showEnsureDialog(context, cancelAble, msg, okWord, centerWord, cancelWord, listener, false);
    }

    public static Dialog showEnsureDialog(Context context, boolean cancelAble, String msg, String okWord, String centerWord, String cancelWord, final EnsureDialogListener listener, boolean isSystem) {
        EnsureDialog dialog = new Builder(context).setMessage(msg).setPositiveButton(cancelWord, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onCancel(dialog);
            }
        }).setNegativeButton(okWord, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onOk(dialog);

            }
        }).setCancelable(cancelAble).setNeutralButton(centerWord, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener instanceof  EnsureDialogListener1)
                ((EnsureDialogListener1) listener).onCenter(dialog);
            }
        }).create();
        dialog.show();
        return dialog;
    }

    public static interface EnsureDialogListener {
        void onOk(DialogInterface dialog);

        void onCancel(DialogInterface dialog);

    }

    public static interface EnsureDialogListener1 extends EnsureDialogListener {
        void onCenter(DialogInterface dialog);
    }

    public static void showClubDialog(Activity context, String title, String message) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // instantiate the dialog with the custom Theme
        final EnsureDialog dialog = new EnsureDialog(context, R.style.dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        View layout = inflater.inflate(R.layout.dialog_club, null);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.width = (int) (FFUtils.getDisWidth() * 0.7);
        dialog.onWindowAttributesChanged(attributes);
//        dialog.addContentView(layout, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
        TextView tv_message = (TextView) layout.findViewById(R.id.tv_message);
        Button button = (Button) layout.findViewById(R.id.button);
        tv_title.setText(title);
        tv_message.setText(message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // set the confirm button
        dialog.setContentView(layout);
        dialog.show();
    }
}
