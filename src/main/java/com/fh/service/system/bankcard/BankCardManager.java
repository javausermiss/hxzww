package com.fh.service.system.bankcard;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.system.BankCard;
import com.fh.util.PageData;

/** 
 * 说明： 银行卡信息接口
 * 创建人：FH Q313596790
 * 创建时间：2018-03-13
 * @version
 */
public interface BankCardManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	/**
	 * 添加用户银行卡信息
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	public int regBankInf(BankCard bankCard)throws Exception;

	/**
	 * 通过用户ID查询用户的银行卡信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public BankCard getBankInfByUserId(String userId)throws Exception;

	/**
	 * 通过用户ID修改用户的银行卡信息
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	public int updateBankInfByUserId(BankCard bankCard)throws Exception;
	
	/**
	 * APP通过用户ID修改用户的银行卡信息
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	public BankCard getBankInfForAppByUserId(String userId)throws Exception;

	
}

