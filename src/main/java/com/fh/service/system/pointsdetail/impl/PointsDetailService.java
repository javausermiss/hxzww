package com.fh.service.system.pointsdetail.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.PointsDetail;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.pointsdetail.PointsDetailManager;

/** 
 * 说明： 积分记录模块
 * 创建人：FH Q313596790
 * 创建时间：2018-08-08
 * @version
 */
@Service("pointsdetailService")
public class PointsDetailService implements PointsDetailManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("PointsDetailMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PointsDetailMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PointsDetailMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PointsDetailMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("PointsDetailMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PointsDetailMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PointsDetailMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public int regPointsDetail(PointsDetail pointsDetail) throws Exception{
		return (int)dao.save("PointsDetailMapper.regPointsDetail",pointsDetail);
	}

	@Override
	public List<PointsDetail> getPointsDetail(String userId) throws Exception{
		return (List<PointsDetail>)dao.findForList("PointsDetailMapper.getPointsDetail",userId);
	}
}

