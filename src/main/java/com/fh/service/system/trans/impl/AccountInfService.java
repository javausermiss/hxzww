package com.fh.service.system.trans.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.AccountInf;
import com.fh.service.system.trans.AccountInfManager;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.StringUtils;

/** 
 * 说明： 账户信息
 * 创建人：FH Q313596790
 * 创建时间：2017-12-26
 * @version
 */
@Service("accountInfService")
public class AccountInfService implements AccountInfManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(AccountInf pd)throws Exception{
		pd.setLastTxnDate(DateUtil.getDays());
		pd.setLastTxnTime(DateUtil.getTimeHHmmss());
		dao.save("AccountInfMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(AccountInf pd)throws Exception{
		dao.delete("AccountInfMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(AccountInf pd)throws Exception{
		pd.setLastTxnDate(DateUtil.getDays());
		pd.setLastTxnTime(DateUtil.getTimeHHmmss());
		dao.update("AccountInfMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AccountInfMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(AccountInf pd)throws Exception{
		return (List<PageData>)dao.findForList("AccountInfMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public AccountInf findById(String Id)throws Exception{
		return (AccountInf)dao.findForObject("AccountInfMapper.findById", Id);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("AccountInfMapper.deleteAll", ArrayDATA_IDS);
	}
	
	
	/**通过userId获取数据
	 * @param pd
	 * @throws Exception
	 */
	public AccountInf findByUserId(String userId)throws Exception{
		return (AccountInf)dao.findForObject("AccountInfMapper.findByUserId", userId);
	}
	
	/***
	 * 查询用户的账户余额
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public String getAccountCountByUserId(String userId)throws Exception{
		String accBal= (String)dao.findForObject("AccountInfMapper.getAccountCountByUserId", userId);
		if(StringUtils.isEmpty(accBal)){
			accBal="0"; 
		}
		return accBal;
	}
}

