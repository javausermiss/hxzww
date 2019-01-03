package com.fh.controller.system.tgappuser;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.util.Jurisdiction;
import com.fh.util.PageData;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping(value="/tgappuser")
public class TgAppUserController extends BaseController {
    String menuUrl = "tgappuser/list.do"; //菜单地址(权限用)

    @Resource(name="appuserService")
    private AppuserManager appuserService;

    @Resource(name="paymentService")
    private PaymentManager paymentService;

    /**推广用户列表
     * @param page
     * @throws Exception
     */
    @RequestMapping(value="/list")
    public ModelAndView list(Page page) throws Exception{
        logger.info("------------------------------------->进入控制类");
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");				//关键词检索条件
        if(null != keywords && !"".equals(keywords)){
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData>	varList = appuserService.gettgUser(page);
        mv.setViewName("system/tgappuser/tgappuser_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
        return mv;
    }


    /**下级用户列表
     * @param page
     * @throws Exception
     */
    @RequestMapping(value="/proUserList")
    public ModelAndView proUserList(Page page) throws Exception{
        logBefore(logger, Jurisdiction.getUsername()+"列表proUserList");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");				//关键词检索条件
        if(null != keywords && !"".equals(keywords)){
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData>	userList = appuserService.getxxUser(page);		//列出会员列表
        mv.setViewName("system/tgappuser/xxappuser_list");
        mv.addObject("varList", userList);
        mv.addObject("pd", pd);
        mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
        return mv;
    }


    /**下级用户充值列表
     * @param page
     * @throws Exception
     */
    @RequestMapping(value="/proUserListPay")
    public ModelAndView proUserListPay(Page page) throws Exception{

        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");				//关键词检索条件
        if(null != keywords && !"".equals(keywords)){
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varlist=paymentService.getUserOrderList(page);
        mv.setViewName("system/account/appuser_order_list");
        mv.addObject("varlist", varlist);
        mv.addObject("pd", pd);
        mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
        return mv;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
    }





}
