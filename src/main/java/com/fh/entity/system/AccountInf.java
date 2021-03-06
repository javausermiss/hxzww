package com.fh.entity.system;

import java.util.Date;

/**
 * 账户余额信息实体类
 */
public class AccountInf{
	
	private String accId;
	
	private String mchntId;
	
	private String userId;
	
	private String accType;
	
	private String accState; //账户状态 10：正常、20：挂失、40：冻结、99：销户
	
	private String accBal;
	
	private String accBalCode;
	
	private String freezeAmt;
	
	private String lastTxnDate;

	private String lastTxnTime;

	private String resColumn1;

	private String resColumn2;

	private String resColumn3;

	private String createUser;

	private Date createDate;

	private String updateUser;

	private Date updateDate;

	private String lockVersion;

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getAccState() {
		return accState;
	}

	public void setAccState(String accState) {
		this.accState = accState;
	}

	public String getAccBal() {
		return accBal;
	}

	public void setAccBal(String accBal) {
		this.accBal = accBal;
	}

	public String getAccBalCode() {
		return accBalCode;
	}

	public void setAccBalCode(String accBalCode) {
		this.accBalCode = accBalCode;
	}

	public String getFreezeAmt() {
		return freezeAmt;
	}

	public void setFreezeAmt(String freezeAmt) {
		this.freezeAmt = freezeAmt;
	}

	public String getLastTxnDate() {
		return lastTxnDate;
	}

	public void setLastTxnDate(String lastTxnDate) {
		this.lastTxnDate = lastTxnDate;
	}

	public String getLastTxnTime() {
		return lastTxnTime;
	}

	public void setLastTxnTime(String lastTxnTime) {
		this.lastTxnTime = lastTxnTime;
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

	public String getLockVersion() {
		return lockVersion;
	}

	public void setLockVersion(String lockVersion) {
		this.lockVersion = lockVersion;
	}
}
