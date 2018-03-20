package com.fh.service.system.trans;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.system.AccountLog;
import com.fh.util.PageData;

/** 
 * 说明： 订单表接口
 * 创建人：FH Q313596790
 * 创建时间：2017-12-26
 * @version
 */
public interface AccountLogManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(AccountLog accountLog)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(AccountLog accountLog)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(AccountLog accountLog)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(AccountLog accountLog)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public AccountLog findById(String id)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**获取用户的账户日志
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> getAccountLogByTypelistAll(PageData pd)throws Exception;
	
}

