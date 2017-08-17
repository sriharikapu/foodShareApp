package com.fengnian.smallyellowo.foodie.feeddetail.detail;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fan.framework.imageloader.FFImageLoader;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.UserInfoActivity;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.bean.results.BaseResult;
import com.fengnian.smallyellowo.foodie.intentdatas.UserInfoIntent;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-5-8.
 */

public class PriseUserListDialog extends Dialog {
    private BaseActivity mActivity;
    private ListView mListView;
    private MyAdapter mAdapter;
    private String mFoodRecordId;

    public PriseUserListDialog(BaseActivity activity, String foodRecordId) {
        this(activity, R.style.dialog);
        mFoodRecordId = foodRecordId;
        mActivity = activity;
    }

    public PriseUserListDialog(Context mcontext, int themeResId) {
        super(mcontext, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prise_dialog_user_list_layout);

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new MyAdapter(mActivity);
        mListView.setAdapter(mAdapter);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getData();
    }

    public void getData() {
//        mActivity.post(Constants.shareConstants().getNetHeaderAdress() + "/praise/queryPraiseList.do",
        mActivity.post(IUrlUtils.Search.queryPraiseList,
                "", null, new FFNetWorkCallBack<PriseModel>() {
                    @Override
                    public void onSuccess(PriseModel priseModel, FFExtraParams extra) {
                        if (priseModel != null) {
                            mAdapter.setData(priseModel.personList);
                        }
                    }

                    @Override
                    public boolean onFail(FFExtraParams extra) {
                        return false;
                    }
                }, "foodRecordId", mFoodRecordId
                , "pageNo", "0"
                , "count", 1000);

    }


    private static class MyAdapter extends BaseAdapter {
        private BaseActivity mActivity;
        private List<PriseItem> mDataList = new ArrayList<>();

        private MyAdapter() {
        }

        public MyAdapter(BaseActivity context) {
            mActivity = context;
        }

        public void setData(List<PriseItem> list) {
            if (null != list) {
                mDataList.clear();
                mDataList.addAll(list);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public PriseItem getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mActivity, R.layout.prise_dialog_user_list_item_layout, null);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.item_root = convertView.findViewById(R.id.item_root);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final PriseItem item = getItem(position);
            FFImageLoader.loadAvatar(mActivity, item.headImage, holder.imageView);

            if (!TextUtils.isEmpty(item.nickname)) {
                holder.text.setText(item.nickname);
            } else {
                holder.text.setText("");
            }

            if (!TextUtils.isEmpty(item.praiseTime)) {
                holder.time.setText(item.praiseTime);
            } else {
                holder.time.setText("");
            }

            holder.item_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInfoIntent intent = new UserInfoIntent();
                    intent.setId(item.userId);
                    mActivity.startActivity(UserInfoActivity.class, intent);
                }
            });

            return convertView;
        }

        private static class ViewHolder {
            public ImageView imageView;
            public TextView text, time;
            public View item_root;
        }

    }


    public static class PriseModel extends BaseResult {
        public List<PriseItem> personList;
    }

    public static class PriseItem {
        public String praiseTime;
        public String foodRecordId;
        public String personalitySignature;
        public String phoneNum;
        public String nickname;
        public String headImage;
        public String userId;
        public double score;
        public int userType;
    }
}
