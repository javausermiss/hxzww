package com.fh.service.system.appuser.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.system.appuser.AppUserCodeManager;
import com.fh.util.PageData;

/** 
 * 说明： USER_CODE
 * 创建人：FH Q313596790
 * 创建时间：2018-02-06
 * @version
 */
@Service("appUserCodeService")
public class AppUserCodeService implements AppUserCodeManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public int save(PageData pd)throws Exception{
		return (int)dao.save("AppUserCodeMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("AppUserCodeMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("AppUserCodeMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AppUserCodeMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AppUserCodeMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppUserCodeMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("AppUserCodeMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**根据code查询用户兑换码
	 * @param pd
	 * @throws Exception
	 */
	public PageData getUserCodeByCode(String code)throws Exception{
		return (PageData)dao.findForObject("AppUserCodeMapper.getUserCodeByCode", code);
	}
	
	/**根据userId查询用户兑换码
	 * @param pd
	 * @throws Exception
	 */
	public PageData getUserCodeByUserId(String userId)throws Exception{
		return (PageData)dao.findForObject("AppUserCodeMapper.getUserCodeByUserId", userId);
	}
}

