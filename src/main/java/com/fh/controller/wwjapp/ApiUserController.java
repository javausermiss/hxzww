package com.fh.controller.wwjapp;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JsonConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fh.entity.system.AppUser;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.util.FastDFSClient;
import com.fh.util.PropertiesUtils;
import com.fh.util.wwjUtil.Base64Image;
import com.fh.util.wwjUtil.Base64Util;
import com.fh.util.wwjUtil.RespStatus;

import net.sf.json.JSONObject;

/**
 * @author wjy
 * @date
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/user")
public class ApiUserController {

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
    							 @RequestParam(value = "base64Image", required = false)String  base64Image //保存图片文件上传路径
                                ) {
        try {
            //String phone = new String(Base64Util.decryptBASE64(aPhone));
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser != null) {
        		String faceName=Base64Image.GenerateImageBytes(base64Image, "defualt.png");
        		
        		//如果当前用户图像不是默认头像，则先删除，再上传
        		if(faceName !=null && appUser.getIMAGE_URL() !=null){
        			String defaultUrl=PropertiesUtils.getCurrProperty("user.default.header.url"); //获取默认头像Id
        			if(!defaultUrl.equals(appUser.getIMAGE_URL())){
        				FastDFSClient.deleteFile(appUser.getIMAGE_URL());
        			}
        		}
                appUser.setIMAGE_URL(faceName);
                int n = appuserService.updateAppUserImage(appUser);
        		//头像上传
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
     * 更改用户信息,发红包需要完善此信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject updateUserInfo(@RequestParam("userId") String userId,
                                 @RequestParam(value = "gender")String gender,
                                 @RequestParam(value = "age")Integer age,
                                 @RequestParam(value = "nickname") String nickname

    ) {
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser != null) {
                appUser.setAGE(age);
                appUser.setGENDER(gender);
                appUser.setNICKNAME(nickname);
                int n = appuserService.updateAppUserInfo(appUser);
                //头像上传
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
                map.put("appUser", getAppUserInfoByID(userId));
                return RespStatus.successs().element("data", map);
            } else {
                List<AppUser> userExist = appuserService.getAppUserByNickNameList(name);
                if (userExist.size() !=0) {
                    return RespStatus.fail("该昵称已经存在");
                } else {
                    user.setNICKNAME(name);
                    int n = appuserService.updateAppUsernickName(user);
                    if (n != 0) {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("appUser", getAppUserInfoByID(userId));
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
    @CrossOrigin(origins = "*", maxAge = 3600)
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
