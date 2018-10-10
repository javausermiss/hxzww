package com.fh.service.system.rewardgoldmanager.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.rewardgoldmanager.RewardGoldManagerManager;

/** 
 * 说明： 动态管理次日赠送金币数
 * 创建人：FH Q313596790
 * 创建时间：2018-09-29
 * @version
 */
@Service("rewardgoldmanagerService")
public class RewardGoldManagerService implements RewardGoldManagerManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("RewardGoldManagerMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("RewardGoldManagerMapper.delete", pd);
	}


	@Override
	public void updateSupportNum(PageData pd) throws Exception {
		dao.update("RewardGoldManagerMapper.updateSupportNum", pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("RewardGoldManagerMapper.edit", pd);
	}

	@Override
	public void editGold(PageData pd) throws Exception {
		dao.update("RewardGoldManagerMapper.editGold", pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("RewardGoldManagerMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("RewardGoldManagerMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("RewardGoldManagerMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("RewardGoldManagerMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

