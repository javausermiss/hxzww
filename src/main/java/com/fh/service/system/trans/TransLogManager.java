package com.fh.service.system.trans;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.system.TransLog;
import com.fh.util.PageData;

/** 
 * 说明： 订单表接口
 * 创建人：FH Q313596790
 * 创建时间：2017-12-26
 * @version
 */
public interface TransLogManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(TransLog transLog)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(TransLog transLog)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(TransLog transLog)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(TransLog transLog)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public TransLog findById(String id)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;
	
	/**修订单返回状态
	 * @param pd
	 * @throws Exception
	 */
	public void editOrderLogResp(TransLog transLog)throws Exception;
	
}

