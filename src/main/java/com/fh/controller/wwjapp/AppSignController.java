package com.fh.controller.wwjapp;

import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.conversion.ConversionManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.sendgoods.SendGoodsManager;
import com.fh.service.system.sign.SignManager;
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
     * 断签(未完成)
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sign1", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject sign1(@RequestParam("userId") String userId) {
        try {
            Sign signLast = signService.getSignLastByUserId(userId);//查询出用户最近的一条签到记录
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String dateString = formatter.format(date);
            String gold = "";
            Sign s = new Sign();
            if (signLast == null) {
                s.setUSERID(userId);
                s.setSIGNTIME(dateString);
                s.setCSDATE("1");
                signService.insertSign(s);
            } else {
                //判断今天是否已经签过，防止恶意签到
                if (signLast.getSIGNTIME().equals(dateString)) {
                    return null;
                }
                String time = signLast.getSIGNTIME();
                String signNum = signLast.getCSDATE();
                if (time.substring(0, 4).equals(dateString.substring(0, 4))) {
                    if (time.substring(0, 6).equals(dateString.substring(0, 6))) {
                        int n = Integer.valueOf(dateString) - Integer.valueOf(time);
                        if (n == 1) {
                            if (!signLast.getCSDATE().equals("7")) {
                                signNum = String.valueOf(Integer.valueOf(signNum) + 1);
                                s.setCSDATE(signNum);
                                s.setUSERID(userId);
                                s.setSIGNTIME(dateString);
                                signService.insertSign(s);
                                switch (signNum) {
                                    case "1":
                                        gold = "10";
                                        break;
                                    case "2":
                                        gold = "10";
                                        break;
                                    case "3":
                                        gold = "20";
                                        break;
                                    case "4":
                                        gold = "10";
                                        break;
                                    case "5":
                                        gold = "10";
                                        break;
                                    case "6":
                                        gold = "10";
                                        break;
                                    case "7":
                                        gold = "40";
                                        break;
                                }
                                AppUser appUser = appuserService.getUserByID(userId);
                                String oldBalance = appUser.getBALANCE();
                                int newBalance = Integer.valueOf(oldBalance) + Integer.valueOf(gold);
                                appUser.setBALANCE(String.valueOf(newBalance));
                                Payment payment = new Payment();
                                payment.setCOST_TYPE("8");
                                payment.setUSERID(userId);
                                payment.setGOLD("+" + gold);
                                payment.setREMARK("签到奖励");
                                paymentService.reg(payment);
                            } else {

                            }


                        } else {


                        }

                    } else {

                    }

                } else {


                }


            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }


    }

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
     * 定时器。0点刷新用户的签到标签 ,竞猜次数标签
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void flushAppuserSign() {
        try {
            List<AppUser> list = appuserService.getAppUserList();
            for (int i = 0; i < list.size(); i++) {
                AppUser appUser = list.get(i);
                appUser.setSIGN_TAG("0");
                appuserService.updateAppUserSign(appUser);
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
