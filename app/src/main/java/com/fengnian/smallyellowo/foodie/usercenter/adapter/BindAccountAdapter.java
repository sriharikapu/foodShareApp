package com.fengnian.smallyellowo.foodie.usercenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.appbase.MyBaseHolder;
import com.fengnian.smallyellowo.foodie.bean.publics.OtherAccount.Account;
import com.fengnian.smallyellowo.foodie.emoji.CustomEmojiTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by elaine on 2017/6/6.
 */

public class BindAccountAdapter extends RecyclerView.Adapter<BindAccountAdapter.BindHolder> {

    private Context mContext;
    private ArrayList<Account> accountList;
    private ItemClick mItemClickListener;

    public BindAccountAdapter(Context ctx, List<Account> list) {
        this.mContext = ctx;
        this.accountList = (ArrayList<Account>) list;
    }

    @Override
    public BindHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BindHolder(View.inflate(mContext, R.layout.item_bind_account, null));
    }

    @Override
    public void onBindViewHolder(BindHolder holder, int position) {
        holder.onBind(position, accountList.get(position));
    }

    @Override
    public int getItemCount() {
        if (FFUtils.isListEmpty(accountList)) {
            return 0;
        }
        return accountList.size();
    }

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public void setmItemClickListener(ItemClick mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    class BindHolder extends MyBaseHolder {
        @Bind(R.id.item_qq)
        ImageView itemQq;
        @Bind(R.id.item_qq_name)
        TextView itemQqName;
        @Bind(R.id.right_qq_image)
        ImageView rightQqImage;
        @Bind(R.id.account_qq_name)
        CustomEmojiTextView accountQqName;
        @Bind(R.id.bottom_line)
        View line;

        public BindHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBind(int position) {

        }

        public void onBind(final int postion, final Account account) {
            itemQq.setImageResource(account.getPlatformImage());
            itemQqName.setText(account.getPlatform());
            accountQqName.setText(account.getAccountName());

            updatePosition(accountQqName, account.isBinded());

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) line.getLayoutParams();
            if (postion == getItemCount() - 1) {
                params.setMargins(0, 0, 0, 0);
            } else {
                params.setMargins(30, 0, 0, 0);
            }
            line.setLayoutParams(params);

            accountQqName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener == null){
                        return;
                    }

                    mItemClickListener.onItemClickListener(postion, account);
                }
            });
        }

        private void updatePosition(View view, boolean isBind) {
            rightQqImage.setVisibility(isBind ? View.GONE : View.VISIBLE);

            if (view instanceof TextView) {
                ((TextView) view).setTextColor(mContext.getResources().getColor(isBind ? R.color.title_text_color : R.color.text_food_list_color_press));
            }

            if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
                if (isBind) {
                    ((RelativeLayout.LayoutParams) view.getLayoutParams()).setMargins(0, 0, 30, 0);
                    ((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                } else {
                    ((RelativeLayout.LayoutParams) view.getLayoutParams()).setMargins(0, 0, 20, 0);
                    ((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(RelativeLayout.LEFT_OF, R.id.right_qq_image);
                }
                view.setLayoutParams(view.getLayoutParams());
            }
        }
    }

    public interface ItemClick{
        void onItemClickListener(int position,Account account);
    }
}
