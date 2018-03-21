package com.fh.service.system.trans;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.system.AccountInf;
import com.fh.util.PageData;

/** 
 * 说明： 订单表接口
 * 创建人：FH Q313596790
 * 创建时间：2017-12-26
 * @version
 */
public interface AccountInfManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(AccountInf accountInf)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(AccountInf accountInf)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(AccountInf accountInf)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(AccountInf accountInf)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public AccountInf findById(String id)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	
	
	/**通过userId获取数据
	 * @param pd
	 * @throws Exception
	 */
	public AccountInf findByUserId(String userId)throws Exception;
	
	/***
	 * 查询用户的账户余额
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getAccountCountByUserId(String userId)throws Exception;
}

