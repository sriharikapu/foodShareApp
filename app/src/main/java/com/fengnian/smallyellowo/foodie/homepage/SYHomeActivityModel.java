package com.fengnian.smallyellowo.foodie.homepage;

import java.io.Serializable;

/**
 * Created by chenglin on 2017-4-12.
 * 文档地址：http://tools.tinydonuts.cn:8090/display/yellowCircle/SYHomeActivityModel
 */

public class SYHomeActivityModel implements Serializable {
    public String imageUrl;//图片地址
    public String messageTitle;//活动标题
    public String messageTime;//活动发布时间
    public String messageContent;//活动描述
    public String messageHtmlUrl;//活动链接
    public String messageId;//活动ID
}
