package com.fh.service.system.trans.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fh.entity.system.AccountInf;
import com.fh.entity.system.AccountLog;
import com.fh.entity.system.TransLog;
import com.fh.service.system.trans.AccountInfManager;
import com.fh.service.system.trans.AccountLogManager;
import com.fh.service.system.trans.AccountOperManager;
import com.fh.service.system.trans.TransLogManager;
import com.fh.util.AccountUtil;
import com.fh.util.DateUtil;
import com.fh.util.NumberUtils;
import com.fh.util.resp.TxnResp;

/** 
 * 说明： 账户操作
 * 创建人：FH Q313596790
 * 创建时间：2017-12-26
 * @version
 */
@Service("accountOperService")
public class AccountOperService implements AccountOperManager{
	
	/**
	 * transLogManager 交易流水表
	 */
	@Resource(name = "translogService")
	private TransLogManager translogService;
	
	
	/**
	 * accountLogManager 账户日志表
	 */
	@Resource(name = "accountLogService")
	private AccountLogManager accountLogService;
	
	/**
	 * accountInfManager 账户信息表
	 */
	@Resource(name = "accountInfService")
	private AccountInfManager accountInfService;
	

	/**
	 * 用户开户操作
	 * @param mchntId 所属的合作商户
	 * @param userId  用户主键
	 * @return
	 * @throws Exception
	 */
	public TxnResp openAccountInfByUser(String userId) throws Exception {
		
		TxnResp txnResp=new TxnResp();
		txnResp.setRespCode(true);
		
		AccountInf accountInf= accountInfService.findByUserId(userId);
		if(accountInf ==null){
			accountInf=new AccountInf();
			accountInf.setUserId(userId);
			accountInf.setAccType("10");//10：用户，20：商户
			accountInf.setAccState("0"); //账户状态
			accountInf.setAccBal("0"); //账户余额
			accountInf.setAccBalCode(AccountUtil.MD5SignAccBal(userId, accountInf.getAccBal()));//账户余额密文
			accountInf.setFreezeAmt("0");//冻结资金
			//保存账户信息
			accountInfService.save(accountInf);
		}
		
		txnResp.setPriAccId(accountInf.getAccId()); //返回当前的主账户ID
		txnResp.setResultCode("00"); //操作成功
		return txnResp;
	}
	
	/**
	 * addTransOrder 添加交易订单
	 * @param transLog 交易流水记录
	 * @return
	 * @throws Exception
	 */
	public TxnResp addTransOrder(TransLog transLog) throws Exception{
		TxnResp txnResp=new TxnResp();
		txnResp.setRespCode(true);
		translogService.save(transLog);
		txnResp.setResultCode("00"); //操作成功
		txnResp.setMsg(transLog.getTransId()); //返回交易订单的Id
		return txnResp;
	}

	/**
	 * 账户充值
	 * @param accountInf 账户信息表
	 * @return
	 * @throws Exception 
	 */
	public TxnResp rechargeAccountInf(String transId) throws Exception {
		TxnResp txnResp=new TxnResp();
		txnResp.setRespCode(true);
		
		//查找交易流水记录
		TransLog transLog=translogService.findById(transId);
		if(transLog==null){
			txnResp.setResultCode("10001"); //返回状态
			txnResp.setMsg("交易流水不存在");
			return txnResp;
		}
		
		//查找账户信息
		AccountInf accountInf=accountInfService.findById(transLog.getPriAccId());
		if(accountInf==null){
			txnResp.setResultCode("10002"); //返回状态
			txnResp.setMsg("账户信息不存在");
			return txnResp;
		}
		
		//校验账户信息是否正常
		if(!AccountUtil.accBalEqualsCode(accountInf.getUserId(), accountInf.getAccBal(), accountInf.getAccBalCode())){
			txnResp.setResultCode("10003"); //返回状态
			txnResp.setMsg("账户余额信息异常");
			return txnResp;
		}
		accountInf.setAccBal(NumberUtils.add(accountInf.getAccBal(), transLog.getTransAmt()));
		accountInf.setAccBalCode(AccountUtil.MD5SignAccBal(accountInf.getUserId(), accountInf.getAccBal()));
		accountInfService.edit(accountInf); //修改账户信息
		
		//记录账户日志记录
		AccountLog accountLog=new AccountLog();
		accountLog.setLastTxnDate(DateUtil.getDays());
		accountLog.setLastTxnTime(DateUtil.getTimeHHmmss());
		accountLog.setAccId(accountInf.getAccId());
		accountLog.setTransType(transLog.getTransType());//交易类型
		accountLog.setTransAmt(transLog.getTransAmt()); //交易流水表 充值金币金额
		accountLog.setOrgTransAmt(transLog.getOrgTransAmt());
		accountLog.setAccAmt(accountLog.getTransAmt());
		accountLog.setAccTotalAmt(accountInf.getAccBal()); //账户处理后的总金额
		accountLog.setLogType("1");//加款
		accountLog.setResColumn1(transLog.getResColumn1());
		accountLogService.save(accountLog);
		txnResp.setResultCode("00"); //操作成功
		
		return txnResp;
	}

	/**
	 * 账户扣款
	 * @param transId 交易流水表
	 * @return
	 */
	public TxnResp deductAccountInf(String  transId) throws Exception {
		TxnResp txnResp=new TxnResp();
		txnResp.setRespCode(true);
		
		//查找交易流水记录
		TransLog transLog=translogService.findById(transId);
		if(transLog==null){
			txnResp.setResultCode("10001"); //返回状态
			txnResp.setMsg("交易流水不存在");
			return txnResp;
		}
		
		//查找账户信息
		AccountInf accountInf=accountInfService.findById(transLog.getPriAccId());
		if(accountInf==null){
			txnResp.setResultCode("10002"); //返回状态
			txnResp.setMsg("账户信息不存在");
			return txnResp;
		}
		
		//校验账户信息是否正常
		if(!AccountUtil.accBalEqualsCode(accountInf.getUserId(), accountInf.getAccBal(), accountInf.getAccBalCode())){
			txnResp.setResultCode("10003"); //返回状态
			txnResp.setMsg("账户余额信息异常");
			return txnResp;
		}
		
		//校验余额
		String accBal=NumberUtils.sub(accountInf.getAccBal(), transLog.getTransAmt());
		if(Integer.parseInt(accBal)<0){
			txnResp.setResultCode("10004"); //返回状态
			txnResp.setMsg("账户余额不足");
			return txnResp;
		}
		
		accountInf.setAccBal(NumberUtils.sub(accountInf.getAccBal(), transLog.getTransAmt()));  //扣钱
		accountInf.setAccBalCode(AccountUtil.MD5SignAccBal(accountInf.getUserId(), accountInf.getAccBal()));
		accountInfService.edit(accountInf); //修改账户信息
		
		//记录账户日志记录
		AccountLog accountLog=new AccountLog();
		accountLog.setAccId(accountInf.getAccId());
		accountLog.setLastTxnDate(DateUtil.getDays());
		accountLog.setLastTxnTime(DateUtil.getTimeHHmmss());
		accountLog.setTransType(transLog.getTransType());//交易类型
		accountLog.setTransAmt(transLog.getTransAmt()); //交易流水表 充值金币金额
		accountLog.setOrgTransAmt(transLog.getOrgTransAmt()); //原交易金额
		accountLog.setAccAmt(accountLog.getTransAmt());
		accountLog.setAccTotalAmt(accountInf.getAccBal()); //账户处理后的总金额
		accountLog.setLogType("2"); //减款
		accountLog.setResColumn1(transLog.getResColumn1());
		accountLogService.save(accountLog);
		txnResp.setResultCode("00"); //操作成功
		return txnResp;
	}
	
}

