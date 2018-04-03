package com.fh.rpc;

import com.alibaba.dubbo.config.annotation.Service;
import com.fh.entity.system.*;
import com.fh.service.system.afterVoting.AfterVotingManager;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.betgame.BetGameManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.pond.PondManager;
import com.fh.util.DateUtil;
import com.iot.game.pooh.server.entity.json.enums.PoohAbnormalStatus;
import com.iot.game.pooh.server.entity.json.enums.PoohNormalStatus;
import com.iot.game.pooh.server.rpc.interfaces.LotteryServerRpcService;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcCommandResult;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcReturnCode;
import com.iot.game.pooh.web.rpc.interfaces.LotteryWebRpcService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
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
                 rpcCommandResult.setInfo("设备不存在"); ///这里写期号
            }
            
            //获取用户
            AppUser appUser = appuserService.getUserByID(userId);
            if(appUser==null){
           	 rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
                rpcCommandResult.setInfo("用户不存在"); ///这里写期号
           }
            
            //判断用户金币大于等于每次抓取的值
            String appUserBalance = appUser.getBALANCE(); //用户余额
            int userBalance = Integer.valueOf(appUserBalance);
            int dollGold = doll.getDOLL_GOLD();//抓取金币额度
          
            
            if (Integer.valueOf(userBalance) < dollGold) {
            	 rpcCommandResult.setRpcReturnCode(RpcReturnCode.FAILURE);
                 rpcCommandResult.setInfo("余额不足"); ///这里写期号
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
    public RpcCommandResult endLottery(String roomId, String userName) {
        try {

            RpcCommandResult rpcCommandResult = new RpcCommandResult();
            rpcCommandResult.setRpcReturnCode(RpcReturnCode.SUCCESS);
            //获取服务器时间戳最后一位毫秒作为开奖数字
            String catch_time = String.valueOf(System.currentTimeMillis());
            log.info(roomId+"--------下爪");
            String reword_num = catch_time.substring(catch_time.length()-1,catch_time.length());

            PlayDetail playDetail = playDetailService.getPlayIdForPeople(roomId);//根据房间取得最新的游戏记录
            if (!playDetail.getSTOP_FLAG().equals("0") || !playDetail.getPOST_STATE().equals("-1")) {
                rpcCommandResult.setInfo("该指令为机器自动下抓");
                return rpcCommandResult;
            }
            String gold = playDetail.getGOLD();//获取下注金币，即竞猜用户扣除的金币数
           //设置游戏列表中的开奖数字
            playDetail.setSTOP_FLAG("-1");
            playDetail.setREWARD_NUM(reword_num);
            int a = playDetailService.updatePlayDetailStopFlag(playDetail);
            if (a==1){
                log.info("机器下抓--------->获得开奖数存储成功");
            }
            List<GuessDetailL> guessDetailLS =  betGameService.getAllGuesser(playDetail.getGUESS_ID());
            for (int i = 0; i < guessDetailLS.size(); i++) {
                GuessDetailL guessDetailL =  guessDetailLS.get(i);
                guessDetailL.setGUESS_TYPE(reword_num);
                guessDetailL.setSETTLEMENT_FLAG("Y");//此标签已不具有结算意义
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

          /*
            log.info("执行复位时间----------------->"+DateUtil.getTime());
            PlayDetail playDetail = playDetailService.getPlayIdForPeople(roomId);//根据房间取得最新的游戏记录
            if (playDetail==null){
                return null;
            }
            String gold = playDetail.getGOLD();//获取下注金币，即竞猜用户扣除的金币数
            String state = playDetail.getPOST_STATE();//获取娃娃发送状态
            //网关自检发送多次free进入此判断逻辑,POST_STATE初始值为"-1"，判断是否已经结算过
            //STOP_FLAG 初始值为"0",下抓后为"-1",判断流程是否走完
            if (playDetail.getSTOP_FLAG().equals("0")
                    || state.equals("0")
                    || state.equals("1")
                    || state.equals("2")
                    ) {
                return null;
            }
            if (gifinumber!=0){
                gifinumber = 1;
            }
            playDetail.setSTATE(String.valueOf(gifinumber));
            playDetail.setPOST_STATE("0");
            playDetail.setDOLLID(roomId);
            playDetailService.updatePlayDetailState(playDetail);
            log.info("更新抓取状态时间----------------->"+DateUtil.getTime());
            Pond p = new Pond();
            p.setDOLLID(roomId);
            p.setGUESS_ID(playDetail.getGUESS_ID());
            Pond pond = pondService.getPondByPlayId(p);//查询对应奖池信息
            if (!pond.getPOND_FLAG().equals("0")) {
                return null;
            }
            pond.setPOND_FLAG("-1");//此标签代表禁止再次结算
            pond.setALLPEOPLE(pond.getGUESS_N()+pond.getGUESS_Y());//获取竞猜总人数
            int an = pond.getGUESS_N()*Integer.valueOf(gold);
            int ay = pond.getGUESS_Y()*Integer.valueOf(gold);
            pond.setGOLD_N(an);//猜不中人下注总金额
            pond.setGOLD_Y(ay);//猜中的人下注总金额
            pond.setGUESS_STATE(String.valueOf(gifinumber));//本局抓中状态
            pondService.updatePondFlag(pond);//更新标签
            int allGold = pond.getGUESS_GOLD();
            int y = pond.getGUESS_Y();//猜1
            int cn = pond.getGUESS_N();//猜0
            int avg; //结算获胜者的平均金额
            String s = playDetail.getSTATE();//取得压中的状态
            String file = "";//失败状态
            String userid = playDetail.getUSERID();
            //判断是否抓中
            if (!String.valueOf(gifinumber).equals("0")) {
                file = "0";
                //用户抓中，更新抓中总数
                AppUser appUser = appuserService.getUserByID(userid);
                System.out.println("================================appuser:" + appUser);
                int oldCount = appUser.getDOLLTOTAL();
                System.out.println("===================================oldcount:" + oldCount);
                appUser.setDOLLTOTAL(oldCount + 1);
                appuserService.updateAppUserDollTotalById(appUser);
                //以下2种判断为单向竞猜 统统返还给用户竞猜金币
                if (y == 0 && cn != 0) {
                    List<GuessDetailL> filler = betGameService.getFailer(new GuessDetailL(file, playDetail.getGUESS_ID(), roomId));//获取失败者
                    for (int i = 0; i < filler.size(); i++) {
                        GuessDetailL guessDetailL = filler.get(i);
                        String uid = guessDetailL.getAPP_USER_ID();
                        AppUser appUser1 = appuserService.getUserByID(uid);
                        String old = appUser1.getBALANCE();
                        int k = Integer.valueOf(old) + Integer.valueOf(gold);
                        appUser1.setBALANCE(String.valueOf(k));
                        appuserService.updateAppUserBalanceById(appUser1);
                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+"+gold);
                        payment.setUSERID(uid);
                        payment.setDOLLID(roomId);
                        payment.setCOST_TYPE("4");
                        payment.setREMARK("流局返款");
                        paymentService.reg(payment);
                        //更改竞猜记录
                        guessDetailL.setGUESS_TYPE("-1");//流局标识 -1
                        guessDetailL.setSETTLEMENT_FLAG("Y");//结算
                        guessDetailL.setSETTLEMENT_GOLD(Integer.valueOf(gold));
                        betGameService.updateGuessDetail(guessDetailL);
                    }
                    RpcCommandResult rpcCommandResult = new RpcCommandResult();
                    RpcReturnCode result = lotteryServerRpcService.noticeDrawLottery(roomId, playDetail.getGUESS_ID(), null);
                    if (RpcReturnCode.SUCCESS == rpcCommandResult.getRpcReturnCode()) {
                        log.info("通知成功");
                    } else {
                        log.info("通知失败");
                    }
                    rpcCommandResult.setRpcReturnCode(result);
                    return rpcCommandResult;
                }
                if (y != 0 && cn == 0) {
                    List<GuessDetailL> winner = betGameService.getWinner(new GuessDetailL(s, playDetail.getGUESS_ID(), roomId));//获取成功者
                    for (int i = 0; i < winner.size(); i++) {
                        GuessDetailL guessDetailL = winner.get(i);
                        String uid = guessDetailL.getAPP_USER_ID();
                        AppUser appUser1 = appuserService.getUserByID(uid);
                        String old = appUser1.getBALANCE();
                        int k = Integer.valueOf(old) + Integer.valueOf(gold);
                        appUser1.setBALANCE(String.valueOf(k));
                        appuserService.updateAppUserBalanceById(appUser1);
                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+"+gold);
                        payment.setUSERID(uid);
                        payment.setDOLLID(roomId);
                        payment.setCOST_TYPE("4");
                        payment.setREMARK("流局返款");
                        paymentService.reg(payment);
                        //更改竞猜记录
                        guessDetailL.setGUESS_TYPE("-1");//流局标识 -1
                        guessDetailL.setSETTLEMENT_FLAG("Y");//结算
                        guessDetailL.setSETTLEMENT_GOLD(Integer.valueOf(gold));
                        betGameService.updateGuessDetail(guessDetailL);
                    }
                    RpcCommandResult rpcCommandResult = new RpcCommandResult();
                    RpcReturnCode result = lotteryServerRpcService.noticeDrawLottery(roomId, playDetail.getGUESS_ID(), null);
                    if (RpcReturnCode.SUCCESS == rpcCommandResult.getRpcReturnCode()) {
                        log.info("通知成功");
                    } else {
                        log.info("通知失败");
                    }
                    rpcCommandResult.setRpcReturnCode(result);
                    return rpcCommandResult;
                }
                //没人竞猜
                if (y == 0 && cn == 0) {
                    RpcCommandResult rpcCommandResult = new RpcCommandResult();
                    RpcReturnCode result = lotteryServerRpcService.noticeDrawLottery(roomId, playDetail.getGUESS_ID(), null);
                    if (RpcReturnCode.SUCCESS == rpcCommandResult.getRpcReturnCode()) {
                        log.info("通知成功");
                    } else {
                        log.info("通知失败");
                    }
                    rpcCommandResult.setRpcReturnCode(result);
                    return rpcCommandResult;
                }
                avg = (int) Math.floor(allGold / y);
            } else {
                //没有抓中的情况
                file = "1";
                //以下2种判断为单向竞猜 统统返还给用户竞猜金币
                if (y == 0 && cn != 0) {
                    List<GuessDetailL> winner = betGameService.getWinner(new GuessDetailL(s, playDetail.getGUESS_ID(), roomId));//获取成功者
                    for (int i = 0; i < winner.size(); i++) {
                        GuessDetailL guessDetailL = winner.get(i);
                        String uid = guessDetailL.getAPP_USER_ID();
                        AppUser appUser1 = appuserService.getUserByID(uid);
                        String old = appUser1.getBALANCE();
                        int k = Integer.valueOf(old) + Integer.valueOf(gold);
                        appUser1.setBALANCE(String.valueOf(k));
                        appuserService.updateAppUserBalanceById(appUser1);
                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+"+gold);
                        payment.setUSERID(uid);
                        payment.setDOLLID(roomId);
                        payment.setCOST_TYPE("4");
                        payment.setREMARK("流局返款");
                        paymentService.reg(payment);
                        //更改竞猜记录
                        guessDetailL.setGUESS_TYPE("-0");//流局标识 -1
                        guessDetailL.setSETTLEMENT_FLAG("Y");//结算
                        guessDetailL.setSETTLEMENT_GOLD(Integer.valueOf(gold));
                        betGameService.updateGuessDetail(guessDetailL);
                    }
                    RpcCommandResult rpcCommandResult = new RpcCommandResult();
                    RpcReturnCode result = lotteryServerRpcService.noticeDrawLottery(roomId, playDetail.getGUESS_ID(), null);
                    if (RpcReturnCode.SUCCESS == rpcCommandResult.getRpcReturnCode()) {
                        log.info("通知成功");
                    } else {
                        log.info("通知失败");
                    }
                    rpcCommandResult.setRpcReturnCode(result);
                    return rpcCommandResult;

                }
                if (y != 0 && cn == 0) {
                    List<GuessDetailL> filler = betGameService.getFailer(new GuessDetailL(file, playDetail.getGUESS_ID(), roomId));//获取失败者
                    for (int i = 0; i < filler.size(); i++) {
                        GuessDetailL guessDetailL = filler.get(i);
                        String uid = guessDetailL.getAPP_USER_ID();
                        AppUser appUser1 = appuserService.getUserByID(uid);
                        String old = appUser1.getBALANCE();
                        int k = Integer.valueOf(old) + Integer.valueOf(gold);
                        appUser1.setBALANCE(String.valueOf(k));
                        appuserService.updateAppUserBalanceById(appUser1);
                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+"+gold);
                        payment.setUSERID(uid);
                        payment.setDOLLID(roomId);
                        payment.setCOST_TYPE("4");
                        payment.setREMARK("流局返款");
                        paymentService.reg(payment);
                        //更改竞猜记录
                        guessDetailL.setGUESS_TYPE("-0");//流局标识 -1
                        guessDetailL.setSETTLEMENT_FLAG("Y");//结算
                        guessDetailL.setSETTLEMENT_GOLD(Integer.valueOf(gold));
                        betGameService.updateGuessDetail(guessDetailL);
                    }
                    RpcCommandResult rpcCommandResult = new RpcCommandResult();
                    RpcReturnCode result = lotteryServerRpcService.noticeDrawLottery(roomId, playDetail.getGUESS_ID(), null);
                    if (RpcReturnCode.SUCCESS == rpcCommandResult.getRpcReturnCode()) {
                        log.info("通知成功");
                    } else {
                        log.info("通知失败");
                    }
                    rpcCommandResult.setRpcReturnCode(result);
                    return rpcCommandResult;
                }
                //无人竞猜
                if (y == 0 && cn == 0) {
                    RpcCommandResult rpcCommandResult = new RpcCommandResult();
                    RpcReturnCode result = lotteryServerRpcService.noticeDrawLottery(roomId, playDetail.getGUESS_ID(), null);
                    if (RpcReturnCode.SUCCESS == rpcCommandResult.getRpcReturnCode()) {
                        log.info("通知成功");
                    } else {
                        log.info("通知失败");
                    }
                    rpcCommandResult.setRpcReturnCode(result);
                    return rpcCommandResult;
                }
                avg = (int) Math.floor(allGold / cn);
            }
            List<GuessDetailL> filler = betGameService.getFailer(new GuessDetailL(file, playDetail.getGUESS_ID(), roomId));//获取失败者
            if (filler.size() != 0) {
                for (int k = 0; k < filler.size(); k++) {
                    GuessDetailL filePerson = filler.get(k);
                    filePerson.setSETTLEMENT_FLAG("Y");
                    filePerson.setGUESS_TYPE(String.valueOf(gifinumber));
                    betGameService.updateGuessDetail(filePerson);
                }
            }

            List<GuessDetailL> winner = betGameService.getWinner(new GuessDetailL(s, playDetail.getGUESS_ID(), roomId));//获取成功者
           log.info("winner size:"+winner.size());
            List<GuessDetail> win = new LinkedList<>();
            if (winner.size() != 0) {
                for (int f = 0; f < winner.size(); f++) {
                    GuessDetailL winPerson = winner.get(f);
                    log.info("localG:"+winPerson);
                    GuessDetail guessDetail1 = new GuessDetail();
                    guessDetail1.setAppUserId(winPerson.getAPP_USER_ID());
                    AppUser appUser = appuserService.getUserByID(winPerson.getAPP_USER_ID());
                    guessDetail1.setNickname(appUser.getNICKNAME());
                    win.add(guessDetail1);
                    log.info("DubboG:"+guessDetail1);
                    String userId = winPerson.getAPP_USER_ID();
                    AppUser appUser1 = appuserService.getUserByID(userId);
                    int balance = Integer.parseInt(appUser1.getBALANCE());
                    appUser1.setBALANCE(String.valueOf(balance + avg));
                    appuserService.updateAppUserBalanceById(appUser1);
                    winPerson.setSETTLEMENT_GOLD(avg);
                    winPerson.setSETTLEMENT_FLAG("Y");
                    winPerson.setGUESS_TYPE(String.valueOf(gifinumber));
                    betGameService.updateGuessDetail(winPerson);
                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+"+String.valueOf(avg));
                    payment.setUSERID(userId);
                    payment.setDOLLID(roomId);
                    payment.setCOST_TYPE("3");
                    payment.setREMARK("竞猜成功");
                    paymentService.reg(payment);
                }
            }
            RpcCommandResult rpcCommandResult = new RpcCommandResult();
            RpcReturnCode result = lotteryServerRpcService.noticeDrawLottery(roomId, playDetail.getGUESS_ID(), win);
            if (RpcReturnCode.SUCCESS == rpcCommandResult.getRpcReturnCode()) {
                log.info("通知成功");
            } else {
                log.info("通知失败");
            }
            rpcCommandResult.setRpcReturnCode(result);
            return rpcCommandResult;*/
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
            List<GuessDetailL> list = betGameService.getAllGuesser(playDetail.getGUESS_ID());
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
