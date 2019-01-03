package com.fh.service.system.appuser;

import java.util.List;

import com.fh.entity.Page;
import com.fh.util.PageData;

/** 
 * 说明： USER_CODE接口
 * 创建人：FH Q313596790
 * 创建时间：2018-02-06
 * @version
 */
public interface AppUserAwardListManager{

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
	 * 查找用户兑换码获取奖励得金币
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public PageData  findAwardCountByUserId(String userId)throws Exception;
	
	
	/**
	 * awarkPd 用户邀请码
	 * userId  当前提交邀请码的userId
	 * @throws Exception
	 */
	public void doAwardByUserCode(PageData awarkPd,String userId,String IMEI_ID,int awardNum,int invite_awardNum)throws Exception;
	
	/**
	 * 查询用户Id是否已经兑换
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public int findUserAwardByUserId(String userId)throws Exception;
	
	/**
	 * 查询用户APP是否已经兑换
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public int findUserAwardByAppId(String userId)throws Exception;
}

