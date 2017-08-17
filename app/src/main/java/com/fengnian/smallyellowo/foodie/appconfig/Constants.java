package com.fengnian.smallyellowo.foodie.appconfig;

import com.fan.framework.utils.FFUtils;
import com.fengnian.smallyellowo.foodie.BuildConfig;
import com.fengnian.smallyellowo.foodie.appbase.PreferencesData;

/**
 * Created by Administrator on 2016-7-12.
 */
public class Constants {
    public static final int ENV_DEVELOP = 0;//开发环境
    public static final int ENV_TEST = 1;//测试环境
    public static final int ENV_EMULATE = 2;//仿真环境
    public static final int ENV_RELEASE = 3;//官方发布环境

    public static final int EMPTY = 100;

    private int mCurrentEnv = ENV_RELEASE;
    private static Constants constants = null;
    private String NET_HEADER_ADRESS = null;
    private String SHARE_URL_PROFIX = null;

    public static final String QQ_APP_ID = "1105925460";

    /**
     * 阿里云配置
     */
    public String accessKey = "lIbEPG3B1z6XPy61";
    public String secretKey = "OcgO34eFyTOB5rfK0CySjqldRIDg0s";
    public String endPoint = "http://oss-cn-beijing.aliyuncs.com";
    public String bucketName = null;
    public String header_original = null;
    public String header_image = null;


    /**
     * 下面两个成员主要用于拼接参数使用，有可能出现图片都不是咱们自己阿里云的数据
     */
    public String publishEnvironmentHeaderOriginal = "http://smallyellow.tinydonuts.cn/";
    public String testEnvironmentHeaderOriginal = "http://smallyellowotest.tinydonuts.cn/";

    public static Constants shareConstants() {
        if (constants == null){
            synchronized (Constants.class) {
                if (constants == null) {
                    constants = new Constants();
                }
            }
        }
        return constants;
    }

    private Constants() {
        super();
        Integer buildConfigDefine = BuildConfig.BUILD_TYPE_DEFINE;

        if (BuildConfig.DEBUG) {
            setDeveloperConfig();
        } else {
            if (buildConfigDefine == 2001) {
                //本地签名打包方式
                setDeveloperConfig();
            } else if (buildConfigDefine == 2002) {
                //测试环境
                setAppEnvironment(ENV_TEST);
            } else if (buildConfigDefine == 2003) {
                //仿真环境
                setAppEnvironment(ENV_EMULATE);
            } else if (buildConfigDefine == 2004) {
                //官方环境
                setAppEnvironment(ENV_RELEASE);
            }
        }
    }


    private void setDeveloperConfig() {
        int type = ENV_TEST;
        setAppEnvironment(type);

        //用户动态切换环境 by chenglin 2017年3月23日
        int index = PreferencesData.getDevelopEnvironmentId();
        if (index >= 0) {
            setAppEnvironment(index);
        }
    }

    //设置环境参数
    private void setAppEnvironment(final int type) {
        mCurrentEnv = type;
        switch (type) {
            case ENV_DEVELOP:
                //开发环境
                this.bucketName = "smallyellowotest";
                this.header_original = "http://smallyellowotest.oss-cn-beijing.aliyuncs.com";
                this.header_image = "http://smallyellowotest.tinydonuts.cn/";
                this.NET_HEADER_ADRESS = "http://120.26.116.103:8080";
                this.SHARE_URL_PROFIX = "http://web-testfiles.tinydonuts.cn";
                break;
            case ENV_TEST:
                //测试环境
                this.bucketName = "smallyellowotest";
                this.header_original = "http://smallyellowotest.oss-cn-beijing.aliyuncs.com";
                this.header_image = "http://smallyellowotest.tinydonuts.cn/";
                this.NET_HEADER_ADRESS = "http://121.43.147.67:8080";
                this.SHARE_URL_PROFIX = "http://web-testfiles.tinydonuts.cn";
                break;
            case ENV_EMULATE:
                //仿真环境
                this.bucketName = "smallyellowo";
                this.header_original = "http://smallyellowo.oss-cn-beijing.aliyuncs.com";
                this.header_image = "http://smallyellow.tinydonuts.cn/";
                this.NET_HEADER_ADRESS = "http://120.55.148.146";
                this.SHARE_URL_PROFIX = "http://web-fzfiles.tinydonuts.cn";
                break;
            case ENV_RELEASE:
                //官方发布环境
                this.bucketName = "smallyellowo";
                this.header_original = "http://smallyellowo.oss-cn-beijing.aliyuncs.com";
                this.header_image = "http://smallyellow.tinydonuts.cn/";
                this.NET_HEADER_ADRESS = "http://o-w.fengnian.cn";
                this.SHARE_URL_PROFIX = "http://web-appfiles.tinydonuts.cn";
                break;
        }
    }

    public int getAppEnvironment(){
        return mCurrentEnv;
    }

    public boolean isPublishEnvironment() {
        if (mCurrentEnv == ENV_RELEASE){
            return true;
        }else {
            return false;
        }
    }


    public String getNetHeaderAdress() {
        return this.NET_HEADER_ADRESS;
    }

    public String getShareUrlProfix() {
        return this.SHARE_URL_PROFIX;
    }

    //
    public static int SmallImage = FFUtils.getPx(90);
    public static int AvatarImage = FFUtils.getPx(60);
    public static int BigImage = -1;
    public static int OriginalImage = 4096;
    public static int MiddleImage = FFUtils.getDisWidth()  / 2;


    public static final int PHOTO_GRAPH = 1101;//拍照

    public static final int BG_ALUM = 1102;// 个人背景
    public static final int HEAD_ALUM = 1103;// 头像 相册
}
