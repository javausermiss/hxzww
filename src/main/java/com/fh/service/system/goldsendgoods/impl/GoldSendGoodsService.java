package com.fh.service.system.goldsendgoods.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.GoldSendGoods;
import com.fh.entity.system.PointsSendGoods;
import com.fh.util.PageData;
import com.fh.service.system.goldsendgoods.GoldSendGoodsManager;

/** 
 * 说明： 金币兑换礼品发货模块
 * 创建人：FH Q313596790
 * 创建时间：2018-09-18
 * @version
 */
@Service("goldsendgoodsService")
public class GoldSendGoodsService implements GoldSendGoodsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("GoldSendGoodsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("GoldSendGoodsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("GoldSendGoodsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("GoldSendGoodsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("GoldSendGoodsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("GoldSendGoodsMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("GoldSendGoodsMapper.deleteAll", ArrayDATA_IDS);
	}
	
	@Override
	public int regGoldSendGoods(GoldSendGoods goldSendGoods)throws Exception {
		return (int)dao.save("GoldSendGoodsMapper.regGoldSendGoods",goldSendGoods);
	}

	@Override
	public List<GoldSendGoods> getGoldGoodsForUser(String userId)throws Exception {
		return (List<GoldSendGoods>)dao.findForList("GoldSendGoodsMapper.getGoldGoodsForUser",userId);
	}
}

