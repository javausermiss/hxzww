package com.fh.controller.wwjapp;

import com.fh.entity.system.AppUser;
import com.fh.entity.system.CoinPusher;
import com.fh.entity.system.Doll;
import com.fh.entity.system.Payment;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.coinpusher.CoinPusherManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.wwjUtil.RespStatus;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcReturnCode;
import net.sf.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 推币机控制类
 * @author wjy
 * @date 2018/06/15
 */
@Controller
@RequestMapping("/app/coinPusher")
public class AppCoinPusherController {

    @Resource(name = "coinpusherService")
    private CoinPusherManager coinpusherService;

    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "dollService")
    private DollManager dollService;
    @Resource(name = "paymentService")
    private PaymentManager paymentService;


    /**
     * 查询用户的投币记录，出币记录
     * @return
     */
    @RequestMapping(value = "/getCoinPusherRecondList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getCoinPusherRecondList(@RequestParam("userId") String userId){
        try {
            List<CoinPusher> list = coinpusherService.getCoinPusherRecondList(userId);
            if (list==null){
                return null;
            }
            Map<String,Object> map = new HashMap<>();
            map.put("coinPusher",list);
            return RespStatus.successs().element("data",map);
        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }


    }

    /**
     * 查询用户的投币记录，出币记录
     * @return
     */
    @RequestMapping(value = "/getCoinSum", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getCoinSum(@RequestParam("userId") String userId){
        try {
            String now = DateUtil.getDay();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            CoinPusher coinPusher_day = new CoinPusher();
            coinPusher_day.setUserId(userId);
            coinPusher_day.setBeginDate(now);
            coinPusher_day.setEndDate(tomorrow);
            CoinPusher coinPusher_sum = coinpusherService.getSumCoinOneDay(coinPusher_day);
            int sum = coinPusher_sum.getSum();
            Map<String,Object> map = new HashMap<>();
            map.put("coinPusher",coinPusher_sum);
            return RespStatus.successs().element("data",map);
        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }


    }





}
