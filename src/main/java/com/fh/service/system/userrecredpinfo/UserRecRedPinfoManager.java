package com.fh.service.system.userrecredpinfo;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.system.User;
import com.fh.entity.system.UserRecRedPInfo;
import com.fh.util.PageData;

/** 
 * 说明： 用户领取红包记录接口
 * 创建人：FH Q313596790
 * 创建时间：2018-12-05
 * @version
 */
public interface UserRecRedPinfoManager {

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


	public int reg(UserRecRedPInfo ui)throws Exception;


	public UserRecRedPInfo find(UserRecRedPInfo userRecRedPInfo)throws Exception;
	/**
	 *
	 * @param userId
	 * @param redUserId
	 * @param redId
	 * @return
	 */
	public  void doGetRedPackage(String num ,String userId,String redUserId,String redId )throws Exception;
	
}

