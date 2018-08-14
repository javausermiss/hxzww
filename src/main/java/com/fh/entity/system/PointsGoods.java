package com.fh.entity.system;

import lombok.Data;

/**
 * 积分商品
 */
@Data
public class PointsGoods {
    private String id;
    private String goodsNum;//编号
    private String goodsName;//名称
    private String imgUrl;//图片地址
    private String points;//积分
    private String type;
    private String imgUrl_goodsDetail_top;//商品详情头部
    private String imgUrl_goodsDetail_mid;//商品详情中部
    private Integer originalValueOfGoods;//商品原始价值
    private String showTag;//是否展示标签

    public static void main(String[] as){
        String a = "showTag";
         System.out.println(a.toUpperCase());
    }
}
