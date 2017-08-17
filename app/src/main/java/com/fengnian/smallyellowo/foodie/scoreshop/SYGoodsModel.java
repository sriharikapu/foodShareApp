package com.fengnian.smallyellowo.foodie.scoreshop;

import com.fengnian.smallyellowo.foodie.bean.publics.SYImage;

import java.util.List;

/**
 * Created by chenglin on 2017-5-17.
 * 文档地址：http://tools.tinydonuts.cn:8090/pages/viewpage.action?pageId=4915653
 */
public class SYGoodsModel {
    public String goodsId;//商品的ID
    public SYImage goodsImage;//商品的图片
    public String goodsPoints;//兑换商品所需的积分
    public String goodsTitle;//商品名称
    public String goodsStock;//兑换商品的库存
    public List<SYGoodsIntroduction> goodsIntroductionList;
}
