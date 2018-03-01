package com.fh.service.system.ordertest;

import java.util.List;

import com.fh.entity.Page;
import com.fh.entity.system.Order;
import com.fh.util.PageData;

public interface OrderManager {
    /**
     * 创建订单记录
     * @param order
     * @return
     * @throws Exception
     */
    public int regmount(Order order)throws Exception;

    /**
     * 修改支付状态
     * @param order
     * @return
     * @throws Exception
     */
    public int update(Order order)throws Exception;

    /**
     * 通过ID查询订单信息
     * @param id
     * @return
     * @throws Exception
     */
    public Order getOrderById(String id) throws Exception;
    
    
	/**用户充值列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> getUserRegDetailList(Page page)throws Exception;
	
	
    /**
     * 查询渠道的订单统计
     * @param channelCode
     * @return
     * @throws Exception
     */
    public PageData getOrderTotalByChannelCode(String channelCode) throws Exception;
}
