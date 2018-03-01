package com.fh.controller.wwjapp;

import com.fh.controller.base.BaseController;
import com.fh.entity.system.RunImage;
import com.fh.service.system.runimage.RunImageManager;
import com.fh.util.wwjUtil.RespStatus;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/app/runimg")
public class AppRunImgController extends BaseController {
    @Resource(name="runimageService")
    private RunImageManager runimageService;

    @RequestMapping(value = "/getRunImage", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getRunImage(){
        try{
            List<RunImage> list = runimageService.getRunImageList();
            Map<String,Object> map = new HashMap<>();
            map.put("runImage",list);
            return RespStatus.successs().element("data",map);

        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }

    }
    
    
    @RequestMapping(value = "/images/news",produces = "application/json;charset=UTF-8")
    public ModelAndView news(String id){
    	ModelAndView mv = this.getModelAndView();
        try{
        	RunImage runImage = runimageService.getRunImageById(id);
        	mv.setViewName("system/runimage/runimage_news");
			mv.addObject("runImage",runImage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mv;
    }


}
