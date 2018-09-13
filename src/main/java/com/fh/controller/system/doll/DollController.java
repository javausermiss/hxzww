package com.fh.controller.system.doll;

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
import com.fh.entity.system.Doll;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.doll.DollToyManager;
import com.fh.util.AppUtil;
import com.fh.util.DateUtil;
import com.fh.util.FastDFSClient;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.RedisUtil;


/** 
 * 说明：娃娃机处理类
 * 创建时间：2017-10-27
 */
@Controller
@RequestMapping(value="/doll")
public class DollController extends BaseController {
	
	String menuUrl = "doll/list.do"; //菜单地址(权限用)
	@Resource(name="dollService")
	private DollManager dollService;
	
	
	@Resource(name="dolltoyService")
	private DollToyManager dolltoyService;

	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save(HttpServletRequest req,
			@RequestParam(value = "DOLL_FILE", required = false)CommonsMultipartFile  multipartFile //保存图片文件上传路径
			) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增Doll");
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
		String doll_sn = req.getParameter("DOLL_SN");
		String doll_name = req.getParameter("DOLL_NAME");
		String doll_gold = req.getParameter("DOLL_GOLD");
		String doll_conversiongold = req.getParameter("DOLL_CONVERSIONGOLD");
		String release_status = req.getParameter("RELEASE_STATUS");
//		pd = this.getPageData();
		pd.put("DOLL_ID", this.get32UUID());	//主键
		pd.put("DOLL_STATE", "2");	//DOLL_ST;ATE
		pd.put("DOLL_URL", fileId);
		
		pd.put("DOLL_SN", doll_sn);
		pd.put("DOLL_NAME", doll_name);
		pd.put("DOLL_GOLD", doll_gold);
		pd.put("DOLL_CONVERSIONGOLD", doll_conversiongold);
		pd.put("RELEASE_STATUS", release_status);
		pd.put("TOY_ID", req.getParameter("TOY_ID"));
		pd.put("ROOM_ID", req.getParameter("ROOM_ID"));
		pd.put("DOLL_TYPE", req.getParameter("DOLL_TYPE"));
		pd.put("MACHINE_TYPE", req.getParameter("MACHINE_TYPE"));
		pd.put("SERIAL_NUMBER", req.getParameter("SERIAL_NUMBER"));
		dollService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除Doll");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		dollService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(HttpServletRequest req,
							 @RequestParam(value = "DOLL_FILE", required = false)CommonsMultipartFile  multipartFile //保存图片文件上传路径
							 ) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改Doll");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		
		//获取当前编辑的对象
		Doll doll=dollService.getDollByID(req.getParameter("DOLL_ID"));
		
		//上传的文件
		String newFilename=multipartFile.getOriginalFilename();
		DiskFileItem fi = (DiskFileItem) multipartFile.getFileItem();
		File file = fi.getStoreLocation();
		
		
		//文件上传，编辑操作
		String fileId="";
		if (doll !=null && doll.getDOLL_URL()==null){
			try{
				fileId = FastDFSClient.uploadFile(file, newFilename);
				logger.info("---------fileId-------------"+fileId);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else{
			 //判断当前文件是否为空
			if(file !=null && !multipartFile.isEmpty() && multipartFile.getSize() >0){
				fileId = FastDFSClient.modifyFile(doll.getDOLL_URL(), file, newFilename);
			}else{
				//
				fileId=doll.getDOLL_URL();
			}
		}
		
		//更新数据
		PageData pd = new PageData();
		pd.put("DOLL_ID", req.getParameter("DOLL_ID"));
		pd.put("DOLL_SN", req.getParameter("DOLL_SN"));
		pd.put("DOLL_NAME", req.getParameter("DOLL_NAME"));
		pd.put("DOLL_GOLD", req.getParameter("DOLL_GOLD"));
		pd.put("DOLL_CONVERSIONGOLD", req.getParameter("DOLL_CONVERSIONGOLD"));
		pd.put("RELEASE_STATUS", req.getParameter("RELEASE_STATUS"));
		pd.put("TOY_ID", req.getParameter("TOY_ID"));
		pd.put("DOLL_URL", fileId);
		pd.put("ROOM_ID", req.getParameter("ROOM_ID"));
		pd.put("DOLL_TYPE", req.getParameter("DOLL_TYPE"));
		pd.put("COINPUSHER_REWORD",req.getParameter("COINPUSHER_REWORD"));
		pd.put("MACHINE_TYPE", req.getParameter("MACHINE_TYPE"));
		pd.put("SERIAL_NUMBER", req.getParameter("SERIAL_NUMBER"));
		dollService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表Doll");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = dollService.list(page);	//列出Doll列表
		
		
		//娃娃机概率
		for (PageData dollVo : varList) {
	        String prob =  RedisUtil.getStr("roomProbability:" + dollVo.getString("DOLL_ID"));
	        if (prob != null){
	        	dollVo.put("ROOMPROBABILITY", prob);
	        }
		 }
        
		mv.setViewName("system/doll/doll_list");
		mv.addObject("varList", varList);
		mv.addObject("pd", pd);
		mv.addObject("QX",Jurisdiction.getHC());	//按钮权限
		return mv;
	}
	
	
	
	/** 娃娃机游戏统计列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/probabilitylist")
	public ModelAndView probabilitylist(Page page) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"列表probabilitylist");
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		
		String lastStart = pd.getString("lastStart");
		String lastEnd  = pd.getString("lastEnd");
		
		if(lastStart != null && !"".equals(lastStart)){
			pd.put("lastStart", lastStart+" 00:00:00");
		}else{
			pd.put("lastStart", DateUtil.getDay()+" 00:00:00");
		}
		
		if(lastEnd != null && !"".equals(lastEnd)){
			pd.put("lastEnd", lastEnd+" 23:59:59");
		}else{
			pd.put("lastEnd", DateUtil.getDay()+" 23:59:59");
		}
		
		page.setPd(pd);
		List<PageData>	varList = dollService.getDollCountlist(page);	//列出Doll列表
		mv.setViewName("system/doll/doll_game_list");
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
		mv.setViewName("system/doll/doll_edit");
		List<PageData>	toyList = dolltoyService.listAll(getPageData());
		
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		mv.addObject("toyList", toyList);
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
		pd = dollService.findById(pd);	//根据ID读取
		List<PageData>	toyList = dolltoyService.listAll(getPageData());
		mv.setViewName("system/doll/doll_edit");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		mv.addObject("toyList", toyList);
		return mv;
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	public Object deleteAll() throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"批量删除Doll");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			dollService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出Doll到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("SN");	//1
		titles.add("DOLL_NAME");	//2
		titles.add("ROOM_ID");	//3
		titles.add("DOLL_STATE");	//4
		dataMap.put("titles", titles);
		List<PageData> varOList = dollService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("DOLL_SN"));	    //1
			vpd.put("var2", varOList.get(i).getString("DOLL_NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("ROOM_ID"));	    //3
			vpd.put("var4", varOList.get(i).getString("DOLL_STATE"));	    //4
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
