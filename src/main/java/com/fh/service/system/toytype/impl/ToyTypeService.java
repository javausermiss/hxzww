package com.fh.service.system.toytype.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.ToyType;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.toytype.ToyTypeManager;

/** 
 * 说明： 分类
 * 创建人：FH Q313596790
 * 创建时间：2018-01-23
 * @version
 */
@Service("toytypeService")
public class ToyTypeService implements ToyTypeManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ToyTypeMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ToyTypeMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ToyTypeMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ToyTypeMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ToyTypeMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(String id)throws Exception{
		return (PageData)dao.findForObject("ToyTypeMapper.findById", id);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ToyTypeMapper.deleteAll", ArrayDATA_IDS);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ToyType> getAllToyType() throws Exception {
		return (List<ToyType>)dao.findForList("ToyTypeMapper.getAllToyType", null);
	}

	@Override
	public ToyType getLastToyTypeId() throws Exception {
		return (ToyType)dao.findForObject("ToyTypeMapper.getLastToyTypeId",null);
	}
}

