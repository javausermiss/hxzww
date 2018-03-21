package com.fh.controller.wwjapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.controller.base.BaseController;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.Doll;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.doll.DollManager;
import com.fh.util.Const;
import com.fh.util.PropertiesUtils;
import com.fh.util.StringUtils;
import com.fh.util.wwjUtil.FaceImageUtil;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RedisUtil;
import com.fh.util.wwjUtil.RespStatus;
import com.fh.util.wwjUtil.TokenVerify;
import com.iot.game.pooh.admin.srs.core.entity.httpback.SrsConnectModel;
import com.iot.game.pooh.admin.srs.core.util.SrsConstants;
import com.iot.game.pooh.admin.srs.core.util.SrsSignUtil;

import net.sf.json.JSONObject;
@Controller
@RequestMapping("/app")
public class AppTencentloginForH5 extends BaseController {
	
    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "dollService")
    private DollManager dollService;

    /**
     * 个人信息
     *
     * @param id
     * @return
     */

    public JSONObject getAppUserInfo(String id) {
        try {
            AppUser appUser = appuserService.getUserByID(id);
            return JSONObject.fromObject(appUser);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 娃娃机信息
     */
    public JSONObject getDollInfo(String id) {
        try {
            Doll doll = dollService.getDollByID(id);
            return JSONObject.fromObject(doll);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 微信 QQ登录接口
     *
     * @param userId
     * @param token
     * @param imageUrl
     * @param nickname
     * @return
     */
    @RequestMapping(value = "/tencentLoginH5", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject tencentLogin(
    		HttpServletRequest req,
            @RequestParam("uid") String userId,
            @RequestParam("accessToken") String token,
            @RequestParam("imageUrl") String imageUrl,
            @RequestParam("nickName") String nickname
    ) {
       	String ctype=req.getParameter("ctype"); //SDK
    	String channel=req.getParameter("channel");//渠道
    	
    	logger.info("userId-->"+userId+",ctype-->"+ctype+",channel-->"+channel);
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
            	 String code ="";
            	if(Const.SDKMenuType.YSDK.getValue().equals(ctype) || (ctype==null || "".equals(ctype)) ){
            		code= TokenVerify.verify(token); //应用宝SDK验证
            	}else{
            		code= TokenVerify.verifyForALL(token); //官方验证
            	}
              
                if (code.equals("SUCCESS")) {
                    AppUser appUser1 = new AppUser();
                    if (imageUrl == null||imageUrl.equals("") ) {
                        imageUrl = PropertiesUtils.getCurrProperty("user.default.header.url"); //默认头像
                    }
                    appUser1.setNICKNAME(nickname);
                    appUser1.setIMAGE_URL(imageUrl);
                    appUser1.setUSER_ID(userId);
                    appuserService.regwx(appUser1);
                    //SRS推流
                    Date currentTime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    String dateString = formatter.format(currentTime);
                    String tid = userId;
                    String type = "U";
                    String time = dateString;
                    Map<String,Object> map = new HashMap<>();
                    map.put("expire",3600);
                    map.put("type",type);
                    map.put("tid",userId);
                    map.put("time",dateString);
                    map.put("key","Pooh4token");
                    Map<String, Object> sortedParams = new TreeMap<String, Object>(map);
                    Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
                    // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
                    StringBuilder basestring = new StringBuilder();
                    for (Map.Entry<String, Object> param : entrys) {
                        basestring.append(param.getKey()).append('=').append(param.getValue()).append('&');
                    }
                    basestring.append("key=").append("Pooh4token");
                    String SRStoken =  TokenVerify.md5(basestring.toString());
                    RedisUtil.getRu().setex("SRStoken:appUser:"+userId,SRStoken,21600);
                    String sessionID = MyUUID.createSessionId();
                    List<Doll> doll = dollService.getAllDoll();
                    RedisUtil.getRu().set("sessionId:appUser:" + userId, sessionID);
                    RedisUtil.getRu().set("tencentToken:" + userId, token);
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("appUser", getAppUserInfo(userId));
                    map1.put("sessionID", sessionID);
                    map1.put("dollList", doll);
                    map1.put("expire",3600);
                    map1.put("SRSUsertoken",SRStoken);
                    map1.put("time",dateString);

                    return RespStatus.successs().element("data", map1);
                } else {
                    return RespStatus.fail("token不合法");
                }
            } else {
             	String code ="";
             	if(Const.SDKMenuType.YSDK.getValue().equals(ctype) || (ctype==null || "".equals(ctype)) ){
             		code= TokenVerify.verify(token); //应用宝SDK验证
             	}else{
             		code= TokenVerify.verifyForALL(token); //官方验证
             	}
                if (code.equals("SUCCESS")) {
                    if ( imageUrl == null||imageUrl.equals("")  ) {
//                        imageUrl = "/default.png";
                        imageUrl = PropertiesUtils.getCurrProperty("user.default.header.url"); //默认头像
                    }
                    String newFace = FaceImageUtil.downloadImage(imageUrl);
                    appUser.setNICKNAME(nickname);
                    appUser.setIMAGE_URL(newFace);
                    appuserService.updateTencentUser(appUser);
                    //SRS推流
                    Date currentTime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    String dateString = formatter.format(currentTime);
                    String tid = userId;
                    String type = "U";
                    String time = dateString;
                    Map<String,Object> map = new HashMap<>();
                    map.put("expire",3600);
                    map.put("type",type);
                    map.put("tid",userId);
                    map.put("time",dateString);
                    map.put("key","Pooh4token");
                    Map<String, Object> sortedParams = new TreeMap<String, Object>(map);
                    Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
                    // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
                    StringBuilder basestring = new StringBuilder();
                    for (Map.Entry<String, Object> param : entrys) {
                        basestring.append(param.getKey()).append('=').append(param.getValue()).append('&');
                    }
                    basestring.append("key=").append("Pooh4token");
                    String SRStoken =  TokenVerify.md5(basestring.toString());
                    RedisUtil.getRu().setex("SRStoken:appUser:"+userId,SRStoken,21600);

                    String sessionID = MyUUID.createSessionId();
                    List<Doll> doll = dollService.getAllDoll();
                    RedisUtil.getRu().set("tencentToken:" + userId, token);
                    RedisUtil.getRu().set("sessionId:appUser:" + userId, sessionID);
                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("appUser", getAppUserInfo(userId));
                    map1.put("sessionID", sessionID);
                    map1.put("dollList", doll);
                    map1.put("expire",3600);
                    map1.put("SRSUsertoken",SRStoken);
                    map1.put("time",dateString);
                    return RespStatus.successs().element("data", map1);
                } else {
                    return RespStatus.fail("token不合法");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }


    /**
     * 自动登录
     *
     * @param userId
     * @param accessToken
     * @return
     */
    @RequestMapping(value = "/tencentAutoLoginH5", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject tencentAutoLogin(
    		HttpServletRequest req,
            @RequestParam("userId") String userId,
            @RequestParam("accessToken") String accessToken
    ) {
     	String ctype=req.getParameter("ctype"); //SDK
    	String channel=req.getParameter("channel");//渠道
    	logger.info("userId-->"+userId+",ctype-->"+ctype+",channel-->"+channel);
        try {
          	 String code ="";
          	if(Const.SDKMenuType.YSDK.getValue().equals(ctype)){
          		code= TokenVerify.verify(accessToken); //应用宝SDK验证
          	}else{
          		code= TokenVerify.verifyForALL(accessToken); //官方验证
          	}
            if ("SUCCESS".equals(code)) {
                if (appuserService.getUserByID(userId) == null) {
                    return RespStatus.fail("用户不存在");
                }
                //SRS推流
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String dateString = formatter.format(currentTime);
                String tid = userId;
                String type = "U";
                String time = dateString;
                Map<String,Object> map = new HashMap<>();
                map.put("expire",3600);
                map.put("type",type);
                map.put("tid",userId);
                map.put("time",dateString);
                map.put("key","Pooh4token");
                Map<String, Object> sortedParams = new TreeMap<String, Object>(map);
                Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
                // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
                StringBuilder basestring = new StringBuilder();
                for (Map.Entry<String, Object> param : entrys) {
                    basestring.append(param.getKey()).append('=').append(param.getValue()).append('&');
                }
                basestring.append("key=").append("Pooh4token");
                String SRStoken =  TokenVerify.md5(basestring.toString());
                RedisUtil.getRu().setex("SRStoken:appUser:"+userId,SRStoken,21600);
                String sessionID = MyUUID.createSessionId();
                List<Doll> doll = dollService.getAllDoll();
                RedisUtil.getRu().set("tencentToken:" + userId, accessToken);
                RedisUtil.getRu().set("sessionId:appUser:" + userId, sessionID);
                Map<String, Object> map1 = new HashMap<>();
                map1.put("appUser", getAppUserInfo(userId));
                map1.put("sessionID", sessionID);
                map1.put("dollList", doll);
                map1.put("accessToken", accessToken);
                map1.put("expire",3600);
                map1.put("SRSUsertoken",SRStoken);
                map1.put("time",dateString);

                return RespStatus.successs().element("data", map1);
            } else {
                return RespStatus.fail("token 失效");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }


    
    @RequestMapping(value = "/robotLoginToList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject robotLoginToList(HttpServletRequest req){
    	
    	String[] userArray=new String[]{"jzzww888888888888888888888888881",
    									"jzzww888888888888888888888888882",
    									"jzzww888888888888888888888888883",
    									"jzzww888888888888888888888888884",
    									"jzzww888888888888888888888888885"};
    	
    	List<Map> data=new ArrayList<>();
    	
    	
    	//遍历用户ID
    	for (String userId : userArray) {
    		 //SRS推流
            SrsConnectModel sc = new SrsConnectModel();
            long time = System.currentTimeMillis();
            sc.setType("U");
            sc.setTid(userId);
            sc.setExpire(3600 * 24);
            sc.setTime(time);
            sc.setToken(SrsSignUtil.genSign(sc, SrsConstants.SRS_CONNECT_KEY));
            
            //用户session存储
            String sessionID =  RedisUtil.getRu().get(Const.REDIS_APPUSER_SESSIONID + userId);
            if(StringUtils.isEmpty(sessionID)){
            	sessionID=MyUUID.createSessionId();
            	 RedisUtil.getRu().set(Const.REDIS_APPUSER_SESSIONID + userId, sessionID);
            }	
            //return data
            Map<String, Object> map = new HashMap<>();
            map.put("appUser", getAppUserInfo(userId));//重新查询用户信息
            map.put("sessionID", sessionID);
            map.put("srsToken", sc);
            
            data.add(map);
    	}
        return RespStatus.successs().element("data", data);
    }



}
