package com.fh.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.fh.entity.system.*;
import com.fh.service.system.afterVoting.AfterVotingManager;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.betgame.BetGameManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.service.system.pointsmall.PointsMallManager;
import com.fh.service.system.pointsreward.PointsRewardManager;
import com.fh.service.system.pond.PondManager;
import com.fh.service.system.userpoints.UserPointsManager;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.StringUtils;
import com.fh.util.wwjUtil.MyUUID;
import com.iot.game.pooh.server.entity.json.enums.PoohAbnormalStatus;
import com.iot.game.pooh.server.entity.json.enums.PoohNormalStatus;
import com.iot.game.pooh.server.rpc.interfaces.LotteryServerRpcService;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcCommandResult;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcReturnCode;
import com.iot.game.pooh.web.rpc.interfaces.LotteryWebRpcService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class LotteryWebServiceImpl implements LotteryWebRpcService {
    @Resource(name = "betGameService")
    private BetGameManager betGameService;
    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "pondService")
    private PondManager pondService;
    @Resource(name = "playDetailService")
    private PlayDetailManage playDetailService;
    @Resource(name = "paymentService")
    private PaymentManager paymentService;
    @Resource(name = "dollService")
    private DollManager dollService;
    @Resource
    private LotteryServerRpcService lotteryServerRpcService;
    @Resource(name = "afterVotingService")
    private AfterVotingManager afterVotingService;
    @Resource(name="userpointsService")
    private UserPointsManager userpointsService;
    @Resource(name="pointsmallService")
    private PointsMallManager pointsmallService;

    @Resource(name="pointsdetailService")
    private PointsDetailManager pointsdetailService;

    @Resource(name = "pointsrewardService")
    private PointsRewardManager pointsrewardService;

    /**
     * 开始竞猜(点击开始 start )
     */
    @Override
    public RpcCommandResult startLottery(String dollId, String userId) {
    	
    	 RpcCommandResult rpcCommandResult = new RpcCommandResult();
        try {
            log.info("start时间-------------->"+ DateUtil.getTime());
            
        	//查找娃娃机信息
            Doll doll= dollService.getDollByID(dollId);
            if(doll==null){
            	 rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
                 rpcCommandResult.setInfo("设备不存在");
                return rpcCommandResult;
            }
            
            //获取用户
            AppUser appUser = appuserService.getUserByID(userId);
            if(appUser==null){
           	 rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
                rpcCommandResult.setInfo("用户不存在");
                return rpcCommandResult;
           }
            
            //判断用户金币大于等于每次抓取的值
            //用户余额
            String appUserBalance = appUser.getBALANCE();
            int userBalance = Integer.valueOf(appUserBalance);
            //抓取金币额度
            int dollGold = doll.getDOLL_GOLD();
            
            if (Integer.valueOf(userBalance) < dollGold) {
            	 rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
                 rpcCommandResult.setInfo("余额不足");
                return rpcCommandResult;
            }
            
            
            //添加游戏金币明细记录
            Payment payment = new Payment();
            payment.setCOST_TYPE("0");
            payment.setDOLLID(dollId);
            payment.setUSERID(userId);
            payment.setGOLD("-"+String.valueOf(doll.getDOLL_GOLD()));
            payment.setREMARK("抓取"+doll.getDOLL_NAME());
            paymentService.reg(payment);

            //修改金币数量
            appUser.setBALANCE(String.valueOf(userBalance - dollGold));
            appuserService.updateAppUserBalanceById(appUser);
            
            //获取最新的场次
            PlayDetail playDetail = new PlayDetail();
            playDetail.setDOLLID(dollId);
            PlayDetail p = playDetailService.getPlayDetailLast(playDetail);
            
             //获取娃娃机兑换的金币
             String conversionGold=doll.getDOLL_CONVERSIONGOLD();

            String newGuessID = "";
             
            if (p == null) {
                Date currentTime1 = new Date();
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyyMMdd");
                String dateString1 = formatter1.format(currentTime1);
                String num = "0001";
                newGuessID = dateString1 + num;
                PlayDetail newPlayDetail = new PlayDetail();
                newPlayDetail.setGUESS_ID(newGuessID);
                newPlayDetail.setDOLLID(dollId);
                newPlayDetail.setUSERID(userId);
                newPlayDetail.setCONVERSIONGOLD(conversionGold);
                newPlayDetail.setGOLD(String.valueOf(doll.getDOLL_GOLD()));
                newPlayDetail.setTOY_ID(Integer.valueOf(doll.getTOY_ID()));//增加娃娃编号
                playDetailService.reg(newPlayDetail);
                Pond pond = new Pond(newPlayDetail.getGUESS_ID(), dollId, null);
                pondService.regPond(pond);

            } else {
                String guessid = p.getGUESS_ID();//获取到场次ID 201712100001
                Date currentTime = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                String dateString = formatter.format(currentTime);
                String x = guessid.substring(0, 8);//取前八位进行判断
                if (x.equals(dateString)) {
                    newGuessID = String.valueOf(Long.parseLong(guessid) + 1);
                    PlayDetail newp = new PlayDetail();
                    newp.setGUESS_ID(newGuessID);
                    newp.setUSERID(userId);
                    newp.setDOLLID(dollId);
                    newp.setCONVERSIONGOLD(conversionGold);
                    newp.setGOLD(String.valueOf(doll.getDOLL_GOLD()));
                    newp.setTOY_ID(Integer.valueOf(doll.getTOY_ID()));//增加娃娃编号
                    playDetailService.reg(newp);
                    Pond pond = new Pond(newp.getGUESS_ID(), dollId, null);
                    pondService.regPond(pond);

                } else {
                    Date current = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                    String date = format.format(current);
                    newGuessID = date + "0001";
                    PlayDetail playDetail1 = new PlayDetail();
                    playDetail1.setDOLLID(dollId);
                    playDetail1.setUSERID(userId);
                    playDetail1.setCONVERSIONGOLD(conversionGold);
                    playDetail1.setGUESS_ID(newGuessID);
                    playDetail1.setGOLD(String.valueOf(doll.getDOLL_GOLD()));
                    playDetail1.setTOY_ID(Integer.valueOf(doll.getTOY_ID()));//增加娃娃编号
                    playDetailService.reg(playDetail1);
                    Pond pond = new Pond(playDetail1.getGUESS_ID(), dollId, null);
                    pondService.regPond(pond);

                }
            }
            //增加追投人的竞猜记录,查询是否存在倍数不一致的记录，若存在，则按先后顺序依次进行
            List<AfterVoting> afterVotings = afterVotingService.getListAfterVoting(dollId);
            if (afterVotings != null) {
                for (int i = 0; i < afterVotings.size(); i++) {
                    AfterVoting afterVoting = afterVotings.get(i);
                    int af = afterVoting.getAFTER_VOTING();
                    if (af != 0) {
                        int new_af = af - 1;
                        afterVoting.setAFTER_VOTING(new_af);
                        afterVotingService.updateAfterVoting_Num(afterVoting);
                        //增加竞猜记录
                        GuessDetailL guessDetailL = new GuessDetailL(afterVoting.getUSER_ID(), dollId, afterVoting.getLOTTERY_NUM(), afterVoting.getMULTIPLE() * dollGold, newGuessID);
                        betGameService.regGuessDetail(guessDetailL);
                    }

                }

            }
            //用户的消费总金币对应增加积分
            userpointsService.doCostRewardPoints(appUser,userId);

            //首先查询积分列表是否有该用户信息

            UserPoints userPoints =  userpointsService.getUserPointsFinish(userId);
            PointsMall pointsMall =  pointsmallService.getInfoById(Const.pointsMallType.points_type03.getValue());
            String tag =  userPoints.getPoohGame();
            if(tag.equals("0")){
                int a = userPoints.getTodayPoints();
                userPoints.setTodayPoints(a + pointsMall.getPointsValue());
                userPoints.setPoohGame("1");
                userpointsService.updateUserPoints(userPoints);
                appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());
                appuserService.updateAppUserBalanceById(appUser);

                //增加积分记录
                PointsDetail pointsDetail = new PointsDetail();
                pointsDetail.setUserId(appUser.getUSER_ID());
                pointsDetail.setChannel(Const.pointsMallType.points_type03.getName());
                pointsDetail.setType("+");
                pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
                pointsDetail.setPointsValue(pointsMall.getPointsValue());
                pointsdetailService.regPointsDetail(pointsDetail);

                //判断是否增加金币
                userPoints = userpointsService.getUserPointsFinish(appUser.getUSER_ID());
                Integer now_points = userPoints.getTodayPoints();
                String r_tag = userPoints.getPointsReward_Tag();
                Integer goldValue = 0;
                Integer sum = 0;
                Integer ob = Integer.valueOf(appUser.getBALANCE());
                Integer nb_2 = 0;
                List<PointsReward> list = pointsrewardService.getPointsReward();
                String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
                userPoints.setPointsReward_Tag(n_rtag);
                userpointsService.updateUserPoints(userPoints);

            }

                //查询消费金币数
                String now = DateUtil.getDay();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);
                String tomorrow = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

                PageData pageData = new PageData();
                pageData.put("userId",userId);
                pageData.put("beginDate",now);
                pageData.put("endDate",tomorrow);
                String gm = "0";
                PageData pageData1 =  userpointsService.getCostGoldSum(pageData);
                if (pageData1 != null){
                    double aa = (double)pageData1.get("godsum");
                    gm = new DecimalFormat("0").format(aa).substring(1);
                }
                int cgs =   Integer.valueOf(gm);
                if (cgs >= 200 && userPoints.getCostGoldSum_Tag().equals("0")){
                    //增加积分记录
                    PointsDetail pointsDetail_cgs = new PointsDetail();
                    pointsDetail_cgs.setUserId(userId);
                    pointsDetail_cgs.setChannel(Const.pointsMallType.points_type05.getName());
                    pointsDetail_cgs.setType("+");
                    pointsDetail_cgs.setPointsDetail_Id(MyUUID.getUUID32());
                    pointsDetail_cgs.setPointsValue(pointsMall.getPointsValue());
                    pointsdetailService.regPointsDetail(pointsDetail_cgs);

                    userPoints =  userpointsService.getUserPointsFinish(userId);
                    userPoints.setCostGoldSum_Tag("1");
                    PointsMall pointsMall_Cost =  pointsmallService.getInfoById(Const.pointsMallType.points_type05.getValue());
                    userPoints.setTodayPoints( pointsMall_Cost.getPointsValue()+userPoints.getTodayPoints());
                    userpointsService.updateUserPoints(userPoints);

                    appUser = appuserService.getUserByID(userId);
                    appUser.setPOINTS(appUser.getPOINTS() + pointsMall_Cost.getPointsValue());
                    appuserService.updateAppUserBalanceById(appUser);

                    //判断是否增加金币
                    userPoints = userpointsService.getUserPointsFinish(userId);
                    Integer now_points = userPoints.getTodayPoints();
                    String r_tag = userPoints.getPointsReward_Tag();
                    if (Integer.valueOf(r_tag) < 5){
                        Integer goldValue = 0;
                        Integer sum = 0;
                        Integer ob = Integer.valueOf(appUser.getBALANCE());
                        Integer nb_2 = 0;
                        List<PointsReward> list = pointsrewardService.getPointsReward();
                        String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
                        userPoints.setPointsReward_Tag(n_rtag);
                        userpointsService.updateUserPoints(userPoints);
                    }
                }







            rpcCommandResult.setRpcReturnCode(RpcReturnCode.SUCCESS);
            rpcCommandResult.setInfo(newGuessID); ///这里写期号
            return rpcCommandResult;


        } catch (Exception e) {
            e.printStackTrace();
            
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
            rpcCommandResult.setInfo("设备异常"); ///这里写期号
            return rpcCommandResult;
        }

    }


    /**
     * 结束竞猜(机器下爪 catch)
     */
    @Override
    public RpcCommandResult endLottery(String roomId, String userId) {
        try {

            RpcCommandResult rpcCommandResult = new RpcCommandResult();
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.SUCCESS);
            //获取服务器时间戳最后一位毫秒作为开奖数字
            String catch_time = String.valueOf(System.currentTimeMillis());
            log.info(roomId+"--------下爪");
            String reword_num = catch_time.substring(catch_time.length()-1,catch_time.length());

            PlayDetail playDetail = playDetailService.getPlayIdForPeople(roomId);//根据房间取得最新的游戏记录
            if (!playDetail.getSTOP_FLAG().equals("0") || !playDetail.getPOST_STATE().equals("-1")||StringUtils.isEmpty(userId)) {
                rpcCommandResult.setInfo("该指令为机器自动下抓");
                return rpcCommandResult;
            }
            log.info("初始状态下，STOP_FLAG值为 ：0，POST_STATE值为 ：-1");
            log.info("该房间号："+playDetail.getDOLLID()+"||STOP_FLAG："+playDetail.getSTOP_FLAG()+"||POST_STATE"+playDetail.getPOST_STATE()
            +"场次ID"+playDetail.getGUESS_ID()+"||此条记录创建时间为："+playDetail.getCREATE_DATE()+"||当前时间为："+DateUtil.getTimeHHmmss()
            );

            String gold = playDetail.getGOLD();//获取下注金币，即竞猜用户扣除的金币数
           //设置游戏列表中的开奖数字
            playDetail.setSTOP_FLAG("-1");
            playDetail.setREWARD_NUM(reword_num);
            int a = playDetailService.updatePlayDetailStopFlag(playDetail);
            if (a==1){
                log.info("机器下抓--------->获得开奖数存储成功");
            }

            GuessDetailL guessDetailL_n =  new GuessDetailL();
            guessDetailL_n.setPLAYBACK_ID(playDetail.getGUESS_ID());
            guessDetailL_n.setDOLL_ID(playDetail.getDOLLID());

            List<GuessDetailL> guessDetailLS =  betGameService.getAllGuesser(guessDetailL_n);
            for (int i = 0; i < guessDetailLS.size(); i++) {
                GuessDetailL guessDetailL =  guessDetailLS.get(i);
                guessDetailL.setGUESS_TYPE(reword_num);
                guessDetailL.setSETTLEMENT_FLAG("Y");//此标签已不具有结算意义
                guessDetailL.setDOLL_ID(playDetail.getDOLLID());
                betGameService.updateGuessDetailGuessType(guessDetailL);
            }


            //设置奖池表的中奖数字
            Pond p = new Pond();
            p.setDOLL_ID(roomId);
            p.setGUESS_ID(playDetail.getGUESS_ID());
            Pond pond = pondService.getPondByPlayId(p);//查询对应奖池信息
            pond.setGUESS_STATE(reword_num);
            pond.setPOND_FLAG("-1");//此标签代表禁止再次结算
            pond.setALLPEOPLE(pond.getGUESS_N()+pond.getGUESS_Y());//获取竞猜总人数
            int an = pond.getGUESS_N()*Integer.valueOf(gold);
            int ay = pond.getGUESS_Y()*Integer.valueOf(gold);
            pond.setGOLD_N(an);//猜不中人下注总金额
            pond.setGOLD_Y(ay);//猜中的人下注总金额
            pond.setGUESS_STATE(reword_num);//本局抓中状态
            pondService.updatePondFlag(pond);//更新标签

            rpcCommandResult.setInfo("结束下抓，禁止竞猜");
            return rpcCommandResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 开始算奖(机器复位 free)
     */
    @Override
    public RpcCommandResult drawLottery(String roomId, Integer gifinumber) {
        try {
         return betGameService.doFree(roomId,gifinumber);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String gatewayException(String roomId, PoohNormalStatus poohNormalStatus, PoohAbnormalStatus poohAbnormalStatus) {
        try {
            //更新本次游戏记录状态为异常-1
            PlayDetail playDetail = playDetailService.getPlayIdForPeople(roomId);
            playDetail.setSTATE("-1");
            playDetailService.updatePlayDetailState(playDetail);
            //给玩家退回该场游戏金币
            String balance = appuserService.getUserByID(playDetail.getUSERID()).getBALANCE() + playDetail.getGOLD();
            AppUser appUser = appuserService.getUserByID(playDetail.getUSERID());
            appUser.setBALANCE(balance);
            appuserService.updateAppUserBalanceById(appUser);
            //给竞猜用户退回本场竞猜金币,更新本次竞猜记录为异常-1

            GuessDetailL guessDetailL_n =  new GuessDetailL();
            guessDetailL_n.setPLAYBACK_ID(playDetail.getGUESS_ID());
            guessDetailL_n.setDOLL_ID(playDetail.getDOLLID());

            List<GuessDetailL> list = betGameService.getAllGuesser(guessDetailL_n);
            for (int i = 0; i < list.size(); i++) {
                GuessDetailL guessDetailL = list.get(i);
                guessDetailL.setGUESS_TYPE("-2");
                guessDetailL.setSETTLEMENT_FLAG("Y");
                betGameService.updateGuessDetail(guessDetailL);
                AppUser appUser1 = appuserService.getUserByID(guessDetailL.getAPP_USER_ID());
                String nb = appUser1.getBALANCE() + playDetail.getGOLD();
                appUser1.setBALANCE(nb);
                appuserService.updateAppUserBalanceById(appUser1);
                //更新收支表
                Payment payment = new Payment();
                payment.setGOLD(String.valueOf(playDetail.getGOLD()));
                payment.setUSERID(guessDetailL.getAPP_USER_ID());
                payment.setDOLLID(roomId);
                payment.setCOST_TYPE("2");
                payment.setREMARK("机器异常返款");
                paymentService.reg(payment);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }


}
