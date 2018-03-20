package com.fh.service.system.trans;

import com.fh.entity.system.TransLog;
import com.fh.util.resp.TxnResp;

/**
 *
 * @author 账户信息表
 *
 */
public interface AccountOperManager {

	/**
	 * 用户开户操作
	 * @param mchntId 所属的合作商户
	 * @param userId  用户主键
	 * @return
	 * @throws Exception
	 */
	public TxnResp openAccountInfByUser(String userId) throws Exception;
	
	
	/**
	 * addTransOrder 添加交易订单
	 * @param transLog 交易流水记录
	 * @return
	 * @throws Exception
	 */
	public TxnResp addTransOrder(TransLog transLog) throws Exception;
	
	
	/**
	 * 账户充值
	 * @param transId 交易流水号
	 * @return
	 */
	public TxnResp rechargeAccountInf(String transId)throws Exception;
	
	
	/**
	 * 账户扣款
	 * @param accountInf 账户信息表
	 * @return
	 */
	public TxnResp deductAccountInf(String transId) throws Exception;
	
}
