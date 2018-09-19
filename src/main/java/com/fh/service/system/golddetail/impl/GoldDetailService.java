package com.fh.service.system.golddetail.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.GoldDetail;
import com.fh.entity.system.PointsDetail;
import com.fh.service.system.golddetail.GoldDetailManager;
import com.fh.util.PageData;

/** 
 * 说明： 金币记录模块
 * 创建人：FH Q313596790
 * 创建时间：2018-09-17
 * @version
 */
@Service("golddetailService")
public class GoldDetailService implements GoldDetailManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("GoldDetailMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("GoldDetailMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("GoldDetailMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("GoldDetailMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("GoldDetailMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("GoldDetailMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("GoldDetailMapper.deleteAll", ArrayDATA_IDS);
	}
	
	@Override
	public int regGoldDetail(GoldDetail goldDetail) throws Exception{
		return (int)dao.save("GoldDetailMapper.regGoldDetail",goldDetail);
	}
	
	
	@Override
	public List<GoldDetail> getGoldDetail(String userId) throws Exception{
		return (List<GoldDetail>)dao.findForList("GoldDetailMapper.getGoldDetail",userId);
	}
}

