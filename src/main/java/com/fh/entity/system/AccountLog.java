package com.fh.entity.system;

import java.util.Date;

public class AccountLog implements java.io.Serializable {

	private String logId;

	private String accId;

	private String lastTxnDate;

	private String lastTxnTime;

	private String transId;

	private String transAmt;

	private String accAmt;

	private String accTotalAmt;

	private String resColumn1;

	private String resColumn2;

	private String resColumn3;

	private String createUser;

	private Date createDate;

	private String updateUser;

	private Date updateDate;

	private String lockVersion;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
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

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getTransAmt() {
		return transAmt;
	}

	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	public String getAccAmt() {
		return accAmt;
	}

	public void setAccAmt(String accAmt) {
		this.accAmt = accAmt;
	}

	public String getAccTotalAmt() {
		return accTotalAmt;
	}

	public void setAccTotalAmt(String accTotalAmt) {
		this.accTotalAmt = accTotalAmt;
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
