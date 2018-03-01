package com.fh.controller.gateway;

import com.fh.controller.base.BaseController;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.util.AppUtil;
import com.fh.util.PageData;
import com.fh.util.Tools;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**@author FH Q313596790
  * 会员-接口类 
  * 相关参数协议：
  * 00	请求失败
  * 01	请求成功
  * 02	返回空值
  * 03	请求协议参数不完整    
  * 04  用户名或密码错误
  * 05  FKEY验证失败
 */
@Controller
@RequestMapping(value="/api/gatewayuser")
public class ApiIntGatewayuserController extends BaseController {
    
	@Resource(name="appuserService")
	private AppuserManager appuserService;

	@RequestMapping(value="/login")
	@ResponseBody
	public Object login(){
		logBefore(logger, "网关登录");
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String result = "00";
		try{
			if(!Tools.checkKey("USERNAME", pd.getString("FKEY"))){	//检验请求key值是否合法
				if(AppUtil.checkParam("login", pd)){	//检查参数
					String password = new SimpleHash("SHA-1", pd.getString("USERNAME"), pd.getString("PASSWORD")).toString();
					pd = appuserService.findByUsername(pd);
					String apikey = pd.getString("APIKEY");

					PageData rpd = new PageData();
					if (password.equals(pd.getString("PASSWORD"))) {
						if(StringUtils.isNotBlank(apikey)) {
							rpd.put("APIKEY", apikey);
						} else {
							apikey = this.getUUID();
							pd.put("APIKEY", apikey);
							appuserService.editU(pd);
							rpd.put("APIKEY", apikey);
						}
						map.put("pd", rpd);
						result = (null == pd) ?  "02" :  "01";
					} else {
						map.put("pd", null);
						result = "02";
					}

				}else {
					result = "03";
				}
			}else{
				result = "05";
			}
		}catch (Exception e){
			logger.error(e.toString(), e);
		}finally{
			map.put("result", result);
			logAfter(logger);
		}
		return AppUtil.returnObject(new PageData(), map);
	}

	private String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
	
 