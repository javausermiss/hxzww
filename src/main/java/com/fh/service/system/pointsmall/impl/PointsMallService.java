package com.fh.service.system.pointsmall.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.PointsMall;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.pointsmall.PointsMallManager;

/** 
 * 说明： 任务商城任务
 * 创建人：FH Q313596790
 * 创建时间：2018-07-31
 * @version
 */
@Service("pointsmallService")
public class PointsMallService implements PointsMallManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("PointsMallMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("PointsMallMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("PointsMallMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("PointsMallMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("PointsMallMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("PointsMallMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("PointsMallMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public List<PointsMall> getInfo()throws Exception {
		return  (List<PointsMall>)dao.findForList("PointsMallMapper.getInfo",null);
	}

	@Override
	public PointsMall getInfoById(Integer id) throws Exception{
		return (PointsMall)dao.findForObject("PointsMallMapper.getInfoById",id);
	}
}

