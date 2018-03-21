package com.fh.service.system.trans;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.system.TransOrder;
import com.fh.util.PageData;
import com.fh.util.resp.TxnResp;

/** 
 * 说明： 订单表接口
 * 创建人：FH Q313596790
 * 创建时间：2017-12-26
 * @version
 */
public interface TransOrderManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(TransOrder transOrder)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(String orderId)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(TransOrder transOrder)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(TransOrder transOrder)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public TransOrder findById(String id)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**修订单状态
	 * @param pd
	 * @throws Exception
	 */
	public void editTransOrderSt(TransOrder transOrder)throws Exception;
	
	/**
	 * App 提交提现申请
	 * @param transOrder
	 * @return
	 * @throws Exception
	 */
	public TxnResp doAppWithdrawCash(TransOrder transOrder) throws Exception;
	

	/**
	 * 用户 账户收支明细
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> findAccountOrderPage(Page page)throws Exception;
}

