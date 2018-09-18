package com.fh.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.fh.controller.base.BaseController;
import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.coinpusher.CoinPusherManager;
import com.fh.service.system.costgoldrewardpoints.CostGoldRewardPointsManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.service.system.pointsmall.PointsMallManager;
import com.fh.service.system.pointsreward.PointsRewardManager;
import com.fh.service.system.pushergamedetail.PusherGameDetailManager;
import com.fh.service.system.segaproportion.SegaProportionManager;
import com.fh.service.system.userpoints.UserPointsManager;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RedisUtil;
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
public class CoinWebServiceImpl extends BaseController implements CoinRpcService  {

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
    @Resource(name = "pointsrewardService")
    private PointsRewardManager pointsrewardService;
    @Resource(name = "pointsmallService")
    private PointsMallManager pointsmallService;
    @Resource(name = "pointsdetailService")
    private PointsDetailManager pointsdetailService;


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
           SegaProportion segaProportion =  segaproportionService.getInfoByRoomId(roomId);
           int balance = Integer.valueOf(appUser.getBALANCE());
           int costGold = bat *  Integer.valueOf(segaProportion.getSEGA_PROPORTION());
           int mapBalance = 0;
           if (this.getSessionMap("exp"+roomId+userId)==0){
               mapBalance = balance;
           }else {
               mapBalance = this.getSessionMap("userBalance"+roomId+userId);
           }
           if (mapBalance < costGold) {
               rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
               rpcCommandResult.setInfo("余额不足");
               return rpcCommandResult;
           }
            this.setSessionMap("exp"+roomId+userId,this.getSessionMap("exp"+roomId+userId)+1);
            this.setSessionMap("userBalance"+roomId+userId,mapBalance - costGold);

           //推币机的单场游戏记录
           PusherGameDetail pusherGameDetail = new PusherGameDetail();
           pusherGameDetail.setTag("0");
           pusherGameDetail.setUserId(userId);
           pusherGameDetail.setRoomId(roomId);

           PusherGameDetail pd = pushergamedetailService.getInfo(pusherGameDetail);

           String newId = "";

           String exitPusherGameId = RedisUtil.getRu().get("pusherGameId:roomId:" + roomId);
           if (pd == null) {
               if (exitPusherGameId==null){
                   Date currentTime1 = new Date();
                   SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
                   String dateString1 = formatter1.format(currentTime1);
                   String num = "0001";
                   newId = dateString1 + num;
               }else {
                   Date currentTime = new Date();
                   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                   String dateString = formatter.format(currentTime);
                   String x = exitPusherGameId.substring(0, 8);//取前八位进行判断
                   if (x.equals(dateString)) {
                       newId = String.valueOf(Long.parseLong(exitPusherGameId) + 1);
                   } else {
                       Date current = new Date();
                       SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                       String date = format.format(current);
                       newId = date + "0001";
                   }

               }
               RedisUtil.getRu().set("pusherGameId:roomId:" + roomId, newId);
               pusherGameDetail.setUserId(userId);
               pusherGameDetail.setRoomId(roomId);
               pusherGameDetail.setExpenditure(0);
               pusherGameDetail.setId(MyUUID.getUUID32());
               pusherGameDetail.setGameId(newId);
               pusherGameDetail.setTag("0");
               pushergamedetailService.insert(pusherGameDetail);
           }
          //  rpcCommandResult = coinpusherService.doPusherGame(roomId,userId,bat);
             rpcCommandResult.setRpcReturnCode(RpcReturnCode.SUCCESS);
             rpcCommandResult.setInfo(newId);
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
            this.setSessionMap("inc"+roomId+userId,this.getSessionMap("inc"+roomId+userId)+bingo);
            logger.info("开始出币");
            logger.info("当前一共出币数------------------------->>>>"+sessionMap.get("inc"+roomId+userId));

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
            SegaProportion segaProportion =  segaproportionService.getInfoByRoomId(roomId);
            int rate = Integer.valueOf(segaProportion.getSEGA_PROPORTION());

            Doll doll = dollService.getDollByID(roomId);
            //游戏单场记录
            PusherGameDetail pusherGameDetail = new PusherGameDetail();
            pusherGameDetail.setTag("0");
            pusherGameDetail.setUserId(userId);
            pusherGameDetail.setRoomId(roomId);
            pusherGameDetail.setGameId(gameId);
            PusherGameDetail pd = pushergamedetailService.getInfo(pusherGameDetail);
            pd.setTag("1");
            int incom = this.getSessionMap("inc"+roomId+userId);
            pd.setIncome(incom * rate);
            pd.setExpenditure(sessionMap.get("exp"+roomId+userId)*rate);
            pushergamedetailService.update(pd);

            //给用户结算金币

            AppUser appUser = appuserService.getUserByID(userId);
            incom =  incom * rate ;
            Integer newBalance = sessionMap.get("userBalance"+roomId+userId)+ incom;
            Integer userNewPoints = appUser.getPOINTS();

            //消费金币记录
            Payment payment = new Payment();
            payment.setCOST_TYPE("0");
            payment.setDOLLID(roomId);
            payment.setUSERID(userId);
            payment.setGOLD("-" + String.valueOf(sessionMap.get("exp"+roomId+userId) * rate));
            payment.setREMARK(doll.getDOLL_NAME() + "游戏");
            paymentService.reg(payment);

            //增加金币记录
            Payment payment1 = new Payment();
            payment1.setGOLD("+" + incom);
            payment1.setUSERID(userId);
            payment1.setDOLLID(roomId);
            payment1.setCOST_TYPE(Const.PlayMentCostType.cost_type17.getValue());
            payment1.setREMARK(Const.PlayMentCostType.cost_type17.getName() + "+" + incom);
            paymentService.reg(payment1);

            //判断进行的推币机游戏次数
            UserPoints userPoints = userpointsService.getUserPointsFinish(userId);
            PointsMall pointsMall = pointsmallService.getInfoById(Const.pointsMallType.points_type04.getValue());

            int todayPoints = userPoints.getTodayPoints();
            int newpg = userPoints.getPusherGame() + 1;
            String r_tag = userPoints.getPointsReward_Tag();
            if (newpg == 10) {
                todayPoints = (todayPoints + pointsMall.getPointsValue());
                userNewPoints = userNewPoints + pointsMall.getPointsValue();
                //增加积分记录
                PointsDetail pointsDetail = new PointsDetail();
                pointsDetail.setUserId(userId);
                pointsDetail.setChannel(Const.pointsMallType.points_type04.getName());
                pointsDetail.setType("+");
                pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
                pointsDetail.setPointsValue(pointsMall.getPointsValue());
                pointsdetailService.regPointsDetail(pointsDetail);

                //判断是否增加金币

                if (Integer.valueOf(r_tag) < 5){
                    Integer goldValue = 0;
                    Integer sum = 0;
                    Integer ob = newBalance;
                    Integer nb_2 = 0;
                    List<PointsReward> list = pointsrewardService.getPointsReward();
                    Map map =  userpointsService.doGoldRewardForPusher(r_tag,goldValue,sum,ob,list,userPoints.getTodayPoints() + pointsMall.getPointsValue(),nb_2,appUser);
                    userPoints.setPointsReward_Tag(map.get("new_r_tag").toString());
                    r_tag = map.get("new_r_tag").toString();
                    if (map.get("newBalance")!=null){
                        newBalance = Integer.valueOf(map.get("newBalance").toString());
                    }
                }
            }
            //查询消费金币数
            String now = DateUtil.getDay();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

            PageData pageData = new PageData();
            pageData.put("userId", userId);
            pageData.put("beginDate", now);
            pageData.put("endDate", tomorrow);
            String gm = "0";
            PageData pageData1 = userpointsService.getCostGoldSum(pageData);
            if (pageData1 != null) {
                double aa = (double) pageData1.get("godsum");
                gm = new DecimalFormat("0").format(aa).substring(1);
            }
            int cgs = Integer.valueOf(gm);
            String costGoldSum_Tag = userPoints.getCostGoldSum_Tag() ;
            if (cgs >= 200 && costGoldSum_Tag.equals("0")) {
                //增加积分记录
                PointsDetail pointsDetail_cgs = new PointsDetail();
                pointsDetail_cgs.setUserId(userId);
                pointsDetail_cgs.setChannel(Const.pointsMallType.points_type05.getName());
                pointsDetail_cgs.setType("+");
                pointsDetail_cgs.setPointsDetail_Id(MyUUID.getUUID32());
                pointsDetail_cgs.setPointsValue(pointsMall.getPointsValue());
                pointsdetailService.regPointsDetail(pointsDetail_cgs);
                costGoldSum_Tag = "1";
                PointsMall pointsMall_Cost =  pointsmallService.getInfoById(Const.pointsMallType.points_type05.getValue());
                todayPoints = todayPoints + pointsMall_Cost.getPointsValue();
                userNewPoints = userNewPoints + pointsMall_Cost.getPointsValue();

                //判断是否增加金币
                userPoints = userpointsService.getUserPointsFinish(userId);

                if (Integer.valueOf(r_tag) < 5){
                    Integer goldValue = 0;
                    Integer sum = 0;
                    Integer ob = newBalance;
                    Integer nb_2 = 0;
                    List<PointsReward> list = pointsrewardService.getPointsReward();
                    Map map =  userpointsService.doGoldRewardForPusher(r_tag,goldValue,sum,ob,list,todayPoints,nb_2,appUser);
                    r_tag = map.get("new_r_tag").toString();
                    if (map.get("newBalance")!=null){
                        newBalance = Integer.valueOf(map.get("newBalance").toString());
                    }

                }
            }
            //总消费赠送的积分判断
            Map map = userpointsService.doCostRewardPointsForPusher(r_tag,todayPoints,userNewPoints,newBalance,userId, appUser);
            newBalance = Integer.valueOf(map.get("newBalance").toString());
            userNewPoints = Integer.valueOf(map.get("userNewPoints").toString());
            r_tag = map.get("new_r_tag").toString();
            todayPoints = Integer.valueOf(map.get("todayPoints").toString());

            appUser.setBALANCE(String.valueOf(newBalance));
            appUser.setPOINTS(userNewPoints);
            appuserService.updateAppUserBalanceById(appUser);

            userPoints.setPusherGame(newpg);
            userPoints.setTodayPoints(todayPoints);
            userPoints.setPointsReward_Tag(r_tag);
            userPoints.setCostGoldSum_Tag(costGoldSum_Tag);
            userpointsService.updateUserPoints(userPoints);

            this.clearSessionMap("inc"+roomId+userId);
            this.clearSessionMap("exp"+roomId+userId);
            this.clearSessionMap("userBalance"+roomId+userId);

            log.info("map缓存中剩余得数据=====================================>>>>>>>>"+sessionMap);
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
