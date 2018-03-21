package com.fh.service.system.trans.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.AccountInf;
import com.fh.entity.system.TransLog;
import com.fh.entity.system.TransOrder;
import com.fh.service.system.trans.AccountInfManager;
import com.fh.service.system.trans.AccountLogManager;
import com.fh.service.system.trans.AccountOperManager;
import com.fh.service.system.trans.TransLogManager;
import com.fh.service.system.trans.TransOrderManager;
import com.fh.util.NumberUtils;
import com.fh.util.PageData;
import com.fh.util.Const.AccountTransType;
import com.fh.util.resp.TxnResp;

/** 
 * 说明： 订单表
 * 创建人：FH Q313596790
 * 创建时间：2017-12-26
 * @version
 */
@Service("transOrderService")
public class TransOrderService implements TransOrderManager{
	
	/**
	 * transLogManager 交易流水表
	 */
	@Resource(name = "translogService")
	private TransLogManager translogService;
	
	
	/**
	 * accountInfManager 账户信息表
	 */
	@Resource(name = "accountInfService")
	private AccountInfManager accountInfService;
	
	
	@Resource(name = "accountOperService")
	private AccountOperManager accountOperService;

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(TransOrder transOrder)throws Exception{
		dao.save("TransOrderMapper.save", transOrder);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(String  orderId)throws Exception{
		dao.delete("TransOrderMapper.delete", orderId);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(TransOrder transOrder)throws Exception{
		dao.update("TransOrderMapper.edit", transOrder);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("TransOrderMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(TransOrder pd)throws Exception{
		return (List<PageData>)dao.findForList("TransOrderMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public TransOrder findById(String Id)throws Exception{
		return (TransOrder)dao.findForObject("TransOrderMapper.findById", Id);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("TransOrderMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**修订单返回状态
	 * @param pd
	 * @throws Exception
	 */

	public void editTransOrderSt(TransOrder transOrder) throws Exception {
		dao.update("TransOrderMapper.editTransOrderSt", transOrder);
	}
	
	/**
	 * App 提交提现申请
	 * @param transOrder
	 * @return
	 * @throws Exception
	 */
	public TxnResp doAppWithdrawCash(TransOrder transOrder) throws Exception{
		TxnResp txnResp=null;
		
		
		//查找用户的账户信息
		AccountInf accountInf=accountInfService.findByUserId(transOrder.getUserId());
		
		//保存订单信息
		transOrder.setPriAccId(accountInf.getAccId());
		this.save(transOrder);
		
		//判断账户余额
		if(Integer.parseInt(accountInf.getAccBal())<Integer.parseInt(transOrder.getTransAmt())){
			txnResp=new TxnResp();
			txnResp.setResultCode("99");	
		}
	
 		/***添加账户交易订单**/
	 	TransLog transLog=new TransLog();
	 	transLog.setDmsRelatedKey(transOrder.getOrderId());  //
	    transLog.setTransType(AccountTransType.TRANS_5000.getValue()); //提现
	    transLog.setPriAccId(accountInf.getAccId()); //用户的主账户ID
	    transLog.setTransSt("0");
	    transLog.setTransAmt(transOrder.getTransAmt());
	    transLog.setOrgTransAmt(transOrder.getOrgTransAmt()); //订单充值金额
	    transLog.setDmsUserId(transOrder.getUserId()); //当前操作的用户ID
	    transLog.setDmsUserUnionId(transOrder.getUserId()); //执行账户的USER_ID
	 	translogService.save(transLog);
		 	
	 	//账户提现操作
	 	txnResp=accountOperService.deductAccountInf(transLog.getTransId());
	  
		 if(txnResp ==null){
			 txnResp=new TxnResp();
			 txnResp.setResultCode("99");	
		  }
		  transLog.setTransSt("1");
		  transLog.setRespCode(txnResp.getResultCode());
		  translogService.editOrderLogResp(transLog); //交易明细
		
		if("00".equals(txnResp.getResultCode())){
			transOrder.setOrderSt("3"); //订单处理中
			this.editTransOrderSt(transOrder);
			txnResp.setOrderId(transOrder.getOrderId());
		}

		return txnResp;
	}
	
	
	/**用户 账户收支明细
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> findAccountOrderPage(Page page)throws Exception{
		return (List<PageData>)dao.findForList("TransOrderMapper.findAccountOrderlistPage", page);
	}
}

