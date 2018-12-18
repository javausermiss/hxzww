package com.fh.controller.wwjapp;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.service.system.pointsgoods.PointsGoodsManager;
import com.fh.service.system.pointsmall.PointsMallManager;
import com.fh.service.system.pointsreward.PointsRewardManager;
import com.fh.service.system.pointssendgoods.PointsSendGoodsManager;
import com.fh.service.system.userpoints.UserPointsManager;
import com.fh.util.*;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RespStatus;
import net.sf.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分商城
 *
 * @author wjy
 * @date 2018/08/03
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/app/pointsMall")
public class AppPointsMall extends BaseController {

    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "pointsmallService")
    private PointsMallManager pointsmallService;

    @Resource(name = "pointsrewardService")
    private PointsRewardManager pointsrewardService;

    @Resource(name = "userpointsService")
    private UserPointsManager userpointsService;

    @Resource(name = "pointsdetailService")
    private PointsDetailManager pointsdetailService;

    @Resource(name = "pointssendgoodsService")
    private PointsSendGoodsManager pointssendgoodsService;

    @Resource(name = "pointsgoodsService")
    private PointsGoodsManager pointsgoodsService;

    /**
     * 积分任务页面
     *
     * @return
     */
    @RequestMapping(value = "/pointsTask", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPointsReward(@RequestParam("userId") String userId) {
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return RespStatus.fail();
            }

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
            UserPoints userPoints = userpointsService.getUserPointsFinish(userId);
            userPoints.setCostGoldSum(Integer.valueOf(gm));

            List<PointsReward> list = pointsrewardService.getPointsReward();
            List<PointsMall> pointsMall = pointsmallService.getInfo();    //列出PointsMall列表

            Map<String, Object> map = new HashMap<>();
            map.put("pointsReward", list);
            map.put("userPoints", userPoints);
            map.put("pointsMall", pointsMall);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /**
     * 分享游戏
     *
     * @param userId
     * @return
     */

    @RequestMapping(value = "/shareGame", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject shareGame(@RequestParam("userId") String userId) {
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return RespStatus.fail("用户不存在");
            }

            //首先查询积分列表是否有该用户信息

            UserPoints userPoints = userpointsService.getUserPointsFinish(userId);
            PointsMall pointsMall = pointsmallService.getInfoById(Const.pointsMallType.points_type01.getValue());
            if (userPoints == null) {
                UserPoints regUserInfo = new UserPoints();
                regUserInfo.setId(MyUUID.getUUID32());
                regUserInfo.setUserId(userId);
                regUserInfo.setShareGame("1");
                regUserInfo.setTodayPoints(pointsMall.getPointsValue());
                userpointsService.regUserInfo(regUserInfo);
                appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());
                appuserService.updateAppUserBalanceById(appUser);

            } else {
                String tag = userPoints.getShareGame();
                if (tag.equals("0")) {
                    int a = userPoints.getTodayPoints();
                    Integer now_points = a + pointsMall.getPointsValue();
                    userPoints.setTodayPoints(now_points);
                    userPoints.setShareGame("1");
                    userpointsService.updateUserPoints(userPoints);

                    appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());
                    appuserService.updateAppUserBalanceById(appUser);


                    //增加积分记录
                    PointsDetail pointsDetail = new PointsDetail();
                    pointsDetail.setUserId(userId);
                    pointsDetail.setChannel(Const.pointsMallType.points_type01.getName());
                    pointsDetail.setType("+");
                    pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
                    pointsDetail.setPointsValue(pointsMall.getPointsValue());
                    pointsdetailService.regPointsDetail(pointsDetail);

                    //判断是否增加金币
                    String r_tag = userPoints.getPointsReward_Tag();
                    Integer goldValue = 0;
                    Integer sum = 0;
                    Integer ob = Integer.valueOf(appUser.getBALANCE());
                    Integer nb = 0;
                    List<PointsReward> list = pointsrewardService.getPointsReward();
                    String n_rtag = userpointsService.doGoldReward(r_tag, goldValue, sum, ob, list, now_points, nb, appUser);
                    userPoints.setPointsReward_Tag(n_rtag);
                    userpointsService.updateUserPoints(userPoints);
                }

            }
            return RespStatus.successs();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return RespStatus.fail();
        }
    }

    /**
     * 积分列表接口
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getPointsDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPointsDetail(@RequestParam("userId") String userId) {
        try {
            List<PointsDetail> list = pointsdetailService.getPointsDetail(userId);
            Map<String, Object> map = new HashMap<>();
            map.put("pointsDetail", list);
            if (list.size() == 0) {
                return RespStatus.successs().element("data", "no data");
            }
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }


    /**
     * 积分兑换礼品接口
     *
     * @param userId           用户ID
     * @param points           消费积分
     * @param goodsName        商品名称
     * @param consignee        收货人名字
     * @param consigneeAddress 地址
     * @param consigneePhone   电话
     * @return
     */
    @RequestMapping(value = "/exchangePointsGoods", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject exchangePointsGoods(@RequestParam("userId") String userId,
                                          @RequestParam("points") Integer points,
                                          @RequestParam("goodsName") String goodsName,
                                          @RequestParam("consignee") String consignee,
                                          @RequestParam("consigneeAddress") String consigneeAddress,
                                          @RequestParam("consigneePhone") String consigneePhone,
                                          @RequestParam("goodsNum") Integer goodsNum
    ) {

        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return RespStatus.fail("用户不存在");
            }

            Integer user_points = appUser.getPOINTS();
            if (user_points < points) {
                return RespStatus.successs().element("data", "积分不够");
            }
            Integer newp = user_points - points;
            appUser.setPOINTS(newp);
            appuserService.updateAppUserBalanceById(appUser);

            PointsSendGoods pointsSendGoods = new PointsSendGoods();
            pointsSendGoods.setUserId(userId);
            pointsSendGoods.setConsignee(consignee);
            pointsSendGoods.setConsigneeAddress(consigneeAddress);
            pointsSendGoods.setConsigneePhone(consigneePhone);
            pointsSendGoods.setGoodsName(goodsName);
            pointsSendGoods.setGoodsNum(goodsNum);
            pointssendgoodsService.regPointsSendGoods(pointsSendGoods);

            //增加积分消费记录
            PointsDetail pointsDetail = new PointsDetail();
            pointsDetail.setUserId(userId);
            pointsDetail.setChannel(Const.pointsMallType.points_type07.getName());
            pointsDetail.setType("-");
            pointsDetail.setPointsDetail_Id(MyUUID.getUUID32());
            pointsDetail.setPointsValue(points);
            pointsdetailService.regPointsDetail(pointsDetail);
            return RespStatus.successs().element("data", "兑换成功");

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }

    /**
     * 查询积分兑换礼品列表
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getPointsGoodsDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPointsGoodsDetail(@RequestParam("userId") String userId) {
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return RespStatus.fail("用户不存在");
            }
            List<PointsSendGoods> list = pointssendgoodsService.getPointsGoodsForUser(userId);
            Map<String, Object> map = new HashMap<>();
            map.put("PointsSendGoodsList", list);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }


    /**
     * 定时器。0点刷新用户的的积分记录表
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void flushUserPointsDetail() throws Exception {
        List<UserPoints> list = userpointsService.getAllUserPointsDetail();
        for (int i = 0; i < list.size(); i++) {
            UserPoints userPoints = list.get(i);
            userPoints.setTodayPoints(0);
            userPoints.setCostGoldSum_Tag("0");
            userPoints.setShareGame("0");
            userPoints.setInviteGame("0");
            userPoints.setPusherGame(0);
            userPoints.setFirstPay("0");
            userPoints.setLoginGame("0");
            userPoints.setPoohGame("0");
            userPoints.setPointsReward_Tag("0");
            userPoints.setBetGame("0");
            userpointsService.updateUserPoints(userPoints);
        }

    }

    /**
     * 安卓端获取积分商城首页URL
     *
     * @return
     */
    @RequestMapping(value = "/getPointsMallUrl", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPointsMallUrl(@RequestParam("userId") String userId) {
        return RespStatus.successs().element("data", PropertiesUtils.getCurrProperty("service.address")+"/pooh-web/html/integralShop/index.html?userId=" + userId);
    }

    /**
     *首页展示积分任务
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getPointsMallTask", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPointsMallTask(@RequestParam("userId") String userId) {
        return RespStatus.successs().element("data", PropertiesUtils.getCurrProperty("service.address")+"/pooh-web/html/integralShop/IntegralSouYeTask.html?" + userId);
    }

    /**
     * 获取商城首页信息
     *
     * @return
     */
    @RequestMapping(value = "/getPointsMallDetail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPointsMallDetail() {
        try {
            List<PageData> pageData = pointsgoodsService.listAll(new PageData());
            Map<String, Object> map = new HashMap<>();
            map.put("pointsGoodsList", pageData);
            return RespStatus.successs().element("data", map);

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }


    @RequestMapping(value = "/cc", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getPointsMalcc() {
        try {
            AppUser appUser1 = new AppUser();
            String face = PropertiesUtils.getCurrProperty("user.default.header.url"); //默认头像
            // appUser1.setBALANCE("3");
            appUser1.setPASSWORD(MD5.md5("123456"));
            appUser1.setNICKNAME("123");

            String n_userId = MyUUID.createSessionId();
            appUser1.setUSER_ID(n_userId);
            appUser1.setIMAGE_URL(face);
            appUser1.setUSERNAME("123");
            appUser1.setPHONE("123");
            appUser1.setBALANCE("15");
            appUser1.setCHANNEL_NUM("123");
            appUser1.setBDPHONE("123");
            appUser1.setPOINTS(11);
            appuserService.regAppUser(appUser1);
            AppUser aa =  appuserService.getUserByID(n_userId);
            aa.setTODAY_POOH(15);
            appuserService.updateAppUserBalanceById(aa);

            return aa.getUSER_ID();



        } catch (Exception e) {
            e.printStackTrace();
            return "F";
        }

    }
//100 150 225
    public static  void main(String[] A){
        int b = 475;
        int c  = 0;
        double a = 0;
        for (int i = 1; i < 1000 ; i++) {
            a = 200 * ( Math.pow(1.5,i) - 1);
            if (a >= b){
                if (b < a ){
                    c = i - 1 ;
                }else {
                    c = i ;
                }
                break ;
            }
        }
        System.out.println(a);
        System.out.println(c);

    }





}
