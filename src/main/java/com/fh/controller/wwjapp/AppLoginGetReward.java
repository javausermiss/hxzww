package com.fh.controller.wwjapp;

import com.fh.entity.Page;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.LoginRewardGold;
import com.fh.entity.system.Payment;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.loginrewardgold.LoginRewardGoldManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.rewardgoldmanager.RewardGoldManagerManager;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.RespStatus;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户次日登陆领取昨日抓到的金币
 * @author wjy
 * @date 2018-09-27
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/app/loginReward")
public class AppLoginGetReward {


    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name="loginrewardgoldService")
    private LoginRewardGoldManager loginrewardgoldService;

    @Resource(name = "paymentService")
    private PaymentManager paymentService;

    @Resource(name="rewardgoldmanagerService")
    private RewardGoldManagerManager rewardgoldmanagerService;

    /**
     * 获取奖励列表
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getRewardInfo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getRewardInfo(@RequestParam("userId") String userId){
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            String nowDay =  DateUtil.getDay();
            LoginRewardGold loginRewardGold = new LoginRewardGold();
            loginRewardGold.setUserId(userId);
            loginRewardGold.setCreateTime(nowDay);
            List<LoginRewardGold> list =  loginrewardgoldService.getAllInfo(loginRewardGold);
            List<PageData> list1 =  rewardgoldmanagerService.list(new Page());
            PageData p = list1.get(0);
            double rate = Double.valueOf(p.get("GOLD").toString());
            if (list.size()>0){
                for (LoginRewardGold l:
                        list) {
                    int costGold =  l.getGold();
                    int rewardGold = (int)Math.ceil(rate * costGold);
                    l.setRewardGold(rewardGold);
                }
            }
            Map<String,Object> map = new HashMap<>();
            map.put("LoginRewardGold" ,list);
            map.put("RewardGoldManager",p);
            map.put("appUser",appUser);
            return RespStatus.successs().element("data",map);
        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }


    }


    /**
     * 进行兑换
     * @param userId
     * @return
     */
    @RequestMapping(value = "/doReward", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject doReward(@RequestParam("userId") String userId){
        try {
            LoginRewardGold loginRewardGold = new LoginRewardGold();
            loginRewardGold.setUserId(userId);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,-1);
            String yesterDay = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            loginRewardGold.setCreateTime(yesterDay);
            loginRewardGold.setTag("N");
            LoginRewardGold ld =  loginrewardgoldService.getInfo(loginRewardGold);
            if (ld == null){
                return RespStatus.fail("数据不合法");
            }
            AppUser appUser = appuserService.getUserByID(userId);
            int ob = Integer.valueOf(appUser.getBALANCE()) ;
            List<PageData> list1 =  rewardgoldmanagerService.list(new Page());
            PageData p = list1.get(0);
            double rate = Double.valueOf(p.get("GOLD").toString());
            int rewardGold = (int)Math.ceil(rate * ld.getGold());
            appUser.setBALANCE(String.valueOf(ob+rewardGold));
            appuserService.updateAppUserBalanceById(appUser);

            ld.setTag("Y");
            loginrewardgoldService.updateInfo(ld);

            //增加金币记录
            Payment payment = new Payment();
            payment.setREMARK(Const.PlayMentCostType.cost_type30.getName());
            payment.setGOLD("+"+rewardGold);
            payment.setCOST_TYPE(Const.PlayMentCostType.cost_type30.getValue());
            payment.setUSERID(userId);
            paymentService.reg(payment);
            return RespStatus.successs();

        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /**
     * 用户点赞
     * @param userId
     * @return
     */
    @RequestMapping(value = "/doSupport", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject doSupport(@RequestParam("userId") String userId){
        try {

            AppUser appUser =  appuserService.getUserByID(userId);
            String tag = appUser.getSUPPORTTAG();
            if (tag.equals("1")){
                return RespStatus.successs();
            }
            List<PageData> list =  rewardgoldmanagerService.list(new Page());
            PageData pageData =  list.get(0);
            Integer supportnum =  (Integer) pageData.get("SUPPORTNUM");
            int newSnum = supportnum + 1;
            pageData.put("SUPPORTNUM",newSnum);
            rewardgoldmanagerService.updateSupportNum(pageData);
            appUser.setSUPPORTTAG("1");
            appuserService.updateAppUserBalanceById(appUser);
            Map<String,Integer> map = new HashMap<>();
            map.put("allSupportNum",newSnum);
            return RespStatus.successs().element("data",map);

        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }
    }



}
