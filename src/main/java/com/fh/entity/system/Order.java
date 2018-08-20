package com.fh.entity.system;

/**
 * 支付订单临时测试实体类
 */
public class Order {
    private String REC_ID;
    private String ORDER_ID;
    private String USER_ID;
    private String CREATETIME;
    private String  REGAMOUNT;
    private String STATUS;
    private String REGGOLD;
    private String ORDER_NO;//外部订单
    private String CTYPE;
    private String CHANNEL;
    private String PAY_TYPE;  //支付的类型
    private String PRO_USER_ID;//订单用户所属的推广用户ID
    
    private String ADD_INFO; //追加信息
    private String OUT_ORDER_ID; //外部订单信息
    private String PAYOUT_TYPE;//支付所属类型，月卡，周卡或者其他

    private String APPVERSION;//app版本号

    
    /***业务扩展字段***/
    private String userNickName;

    public Order() {
    }

    public String getAPPVERSION() {
        return APPVERSION;
    }

    public void setAPPVERSION(String APPVERSION) {
        this.APPVERSION = APPVERSION;
    }

    public String getPAYOUT_TYPE() {
        return PAYOUT_TYPE;
    }

    public void setPAYOUT_TYPE(String PAYOUT_TYPE) {
        this.PAYOUT_TYPE = PAYOUT_TYPE;
    }

    public String getCTYPE() {
        return CTYPE;
    }

    public void setCTYPE(String CTYPE) {
        this.CTYPE = CTYPE;
    }

    public String getCHANNEL() {
        return CHANNEL;
    }

    public void setCHANNEL(String CHANNEL) {
        this.CHANNEL = CHANNEL;
    }

    public String getORDER_NO() {
        return ORDER_NO;
    }

    public void setORDER_NO(String ORDER_NO) {
        this.ORDER_NO = ORDER_NO;
    }

    public String getREGGOLD() {
        return REGGOLD;
    }

    public void setREGGOLD(String REGGOLD) {
        this.REGGOLD = REGGOLD;
    }

    public String getREC_ID() {
        return REC_ID;
    }

    public void setREC_ID(String REC_ID) {
        this.REC_ID = REC_ID;
    }

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getCREATETIME() {
        return CREATETIME;
    }

    public void setCREATETIME(String CREATETIME) {
        this.CREATETIME = CREATETIME;
    }

    public String getREGAMOUNT() {
        return REGAMOUNT;
    }

    public void setREGAMOUNT(String REGAMOUNT) {
        this.REGAMOUNT = REGAMOUNT;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

	public String getPAY_TYPE() {
		return PAY_TYPE;
	}

	public void setPAY_TYPE(String PAY_TYPE) {
		this.PAY_TYPE = PAY_TYPE;
	}

	public String getPRO_USER_ID() {
		return PRO_USER_ID;
	}

	public void setPRO_USER_ID(String PRO_USER_ID) {
		this.PRO_USER_ID = PRO_USER_ID;
	}

	public String getADD_INFO() {
		return ADD_INFO;
	}

	public void setADD_INFO(String ADD_INFO) {
		this.ADD_INFO = ADD_INFO;
	}

	public String getOUT_ORDER_ID() {
		return OUT_ORDER_ID;
	}

	public void setOUT_ORDER_ID(String OUT_ORDER_ID) {
		this.OUT_ORDER_ID = OUT_ORDER_ID;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}
	
}
