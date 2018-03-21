package com.fh.controller.system.account;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.payment.PaymentManager;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;

/** 
 * 类名称：账户明细
 * 创建人：FH Q313596790
 * 修改时间：2014年11月17日
 * @version
 */
@Controller
@RequestMapping(value="/account/detail")
public class AccountDetailController extends BaseController {
	
	@Resource(name="paymentService")
	private PaymentManager paymentService;


	/**显示APP_USER 收支明细列表
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/appUserAcountList")
	public ModelAndView appUserAcountList(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String keywords = pd.getString("keywords");							//检索条件 关键词
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			page.setPd(pd);
			List<PageData> detaillist=paymentService.findUserGameGoldDetaillist(page);
			mv.setViewName("system/account/appuser_detail_list");
			mv.addObject("detaillist", detaillist);
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	

	/**显示APP_USER 订单充值明细
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/appUserOrderList")
	public ModelAndView appUserOrderList(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String keywords = pd.getString("keywords");							//检索条件 关键词
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			page.setPd(pd);
			List<PageData> varlist=paymentService.findRegDetaillistPage(page);
			mv.setViewName("system/account/appuser_order_list");
			mv.addObject("varlist", varlist);
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.toString(), e);
		}
		return mv;
	}
	
	//充值统计
	@RequestMapping(value="/appUserOrderStatistics")
	public ModelAndView appUserOrderStatistics(Page page){
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		try{
			pd = this.getPageData();
			String keywords = pd.getString("keywords");							//检索条件 关键词
			if(null != keywords && !"".equals(keywords)){
				pd.put("keywords", keywords.trim());
			}
			page.setPd(pd);
			List<PageData> varlist=paymentService.findRegTotallistPage(page);
//			List<PageData> total = paymentService.getUserTotal(page);	
			mv.setViewName("system/account/appuser_order_statistics");
//			mv.addObject("total",total);
			mv.addObject("varlist", varlist);
			mv.addObject("pd", pd);
			mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		} catch(Exception e){
			logger.error(e.getMessage());
		}
		return mv;
	}
	//月充值统计
		@RequestMapping(value="/appUserOrderMonthStatistics")
		public ModelAndView appUserOrderMonthStatistics(Page page){
			ModelAndView mv = this.getModelAndView();
			PageData pd = new PageData();
			try{
				pd = this.getPageData();
				String keywords = pd.getString("keywords");							//检索条件 关键词
				if(null != keywords && !"".equals(keywords)){
					pd.put("keywords", keywords.trim());
				}
				page.setPd(pd);
				
				List<PageData> monthlist=paymentService.findRegTotallistMonthPage(page);
				mv.setViewName("system/account/appuser_order_statistics_month");
				
				mv.addObject("monthlist", monthlist);
				mv.addObject("pd", pd);
				mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
			} catch(Exception e){
				logger.error(e.getMessage());
			}
			return mv;
		}
}
