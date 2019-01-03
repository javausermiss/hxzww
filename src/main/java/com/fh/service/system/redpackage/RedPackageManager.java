package com.fh.service.system.redpackage;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.system.RedPackage;
import com.fh.util.PageData;

/** 
 * 说明： 红包系统接口
 * 创建人：FH Q313596790
 * 创建时间：2018-11-29
 * @version
 */
public interface RedPackageManager{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

	public int inset(RedPackage redPackage)throws Exception;

	public List<RedPackage> getRedPackageByGender(String gender)throws Exception;

	public RedPackage getRedPackageById(String id)throws Exception;

	public int updateInfo(RedPackage redPackage)throws Exception;

	public List<RedPackage> getRedPackageInfo(String id) throws Exception;


	
}

