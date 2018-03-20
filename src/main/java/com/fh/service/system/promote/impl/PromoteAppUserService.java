package com.fh.service.system.promote.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.system.promote.PromoteAppUserManager;
import com.fh.util.PageData;

/** 
 * 说明： 推广加盟
 * 创建人：FH Q313596790
 * 创建时间：2018-03-07
 * @version
 */
@Service("promoteAppUserService")
public class PromoteAppUserService implements PromoteAppUserManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("PromoteAppUserMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PromoteAppUserMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PromoteAppUserMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PromoteAppUserMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("PromoteAppUserMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PromoteAppUserMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PromoteAppUserMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**通过userid获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByUserId(String userId)throws Exception{
		return (PageData)dao.findForObject("PromoteAppUserMapper.findByUserId", userId);
	}

	/**
	 * 根据推广码查询
	 */
	public PageData findByProCode(String proCode) throws Exception {
		return (PageData)dao.findForObject("PromoteAppUserMapper.findByProCode", proCode);
	}
}

