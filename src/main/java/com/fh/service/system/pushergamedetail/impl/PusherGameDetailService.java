package com.fh.service.system.pushergamedetail.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.PusherGameDetail;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.pushergamedetail.PusherGameDetailManager;

/** 
 * 说明： 推币机游戏的单场游戏记录(上机--->下机)
 * 创建人：FH Q313596790
 * 创建时间：2018-09-04
 * @version
 */
@Service("pushergamedetailService")
public class PusherGameDetailService implements PusherGameDetailManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("PusherGameDetailMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PusherGameDetailMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PusherGameDetailMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PusherGameDetailMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("PusherGameDetailMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PusherGameDetailMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PusherGameDetailMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public int insert(PusherGameDetail pusherGameDetail) throws Exception {
		return (int)dao.save("PusherGameDetailMapper.insert",pusherGameDetail);
	}

	@Override
	public PusherGameDetail getInfo(PusherGameDetail pusherGameDetail) throws Exception {
		return (PusherGameDetail)dao.findForObject("PusherGameDetailMapper.getInfo",pusherGameDetail);
	}

	@Override
	public int update(PusherGameDetail pusherGameDetail) throws Exception {
		return (int)dao.update("PusherGameDetailMapper.update",pusherGameDetail);
	}
}

