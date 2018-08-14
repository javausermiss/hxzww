package com.fh.service.system.pointssendgoods.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.PointsSendGoods;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.pointssendgoods.PointsSendGoodsManager;

/** 
 * 说明： 积分兑换礼品发货模块
 * 创建人：FH Q313596790
 * 创建时间：2018-08-08
 * @version
 */
@Service("pointssendgoodsService")
public class PointsSendGoodsService implements PointsSendGoodsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("PointsSendGoodsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PointsSendGoodsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PointsSendGoodsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PointsSendGoodsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("PointsSendGoodsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PointsSendGoodsMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PointsSendGoodsMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public int regPointsSendGoods(PointsSendGoods pointsSendGoods)throws Exception {
		return (int)dao.save("PointsSendGoodsMapper.regPointsSendGoods",pointsSendGoods);
	}

	@Override
	public List<PointsSendGoods> getPointsGoodsForUser(String userId)throws Exception {
		return (List<PointsSendGoods>)dao.findForList("PointsSendGoodsMapper.getPointsGoodsForUser",userId);
	}
}

