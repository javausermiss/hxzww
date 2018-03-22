package com.fh.controller.system.runimage;

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

import com.fh.entity.system.RunImage;
import com.fh.util.*;
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
import com.fh.service.system.runimage.RunImageManager;

/** 
 * 说明：首页滚动图
 * 创建人：FH Q313596790
 * 创建时间：2018-01-09
 */
@Controller
@RequestMapping(value="/runimage")
public class RunImageController extends BaseController {
	
	String menuUrl = "runimage/list.do"; //菜单地址(权限用)
	@Resource(name="runimageService")
	private RunImageManager runimageService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save(
			HttpServletRequest req,
			@RequestParam(value = "RUN_FILE", required = false)CommonsMultipartFile multipartFile
	) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增RunImage");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
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
		pd.put("RUNIMAGE_ID", this.get32UUID());	//主键
		pd.put("RUN_NAME",req.getParameter("RUN_NAME"));
		pd.put("IMAGE_URL",fileId);
		pd.put("CONTENT",req.getParameter("CONTENT"));
		pd.put("TIME",req.getParameter("TIME"));
		pd.put("HREF_ST",req.getParameter("HREF_ST"));
		pd.put("LIVESTREAM",req.getParameter("LIVESTREAM"));
		pd.put("SERVER_NAME",req.getParameter("SERVER_NAME"));
		pd.put("RTMP_URL",req.getParameter("RTMP_URL"));
		pd.put("H5_URL",req.getParameter("H5_URL"));
		pd.put("DEVICE_STATE",req.getParameter("DEVICE_STATE"));
		pd.put("STATE",req.getParameter("STATE"));
		runimageService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除RunImage");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		runimageService.delete(pd);
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
			@RequestParam(value = "RUN_FILE", required = false)CommonsMultipartFile multipartFile
	) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改RunImage");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		
		RunImage runImage =  runimageService.getRunImageById(req.getParameter("RUNIMAGE_ID"));

		//上传的文件
		String newFilename=multipartFile.getOriginalFilename();
		DiskFileItem fi = (DiskFileItem) multipartFile.getFileItem();
		File file = fi.getStoreLocation();


		//文件上传，编辑操作
		String fileId="";
		if (runImage !=null && runImage.getIMAGE_URL()==null){
			try{
				fileId = FastDFSClient.uploadFile(file, newFilename);
				logger.info("---------fileId-------------"+fileId);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else{
			//判断当前文件是否为空
			if(file !=null && !multipartFile.isEmpty() && multipartFile.getSize() >0){
				fileId = FastDFSClient.modifyFile(runImage.getIMAGE_URL(), file, newFilename);
			}else{
				//
				fileId=runImage.getIMAGE_URL();
			}
		}
		PageData pd = new PageData();
		pd.put("RUNIMAGE_ID",req.getParameter("RUNIMAGE_ID"));
		pd.put("RUN_NAME",req.getParameter("RUN_NAME"));
		pd.put("IMAGE_URL",fileId);
		pd.put("CONTENT",req.getParameter("CONTENT"));
		pd.put("TIME",req.getParameter("TIME"));
		pd.put("HREF_ST",req.getParameter("HREF_ST"));
		pd.put("LIVESTREAM",req.getParameter("LIVESTREAM"));
		pd.put("SERVER_NAME",req.getParameter("SERVER_NAME"));
		pd.put("RTMP_URL",req.getParameter("RTMP_URL"));
		pd.put("H5_URL",req.getParameter("H5_URL"));
		pd.put("DEVICE_STATE",req.getParameter("DEVICE_STATE"));
		pd.put("STATE",req.getParameter("STATE"));
		runimageService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表RunImage");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = runimageService.list(page);	//列出RunImage列表
		mv.setViewName("system/runimage/runimage_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	
	/**直播列表
	 * @param page
	 * @throws Exception
	 */
	
	
	
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("system/runimage/runimage_edit");
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
		pd = runimageService.findById(pd);	//根据ID读取
		mv.setViewName("system/runimage/runimage_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除RunImage");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			runimageService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出RunImage到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("图像地址");	//1
		titles.add("图片名称");	//2
		dataMap.put("titles", titles);
		List<PageData> varOList = runimageService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("IMAGE_URL"));	    //1
			vpd.put("var2", varOList.get(i).getString("RUN_NAME"));	    //2
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
