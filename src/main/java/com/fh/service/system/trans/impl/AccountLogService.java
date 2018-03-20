package com.fh.service.system.trans.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.AccountLog;
import com.fh.service.system.trans.AccountLogManager;
import com.fh.util.PageData;

/** 
 * 说明： 账户明细
 * 创建人：FH Q313596790
 * 创建时间：2017-12-26
 * @version
 */
@Service("accountLogService")
public class AccountLogService implements AccountLogManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(AccountLog pd)throws Exception{
		dao.save("AccountLogMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(AccountLog pd)throws Exception{
		dao.delete("AccountLogMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(AccountLog pd)throws Exception{
		dao.update("AccountLogMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AccountLogMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(AccountLog pd)throws Exception{
		return (List<PageData>)dao.findForList("AccountLogMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public AccountLog findById(String Id)throws Exception{
		return (AccountLog)dao.findForObject("AccountLogMapper.findById", Id);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("AccountLogMapper.deleteAll", ArrayDATA_IDS);
	}
	
	
	/**获取用户的账户日志
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getAccountLogByTypelistAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AccountLogMapper.getAccountLogByTypelistAll", pd);
	}
}

