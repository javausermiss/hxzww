package com.fh.util.resp;

/**
 * 交易返回
 * @author zhuqiuyou
 *
 */
public class TxnResp {

	private boolean respCode=false; //请求状态
	
	private String resultCode;//结果返回状态
	
	private String msg; //错误信息
	
	private String priAccId; //当前的用户账户ID
	
	private String orderId;  //当前操作的订单ID
	
	private String userId;	//当前操作的用户ID

	public boolean isRespCode() {
		return respCode;
	}

	public void setRespCode(boolean respCode) {
		this.respCode = respCode;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPriAccId() {
		return priAccId;
	}

	public void setPriAccId(String priAccId) {
		this.priAccId = priAccId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
