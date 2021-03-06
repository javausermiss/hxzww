package com.fh.controller.wwjapp;


import com.fh.controller.base.BaseController;
import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.appuserlogininfo.AppuserLoginInfoManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.service.system.pointsmall.PointsMallManager;
import com.fh.service.system.pointsreward.PointsRewardManager;
import com.fh.service.system.userpoints.UserPointsManager;
import com.fh.util.Const;
import com.fh.util.FastDFSClient;
import com.fh.util.MD5;
import com.fh.util.PageData;
import com.fh.util.PropertiesUtils;
import com.fh.util.wwjUtil.*;
import com.iot.game.pooh.admin.srs.core.entity.httpback.SrsConnectModel;
import com.iot.game.pooh.admin.srs.core.util.SrsConstants;
import com.iot.game.pooh.admin.srs.core.util.SrsSignUtil;

import cn.jpush.http.RequestTypeEnum;
import net.sf.json.JSONObject;

import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

/**
 * 短信接口类
 *
 * @author wjy
 */
@Controller
@RequestMapping("/app/sms")
public class AppLoginController extends BaseController {

    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "dollService")
    private DollManager dollService;

    @Resource(name = "paymentService")
    private PaymentManager paymentService;

    @Resource(name = "appuserlogininfoService")
    private AppuserLoginInfoManager appuserlogininfoService;

    @Resource(name="userpointsService")
    private UserPointsManager userpointsService;

    @Resource(name="pointsmallService")
    private PointsMallManager pointsmallService;

    @Resource(name="pointsdetailService")
    private PointsDetailManager pointsdetailService;


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
     * 登陆短信验证码
     *
     * @param aPhone
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/getRegSMSCode", method = {RequestMethod.POST, RequestMethod.GET}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getRegSMSCode(@RequestParam("phone") String aPhone,
                                    @RequestParam(value = "callback", required = false) String callback) {
        try {
            String phone = new String(Base64Util.decryptBASE64(aPhone));
            if (phone == null || phone.trim().length() <= 0) {
                return RespStatus.fail("手机号码不能为空！");
            } else {
                if (!phone.matches("^1(3|4|5|7|8)\\d{9}$")) {
                    return RespStatus.fail("手机号码格式错误！");
                }
            }
            Random r = new Random();
            String code = "";
            for (int i = 0; i <= 5; i++) {
                code += String.valueOf(r.nextInt(10));
            }
            SMSUtil.veriCode1(phone, code);
            RedisUtil.getRu().setex("SMSCode:" + phone, code, 300);

           /* if (StringUtils.isNotEmpty(callback)){
                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(RespStatus.successs());
                mappingJacksonValue.setJsonpFunction(callback);
                return JSONObject.fromObject(mappingJacksonValue);
            }else {

            }*/
            return RespStatus.successs();

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.exception();
        }
    }

    /**
     * 登陆/注册
     *
     * @param aPhone
     * @param aCode
     * @return
     */
    @RequestMapping(value = "/getSMSCodeLogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getSMSCodeLogin(@RequestParam("phone") String aPhone, @RequestParam("code") String aCode) {

        try {
            String phone = new String(Base64Util.decryptBASE64(aPhone));
            String code = new String(Base64Util.decryptBASE64(aCode));
            if (phone == null || phone.trim().length() <= 0) {
                return RespStatus.fail("手机号不能为空！");
            } else if (!phone.matches("^1(3|4|5|7|8)\\d{9}$")) {
                return RespStatus.fail("手机号码格式错误！");
            } else if (code == null || code.trim().length() <= 0) {
                return RespStatus.fail("验证码不能为空！");
            }
            if (!RedisUtil.getRu().exists("SMSCode:" + phone)) {
                return RespStatus.fail("此手机号尚未请求验证码！");
            }
            String exitCode = RedisUtil.getRu().get("SMSCode:" + phone);
            if (!exitCode.equals(code)) {
                return RespStatus.fail("验证码无效！");
            }
            RedisUtil.getRu().del("SMSCode:" + phone);
            AppUser appUser = appuserService.getUserByPhone(phone);
            if (appUser != null) {
                String sessionID = MyUUID.createSessionId();
                String userId = appUser.getUSER_ID();

                //登录日志
                AppuserLogin appuserLogin = new AppuserLogin();
                appuserLogin.setAPPUSERLOGININFO_ID(MyUUID.getUUID32());
                appuserLogin.setUSER_ID(userId);
                appuserlogininfoService.insertLoginLog(appuserLogin);

                //SRS推流
                SrsConnectModel sc = new SrsConnectModel();
                long time = System.currentTimeMillis();
                sc.setType("U");
                sc.setTid(userId);
                sc.setExpire(3600 * 24);
                sc.setTime(time);
                sc.setToken(SrsSignUtil.genSign(sc, SrsConstants.SRS_CONNECT_KEY));
                RedisUtil.getRu().set("sessionId:appUser:" + appUser.getUSER_ID(), sessionID);
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("sessionID", sessionID);
                map.put("appUser", getAppUserInfo(appUser.getUSER_ID()));
                map.put("srsToken", sc);
                return RespStatus.successs().element("data", map);
            } else {
                String face = PropertiesUtils.getCurrProperty("user.default.header.url"); //默认头像
                int a1 = appuserService.reg(phone, face);
                if (a1 != 1) {
                    return RespStatus.fail("注册失败");

                }
                AppUser appUserNew = appuserService.getUserByPhone(phone);
                String userId = appUserNew.getUSER_ID();

                //登录日志
                AppuserLogin appuserLogin = new AppuserLogin();
                appuserLogin.setAPPUSERLOGININFO_ID(MyUUID.getUUID32());
                appuserLogin.setUSER_ID(userId);
                appuserlogininfoService.insertLoginLog(appuserLogin);

                logger.info("tencentLogin--> userId=" + userId + ",首次登陆，注册赠送金币...");
                //增加赠送金币明细
                Payment payment = new Payment();
                payment.setREMARK(Const.PlayMentCostType.cost_type13.getName());
                payment.setGOLD("+3");
                payment.setCOST_TYPE(Const.PlayMentCostType.cost_type13.getValue());
                payment.setUSERID(userId);
                paymentService.reg(payment);

                //SRS推流
                SrsConnectModel sc = new SrsConnectModel();
                long time = System.currentTimeMillis();
                sc.setType("U");
                sc.setTid(userId);
                sc.setExpire(3600 * 24);
                sc.setTime(time);
                sc.setToken(SrsSignUtil.genSign(sc, SrsConstants.SRS_CONNECT_KEY));
                String sessionID = MyUUID.createSessionId();
                RedisUtil.getRu().set("sessionId:appUser:" + appUserNew.getUSER_ID(), sessionID);
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("sessionID", sessionID);
                map.put("appUser", getAppUserInfo(appUserNew.getUSER_ID()));
                map.put("srsToken", sc);
                return RespStatus.successs().element("data", map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.exception();
        }

    }

    /**
     * 忘记密码，修改密码
     *
     * @param phone
     * @param code
     * @param password
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject resetPassword(@RequestParam("phone") String phone,
                                    @RequestParam("code") String code,
                                    @RequestParam("password") String password
    ) {
        try {
            if (phone == null || phone.trim().length() <= 0) {
                return RespStatus.fail("手机号不能为空！");
            } else if (!phone.matches("^1(3|4|5|7|8)\\d{9}$")) {
                return RespStatus.fail("手机号码格式错误！");
            } else if (code == null || code.trim().length() <= 0) {
                return RespStatus.fail("验证码不能为空！");
            }

            AppUser appUser = appuserService.getUserByPhone(phone);
            if (appUser == null) {
                return RespStatus.fail("该用户暂未注册");
            }

            if (!RedisUtil.getRu().exists("SMSCode:" + phone)) {
                return RespStatus.fail("此手机号尚未请求验证码！");
            }
            String exitCode = RedisUtil.getRu().get("SMSCode:" + phone);
            if (!exitCode.equals(code)) {
                return RespStatus.fail("验证码无效！");
            }
            RedisUtil.getRu().del("SMSCode:" + phone);
            appUser.setPASSWORD(MD5.md5(password));
            appuserService.updateAppuserpw(appUser);
            return RespStatus.successs();

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /**
     * 手机号码直登获取娃娃机信息
     *
     * @param userId
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/getDoll", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getDoll(@RequestParam("userId") String userId, HttpServletRequest httpServletRequest) {
        try {
            return appuserService.dogetDoll(userId,httpServletRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.exception();
        }

    }

    /**
     * 访客模式，只允许查看房间
     *
     * @return
     */

    @RequestMapping(value = "/autoLogin", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject autoLogin() {
        try {
            String accessToken = "";
            if (RedisUtil.getRu().exists("accessToken")) {
                accessToken = RedisUtil.getRu().get("accessToken");
            } else {
                accessToken = CameraUtils.getAccessToken();
            }
            String sessionID = MyUUID.createSessionId();
            RedisUtil.getRu().setex("sessionId:autoVistor:" + sessionID, sessionID, 3600);
            List<Doll> doll = dollService.getAllDoll();
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("accessToken", accessToken);
            map.put("sessionID", sessionID);
            map.put("dollList", doll);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /**
     * 注册用户
     *
     * @param phone
     * @param pw
     * @param smsCode
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/userRegister", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject userPassLogin(@RequestParam("phone") String phone,
                                    @RequestParam("password") String pw,
                                    @RequestParam("smsCode") String smsCode,
                                    @RequestParam(value = "channelNum", required = false, defaultValue = "100001") String channelNum,
                                    HttpServletRequest httpServletRequest
    ) {
        try {
            if (phone == null || phone.trim().length() <= 0) {
                return RespStatus.fail("手机号不能为空！");
            } else if (!phone.matches("^1(3|4|5|7|8)\\d{9}$")) {
                return RespStatus.fail("手机号码格式错误！");
            } else if (smsCode == null || smsCode.trim().length() <= 0) {
                return RespStatus.fail("验证码不能为空！");
            }
            if (!RedisUtil.getRu().exists("SMSCode:" + phone)) {
                return RespStatus.fail("此手机号尚未请求验证码！");
            }
            String exitCode = RedisUtil.getRu().get("SMSCode:" + phone);
            if (!exitCode.equals(smsCode)) {
                return RespStatus.fail("验证码无效！");
            }
            AppUser appUser = appuserService.getUserByPhone(phone);
            if (appUser != null) {
                return RespStatus.fail("该用户已经注册过");
            }

            RedisUtil.getRu().del("SMSCode:" + phone);
            PointsMall pointsMall =  pointsmallService.getInfoById(Const.pointsMallType.points_type00.getValue());
            //注册用户
            AppUser appUser1 = new AppUser();
            String face = PropertiesUtils.getCurrProperty("user.default.header.url"); //默认头像
            appUser1.setPASSWORD(MD5.md5(pw));
            appUser1.setNICKNAME(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            String n_userId = MyUUID.createSessionId();
            appUser1.setUSER_ID(n_userId);
            appUser1.setIMAGE_URL(face);
            appUser1.setUSERNAME(phone);
            appUser1.setPHONE(phone);
            appUser1.setBALANCE("0");
            appUser1.setCHANNEL_NUM(channelNum);
            appUser1.setBDPHONE(phone);
            appUser1.setPOINTS(pointsMall.getPointsValue());
            //sessionId
            String sessionID = MyUUID.createSessionId();
            RedisUtil.getRu().set(Const.REDIS_APPUSER_SESSIONID + appUser1.getUSER_ID(), sessionID);

            appuserService.doRegSMSUser(appUser1,pointsMall,sessionID,httpServletRequest);
            //SRS推流
            SrsConnectModel sc = new SrsConnectModel();
            long time = System.currentTimeMillis();
            sc.setType("U");
            sc.setTid(appUser1.getUSER_ID());
            sc.setExpire(3600 * 24);
            sc.setTime(time);
            sc.setToken(SrsSignUtil.genSign(sc, SrsConstants.SRS_CONNECT_KEY));



            Map<String, Object> map = new HashMap<>();
            map.put("sessionID", sessionID);
            map.put("appUser", appUser1);
            map.put("srsToken", sc);
            return RespStatus.successs().element("data", map);

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }


    /**
     * 采用账号密码的方式登陆
     *
     * @param phone
     * @param pw
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/userPassLogin", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject userPassLogin(@RequestParam("phone") String phone, @RequestParam("pw") String pw, HttpServletRequest httpServletRequest) {
        try {
            return appuserService.doUserPassLogin(phone,pw,httpServletRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.exception();
        }
    }

    /**
     * @param uid        用户id
     * @param nickname   用户昵称
     * @param gender     性别
     * @param imgUrl     头像
     * @param regChannel 登录渠道微信或者QQ
     * @return
     */
    @CrossOrigin(origins = "*", maxAge = 3600)
    @RequestMapping(value = "/wxRegister", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject wxRegister(@RequestParam("uid") String uid,
                                 @RequestParam("name") String nickname,
                                 @RequestParam("gender") String gender,
                                 @RequestParam("iconurl") String imgUrl,
                                 @RequestParam("regChannel") String regChannel,
                                 @RequestParam(value = "channelNum", required = false, defaultValue = "100001") String channelNum
    ) {
        //查看用户是否存在
        try {
            PointsMall pointsMall =  pointsmallService.getInfoById(Const.pointsMallType.points_type00.getValue());
            AppUser appUser = appuserService.getUserByID(uid);
            logger.info("头像URL-----------------------》"+imgUrl);
            String newFace = "";
            //首先查询积分列表是否有该用户信息
            UserPoints userPoints =  userpointsService.getUserPointsFinish(uid);
            return appuserService.doRegTencentUser(appUser,imgUrl,newFace,nickname,uid,channelNum,gender,regChannel,userPoints,pointsMall);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }



}









