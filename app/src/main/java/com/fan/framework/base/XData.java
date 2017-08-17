package com.fan.framework.base;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.appbase.APP;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * 基础对象 其它数据相关的对象都需要继承自它
 */
public class XData implements Serializable, Parcelable {

    /**
     * 对象ID，用于区分不同对象
     */
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public XData() {
        super();
        setId(XData.uuid());
    }


    public boolean isEqual(XData data) {
        boolean bEqual = false;
        if (data != null) {
            if (getId().equals(data.getId())) {
                bEqual = true;
            } else {
                bEqual = false;
            }
        }
        return bEqual;
    }

    /**
     * 生成对象的uuid
     *
     * @return 返回唯一的uuid
     */
    public static synchronized String uuid() {
        try {
            String valu = System.currentTimeMillis() + getMyUUID()+Math.random();
            return Md5(valu);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 验证对象是否合法
     *
     * @return true表示合法 否则不合法
     */
    public boolean validateID() {
        if (getId().length() <= 0)
            return false;
        else
            return true;
    }


    public static String Md5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String getMyUUID() {
        //某种情况下获取不到读取手机状态的权限，会报：
        //java.lang.SecurityException: getDeviceId: Neither user 10737 nor current process has android.permission.READ_PHONE_STATE.
        //by chenglin 2017年7月25日
        try {
            TelephonyManager tm = (TelephonyManager) APP.app
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (FFUtils.isStringEmpty(imei)) {
                final String tmDevice, tmSerial, androidId;
                tmDevice = "" + tm.getDeviceId();
                tmSerial = "" + tm.getSimSerialNumber();
                androidId = ""
                        + android.provider.Settings.Secure.getString(
                        APP.app.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
                UUID deviceUuid = new UUID(androidId.hashCode(),
                        ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
                String uniqueId = deviceUuid.toString();
                String uniqueIdmd5 = Md5(uniqueId);
                Log.d("debug", "uuid=" + uniqueIdmd5);
                return uniqueIdmd5;
            }
            return imei;
        }catch (Exception e) {
            return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
    }

    protected XData(Parcel in) {
        this.id = in.readString();
    }

    public static final Creator<XData> CREATOR = new Creator<XData>() {
        @Override
        public XData createFromParcel(Parcel source) {
            return new XData(source);
        }

        @Override
        public XData[] newArray(int size) {
            return new XData[size];
        }
    };
}
