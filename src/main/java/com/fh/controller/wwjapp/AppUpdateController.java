package com.fh.controller.wwjapp;

import com.fh.controller.base.BaseController;
import com.fh.entity.system.AppVersion;
import com.fh.service.system.appversion.AppVersionManager;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.RespStatus;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/app/version")
public class AppUpdateController extends BaseController {

    @Resource(name="appversionService")
    private AppVersionManager appversionService;


    @RequestMapping(value = "/checkVersion", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject checkVersion() {

        try {
            //查询最新可用的版本号
            AppVersion appVersion =  appversionService.getNewVersion();
            String new_version  = appVersion.getVERSION();
            Map<String,Object> map  = new HashMap<>();
            map.put("appVersion",appVersion);
            return RespStatus.successs().element("data",appVersion);

        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }
    }
    
    
    
    @RequestMapping(value="/versionInformation", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject versionInformation (@RequestParam("version")String version)
    {
    	  try {
    		  //根据版本号查询
    		  AppVersion versiontag= appversionService.findByVersion(version);
    		  Map<String,Object> map  = new HashMap<>();
    		  map.put("version", versiontag);
    		  return RespStatus.successs().element("data",versiontag);
		} catch (Exception e) {
			e.printStackTrace();
			return RespStatus.fail();
		}
    }

    
    @RequestMapping(value = "/versionDisplay", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject versionDisplay() {

        try {
           
            AppVersion appVersion =  appversionService.versionDisplay();
            Map<String,Object> map  = new HashMap<>();
            map.put("appVersion",appVersion);
            return RespStatus.successs().element("data",appVersion);

        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }
    }
    
    
}
