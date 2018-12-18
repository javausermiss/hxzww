package com.fh.entity.system;

public class SendGoods {
    private Integer ID;
    private Integer PLAYBACK_ID;
    private String USER_ID;
    private String GOODS_NUM;
    private String CNEE_NAME;
    private String CNEE_ADDRESS;
    private String CNEE_PHONE;
    private String CREATE_TIME;
    private String REMARK;
    private String UPDATE_TIME;
    private String POST_REMARK;//发货备注
    private String MODE_DESPATCH;//发货方式
    private String FMS_NAME;//物流名称
    private String FMS_ORDER_NO;//物流单号
    private String FMS_TIME;//发货时间
    private String SENDBOOLEAN;//快递状态
    private String TOY_NUM;//抓中的娃娃编号，即游戏列表ID
    private String SEND_NUM_ID;//拼接以后的订单流水的单号
    private String LEVELGIFT;//等级礼包



    public SendGoods(){}

    public SendGoods(Integer PLAYBACK_ID, String USER_ID, String GOODS_NUM, String CNEE_NAME, String CNEE_ADDRESS, String CNEE_PHONE, String CREATE_TIME, String REMARK) {
        this.PLAYBACK_ID = PLAYBACK_ID;
        this.USER_ID = USER_ID;
        this.GOODS_NUM = GOODS_NUM;
        this.CNEE_NAME = CNEE_NAME;
        this.CNEE_ADDRESS = CNEE_ADDRESS;
        this.CNEE_PHONE = CNEE_PHONE;
        this.CREATE_TIME = CREATE_TIME;
        this.REMARK = REMARK;
    }

    public String getLEVELGIFT() {
        return LEVELGIFT;
    }

    public void setLEVELGIFT(String LEVELGIFT) {
        this.LEVELGIFT = LEVELGIFT;
    }

    public String getSEND_NUM_ID() {
        return SEND_NUM_ID;
    }

    public void setSEND_NUM_ID(String SEND_NUM_ID) {
        this.SEND_NUM_ID = SEND_NUM_ID;
    }

    public String getTOY_NUM() {
        return TOY_NUM;
    }

    public void setTOY_NUM(String TOY_NUM) {
        this.TOY_NUM = TOY_NUM;
    }

    public String getSENDBOOLEAN() {
        return SENDBOOLEAN;
    }

    public void setSENDBOOLEAN(String SENDBOOLEAN) {
        this.SENDBOOLEAN = SENDBOOLEAN;
    }

    public String getFMS_TIME() {
        return FMS_TIME;
    }

    public void setFMS_TIME(String FMS_TIME) {
        this.FMS_TIME = FMS_TIME;
    }

    public String getFMS_NAME() {
        return FMS_NAME;
    }

    public void setFMS_NAME(String FMS_NAME) {
        this.FMS_NAME = FMS_NAME;
    }

    public String getFMS_ORDER_NO() {
        return FMS_ORDER_NO;
    }

    public void setFMS_ORDER_NO(String FMS_ORDER_NO) {
        this.FMS_ORDER_NO = FMS_ORDER_NO;
    }

    public String getMODE_DESPATCH() {
        return MODE_DESPATCH;
    }

    public void setMODE_DESPATCH(String MODE_DESPATCH) {
        this.MODE_DESPATCH = MODE_DESPATCH;
    }

    public String getPOST_REMARK() {
        return POST_REMARK;
    }

    public void setPOST_REMARK(String POST_REMARK) {
        this.POST_REMARK = POST_REMARK;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getPLAYBACK_ID() {
        return PLAYBACK_ID;
    }

    public void setPLAYBACK_ID(Integer PLAYBACK_ID) {
        this.PLAYBACK_ID = PLAYBACK_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getGOODS_NUM() {
        return GOODS_NUM;
    }

    public void setGOODS_NUM(String GOODS_NUM) {
        this.GOODS_NUM = GOODS_NUM;
    }

    public String getCNEE_NAME() {
        return CNEE_NAME;
    }

    public void setCNEE_NAME(String CNEE_NAME) {
        this.CNEE_NAME = CNEE_NAME;
    }

    public String getCNEE_ADDRESS() {
        return CNEE_ADDRESS;
    }

    public void setCNEE_ADDRESS(String CNEE_ADDRESS) {
        this.CNEE_ADDRESS = CNEE_ADDRESS;
    }

    public String getCNEE_PHONE() {
        return CNEE_PHONE;
    }

    public void setCNEE_PHONE(String CNEE_PHONE) {
        this.CNEE_PHONE = CNEE_PHONE;
    }

    public String getCREATE_TIME() {
        return CREATE_TIME;
    }

    public void setCREATE_TIME(String CREATE_TIME) {
        this.CREATE_TIME = CREATE_TIME;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getUPDATE_TIME() {
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME(String UPDATE_TIME) {
        this.UPDATE_TIME = UPDATE_TIME;
    }
}
