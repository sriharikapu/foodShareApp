package com.fengnian.smallyellowo.foodie.fragments;

import android.view.View;
import android.widget.TextView;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.R;
import com.fengnian.smallyellowo.foodie.bean.publics.SYFindMerchantTag;

import java.util.List;

/**
 * Created by Administrator on 2017-7-25.
 */

public class RemarkManager {
    final View ll;
    final TextView tv1;
    final TextView tv2;
    final TextView tv3;
    final TextView tv4;

    public RemarkManager(View view) {
        ll = view.findViewById(R.id.ll_remark);
        tv1 = (TextView) view.findViewById(R.id.tv_1);
        tv2 = (TextView) view.findViewById(R.id.tv_2);
        tv3 = (TextView) view.findViewById(R.id.tv_3);
        tv4 = (TextView) view.findViewById(R.id.tv_4);
        setData(null);
    }

    public void setData(List<SYFindMerchantTag> data) {
        if(FFUtils.isListEmpty(data)){
            ll.setVisibility(View.GONE);
            return;
        }
        ll.setVisibility(View.VISIBLE);
        int i = 0;
        TextView[] tvs = new TextView[]{tv1,tv2,tv3,tv4};
        for (int j =0;j<tvs.length;j++){
            tvs[j].setVisibility(View.GONE);
        }
        for (SYFindMerchantTag tag:data){
            if(i >= tvs.length-1){
                break;
            }
            tvs[i].setVisibility(View.VISIBLE);
            tvs[i].setText(tag.getFoodSceneTypeName()+"("+tag.getNumber()+")");
            i++;
        }
    }

}
