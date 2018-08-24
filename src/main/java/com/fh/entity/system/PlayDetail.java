package com.fh.entity.system;

/**
 * 游戏记录实体类
 */
public class PlayDetail {

    private String ID;//主键
    private String USERID;//用户昵称
    private String GUESS_ID;//场次ID
    private String DOLLID;//房间昵称
    private String STATE;//是否抓取成功'1'抓取成功 '0'抓取失败
    private String CREATE_DATE;//创建时间
    private String GOLD;
    private String CAMERA_DATE;//视频日期
    private String STOP_FLAG;//0可以下注 -1禁止下注
    private String CONVERSIONGOLD;
    private String POST_STATE; //0 寄存  1 发货  2 兑换 3已发货
    private String SENDGOODS; //发货信息
    private String NICKNAME;//用户昵称
    private String IMAGE_URL;//用户头像
    private String DOLL_URL;//娃娃头像
    private String DOLL_NAME;//娃娃名称
    private String COUNT;
    private String VIEW_STATE;
    private String FREE_DATE;//娃娃机FREE状态更新时间
    private String SEND_ORDER_ID;//发货订单主键ID
    private Integer TOY_ID;//娃娃编号
    private String REWARD_NUM;//开奖号码
    private String MACHINE_TYPE;  //娃娃机类型：1、娃娃机 2、推币机 3、金币机
    
    public String getREWARD_NUM() {
        return REWARD_NUM;
    }

    public void setREWARD_NUM(String REWARD_NUM) {
        this.REWARD_NUM = REWARD_NUM;
    }

    public PlayDetail() {
    }

    public String getFREE_DATE() {
        return FREE_DATE;
    }

    public void setFREE_DATE(String FREE_DATE) {
        this.FREE_DATE = FREE_DATE;
    }

    public Integer getTOY_ID() {
        return TOY_ID;
    }

    public void setTOY_ID(Integer TOY_ID) {
        this.TOY_ID = TOY_ID;
    }

    public String getSEND_ORDER_ID() {
        return SEND_ORDER_ID;
    }

    public void setSEND_ORDER_ID(String SEND_ORDER_ID) {
        this.SEND_ORDER_ID = SEND_ORDER_ID;
    }

    public String getVIEW_STATE() {
        return VIEW_STATE;
    }

    public void setVIEW_STATE(String VIEW_STATE) {
        this.VIEW_STATE = VIEW_STATE;
    }

    public String getSENDGOODS() {
        return SENDGOODS;
    }

    public void setSENDGOODS(String SENDGOODS) {
        this.SENDGOODS = SENDGOODS;
    }

    public String getPOST_STATE() {
        return POST_STATE;
    }

    public void setPOST_STATE(String POST_STATE) {
        this.POST_STATE = POST_STATE;
    }

    public String getCOUNT() {
        return COUNT;
    }

    public void setCOUNT(String COUNT) {
        this.COUNT = COUNT;
    }

    public String getCAMERA_DATE() {
        return CAMERA_DATE;
    }

    public void setCAMERA_DATE(String CAMERA_DATE) {
        this.CAMERA_DATE = CAMERA_DATE;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getDOLL_URL() {
        return DOLL_URL;
    }

    public void setDOLL_URL(String DOLL_URL) {
        this.DOLL_URL = DOLL_URL;
    }

    public String getDOLL_NAME() {
        return DOLL_NAME;
    }

    public void setDOLL_NAME(String DOLL_NAME) {
        this.DOLL_NAME = DOLL_NAME;
    }

    public String getSTOP_FLAG() {
        return STOP_FLAG;
    }

    public void setSTOP_FLAG(String STOP_FLAG) {
        this.STOP_FLAG = STOP_FLAG;
    }

    public String getGOLD() {
        return GOLD;
    }

    public void setGOLD(String GOLD) {
        this.GOLD = GOLD;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCONVERSIONGOLD() {
        return CONVERSIONGOLD;
    }

    public void setCONVERSIONGOLD(String CONVERSIONGOLD) {
        this.CONVERSIONGOLD = CONVERSIONGOLD;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getDOLLID() {
        return DOLLID;
    }

    public void setDOLLID(String DOLLID) {
        this.DOLLID = DOLLID;
    }

    public String getGUESS_ID() {
        return GUESS_ID;
    }

    public void setGUESS_ID(String GUESS_ID) {
        this.GUESS_ID = GUESS_ID;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(String CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

	public String getMACHINE_TYPE() {
		return MACHINE_TYPE;
	}

	public void setMACHINE_TYPE(String mACHINE_TYPE) {
		MACHINE_TYPE = mACHINE_TYPE;
	}

	
}
