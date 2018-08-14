package com.fh.entity.system;

import lombok.Data;

@Data
public class PointsSendGoods {
    private String id;
    private String userId;
    private Integer goodsNum;//商品编号
    private Integer quantityOfGoods;//商品数量
    private String goodsName;//商品名称
    private String consignee;//名字
    private String consigneeAddress;//地址
    private String consigneePhone;//电话
    private String createTime;//创建时间
    private String logistics;//物流单号
    private String logisticsCompany;//物流公司
    private String sendsTag;//是否发送
    private String remark;//备注
    private String imgUrl;

}
