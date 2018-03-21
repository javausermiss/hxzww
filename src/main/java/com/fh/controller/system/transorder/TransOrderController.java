package com.fh.controller.system.transorder;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.entity.system.BankCard;
import com.fh.entity.system.TransOrder;
import com.fh.service.system.bankcard.BankCardManager;
import com.fh.service.system.trans.TransOrderManager;
import com.fh.util.Const;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;

/** 
 * 说明：分类
 * 创建人：FH Q313596790
 * 创建时间：2018-01-23
 */
@Controller
@RequestMapping(value="/trans/order")
public class TransOrderController extends BaseController {
	
	String menuUrl = "trans/order/list.do"; //菜单地址(权限用)
	
	
	@Resource(name="transOrderService")
	private TransOrderManager transOrderService;
	
	@Resource(name="bankcardService")
	private BankCardManager bankcardService;

	
	/** 提现申请，后台管理列表页面
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/queryWithdrawCashlist")
	public ModelAndView queryWithdrawCashlist(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表transorder");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		pd.put("TRANS_TYPE", Const.AccountTransType.TRANS_5000.getValue());
		page.setPd(pd);
		List<PageData>	varList = transOrderService.list(page);	//列出ToyType列表
		mv.setViewName("system/transorder/transorder_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditOrder")
	public ModelAndView goEdit(
			HttpServletRequest request,
			@RequestParam(value="orderId" ,required = true) String orderId
			)throws Exception{
		ModelAndView mv = this.getModelAndView();
		
		TransOrder transOrder = transOrderService.findById(orderId);	//根据ID读取
		BankCard bankCard=bankcardService.getBankInfByUserId(transOrder.getUserId()); //当前用户ID 查询银行账户信息
	
		mv.setViewName("system/transorder/transorder_edit");
		mv.addObject("msg", "edit");
		mv.addObject("transOrder", transOrder);
		mv.addObject("bankCard", bankCard);
		return mv;
	}	
	
	/**财务修改订单打款状态
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editWithdrawCashSt")
	public ModelAndView edit(HttpServletRequest req) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"打款操作 editWithdrawCashSt");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		String orderId=req.getParameter("orderId");
		TransOrder transOrder = transOrderService.findById(orderId);	//根据ID读取
		
		String dmsRelatedKey=req.getParameter("dmsRelatedKey");
		String orderSt=req.getParameter("orderSt");
		
		transOrder.setDmsRelatedKey(dmsRelatedKey);
		transOrder.setOrderSt(orderSt);
		transOrderService.editTransOrderSt(transOrder);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}

}
