package com.fh.entity.system;

import java.util.Date;

/**
 * 交易流水实体类
 */
public class TransLog implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3868626902078983436L;
	
	private String transId;
	private String orgTransId;
	private String dmsRelatedKey;
	private String orgDmsRelatedKey;
	private String transType;
	private String orderSt;
	private String transSt;
	private String transCode;
	private String respCode;
	private String priAccId;
	private String dmsUserId;
	private String dmsUserUnionId;
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
	
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getOrgTransId() {
		return orgTransId;
	}
	public void setOrgTransId(String orgTransId) {
		this.orgTransId = orgTransId;
	}
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
	public String getTransSt() {
		return transSt;
	}
	public void setTransSt(String transSt) {
		this.transSt = transSt;
	}
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getRespCode() {
		return respCode;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public String getPriAccId() {
		return priAccId;
	}
	public void setPriAccId(String priAccId) {
		this.priAccId = priAccId;
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
}
