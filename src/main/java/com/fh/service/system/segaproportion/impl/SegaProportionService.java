package com.fh.service.system.segaproportion.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.SegaProportion;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.segaproportion.SegaProportionManager;

/** 
 * 说明： 推币机比例
 * 创建人：FH Q313596790
 * 创建时间：2018-09-04
 * @version
 */
@Service("segaproportionService")
public class SegaProportionService implements SegaProportionManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("SegaProportionMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("SegaProportionMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("SegaProportionMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("SegaProportionMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("SegaProportionMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SegaProportionMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("SegaProportionMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public SegaProportion getInfoByRoomId(String roomId) throws Exception {
		return (SegaProportion)dao.findForObject("SegaProportionMapper.getInfoByRoomId",roomId);
	}
}

