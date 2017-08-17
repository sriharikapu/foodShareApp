package com.fengnian.smallyellowo.foodie.scoreshop;

import android.text.TextUtils;

import com.fan.framework.http.FFExtraParams;
import com.fan.framework.http.FFNetWorkCallBack;
import com.fengnian.smallyellowo.foodie.appbase.BaseActivity;
import com.fengnian.smallyellowo.foodie.appconfig.Constants;
import com.fengnian.smallyellowo.foodie.utils.IUrlUtils;

/**
 * Created by chenglin on 2017-5-9.
 */

public class AddressManager {
    private static AddressManager INSTANCE;

    private AddressManager() {
    }

    public static AddressManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AddressManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AddressManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 提交收货地址
     * 文档地址：http://tools.tinydonuts.cn:8090/pages/viewpage.action?pageId=4915629
     */
    public void PostAddress(BaseActivity baseActivity, FFNetWorkCallBack<SYAddressListModel> callBack,
                            String name, String phone, String id, String province, String city, String region, String address) {
        Object[] array_1 = {"name", name
                , "phone", phone
                , "id", id
                , "province", province
                , "city", city
                , "region", region
                , "address", address};

        Object[] array_2 = {"name", name
                , "phone", phone
                , "province", province
                , "city", city
                , "region", region
                , "address", address};

        Object[] array = array_1;
        if (TextUtils.isEmpty(id)) {
            array = array_2;
        }

//        baseActivity.post(Constants.shareConstants().getNetHeaderAdress() + "/consignee/inupConsignee.do", "", null, callBack, array);
        baseActivity.post(IUrlUtils.Search.inupConsignee, "", null, callBack, array);
    }

    /**
     * 获取收货地址
     * 文档地址：http://tools.tinydonuts.cn:8090/pages/viewpage.action?pageId=4915619
     */
    public void getAddress(BaseActivity baseActivity, boolean isShow,FFNetWorkCallBack<SYAddressListModel> callBack) {
        String text = null;
        if (isShow){
            text = "";
        }

        baseActivity.post(IUrlUtils.Search.getLastConsigneeInfo, text,
                new FFExtraParams().setProgressDialogcancelAble(false), callBack);
    }


}
