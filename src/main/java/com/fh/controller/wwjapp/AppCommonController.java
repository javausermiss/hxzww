package com.fh.controller.wwjapp;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.entity.system.AppUser;
import com.fh.entity.system.BankCard;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.bankcard.BankCardManager;
import com.fh.util.Const;
import com.fh.util.PageData;
import com.fh.util.RandomUtils;
import com.fh.util.StringUtils;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RedisUtil;
import com.fh.util.wwjUtil.RespStatus;
import com.fh.util.wwjUtil.SMSUtil;

import net.sf.json.JSONObject;

/**
 * 短信接口类
 *
 * @author wjy
 */
@Controller
@RequestMapping("/app/common")
public class AppCommonController {

	@Resource(name="appuserService")
	private AppuserManager appuserService;
	
	
	@Resource(name="bankcardService")
	private BankCardManager bankcardService;
	
	
    /**
     * 用户用户账户信息
     * @param JSONObject
     * @return
     */
    @RequestMapping(value = "/getAppUserInf", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getAppUserInf(
    		HttpServletRequest request,
    		@RequestParam(value="userId" ,required = true) String userId) {
        try {
        	
        	PageData appUser=appuserService.getAppUserForAppByUserId(userId);
        	if(appUser==null){
        		return RespStatus.fail("用户信息不存在");
        	}
        	
        	//银行卡信息
        	String isBankInf="0";
        	BankCard bankCard=bankcardService.getBankInfForAppByUserId(userId);
        	if(bankCard==null){
        		bankCard=new BankCard();
        	}else{
        		isBankInf="1";
        	}
        	
        	 Map<String, Object> dataMap = new HashMap<>();
        	 dataMap.put("appUser", appUser);
        	 dataMap.put("isBankInf", isBankInf);
        	 dataMap.put("bankCard", bankCard);
		        
		     return RespStatus.successs().element("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.exception();
        }
    }
    
	
    /**
     * 用户推广加盟提现银行卡信息
     * @param select "0" 查询 "1" 修改
     * @param userId 用户ID
     * @param bankAddress 银行地址
     * @param bankName 银行名称
     * @param bankBranch 支行名称
     * @param bankCardNo 银行卡号
     * @param idNumber 身份证号码
     * @param userName 用户真实姓名
     * @param bankPhone 手机号码
     * @param isDefault 是否默认
     * @return
     */
    @RequestMapping(value = "/regBankInf",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject regBankInf(
            @RequestParam(value ="userId",required = true) String userId,
            @RequestParam(value ="smsType",required = true) String smsType,
            @RequestParam(value ="phoneNumber",required = false) String phoneNumber,
            @RequestParam(value ="phoneCode",required = true) String phoneCode,
            @RequestParam(value ="bankAddress",required = false) String bankAddress,
            @RequestParam(value ="bankName",required = false) String bankName,
            @RequestParam(value ="bankBranch",required = false) String bankBranch,
            @RequestParam(value ="bankCardNo",required = false) String bankCardNo,
            @RequestParam(value ="idNumber",required = false) String idNumber,
            @RequestParam(value ="userName",required = false) String userName,
            @RequestParam(value = "isDefault",required = false) String isDefault,
            HttpServletRequest httpServletRequest
    ) {
        try {
        	
        	if(StringUtils.isEmpty(smsType) || StringUtils.isEmpty(phoneCode)){
        		return RespStatus.fail("验证码不存在");
        	}
        	
        	if(!phoneCode.equals( RedisUtil.getRu().get(Const.getReidsSmsKey(userId, smsType)))){
        		return RespStatus.fail("验证码错误");
        	}
        	
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return RespStatus.fail("无此用户");
            }
            
            //保存银行信息
            boolean isCard=false;
            BankCard bankCard = bankcardService.getBankInfByUserId(userId);
            if(bankCard==null){
            	bankCard=new BankCard();
            	bankCard.setBANKCARD_ID(MyUUID.getUUID32());
            	isCard=true;
            }
            bankCard.setBANK_ADDRESS(bankAddress);
            bankCard.setUSER_ID(userId);
            bankCard.setBANK_BRANCH(bankBranch);
            bankCard.setBANK_CARD_NO(bankCardNo);
            bankCard.setBANK_PHONE(phoneNumber);
            bankCard.setBANK_NAME(bankName);
            bankCard.setUSER_REA_NAME(userName);
            bankCard.setID_NUMBER(idNumber);
            if(isCard){
            	bankcardService.regBankInf(bankCard);
            }else{
            	bankcardService.updateBankInfByUserId(bankCard);
            }
           
            
        	//返回银行卡信息
        	String isBankInf="0";
            bankCard=bankcardService.getBankInfForAppByUserId(userId);
        	if(bankCard==null){
        		bankCard=new BankCard();
        	}else{
        		isBankInf="1";
        	}
        	
        	 Map<String, Object> dataMap = new HashMap<>();
        	 dataMap.put("appUser", appUser);
        	 dataMap.put("isBankInf", isBankInf);
        	 dataMap.put("bankCard", bankCard);
            return RespStatus.successs().element("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }
    
    /**
     * 修改用户手机号
     * @param JSONObject
     * @return
     */
    @RequestMapping(value = "/editAppUserPhone", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject editAppUserPhone(
    		HttpServletRequest request,
    		@RequestParam(value="userId" ,required = true) String userId,
    		@RequestParam(value="smsType" ,required = true) String smsType,
    		@RequestParam(value="phoneNumber" ,required = false) String phoneNumber,
    		@RequestParam(value="phoneCode" ,required = false) String phoneCode){
    	
        try {
        	
        	if(StringUtils.isEmpty(smsType) || StringUtils.isEmpty(phoneCode) ||  StringUtils.isEmpty(phoneNumber)){
        		return RespStatus.fail("验证码不存在");
        	}
        	
        	if(!phoneCode.equals( RedisUtil.getRu().get(Const.getReidsSmsKey(userId, smsType)))){
        		return RespStatus.fail("验证码错误");
        	}
        	
          	AppUser appUser = appuserService.getUserByID(userId);
        	if(appUser==null){
        		return RespStatus.fail("用户信息不存在");
        	}
        	
        	/**修改用户的手机号**/
        	appUser.setPHONE(phoneNumber);
        	appuserService.updateAppUserPhone(appUser);
        	
        	PageData userPd=appuserService.getAppUserForAppByUserId(userId);
        	if(appUser==null){
        		return RespStatus.fail("用户信息不存在");
        	}
        	 Map<String, Object> dataMap = new HashMap<>();
        	 dataMap.put("appUser", userPd);
		     return RespStatus.successs().element("data", dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.exception();
        }
    }
	
    /**
     * 获取短信验证码
     *
     * @param aPhone
     * @return
     */
    @RequestMapping(value = "/getPhoneCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject doSendSmsByType(
    		HttpServletRequest request,
    		@RequestParam(value="userId" ,required = true) String userId,
    		@RequestParam(value="smsType" ,required = true) String smsType,
    		@RequestParam(value="phoneNumber" ,required = false) String phoneNumber
    		) {
    	    	
        try {
        	AppUser appUser=null;
        	
        	String randCode=RandomUtils.getRandomNumbernStr(6); //获取6位数字
        	//提现
        	if(Const.AppPhoneSmsMenu.PHONE_SMS_TYPE_4000.getCode().equals(smsType) 
        			|| Const.AppPhoneSmsMenu.PHONE_SMS_TYPE_3000.getCode().equals(smsType)){
        		appUser=appuserService.getUserByID(userId);
        		
        		if(StringUtils.isNotEmpty(appUser.getPHONE())){
        			phoneNumber=appUser.getPHONE();
        		}else{
        			return RespStatus.fail("请先绑定手机号");
        		}
        	}
            SMSUtil.veriCode1(phoneNumber, randCode);
            RedisUtil.getRu().setex(Const.getReidsSmsKey(userId, smsType), randCode, 300);  //失效时间5分钟
            return RespStatus.successs();
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.exception();
        }
    }
    

}



