package com.fh.entity.system;

import java.util.Date;

/**
 * 交易流水实体类
 */
public class TransOrder implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3868626902078983436L;
	
	private String orderId;
	private String orgOrderId;
	private String dmsRelatedKey;
	private String orgDmsRelatedKey;
	private String userId; //执行用户Id
	private String transType;
	private String orderSt;
	private String transCode;
	private String priAccId;
	private String dmsUserId;	//当前操作的用户Id
	private String dmsUserUnionId; //当前账户的userId
	private String transAmt;
	private String orgTransAmt;
	private String transCurrCd;
	private String transChnl;
	private String transFee;
	private String transFeeType;
	private String tfrInAccId;
	private String tfrOutAccId;
	private String addInfo;
	private String remarks;
	private String createUser;
	private Date createDate;
	private String updateUser;
	private Date updateDate;
	private long lockVersion;
	private String mchntId;
	
	private String resColumn1;
	private String resColumn2;
	private String resColumn3;
	

	public String getDmsRelatedKey() {
		return dmsRelatedKey;
	}
	public void setDmsRelatedKey(String dmsRelatedKey) {
		this.dmsRelatedKey = dmsRelatedKey;
	}
	public String getOrgDmsRelatedKey() {
		return orgDmsRelatedKey;
	}
	public void setOrgDmsRelatedKey(String orgDmsRelatedKey) {
		this.orgDmsRelatedKey = orgDmsRelatedKey;
	}
	public String getOrderSt() {
		return orderSt;
	}
	public void setOrderSt(String orderSt) {
		this.orderSt = orderSt;
	}

	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getPriAccId() {
		return priAccId;
	}
	public void setPriAccId(String priAccId) {
		this.priAccId = priAccId;
	}

	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getOrgTransAmt() {
		return orgTransAmt;
	}
	public void setOrgTransAmt(String orgTransAmt) {
		this.orgTransAmt = orgTransAmt;
	}
	public String getTransCurrCd() {
		return transCurrCd;
	}
	public void setTransCurrCd(String transCurrCd) {
		this.transCurrCd = transCurrCd;
	}
	public String getTransChnl() {
		return transChnl;
	}
	public void setTransChnl(String transChnl) {
		this.transChnl = transChnl;
	}
	public String getTransFee() {
		return transFee;
	}
	public void setTransFee(String transFee) {
		this.transFee = transFee;
	}
	public String getTransFeeType() {
		return transFeeType;
	}
	public void setTransFeeType(String transFeeType) {
		this.transFeeType = transFeeType;
	}
	public String getTfrInAccId() {
		return tfrInAccId;
	}
	public void setTfrInAccId(String tfrInAccId) {
		this.tfrInAccId = tfrInAccId;
	}
	public String getTfrOutAccId() {
		return tfrOutAccId;
	}
	public void setTfrOutAccId(String tfrOutAccId) {
		this.tfrOutAccId = tfrOutAccId;
	}
	public String getAddInfo() {
		return addInfo;
	}
	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public long getLockVersion() {
		return lockVersion;
	}
	public void setLockVersion(long lockVersion) {
		this.lockVersion = lockVersion;
	}
	public String getMchntId() {
		return mchntId;
	}
	public void setMchntId(String mchntId) {
		this.mchntId = mchntId;
	}
	public String getResColumn1() {
		return resColumn1;
	}
	public void setResColumn1(String resColumn1) {
		this.resColumn1 = resColumn1;
	}
	public String getResColumn2() {
		return resColumn2;
	}
	public void setResColumn2(String resColumn2) {
		this.resColumn2 = resColumn2;
	}
	public String getResColumn3() {
		return resColumn3;
	}
	public void setResColumn3(String resColumn3) {
		this.resColumn3 = resColumn3;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrgOrderId() {
		return orgOrderId;
	}
	public void setOrgOrderId(String orgOrderId) {
		this.orgOrderId = orgOrderId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDmsUserId() {
		return dmsUserId;
	}
	public void setDmsUserId(String dmsUserId) {
		this.dmsUserId = dmsUserId;
	}
	public String getDmsUserUnionId() {
		return dmsUserUnionId;
	}
	public void setDmsUserUnionId(String dmsUserUnionId) {
		this.dmsUserUnionId = dmsUserUnionId;
	}
}
