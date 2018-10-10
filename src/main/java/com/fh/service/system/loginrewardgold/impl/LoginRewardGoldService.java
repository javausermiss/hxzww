package com.fh.service.system.loginrewardgold.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.LoginRewardGold;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.loginrewardgold.LoginRewardGoldManager;

/** 
 * 说明： 次日登录领取金币
 * 创建人：FH Q313596790
 * 创建时间：2018-09-26
 * @version
 */
@Service("loginrewardgoldService")
public class LoginRewardGoldService implements LoginRewardGoldManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("LoginRewardGoldMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("LoginRewardGoldMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("LoginRewardGoldMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("LoginRewardGoldMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("LoginRewardGoldMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("LoginRewardGoldMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("LoginRewardGoldMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public int regInfo(LoginRewardGold loginRewardGold) throws Exception {
		return (int)dao.save("LoginRewardGoldMapper.regInfo",loginRewardGold);
	}

	@Override
	public List<LoginRewardGold> getAllInfo(LoginRewardGold loginRewardGold) throws Exception {
		return (List<LoginRewardGold>)dao.findForList("LoginRewardGoldMapper.getAllInfo",loginRewardGold);
	}

	@Override
	public LoginRewardGold getInfo(LoginRewardGold loginRewardGold) throws Exception {
		return (LoginRewardGold)dao.findForObject("LoginRewardGoldMapper.getInfo",loginRewardGold);
	}

	@Override
	public int updateInfo(LoginRewardGold loginRewardGold) throws Exception {
		return (int)dao.update("LoginRewardGoldMapper.updateInfo",loginRewardGold);
	}
}

