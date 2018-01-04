package com.fh.controller.wwjapp;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fh.entity.system.AppUser;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.util.FastDFSClient;
import com.fh.util.PropertiesUtils;
import com.fh.util.wwjUtil.Base64Util;
import com.fh.util.wwjUtil.RespStatus;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/api")
public class AppUserController {

    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    /**
     * 通过手机号码获取个人信息
     *
     * @param phone
     * @return
     */

    public JSONObject getAppUserInfoByPhone(String phone) {
        try {
            AppUser appUser = appuserService.getUserByPhone(phone);
            return JSONObject.fromObject(appUser);
        } catch (Exception e) {
            return null;
        }

    }

    public JSONObject getAppUserInfoByID(String id) {
        try {
            AppUser appUser = appuserService.getUserByID(id);
            return JSONObject.fromObject(appUser);
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 通过昵称获取个人信息
     * @param nickname
     * @return
     */
    public JSONObject getAppUserByNickName(String nickname) {
        try {
            AppUser appUser = appuserService.getAppUserByNickName(nickname);
            return JSONObject.fromObject(appUser);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取用户信息
     *
     * @param aPhone
     * @return
     */

    @RequestMapping(value = "/getAppUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getAppUser(@RequestParam("phone") String aPhone) {
        try {
            String phone = new String(Base64Util.decryptBASE64(aPhone));
            AppUser appUser = appuserService.getUserByPhone(phone);
            if (appUser != null) {
                appuserService.updateAppUserImage(appUser);
                return RespStatus.successs().element("appUser", getAppUserInfoByPhone(phone));
            } else {
                return RespStatus.fail("无此用户！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }

    /**
     * 更改用户头像信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject updateUser(@RequestParam("userId") String userId,
    							 @RequestParam(value = "base64Image", required = false)CommonsMultipartFile  multipartFile //保存图片文件上传路径
                                ) {
        try {
            //String phone = new String(Base64Util.decryptBASE64(aPhone));
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser != null) {
        		String faceName="";
//                String faceName = MyUUID.createSessionId();
//                if (!Base64Image.GenerateImage(base64Image, "/usr/local/tomcat/webapps/faceImage/" + faceName + ".png")) {
//                    return RespStatus.fail("上传失败");
//                }
//                appUser.setIMAGE_URL("/" + faceName + ".png");
//                int n = appuserService.updateAppUserImage(appUser);
        		//头像上传
        		try{
        			String newFilename=multipartFile.getOriginalFilename();
        			DiskFileItem fi = (DiskFileItem) multipartFile.getFileItem();
        			File file = fi.getStoreLocation();
        			faceName = FastDFSClient.uploadFile(file, newFilename);
        		}catch(Exception ex){
        			ex.printStackTrace();
        		}
        		appUser.setIMAGE_URL(faceName);
                int n = appuserService.updateAppUserImage(appUser);
                if (n >0 ) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("appUser", getAppUserInfoByID(userId));
                    return RespStatus.successs().element("data", map);
                } else {
                    return RespStatus.fail("更新头像失败");
                }

            } else {
                return RespStatus.fail("无此用户！");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }


    }

    /**
     * 更改用户昵称
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject updateUserName(@RequestParam("userId") String userId,
                                     @RequestParam("nickName") String name) {

        try {
           // String phone = new String(Base64Util.decryptBASE64(aPhone));
            AppUser user = appuserService.getUserByID(userId);
            if (user == null) {
                return RespStatus.fail("此用户没有注册！");
            }
            if (name == null || name.trim().length() <= 0) {
                return RespStatus.fail("用户名为空，不合法");
            }
            if (user.getNICKNAME().equals(name)) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("appUser", getAppUserByNickName(name));
                return RespStatus.successs().element("data", map);
            } else {
                AppUser userExist = appuserService.getAppUserByNickName(name);
                if (userExist != null) {
                    return RespStatus.fail("用户名已经存在");
                } else {
                    user.setNICKNAME(name);
                    int n = appuserService.updateAppUsernickName(user);
                    if (n != 0) {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("appUser", getAppUserByNickName(name));
                        return RespStatus.successs().element("data", map);
                    } else {
                        return RespStatus.fail("更改昵称失败！");

                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }

    /**
     * 通过ID获取用户信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserInfo(
            @RequestParam("userId") String userId
    ){
        try{
          Map<String,Object> map = new HashMap<>();
          map.put("appUser",getAppUserInfoByID(userId));
          return RespStatus.successs().element("data",map);
        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }

    }




    /**
     * 通过昵称获取用户信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody

    public JSONObject getUser(@RequestParam("nickName") String nickName){

        try{
            AppUser appUser = appuserService.getAppUserByNickName(nickName);
            if (appUser!=null){
                Map<String, Object> map = new HashMap<>();
                map.put("appUser",getAppUserByNickName(nickName));
                return RespStatus.successs().element("data", map);
            }else {
                return RespStatus.fail("无此用户");
            }

        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }


    }

}
