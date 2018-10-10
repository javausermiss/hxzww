package com.fh.controller.system.rewardgoldmanager;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import com.fh.entity.system.AppUser;
import com.fh.service.system.appuser.AppuserManager;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.util.AppUtil;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.Jurisdiction;
import com.fh.util.Tools;
import com.fh.service.system.rewardgoldmanager.RewardGoldManagerManager;

/** 
 * 说明：动态管理次日赠送金币数
 * 创建人：FH Q313596790
 * 创建时间：2018-09-29
 */
@Controller
@RequestMapping(value="/rewardgoldmanager")
public class RewardGoldManagerController extends BaseController {
	
	String menuUrl = "rewardgoldmanager/list.do"; //菜单地址(权限用)
	@Resource(name="rewardgoldmanagerService")
	private RewardGoldManagerManager rewardgoldmanagerService;

	@Resource(name = "appuserService")
	private AppuserManager appuserService;


	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增RewardGoldManager");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("REWARDGOLDMANAGER_ID", this.get32UUID());	//主键
		pd.put("UPDATETIME", Tools.date2Str(new Date()));	//更新时间
		pd.put("SUPPORTNUM", "0");	//点赞数
		rewardgoldmanagerService.save(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	public void delete(PrintWriter out) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"删除RewardGoldManager");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		rewardgoldmanagerService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改RewardGoldManager");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		rewardgoldmanagerService.edit(pd);

		//修改所有用户的点赞标签
		List<AppUser> list = appuserService.getAppUserList();
		for (int i = 0; i < list.size(); i++) {
			AppUser appUser = list.get(i);
			appUser.setSUPPORTTAG("0");
			appuserService.updateAppUserSupt(appUser);
		}

		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}

	/**修改金币
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/editGold")
	public ModelAndView editGold() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改RewardGoldManager");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		rewardgoldmanagerService.editGold(pd);
		mv.addObject("msg","success");
		mv.setViewName("save_result");
		return mv;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表RewardGoldManager");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = rewardgoldmanagerService.list(page);	//列出RewardGoldManager列表
		mv.setViewName("system/rewardgoldmanager/rewardgoldmanager_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/rewardgoldmanager/rewardgoldmanager_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改笑话页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = rewardgoldmanagerService.findById(pd);	//根据ID读取
		mv.setViewName("system/rewardgoldmanager/rewardgoldmanager_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}

	/**去修改金币页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEditGold")
	public ModelAndView goEditGold()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = rewardgoldmanagerService.findById(pd);	//根据ID读取
		mv.setViewName("system/rewardgoldmanager/rewardgoldmanager_edit_gold");
		mv.addObject("msg", "editGold");
		mv.addObject("pd", pd);
		return mv;
	}

	/**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除RewardGoldManager");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			rewardgoldmanagerService.deleteAll(ArrayDATA_IDS);
			pd.put("msg", "ok");
		}else{
			pd.put("msg", "no");
		}
		pdList.add(pd);
		map.put("list", pdList);
		return AppUtil.returnObject(pd, map);
	}
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	public ModelAndView exportExcel() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"导出RewardGoldManager到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("次日赠送金币数");	//1
		titles.add("更新时间");	//2
		titles.add("每日笑话");	//3
		titles.add("点赞数");	//4
		dataMap.put("titles", titles);
		List<PageData> varOList = rewardgoldmanagerService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("GOLD").toString());	//1
			vpd.put("var2", varOList.get(i).getString("UPDATETIME"));	    //2
			vpd.put("var3", varOList.get(i).getString("WORD"));	    //3
			vpd.put("var4", varOList.get(i).get("SUPPORTNUM").toString());	//4
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
