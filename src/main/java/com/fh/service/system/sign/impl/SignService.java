package com.fh.service.system.sign.impl;

import com.fh.dao.DaoSupport;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.Payment;
import com.fh.entity.system.Sign;
import com.fh.entity.system.SignGold;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.sign.SignManager;
import com.fh.service.system.signgold.SignGoldManager;
import com.fh.util.wwjUtil.RespStatus;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("signService")
public class SignService implements SignManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "paymentService")
    private PaymentManager paymentService;

    @Resource(name = "signgoldService")
    private SignGoldManager signgoldService;

    @Override
    public int insertSign(Sign sign) throws Exception {
        return (int) dao.save("SignMapper.insertSign", sign);
    }

    @Override
    public Sign getSignLastByUserId(String userId) throws Exception {
        return (Sign) dao.findForObject("SignMapper.getSignLastByUserId", userId);
    }

    @Override
    public int updateSign(Sign sign) throws Exception {
        return (int) dao.update("SignMapper.updateSign", sign);
    }


    @Override
    public JSONObject
    doSign(String userId, String signType) throws Exception {
        AppUser appUser1 = appuserService.getUserByID(userId);
        if (appUser1 == null) {
            return null;
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(date);
        if (signType.equals("0")) {
            Sign newSignLast = this.getSignLastByUserId(userId);
            Sign sign = new Sign();
            if (newSignLast == null) {
                sign.setCSDATE("0");
                sign.setUSERID(userId);
                sign.setSIGNTIME(dateString);
                this.insertSign(sign);
                Sign newSignLast1 = this.getSignLastByUserId(userId);
                Map<String, Object> map = new HashMap<>();
                map.put("sign", newSignLast1);
                return RespStatus.successs().element("data", map);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("sign", newSignLast);
            return RespStatus.successs().element("data", map);
        } else {
            Sign signLast = this.getSignLastByUserId(userId);//查询出用户最近的一条签到记录
            String gold = "";
            Sign s = new Sign();
            if (signLast.getCSDATE().equals("0")) {
                signLast.setCSDATE("1");
                this.updateSign(signLast);
                SignGold signGold = signgoldService.getSignGoldByDay("1");
                gold = signGold.getGOLD_NUM();

                AppUser appUser = appuserService.getUserByID(userId);
                String oldBalance = appUser.getBALANCE();
                int newBalance = Integer.valueOf(oldBalance) + Integer.valueOf(gold);
                appUser.setBALANCE(String.valueOf(newBalance));
                appUser.setSIGN_TAG("1");
                appuserService.updateAppUserSB(appUser);
                Payment payment = new Payment();
                payment.setCOST_TYPE("8");
                payment.setUSERID(userId);
                payment.setGOLD("+" + gold);
                payment.setREMARK("签到奖励");
                paymentService.reg(payment);
            } else {
                if (signLast.getSIGNTIME().equals(dateString)) {
                    return RespStatus.fail("已经签到！");
                }
                String signday = signLast.getCSDATE();//查询最近的签到天数
                int h = Integer.valueOf(signday);
                if (h != 7) {
                    SignGold signGold = signgoldService.getSignGoldByDay(String.valueOf(h+1));
                    gold = signGold.getGOLD_NUM();
                    signday = String.valueOf(h + 1);
                    s.setUSERID(userId);
                    s.setSIGNTIME(dateString);
                    s.setCSDATE(signday);
                    this.insertSign(s);
                    AppUser appUser = appuserService.getUserByID(userId);
                    String oldBalance = appUser.getBALANCE();
                    int newBalance = Integer.valueOf(oldBalance) + Integer.valueOf(gold);
                    appUser.setBALANCE(String.valueOf(newBalance));
                    appUser.setSIGN_TAG("1");
                    appuserService.updateAppUserSB(appUser);
                    Payment payment = new Payment();
                    payment.setCOST_TYPE("8");
                    payment.setUSERID(userId);
                    payment.setGOLD("+" + gold);
                    payment.setREMARK("签到奖励");
                    paymentService.reg(payment);
                }
            }
            Sign newSignLast = this.getSignLastByUserId(userId);
            newSignLast.setSIGNGOLD(gold);
            Map<String, Object> map = new HashMap<>();
            map.put("sign", newSignLast);
            return RespStatus.successs().element("data", map);
        }

    }
}
