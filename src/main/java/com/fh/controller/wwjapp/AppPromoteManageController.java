package com.fh.controller.wwjapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fh.controller.base.BaseController;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.Payment;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.promote.PromoteAppUserManager;
import com.fh.service.system.promotemanage.PromoteManageManager;
import com.fh.util.Const;
import com.fh.util.PageData;
import com.fh.util.StringUtils;
import com.fh.util.wwjUtil.RespStatus;

import net.sf.json.JSONObject;
/**
 * 	查询渠道推广
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/app/promomote")
public class AppPromoteManageController extends BaseController {

	@Resource(name="promotemanageService")
	private PromoteManageManager promotemanageService;
	
	@Resource(name="promoteAppUserService")
	private PromoteAppUserManager promoteAppUserService;
	
    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    
    @Resource(name = "paymentService")
    private PaymentManager paymentService;
	

	/**
	 * 查询渠道推广列表信息
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "/getpromomoteManage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getpromomoteManage(HttpServletRequest request) {
		try{
			List<PageData>	varList = promotemanageService.listAll();	//列出PromoteManage列表
			Map<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("varList", varList);
			return RespStatus.successs().element("data", dataMap);
		}catch (Exception e) {
			logger.info(e.getMessage());
			return RespStatus.fail();
		}
	}
    
	/**
	 * 查询用户购买的推广加盟信息
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "/getUserPromoteInf", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getpromomoteManage(HttpServletRequest request,
    		@RequestParam(value="userId" ,required = true) String userId,
            @RequestParam(value="ctype" ,required = false) String ctype,
            @RequestParam(value = "channel" ,required = false) String channel,
            @RequestParam(value = "deviceType" ,required = false) String deviceType,
            @RequestParam(value = "osVersion" ,required = false) String osVersion,
            @RequestParam(value = "appVersion" ,required = false) String appVersion,
            @RequestParam(value = "sfId" ,required = false) String sfId
    		) {
    	logger.info("getUserPromoteInf request params -->"+request.getQueryString());
    	
    	if(StringUtils.isEmpty(userId)){
    		return RespStatus.fail("用户不存在");
    	}
    	
		try{
			
			Map<String,Object> dataMap = new HashMap<String,Object>();
			
			//查询用购买的推广信息
			PageData proPd =promoteAppUserService.findByUserId(userId);
			
			if(proPd !=null){
				dataMap.put("promoteFlag", "1"); //加入推广标志
				dataMap.put("proPd", proPd);
			}else{
				dataMap.put("promoteFlag", "0");
			}
			return RespStatus.successs().element("data", dataMap);
		}catch (Exception e) {
			logger.info(e.getMessage());
			return RespStatus.fail();
		}
	}
    
    
	/**
	 * 用户输入加盟推广码
	 * @param request
	 * @return
	 */
    @RequestMapping(value = "/commitUserPromoteCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getpromomoteManage(HttpServletRequest request,
    		@RequestParam(value="userId" ,required = true) String userId,
    		@RequestParam(value="promoteCode" ,required = true) String promoteCode,
            @RequestParam(value="ctype" ,required = false) String ctype,
            @RequestParam(value = "channel" ,required = false) String channel,
            @RequestParam(value = "deviceType" ,required = false) String deviceType,
            @RequestParam(value = "osVersion" ,required = false) String osVersion,
            @RequestParam(value = "appVersion" ,required = false) String appVersion,
            @RequestParam(value = "sfId" ,required = false) String sfId
    		) {
    	logger.info("commitUserPromoteCode request params -->"+request.getQueryString());
    	
    	if(StringUtils.isEmpty(userId)){
    		return RespStatus.fail("用户不存在");
    	}
    	
    	if(StringUtils.isEmpty(promoteCode)){
    		return RespStatus.fail("请输入推广码");
    	}
    	
		try{
			
			AppUser appUser = appuserService.getUserByID(userId);
			if(appUser==null){
				return RespStatus.fail("用户不存在");
			}
			
			if(StringUtils.isNotEmpty(appUser.getPRO_USER_ID())){
				return RespStatus.fail("用户已输入过推广码");
			}
			
			//查询推广码信息
			PageData proUserPd =promoteAppUserService.findByProCode(promoteCode);
			if(proUserPd==null){
				return RespStatus.fail("推广码不存在");
			}
			
			if(userId.equals(proUserPd.getString("USER_ID"))){
				return RespStatus.fail("不能输入自己的推广码");
			}
			
			//更改用户所属的推广用户
			appUser.setPRO_USER_ID(proUserPd.getString("USER_ID"));
			appuserService.updateProUserId(appUser);
			
			//查询购买的推广权益 输入推广码可获取的金币数量
			long proManageId=(long)proUserPd.get("PRO_MANAGE_ID");
			PageData mngPro=promotemanageService.findById(String.valueOf(proManageId));
			int gold=10;
			if(mngPro !=null && StringUtils.isNotEmpty(mngPro.getString("CONVER_GOLD"))){
				gold=Integer.parseInt(mngPro.getString("CONVER_GOLD"));
			}
            //step4 更新账户金币余额
            int balance = Integer.valueOf(appUser.getBALANCE()) + gold;
            appUser.setBALANCE(String.valueOf(balance));
            appuserService.updateAppUserBalanceById(appUser);
            
            //step5 更新收支表
            Payment payment = new Payment();
            payment.setGOLD("+" + gold);
            payment.setUSERID(appUser.getUSER_ID());
            payment.setDOLLID(null);
            payment.setCOST_TYPE(Const.PlayMentCostType.cost_type20.getValue());
            payment.setREMARK(Const.PlayMentCostType.cost_type20.getName() +"+"+ gold);
            paymentService.reg(payment);
            
			
			Map<String,Object> dataMap = new HashMap<String,Object>();
			dataMap.put("promoteFlag", "1"); //加入推广标志
			dataMap.put("appUser",appuserService.getUserByID(userId));
			return RespStatus.successs().element("data", dataMap);
		}catch (Exception e) {
			e.printStackTrace();
			return RespStatus.fail();
		}
	}
}
