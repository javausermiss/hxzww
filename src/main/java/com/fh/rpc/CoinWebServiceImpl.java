package com.fh.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.coinpusher.CoinPusherManager;
import com.fh.service.system.costgoldrewardpoints.CostGoldRewardPointsManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.service.system.pushergamedetail.PusherGameDetailManager;
import com.fh.service.system.segaproportion.SegaProportionManager;
import com.fh.service.system.userpoints.UserPointsManager;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.MyUUID;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcCommandResult;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcReturnCode;
import com.iot.game.pooh.web.rpc.interfaces.CoinRpcService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class CoinWebServiceImpl implements CoinRpcService {

    private static final Map<String,Integer> sessionMap = new HashMap<String,Integer>();

    private  Integer getSessionMap(String key) {
        if(sessionMap.get(key) == null){
            return 0;
        }
        return sessionMap.get(key);
    }
    private  void setSessionMap(String key, Integer value) {
        sessionMap.put(key, value);
    }

    private  void clearSessionMap(String key){
        if(sessionMap.get(key) == null){
            return ;
        }
        sessionMap.remove(key);
    }
        @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "dollService")
    private DollManager dollService;
    @Resource(name = "coinpusherService")
    private CoinPusherManager coinpusherService;
    @Resource(name = "paymentService")
    private PaymentManager paymentService;
    @Resource(name = "userpointsService")
    private UserPointsManager userpointsService;
    @Resource(name="pushergamedetailService")
    private PusherGameDetailManager pushergamedetailService;
    @Resource(name="segaproportionService")
    private SegaProportionManager segaproportionService;


    /**
     * @param roomId
     * @param userId
     * @param bat
     * @return
     */
    @Override
    public RpcCommandResult checkCoin(String roomId, String userId, Integer bat) {
        RpcCommandResult rpcCommandResult = new RpcCommandResult();
       try {
            this.setSessionMap("exp"+roomId+userId,this.getSessionMap("exp"+roomId+userId)+1);
            rpcCommandResult = coinpusherService.doPusherGame(roomId,userId,bat);
            return  rpcCommandResult;
       } catch (Exception e) {
            e.printStackTrace();
            log.info("error",e);
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
            rpcCommandResult.setInfo("程序异常");
            return rpcCommandResult;
        }

    }

    /**
     * @param roomId
     * @param userId
     * @param bat
     * @param bingo
     * @return
     */
    @Override
    public RpcCommandResult playResult(String roomId, String userId, Integer bat, Integer bingo ,String gameId) {
        log.info("用户:" + userId + "-------------------->" + roomId + "开始出币，数量为" + bingo);
        RpcCommandResult rpcCommandResult = new RpcCommandResult();
        try {
            //查找设备信息
            Doll doll = dollService.getDollByID(roomId);
            if (doll == null) {
                rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
                rpcCommandResult.setInfo("设备不存在");
                return rpcCommandResult;
            }
            //获取用户信息
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
                rpcCommandResult.setInfo("用户不存在");
                return rpcCommandResult;
            }
            //出币数统计，并进行换算娃娃币比例 1:1

            String newId = MyUUID.getUUID32();
            CoinPusher coinPusher = new CoinPusher();
            coinPusher.setId(newId);
            coinPusher.setRoomId(roomId);
            coinPusher.setUserId(userId);
            coinPusher.setCostGold(String.valueOf(bat));
            coinPusher.setCreateTime(DateUtil.getTime());
            coinPusher.setFinishFlag("Y");
            coinPusher.setReturnGold(String.valueOf(bingo));
            coinpusherService.reg(coinPusher);

            this.setSessionMap("inc"+roomId+userId,this.getSessionMap("inc"+roomId+userId)+bingo);
            //推币机的单场游戏记录,统计出币数
         /*   PusherGameDetail pusherGameDetail = new PusherGameDetail();
            pusherGameDetail.setTag("0");
            pusherGameDetail.setUserId(userId);
            pusherGameDetail.setRoomId(roomId);
            pusherGameDetail.setGameId(gameId);
            PusherGameDetail pd = pushergamedetailService.getInfo(pusherGameDetail);
            pd.setIncome(pd.getIncome()+bingo);
            pushergamedetailService.update(pd);*/



            //娃娃币换算

            SegaProportion segaProportion =  segaproportionService.getInfoByRoomId(roomId);
            if (bingo != 0) {
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
                int mp_new = sum / 50;
                int mp_old = appUser.getCOIN_MULTIPLES();
                int newBalance;
                int wwb ;
                if (mp_old != mp_new && mp_new != 0 ) {
                    wwb = bingo * Integer.valueOf(segaProportion.getSEGA_PROPORTION()); ;
                    int reward = (mp_new - mp_old) * doll.getCOINPUSHER_REWORD();
                    newBalance = Integer.valueOf(appUser.getBALANCE()) + wwb + reward;
                    //修改金币数量
                    appUser.setBALANCE(String.valueOf(newBalance));
                    appUser.setCOIN_MULTIPLES(mp_new);
                    appuserService.updateAppUserCoinMultiples(appUser);


                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+" + wwb);
                    payment.setUSERID(userId);
                    payment.setDOLLID(roomId);
                    payment.setCOST_TYPE(Const.PlayMentCostType.cost_type17.getValue());
                    payment.setREMARK(Const.PlayMentCostType.cost_type17.getName() + "+" + wwb);
                    paymentService.reg(payment);

                    //更新奖励收支表
                    Payment payment_reward = new Payment();
                    payment_reward.setGOLD("+" + reward);
                    payment_reward.setUSERID(userId);
                    payment_reward.setDOLLID(roomId);
                    payment_reward.setCOST_TYPE(Const.PlayMentCostType.cost_type18.getValue());
                    payment_reward.setREMARK(Const.PlayMentCostType.cost_type18.getName() + "+" + reward);
                    paymentService.reg(payment_reward);

                }else {
                    wwb = bingo * Integer.valueOf(segaProportion.getSEGA_PROPORTION());
                    newBalance = Integer.valueOf(appUser.getBALANCE()) + wwb;
                    //修改金币数量
                    appUser.setBALANCE(String.valueOf(newBalance));
                    appuserService.updateAppUserBalanceById(appUser);

                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+" + wwb);
                    payment.setUSERID(userId);
                    payment.setDOLLID(roomId);
                    payment.setCOST_TYPE(Const.PlayMentCostType.cost_type17.getValue());
                    payment.setREMARK(Const.PlayMentCostType.cost_type17.getName() + "+" + wwb);
                    paymentService.reg(payment);
                }
            }
            userpointsService.doCostRewardPoints(appUser,userId);
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.SUCCESS);
            rpcCommandResult.setInfo("SUCCESS");
            return rpcCommandResult;

        } catch (Exception e) {
            e.printStackTrace();
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
            rpcCommandResult.setInfo("程序异常");
            return rpcCommandResult;
        }

    }

    @Override
    public RpcCommandResult userDownMachine(String userId, String gameId, String roomId) {
        RpcCommandResult rpcCommandResult = new RpcCommandResult();
        try {
            PusherGameDetail pusherGameDetail = new PusherGameDetail();
            pusherGameDetail.setTag("0");
            pusherGameDetail.setUserId(userId);
            pusherGameDetail.setRoomId(roomId);
            pusherGameDetail.setGameId(gameId);
            PusherGameDetail pd = pushergamedetailService.getInfo(pusherGameDetail);
            pd.setTag("1");
            pd.setIncome(sessionMap.get("inc"+roomId+userId));
            pd.setExpenditure(sessionMap.get("exp"+roomId+userId));
            pushergamedetailService.update(pd);
            this.clearSessionMap("inc"+roomId+userId);
            this.clearSessionMap("exp"+roomId+userId);
            log.info("map缓存中剩余得数据=====>>>>>>>>"+sessionMap);
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.SUCCESS);
            rpcCommandResult.setInfo("SUCCESS");
            return rpcCommandResult;

        } catch (Exception e) {
            e.printStackTrace();
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
            rpcCommandResult.setInfo("程序异常");
            return rpcCommandResult;
        }

    }
}
