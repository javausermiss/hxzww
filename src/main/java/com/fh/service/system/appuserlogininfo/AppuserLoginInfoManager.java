package com.fh.service.system.appuserlogininfo;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.system.AppuserLogin;
import com.fh.util.PageData;

/** 
 * 说明： 用户登录接口
 * 创建人：FH Q313596790
 * 创建时间：2018-01-09
 * @version
 */
public interface AppuserLoginInfoManager{

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


	public int insertLoginLog(AppuserLogin appuserLogin)throws Exception;


	public List<PageData> LandingStatelist(Page page)throws Exception;
	
	/**退出登陆
	 * @throws Exception
	 */
	public void editAppUserLoginLog(String userId)throws Exception;
	
	/**更新30分钟前的登陆状态
	 * @throws Exception
	 */
	public void changestate(PageData pd)throws Exception;
}

