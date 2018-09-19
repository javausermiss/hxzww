package com.fh.controller.system.goldgoods;

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
import com.fh.entity.system.GoldGoods;
import com.fh.service.system.goldgoods.GoldGoodsManager;
import com.fh.util.AppUtil;
import com.fh.util.FastDFSClient;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;

/** 
 * 说明：金币商城
 * 创建人：FH Q313596790
 * 创建时间：2018-09-18
 */
@Controller
@RequestMapping(value="/goldgoods")
public class GoldGoodsController extends BaseController {
	
	String menuUrl = "goldgoods/list.do"; //菜单地址(权限用)
	@Resource(name="goldgoodsService")
	private GoldGoodsManager goldgoodsService;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	public ModelAndView save(
			 @RequestParam(value = "IMGURL")CommonsMultipartFile multipartFile,
			 @RequestParam(value = "IMGURL_GOODSDETAIL_TOP") CommonsMultipartFile multipartFile_top,
			 @RequestParam(value = "IMGURL_GOODSDETAIL_MID") CommonsMultipartFile multipartFile_mid,
			 HttpServletRequest req
			 ) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"新增GoldGoods");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "add")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		//文件上传
				String fileId="";
				String fileId_top = "";
				String fileId_mid = "";

				try{
					String newFilename=multipartFile.getOriginalFilename();
					DiskFileItem fi = (DiskFileItem) multipartFile.getFileItem();
					File file = fi.getStoreLocation();
					fileId = FastDFSClient.uploadFile(file, newFilename);
					logger.info("---------fileId-------------"+fileId);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				try{
					String newFilename=multipartFile_top.getOriginalFilename();
					DiskFileItem fi = (DiskFileItem) multipartFile_top.getFileItem();
					File file = fi.getStoreLocation();
					fileId_top = FastDFSClient.uploadFile(file, newFilename);
					logger.info("---------fileId_top-------------"+fileId_top);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				try{
					String newFilename_mid=multipartFile_mid.getOriginalFilename();
					DiskFileItem fi = (DiskFileItem) multipartFile_mid.getFileItem();
					File file = fi.getStoreLocation();
					fileId_mid = FastDFSClient.uploadFile(file, newFilename_mid);
					logger.info("---------fileId_mid-------------"+fileId_mid);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				pd.put("GOLDGOODS_ID", this.get32UUID());	//主键
				pd.put("IMGURL",fileId);
				pd.put("IMGURL_GOODSDETAIL_TOP",fileId_top);
				pd.put("IMGURL_GOODSDETAIL_MID",fileId_mid);
				pd.put("GOODSNUM",req.getParameter("GOODSNUM"));
				pd.put("GOODSNAME",req.getParameter("GOODSNAME"));
				pd.put("POINTS",req.getParameter("POINTS"));
				pd.put("ORIGINALVALUEOFGOODS",req.getParameter("ORIGINALVALUEOFGOODS"));
				pd.put("SHOWTAG",req.getParameter("SHOWTAG"));
				goldgoodsService.save(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"删除GoldGoods");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return;} //校验权限
		PageData pd = new PageData();
		pd = this.getPageData();
		goldgoodsService.delete(pd);
		out.write("success");
		out.close();
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	public ModelAndView edit(HttpServletRequest req,
			 @RequestParam(value = "IMGURL", required = false)CommonsMultipartFile multipartFile,
			 @RequestParam(value = "IMGURL_GOODSDETAIL_TOP") CommonsMultipartFile multipartFile_top,
			 @RequestParam(value = "IMGURL_GOODSDETAIL_MID") CommonsMultipartFile multipartFile_mid
			 ) throws Exception{
		logBefore(logger, Jurisdiction.getUsername()+"修改PointsGoods");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "edit")){return null;} //校验权限
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		GoldGoods goldGoods = goldgoodsService.getById(req.getParameter("GOLDGOODS_ID"));
		String IMGU =  goldGoods.getImgUrl();
		String top = goldGoods.getImgUrl_goodsDetail_top();
		String mid = goldGoods.getImgUrl_goodsDetail_mid();
		//上传商品图像
		String newFilename=multipartFile.getOriginalFilename();
		DiskFileItem fi = (DiskFileItem) multipartFile.getFileItem();
		File file = fi.getStoreLocation();
		//文件上传，编辑操作
		String fileId="";
		if (goldGoods !=null && IMGU==null){
			try{
				fileId = FastDFSClient.uploadFile(file, newFilename);
				logger.info("---------fileId-------------"+fileId);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else{
			//判断当前文件是否为空
			if(file !=null && !multipartFile.isEmpty() && multipartFile.getSize() >0){
				fileId = FastDFSClient.modifyFile(IMGU, file, newFilename);
			}else{
				fileId=IMGU;
			}
		}
		//上传商品详情头部图像
		String newFilename_top=multipartFile_top.getOriginalFilename();
		DiskFileItem fi_top = (DiskFileItem) multipartFile.getFileItem();
		File file_top = fi_top.getStoreLocation();
		//文件上传，编辑操作
		String fileId_top="";
		if (goldGoods !=null && goldGoods.getImgUrl_goodsDetail_top()==null){
			try{
				fileId_top = FastDFSClient.uploadFile(file_top, newFilename_top);
				logger.info("---------fileId_top-------------"+fileId_top);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else{
			//判断当前文件是否为空
			if(file_top !=null && !multipartFile.isEmpty() && multipartFile.getSize() >0){
				fileId_top = FastDFSClient.modifyFile(top, file_top, newFilename_top);
			}else{
				fileId_top=top;
			}
		}
		//上传商品详情中部图像
		String newFilename_mid=multipartFile_mid.getOriginalFilename();
		DiskFileItem fi_mid = (DiskFileItem) multipartFile_mid.getFileItem();
		File file_mid = fi_mid.getStoreLocation();
		//文件上传，编辑操作
		String fileId_mid="";
		if (goldGoods !=null && goldGoods.getImgUrl_goodsDetail_mid()==null){
			try{
				fileId_mid = FastDFSClient.uploadFile(file_mid, newFilename_mid);
				logger.info("---------fileId-------------"+fileId_mid);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}else{
			//判断当前文件是否为空
			if(file_mid !=null && !multipartFile_mid.isEmpty() && multipartFile_mid.getSize() >0){
				fileId_mid = FastDFSClient.modifyFile(IMGU, file_mid, newFilename_mid);
			}else{
				fileId_mid=mid;
			}
		}
		pd.put("IMGURL",fileId);
		pd.put("IMGURL_GOODSDETAIL_TOP",fileId_top);
		pd.put("IMGURL_GOODSDETAIL_MID",fileId_mid);
		pd.put("GOODSNUM",req.getParameter("GOODSNUM"));
		pd.put("GOODSNAME",req.getParameter("GOODSNAME"));
		pd.put("POINTS",req.getParameter("POINTS"));
		pd.put("ORIGINALVALUEOFGOODS",req.getParameter("ORIGINALVALUEOFGOODS"));
		pd.put("SHOWTAG",req.getParameter("SHOWTAG"));
		pd.put("GOLDGOODS_ID",req.getParameter("GOLDGOODS_ID"));
		goldgoodsService.edit(pd);
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
		logBefore(logger, Jurisdiction.getUsername()+"列表GoldGoods");
		//if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
		ModelAndView mv = this.getModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String keywords = pd.getString("keywords");				//关键词检索条件
		if(null != keywords && !"".equals(keywords)){
			pd.put("keywords", keywords.trim());
		}
		page.setPd(pd);
		List<PageData>	varList = goldgoodsService.list(page);	//列出GoldGoods列表
		mv.setViewName("system/goldgoods/goldgoods_list");
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
		mv.setViewName("system/goldgoods/goldgoods_edit");
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
		pd = goldgoodsService.findById(pd);	//根据ID读取
		mv.setViewName("system/goldgoods/goldgoods_edit");
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
		logBefore(logger, Jurisdiction.getUsername()+"批量删除GoldGoods");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "del")){return null;} //校验权限
		PageData pd = new PageData();		
		Map<String,Object> map = new HashMap<String,Object>();
		pd = this.getPageData();
		List<PageData> pdList = new ArrayList<PageData>();
		String DATA_IDS = pd.getString("DATA_IDS");
		if(null != DATA_IDS && !"".equals(DATA_IDS)){
			String ArrayDATA_IDS[] = DATA_IDS.split(",");
			goldgoodsService.deleteAll(ArrayDATA_IDS);
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
		logBefore(logger, Jurisdiction.getUsername()+"导出GoldGoods到excel");
		if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;}
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("编号");	//1
		titles.add("商品名");	//2
		titles.add("图片地址");	//3
		titles.add("类型");	//4
		titles.add("商品详情头部");	//5
		titles.add("商品详情中部");	//6
		titles.add("商品原始价值");	//7
		titles.add("是否展示");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = goldgoodsService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).get("GOODSNUM").toString());	//1
			vpd.put("var2", varOList.get(i).getString("GOODSNAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("IMGURL"));	    //3
			vpd.put("var4", varOList.get(i).get("POINTS").toString());	//4
			vpd.put("var5", varOList.get(i).getString("IMGURL_GOODSDETAIL_TOP"));	    //5
			vpd.put("var6", varOList.get(i).getString("IMGURL_GOODSDETAIL_MID"));	    //6
			vpd.put("var7", varOList.get(i).get("ORIGINALVALUEOFGOODS").toString());	//7
			vpd.put("var8", varOList.get(i).getString("SHOWTAG"));	    //8
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
