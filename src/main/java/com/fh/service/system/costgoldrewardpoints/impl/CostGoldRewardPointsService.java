package com.fh.service.system.costgoldrewardpoints.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.CostGoldRewardPoints;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.costgoldrewardpoints.CostGoldRewardPointsManager;

/** 
 * 说明： 消费金币奖励积分模块
 * 创建人：FH Q313596790
 * 创建时间：2018-08-13
 * @version
 */
@Service("costgoldrewardpointsService")
public class CostGoldRewardPointsService implements CostGoldRewardPointsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("CostGoldRewardPointsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("CostGoldRewardPointsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("CostGoldRewardPointsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CostGoldRewardPointsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CostGoldRewardPointsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CostGoldRewardPointsMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CostGoldRewardPointsMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public CostGoldRewardPoints getInfo() throws Exception{
		return (CostGoldRewardPoints)dao.findForObject("CostGoldRewardPointsMapper.getInfo",null);
	}
}

