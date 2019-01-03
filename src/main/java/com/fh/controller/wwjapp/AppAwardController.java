package com.fh.controller.wwjapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.fh.entity.Page;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.Order;
import com.fh.entity.system.Spreader;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.invateamount.InvateAmountManager;
import com.fh.service.system.ordertest.OrderManager;
import com.fh.service.system.spreader.SpreaderManager;
import com.fh.util.DateUtil;
import com.fh.util.wwjUtil.MyUUID;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fh.controller.base.BaseController;
import com.fh.service.system.appuser.AppUserCodeManager;
import com.fh.service.system.appuser.impl.AppUserAwardListService;
import com.fh.util.PageData;
import com.fh.util.RandomUtils;
import com.fh.util.StringUtils;
import com.fh.util.Const.BaseDictRedisHsetKey;
import com.fh.util.Const.RedisDictKeyConst;
import com.fh.util.wwjUtil.RedisUtil;
import com.fh.util.wwjUtil.RespStatus;

import net.sf.json.JSONObject;
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/app/award")
public class AppAwardController extends BaseController {


    @Resource(name = "appUserAwardListService")
    private AppUserAwardListService appUserAwardListService;


    @Resource(name = "appUserCodeService")
    private AppUserCodeManager appUserCodeService;


	@Resource(name = "appuserService")
	private AppuserManager appuserService;

	@Resource(name = "orderService")
	private OrderManager orderService;

	@Resource(name="spreaderService")
	private SpreaderManager spreaderService;

	@Resource(name="invateamountService")
	private InvateAmountManager invateamountService;

    
    
    /**
     * 获取用户邀请码
     * @return
     */
    @RequestMapping(value = "/getUserAwardCode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserAwardCode(HttpServletRequest request) {
    	
    	
    	PageData reqData=this.getBaseParams(request);  //获取请求参数
    	String userId=reqData.getString("userId");
    	
    	//判断用户Id是否为空
    	if(StringUtils.isEmpty(userId)){
    		return RespStatus.exception();
    	}
    	try {
    		//获取用户兑换码
    		PageData userCode=appUserCodeService.getUserCodeByUserId(reqData.getString("userId"));
    		if(userCode==null){
    			String randomCode=RandomUtils.getRandomLetterStr(8);
    			userCode=appUserCodeService.getUserCodeByCode(randomCode);
    			if(userCode!=null){
    				randomCode=RandomUtils.getRandomLetterStr(8);
			}
    			userCode=new PageData();
    			userCode.put("USER_ID", userId);
    			userCode.put("CODE_VALUE", randomCode);
    			userCode.put("CODE_NUM", 100000);
    			userCode.put("CODE_TYPE", "1" );
    			int oper=appUserCodeService.save(userCode); //保存用户兑换码
    			if(oper<1){
    				 return RespStatus.exception();
    			}
    		}
			
			//查询统计用户分享数 和 分享奖励金币数量
			PageData awardPd=appUserAwardListService.findAwardCountByUserId(userId);


			//查询用户的推广金额和奖励金额以及可兑换次数
			List<PageData> list =  invateamountService.list(new Page());
			PageData  pageData =  list.get(0);
			int inviteAmount = Integer.valueOf(pageData.get("GOLD").toString());
			int exchangeAmount = Integer.valueOf(pageData.get("XXGOLD").toString());
			int inviteTotalNum = Integer.valueOf(pageData.get("TOPINVITENUM").toString());


			//兑换奖励限制
			PageData setPd=new PageData();
			//兑换金币数
			setPd.put("exchangeAmount", exchangeAmount);
			//邀请金币数量
			setPd.put("inviteAmount", inviteAmount);
			//邀请可兑换次数
			setPd.put("inviteTotalNum", inviteTotalNum);
			

			
			//返回
			 Map<String, Object> dataMap = new HashMap<>();
			 dataMap.put("awardPd", awardPd);
			 dataMap.put("setPd", setPd);
			 dataMap.put("codeVale", userCode.getString("CODE_VALUE"));
			 
			/**查看用户是否已经兑换*/
			int s=appUserAwardListService.findUserAwardByUserId(userId);
			if(s>0){
				 dataMap.put("awradFlag", "Y");
			}else{
				 dataMap.put("awradFlag", "N");
			}
	
             return RespStatus.successs().element("data", dataMap);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return RespStatus.fail();
		}
    }
    
    
    /**
     * 获取邀请码兑换
     * @return
     */
    @RequestMapping(value = "/doAwardByUserCode", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject doAwardByUserCode(HttpServletRequest request) {
    	
    	PageData reqData=this.getBaseParams(request);  //获取请求参数
    	
    	String userId=reqData.getString("userId"); //用户Id
    	
    	String awardCode=request.getParameter("awardCode"); //用户兑换码
    	
    	//判断用户Id 和 邀请码是否为空
    	if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(awardCode)){
    		return RespStatus.exception();
    	}
    	
    	try {
    		
    		//step1 判断当前App是否已经兑换过
    		int s=appUserAwardListService.findUserAwardByUserId(userId);
			if(s>0){
				return RespStatus.fail("当前用户已经兑换");
			}
    		//step2 判断当前App是否已经兑换过
			if(!StringUtils.isEmpty(reqData.getString("sfId"))){
				//当前用户是否已经兑换
				s=appUserAwardListService.findUserAwardByAppId(reqData.getString("sfId"));
				if(s>0){
					return RespStatus.fail("当前设备已经兑换");
				}
			}
    		
    		//step3 判断兑换码是否存在
			PageData awarkPd=appUserCodeService.getUserCodeByCode(awardCode);
			if(awarkPd==null){
				return RespStatus.fail("兑换码不存在");
			}
			
			//step4 判断兑换码是否是当前用户
			if(userId.equals(awarkPd.getString("USER_ID"))){
				return RespStatus.fail("兑换码是当前用户");
			}
			//查询用户的推广金额和奖励金额以及可兑换次数
			List<PageData> list =  invateamountService.list(new Page());
			PageData  pageData =  list.get(0);
			int inviteAmount = Integer.valueOf(pageData.get("GOLD").toString());
			int exchangeAmount = Integer.valueOf(pageData.get("XXGOLD").toString());
			int inviteTotalNum = Integer.valueOf(pageData.get("TOPINVITENUM").toString());

			
			//step5 查询兑换码已经兑换的次数
			PageData awardTotal=appUserAwardListService.findAwardCountByUserId(awarkPd.getString("USER_ID"));
			int awardCount=0; //已经兑换次数
			if(awardTotal !=null){
				//已经兑换的次数
				awardCount=Integer.parseInt(awardTotal.get("AWARDCOUNT").toString());
			}
			int invite_awardTotal=10000;
			try{
				String award_Total_NumStr =pageData.get("TOPINVITENUM").toString();
				invite_awardTotal=Integer.parseInt(award_Total_NumStr);
			}catch(Exception ex){
				logger.info(ex.getMessage());
			}
			
			if(awardCount>invite_awardTotal){
				return RespStatus.fail("兑换码次数已经超过限制");
			}
			appUserAwardListService.doAwardByUserCode(awarkPd, userId, reqData.getString("sfId"),exchangeAmount,inviteAmount);

			//兑换奖励限制
			PageData setPd=new PageData();
			//兑换金币数
			setPd.put("exchangeAmount", exchangeAmount);
			//邀请金币数量
			setPd.put("inviteAmount", inviteAmount);
			//邀请可兑换次数
			setPd.put("inviteTotalNum", inviteTotalNum);
			 
			
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("setPd", setPd);
			
			/**查看用户是否已经兑换*/
			s=appUserAwardListService.findUserAwardByUserId(userId);
			if(s>0){
				 dataMap.put("awradFlag", "Y");
			}else{
				 dataMap.put("awradFlag", "N");
			}
			
			return RespStatus.successs().element("data", dataMap);
			
		} catch (Exception e) {
			logger.info(e.getMessage());
			return RespStatus.fail();
		}
    
    }
/************************************************************ 推广版本系列接口* **********************************************************************************/
	/**
	 * 查询推广的下线人员
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getpsUser",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
    public JSONObject getpsUser(@RequestParam("userId") String userId
								){
    	try {
			List<PageData> list = appuserService.getpsUser(userId);
			for (int i = 0; i < list.size() ; i++) {
				PageData p =  list.get(i);
				Object a = p.get("ALLREGAMOUNT");
				if (a == null){
					p.put("ALLREGAMOUNT",0);
				}
			}
			Map<String ,Object> map = new HashMap<>();
			map.put("userList",list);
			return RespStatus.successs().element("data",map);
		}catch (Exception e){
    		e.printStackTrace();
    		return RespStatus.fail();

		}
    }

	/**
	 * 查询该用户的充值记录
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/getpsUserCharge",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject getpsUserCharge(@RequestParam("userId") String userId,
									  @RequestParam("time") String time
									  ){
    	try {
			String begin_time = DateUtil.getMinMonthDate(time);
			String end_time = DateUtil.getMaxMonthDate(time);
    		PageData pageData = new PageData();
    		pageData.put("userId",userId);
			pageData.put("begin_time",begin_time);
			pageData.put("end_time",end_time);
    		List<PageData> list = orderService.getpsUserCharge(pageData);
    		int summoney = 0;
    		if (list != null){
				for (int i = 0; i < list.size(); i++) {
					PageData pd=  list.get(i);
					 summoney +=  Integer.valueOf(pd.get("REGAMOUNT").toString());
				}
			}
			Map<String ,Object> map = new HashMap<>();
			map.put("chargeList",list);
			map.put("summoney",summoney);
			return RespStatus.successs().element("data",map);
		}catch (Exception e){
    		e.printStackTrace();
    		return RespStatus.fail();
		}
	}

	/**
	 * 推广用户存储提款账号
	 * @param userId
	 * @param account
	 * @return
	 */
	@RequestMapping(value = "/insertProAccount",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject insertProAccount(@RequestParam("userId") String userId,
									   @RequestParam(value = "account")String account,
									   @RequestParam("type") String type){
		try {
			AppUser appUser = appuserService.getUserByID(userId);
			if (type.equals("wx")){
				appUser.setPRO_WXACCOUNT(account);
			}else {
				appUser.setPRO_ZFBACCOUNT(account);
			}
			appuserService.updateAppUserBalanceById(appUser);
			return RespStatus.successs();

		}catch (Exception e){
			e.printStackTrace();
			return RespStatus.fail();
		}
	}



	/**
	 * 推广者提款
	 * @param userId
	 * @param money
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/extractingAmount",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject extractingAmount(@RequestParam("userId") String userId,
										@RequestParam("money") Integer money,
									   	@RequestParam("type") String type,
									    @RequestParam(value = "account",required = false)String account


	){
		try {
			AppUser appUser = appuserService.getUserByID(userId);
			int pb =  appUser.getPRO_BALANCE();
			if (money > pb){
				return RespStatus.fail("数据不合法");
			}
			int np = pb - money;
			appUser.setPRO_BALANCE(np);
			appuserService.updateAppUserBalanceById(appUser);


			Spreader spreader = new Spreader();
			spreader.setUSERID(userId);
			spreader.setWITHDRAWALS(money);
			if (type.equals("wx")){
				spreader.setACCOUNT(appUser.getPRO_WXACCOUNT());
			}else {
				spreader.setACCOUNT(appUser.getPRO_ZFBACCOUNT());
			}
			spreader.setTYPE(type);
			spreader.setSPREADER_ID(MyUUID.getUUID32());
			spreaderService.regInfo(spreader);

			List<Spreader> list = spreaderService.listS(userId);
			Map<String,Object> map = new HashMap<>();
			map.put("listS",list);
			map.put("proBlance",np);
			return RespStatus.successs().element("data",map);
		}catch (Exception e){
			e.printStackTrace();
			return RespStatus.fail();
		}
	}

	/**
	 * 查询下线客户抓中次数
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/userCatchNum",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject userCatchNum(@RequestParam("userId") String userId
								   ){
		try {
			PageData pageData = new PageData();
			pageData.put("PRO_USER_ID",userId);
			List<PageData> list =  appuserService.getcpUser(pageData);
			Map<String,Object> map = new HashMap<>();
			map.put("userList",list);
			return RespStatus.successs().element("data",map);
		}catch (Exception e){
			e.printStackTrace();
			return RespStatus.fail();
		}

	}

	/**
	 * 推广者取款明细
	 * @param userId
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "/getMoneyInfo",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public JSONObject getMoneyInfo(@RequestParam("userId") String userId,
								   @RequestParam("time") String time){

		try {
			String begin_time = DateUtil.getMinMonthDate(time);
			String end_time = DateUtil.getMaxMonthDate(time);
			PageData pageData = new PageData();
			pageData.put("userId",userId);
			pageData.put("begin_time",begin_time);
			pageData.put("end_time",end_time);
			List<PageData> list = spreaderService.list_time(pageData);

			PageData p1 = spreaderService.list_time_money(pageData);
			PageData p = new PageData();
			Map<String,Object> map = new HashMap<>();
			if ( p1 == null){
				p.put("USERID",userId);
				p.put("ALLMONEY",0);
				map.put("money",p);
			}else {
				map.put("money",p1);
			}
			map.put("info",list);

			return RespStatus.successs().element("data",map);

		}catch (Exception e){
			e.printStackTrace();
			return RespStatus.fail();
		}
	}

	public static void main(String[] a){
		String aa = DateUtil.getMaxMonthDate("2015-06-25");
		System.out.println(aa);

	}







}
