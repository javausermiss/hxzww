package com.fh.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.CoinPusher;
import com.fh.entity.system.Doll;
import com.fh.entity.system.Payment;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.coinpusher.CoinPusherManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.util.Const;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcCommandResult;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcReturnCode;
import com.iot.game.pooh.web.rpc.interfaces.CoinRpcService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class CoinWebServiceImpl implements CoinRpcService {
    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "dollService")
    private DollManager dollService;
    @Resource(name = "coinpusherService")
    private CoinPusherManager coinpusherService;
    @Resource(name = "paymentService")
    private PaymentManager paymentService;

    /**
     *
     * @param roomId
     * @param userId
     * @param bat
     * @return
     */
    @Override
    public RpcCommandResult checkCoin(String roomId, String userId, Integer bat) {

        log.info("用户:"+userId+"-------------------->"+roomId+"开始投币，数量为"+bat);
        RpcCommandResult rpcCommandResult = new RpcCommandResult();
        try{
            //查找娃娃机信息
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
            //判断金币是否充足,用户选择投一个币，相当于消费10个娃娃币

            int balance = Integer.valueOf(appUser.getBALANCE());
            int costGold = bat * 10;
            if (balance < costGold){
                rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
                rpcCommandResult.setInfo("余额不足");
                return rpcCommandResult;
            }

            //修改金币数量
            appUser.setBALANCE(String.valueOf(balance - costGold));
            appuserService.updateAppUserBalanceById(appUser);

            //添加游戏金币明细记录
            Payment payment = new Payment();
            payment.setCOST_TYPE("0");
            payment.setDOLLID(roomId);
            payment.setUSERID(userId);
            payment.setGOLD("-" + String.valueOf(costGold));
            payment.setREMARK( doll.getDOLL_NAME()+"游戏");
            paymentService.reg(payment);

            //增加用户的推币机游戏记录

            CoinPusher cp =  coinpusherService.getLatestRecordForId(roomId);
            String newId ;
            if (cp == null) {
                Date currentTime1 = new Date();
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
                String dateString1 = formatter1.format(currentTime1);
                String num = "0001";
                newId = dateString1 + num;
            } else {
                String guessid = cp.getId();//获取到场次ID 201712100001
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                String dateString = formatter.format(currentTime);
                String x = guessid.substring(0, 8);//取前八位进行判断
                if (x.equals(dateString)) {
                    newId = String.valueOf(Long.parseLong(guessid) + 1);
                } else {
                    Date current = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                    String date = format.format(current);
                    newId = date + "0001";
                }
            }

            CoinPusher coinPusher = new CoinPusher();
            coinPusher.setId(newId);
            coinPusher.setRoomId(roomId);
            coinPusher.setUserId(userId);
            coinPusher.setCostGold(String.valueOf(bat));
            coinpusherService.reg(coinPusher);

            rpcCommandResult.setRpcReturnCode(RpcReturnCode.SUCCESS);
            rpcCommandResult.setInfo("SUCCESS");
            return rpcCommandResult;
        }catch (Exception e){
            e.printStackTrace();
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
            rpcCommandResult.setInfo("程序异常");
            return rpcCommandResult;
        }

    }

    /**
     *
     * @param roomId
     * @param userId
     * @param bat
     * @param bingo
     * @return
     */
    @Override
    public RpcCommandResult playResult(String roomId, String userId, Integer bat, Integer bingo) {
        log.info("用户:"+userId+"-------------------->"+roomId+"开始出币，数量为"+bingo);
        RpcCommandResult rpcCommandResult = new RpcCommandResult();
        try{
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
            //出币数统计，并进行换算娃娃币比例 1:10

            CoinPusher coinPusher = new CoinPusher();
            coinPusher.setRoomId(roomId);
            coinPusher.setUserId(userId);
            CoinPusher coinPusher1 = coinpusherService.getLatestRecord(coinPusher);
            if (coinPusher1==null){
                rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
                rpcCommandResult.setInfo("无此记录");
                return rpcCommandResult;
            }
            coinPusher1.setReturnGold(String.valueOf(bingo));
            coinPusher1.setFinishFlag("Y");
            coinpusherService.updateOutCoin(coinPusher1);
            //娃娃币换算
            if (bingo!=0){
                int wwb =  bingo * 10;
                int newBalance = Integer.valueOf(appUser.getBALANCE())+wwb;
                //修改金币数量
                appUser.setBALANCE(String.valueOf(newBalance));
                appuserService.updateAppUserBalanceById(appUser);

                //更新收支表
                Payment payment = new Payment();
                payment.setGOLD("+" + wwb);
                payment.setUSERID(userId);
                payment.setDOLLID(roomId);
                payment.setCOST_TYPE(Const.PlayMentCostType.cost_type17.getValue());
                payment.setREMARK(Const.PlayMentCostType.cost_type17.getName() +"+"+ wwb);
                paymentService.reg(payment);
            }
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.SUCCESS);
            rpcCommandResult.setInfo("SUCCESS");
            return rpcCommandResult;

        }catch (Exception e){
            e.printStackTrace();
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
            rpcCommandResult.setInfo("程序异常");
            return rpcCommandResult;
        }

    }
}
