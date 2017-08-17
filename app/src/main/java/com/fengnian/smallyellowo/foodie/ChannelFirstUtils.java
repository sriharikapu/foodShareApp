package com.fengnian.smallyellowo.foodie;

import android.view.View;
import android.widget.ImageView;

import com.fan.framework.config.Tool;
import com.fan.framework.utils.FFUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenglin on 2017-3-22.
 */

public class ChannelFirstUtils {
//    渠道列表
//    UMENG_CHANNEL|CHANNEL 360应用平台 360|1
//    UMENG_CHANNEL|CHANNEL 腾讯应用宝 yyb|2
//    UMENG_CHANNEL|CHANNEL 小米 mi|3
//    UMENG_CHANNEL|CHANNEL 魅族商店 meizu|4
//    UMENG_CHANNEL|CHANNEL 华为 huawei|5
//    UMENG_CHANNEL|CHANNEL VIVO vivo|6
//    UMENG_CHANNEL|CHANNEL oppo oppo|7
//    UMENG_CHANNEL|CHANNEL 百度手机助手 baidu|8
//    UMENG_CHANNEL|CHANNEL ali alii|9
//    UMENG_CHANNEL|CHANNEL 其他 other|9999
//    UMENG_CHANNEL|CHANNEL 粉丝通 fensit|10
//    UMENG_CHANNEL|CHANNEL 今日头条 toutiao|11
//    UMENG_CHANNEL|CHANNEL 广点通1 gdt1|12
//    UMENG_CHANNEL|CHANNEL 广点通2 gdt2|13
//    UMENG_CHANNEL|CHANNEL 广点通3 gdt3|14
//    UMENG_CHANNEL|CHANNEL 智汇推1 zhiht1|15
//    UMENG_CHANNEL|CHANNEL 智汇推2 zhiht2|16
//    UMENG_CHANNEL|CHANNEL 智汇推3 zhiht3|17
//    UMENG_CHANNEL|CHANNEL 微信 wx|18

    private static final String FIRST_CHANNEL = "2.8.1";
    private static final List<ChannelItem> mChannelList = new ArrayList<ChannelItem>();

    private static final String[] channels = new String[]{"",""};

    static {
        ChannelItem item1;
        item1 = new ChannelItem("1", R.drawable.channel_360_flag);
        mChannelList.add(item1);
//        item1 = new ChannelItem("2", R.drawable.channel_yyb_flag);
//        mChannelList.add(item1);
        item1 = new ChannelItem("3", R.drawable.mi_logo);
        mChannelList.add(item1);
    }

    /**
     * 设置首发渠道。写明首发渠道的版本号才是首发，默认只有改了首发版本号才会是首发。
     * 如果有多个首发渠道。下面写同样的代码，或者再继续优化代码
     *
     * @return true为有首发
     */
    public static boolean setFirstChannelFlag(ImageView channelImage) {
        boolean isHasFirst = false;

        channelImage.setVisibility(View.GONE);
        if (mChannelList.size() <= 0) {
            return isHasFirst;
        }
        for (ChannelItem item : mChannelList) {
            if (FIRST_CHANNEL.equals(FFUtils.getVerName())) {
                Object channel = Tool.getChannel();
                if (channel != null) {
                    String currentChannel = channel.toString();
                    if (item.channelId.equals(currentChannel)) {
                        isHasFirst = true;
                        channelImage.setVisibility(View.VISIBLE);
                        channelImage.setImageResource(item.channelResid);
                        break;
                    }
                }
            }
        }

        return isHasFirst;
    }

    public static final class ChannelItem {
        public String channelId;
        public int channelResid;

        public ChannelItem(String channel_id, int channel_resid) {
            channelId = channel_id;
            channelResid = channel_resid;
        }
    }
}
