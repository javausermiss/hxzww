package com.fh.controller.wwjapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.fh.entity.system.*;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.service.system.pointsmall.PointsMallManager;
import com.fh.service.system.pointsreward.PointsRewardManager;
import com.fh.service.system.userpoints.UserPointsManager;
import com.fh.util.*;
import com.fh.util.wwjUtil.*;
import com.fh.util.xdpayutil.FormDateReportConvertor;
import com.fh.util.xdpayutil.MD5Facade;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.fh.alipay.AlipayConfig;
import com.fh.controller.base.BaseController;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.ordertest.OrderManager;
import com.fh.service.system.paycard.PaycardManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.playback.PlayBackManage;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.pond.PondManager;
import com.fh.service.system.promote.PromoteAppUserManager;
import com.fh.service.system.promotemanage.PromoteManageManager;
import com.fh.service.system.trans.AccountOperManager;

import net.sf.json.JSONObject;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/app/pay")
public class AppPayController extends BaseController {

    public static AlipayTradeAppPayResponse response;
    private static final String nurl = PropertiesUtils.getCurrProperty("service.address")+"/pooh-web/html/recharge/Recharge.html?userId=";
    private static final String furl = PropertiesUtils.getCurrProperty("service.address")+"/pooh-web/html/recharge/fistRecharge.html?userId=";

    //  private final String ckey = "y3WfBKF1FY4=";
    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "playBackService")
    private PlayBackManage playBackService;

    @Resource(name = "pondService")
    private PondManager pondService;

    @Resource(name = "playDetailService")
    private PlayDetailManage playDetailService;

    @Resource(name = "dollService")
    private DollManager dollService;

    @Resource(name = "paymentService")
    private PaymentManager paymentService;

    @Resource(name = "orderService")
    private OrderManager orderService;

    @Resource(name = "paycardService")
    private PaycardManager paycardService;

    /**
     * 渠道信息
     */
    @Resource(name = "promotemanageService")
    private PromoteManageManager promotemanageService;


    @Resource(name = "promoteAppUserService")
    public PromoteAppUserManager promoteAppUserService;

    @Resource(name = "accountOperService")
    public AccountOperManager accountOperService;


    @Resource(name = "userpointsService")
    private UserPointsManager userpointsService;

    @Resource(name = "pointsmallService")
    private PointsMallManager pointsmallService;

    @Resource(name = "pointsdetailService")
    private PointsDetailManager pointsdetailService;

    @Resource(name = "pointsrewardService")
    private PointsRewardManager pointsrewardService;


    /**
     * 个人信息
     *
     * @param phone
     * @return
     */
    public JSONObject getAppUserInfo(String phone) {
        try {
            AppUser appUser = appuserService.getUserByPhone(phone);
            return JSONObject.fromObject(appUser);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 个人信息
     *
     * @param userId
     * @return
     */

    public JSONObject getAppUserInfoById(String userId) {
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            return JSONObject.fromObject(appUser);
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 订单信息
     *
     * @param id
     * @return
     */

    public JSONObject getOrderInfo(String id) {
        try {
            Order o = orderService.getOrderById(id);
            return JSONObject.fromObject(o);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 获取动态充值卡信息
     *
     * @return
     */
    @RequestMapping(value = "/getPaycard", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getPaycard() {
        try {
            List<Paycard> list = paycardService.getPayCard();
            Map<String, Object> map = new HashMap<>();
            map.put("paycard", list);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }


    /**
     * 购买推广权益，提交订单接口
     *
     * @param userId
     * @param proManageId
     * @param ctype
     * @param channel
     * @return
     */

    @RequestMapping(value = "/commitPromoteOrderToGold", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject commitPromoteOrderToGold(
            @RequestParam("userId") String userId,
            @RequestParam("proManageId") String proManageId,
            @RequestParam(value = "ctype", required = false) String ctype,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "payType", required = false) String payType) {
        try {

            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return RespStatus.fail();
            }

            PageData proPd = promotemanageService.findById(proManageId);
            if (proPd == null) {
                return RespStatus.fail();
            }

            //判断当前用户金币余额
            int pay_gold = Integer.valueOf(proPd.getString("PAY_GOLD"));//扣减的金币数量
            int surplus_gold = Integer.valueOf(appUser.getBALANCE()) - pay_gold;
            if (surplus_gold < 0) {
                return RespStatus.fail("金币余额不足");
            }
            //权益分成管理表
            PageData promoteMgPd = promotemanageService.findById(proManageId);

            //权益分成
            PageData promotepd = promoteAppUserService.findByUserId(appUser.getUSER_ID());
            if (promotepd == null) {
                promotepd = new PageData();
                promotepd.put("USER_ID", appUser.getUSER_ID());
                promotepd.put("PRO_MANAGE_ID", promoteMgPd.get("PRO_MANAGE_ID"));
                promotepd.put("RETURN_RATIO", promoteMgPd.get("RETURN_RATIO"));

                promoteAppUserService.save(promotepd);
            } else {
                return RespStatus.fail("您已经购买过推广分成");
            }
            //扣减金币数
            appUser.setBALANCE(String.valueOf(surplus_gold));
            appuserService.updateAppUserBalanceById(appUser);

            //更新收支表
            Payment payment = new Payment();
            payment.setGOLD("-" + pay_gold);
            payment.setUSERID(appUser.getUSER_ID());
            payment.setDOLLID(null);
            payment.setCOST_TYPE(Const.PlayMentCostType.cost_type20.getValue());
            payment.setREMARK("购买权益扣减：" + pay_gold);
            paymentService.reg(payment);


            //用户现金账户开户
            accountOperService.openAccountInfByUser(appUser.getUSER_ID());

            //返回
            Map<String, Object> map = new HashMap<>();
            promotepd.put("PAY_GOLD", pay_gold); //当前购买金币数量
            map.put("promoteInf", promotepd);

            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }


    /**
     * 支付宝下单
     * <p>
     * //周卡
     * String wc = "wc";
     * //月卡
     * String mc = "mc";
     * //首充用户
     * String fc = "fc";
     * //正常充值
     * String nm = "nm";
     *
     * @param userId
     * @param amt
     * @param ctype
     * @param channel
     * @return
     */
    @RequestMapping(value = "/getTradeOrderAlipay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getTradeOrder(@RequestParam("userId") String userId,
                                    @RequestParam("amount") String amt,
                                    @RequestParam(value = "regGold") String regGold,
                                    @RequestParam(value = "ctype", required = false) String ctype,
                                    @RequestParam(value = "channel", required = false) String channel,
                                    @RequestParam(value = "payType", required = false) String payType,
                                    @RequestParam(value = "payOutType") String payOutType

    ) {

        String newOrder = "";
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return null;
            }
            String datetime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

            if (amt == null || amt.equals("")) {
                return null;
            }

            //amount = new DecimalFormat("#0.00").format(Double.valueOf(paycard.getAMOUNT()));//金额
            boolean a = RedisUtil.getRu().exists("tradeOrder");
            if (a) {
                String tradeOrder = RedisUtil.getRu().get("tradeOrder");
                String x = tradeOrder.substring(0, 8);//取前八位进行判断
                if (datetime.substring(0, 8).equals(x)) {
                    String six = tradeOrder.substring(tradeOrder.length() - 6, tradeOrder.length());
                    String newsix = String.format("%06d", (Integer.valueOf(six) + 1));
                    newOrder = datetime + newsix;//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                } else {
                    newOrder = datetime + "000001";//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                }
            } else {
                newOrder = datetime + "000001";//新的订单编号
                RedisUtil.getRu().set("tradeOrder", newOrder);
            }

            if (payOutType.equals("nm") &&
                    ((amt.equals("10") && regGold.equals("100"))
                            || (amt.equals("20") && regGold.equals("210"))
                            || (amt.equals("50") && regGold.equals("550"))
                            || (amt.equals("100") && regGold.equals("1125"))
                            || (amt.equals("200") && regGold.equals("2280"))
                            || (amt.equals("500") && regGold.equals("5750"))
                            || (amt.equals("1000") && regGold.equals("12000")))) {
                //若用户大于31级，则增加额外的30%
                int level = appUser.getLEVEL();
                if (level >= 31){
                    int regGold_level =  new Double(Integer.valueOf(regGold) * 0.3).intValue();
                    regGold = String.valueOf(regGold_level + Integer.valueOf(regGold)) ;
                }
                Order order = new Order();
                order.setUSER_ID(userId);
                order.setREC_ID(newOrder);
                order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
                order.setORDER_ID(newOrder);
                order.setREGGOLD(regGold); //充值的金币数量
                order.setCHANNEL(channel);
                order.setCTYPE(ctype);
                order.setPAY_TYPE(payType);
                order.setPRO_USER_ID(appUser.getPRO_USER_ID());
                order.setPAYOUT_TYPE(payOutType);
                orderService.regmount(order);

            } else if (payOutType.equals("fc") &&
                    ((amt.equals("10") && regGold.equals("200"))
                            || (amt.equals("20") && regGold.equals("400"))
                            || (amt.equals("50") && regGold.equals("1000"))
                            || (amt.equals("100") && regGold.equals("2000"))
                            || (amt.equals("200") && regGold.equals("4000"))
                            || (amt.equals("500") && regGold.equals("10000"))
                            || (amt.equals("1000") && regGold.equals("20000")))){
                Order order = new Order();
                order.setUSER_ID(userId);
                order.setREC_ID(newOrder);
                order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
                order.setORDER_ID(newOrder);
                order.setREGGOLD(regGold); //充值的金币数量
                order.setCHANNEL(channel);
                order.setCTYPE(ctype);
                order.setPAY_TYPE(payType);
                order.setPRO_USER_ID(appUser.getPRO_USER_ID());
                order.setPAYOUT_TYPE(payOutType);
                orderService.regmount(order);

            }else if (payOutType.equals("mc") && amt.equals("98") && regGold.equals("980")) {
                Order order = new Order();
                order.setUSER_ID(userId);
                order.setREC_ID(newOrder);
                order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
                order.setORDER_ID(newOrder);
                order.setREGGOLD(regGold); //充值的金币数量
                order.setCHANNEL(channel);
                order.setCTYPE(ctype);
                order.setPAY_TYPE(payType);
                order.setPRO_USER_ID(appUser.getPRO_USER_ID());
                order.setPAYOUT_TYPE(payOutType);
                orderService.regmount(order);

            } else if (payOutType.equals("wc") && amt.equals("28") && regGold.equals("280")){
                Order order = new Order();
                order.setUSER_ID(userId);
                order.setREC_ID(newOrder);
                order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
                order.setORDER_ID(newOrder);
                order.setREGGOLD(regGold); //充值的金币数量
                order.setCHANNEL(channel);
                order.setCTYPE(ctype);
                order.setPAY_TYPE(payType);
                order.setPRO_USER_ID(appUser.getPRO_USER_ID());
                order.setPAYOUT_TYPE(payOutType);
                orderService.regmount(order);
            } else {
                return RespStatus.fail("数据不合法");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("娃娃币");
        model.setSubject("汤姆抓娃娃支付");
        model.setOutTradeNo(newOrder);
        model.setTimeoutExpress("10m");
        model.setTotalAmount(amt);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AlipayConfig.notify_url);
        try {
            response = alipayClient.sdkExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();

        }

        Map<String, Object> map = new HashMap<>();
        map.put("alipay", response.getBody());
        return RespStatus.successs().element("data", map);
    }


    /**
     * 支付宝回调
     *
     * @param request
     * @param httpServletResponse
     * @return
     */
    @RequestMapping(value = "/AlipayCallBack", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String AlipayCallBack(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        logger.info("支付宝回调开始================>>>>>>>>>>>>>>>>");

        String out_trade_no = "";
        String amount = "";
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }


        try {
            boolean flag = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);

            logger.info("支付宝验签=======================>>>>>>>>>>>>>>>>>>" + flag);
            if (flag) {
                try {
                    //商户订单号
                    out_trade_no = params.get("out_trade_no");
                    Order o = orderService.getOrderById(out_trade_no);
                    logger.info("订单号为=================>>>>>>>>>>>>>>>>" + out_trade_no);
                    if (o == null) {
                        return "failure";
                    }

                    //实际收到的金额
                    amount = params.get("receipt_amount");
                    String am = NumberUtils.RMBYuanToCent(String.valueOf(amount));
                    String tb_amount = o.getREGAMOUNT();

                    logger.info("实际收到的金额为:" + am + "分，分为单位");
                    logger.info("订单支付的金额为:" + tb_amount + "分，分为单位");
                    if (!am.equals(tb_amount)) {
                        return "failure";
                    }
                    //校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方

                    String seller_id = params.get("seller_id");
                    if (!seller_id.equals(AlipayConfig.SELLER_ID)) {
                        return "failure";
                    }
                    //验证app_id是否为该商户本身

                    String app_id = params.get("app_id");
                    if (!app_id.equals(AlipayConfig.APPID)) {
                        return "failure";
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }
        if ("TRADE_SUCCESS".equals(params.get("trade_status")) || "TRADE_FINISHED".equals(params.get("trade_status"))) {
            logger.info("支付订单状态----------->>>>>>>>>>>>>>" + params.get("trade_status"));
            try {
                Order o = orderService.getOrderById(out_trade_no);
                AppUser appUser = appuserService.getUserByID(o.getUSER_ID());
                if (o.getSTATUS().equals("1")) {
                    return "success";
                }
                //周卡
                String wc = "wc";
                //月卡
                String mc = "mc";
                //首充用户
                String fc = "fc";
                //正常充值
                String nm = "nm";

                String rechare = o.getREGGOLD();
                int gold = Integer.valueOf(rechare);
                int nb = 0;

                if (o.getPAYOUT_TYPE().equals(wc)) {
                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+" + rechare);
                    payment.setUSERID(o.getUSER_ID());
                    payment.setDOLLID(null);
                    payment.setCOST_TYPE(Const.PlayMentCostType.cost_type19.getValue());
                    payment.setREMARK(Const.PlayMentCostType.cost_type19.getName());
                    paymentService.reg(payment);

                    Integer wd = appUser.getWEEKS_CARD();
                    String sdt = appUser.getWEEKS_CARD_TAG();
                    if (sdt.equals("1")) {
                        appUser.setWEEKS_CARD(wd + 7);
                        nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                    } else {
                        //更新收支表
                        Payment payment_1 = new Payment();
                        payment_1.setGOLD("+" + 20);
                        payment_1.setUSERID(o.getUSER_ID());
                        payment_1.setDOLLID(null);
                        payment_1.setCOST_TYPE(Const.PlayMentCostType.cost_type22.getValue());
                        payment_1.setREMARK(Const.PlayMentCostType.cost_type22.getName());
                        paymentService.reg(payment_1);
                        appUser.setWEEKS_CARD(wd + 6);
                        nb = Integer.valueOf(appUser.getBALANCE()) + gold + 20;
                    }
                    appUser.setWEEKS_CARD_TAG("1");
                    appUser.setBALANCE(String.valueOf(nb));
                    appuserService.updateAppUserBalanceById(appUser);

                }
                if (o.getPAYOUT_TYPE().equals(mc)) {
                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+" + rechare);
                    payment.setUSERID(o.getUSER_ID());
                    payment.setDOLLID(null);
                    payment.setCOST_TYPE(Const.PlayMentCostType.cost_type21.getValue());
                    payment.setREMARK(Const.PlayMentCostType.cost_type21.getName());
                    paymentService.reg(payment);

                    Integer wd = appUser.getMONTH_CARD();
                    String sdt = appUser.getMONTH_CARD_TAG();
                    if (sdt.equals("1")) {
                        appUser.setMONTH_CARD(wd + 30);
                        nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                    } else {
                        //更新收支表
                        Payment payment_2 = new Payment();
                        payment_2.setGOLD("+" + 33);
                        payment_2.setUSERID(o.getUSER_ID());
                        payment_2.setDOLLID(null);
                        payment_2.setCOST_TYPE(Const.PlayMentCostType.cost_type23.getValue());
                        payment_2.setREMARK(Const.PlayMentCostType.cost_type23.getName());
                        paymentService.reg(payment_2);
                        appUser.setMONTH_CARD(wd + 29);
                        nb = Integer.valueOf(appUser.getBALANCE()) + gold + 33;
                    }
                    appUser.setMONTH_CARD_TAG("1");
                    appUser.setBALANCE(String.valueOf(nb));
                    appuserService.updateAppUserBalanceById(appUser);


                }
                if (o.getPAYOUT_TYPE().equals(fc)) {
                    appUser.setFIRST_CHARGE("1");
                    nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                    appUser.setBALANCE(String.valueOf(nb));
                    appuserService.updateAppUserBalanceById(appUser);

                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+" + rechare);
                    payment.setUSERID(o.getUSER_ID());
                    payment.setDOLLID(null);
                    payment.setCOST_TYPE(Const.PlayMentCostType.cost_type24.getValue());
                    payment.setREMARK(Const.PlayMentCostType.cost_type24.getName());
                    paymentService.reg(payment);

                }
                if (o.getPAYOUT_TYPE().equals(nm)) {
                    nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                    appUser.setBALANCE(String.valueOf(nb));
                    appuserService.updateAppUserBalanceById(appUser);

                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+" + rechare);
                    payment.setUSERID(o.getUSER_ID());
                    payment.setDOLLID(null);
                    payment.setCOST_TYPE(Const.PlayMentCostType.cost_type05.getValue());
                    payment.setREMARK(Const.PlayMentCostType.cost_type05.getName());
                    paymentService.reg(payment);

                }

                //首先查询积分列表是否有该用户信息

                String userId = appUser.getUSER_ID();
                UserPoints userPoints = userpointsService.getUserPointsFinish(userId);
                PointsMall pointsMall = pointsmallService.getInfoById(Const.pointsMallType.points_type06.getValue());

                String tag = userPoints.getFirstPay();
                if (tag.equals("0")) {
                    int a = userPoints.getTodayPoints();
                    Integer now_points = a + pointsMall.getPointsValue();
                    userPoints.setTodayPoints(now_points);
                    userPoints.setFirstPay("1");
                    userpointsService.updateUserPoints(userPoints);

                    appUser = appuserService.getUserByID(userId);
                    appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());

                    //增加积分记录
                    PointsDetail pointsDetail = new PointsDetail();
                    pointsDetail.setUserId(userId);
                    pointsDetail.setChannel(Const.pointsMallType.points_type06.getName());
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
                        String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
                        userPoints.setPointsReward_Tag(n_rtag);
                        userpointsService.updateUserPoints(userPoints);
                    }

                }

                //当前订单的用户昵称
                o.setUserNickName(appUser.getNICKNAME());
                o.setREGGOLD(String.valueOf(gold));
                o.setORDER_NO(params.get("trade_no"));//支付宝交易凭证号
                o.setSTATUS("1");
                orderService.doRegCallbackUpdateOrder(o);
                //推广者进行结算
                int reg = Integer.valueOf(o.getREGAMOUNT())/100;
                String pro_user_id = appUser.getPRO_USER_ID();
                AppUser appUser1  =   appuserService.getUserByID(pro_user_id);
                if (appUser1 != null){
                    int ob_pro =  appUser1.getPRO_BALANCE();
                    int nb_pro = ob_pro + reg;
                    appUser1.setPRO_BALANCE(nb_pro);
                    appuserService.updateAppUserBalanceById(appUser1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "success";

    }
    /********************************************************************IOS支付宝H5支付******************************************************************************/

    /**
     * 支付宝下单
     * <p>
     * //周卡
     * String wc = "wc";
     * //月卡
     * String mc = "mc";
     * //首充用户
     * String fc = "fc";
     * //正常充值
     * String nm = "nm";
     *
     * @param userId
     * @param ctype
     * @param channel
     * @return
     */
    @RequestMapping(value = "/getTradeOrderAlipayForIos", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void getTradeOrderAlipayForIos(@RequestParam("userId") String userId,
                                          @RequestParam(value = "pid") String pid,
                                          @RequestParam(value = "ctype", required = false) String ctype,
                                          @RequestParam(value = "channel", required = false) String channel,
                                          @RequestParam(value = "payType", required = false) String payType,
                                          HttpServletResponse httpResponse

    ) {

        //周卡
        String wc = "wc";
        //月卡
        String mc = "mc";
        //首充用户
        String fc = "fc";
        //正常充值
        String nm = "nm";

        String newOrder = "";
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                //return null;
                return;
            }
            String tag = appUser.getFIRST_CHARGE();
            String datetime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());


            //amount = new DecimalFormat("#0.00").format(Double.valueOf(paycard.getAMOUNT()));//金额
            boolean a = RedisUtil.getRu().exists("tradeOrder");
            if (a) {
                String tradeOrder = RedisUtil.getRu().get("tradeOrder");
                String x = tradeOrder.substring(0, 8);//取前八位进行判断
                if (datetime.substring(0, 8).equals(x)) {
                    String six = tradeOrder.substring(tradeOrder.length() - 6, tradeOrder.length());
                    String newsix = String.format("%06d", (Integer.valueOf(six) + 1));
                    newOrder = datetime + newsix;//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                } else {
                    newOrder = datetime + "000001";//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                }
            } else {
                newOrder = datetime + "000001";//新的订单编号
                RedisUtil.getRu().set("tradeOrder", newOrder);
            }
            String regGold = "";
            String payOutType = "";
            Paycard paycard = paycardService.getPayCardById(pid);
            String _pid[] = {"1","2","3","4","5","6","7","8","9"};
            boolean iscon = Arrays.asList(_pid).contains(pid);
            if (!iscon){
                return;
            }
            String amt = paycard.getAMOUNT();
            if (pid.equals("8")) {
                regGold = paycard.getRECHARE();
                payOutType = wc;

            }
            if (pid.equals("9")) {
                regGold = paycard.getRECHARE();
                payOutType = mc;

            }

            if (!pid.equals("9") && !pid.equals("8") && tag.equals("0")) {
                regGold = paycard.getFIRSTAWARD_GOLD();
                payOutType = fc;
            }

            if (!pid.equals("9") && !pid.equals("8") && tag.equals("1")) {
                regGold = paycard.getGOLD();
                //若用户大于31级，则增加额外的30%
                int level = appUser.getLEVEL();
                if (level >= 31){
                    int regGold_level =  new Double(Integer.valueOf(regGold) * 0.3).intValue();
                    regGold = String.valueOf(regGold_level + Integer.valueOf(regGold)) ;
                }
                payOutType = nm;
            }
            Order order = new Order();
            order.setUSER_ID(userId);
            order.setREC_ID(newOrder);
            order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
            order.setORDER_ID(newOrder);
            order.setREGGOLD(regGold); //充值的金币数量
            order.setCHANNEL(channel);
            order.setCTYPE(ctype);
            order.setPAY_TYPE(payType);
            order.setPRO_USER_ID(appUser.getPRO_USER_ID());
            order.setPAYOUT_TYPE(payOutType);
            orderService.regmount(order);

            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                    AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            //alipayRequest.setReturnUrl(AlipayConfig.ReturnUrl);
            // 在公共参数中设置回跳和通知地址
            alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
            String out_trade_no = newOrder;
            alipayRequest.setBizContent("{" +
                    " \"out_trade_no\":\"" + out_trade_no + "\"," +
                    " \"total_amount\":\"" + amt + "\"," +
                    " \"subject\":\"娃娃币\"," +
                    " \"product_code\":\"QUICK_WAP_PAY\"" +
                    " }");//填充业务参数
            // 调用SDK生成表单

            try {
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                httpResponse.setContentType("text/html;charset=" + "UTF-8");
                httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
                httpResponse.getWriter().flush();
                httpResponse.getWriter().close();
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/getRecUrl", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getRecUrl(@RequestParam("userId") String userId,
                                @RequestParam(value = "appVersion" ,required = false,defaultValue = "default") String appVersion ) {
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return null;
            }
            String tag = appUser.getFIRST_CHARGE();
            if (tag.equals("1")) {
                return RespStatus.successs().element("url", nurl + userId+"&appVersion="+appVersion);

            } else {
                return RespStatus.successs().element("url", furl + userId+"&appVersion="+appVersion);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /********************************************************************现在支付******************************************************************************/

    /**
     * @param
     * @param userId  用户ID
     * @param （元）
     * @param ctype
     * @param channel
     * @param payType 默认R
     * @return
     */
    @RequestMapping(value = "/getTradeOrderxdpay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getTradeOrderxdpay(
            @RequestParam("userId") String userId,
            @RequestParam("amount") String amt,
            @RequestParam(value = "regGold") String regGold,
            @RequestParam(value = "ctype", required = false) String ctype,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "payType", required = false) String payType,
            @RequestParam(value = "payOutType") String payOutType,
            @RequestParam(value = "payChannelType") String payChannelType
    ) {
        try {
            String newOrder = "";
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return null;
            }
            String datetime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

            if (amt == null || amt.equals("")) {
                return null;
            }

            //amount = new DecimalFormat("#0.00").format(Double.valueOf(paycard.getAMOUNT()));//金额
            boolean a = RedisUtil.getRu().exists("tradeOrder");
            if (a) {
                String tradeOrder = RedisUtil.getRu().get("tradeOrder");
                String x = tradeOrder.substring(0, 8);//取前八位进行判断
                if (datetime.substring(0, 8).equals(x)) {
                    String six = tradeOrder.substring(tradeOrder.length() - 6, tradeOrder.length());
                    String newsix = String.format("%06d", (Integer.valueOf(six) + 1));
                    newOrder = datetime + newsix;//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                } else {
                    newOrder = datetime + "000001";//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                }
            } else {
                newOrder = datetime + "000001";//新的订单编号
                RedisUtil.getRu().set("tradeOrder", newOrder);
            }
            Order order = new Order();
            if (payOutType.equals("nm") &&
                    ((amt.equals("10") && regGold.equals("100"))
                            || (amt.equals("20") && regGold.equals("210"))
                            || (amt.equals("50") && regGold.equals("550"))
                            || (amt.equals("100") && regGold.equals("1125"))
                            || (amt.equals("200") && regGold.equals("2280"))
                            || (amt.equals("500") && regGold.equals("5750"))
                            || (amt.equals("1000") && regGold.equals("12000")))) {

                //若用户大于31级，则增加额外的30%
                int level = appUser.getLEVEL();
                if (level >= 31){
                    int regGold_level =  new Double(Integer.valueOf(regGold) * 0.3).intValue();
                    regGold = String.valueOf(regGold_level + Integer.valueOf(regGold)) ;
                }

                order.setUSER_ID(userId);
                order.setREC_ID(newOrder);
                order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
                order.setORDER_ID(newOrder);
                order.setREGGOLD(regGold); //充值的金币数量
                order.setCHANNEL(channel);
                order.setCTYPE(ctype);
                order.setPAY_TYPE(payType);
                order.setPRO_USER_ID(appUser.getPRO_USER_ID());
                order.setPAYOUT_TYPE(payOutType);
                orderService.regmount(order);

            } else {
                return RespStatus.fail("数据不合法");
            }
            if (payOutType.equals("fc") &&
                    ((amt.equals("10") && regGold.equals("200"))
                            || (amt.equals("20") && regGold.equals("400"))
                            || (amt.equals("50") && regGold.equals("1000"))
                            || (amt.equals("100") && regGold.equals("2000"))
                            || (amt.equals("200") && regGold.equals("4000"))
                            || (amt.equals("500") && regGold.equals("10000"))
                            || (amt.equals("1000") && regGold.equals("20000")))) {

                order.setUSER_ID(userId);
                order.setREC_ID(newOrder);
                order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
                order.setORDER_ID(newOrder);
                order.setREGGOLD(regGold); //充值的金币数量
                order.setCHANNEL(channel);
                order.setCTYPE(ctype);
                order.setPAY_TYPE(payType);
                order.setPRO_USER_ID(appUser.getPRO_USER_ID());
                order.setPAYOUT_TYPE(payOutType);
                orderService.regmount(order);

            } else {
                return RespStatus.fail("数据不合法");
            }
            if (payOutType.equals("mc") && amt.equals("98") && regGold.equals("980")) {

                order.setUSER_ID(userId);
                order.setREC_ID(newOrder);
                order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
                order.setORDER_ID(newOrder);
                order.setREGGOLD(regGold); //充值的金币数量
                order.setCHANNEL(channel);
                order.setCTYPE(ctype);
                order.setPAY_TYPE(payType);
                order.setPRO_USER_ID(appUser.getPRO_USER_ID());
                order.setPAYOUT_TYPE(payOutType);
                orderService.regmount(order);

            } else {
                return RespStatus.fail("数据不合法");
            }
            if (payOutType.equals("wc") && amt.equals("28") && regGold.equals("280")) {
                order.setUSER_ID(userId);
                order.setREC_ID(newOrder);
                order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
                order.setORDER_ID(newOrder);
                order.setREGGOLD(regGold); //充值的金币数量
                order.setCHANNEL(channel);
                order.setCTYPE(ctype);
                order.setPAY_TYPE(payType);
                order.setPRO_USER_ID(appUser.getPRO_USER_ID());
                order.setPAYOUT_TYPE(payOutType);
                orderService.regmount(order);

            } else {
                return RespStatus.fail("数据不合法");
            }


            Map<String, Object> map = new HashMap<>();
            map.put("funcode", "WP001");//
            map.put("version", "1.0.0");//
            map.put("appId", PropertiesUtils.getCurrProperty("nowpay.appId"));//
            map.put("mhtOrderNo", newOrder);//
            map.put("mhtOrderName", "娃娃币");//
            map.put("mhtOrderType", "01");//
            map.put("mhtCurrencyType", "156");//
            map.put("mhtOrderAmt", Integer.valueOf(NumberUtils.RMBYuanToCent(amt)));//
            map.put("mhtOrderStartTime", DateUtil.getSdfTimes());//
            map.put("mhtOrderTimeOut", 3600);
            map.put("frontNotifyUrl", "http://sds");
            map.put("notifyUrl", PropertiesUtils.getCurrProperty("service.address") + "/pooh-web/app/pay/xdpayCallBack");//
            map.put("mhtCharset", "UTF-8");//
            map.put("deviceType", "01");//
            map.put("payChannelType", payChannelType);//
            map.put("consumerId", userId);//.......
            map.put("consumerName", appUser.getNICKNAME());//........
            map.put("mhtSignType", "MD5");//
            //map.put("outputType", "2");//
            map.put("mhtOrderDetail", "娃娃币");
            String signature = MD5Facade.getFormDataParamMD5(map, PropertiesUtils.getCurrProperty("nowpay.md5Key"), "UTF-8");
            map.put("mhtSignature", signature);//
            FormDateReportConvertor.postBraceFormLinkReportWithURLEncode(map, "UTF-8");

            StringBuilder toMD5StringBuilder = new StringBuilder();
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                String value = map.get(key).toString();
                if (value != null && value.length() > 0) {
                    toMD5StringBuilder.append(key + "=" + value + "&");
                }
            }
            toMD5StringBuilder.delete(toMD5StringBuilder.length() - 1, toMD5StringBuilder.length());
            String s = toMD5StringBuilder.toString();
            logger.info("发送的参数" + s);
            // String Code = NowPayUtil.doPost(s);
            // logger.info("Code---------------" + Code);
            Map<String, Object> map_1 = new HashMap<>();
            map_1.put("Order", getOrderInfo(order.getORDER_ID()));
            return RespStatus.successs().element("data", map_1).element("nowpayData", s);

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    @RequestMapping(value = "/xdpayCallBack", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String xdpayCallBack(HttpServletRequest req) {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder reportBuilder = new StringBuilder();
            String tempStr = "";
            while ((tempStr = reader.readLine()) != null) {
                reportBuilder.append(tempStr);
            }

            String reportContent = reportBuilder.toString();

            Map<String, String> dataMap = FormDateReportConvertor.parseFormDataPatternReportWithDecode(reportContent, "UTF-8", "UTF-8");

            //dataMap.remove("signType");
            String signature = dataMap.remove("signature");

           /* InputStream propertiesInput = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
            Properties properties = new Properties();
            properties.load(propertiesInput);
            String md5Key = (String) properties.get("md5Key");*/
            String md5Key = PropertiesUtils.getCurrProperty("nowpay.md5Key");
            boolean isValidSignature = MD5Facade.validateFormDataParamMD5(dataMap, md5Key, signature);
            logger.info("验签结果：----------------------------->" + isValidSignature);
            if (isValidSignature) {

                String out_trade_no = dataMap.get("mhtOrderNo");
                String nowPayOrderNo = dataMap.get("nowPayOrderNo");
                Order o = orderService.getOrderById(out_trade_no);
                AppUser appUser = appuserService.getUserByID(o.getUSER_ID());
                if (o.getSTATUS().equals("1")) {
                    return "success=Y";
                }
                try {
                    //周卡
                    String wc = "wc";
                    //月卡
                    String mc = "mc";
                    //首充用户
                    String fc = "fc";
                    //正常充值
                    String nm = "nm";

                    String rechare = o.getREGGOLD();
                    int gold = Integer.valueOf(rechare);
                    int nb = 0;

                    if (o.getPAYOUT_TYPE().equals(wc)) {
                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+" + rechare);
                        payment.setUSERID(o.getUSER_ID());
                        payment.setDOLLID(null);
                        payment.setCOST_TYPE(Const.PlayMentCostType.cost_type19.getValue());
                        payment.setREMARK(Const.PlayMentCostType.cost_type19.getName());
                        paymentService.reg(payment);

                        Integer wd = appUser.getWEEKS_CARD();
                        String sdt = appUser.getWEEKS_CARD_TAG();
                        if (sdt.equals("1")) {
                            appUser.setWEEKS_CARD(wd + 7);
                            nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                        } else {
                            //更新收支表
                            Payment payment_1 = new Payment();
                            payment_1.setGOLD("+" + 20);
                            payment_1.setUSERID(o.getUSER_ID());
                            payment_1.setDOLLID(null);
                            payment_1.setCOST_TYPE(Const.PlayMentCostType.cost_type22.getValue());
                            payment_1.setREMARK(Const.PlayMentCostType.cost_type22.getName());
                            paymentService.reg(payment_1);
                            appUser.setWEEKS_CARD(wd + 6);
                            nb = Integer.valueOf(appUser.getBALANCE()) + gold + 20;
                        }
                        appUser.setWEEKS_CARD_TAG("1");
                        appUser.setBALANCE(String.valueOf(nb));
                        appuserService.updateAppUserBalanceById(appUser);

                    }
                    if (o.getPAYOUT_TYPE().equals(mc)) {
                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+" + rechare);
                        payment.setUSERID(o.getUSER_ID());
                        payment.setDOLLID(null);
                        payment.setCOST_TYPE(Const.PlayMentCostType.cost_type21.getValue());
                        payment.setREMARK(Const.PlayMentCostType.cost_type21.getName());
                        paymentService.reg(payment);

                        Integer wd = appUser.getMONTH_CARD();
                        String sdt = appUser.getMONTH_CARD_TAG();
                        if (sdt.equals("1")) {
                            appUser.setMONTH_CARD(wd + 30);
                            nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                        } else {
                            //更新收支表
                            Payment payment_2 = new Payment();
                            payment_2.setGOLD("+" + 33);
                            payment_2.setUSERID(o.getUSER_ID());
                            payment_2.setDOLLID(null);
                            payment_2.setCOST_TYPE(Const.PlayMentCostType.cost_type23.getValue());
                            payment_2.setREMARK(Const.PlayMentCostType.cost_type23.getName());
                            paymentService.reg(payment_2);
                            appUser.setMONTH_CARD(wd + 29);
                            nb = Integer.valueOf(appUser.getBALANCE()) + gold + 33;
                        }
                        appUser.setMONTH_CARD_TAG("1");
                        appUser.setBALANCE(String.valueOf(nb));
                        appuserService.updateAppUserBalanceById(appUser);


                    }
                    if (o.getPAYOUT_TYPE().equals(fc)) {
                        appUser.setFIRST_CHARGE("1");
                        nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                        appUser.setBALANCE(String.valueOf(nb));
                        appuserService.updateAppUserBalanceById(appUser);

                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+" + rechare);
                        payment.setUSERID(o.getUSER_ID());
                        payment.setDOLLID(null);
                        payment.setCOST_TYPE(Const.PlayMentCostType.cost_type24.getValue());
                        payment.setREMARK(Const.PlayMentCostType.cost_type24.getName());
                        paymentService.reg(payment);

                    }
                    if (o.getPAYOUT_TYPE().equals(nm)) {
                        nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                        appUser.setBALANCE(String.valueOf(nb));
                        appuserService.updateAppUserBalanceById(appUser);

                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+" + rechare);
                        payment.setUSERID(o.getUSER_ID());
                        payment.setDOLLID(null);
                        payment.setCOST_TYPE(Const.PlayMentCostType.cost_type05.getValue());
                        payment.setREMARK(Const.PlayMentCostType.cost_type05.getName());
                        paymentService.reg(payment);

                    }

                    //首先查询积分列表是否有该用户信息

                    String userId = appUser.getUSER_ID();
                    UserPoints userPoints = userpointsService.getUserPointsFinish(userId);
                    PointsMall pointsMall = pointsmallService.getInfoById(Const.pointsMallType.points_type06.getValue());

                    String tag = userPoints.getFirstPay();
                    if (tag.equals("0")) {
                        int a = userPoints.getTodayPoints();
                        Integer now_points = a + pointsMall.getPointsValue();
                        userPoints.setTodayPoints(now_points);
                        userPoints.setFirstPay("1");
                        userpointsService.updateUserPoints(userPoints);

                        appUser = appuserService.getUserByID(userId);
                        appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());

                        //增加积分记录
                        PointsDetail pointsDetail = new PointsDetail();
                        pointsDetail.setUserId(userId);
                        pointsDetail.setChannel(Const.pointsMallType.points_type06.getName());
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
                            String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
                            userPoints.setPointsReward_Tag(n_rtag);
                            userpointsService.updateUserPoints(userPoints);
                        }
                    }

                    //当前订单的用户昵称
                    o.setUserNickName(appUser.getNICKNAME());
                    o.setREGGOLD(String.valueOf(gold));
                    o.setORDER_NO(nowPayOrderNo);//现在支付交易凭证号
                    o.setSTATUS("1");
                    orderService.doRegCallbackUpdateOrder(o);
                    //推广者进行结算
                    int reg = Integer.valueOf(o.getREGAMOUNT())/100;
                    String pro_user_id = appUser.getPRO_USER_ID();
                    AppUser appUser1  =  appuserService.getUserByID(pro_user_id);
                    if (appUser1 != null){
                        int ob_pro =  appUser1.getPRO_BALANCE();
                        int nb_pro = ob_pro + reg;
                        appUser1.setPRO_BALANCE(nb_pro);
                        appuserService.updateAppUserBalanceById(appUser1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "success=Y";
            } else {
                return "success=N";
            }
        } catch (Exception e) {
            logger.info("程序异常" + e);
            e.printStackTrace();
            return "error";
        }

    }


    /*****************************************************************支付宝订单新接口20180820*******************************************************************************************************/
    /**
     * @param userId
     * @param pid
     * @param ctype
     * @param channel
     * @param payType
     * @param appVersion
     * @return
     */
    @RequestMapping(value = "/getTradeOrderAlipayVer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getTradeOrderAlipayVer(@RequestParam("userId") String userId,
                                             @RequestParam(value = "pid") String pid,
                                             @RequestParam(value = "ctype", required = false) String ctype,
                                             @RequestParam(value = "channel", required = false) String channel,
                                             @RequestParam(value = "payType", required = false) String payType,
                                             @RequestParam(value = "appVersion", required = false) String appVersion

    ) {

        //周卡
        String wc = "wc";
        //月卡
        String mc = "mc";
        //首充用户
        String fc = "fc";
        //正常充值
        String nm = "nm";

        String newOrder = "";
        String amt = "";
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return null;
            }
            String tag = appUser.getFIRST_CHARGE();
            String datetime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());


            //amount = new DecimalFormat("#0.00").format(Double.valueOf(paycard.getAMOUNT()));//金额
            boolean a = RedisUtil.getRu().exists("tradeOrder");
            if (a) {
                String tradeOrder = RedisUtil.getRu().get("tradeOrder");
                String x = tradeOrder.substring(0, 8);//取前八位进行判断
                if (datetime.substring(0, 8).equals(x)) {
                    String six = tradeOrder.substring(tradeOrder.length() - 6, tradeOrder.length());
                    String newsix = String.format("%06d", (Integer.valueOf(six) + 1));
                    newOrder = datetime + newsix;//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                } else {
                    newOrder = datetime + "000001";//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                }
            } else {
                newOrder = datetime + "000001";//新的订单编号
                RedisUtil.getRu().set("tradeOrder", newOrder);
            }
            String regGold = "";
            String payOutType = "";
            Paycard paycard = paycardService.getPayCardById(pid);
            amt = paycard.getAMOUNT();

            if (pid.equals("8")) {
                regGold = paycard.getRECHARE();
                payOutType = wc;

            }
            if (pid.equals("9")) {
                regGold = paycard.getRECHARE();
                payOutType = mc;

            }
            if (!pid.equals("9") && !pid.equals("8") && tag.equals("0")) {
                regGold = paycard.getFIRSTAWARD_GOLD();
                payOutType = fc;
            }

            if (!pid.equals("9") && !pid.equals("8") && tag.equals("1")) {
                regGold = paycard.getGOLD();
                //若用户大于31级，则增加额外的30%
                int level = appUser.getLEVEL();
                if (level >= 31){
                    int regGold_level =  new Double(Integer.valueOf(regGold) * 0.3).intValue();
                    regGold = String.valueOf(regGold_level + Integer.valueOf(regGold)) ;
                }
                payOutType = nm;
            }

            Order order = new Order();
            order.setUSER_ID(userId);
            order.setREC_ID(newOrder);
            order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
            order.setORDER_ID(newOrder);
            order.setREGGOLD(regGold); //充值的金币数量
            order.setCHANNEL(channel);
            order.setCTYPE(ctype);
            order.setPAY_TYPE(payType);
            order.setPRO_USER_ID(appUser.getPRO_USER_ID());
            order.setPAYOUT_TYPE(payOutType);
            order.setAPPVERSION(appVersion);
            orderService.regmount(order);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("娃娃币");
        model.setSubject("汤姆抓娃娃支付");
        model.setOutTradeNo(newOrder);
        model.setTimeoutExpress("10m");
        model.setTotalAmount(amt);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AlipayConfig.notify_url);
        try {
            response = alipayClient.sdkExecute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();

        }

        Map<String, Object> map = new HashMap<>();
        map.put("alipay", response.getBody());
        return RespStatus.successs().element("data", map);
    }


    /******************************************************************现在支付App微信新接口20180820************************************************************************************************************************/

    @RequestMapping(value = "/getTradeOrderxdpayVer", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getTradeOrderxdpayVer(
            @RequestParam("userId") String userId,
            @RequestParam(value = "pid") String pid,
            @RequestParam(value = "ctype", required = false) String ctype,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "payType", required = false) String payType,
            @RequestParam(value = "appVersion", required = false) String appVersion,
            @RequestParam(value = "payChannelType") String payChannelType
    ) {
        //周卡
        String wc = "wc";
        //月卡
        String mc = "mc";
        //首充用户
        String fc = "fc";
        //正常充值
        String nm = "nm";

        String newOrder = "";
        String amt = "";
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return null;
            }
            String tag = appUser.getFIRST_CHARGE();
            String datetime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

            boolean a = RedisUtil.getRu().exists("tradeOrder");
            if (a) {
                String tradeOrder = RedisUtil.getRu().get("tradeOrder");
                String x = tradeOrder.substring(0, 8);//取前八位进行判断
                if (datetime.substring(0, 8).equals(x)) {
                    String six = tradeOrder.substring(tradeOrder.length() - 6, tradeOrder.length());
                    String newsix = String.format("%06d", (Integer.valueOf(six) + 1));
                    newOrder = datetime + newsix;//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                } else {
                    newOrder = datetime + "000001";//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                }
            } else {
                newOrder = datetime + "000001";//新的订单编号
                RedisUtil.getRu().set("tradeOrder", newOrder);
            }
            String regGold = "";
            String payOutType = "";
            Paycard paycard = paycardService.getPayCardById(pid);
            amt = paycard.getAMOUNT();

            String _pid[] = {"1","2","3","4","5","6","7","8","9"};
            boolean iscon = Arrays.asList(_pid).contains(pid);
            if (!iscon){
                return RespStatus.fail("参数异常");
            }

            if (pid.equals("8")) {
                regGold = paycard.getRECHARE();
                payOutType = wc;

            }
            if (pid.equals("9")) {
                regGold = paycard.getRECHARE();
                payOutType = mc;

            }
            if (!pid.equals("9") && !pid.equals("8") && tag.equals("0")) {
                regGold = paycard.getFIRSTAWARD_GOLD();
                payOutType = fc;
            }

            if (!pid.equals("9") && !pid.equals("8") && tag.equals("1")) {
                regGold = paycard.getGOLD();
                //若用户大于31级，则增加额外的30%
                int level = appUser.getLEVEL();
                if (level >= 31){
                    int regGold_level =  new Double(Integer.valueOf(regGold) * 0.3).intValue();
                    regGold = String.valueOf(regGold_level + Integer.valueOf(regGold)) ;
                }
                payOutType = nm;
            }

            Order order = new Order();
            order.setUSER_ID(userId);
            order.setREC_ID(newOrder);
            order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
            order.setORDER_ID(newOrder);
            order.setREGGOLD(regGold); //充值的金币数量
            order.setCHANNEL(channel);
            order.setCTYPE(ctype);
            order.setPAY_TYPE(payType);
            order.setPRO_USER_ID(appUser.getPRO_USER_ID());
            order.setPAYOUT_TYPE(payOutType);
            order.setAPPVERSION(appVersion);
            orderService.regmount(order);

            Map<String, Object> map = new HashMap<>();
            map.put("funcode", "WP001");//
            map.put("version", "1.0.0");//
            map.put("appId", PropertiesUtils.getCurrProperty("nowpay.appId"));//
            map.put("mhtOrderNo", newOrder);//
            map.put("mhtOrderName", "娃娃币");//
            map.put("mhtOrderType", "01");//
            map.put("mhtCurrencyType", "156");//
            map.put("mhtOrderAmt", Integer.valueOf(NumberUtils.RMBYuanToCent(amt)));//
            map.put("mhtOrderStartTime", DateUtil.getSdfTimes());//
            map.put("mhtOrderTimeOut", 3600);
            map.put("frontNotifyUrl", "http://sds");
            map.put("notifyUrl", PropertiesUtils.getCurrProperty("service.address") + "/pooh-web/app/pay/xdpayCallBack");//
            map.put("mhtCharset", "UTF-8");//
            map.put("deviceType", "01");//
            map.put("payChannelType", payChannelType);//
            map.put("consumerId", userId);//.......
            map.put("consumerName", appUser.getNICKNAME());//........
            map.put("mhtSignType", "MD5");//
            map.put("mhtOrderDetail", "娃娃币");
            String signature = MD5Facade.getFormDataParamMD5(map, PropertiesUtils.getCurrProperty("nowpay.md5Key"), "UTF-8");
            map.put("mhtSignature", signature);//
            FormDateReportConvertor.postBraceFormLinkReportWithURLEncode(map, "UTF-8");

            StringBuilder toMD5StringBuilder = new StringBuilder();
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                String value = map.get(key).toString();
                if (value != null && value.length() > 0) {
                    toMD5StringBuilder.append(key + "=" + value + "&");
                }
            }
            toMD5StringBuilder.delete(toMD5StringBuilder.length() - 1, toMD5StringBuilder.length());
            String s = toMD5StringBuilder.toString();
            logger.info("发送的参数------------>" + s);
            Map<String, Object> map_1 = new HashMap<>();
            map_1.put("Order", getOrderInfo(order.getORDER_ID()));
            return RespStatus.successs().element("data", map_1).element("nowpayData", s);
        }
        catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }
    }
    /******************************************************************现在支付h5微信新接口IOS************************************************************************************************************************/

    @RequestMapping(value = "/getTradeOrderxdpayIOS", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getTradeOrderxdpayIOS(
            @RequestParam("userId") String userId,
            @RequestParam(value = "pid") String pid,
            @RequestParam(value = "ctype", required = false) String ctype,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "payType", required = false) String payType,
            @RequestParam(value = "appVersion", required = false) String appVersion,
            @RequestParam(value = "payChannelType") String payChannelType
    ) {
        //周卡
        String wc = "wc";
        //月卡
        String mc = "mc";
        //首充用户
        String fc = "fc";
        //正常充值
        String nm = "nm";

        String newOrder = "";
        String amt = "";
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return null;
            }
            String tag = appUser.getFIRST_CHARGE();
            String datetime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

            boolean a = RedisUtil.getRu().exists("tradeOrder");
            if (a) {
                String tradeOrder = RedisUtil.getRu().get("tradeOrder");
                String x = tradeOrder.substring(0, 8);//取前八位进行判断
                if (datetime.substring(0, 8).equals(x)) {
                    String six = tradeOrder.substring(tradeOrder.length() - 6, tradeOrder.length());
                    String newsix = String.format("%06d", (Integer.valueOf(six) + 1));
                    newOrder = datetime + newsix;//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                } else {
                    newOrder = datetime + "000001";//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                }
            } else {
                newOrder = datetime + "000001";//新的订单编号
                RedisUtil.getRu().set("tradeOrder", newOrder);
            }
            String regGold = "";
            String payOutType = "";
            Paycard paycard = paycardService.getPayCardById(pid);
            amt = paycard.getAMOUNT();

            String _pid[] = {"1","2","3","4","5","6","7","8","9"};
            boolean iscon = Arrays.asList(_pid).contains(pid);
            if (!iscon){
                return RespStatus.fail("参数异常");
            }

            if (pid.equals("8")) {
                regGold = paycard.getRECHARE();
                payOutType = wc;

            }
            if (pid.equals("9")) {
                regGold = paycard.getRECHARE();
                payOutType = mc;

            }
            if (!pid.equals("9") && !pid.equals("8") && tag.equals("0")) {
                regGold = paycard.getFIRSTAWARD_GOLD();
                payOutType = fc;
            }

            if (!pid.equals("9") && !pid.equals("8") && tag.equals("1")) {
                regGold = paycard.getGOLD();
                //若用户大于31级，则增加额外的30%
                int level = appUser.getLEVEL();
                if (level >= 31){
                    int regGold_level =  new Double(Integer.valueOf(regGold) * 0.3).intValue();
                    regGold = String.valueOf(regGold_level + Integer.valueOf(regGold)) ;
                }
                payOutType = nm;
            }

            Order order = new Order();
            order.setUSER_ID(userId);
            order.setREC_ID(newOrder);
            order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amt)); //元转分
            order.setORDER_ID(newOrder);
            order.setREGGOLD(regGold); //充值的金币数量
            order.setCHANNEL(channel);
            order.setCTYPE(ctype);
            order.setPAY_TYPE(payType);
            order.setPRO_USER_ID(appUser.getPRO_USER_ID());
            order.setPAYOUT_TYPE(payOutType);
            order.setAPPVERSION(appVersion);
            orderService.regmount(order);

            Map<String, Object> map = new HashMap<>();
            map.put("funcode", "WP001");//
            map.put("version", "1.0.0");//
            map.put("appId", PropertiesUtils.getCurrProperty("nowpay.h5.appId"));//
            map.put("mhtOrderNo", newOrder);//
            map.put("mhtOrderName", "娃娃币");//
            map.put("mhtOrderType", "01");//
            map.put("mhtCurrencyType", "156");//
            map.put("mhtOrderAmt", Integer.valueOf(NumberUtils.RMBYuanToCent(amt)));//
            map.put("mhtOrderStartTime", DateUtil.getSdfTimes());//
            map.put("mhtOrderTimeOut", 3600);
            map.put("frontNotifyUrl", "http://sds");
            map.put("notifyUrl", PropertiesUtils.getCurrProperty("service.address") + "/pooh-web/app/pay/xdpayCallBackIOS");//
            map.put("mhtCharset", "UTF-8");//
            map.put("deviceType", "0601");//
            map.put("outputType","2");
            map.put("payChannelType", payChannelType);//
            map.put("consumerId", userId);//.......
            map.put("consumerName", appUser.getNICKNAME());//........
            map.put("mhtSignType", "MD5");//
            map.put("mhtOrderDetail", "娃娃币");
            String signature = MD5Facade.getFormDataParamMD5(map, PropertiesUtils.getCurrProperty("nowpay.h5.md5Key"), "UTF-8");
            map.put("mhtSignature", signature);//
            FormDateReportConvertor.postBraceFormLinkReportWithURLEncode(map, "UTF-8");

            StringBuilder toMD5StringBuilder = new StringBuilder();
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                String value = map.get(key).toString();
                if (value != null && value.length() > 0) {
                    toMD5StringBuilder.append(key + "=" + value + "&");
                }
            }
            toMD5StringBuilder.delete(toMD5StringBuilder.length() - 1, toMD5StringBuilder.length());
            String s = toMD5StringBuilder.toString();
            logger.info("发送的参数------------>" + s);
            String msg = NowPayUtil.doPost(s);
            msg =  URLDecoder.decode(msg,"UTF-8");
            String payUrl = msg;
            int begin = msg.indexOf("weixin:");
            int end = msg.indexOf("&appId");
            payUrl = payUrl.substring(begin,end);
            return RespStatus.successs().element("data", payUrl);
        }
        catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /**
     * 现在微信支付H5回调接口
     * @param req
     * @return
     */
    @RequestMapping(value = "/xdpayCallBackIOS", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String xdpayCallBackIOS(HttpServletRequest req) {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder reportBuilder = new StringBuilder();
            String tempStr = "";
            while ((tempStr = reader.readLine()) != null) {
                reportBuilder.append(tempStr);
            }

            String reportContent = reportBuilder.toString();

            Map<String, String> dataMap = FormDateReportConvertor.parseFormDataPatternReportWithDecode(reportContent, "UTF-8", "UTF-8");

            //dataMap.remove("signType");
            String signature = dataMap.remove("signature");

           /* InputStream propertiesInput = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
            Properties properties = new Properties();
            properties.load(propertiesInput);
            String md5Key = (String) properties.get("md5Key");*/
            String md5Key = PropertiesUtils.getCurrProperty("nowpay.h5.md5Key");
            boolean isValidSignature = MD5Facade.validateFormDataParamMD5(dataMap, md5Key, signature);
            logger.info("验签结果：----------------------------->" + isValidSignature);
            if (isValidSignature) {

                String out_trade_no = dataMap.get("mhtOrderNo");
                String nowPayOrderNo = dataMap.get("nowPayOrderNo");
                Order o = orderService.getOrderById(out_trade_no);
                AppUser appUser = appuserService.getUserByID(o.getUSER_ID());
                if (o.getSTATUS().equals("1")) {
                    return "success=Y";
                }
                try {
                    //周卡
                    String wc = "wc";
                    //月卡
                    String mc = "mc";
                    //首充用户
                    String fc = "fc";
                    //正常充值
                    String nm = "nm";

                    String rechare = o.getREGGOLD();
                    int gold = Integer.valueOf(rechare);
                    int nb = 0;

                    if (o.getPAYOUT_TYPE().equals(wc)) {
                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+" + rechare);
                        payment.setUSERID(o.getUSER_ID());
                        payment.setDOLLID(null);
                        payment.setCOST_TYPE(Const.PlayMentCostType.cost_type19.getValue());
                        payment.setREMARK(Const.PlayMentCostType.cost_type19.getName());
                        paymentService.reg(payment);

                        Integer wd = appUser.getWEEKS_CARD();
                        String sdt = appUser.getWEEKS_CARD_TAG();
                        if (sdt.equals("1")) {
                            appUser.setWEEKS_CARD(wd + 7);
                            nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                        } else {
                            //更新收支表
                            Payment payment_1 = new Payment();
                            payment_1.setGOLD("+" + 20);
                            payment_1.setUSERID(o.getUSER_ID());
                            payment_1.setDOLLID(null);
                            payment_1.setCOST_TYPE(Const.PlayMentCostType.cost_type22.getValue());
                            payment_1.setREMARK(Const.PlayMentCostType.cost_type22.getName());
                            paymentService.reg(payment_1);
                            appUser.setWEEKS_CARD(wd + 6);
                            nb = Integer.valueOf(appUser.getBALANCE()) + gold + 20;
                        }
                        appUser.setWEEKS_CARD_TAG("1");
                        appUser.setBALANCE(String.valueOf(nb));
                        appuserService.updateAppUserBalanceById(appUser);

                    }
                    if (o.getPAYOUT_TYPE().equals(mc)) {
                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+" + rechare);
                        payment.setUSERID(o.getUSER_ID());
                        payment.setDOLLID(null);
                        payment.setCOST_TYPE(Const.PlayMentCostType.cost_type21.getValue());
                        payment.setREMARK(Const.PlayMentCostType.cost_type21.getName());
                        paymentService.reg(payment);

                        Integer wd = appUser.getMONTH_CARD();
                        String sdt = appUser.getMONTH_CARD_TAG();
                        if (sdt.equals("1")) {
                            appUser.setMONTH_CARD(wd + 30);
                            nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                        } else {
                            //更新收支表
                            Payment payment_2 = new Payment();
                            payment_2.setGOLD("+" + 33);
                            payment_2.setUSERID(o.getUSER_ID());
                            payment_2.setDOLLID(null);
                            payment_2.setCOST_TYPE(Const.PlayMentCostType.cost_type23.getValue());
                            payment_2.setREMARK(Const.PlayMentCostType.cost_type23.getName());
                            paymentService.reg(payment_2);
                            appUser.setMONTH_CARD(wd + 29);
                            nb = Integer.valueOf(appUser.getBALANCE()) + gold + 33;
                        }
                        appUser.setMONTH_CARD_TAG("1");
                        appUser.setBALANCE(String.valueOf(nb));
                        appuserService.updateAppUserBalanceById(appUser);


                    }
                    if (o.getPAYOUT_TYPE().equals(fc)) {
                        appUser.setFIRST_CHARGE("1");
                        nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                        appUser.setBALANCE(String.valueOf(nb));
                        appuserService.updateAppUserBalanceById(appUser);

                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+" + rechare);
                        payment.setUSERID(o.getUSER_ID());
                        payment.setDOLLID(null);
                        payment.setCOST_TYPE(Const.PlayMentCostType.cost_type24.getValue());
                        payment.setREMARK(Const.PlayMentCostType.cost_type24.getName());
                        paymentService.reg(payment);

                    }
                    if (o.getPAYOUT_TYPE().equals(nm)) {
                        nb = Integer.valueOf(appUser.getBALANCE()) + gold;
                        appUser.setBALANCE(String.valueOf(nb));
                        appuserService.updateAppUserBalanceById(appUser);

                        //更新收支表
                        Payment payment = new Payment();
                        payment.setGOLD("+" + rechare);
                        payment.setUSERID(o.getUSER_ID());
                        payment.setDOLLID(null);
                        payment.setCOST_TYPE(Const.PlayMentCostType.cost_type05.getValue());
                        payment.setREMARK(Const.PlayMentCostType.cost_type05.getName());
                        paymentService.reg(payment);

                    }

                    //首先查询积分列表是否有该用户信息

                    String userId = appUser.getUSER_ID();
                    UserPoints userPoints = userpointsService.getUserPointsFinish(userId);
                    PointsMall pointsMall = pointsmallService.getInfoById(Const.pointsMallType.points_type06.getValue());

                    String tag = userPoints.getFirstPay();
                    if (tag.equals("0")) {
                        int a = userPoints.getTodayPoints();
                        Integer now_points = a + pointsMall.getPointsValue();
                        userPoints.setTodayPoints(now_points);
                        userPoints.setFirstPay("1");
                        userpointsService.updateUserPoints(userPoints);

                        appUser = appuserService.getUserByID(userId);
                        appUser.setPOINTS(appUser.getPOINTS() + pointsMall.getPointsValue());

                        //增加积分记录
                        PointsDetail pointsDetail = new PointsDetail();
                        pointsDetail.setUserId(userId);
                        pointsDetail.setChannel(Const.pointsMallType.points_type06.getName());
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
                            String n_rtag =  userpointsService.doGoldReward(r_tag,goldValue,sum,ob,list,now_points,nb_2,appUser);
                            userPoints.setPointsReward_Tag(n_rtag);
                            userpointsService.updateUserPoints(userPoints);
                        }
                    }

                    //当前订单的用户昵称
                    o.setUserNickName(appUser.getNICKNAME());
                    o.setREGGOLD(String.valueOf(gold));
                    o.setORDER_NO(nowPayOrderNo);//现在支付交易凭证号
                    o.setSTATUS("1");
                    orderService.doRegCallbackUpdateOrder(o);

                    //推广者进行结算
                    int reg = Integer.valueOf(o.getREGAMOUNT())/100;
                    String pro_user_id = appUser.getPRO_USER_ID();
                    AppUser appUser1  =   appuserService.getUserByID(pro_user_id);
                    if (appUser1 != null){
                        int ob_pro =  appUser1.getPRO_BALANCE();
                        int nb_pro = ob_pro + reg;
                        appUser1.setPRO_BALANCE(nb_pro);
                        appuserService.updateAppUserBalanceById(appUser1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "success=Y";
            } else {
                return "success=N";
            }
        } catch (Exception e) {
            logger.info("程序异常" + e);
            e.printStackTrace();
            return "error";
        }

    }


}
