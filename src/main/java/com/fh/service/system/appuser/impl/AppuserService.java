package com.fh.service.system.appuser.impl;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.appuserlogininfo.AppuserLoginInfoManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.service.system.pointsmall.PointsMallManager;
import com.fh.service.system.pointsreward.PointsRewardManager;
import com.fh.service.system.userpoints.UserPointsManager;
import com.fh.util.*;
import com.fh.util.wwjUtil.FaceImageUtil;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RedisUtil;
import com.fh.util.wwjUtil.RespStatus;
import com.iot.game.pooh.admin.srs.core.entity.httpback.SrsConnectModel;
import com.iot.game.pooh.admin.srs.core.util.SrsConstants;
import com.iot.game.pooh.admin.srs.core.util.SrsSignUtil;
import net.sf.json.JSONObject;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**类名称：AppuserService
 * @author FH Q313596790
 * 修改时间：2015年11月6日
 */
@Service("appuserService")
public class AppuserService implements AppuserManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	
    @Resource(name = "paymentService")
    private PaymentManager paymentService;

	@Resource(name = "appuserService")
	private AppuserManager appuserService;

	@Resource(name = "dollService")
	private DollManager dollService;

	@Resource(name = "appuserlogininfoService")
	private AppuserLoginInfoManager appuserlogininfoService;

	@Resource(name="userpointsService")
	private UserPointsManager userpointsService;

	@Resource(name="pointsmallService")
	private PointsMallManager pointsmallService;

	@Resource(name="pointsdetailService")
	private PointsDetailManager pointsdetailService;

	@Resource(name = "pointsrewardService")
	private PointsRewardManager pointsrewardService;
	
	/**列出某角色下的所有会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllAppuserByRorlid(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("AppuserMapper.listAllAppuserByRorlid", pd);
	}
	
	/**会员列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listPdPageUser(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.userlistPage", page);
	}
	
	/**通过用户名获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByUsername", pd);
	}
	
	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByEmail(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByEmail", pd);
	}
	
	/**通过编号获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByNumber(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByNumber", pd);
	}
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(PageData pd)throws Exception{
		dao.save("AppuserMapper.saveU", pd);
	}
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception{
		dao.delete("AppuserMapper.deleteU", pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception{
		dao.update("AppuserMapper.editU", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUiId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByUiId", pd);
	}
	
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllUser(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.listAllUser", pd);
	}
	
	/**批量删除用户
	 * @param USER_IDS
	 * @throws Exception
	 */
	public void deleteAllU(String[] USER_IDS)throws Exception{
		dao.delete("AppuserMapper.deleteAllU", USER_IDS);
	}
	
	/**获取总数
	 * @param value
	 * @throws Exception
	 */
	public PageData getAppUserCount(String value)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.getAppUserCount", value);
	}

	/**
	 * 注册用户
	 * @param phone
	 * @return
	 * @throws Exception
	 */

	public int reg(String phone,String url) throws Exception {
		return (int)dao.save("AppuserMapper.reg",new AppUser(MyUUID.createSessionId(),phone,null,phone,phone,url,"3"));
	}

	public int regAppUser(AppUser appUser) throws Exception {
		return (int)dao.save("AppuserMapper.regAppUser",appUser);
	}
/**
	 * 通过手机号码查询用户信息
	 * @param phone
	 * @return
	 * @throws Exception
	 */

	public AppUser getUserByPhone(String phone) throws Exception {
		return (AppUser) dao.findForObject("AppuserMapper.getUserByPhone",phone);
	}

	/**
	 * 通过ID查询用户信息
	 * @param id
	 * @return
	 * @throws Exception
	 */

	public AppUser getUserByID(String id) throws Exception {
		return (AppUser) dao.findForObject("AppuserMapper.getUserByID",id);
	}

	/**
	 * 更改用户头像
	 * @param appUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateAppUserImage(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserImage",appUser);
	}

	/**
	 * 更改用户昵称
	 * @param appUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateAppUserName(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserName",appUser);
	}

	/**
	 * 根据用户名获取用户信息
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppUser getAppUserByUserName(String name) throws Exception {
		return (AppUser) dao.findForObject("AppuserMapper.getAppUserByNickName",name);
	}

	/**
	 * 根据昵称获取用户信息
	 * @param nickName
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppUser getAppUserByNickName(String nickName) throws Exception {
		return (AppUser)dao.findForObject("AppuserMapper.getAppUserByNickName",nickName);
	}

	@Override
	public List<AppUser> getAppUserByNickNameList(String nickName) throws Exception {
		return (List<AppUser>)dao.findForList("AppuserMapper.getAppUserByNickNameList",nickName);
	}

	/**
	 * 获取用户更新的的账户余额
	 * @param appUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateAppUserBalanceByPhone(AppUser appUser) throws Exception {
		return (int) dao.update("AppuserMapper.updateAppUserBalanceByPhone",appUser);
	}

	/**
	 * rank榜单
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<AppUser> rankList() throws Exception {
		return (List<AppUser>) dao.findForList("AppuserMapper.rankList",null);
	}

	/**
	 * 更新用户的抓娃娃数量
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateAppUserDollTotalById(AppUser appUser) throws Exception {
		return (int) dao.update("AppuserMapper.updateAppUserDollTotalById",appUser);
	}

	@Override
	public int updateAppUserBalanceById(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserBalanceById",appUser);
	}

	@Override
	public int updateAppUsernickName(AppUser appUser) throws Exception {
		return (int) dao.update("AppuserMapper.updateNickName",appUser);
	}

	@Override
	public int updateAppUserCnee(AppUser appUser) throws Exception {
		return (int) dao.update("AppuserMapper.updateAppUserCnee",appUser);
	}

	@Override
	public int regwx(AppUser appUser) throws Exception {
		appUser.setNICKNAME(EmojiUtil.emojiConverterToAlias(appUser.getNICKNAME())); //修改用户昵称转码
		return (int) dao.save("AppuserMapper.regwx",appUser);
	}

	@Override
	public int updateTencentUser(AppUser appUser) throws Exception {
		appUser.setNICKNAME(EmojiUtil.emojiConverterToAlias(appUser.getNICKNAME())); //修改用户昵称转码
		return (int)dao.update("AppuserMapper.updateTencentUser",appUser);
	}

	@Override
	public AppUser testuser(AppUser appUser) throws Exception {
		return (AppUser)dao.findForObject("AppuserMapper.testuser",appUser);
	}

	@Override
	public List<AppUser> getAppUserList() throws Exception {
		return (List<AppUser>)dao.findForList("AppuserMapper.getAppUserList",null);
	}

	@Override
	public int updateAppUserSB(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserSB",appUser);
	}

	@Override
	public int updateAppUserSign(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserSign",appUser);
	}
	
	

    /**
     * 修改账户金币
     * @param userId appUser.userId 用户对象主键
     * @param operNum 操作的数量
     * @param operType A:add,加; S:sub,减
     * @param operMenu 操作的枚举
     * @return
     */
    public int updateUserBalance(String userId,int operNum,String operType,Const.PlayMentCostType operMenu) throws Exception{
    	
    	int oper=0;
    	AppUser appUser=this.getUserAppById(userId);
    	//step1 判断用户是否存在
    	if(appUser ==null){
    		return 0;
    	}
    	if(StrUtil.isNullOrEmpty(appUser.getUSER_ID())){
    		return 0;
    	}
    	if(StrUtil.isNullOrEmpty(appUser.getBALANCE())){
    		appUser.setBALANCE("0");
    	}
    	//step2  判断金额
    	if(operNum<=0){
    		return 0;
    	}
    	
    	//step3  判断追加类型
    	String operGlod="0";
    	if(StrUtil.isNullOrEmpty(operType)){
    		return 0;
    	}else{
    		String newBalance=appUser.getBALANCE();
    		if("A".equals(operType)){
    			operGlod="+"+operNum;
    			newBalance=NumberUtils.add(appUser.getBALANCE(), String.valueOf(operNum));
    		}else if("S".equals(operType)){
    			operGlod="-"+operNum;
    			newBalance=NumberUtils.sub(appUser.getBALANCE(), String.valueOf(operNum));
    		}
    		appUser.setBALANCE(newBalance);
    		
    	}
    	
    	
    	//step4 判断操作类型
    	if(operMenu==null){
    		return 0;
    	}
    	
    	
    	//step5 记录交易明细
        Payment payment = new Payment();
        payment.setCOST_TYPE(operMenu.getValue());
        payment.setUSERID(appUser.getUSER_ID());
        payment.setGOLD(operGlod);
        payment.setREMARK(operMenu.getName());
        paymentService.reg(payment);
    	
        
        //修改金额余额
        oper=this.updateAppUserBalanceNew(appUser);
    	
    	return oper;
    }
    
	/**
	 * 修改用户金币
	 * @param appUser
	 * @return
	 * @throws Exception
	 */
	private int updateAppUserBalanceNew(AppUser appUser)throws Exception{
		return (int)dao.update("AppuserMapper.updateAppUserBalanceNew",appUser);
	}
	
	/**
	 * 查询用户信
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private AppUser getUserAppById(String userId)throws Exception{
		return (AppUser)dao.findForObject("AppuserMapper.getUserAppById",userId);
	}

	@Override
	public AppUser getAppUserRanklist(String userId) throws Exception {
		return (AppUser)dao.findForObject("AppuserMapper.getAppUserRanklist",userId);
	}
	
	/**用户游戏统计列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listUserGames(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.getAppUserGamelistPage", page);
	}
	
	
	/**查看用户的充值总记录**/
	public PageData getAppUesrRechargeToTal(String userId)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.getAppUesrRechargeToTal",userId);
	}
	
    /**
     * 查询渠道的总注册用户数量
     * @param channelCode
     * @return
     * @throws Exception
     */
    public Integer getSysAppUserCountByChannelCode(String channelCode)throws Exception{
    	return (int)dao.findForObject("AppuserMapper.getSysAppUserCountByChannelCode",channelCode);
    }
    
    /**
     * 修改用户的所属推广用户信息
     * @param appUser
     * @return
     * @throws Exception
     */
    public int updateProUserId(AppUser appUser)throws Exception{
    	return (int)dao.update("AppuserMapper.updateProUserId",appUser);
    }
    
    /**
     * 获取用户信息 返回给前端
     * @param userid
     * @return
     * @throws Exception
     */
    public PageData getAppUserForAppByUserId(String userid)throws Exception{
    	PageData pd=(PageData) dao.findForObject("AppuserMapper.getAppUserForAppByUserId",userid);
    	if(pd !=null && StringUtils.isNotEmpty(pd.getString("BDPHONE"))){
    		pd.put("BDPHONE", pd.getString("BDPHONE"));
    	}else{
    		pd.put("BDPHONE","");
    	}
    	return pd;
    }
    
    /**
     * 修改用户手机号码
     * @param appUser
     * @return
     * @throws Exception
     */
    public int updateAppUserPhone(AppUser appUser)throws Exception{
    	return (int)dao.update("AppuserMapper.updateAppUserPhone",appUser);
    }

	@Override
	public List<PageData> rankBetList() throws Exception {
		return (List<PageData>)dao.findForList("AppuserMapper.rankBetList",null);
	}

	@Override
	public List<PageData> rankBetListToday()throws  Exception {
		return (List<PageData>)dao.findForList("AppuserMapper.rankBetListToday",null);
	}

	@Override
	public PageData getAppUserBetRanklist(String userid) throws Exception {
		return (PageData) dao.findForObject("AppuserMapper.getAppUserBetRanklist",userid);
	}

	@Override
	public PageData getAppUserBetRanklistToday(String userid)throws Exception {
		return (PageData) dao.findForObject("AppuserMapper.getAppUserBetRanklistToday",userid);
	}

	@Override
	public int updateAppuserpw(AppUser appUser)throws Exception {
		return (int)dao.update("AppuserMapper.updateAppuserpw",appUser);
	}

	@Override
	public int updateAppUserBlAndBnById(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserBlAndBnById",appUser);
	}

	@Override
	public int updateAppUserCoinMultiples(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserCoinMultiples",appUser);
	}

	@Override
	public List<AppUser> getWeekCardPeoples() throws Exception {
		return (List<AppUser>)dao.findForList("AppuserMapper.getWeekCardPeoples",null);
	}

	@Override
	public List<AppUser> getMonthCardPeoples() throws Exception{
		return (List<AppUser>)dao.findForList("AppuserMapper.getMonthCardPeoples",null);
	}

	@Override
	public List<AppUser> rankListToday() throws Exception{
		return (List<AppUser>)dao.findForList("AppuserMapper.rankListToday",null);
	}

	@Override
	public AppUser getAppUserRanklistToday(String userId) throws Exception {
		return (AppUser)dao.findForObject("AppuserMapper.getAppUserRanklistToday",userId);
	}

	@Override
	public JSONObject doRegTencentUser(AppUser appUser, String imgUrl, String newFace, String nickname, String uid, String channelNum, String gender, String regChannel, UserPoints userPoints, PointsMall pointsMall)throws Exception {
		if (appUser == null) {
			appUser = new AppUser();
			if (imgUrl == null || imgUrl.equals("")) {
				newFace = PropertiesUtils.getCurrProperty("user.default.header.url"); //默认头像
			} else {
				newFace = FaceImageUtil.downloadImage(imgUrl);
			}
			appUser.setNICKNAME(nickname);
			appUser.setIMAGE_URL(newFace);
			appUser.setUSER_ID(uid);
			appUser.setCHANNEL_NUM(channelNum);
			appUser.setGENDER(gender);
			appUser.setOPEN_TYPE(regChannel);
			appUser.setBALANCE("0");
			//   appUser.setPOINTS(pointsMall.getPointsValue());
			appuserService.regwx(appUser); //未注册用户 先注册用户

			if (userPoints==null){
				UserPoints regUserInfo = new UserPoints();
				regUserInfo.setId(MyUUID.getUUID32());
				regUserInfo.setUserId(uid);
				regUserInfo.setLoginGame("1");
				regUserInfo.setTodayPoints(pointsMall.getPointsValue());
				userpointsService.regUserInfo(regUserInfo);

				AppUser appUser1 = appuserService.getUserByID(uid);
				appUser1.setPOINTS(pointsMall.getPointsValue());
				appuserService.updateAppUserBalanceById(appUser1);

				//增加积分记录
				PointsDetail pointsDetail = new PointsDetail();
				pointsDetail.setUserId(uid);
				pointsDetail.setChannel(Const.pointsMallType.points_type00.getName());
				pointsDetail.setType("+");
				pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
				pointsDetail.setPointsValue(pointsMall.getPointsValue());
				pointsdetailService.regPointsDetail(pointsDetail);

				//判断是否增加金币
				userPoints = userpointsService.getUserPointsFinish(uid);
				Integer now_points = userPoints.getTodayPoints();
				String r_tag = userPoints.getPointsReward_Tag();
				if (Integer.valueOf(r_tag) < 5){
					Integer goldValue = 0;
					Integer sum = 0;
					Integer ob = Integer.valueOf(appUser1.getBALANCE());
					Integer nb_2 = 0;
					List<PointsReward> list = pointsrewardService.getPointsReward();
					String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser1);
					userPoints.setPointsReward_Tag(n_rtag);
					userpointsService.updateUserPoints(userPoints);
				}

			}

		} else {
			if (appUser.getSTATUS() != null&&appUser.getSTATUS().equals("0")){
				return RespStatus.fail("该用户已被冻结");
			}

			//如果传过来的头像url是否为空
			if (imgUrl == null || imgUrl.equals("")) {
				newFace = PropertiesUtils.getCurrProperty("user.default.header.url"); //默认头像
			} else {
				newFace = FaceImageUtil.downloadImage(imgUrl);
			}
			//如果当前用户图像不是默认头像，则先删除，再上传
			if (newFace != null && appUser.getIMAGE_URL() != null) {
				String defaultUrl = PropertiesUtils.getCurrProperty("user.default.header.url"); //获取默认头像Id
				if (!defaultUrl.equals(appUser.getIMAGE_URL())) {
					FastDFSClient.deleteFile(appUser.getIMAGE_URL());
				}
			}
			appUser.setNICKNAME(nickname);
			appUser.setIMAGE_URL(newFace);
			appuserService.updateTencentUser(appUser); //已注册用户 更新用户昵称和头像
		}


		String tag =  userPoints.getLoginGame();
		if (tag.equals("0")){
			int a = userPoints.getTodayPoints();
			userPoints.setTodayPoints(a + pointsMall.getPointsValue());
			userPoints.setLoginGame("1");
			userpointsService.updateUserPoints(userPoints);
			appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());
			appuserService.updateAppUserBalanceById(appUser);

			//增加积分记录
			PointsDetail pointsDetail = new PointsDetail();
			pointsDetail.setUserId(uid);
			pointsDetail.setChannel(Const.pointsMallType.points_type00.getName());
			pointsDetail.setType("+");
			pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
			pointsDetail.setPointsValue(pointsMall.getPointsValue());
			pointsdetailService.regPointsDetail(pointsDetail);

			//判断是否增加金币
			userPoints = userpointsService.getUserPointsFinish(uid);
			Integer now_points = userPoints.getTodayPoints();
			String r_tag = userPoints.getPointsReward_Tag();
			if (Integer.valueOf(r_tag) < 5){
				Integer goldValue = 0;
				Integer sum = 0;
				Integer ob = Integer.valueOf(appUser.getBALANCE());
				Integer nb_2 = 0;
				List<PointsReward> list = pointsrewardService.getPointsReward();
				String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
				userPoints.setPointsReward_Tag(n_rtag);
				userpointsService.updateUserPoints(userPoints);
			}
		}



		//登录日志
		AppuserLogin appuserLogin = new AppuserLogin();
		appuserLogin.setAPPUSERLOGININFO_ID(MyUUID.getUUID32());
		appuserLogin.setUSER_ID(uid);
		appuserlogininfoService.insertLoginLog(appuserLogin);

		//SRS推流
		SrsConnectModel sc = new SrsConnectModel();
		long time = System.currentTimeMillis();
		sc.setType("U");
		sc.setTid(appUser.getUSER_ID());
		sc.setExpire(3600 * 24);
		sc.setTime(time);
		sc.setToken(SrsSignUtil.genSign(sc, SrsConstants.SRS_CONNECT_KEY));

		//sessionId
		String sessionID = MyUUID.createSessionId();
		RedisUtil.getRu().set(Const.REDIS_APPUSER_SESSIONID + uid, sessionID);

		Map<String, Object> map = new HashMap<>();
		map.put("sessionID", sessionID);
		map.put("appUser", appuserService.getUserByID(uid));
		map.put("srsToken", sc);
		return RespStatus.successs().element("data", map);

	}


	@Override
	public void doRegSMSUser(AppUser appUser1,PointsMall pointsMall,String sessionId ,HttpServletRequest httpServletRequest) throws Exception{
		this.regAppUser(appUser1);
		String userId = appUser1.getUSER_ID();
		UserPoints userPoints =  userpointsService.getUserPointsFinish(userId);
		if (userPoints==null) {
			UserPoints regUserInfo = new UserPoints();
			regUserInfo.setId(MyUUID.getUUID32());
			regUserInfo.setUserId(userId);
			regUserInfo.setLoginGame("1");
			regUserInfo.setTodayPoints(pointsMall.getPointsValue());
			userpointsService.regUserInfo(regUserInfo);

			//增加积分记录
			PointsDetail pointsDetail = new PointsDetail();
			pointsDetail.setUserId(userId);
			pointsDetail.setChannel(Const.pointsMallType.points_type00.getName());
			pointsDetail.setType("+");
			pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
			pointsDetail.setPointsValue(pointsMall.getPointsValue());
			pointsdetailService.regPointsDetail(pointsDetail);

			//判断是否增加金币
			userPoints = userpointsService.getUserPointsFinish(userId);
			Integer now_points = userPoints.getTodayPoints();
			String r_tag = userPoints.getPointsReward_Tag();
			if (Integer.valueOf(r_tag) < 5){
				Integer goldValue = 0;
				Integer sum = 0;
				Integer ob = Integer.valueOf(appUser1.getBALANCE());
				Integer nb_2 = 0;
				List<PointsReward> list = pointsrewardService.getPointsReward();
				String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser1);
				userPoints.setPointsReward_Tag(n_rtag);
				userpointsService.updateUserPoints(userPoints);
			}
		}

		//登录日志
		AppuserLogin appuserLogin = new AppuserLogin();
		appuserLogin.setAPPUSERLOGININFO_ID(MyUUID.getUUID32());
		appuserLogin.setUSER_ID(appUser1.getUSER_ID());
		appuserLogin.setACCESS_TOKEN(sessionId);
		appuserLogin.setCHANNEL(httpServletRequest.getParameter("channel"));
		appuserLogin.setCTYPE(httpServletRequest.getParameter("ctype"));
		appuserLogin.setNICKNAME(appUser1.getNICKNAME());
		appuserLogin.setONLINE_TYPE("1");
		appuserlogininfoService.insertLoginLog(appuserLogin);

	}

	@Override
	public JSONObject doUserPassLogin(String phone, String pw , HttpServletRequest httpServletRequest) throws Exception{

		if (phone == null || phone.trim().length() <= 0) {
			return RespStatus.fail("手机号不能为空！");
		} else if (!phone.matches("^1(3|4|5|7|8)\\d{9}$")) {
			return RespStatus.fail("手机号码格式错误！");
		} else if (pw == null || pw.trim().length() <= 0) {
			return RespStatus.fail("密码不能为空！");
		}
		AppUser appUser = this.getUserByPhone(phone);
		if (appUser != null) {

			if (appUser.getSTATUS() != null&&appUser.getSTATUS().equals("0")){
				return RespStatus.fail("该用户已被冻结");
			}
			String password = appUser.getPASSWORD();
			String pwmd5 = MD5.md5(pw);
			if (pwmd5.equals(password)) {
				//首先查询积分列表是否有该用户信息

				String userId = appUser.getUSER_ID();
				UserPoints userPoints =  userpointsService.getUserPointsFinish(userId);
				PointsMall pointsMall =  pointsmallService.getInfoById(Const.pointsMallType.points_type00.getValue());
				if (userPoints==null){
					UserPoints regUserInfo = new UserPoints();
					regUserInfo.setId(MyUUID.getUUID32());
					regUserInfo.setUserId(userId);
					regUserInfo.setLoginGame("1");
					regUserInfo.setTodayPoints(pointsMall.getPointsValue());
					userpointsService.regUserInfo(regUserInfo);
					appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());
					appuserService.updateAppUserBalanceById(appUser);

					//增加积分记录
					PointsDetail pointsDetail = new PointsDetail();
					pointsDetail.setUserId(userId);
					pointsDetail.setChannel(Const.pointsMallType.points_type00.getName());
					pointsDetail.setType("+");
					pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
					pointsDetail.setPointsValue(pointsMall.getPointsValue());
					pointsdetailService.regPointsDetail(pointsDetail);

					//判断是否增加金币
					userPoints = userpointsService.getUserPointsFinish(userId);
					Integer now_points = userPoints.getTodayPoints();
					String r_tag = userPoints.getPointsReward_Tag();
					if (Integer.valueOf(r_tag) < 5){
						Integer goldValue = 0;
						Integer sum = 0;
						Integer ob = Integer.valueOf(appUser.getBALANCE());
						Integer nb_2 = 0;
						List<PointsReward> list = pointsrewardService.getPointsReward();
						String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
						userPoints.setPointsReward_Tag(n_rtag);
						userpointsService.updateUserPoints(userPoints);
					}

				}else {
					String tag =  userPoints.getLoginGame();
					if (tag.equals("0")){
						int a = userPoints.getTodayPoints();
						userPoints.setTodayPoints(a + pointsMall.getPointsValue());
						userPoints.setLoginGame("1");
						userpointsService.updateUserPoints(userPoints);
						appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());
						appuserService.updateAppUserBalanceById(appUser);

						//增加积分记录
						PointsDetail pointsDetail = new PointsDetail();
						pointsDetail.setUserId(userId);
						pointsDetail.setChannel(Const.pointsMallType.points_type00.getName());
						pointsDetail.setType("+");
						pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
						pointsDetail.setPointsValue(pointsMall.getPointsValue());
						pointsdetailService.regPointsDetail(pointsDetail);

						//判断是否增加金币
						userPoints = userpointsService.getUserPointsFinish(userId);
						Integer now_points = userPoints.getTodayPoints();
						String r_tag = userPoints.getPointsReward_Tag();
						if (Integer.valueOf(r_tag) < 5){
							Integer goldValue = 0;
							Integer sum = 0;
							Integer ob = Integer.valueOf(appUser.getBALANCE());
							Integer nb_2 = 0;
							List<PointsReward> list = pointsrewardService.getPointsReward();
							String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
							userPoints.setPointsReward_Tag(n_rtag);
							userpointsService.updateUserPoints(userPoints);
						}
					}

				}

				//sessionId
				String sessionID = MyUUID.createSessionId();
				RedisUtil.getRu().set(Const.REDIS_APPUSER_SESSIONID + appUser.getUSER_ID(), sessionID);
				//登录日志
				AppuserLogin appuserLogin = new AppuserLogin();
				appuserLogin.setAPPUSERLOGININFO_ID(MyUUID.getUUID32());
				appuserLogin.setUSER_ID(appUser.getUSER_ID());
				appuserLogin.setACCESS_TOKEN(sessionID);
				appuserLogin.setCHANNEL(httpServletRequest.getParameter("channel"));
				appuserLogin.setCTYPE(httpServletRequest.getParameter("ctype"));
				appuserLogin.setNICKNAME(appUser.getNICKNAME());
				appuserLogin.setONLINE_TYPE("1");
				appuserlogininfoService.insertLoginLog(appuserLogin);

				//SRS推流
				SrsConnectModel sc = new SrsConnectModel();
				long time = System.currentTimeMillis();
				sc.setType("U");
				sc.setTid(appUser.getUSER_ID());
				sc.setExpire(3600 * 24);
				sc.setTime(time);
				sc.setToken(SrsSignUtil.genSign(sc, SrsConstants.SRS_CONNECT_KEY));


				Map<String, Object> map = new LinkedHashMap<>();
				map.put("sessionID", sessionID);
				map.put("appUser", appUser);
				map.put("srsToken", sc);
				return RespStatus.successs().element("data", map);
			} else {
				return RespStatus.fail("账号密码错误");
			}
		} else {
			return RespStatus.fail("无此用户");
		}
	}

	@Override
	public JSONObject dogetDoll(String userId, HttpServletRequest httpServletRequest)throws Exception {
		// String phone = new String(Base64Util.decryptBASE64(aPhone));
		AppUser appUser = appuserService.getUserByID(userId);
		if (appUser != null) {
			if (appUser.getSTATUS() != null&&appUser.getSTATUS().equals("0")){
				return RespStatus.fail("该用户已被冻结");
			}
			String sessionID = MyUUID.createSessionId();
			RedisUtil.getRu().set("sessionId:appUser:" + userId, sessionID);

			//首先查询积分列表是否有该用户信息
			UserPoints userPoints =  userpointsService.getUserPointsFinish(userId);
			PointsMall pointsMall =  pointsmallService.getInfoById(Const.pointsMallType.points_type00.getValue());
			if (userPoints==null){
				UserPoints regUserInfo = new UserPoints();
				regUserInfo.setId(MyUUID.getUUID32());
				regUserInfo.setUserId(userId);
				regUserInfo.setLoginGame("1");
				regUserInfo.setTodayPoints(pointsMall.getPointsValue());
				userpointsService.regUserInfo(regUserInfo);
				appUser.setPOINTS(appUser.getPOINTS()+pointsMall.getPointsValue());
				appuserService.updateAppUserBalanceById(appUser);
				//增加积分记录
				PointsDetail pointsDetail = new PointsDetail();
				pointsDetail.setUserId(userId);
				pointsDetail.setChannel(Const.pointsMallType.points_type00.getName());
				pointsDetail.setType("+");
				pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
				pointsDetail.setPointsValue(pointsMall.getPointsValue());
				pointsdetailService.regPointsDetail(pointsDetail);

				//判断是否增加金币
				userPoints = userpointsService.getUserPointsFinish(userId);
				Integer now_points = userPoints.getTodayPoints();
				String r_tag = userPoints.getPointsReward_Tag();
				if (Integer.valueOf(r_tag) < 5){
					Integer goldValue = 0;
					Integer sum = 0;
					Integer ob = Integer.valueOf(appUser.getBALANCE());
					Integer nb_2 = 0;
					List<PointsReward> list = pointsrewardService.getPointsReward();
					String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
					userPoints.setPointsReward_Tag(n_rtag);
					userpointsService.updateUserPoints(userPoints);
				}

			}else {
				String tag =  userPoints.getLoginGame();
				if (tag.equals("0")){
					int a = userPoints.getTodayPoints();
					Integer now_points = a + pointsMall.getPointsValue();
					userPoints.setTodayPoints(now_points);
					userPoints.setLoginGame("1");
					userpointsService.updateUserPoints(userPoints);
					appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());
					appuserService.updateAppUserBalanceById(appUser);

					//增加积分记录
					PointsDetail pointsDetail = new PointsDetail();
					pointsDetail.setUserId(userId);
					pointsDetail.setChannel(Const.pointsMallType.points_type00.getName());
					pointsDetail.setType("+");
					pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
					pointsDetail.setPointsValue(pointsMall.getPointsValue());
					pointsdetailService.regPointsDetail(pointsDetail);

					//判断是否增加金币
					userPoints = userpointsService.getUserPointsFinish(userId);
					String r_tag = userPoints.getPointsReward_Tag();
					if (Integer.valueOf(r_tag) < 5){
						Integer goldValue = 0;
						Integer sum = 0;
						Integer ob = Integer.valueOf(appUser.getBALANCE());
						Integer nb_2 = 0;
						List<PointsReward> list = pointsrewardService.getPointsReward();
						String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
						userPoints.setPointsReward_Tag(n_rtag);
						userpointsService.updateUserPoints(userPoints);
					}
				}

			}

			//登录日志
			AppuserLogin appuserLogin = new AppuserLogin();
			appuserLogin.setAPPUSERLOGININFO_ID(MyUUID.getUUID32());
			appuserLogin.setUSER_ID(appUser.getUSER_ID());
			appuserLogin.setACCESS_TOKEN(sessionID);
			appuserLogin.setCHANNEL(httpServletRequest.getParameter("channel"));
			appuserLogin.setCTYPE(httpServletRequest.getParameter("ctype"));
			appuserLogin.setNICKNAME(appUser.getNICKNAME());
			appuserLogin.setONLINE_TYPE("1");
			appuserlogininfoService.insertLoginLog(appuserLogin);


			//SRS推流
			SrsConnectModel sc = new SrsConnectModel();
			long time = System.currentTimeMillis();
			sc.setType("U");
			sc.setTid(userId);
			sc.setExpire(3600 * 24);
			sc.setTime(time);
			sc.setToken(SrsSignUtil.genSign(sc, SrsConstants.SRS_CONNECT_KEY));
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("sessionID", sessionID);
			map.put("appUser", this.getUserAppById(userId));
			map.put("srsToken", sc);
			return RespStatus.successs().element("data", map);
		} else {
			return RespStatus.fail("此用户尚未注册！");
		}
	}
}

