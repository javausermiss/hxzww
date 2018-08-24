package com.fh.controller.wwjapp;

import com.fh.controller.base.BaseController;
import com.fh.entity.system.*;

import com.fh.service.system.appuser.AppuserManager;

import com.fh.service.system.goldenbeanexchangerate.GoldenBeanexchangeRateManager;
import com.fh.service.system.payment.PaymentManager;

import com.fh.util.Const;
import com.fh.util.PropertiesUtils;
import com.fh.util.wwjUtil.JcjdUtil;

import com.fh.util.wwjUtil.RespStatus;

import io.netty.handler.codec.EncoderException;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 聚彩51H5游戏
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/app/Jc51H5Game")
public class Jc51H5Game extends BaseController {

    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "goldenbeanexchangerateService")
    private GoldenBeanexchangeRateManager goldenrateService;

    @Resource(name = "paymentService")
    private PaymentManager paymentService;

    @RequestMapping(value = "/login",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject regH5Game(@RequestParam("userId") String userId) throws EncoderException {
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return RespStatus.fail("此用户不存在");
            }
            String jcid = appUser.getJCID();
            String userimg = PropertiesUtils.getCurrProperty("service.img.address") + appUser.getIMAGE_URL();
            if (appUser.getIMAGE_URL() == null || appUser.getIMAGE_URL().equals("")) {
                userimg = PropertiesUtils.getCurrProperty("service.img.address") + PropertiesUtils.getCurrProperty("user.default.header.url"); //默认头像
            }
            if (jcid.equals("")) {
                jcid =  JcjdUtil.doRegUser(appUser,userimg);
                appUser.setJCID(jcid);
                appuserService.updateAppUserBalanceById(appUser);

            } else {
                JcjdUtil.doLogin(appUser,userimg);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("appUser", appUser);
            return RespStatus.successs().element("data", map);

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    @RequestMapping(value = "/getGoldenbean",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject regH5Game(@RequestParam("userId") String userId, @RequestParam("beanNum") Integer beanNum) {

        try {
            if (beanNum == 0) {
                return RespStatus.fail();
            }
            AppUser appUser = appuserService.getUserByID(userId);
            Integer balance = Integer.valueOf(appUser.getBALANCE());
            BeanRate beanRate = goldenrateService.getBeanRate();
            Integer rate = beanRate.getRATE();
            Integer cost = beanNum / rate;
            if (balance < cost||balance <= 0) {
                return RespStatus.fail("金币不够");
            }

            String jcid = appUser.getJCID();
            String userimg = PropertiesUtils.getCurrProperty("service.img.address") + appUser.getIMAGE_URL();
            if (appUser.getIMAGE_URL() == null || appUser.getIMAGE_URL().equals("")) {
                userimg =PropertiesUtils.getCurrProperty("service.img.address") + PropertiesUtils.getCurrProperty("user.default.header.url"); //默认头像
            }

            String jdnum = "";
            if (jcid==null||jcid.equals("")){
                String jdid =  JcjdUtil.doRegUser(appUser,userimg);
                appUser.setJCID(jdid);
                jdnum =  JcjdUtil.doExchangeBean(appUser,beanNum);
            }else {
                jdnum =  JcjdUtil.doExchangeBean(appUser,beanNum);
            }
            appUser.setBALANCE(String.valueOf(balance - cost));
            appUser.setJDNUM(jdnum);
            appuserService.updateAppUserBalanceById(appUser);

            //添加游戏金币明细记录
            Payment payment = new Payment();
            payment.setCOST_TYPE(Const.PlayMentCostType.cost_type26.getValue());
            payment.setDOLLID("");
            payment.setUSERID(userId);
            payment.setGOLD("-" + String.valueOf(cost));
            payment.setREMARK(Const.PlayMentCostType.cost_type26.getName());
            paymentService.reg(payment);

            Map<String,Object> map  = new HashMap<>();
            map.put("appUser",appUser);
            return RespStatus.successs().element("data",map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }


}
