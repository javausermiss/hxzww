package com.fh.service.system.userpoints.impl;

import java.text.DecimalFormat;
import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.costgoldrewardpoints.CostGoldRewardPointsManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.util.Const;
import com.fh.util.wwjUtil.MyUUID;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.userpoints.UserPointsManager;

/** 
 * 说明： 用户任务完成模块
 * 创建人：FH Q313596790
 * 创建时间：2018-07-31
 * @version
 */
@Service("userpointsService")
public class UserPointsService implements UserPointsManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Resource(name = "appuserService")
	private AppuserManager appuserService;
	@Resource(name = "paymentService")
	private PaymentManager paymentService;
	@Resource(name="costgoldrewardpointsService")
	private CostGoldRewardPointsManager costgoldrewardpointsService;
	@Resource(name="pointsdetailService")
	private PointsDetailManager pointsdetailService;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("UserPointsMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("UserPointsMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("UserPointsMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("UserPointsMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("UserPointsMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserPointsMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("UserPointsMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public PageData getCostGoldSum(PageData pageData) throws Exception{
		return (PageData)dao.findForObject("UserPointsMapper.getCostGoldSum",pageData);
	}

	@Override
	public PageData getCostGoldSumAll(PageData pageData) throws Exception{
		return (PageData)dao.findForObject("UserPointsMapper.getCostGoldSumAll",pageData);
	}

	@Override
	public UserPoints getUserPointsFinish(String userid) throws Exception{
		return (UserPoints)dao.findForObject("UserPointsMapper.getUserPointsFinish",userid);
	}

	@Override
	public int regUserInfo(UserPoints userPoints) throws Exception{
		return (int)dao.save("UserPointsMapper.regUserInfo",userPoints);
	}

	@Override
	public int updateUserPoints(UserPoints userPoints) throws Exception{
		return (int)dao.update("UserPointsMapper.updateUserPoints",userPoints);
	}

	@Override
	public List<UserPoints> getAllUserPointsDetail() throws Exception{
		return (List<UserPoints>)dao.findForList("UserPointsMapper.getAllUserPointsDetail",null);
	}

	@Override
	public String doGoldReward(String r_tag, Integer goldValue, Integer sum, Integer ob, List<PointsReward> list, Integer now_points, Integer nb , AppUser appUser) throws Exception {
		String new_r_tag = "";
		String tag = "0";
		while (r_tag.equals("0")) {
			PointsReward pointsReward = list.get(list.size() - 1);
			Integer pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 0; i < list.size(); i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "5";
			}
			if (tag.equals("1")){
				break;
			}

			pointsReward = list.get(list.size() - 2);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 0; i < list.size() - 1; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "4";
			}
			if (tag.equals("1")){
				break;
			}

			pointsReward = list.get(list.size() - 3);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 0; i < list.size() - 2; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "3";
			}
			if (tag.equals("1")){
				break;
			}

			pointsReward = list.get(list.size() - 4);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 0; i < list.size() - 3; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "2";
			}
			if (tag.equals("1")){
				break;
			}

			pointsReward = list.get(list.size() - 5);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 0; i < list.size() - 4; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "1";
			}
			if (tag.equals("1")){
				break;
			}
		}

		while (r_tag.equals("1")) {
			PointsReward pointsReward = list.get(list.size() - 1);
			Integer pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 1; i < list.size(); i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "5";
			}
			if (tag.equals("1")){
				break;
			}
			pointsReward = list.get(list.size() - 2);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 1; i < list.size() - 1; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "4";
			}
			if (tag.equals("1")){
				break;
			}

			pointsReward = list.get(list.size() - 3);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 1; i < list.size() - 2; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "3";
			}
			if (tag.equals("1")){
				break;
			}

			pointsReward = list.get(list.size() - 4);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 1; i < list.size() - 3; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "2";
			}
			if (tag.equals("1")){
				break;
			}

		}
		while (r_tag.equals("2")) {
			PointsReward pointsReward = list.get(list.size() - 1);
			Integer pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 2; i < list.size(); i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "5";
			}
			if (tag.equals("1")){
				break;
			}
			pointsReward = list.get(list.size() - 2);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 2; i < list.size() - 1; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "4";
			}
			if (tag.equals("1")){
				break;
			}
			pointsReward = list.get(list.size() - 3);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 2; i < list.size() - 2; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "3";
			}
			if (tag.equals("1")){
				break;
			}
		}
		while (r_tag.equals("3")) {

			PointsReward pointsReward = list.get(list.size() - 1);
			Integer pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 3; i < list.size(); i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "5";
			}
			if (tag.equals("1")){
				break;
			}
			pointsReward = list.get(list.size() - 2);
			pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 3; i < list.size() - 1; i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				tag = "1";
				nb = sum + ob;
				new_r_tag = "4";
			}
			if (tag.equals("1")){
				break;
			}

		}
		if (r_tag.equals("4")) {
			PointsReward pointsReward = list.get(list.size() - 1);
			Integer pr = pointsReward.getPointsValue();
			if (now_points >= pr) {
				for (int i = 4; i < list.size(); i++) {
					PointsReward pointsR = list.get(i);
					goldValue = pointsR.getGoldValue();
					sum += goldValue;
				}
				nb = sum + ob;
				new_r_tag = "5";
			}
		}
		//金币增加记录
		//更新收支表
		if (sum!=0){
			Payment payment = new Payment();
			payment.setGOLD("+" + sum);
			payment.setUSERID(appUser.getUSER_ID());
			payment.setDOLLID(null);
			payment.setCOST_TYPE(Const.PlayMentCostType.cost_type25.getValue());
			payment.setREMARK(Const.PlayMentCostType.cost_type25.getName());
			paymentService.reg(payment);
		}
		appUser.setBALANCE(String.valueOf(nb));
		appuserService.updateAppUserBalanceById(appUser);
		return new_r_tag;

	}

	@Override
	public void doCostRewardPoints(AppUser appUser, String userId) throws Exception {
		//消费金币奖励积分
		PageData pageData = new PageData();
		pageData.put("userId",userId);
		PageData pageData1 =  this.getCostGoldSumAll(pageData);
		String gm = "0";
		if (pageData1 != null) {
			double aa = (double) pageData1.get("godsum");
			gm = new DecimalFormat("0").format(aa).substring(1);
		}
		int nm = 0 ;
		int regPoints  = 0;
		CostGoldRewardPoints costGoldRewardPoints = costgoldrewardpointsService.getInfo();
		Integer gold =  costGoldRewardPoints.getGOLD_VALUE();
		Integer points =  costGoldRewardPoints.getPOINTS_VALUE();
		nm =  Integer.valueOf(gm) /gold;
		Integer pm =  appUser.getPOINTS_MULTIPLES();
		if (nm>pm){
			regPoints = (nm-pm)*points;
			//给用户增加总积分
			appUser.setPOINTS_MULTIPLES(nm);
			appUser.setPOINTS(appUser.getPOINTS()+regPoints);
			appuserService.updateAppUserBalanceById(appUser);
			//给用户增加当日积分
			UserPoints up =  this.getUserPointsFinish(userId);
			up.setTodayPoints(up.getTodayPoints()+regPoints);
			this.updateUserPoints(up);
			//增加积分记录

			PointsDetail pointsDetail_cgs = new PointsDetail();
			pointsDetail_cgs.setUserId(userId);
			pointsDetail_cgs.setChannel(Const.pointsMallType.points_type08.getName());
			pointsDetail_cgs.setType("+");
			pointsDetail_cgs.setPointsDetail_Id(MyUUID.getUUID32());
			pointsDetail_cgs.setPointsValue(regPoints);
			pointsdetailService.regPointsDetail(pointsDetail_cgs);
		}

	}
}

