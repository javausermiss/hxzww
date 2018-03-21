package com.fh.service.system.payment.impl;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.Payment;
import com.fh.service.system.payment.PaymentManager;
import com.fh.util.PageData;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

@Service("paymentService")
public class PaymentService implements PaymentManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public int reg(Payment payment) throws Exception {
        return (int)dao.save("paymentMapper.reg",payment);
    }
    
    /**用户游戏收支明细
	 * @param page
	 * @throws Exception
	 */
    public List<PageData> findUserGameGoldDetaillist(Page page)throws Exception{
    	return (List<PageData>)dao.findForList("paymentMapper.findGoldDetaillistPage", page);
    }
    
	 /**
	  * 用户充值明细
	  * @param page
	  * @return
	  * @throws Exception
	  */
    public List<PageData> findRegDetaillistPage(Page page)throws Exception{
		 return (List<PageData>)dao.findForList("paymentMapper.findRegDetaillistPage", page);
	 }

	@Override
	public List<Payment> getPaymenlist(String userId) throws Exception {
		return (List<Payment>)dao.findForList("paymentMapper.getPaymenlist",userId);
	}
	
	 /**
	  * 获取用户的充值统计
	  * @param pd
	  * @return
	  * @throws Exception
	  */
	public List<PageData> getRechargeCount(PageData pd)throws Exception{
		 return (List<PageData>)dao.findForList("paymentMapper.getRechargeCount",pd);
	 }
	 
	 
	 /**
	  * 获取单日留存用户
	  * @param pd
	  * @return
	  * @throws Exception
	  */
	public List<PageData> getRemainCount(PageData pd)throws Exception{
		 return (List<PageData>)dao.findForList("paymentMapper.getRemainCount",pd);
	 }

	/**
	  * 获取充值总额
	  * @param pd
	  * @return
	  * @throws Exception
	  */
	@Override
	public List<PageData> getUserTotal(Page page) throws Exception {
		return (List<PageData>) dao.findForList("paymentMapper.getUserTotal",page);
	}

	/**
	  * 获取日充值用户
	  * @param page
	  * @return
	  * @throws Exception
	  */
	@Override
	public List<PageData> findRegTotallistPage(Page page) throws Exception {
		 return (List<PageData>)dao.findForList("paymentMapper.findRegTotallistPage", page);
	}
	/**
	  * 获取月充值用户
	  * @param page
	  * @return
	  * @throws Exception
	  */
	@Override
	public List<PageData> findRegTotallistMonthPage(Page page) throws Exception {
		 return (List<PageData>)dao.findForList("paymentMapper.findRegTotalMonthlistPage", page);
	}
}
