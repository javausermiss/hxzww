package com.fh.service.system.appuserlogininfo.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.AppuserLogin;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.appuserlogininfo.AppuserLoginInfoManager;

/** 
 * 说明： 用户登录
 * 创建人：FH Q313596790
 * 创建时间：2018-01-09
 * @version
 */
@Service("appuserlogininfoService")
public class AppuserLoginInfoService implements AppuserLoginInfoManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("AppuserLoginInfoMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("AppuserLoginInfoMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("AppuserLoginInfoMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AppuserLoginInfoMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("AppuserLoginInfoMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserLoginInfoMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("AppuserLoginInfoMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public int insertLoginLog(AppuserLogin appuserLogin) throws Exception {
		return (int)dao.save("AppuserLoginInfoMapper.insertLoginLog",appuserLogin);
	}
	
	public List<PageData> LandingStatelist(Page page)throws Exception{
		return (List<PageData>)dao.findForList("AppuserLoginInfoMapper.landingstatelistPage", page);
	}
	
	/**退出登陆
	 * @throws Exception
	 */
	public void editAppUserLoginLog(String userId)throws Exception{
		dao.update("AppuserLoginInfoMapper.editAppUserLoginLog", userId);
	}
	/**更新30分钟前的登陆状态
	 * @throws Exception
	 */
	public void changestate(PageData pd)throws Exception{
		dao.update("AppuserLoginInfoMapper.changestate", pd);
	}
}

