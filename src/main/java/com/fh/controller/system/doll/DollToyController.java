package com.fh.controller.system.doll;

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

import com.fh.entity.system.ToyType;
import com.fh.entity.system.Toy_type;
import com.fh.service.system.toy_type.Toy_typeManager;
import com.fh.service.system.toytype.ToyTypeManager;
import com.fh.vo.system.DollToyVo;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.service.system.doll.DollToyManager;
import com.fh.util.AppUtil;
import com.fh.util.Jurisdiction;
import com.fh.util.ObjectExcelView;
import com.fh.util.PageData;

/**
 * 说明：SYS_APP_DOLL_TOY
 * 创建人：FH Q313596790
 * 创建时间：2017-12-29
 */
@Controller
@RequestMapping(value = "/dolltoy")
public class DollToyController extends BaseController {

    String menuUrl = "dolltoy/list.do"; //菜单地址(权限用)
    @Resource(name = "dolltoyService")
    private DollToyManager dolltoyService;
    @Resource(name = "toy_typeService")
    private Toy_typeManager toy_typeService;

    @Resource(name = "toytypeService")
    private ToyTypeManager toytypeService;

    /**
     * 保存
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/save")
    public ModelAndView save() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "新增DollToy");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "add")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        dolltoyService.save(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 删除
     *
     * @param out
     * @throws Exception
     */
    @RequestMapping(value = "/delete")
    public void delete(PrintWriter out) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "删除DollToy");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return;
        } //校验权限
        PageData pd = new PageData();
        pd = this.getPageData();
        dolltoyService.delete(pd);
        out.write("success");
        out.close();
    }

    /**
     * 修改
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/edit")
    public ModelAndView edit() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "修改DollToy");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        dolltoyService.edit(pd);
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @RequestMapping(value = "/list")
    public ModelAndView list(Page page) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "列表DollToy");
        //if(!Jurisdiction.buttonJurisdiction(menuUrl, "cha")){return null;} //校验权限(无权查看时页面会有提示,如果不注释掉这句代码就无法进入列表页面,所以根据情况是否加入本句代码)
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String keywords = pd.getString("keywords");                //关键词检索条件
        if (null != keywords && !"".equals(keywords)) {
            pd.put("keywords", keywords.trim());
        }
        page.setPd(pd);
        List<PageData> varList = dolltoyService.list(page);    //列出DollToy列表
        mv.setViewName("system/doll/dolltoy_list");
        mv.addObject("varList", varList);
        mv.addObject("pd", pd);
        mv.addObject("QX", Jurisdiction.getHC());    //按钮权限
        return mv;
    }

    /**
     * 去新增页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goAdd")
    public ModelAndView goAdd() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        mv.setViewName("system/doll/dolltoy_edit");
        mv.addObject("msg", "save");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 去修改页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEdit")
    public ModelAndView goEdit() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = dolltoyService.findById(pd);    //根据ID读取
        mv.setViewName("system/doll/dolltoy_edit");
        mv.addObject("msg", "edit");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 去修改分类页面
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/goEditToyType")
    public ModelAndView goEditToyType() throws Exception {
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        pd = dolltoyService.findById(pd);    //根据ID读取
        
        List<PageData> typeList= toytypeService.listAll(new PageData());
        mv.setViewName("system/doll/dolltoytype_edit");
        mv.addObject("msg", "edit");
        mv.addObject("typeList", typeList);
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 修改
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editToyType")
    public ModelAndView editToyType(HttpServletRequest httpServletRequest) throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "修改DollToyType");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "edit")) {
            return null;
        } //校验权限
        ModelAndView mv = this.getModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        String toyId = httpServletRequest.getParameter("TOY_ID");
        DollToyVo dollToyVo = dolltoyService.getDollToyByToyId(toyId);
        String types = dollToyVo.getToy_type();
        String[] values = httpServletRequest.getParameterValues("TOY_TYPE_ID");
        //查询出关联表中所有的数据
        List<Toy_type> list = toy_typeService.getToy_TypeList(Integer.valueOf(toyId));
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        if (list.size() != 0) {
            //删除关联表中的数据
            toy_typeService.deleteToy_TypeAll(Integer.valueOf(toyId));

            if (values == null) {
                //删除该娃娃的的分类数据
                dollToyVo.setToy_type(null);
                dolltoyService.updateToyType(dollToyVo);
            } else {
                //增加关联表信息，添加分类
                for (int i = 0; i < values.length; i++) {
                    String ts = values[i];
                    Toy_type toy_type = new Toy_type();
                    toy_type.setTOY_ID(Integer.valueOf(toyId));
                    toy_type.setTOY_TYPE_ID(Integer.valueOf(ts));
                    toy_typeService.regToy_Type(toy_type);
                    sb.append(ts).append("，");
                  //  ToyType toyType =  toytypeService.getToyType(Integer.valueOf(ts));
                  //  String toyType1 =  toyType.getTOY_TYPE();
                  //  sb1.append(toyType1).append(",");

                }
               // sb1.deleteCharAt(sb1.length()-1);
                sb.deleteCharAt(sb.length() - 1);
                dollToyVo.setToy_type(sb.toString());
                dolltoyService.updateToyType(dollToyVo);
            }

        }else {
                //增加关联表信息，添加分类
                for (int i = 0; i < values.length; i++) {
                    String ts = values[i];
                    Toy_type toy_type = new Toy_type();
                    toy_type.setTOY_ID(Integer.valueOf(toyId));
                    toy_type.setTOY_TYPE_ID(Integer.valueOf(ts));
                    toy_typeService.regToy_Type(toy_type);
                    sb.append(ts).append("，");
                }
            sb.deleteCharAt(sb.length() - 1);
            dollToyVo.setToy_type(sb.toString());
            dolltoyService.updateToyType(dollToyVo);
            }
        mv.addObject("msg", "success");
        mv.setViewName("save_result");
        return mv;
    }

    /**
     * 批量删除
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/deleteAll")
    @ResponseBody
    public Object deleteAll() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "批量删除DollToy");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "del")) {
            return null;
        } //校验权限
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        pd = this.getPageData();
        List<PageData> pdList = new ArrayList<PageData>();
        String DATA_IDS = pd.getString("DATA_IDS");
        if (null != DATA_IDS && !"".equals(DATA_IDS)) {
            String ArrayDATA_IDS[] = DATA_IDS.split(",");
            dolltoyService.deleteAll(ArrayDATA_IDS);
            pd.put("msg", "ok");
        } else {
            pd.put("msg", "no");
        }
        pdList.add(pd);
        map.put("list", pdList);
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 导出到excel
     *
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/excel")
    public ModelAndView exportExcel() throws Exception {
        logBefore(logger, Jurisdiction.getUsername() + "导出DollToy到excel");
        if (!Jurisdiction.buttonJurisdiction(menuUrl, "cha")) {
            return null;
        }
        ModelAndView mv = new ModelAndView();
        PageData pd = new PageData();
        pd = this.getPageData();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        titles.add("玩具名称");    //1
        titles.add("库存数量");    //2
        titles.add("采购价格");    //3
        titles.add("抓取金币");    //4
        titles.add("备注");    //5
        titles.add("创建时间");    //6
        titles.add("更新时间");    //7
        dataMap.put("titles", titles);
        List<PageData> varOList = dolltoyService.listAll(pd);
        List<PageData> varList = new ArrayList<PageData>();
        for (int i = 0; i < varOList.size(); i++) {
            PageData vpd = new PageData();
            vpd.put("var1", varOList.get(i).getString("TOY_NAME"));        //1
            vpd.put("var2", varOList.get(i).get("TOY_NUM").toString());    //2
            vpd.put("var3", varOList.get(i).getString("BUY_PRICE"));        //3
            vpd.put("var4", varOList.get(i).get("DOOL_GOLD").toString());    //4
            vpd.put("var5", varOList.get(i).getString("REMARK"));        //5
            vpd.put("var6", varOList.get(i).getString("CREATE_TIME"));        //6
            vpd.put("var7", varOList.get(i).getString("UPDATE_TIME"));        //7
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView();
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(format, true));
    }
}
