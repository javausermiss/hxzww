package com.fh.controller.wwjapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.entity.system.AccountInf;
import com.fh.entity.system.TransOrder;
import com.fh.service.system.trans.AccountInfManager;
import com.fh.service.system.trans.AccountLogManager;
import com.fh.service.system.trans.impl.TransOrderService;
import com.fh.util.Const;
import com.fh.util.NumberUtils;
import com.fh.util.PageData;
import com.fh.util.StringUtils;
import com.fh.util.Const.AccountTransType;
import com.fh.util.resp.TxnResp;
import com.fh.util.wwjUtil.RedisUtil;
import com.fh.util.wwjUtil.RespStatus;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/app/account")
public class AppAcountController extends BaseController {

    @Resource(name = "accountLogService")
    private AccountLogManager accountLogService;
    
    @Resource(name = "accountInfService")
    private AccountInfManager accountInfService;
    
    
    @Resource(name = "transOrderService")
    private TransOrderService transOrderService;
    
    /**
     * 获取用户余额
     * @return
     */
    @RequestMapping(value = "/getUserAccBalCount", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserAccBalCount(HttpServletRequest request) {
    	String userId=request.getParameter("userId");
    	
    	if(StringUtils.isEmpty(userId)){
    		return RespStatus.fail("用户不存在");
    	}
    	
    	try{
    	   String accBal=accountInfService.getAccountCountByUserId(userId);
	    	Map dataMap=new HashMap<>();
	    	dataMap.put("accBal", NumberUtils.RMBCentToYuan(accBal));
            return RespStatus.successs().element("data", dataMap);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return RespStatus.fail();
		}
    }
    
    /**
     * 获取用户的推广的收益明细
     * @return
     */
    @RequestMapping(value = "/getUserPromoteList",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserPromoteList(HttpServletRequest request) {
    	String userId=request.getParameter("userId");
    	
    	if(StringUtils.isEmpty(userId)){
    		return RespStatus.fail("用户不存在");
    	}
    	
    	try{
	    	PageData pd=new PageData();
	    	pd.put("userId", userId);
	    	pd.put("transType", Const.AccountTransType.TRANS_1001.getValue());
	    	List<PageData> logList= accountLogService.getAccountLogByTypelistAll(pd);
	    	
	    	Map dataMap=new HashMap<>();
	    	dataMap.put("logList", logList);
            return RespStatus.successs().element("data", dataMap);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return RespStatus.fail();
		}
    }
    
    
    /**
     * 获取用户收支明细
     * @return
     */
    @RequestMapping(value = "/getUserAccountDetailPage",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserAccountDetailPage(HttpServletRequest request) {
    	String userId=request.getParameter("userId");
    	
    	if(StringUtils.isEmpty(userId)){
    		return RespStatus.fail("用户不存在");
    	}
    	
        //获取前端分类
        PageData pd = new PageData();
        pd.put("USER_ID", userId);
        
        
        //获取当前页
        Page page = new Page();
        int currentPage = NumberUtils.parseInt(request.getParameter("nextPage"), 1);
        page.setCurrentPage(currentPage); //当前页数

        //分页查询条件
        page.setPd(pd);
        
    	try{
    	    List<PageData> varList=transOrderService.findAccountOrderPage(page);
	    	Map dataMap=new HashMap<>();
	    	
	    	for (PageData pageData : varList) {
				
	    		if(!StringUtils.isEmpty(pageData.getString("TRANS_TYPE"))){
		    		if(AccountTransType.findByCode(pageData.getString("TRANS_TYPE")).getValue().equals(AccountTransType.TRANS_1001.getValue())){
		    			pageData.put("LOG_STR", AccountTransType.TRANS_1001.getName()+"-"+pageData.getString("RES_COLUMN1")+"充值");
		    		}else{
		    			pageData.put("LOG_STR",AccountTransType.findByCode(pageData.getString("TRANS_TYPE")).getName());
		    		}
	    		}
			}
	    	dataMap.put("logList", varList);
	    	
	         //分页信息
            pd.put("currentPage", page.getCurrentPage());
            pd.put("showCount", (page.getShowCount()));
            pd.put("totalPage", (page.getTotalPage()));
            dataMap.put("pd", pd); //搜索的顺序
	    	
            return RespStatus.successs().element("data", dataMap);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return RespStatus.fail();
		}
    }
    
    /**
     * 用户提现申请
     * @param userId
     * @param transAmt
     * @param ctype
     * @param channel
     * @param deviceType
     * @param osVersion
     * @param appVersion
     * @param sfId
     * @return
     */
    @RequestMapping(value = "/doWithdrawCash", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject doWithdrawCash(
    		HttpServletRequest request,
    		@RequestParam(value="userId" ,required = true) String userId,
    		@RequestParam(value="smsType" ,required = true) String smsType,
    		@RequestParam(value="phoneCode" ,required = false) String phoneCode,
    		@RequestParam(value="orderAmt" ,required = true) String orderAmt,
            @RequestParam(value="ctype" ,required = false) String ctype,
            @RequestParam(value = "channel" ,required = false) String channel,
            @RequestParam(value = "deviceType" ,required = false) String deviceType,
            @RequestParam(value = "osVersion" ,required = false) String osVersion,
            @RequestParam(value = "appVersion" ,required = false) String appVersion,
            @RequestParam(value = "sfId" ,required = false) String sfId){
    		
    		try{
		    	
		    	//验证手机短信
            	if(StringUtils.isEmpty(smsType) || StringUtils.isEmpty(phoneCode)){
            		return RespStatus.fail("验证码不存在");
            	}
            	
            	if(!phoneCode.equals( RedisUtil.getRu().get(Const.getReidsSmsKey(userId, smsType)))){
            		return RespStatus.fail("验证码错误");
            	}
		    	
		    	//验证账户余额
	    		AccountInf accountInf=accountInfService.findByUserId(userId);
	    		if(accountInf==null){
	    			return RespStatus.fail("请先购买推广权益分成");
	    		}
	    		
		    	
		    	//订单信息
		    	String transAmt=NumberUtils.RMBYuanToCent(orderAmt);
		    	
		    	TransOrder transOrder=new TransOrder();
		    	transOrder.setUserId(userId);
		    	transOrder.setTransAmt(transAmt);
		    	transOrder.setOrgTransAmt(transAmt);
		    	transOrder.setTransType(AccountTransType.TRANS_5000.getValue()); //提现
		    	transOrder.setDmsUserId(userId);
		    	transOrder.setDmsUserUnionId(userId);
		    	//提现订单请求
		    	TxnResp txnResp=transOrderService.doAppWithdrawCash(transOrder);
		    	
		    	if("00".equals(txnResp.getResultCode())){
		    		//查找用户的账户信息
		    		accountInf=accountInfService.findByUserId(userId);
			        //返回
			        Map<String, Object> map = new HashMap<>();
			        map.put("orderInf", txnResp);
			        map.put("accBal", NumberUtils.RMBCentToYuan(accountInf.getAccBal()));
			        return RespStatus.successs().element("data", map);
		    	}else{
		    		return RespStatus.fail("订单处理失败");
		    	}
		    }catch (Exception e){
		    	logger.info(e.getMessage());
		        return RespStatus.fail();
		    }
    }

    
}
