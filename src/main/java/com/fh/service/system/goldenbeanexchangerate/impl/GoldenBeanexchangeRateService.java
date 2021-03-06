package com.fh.service.system.goldenbeanexchangerate.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.BeanRate;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.goldenbeanexchangerate.GoldenBeanexchangeRateManager;

/** 
 * 说明： 金币金豆兑换比例
 * 创建人：FH Q313596790
 * 创建时间：2018-08-22
 * @version
 */
@Service("goldenbeanexchangerateService")
public class GoldenBeanexchangeRateService implements GoldenBeanexchangeRateManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("GoldenBeanexchangeRateMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("GoldenBeanexchangeRateMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("GoldenBeanexchangeRateMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("GoldenBeanexchangeRateMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("GoldenBeanexchangeRateMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("GoldenBeanexchangeRateMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("GoldenBeanexchangeRateMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public BeanRate getBeanRate()throws Exception {
		return (BeanRate)dao.findForObject("GoldenBeanexchangeRateMapper.getBeanRate",null);
	}
}

