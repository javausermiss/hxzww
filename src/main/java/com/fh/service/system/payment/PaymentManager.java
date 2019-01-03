package com.fh.service.system.payment;

import java.util.List;

import com.fh.entity.Page;
import com.fh.entity.system.Payment;
import com.fh.util.PageData;

public interface PaymentManager {

    public int reg(Payment payment)throws Exception;

    
    
	/**用户游戏收支明细
	 * @param page
	 * @throws Exception
	 */
	 List<PageData> findUserGameGoldDetaillist(Page page)throws Exception;
	 
	 /**
	  * 用户充值明细
	  * @param page
	  * @return
	  * @throws Exception
	  */
	 List<PageData> findRegDetaillistPage(Page page)throws Exception;

	/**
	 * 用户收支明细
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	 List<Payment> getPaymenlist (String userId)throws Exception;

	 
	 /**
	  * 获取用户的充值统计
	  * @param pd
	  * @return
	  * @throws Exception
	  */
	 List<PageData> getRechargeCount(PageData pd)throws Exception;
	 
	 
	 /**
	  * 获取单日留存用户
	  * @param pd
	  * @return
	  * @throws Exception
	  */
	 List<PageData> getRemainCount(PageData pd)throws Exception;
	 
	 //用户充值总额
	 List<PageData> getUserTotal(Page page)throws Exception;
	 
	 //日充值用户
	 List<PageData> findRegTotallistPage(Page page)throws Exception;
	 
	//月充值用户
	List<PageData> findRegTotallistMonthPage(Page page)throws Exception;

	List<PageData> getUserOrderList(Page page)throws Exception;
}
