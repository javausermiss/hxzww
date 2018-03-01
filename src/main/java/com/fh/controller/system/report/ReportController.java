package com.fh.controller.system.report;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.ordertest.impl.OrderService;
import com.fh.service.system.payment.PaymentManager;
import com.fh.util.DateUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;

/** 
 * 类名称：报表
 * 创建人：FH Q313596790
 * 修改时间：2014年11月17日
 * @version
 */
@Controller
@RequestMapping(value="/report")
public class ReportController extends BaseController {
	
	
	
	@Resource(name="paymentService")
	private PaymentManager paymentService;
	
	@Resource(name="orderService")
	private OrderService orderService;
	

	/**显示APP_USER 充值统计
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/rechargeCount")
	public ModelAndView rechargeCount(HttpServletRequest request){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			mv.setViewName("system/report/recharge_count");
			
			if(pd.get("lastStart") ==null || "".equals(pd.get("lastStart"))){
				pd.put("lastStart", DateUtil.getCalendarByMonths(-1)); //获取当前日期的字符
			}
			
			if(pd.get("lastEnd") ==null || "".equals(pd.get("lastEnd"))){
				pd.put("lastEnd", DateUtil.getDay()); //获取一个月之前的时间字符串
			}
			List<PageData> varlist=paymentService.getRechargeCount(pd);
			mv.addObject("varlist", varlist);
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	
	/**显示APP_USER 单日留存用户
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/remainCountByDate")
	public ModelAndView remainCountByDate(HttpServletRequest request){
		ModelAndView mv = this.getModelAndView();
		
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			mv.setViewName("system/report/remain_count");
			
			if(pd.get("lastStart") ==null || "".equals(pd.get("lastStart"))){
				pd.put("lastStart", DateUtil.getCalendarByMonths(-1)); //获取当前日期的字符
			}
			
			if(pd.get("lastEnd") ==null || "".equals(pd.get("lastEnd"))){
				pd.put("lastEnd", DateUtil.getDay()); //获取一个月之前的时间字符串
			}
			List<PageData> varlist=paymentService.getRemainCount(pd);
			mv.addObject("varlist", varlist);
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	/**用户充值明细
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/userRegDetailList")
	public ModelAndView userRegDetailList(HttpServletRequest request,Page page){
		ModelAndView mv = this.getModelAndView();
	
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			mv.setViewName("system/report/user_reg_detail_list");
			
			if(pd.get("lastStart") ==null || "".equals(pd.get("lastStart"))){
				pd.put("lastStart", DateUtil.getCalendarByMonths(-1)); //获取当前日期的字符
			}
			
			if(pd.get("lastEnd") ==null || "".equals(pd.get("lastEnd"))){
				pd.put("lastEnd", DateUtil.getDay()); //获取一个月之前的时间字符串
			}
			String keywords = pd.getString("keywords");				//关键词检索条件
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			page.setPd(pd);
			List<PageData> varlist=orderService.getUserRegDetailList(page);
			mv.addObject("varlist", varlist);
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		
		return mv;
	}
}
