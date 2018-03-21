package com.fh.service.system.trans.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.TransLog;
import com.fh.service.system.trans.TransLogManager;
import com.fh.util.PageData;

/** 
 * 说明： 订单表
 * 创建人：FH Q313596790
 * 创建时间：2017-12-26
 * @version
 */
@Service("translogService")
public class TransLogService implements TransLogManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(TransLog pd)throws Exception{
		dao.save("TransLogMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(TransLog pd)throws Exception{
		dao.delete("TransLogMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(TransLog pd)throws Exception{
		dao.update("TransLogMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("TransLogMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(TransLog pd)throws Exception{
		return (List<PageData>)dao.findForList("TransLogMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public TransLog findById(String Id)throws Exception{
		return (TransLog)dao.findForObject("TransLogMapper.findById", Id);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("TransLogMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**修订单返回状态
	 * @param pd
	 * @throws Exception
	 */
	public void editOrderLogResp(TransLog transLog)throws Exception{
		dao.update("TransLogMapper.editOrderLogResp", transLog);
	}
}

