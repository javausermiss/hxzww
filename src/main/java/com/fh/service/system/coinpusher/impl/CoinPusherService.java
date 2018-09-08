package com.fh.service.system.coinpusher.impl;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.coinpusher.CoinPusherManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.service.system.pointsmall.PointsMallManager;
import com.fh.service.system.pointsreward.PointsRewardManager;
import com.fh.service.system.pushergamedetail.PusherGameDetailManager;
import com.fh.service.system.userpoints.UserPointsManager;
import com.fh.util.Const;
import com.fh.util.DateUtil;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RedisUtil;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcCommandResult;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 说明： 推币机游戏记录
 * 创建人：FH Q313596790
 * 创建时间：2018-05-30
 */
@Slf4j
@Service("coinpusherService")
public class CoinPusherService implements CoinPusherManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Resource(name = "dollService")
    private DollManager dollService;
    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "paymentService")
    private PaymentManager paymentService;
    @Resource(name = "userpointsService")
    private UserPointsManager userpointsService;
    @Resource(name = "pointsmallService")
    private PointsMallManager pointsmallService;
    @Resource(name = "pointsdetailService")
    private PointsDetailManager pointsdetailService;
    @Resource(name = "pointsrewardService")
    private PointsRewardManager pointsrewardService;
    @Resource(name="pushergamedetailService")
    private PusherGameDetailManager pushergamedetailService;


    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    @Override
    public void save(PageData pd) throws Exception {
        dao.save("CoinPusherMapper.save", pd);
    }

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    @Override
    public void delete(PageData pd) throws Exception {
        dao.delete("CoinPusherMapper.delete", pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    @Override
    public void edit(PageData pd) throws Exception {
        dao.update("CoinPusherMapper.edit", pd);
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override

    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("CoinPusherMapper.datalistPage", page);
    }

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("CoinPusherMapper.listAll", pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    @Override
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("CoinPusherMapper.findById", pd);
    }

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    @Override
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("CoinPusherMapper.deleteAll", ArrayDATA_IDS);
    }

    @Override
    public int reg(CoinPusher coinPusher) throws Exception {
        return (int) dao.save("CoinPusherMapper.reg", coinPusher);
    }

    @Override
    public CoinPusher getLatestRecord(CoinPusher coinPusher) throws Exception {
        return (CoinPusher) dao.findForObject("CoinPusherMapper.getLatestRecord", coinPusher);
    }

    @Override
    public int updateOutCoin(CoinPusher coinPusher) throws Exception {
        return (int) dao.update("CoinPusherMapper.updateOutCoin", coinPusher);
    }

    @Override
    public CoinPusher getLatestRecordForId(String roomId) throws Exception {
        return (CoinPusher) dao.findForObject("CoinPusherMapper.getLatestRecordForId", roomId);
    }

    @Override
    public List<CoinPusher> getCoinPusherRecondList(String userId) throws Exception {
        return (List<CoinPusher>) dao.findForList("CoinPusherMapper.getCoinPusherRecondList", userId);
    }

    @Override
    public CoinPusher getSumCoinOneDay(CoinPusher coinPusher) throws Exception {
        return (CoinPusher) dao.findForObject("CoinPusherMapper.getSumCoinOneDay", coinPusher);
    }

    @Override
    public RpcCommandResult doPusherGame(String roomId, String userId, Integer bat) throws Exception {
        log.info("用户:" + userId + "-------------------->" + roomId + "开始投币，数量为" + bat);
        RpcCommandResult rpcCommandResult = new RpcCommandResult();

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
            int costGold = bat;
            if (balance < costGold) {
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
            payment.setREMARK(doll.getDOLL_NAME() + "游戏");
            paymentService.reg(payment);

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


            UserPoints userPoints = userpointsService.getUserPointsFinish(userId);
            PointsMall pointsMall = pointsmallService.getInfoById(Const.pointsMallType.points_type04.getValue());

            int a = userPoints.getTodayPoints();
            int newpg = userPoints.getPusherGame() + 1;
            userPoints.setPusherGame(newpg);
            userpointsService.updateUserPoints(userPoints);

            if (newpg == 10) {
                userPoints.setTodayPoints(a + pointsMall.getPointsValue());
                appUser = appuserService.getUserByID(userId);
                appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());
                appuserService.updateAppUserBalanceById(appUser);
                //增加积分记录
                PointsDetail pointsDetail = new PointsDetail();
                pointsDetail.setUserId(userId);
                pointsDetail.setChannel(Const.pointsMallType.points_type04.getName());
                pointsDetail.setType("+");
                pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
                pointsDetail.setPointsValue(pointsMall.getPointsValue());
                pointsdetailService.regPointsDetail(pointsDetail);

                //判断是否增加金币
                String r_tag = userPoints.getPointsReward_Tag();
                if (Integer.valueOf(r_tag) < 5){
                    Integer goldValue = 0;
                    Integer sum = 0;
                    Integer ob = Integer.valueOf(appUser.getBALANCE());
                    Integer nb_2 = 0;
                    List<PointsReward> list = pointsrewardService.getPointsReward();
                    String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,userPoints.getTodayPoints() + pointsMall.getPointsValue(),nb_2,appUser);
                    userPoints.setPointsReward_Tag(n_rtag);
                    userpointsService.updateUserPoints(userPoints);
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
            if (cgs >= 200 && userPoints.getCostGoldSum_Tag().equals("0")) {
                //增加积分记录
                PointsDetail pointsDetail_cgs = new PointsDetail();
                pointsDetail_cgs.setUserId(userId);
                pointsDetail_cgs.setChannel(Const.pointsMallType.points_type05.getName());
                pointsDetail_cgs.setType("+");
                pointsDetail_cgs.setPointsDetail_Id(MyUUID.getUUID32());
                pointsDetail_cgs.setPointsValue(pointsMall.getPointsValue());
                pointsdetailService.regPointsDetail(pointsDetail_cgs);

                userPoints = userpointsService.getUserPointsFinish(userId);
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
        rpcCommandResult.setInfo(newId);
        return rpcCommandResult;
    }
}

