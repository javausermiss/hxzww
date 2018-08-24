package com.fh.entity.system;

public class AppUser {
    private String USER_ID;
    private String USERNAME;
    private String PASSWORD;
    private String NAME;
    private String RIGHTS;
    private String ROLE_ID;
    private String LAST_LOGIN;
    private String IP;
    private String STATUS;
    private String BZ;
    private String PHONE;
    private String SFID;
    private String START_TIME;
    private String END_TIME;
    private Integer YEARS;
    private String NUMBER;
    private String EMAIL;
    private String CREATETIME;
    private String IMAGE_URL;
    private String ADDRESS;
    private String BALANCE;
    private Integer DOLLTOTAL;
    private String NICKNAME;
    private String CNEE_NAME;
    private String CNEE_ADDRESS;
    private String CNEE_PHONE;
    private String SIGN_TAG;
    private String RANK;
    private Integer BET_NUM;
    //推币机奖励倍数标签
    private Integer COIN_MULTIPLES;
    private String PRO_USER_ID; //
    private String CHANNEL_NUM;//渠道包编号

    //首充标签
    private String FIRST_CHARGE;
    //周卡天数
    private Integer WEEKS_CARD;
    private String WEEKS_CARD_TAG;

    //月卡天数
    private Integer MONTH_CARD;
    private String MONTH_CARD_TAG;
    
    private String OPEN_TYPE;
    private String GENDER;

    //绑定的银行卡手机号码
    private String BDPHONE;

    //积分
    private Integer POINTS;
    //消费金币奖励倍数标签
    private Integer POINTS_MULTIPLES;
    //今日抓娃娃总数
    private Integer TODAY_POOH ;

    private Integer TODAY_GUESS;

    private String JCID;

    private String JDNUM;

    public AppUser() {
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "USER_ID='" + USER_ID + '\'' +
                ", USERNAME='" + USERNAME + '\'' +
                ", PASSWORD='" + PASSWORD + '\'' +
                ", NAME='" + NAME + '\'' +
                ", RIGHTS='" + RIGHTS + '\'' +
                ", ROLE_ID='" + ROLE_ID + '\'' +
                ", LAST_LOGIN='" + LAST_LOGIN + '\'' +
                ", IP='" + IP + '\'' +
                ", STATUS='" + STATUS + '\'' +
                ", BZ='" + BZ + '\'' +
                ", PHONE='" + PHONE + '\'' +
                ", SFID='" + SFID + '\'' +
                ", START_TIME='" + START_TIME + '\'' +
                ", END_TIME='" + END_TIME + '\'' +
                ", YEARS=" + YEARS +
                ", NUMBER='" + NUMBER + '\'' +
                ", EMAIL='" + EMAIL + '\'' +
                ", CREATETIME='" + CREATETIME + '\'' +
                ", IMAGE_URL='" + IMAGE_URL + '\'' +
                ", ADDRESS='" + ADDRESS + '\'' +
                ", BALANCE='" + BALANCE + '\'' +
                ", DOLLTOTAL=" + DOLLTOTAL +
                ", NICKNAME='" + NICKNAME + '\'' +
                ", CNEE_NAME='" + CNEE_NAME + '\'' +
                ", CNEE_ADDRESS='" + CNEE_ADDRESS + '\'' +
                ", CNEE_PHONE='" + CNEE_PHONE + '\'' +
                '}';
    }

    public Integer getTODAY_GUESS() {
        return TODAY_GUESS;
    }

    public void setTODAY_GUESS(Integer TODAY_GUESS) {
        this.TODAY_GUESS = TODAY_GUESS;
    }

    public Integer getTODAY_POOH() {
        return TODAY_POOH;
    }

    public void setTODAY_POOH(Integer TODAY_POOH) {
        this.TODAY_POOH = TODAY_POOH;
    }

    public AppUser(String USER_ID, String PHONE, String CREATETIME, String USERNAME, String NICKNAME , String IMAGE_URL, String BALANCE) {
        this.USER_ID = USER_ID;
        this.PHONE = PHONE;
        this.CREATETIME = CREATETIME;
        this.USERNAME = USERNAME;
        this.NICKNAME = NICKNAME;
        this.IMAGE_URL = IMAGE_URL;
        this.BALANCE = BALANCE;
    }

    public String getJDNUM() {
        return JDNUM;
    }

    public void setJDNUM(String JDNUM) {
        this.JDNUM = JDNUM;
    }

    public String getJCID() {
        return JCID;
    }

    public void setJCID(String JCID) {
        this.JCID = JCID;
    }

    public Integer getPOINTS_MULTIPLES() {
        return POINTS_MULTIPLES;
    }

    public void setPOINTS_MULTIPLES(Integer POINTS_MULTIPLES) {
        this.POINTS_MULTIPLES = POINTS_MULTIPLES;
    }

    public Integer getPOINTS() {
        return POINTS;
    }

    public void setPOINTS(Integer POINTS) {
        this.POINTS = POINTS;
    }

    public String getBDPHONE() {
        return BDPHONE;
    }

    public void setBDPHONE(String BDPHONE) {
        this.BDPHONE = BDPHONE;
    }

    public String getWEEKS_CARD_TAG() {
        return WEEKS_CARD_TAG;
    }

    public void setWEEKS_CARD_TAG(String WEEKS_CARD_TAG) {
        this.WEEKS_CARD_TAG = WEEKS_CARD_TAG;
    }

    public String getMONTH_CARD_TAG() {
        return MONTH_CARD_TAG;
    }

    public void setMONTH_CARD_TAG(String MONTH_CARD_TAG) {
        this.MONTH_CARD_TAG = MONTH_CARD_TAG;
    }

    public Integer getWEEKS_CARD() {
        return WEEKS_CARD;
    }

    public void setWEEKS_CARD(Integer WEEKS_CARD) {
        this.WEEKS_CARD = WEEKS_CARD;
    }

    public Integer getMONTH_CARD() {
        return MONTH_CARD;
    }

    public void setMONTH_CARD(Integer MONTH_CARD) {
        this.MONTH_CARD = MONTH_CARD;
    }

    public String getFIRST_CHARGE() {
        return FIRST_CHARGE;
    }

    public void setFIRST_CHARGE(String FIRST_CHARGE) {
        this.FIRST_CHARGE = FIRST_CHARGE;
    }

    public Integer getCOIN_MULTIPLES() {
        return COIN_MULTIPLES;
    }

    public String getCHANNEL_NUM() {
        return CHANNEL_NUM;
    }

    public void setCHANNEL_NUM(String CHANNEL_NUM) {
        this.CHANNEL_NUM = CHANNEL_NUM;
    }

    public void setCOIN_MULTIPLES(Integer COIN_MULTIPLES) {
        this.COIN_MULTIPLES = COIN_MULTIPLES;
    }

    public Integer getBET_NUM() {
        return BET_NUM;
    }

    public void setBET_NUM(Integer BET_NUM) {
        this.BET_NUM = BET_NUM;
    }

    public String getRANK() {
        return RANK;
    }

    public void setRANK(String RANK) {
        this.RANK = RANK;
    }

    public String getSIGN_TAG() {
        return SIGN_TAG;
    }

    public void setSIGN_TAG(String SIGN_TAG) {
        this.SIGN_TAG = SIGN_TAG;
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

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public Integer getDOLLTOTAL() {
        return DOLLTOTAL;
    }

    public void setDOLLTOTAL(Integer DOLLTOTAL) {
        this.DOLLTOTAL = DOLLTOTAL;
    }

    public String getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(String BALANCE) {
        this.BALANCE = BALANCE;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getRIGHTS() {
        return RIGHTS;
    }

    public void setRIGHTS(String RIGHTS) {
        this.RIGHTS = RIGHTS;
    }

    public String getROLE_ID() {
        return ROLE_ID;
    }

    public void setROLE_ID(String ROLE_ID) {
        this.ROLE_ID = ROLE_ID;
    }

    public String getLAST_LOGIN() {
        return LAST_LOGIN;
    }

    public void setLAST_LOGIN(String LAST_LOGIN) {
        this.LAST_LOGIN = LAST_LOGIN;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getBZ() {
        return BZ;
    }

    public void setBZ(String BZ) {
        this.BZ = BZ;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }

    public String getSFID() {
        return SFID;
    }

    public void setSFID(String SFID) {
        this.SFID = SFID;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }

    public Integer getYEARS() {
        return YEARS;
    }

    public void setYEARS(Integer YEARS) {
        this.YEARS = YEARS;
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public void setNUMBER(String NUMBER) {
        this.NUMBER = NUMBER;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

	public String getPRO_USER_ID() {
		return PRO_USER_ID;
	}

	public void setPRO_USER_ID(String pRO_USER_ID) {
		PRO_USER_ID = pRO_USER_ID;
	}

	public String getOPEN_TYPE() {
		return OPEN_TYPE;
	}

	public void setOPEN_TYPE(String oPEN_TYPE) {
		OPEN_TYPE = oPEN_TYPE;
	}

	public String getGENDER() {
		return GENDER;
	}

	public void setGENDER(String gENDER) {
		GENDER = gENDER;
	}

}
