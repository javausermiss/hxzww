package com.fh.service.system.redpackage.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.RedPackage;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.redpackage.RedPackageManager;

/** 
 * 说明： 红包系统
 * 创建人：FH Q313596790
 * 创建时间：2018-11-29
 * @version
 */
@Service("redpackageService")
public class RedPackageService implements RedPackageManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("RedPackageMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("RedPackageMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("RedPackageMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("RedPackageMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("RedPackageMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("RedPackageMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("RedPackageMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public int inset(RedPackage redPackage)throws Exception {
		return (int)dao.save("RedPackageMapper.inset",redPackage);
	}

	@Override
	public List<RedPackage> getRedPackageByGender(String gender)throws Exception {
		return (List<RedPackage>)dao.findForList("RedPackageMapper.getRedPackageByGender",gender);
	}

	@Override
	public RedPackage getRedPackageById(String id) throws Exception {
		return (RedPackage)dao.findForObject("RedPackageMapper.getRedPackageById",id);
	}

	@Override
	public int updateInfo(RedPackage redPackage) throws Exception {
		return (int)dao.update("RedPackageMapper.updateInfo",redPackage);
	}

	@Override
	public List<RedPackage> getRedPackageInfo(String id) throws Exception {
		return (List<RedPackage>)dao.findForList("RedPackageMapper.getRedPackageInfo",id);
	}
}

