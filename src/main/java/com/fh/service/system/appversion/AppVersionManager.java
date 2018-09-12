package com.fh.service.system.appversion;

import com.fh.entity.Page;
import com.fh.entity.system.AppVersion;
import com.fh.util.PageData;

import java.util.List;

/** 
 * 说明： app更新模块接口
 * 创建人：FH Q313596790
 * 创建时间：2018-04-10
 * @version
 */
public interface AppVersionManager{

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

	/**
	 * 查询最新可用的版本
	 * @return
	 * @throws Exception
	 */

	public AppVersion getNewVersion()throws Exception;
	
	
	public AppVersion getVersionByID(String id) throws Exception;
	/**
	 * 通过版本获取数据
	 * @return
	 * @throws Exception
	 */
	public AppVersion findByVersion(String version)throws Exception;
	
	
	public AppVersion versionDisplay()throws Exception;
}

