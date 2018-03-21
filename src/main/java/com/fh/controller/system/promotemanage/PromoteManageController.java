package com.fh.controller.system.promotemanage;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.promotemanage.PromoteManageManager;
import com.fh.util.AppUtil;
import com.fh.util.FastDFSClient;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;

/** 
 * 说明：渠道推广
 * 创建人：FH Q313596790
 * 创建时间：2018-03-07
 */
@Controller
@RequestMapping(value="/promotemanage")
public class PromoteManageController extends BaseController {
	
	String menuUrl = "promotemanage/list.do"; //菜单地址(权限用)
	@Resource(name="promotemanageService")
	private PromoteManageManager promotemanageService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save(
			HttpServletRequest req,
			@RequestParam(value = "IMG_FILE", required = false)CommonsMultipartFile  multipartFile //保存图片文件上传路径
		) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增PromoteManage");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		
		String PRO_MANAGE_NAME = req.getParameter("PRO_MANAGE_NAME");
		String PAY_GOLD = req.getParameter("PAY_GOLD");
		String CONVER_GOLD = req.getParameter("CONVER_GOLD");
		String RETURN_RATIO = req.getParameter("RETURN_RATIO");
		
		PageData pd = new PageData();
		pd = this.getPageData();
		
		//文件上传
		String fileId="";
		try{
			String newFilename=multipartFile.getOriginalFilename();
			DiskFileItem fi = (DiskFileItem) multipartFile.getFileItem();
			File file = fi.getStoreLocation();
			fileId = FastDFSClient.uploadFile(file, newFilename);
			logger.info("---------fileId-------------"+fileId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		pd.put("PRO_MANAGE_NAME", PRO_MANAGE_NAME);
		pd.put("IMG_URL", fileId);//图片地址
		pd.put("PAY_GOLD", PAY_GOLD);//支付金额
		pd.put("CONVER_GOLD", CONVER_GOLD);//推广用户兑换金币数量
		pd.put("PRO_TYPE","1");
		pd.put("RETURN_RATIO", RETURN_RATIO);//权益比例
		
		promotemanageService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除PromoteManage");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		promotemanageService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(
			HttpServletRequest req,
			@RequestParam(value = "IMG_FILE", required = false)CommonsMultipartFile  multipartFile //保存图片文件上传路径
		) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改PromoteManage");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		
		String PRO_MANAGE_NAME = req.getParameter("PRO_MANAGE_NAME");
		String PAY_GOLD = req.getParameter("PAY_GOLD");
		String CONVER_GOLD = req.getParameter("CONVER_GOLD");
		String RETURN_RATIO = req.getParameter("RETURN_RATIO");
		
		PageData  managePd=promotemanageService.findById(req.getParameter("PRO_MANAGE_ID"));
		
		
		//上传的文件
		String newFilename=multipartFile.getOriginalFilename();
		DiskFileItem fi = (DiskFileItem) multipartFile.getFileItem();
		File file = fi.getStoreLocation();
		
		//文件上传，编辑操作
		String fileId="";
		if (managePd !=null && managePd.get("IMG_URL")==null){
			try{
				fileId = FastDFSClient.uploadFile(file, newFilename);
				logger.info("---------fileId-------------"+fileId);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else{
			 //判断当前文件是否为空
			if(file !=null && !multipartFile.isEmpty() && multipartFile.getSize() >0){
				fileId = FastDFSClient.modifyFile(String.valueOf(managePd.get("IMG_URL")), file, newFilename);
			}
		}
		managePd.put("PRO_MANAGE_NAME", PRO_MANAGE_NAME);		
		managePd.put("IMG_URL", fileId);//图片地址
		managePd.put("PAY_GOLD", PAY_GOLD);//支付金额
		managePd.put("CONVER_GOLD", CONVER_GOLD);//推广用户兑换金币数量
		managePd.put("RETURN_RATIO", RETURN_RATIO);//权益比例
		
		promotemanageService.edit(managePd);
		
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
		logBefore(logger, Jurisdiction.getUsername()+"列表PromoteManage");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = promotemanageService.list(page);	//列出PromoteManage列表
		mv.setViewName("system/promotemanage/promotemanage_list");
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
		mv.setViewName("system/promotemanage/promotemanage_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = promotemanageService.findById(pd.getString("PRO_MANAGE_ID"));	//根据ID读取
		mv.setViewName("system/promotemanage/promotemanage_edit");
		mv.addObject("msg", "edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除PromoteManage");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			promotemanageService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出PromoteManage到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("支付金额");	//1
		titles.add("获取金币数");	//2
		titles.add("图片地址");	//3
		titles.add("类型");	//4
		titles.add("返回比例");	//5
		titles.add("创建时间");	//6
		titles.add("更新时间");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = promotemanageService.listAll();
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("PAY_AMOUNT"));	    //1
			vpd.put("var2", varOList.get(i).getString("GOLD"));	    //2
			vpd.put("var3", varOList.get(i).get("IMG_URL").toString());	//3
			vpd.put("var4", varOList.get(i).getString("TYPE"));	    //4
			vpd.put("var5", varOList.get(i).getString("RETURN_RATIO"));	    //5
			vpd.put("var6", varOList.get(i).getString("CREATE_TIME"));	    //6
			vpd.put("var7", varOList.get(i).getString("UPDATE_TIME"));	    //7
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
