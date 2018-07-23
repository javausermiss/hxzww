package com.fh.controller.wwjapp;

import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.conversion.ConversionManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.sendgoods.SendGoodsManager;
import com.fh.service.system.sign.SignManager;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.wwjUtil.RespStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/app/sign")
public class AppSignController {
    @Resource(name = "signService")
    private SignManager signService;

    @Resource(name = "paymentService")
    private PaymentManager paymentService;

    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "playDetailService")
    private PlayDetailManage playDetailService;

    @Resource(name = "sendGoodsService")
    private SendGoodsManager sendGoodsService;


    /**
     * 连续签到
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sign", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject sign(@RequestParam("userId") String userId,
                           @RequestParam("signType") String signType
    ) {

        try {
            return signService.doSign(userId, signType);

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }

    /**
     * 定时器。0点刷新用户的签到标签 ,竞猜次数标签,刷新奖励倍数标签
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void flushAppuserSign() {
        try {
            List<AppUser> list = appuserService.getAppUserList();
            for (int i = 0; i < list.size(); i++) {
                AppUser appUser = list.get(i);
                appUser.setSIGN_TAG("0");
                appUser.setCOIN_MULTIPLES(0);
                appUser.setWEEKS_CARD_TAG("0");
                appUser.setMONTH_CARD_TAG("0");
                appuserService.updateAppUserSign(appUser);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 定时器。下发奖励周卡用户
     */
    @Scheduled(cron = "0 5 0 * * ?")
    public void flushAppuserWeekAward() {
        try {
            List<AppUser> list = appuserService.getWeekCardPeoples();
            for (int i = 0; i < list.size(); i++) {
                AppUser appUser = list.get(i);
                if (!appUser.getWEEKS_CARD_TAG().equals("1")){
                   int nb =  Integer.valueOf(appUser.getBALANCE()) + 20;
                   appUser.setWEEKS_CARD(appUser.getWEEKS_CARD()-1);
                   appUser.setBALANCE(String.valueOf(nb));
                   appUser.setWEEKS_CARD_TAG("1");
                   appuserService.updateAppUserBalanceById(appUser);
                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+" + 20);
                    payment.setUSERID(appUser.getUSER_ID());
                    payment.setDOLLID(null);
                    payment.setCOST_TYPE(Const.PlayMentCostType.cost_type22.getValue());
                    payment.setREMARK(Const.PlayMentCostType.cost_type22.getName());
                    paymentService.reg(payment);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 定时器。下发奖励月卡用户
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void flushAppuserMonthAward() {
        try {
            List<AppUser> list = appuserService.getMonthCardPeoples();
            for (int i = 0; i < list.size(); i++) {
                AppUser appUser = list.get(i);
                if (!appUser.getMONTH_CARD_TAG().equals("1")){
                    int nb =  Integer.valueOf(appUser.getBALANCE()) + 33;
                    appUser.setMONTH_CARD(appUser.getMONTH_CARD()-1);
                    appUser.setBALANCE(String.valueOf(nb));
                    appUser.setMONTH_CARD_TAG("1");
                    appuserService.updateAppUserBalanceById(appUser);

                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+" + 33);
                    payment.setUSERID(appUser.getUSER_ID());
                    payment.setDOLLID(null);
                    payment.setCOST_TYPE(Const.PlayMentCostType.cost_type23.getValue());
                    payment.setREMARK(Const.PlayMentCostType.cost_type23.getName());
                    paymentService.reg(payment);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    /**
     * 定时兑换用户过期的娃娃,凌晨3点查询
     */
   /* @Scheduled(cron="0 0 3 * * ?")
    public void conversionToy(){
        try {
            List<PlayDetail> list =  playDetailService.getConversionToy();
            for (int i = 0; i <list.size() ; i++) {
                 PlayDetail playDetail = list.get(i);
                 String begin_date =  playDetail.getCAMERA_DATE();
                 String now_date = DateUtil.getDay();
                 Long n =  DateUtil.getDaySub(begin_date,now_date);
                 if (n>15){
                     sendGoodsService.doConversionGoldAuto(playDetail);
                 }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }*/
}
