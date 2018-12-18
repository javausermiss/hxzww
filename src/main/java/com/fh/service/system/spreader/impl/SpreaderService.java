package com.fh.service.system.spreader.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.Spreader;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.spreader.SpreaderManager;

/** 
 * 说明： 推广者提款记录
 * 创建人：FH Q313596790
 * 创建时间：2018-11-19
 * @version
 */
@Service("spreaderService")
public class SpreaderService implements SpreaderManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("SpreaderMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("SpreaderMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("SpreaderMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("SpreaderMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("SpreaderMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("SpreaderMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("SpreaderMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public int regInfo(Spreader spreader) throws Exception {
		return (int)dao.save("SpreaderMapper.regInfo",spreader);
	}

	@Override
	public List<Spreader> listS(String userId) throws Exception {
		return (List<Spreader>)dao.findForList("SpreaderMapper.listS",userId);
	}

	@Override
	public List<PageData> list_time(PageData pageData) throws Exception {
		return (List<PageData>)dao.findForList("SpreaderMapper.list_time",pageData);
	}

	@Override
	public PageData list_time_money(PageData pageData) throws Exception{
		return (PageData)dao.findForObject("SpreaderMapper.list_time_money",pageData);
	}
}

