package com.fh.service.system.userpoints;

import java.util.List;
import java.util.Map;

import com.fh.entity.Page;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.PointsDetail;
import com.fh.entity.system.PointsReward;
import com.fh.entity.system.UserPoints;
import com.fh.util.PageData;

import javax.persistence.criteria.CriteriaBuilder;

/** 
 * 说明： 用户任务完成模块接口
 * 创建人：FH Q313596790
 * 创建时间：2018-07-31
 * @version
 */
public interface UserPointsManager{

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
	 * 查询当日消费的金币数
	 * @param pageData
	 * @return
	 * @throws Exception
	 */
	public PageData getCostGoldSum(PageData pageData)throws Exception;

	/**
	 * 查询总消费金币数
	 * @param pageData
	 * @return
	 * @throws Exception
	 */
	public PageData getCostGoldSumAll(PageData pageData)throws Exception;

	/**
	 * 查询任务完成情况
	 * @return
	 * @throws Exception
	 */
	public UserPoints getUserPointsFinish(String userid)throws Exception;

	public int regUserInfo(UserPoints userPoints)throws Exception;

	public int updateUserPoints(UserPoints userPoints)throws Exception;

	public List<UserPoints> getAllUserPointsDetail()throws Exception;

	public String doGoldReward(String r_tag, Integer goldValue, Integer sum , Integer ob, List<PointsReward> list, Integer now_points, Integer nb, AppUser appUser)throws Exception;

	public void doCostRewardPoints(AppUser appUser ,String userId)throws Exception;


	public Map<String,Object> doGoldRewardForPusher(String r_tag, Integer goldValue, Integer sum , Integer ob, List<PointsReward> list, Integer now_points, Integer nb, AppUser appUser)throws Exception;

	public Map<String,Object> doCostRewardPointsForPusher(String r_tag,Integer todayPoints,Integer points ,Integer newBalance, String userId ,AppUser appUser)throws Exception;
	
}

