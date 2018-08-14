package com.fh.controller.wwjapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping(value = "/app/pay")
public class AppPayController extends BaseController {

    public static AlipayTradeAppPayResponse response;
    private static final String nurl = "http://111.231.139.61:18081/pooh-web/html/recharge/Recharge.html?userId=";
    private static final String furl = "http://111.231.139.61:18081/pooh-web/html/recharge/fistRecharge.html?userId=";

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


    @Resource(name="userpointsService")
    private UserPointsManager userpointsService;

    @Resource(name="pointsmallService")
    private PointsMallManager pointsmallService;

    @Resource(name="pointsdetailService")
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
     * 订单新接口，以编号获取相应的金币数
     *
     * @param userId
     * @param accessToken
     * @param pid
     * @param ctype
     * @param channel
     * @return
     */

    @RequestMapping(value = "/getTradeOrder_new", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getTradeOrder_new(
            @RequestParam("userId") String userId,
            @RequestParam(value = "accessToken", required = false) String accessToken,
            @RequestParam("pid") String pid,
            @RequestParam(value = "ctype", required = false) String ctype,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "payType", required = false) String payType
    ) {
        try {

            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return null;
            }
            String datetime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            Paycard paycard = paycardService.getPayCardById(pid);
            if (paycard == null) {
                return null;
            }
            String glodNum = paycard.getGOLD();//金币数量
            String amount = paycard.getAMOUNT();//金额
            boolean a = RedisUtil.getRu().exists("tradeOrder");
            String newOrder = ""; //订单号
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
            order.setUSER_ID(userId);
            order.setREC_ID(newOrder);
            order.setREGAMOUNT(NumberUtils.RMBYuanToCent(amount)); //元转分
            order.setORDER_ID(newOrder);
            order.setREGGOLD(glodNum); //充值的金币数量
            order.setCHANNEL(channel);
            order.setCTYPE(ctype);
            order.setPAY_TYPE(payType);
            order.setPRO_USER_ID(appUser.getPRO_USER_ID());
            orderService.regmount(order);
            Map<String, Object> map = new HashMap<>();
            map.put("Order", getOrderInfo(order.getORDER_ID()));
            return RespStatus.successs().element("data", map);

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }


    }

    /**
     * 支付回调接口
     *
     * @param chid         201609081254001(由CP自己分配相应渠道号；SDK只负责相应的渠道统计；并不做逻辑判断)
     * @param order_no     201609081254001(平台生成的订单号)
     * @param subject      test(订单主题，长度不要超过100位)
     * @param cid          ff8080814f81f904014f820178390004
     * @param amount       消费-总计金额
     * @param money        消费-支付金额
     * @param balance      消费-账户金额
     * @param vmoney       消费-活动券金额
     * @param time         1442475160430(时间戳)
     * @param user_id      2c90803b560b3c7a01560b3c7aa60000
     * @param out_trade_no 201609081254001(外部订单号, CP游戏的订单号)，长度不要超过32位；
     * @param trade_status 支付成功“SUCCESS”，支付失败“FAILURE”
     * @param payment_id   支付渠道,目前支持如下，后续扩展：
     *                     [Android]
     *                     alipay:支付宝,
     *                     bank:银联支付,
     *                     gf_wechat:微信,
     *                     huapay:易联支付
     *                     wallet:余额支付
     *                     [IOS]
     *                     applepay;苹果支付
     *                     wallet:余额支付
     * @param extra        透传参数，申请支付、消费时传入的参数，原样返回, 长度不要超过100位；
     * @param sign_type    固定值”MD5”
     * @param sign         参数签名，见签名方式
     * @return
     */

    @RequestMapping(value = "/orderCallBack", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String orderCallBack(
            @RequestParam("chid") String chid,
            @RequestParam("order_no") String order_no,
            @RequestParam("subject") String subject,
            @RequestParam("cid") String cid,
            @RequestParam("amount") int amount,
            @RequestParam("money") int money,
            @RequestParam("balance") int balance,
            @RequestParam("vmoney") int vmoney,
            @RequestParam("time") String time,
            @RequestParam("user_id") String user_id,
            @RequestParam("out_trade_no") String out_trade_no,
            @RequestParam("trade_status") String trade_status,
            @RequestParam("payment_id") String payment_id,
            @RequestParam("extra") String extra,
            @RequestParam("sign_type") String sign_type,
            @RequestParam("sign") String sign
    ) {
        try {
            Order o = orderService.getOrderById(out_trade_no);
            String ckey = PropertiesUtils.getCurrProperty("api.app.sdk.ckey");
            if (o == null) {
                return "there is no order";
            }
            if (trade_status.equals("FAILURE")) {
                o.setSTATUS("-1");
                orderService.update(o);
                return "SUCCESS";
            }
            o.setORDER_NO(order_no);
            String decodeStr = URLDecoder.decode(extra, "utf-8");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("chid", chid);
            map.put("order_no", order_no);
            map.put("subject", subject);
            map.put("cid", cid);
            map.put("amount", amount);
            map.put("money", money);
            map.put("balance", balance);
            map.put("vmoney", vmoney);
            map.put("time", time);
            map.put("user_id", user_id);
            map.put("out_trade_no", out_trade_no);
            map.put("trade_status", trade_status);
            map.put("payment_id", payment_id);
            map.put("extra", decodeStr);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                if (entry.getValue() == null || entry.getValue().toString().length() == 0) {
                    map.put(key, "null");
                }
            }

            String tb_amount = o.getREGAMOUNT();
            if (!tb_amount.equals(String.valueOf(amount))) {
                return "充值金额不匹配";
            }
            Map<String, Object> sortedParams = new TreeMap<String, Object>(map);
            Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
            // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
            StringBuilder basestring = new StringBuilder();
            for (Map.Entry<String, Object> param : entrys) {
                basestring.append(param.getKey()).append('=').append(param.getValue()).append('&');
            }
            basestring.delete(basestring.length() - 1, basestring.length()).append(ckey);
            String ss = TokenVerify.md5(basestring.toString());
            if (!ss.equals(sign)) {
                return "SIGN IS ERROR";
            }
            if (trade_status.equals("SUCCESS")) {
                if (o.getSTATUS().equals("1")) {
                    return "SUCCESS";
                }
                Paycard paycard = paycardService.getGold(String.valueOf(amount / 100));
                if (paycard == null) {
                    AppUser appUser = appuserService.getUserByID(o.getUSER_ID());
                    int reggold = Integer.valueOf(o.getREGAMOUNT()) / 10;
                    int a = Integer.valueOf(appUser.getBALANCE()) + reggold;
                    appUser.setBALANCE(String.valueOf(a));
                    appuserService.updateAppUserBalanceById(appUser);
                    Payment payment = new Payment();
                    payment.setGOLD(String.valueOf(reggold));
                    payment.setUSERID(o.getUSER_ID());
                    payment.setDOLLID(null);
                    payment.setCOST_TYPE("5");
                    payment.setREMARK("充值测试");
                    paymentService.reg(payment);
                    o.setORDER_NO(order_no);
                    o.setREGGOLD(String.valueOf(reggold));
                    o.setSTATUS("1");
                    orderService.update(o);
                    return "SUCCESS";
                }
                int gold = Integer.valueOf(paycard.getGOLD());
                String award = paycard.getAWARD();
                String rechare = paycard.getRECHARE();

                AppUser appUser = appuserService.getUserByID(o.getUSER_ID());
                int a = Integer.valueOf(appUser.getBALANCE()) + gold;
                appUser.setBALANCE(String.valueOf(a));
                appuserService.updateAppUserBalanceById(appUser);
                //更新收支表
                Payment payment = new Payment();
                payment.setGOLD("+" + rechare);
                payment.setUSERID(o.getUSER_ID());
                payment.setDOLLID(null);
                payment.setCOST_TYPE("5");
                payment.setREMARK("充值" + rechare);
                paymentService.reg(payment);
                //奖励记录
                Payment payment1 = new Payment();
                payment1.setGOLD("+" + award);
                payment1.setUSERID(o.getUSER_ID());
                payment1.setDOLLID(null);
                payment1.setCOST_TYPE("9");
                payment1.setREMARK("奖励" + award);
                paymentService.reg(payment1);
                //当前订单的用户昵称
                o.setUserNickName(appUser.getNICKNAME());
                o.setREGGOLD(String.valueOf(gold));
                o.setORDER_NO(order_no);
                o.setSTATUS("1");
                orderService.doRegCallbackUpdateOrder(o);
            } else {
                o.setSTATUS("-1");//支付失败
                orderService.update(o);
            }
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "SYSTEM ERROR";
        }
    }


    /**
     * ios h5 支付回调接口
     *
     * @param chid         201609081254001(由CP自己分配相应渠道号；SDK只负责相应的渠道统计；并不做逻辑判断)
     * @param order_no     201609081254001(平台生成的订单号)
     * @param subject      test(订单主题，长度不要超过100位)
     * @param cid          ff8080814f81f904014f820178390004
     * @param amount       消费-总计金额
     * @param money        消费-支付金额
     * @param balance      消费-账户金额
     * @param vmoney       消费-活动券金额
     * @param time         1442475160430(时间戳)
     * @param user_id      2c90803b560b3c7a01560b3c7aa60000
     * @param out_trade_no 201609081254001(外部订单号, CP游戏的订单号)，长度不要超过32位；
     * @param trade_status 支付成功“SUCCESS”，支付失败“FAILURE”
     * @param payment_id   支付渠道,目前支持如下，后续扩展：
     *                     [Android]
     *                     alipay:支付宝,
     *                     bank:银联支付,
     *                     gf_wechat:微信,
     *                     huapay:易联支付
     *                     wallet:余额支付
     *                     [IOS]
     *                     applepay;苹果支付
     *                     wallet:余额支付
     * @param extra        透传参数，申请支付、消费时传入的参数，原样返回, 长度不要超过100位；
     * @param sign_type    固定值”MD5”
     * @param sign         参数签名，见签名方式
     * @return
     */

    @RequestMapping(value = "/i5OrderCallBack", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String i5OrderCallBack(
            @RequestParam("chid") String chid,
            @RequestParam("order_no") String order_no,
            @RequestParam("subject") String subject,
            @RequestParam("cid") String cid,
            @RequestParam("amount") int amount,
            @RequestParam("money") int money,
            @RequestParam("balance") int balance,
            @RequestParam("vmoney") int vmoney,
            @RequestParam("time") String time,
            @RequestParam("user_id") String user_id,
            @RequestParam("out_trade_no") String out_trade_no,
            @RequestParam("trade_status") String trade_status,
            @RequestParam("payment_id") String payment_id,
            @RequestParam("extra") String extra,
            @RequestParam("sign_type") String sign_type,
            @RequestParam("sign") String sign
    ) {
        try {
            Order o = orderService.getOrderById(out_trade_no);
            String ckey = PropertiesUtils.getCurrProperty("api.i5.sdk.ckey");
            if (o == null) {
                return "there is no order";
            }
            if (trade_status.equals("FAILURE")) {
                o.setSTATUS("-1");
                orderService.update(o);
                return "SUCCESS";
            }
            o.setORDER_NO(order_no);
            String decodeStr = URLDecoder.decode(extra, "utf-8");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("chid", chid);
            map.put("order_no", order_no);
            map.put("subject", subject);
            map.put("cid", cid);
            map.put("amount", amount);
            map.put("money", money);
            map.put("balance", balance);
            map.put("vmoney", vmoney);
            map.put("time", time);
            map.put("user_id", user_id);
            map.put("out_trade_no", out_trade_no);
            map.put("trade_status", trade_status);
            map.put("payment_id", payment_id);
            map.put("extra", decodeStr);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                if (entry.getValue() == null || entry.getValue().toString().length() == 0) {
                    map.put(key, "null");
                }
            }

            String tb_amount = o.getREGAMOUNT();
            if (!tb_amount.equals(String.valueOf(amount))) {
                return "充值金额不匹配";
            }
            Map<String, Object> sortedParams = new TreeMap<String, Object>(map);
            Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
            // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
            StringBuilder basestring = new StringBuilder();
            for (Map.Entry<String, Object> param : entrys) {
                basestring.append(param.getKey()).append('=').append(param.getValue()).append('&');
            }
            basestring.delete(basestring.length() - 1, basestring.length()).append(ckey);
            String ss = TokenVerify.md5(basestring.toString());
            if (!ss.equals(sign)) {
                return "SIGN IS ERROR";
            }
            if (trade_status.equals("SUCCESS")) {
                if (o.getSTATUS().equals("1")) {
                    return "SUCCESS";
                }
                Paycard paycard = paycardService.getGold(String.valueOf(amount / 100));
                if (paycard == null) {
                    AppUser appUser = appuserService.getUserByID(o.getUSER_ID());
                    int reggold = Integer.valueOf(o.getREGAMOUNT()) / 10;
                    int a = Integer.valueOf(appUser.getBALANCE()) + reggold;
                    appUser.setBALANCE(String.valueOf(a));
                    appuserService.updateAppUserBalanceById(appUser);
                    Payment payment = new Payment();
                    payment.setGOLD(String.valueOf(reggold));
                    payment.setUSERID(o.getUSER_ID());
                    payment.setDOLLID(null);
                    payment.setCOST_TYPE("5");
                    payment.setREMARK("充值测试");
                    paymentService.reg(payment);
                    o.setORDER_NO(order_no);
                    o.setREGGOLD(String.valueOf(reggold));
                    o.setSTATUS("1");
                    orderService.update(o);
                    return "SUCCESS";
                }
                int gold = Integer.valueOf(paycard.getGOLD());
                String award = paycard.getAWARD();
                String rechare = paycard.getRECHARE();

                AppUser appUser = appuserService.getUserByID(o.getUSER_ID());
                int a = Integer.valueOf(appUser.getBALANCE()) + gold;
                appUser.setBALANCE(String.valueOf(a));
                appuserService.updateAppUserBalanceById(appUser);
                //更新收支表
                Payment payment = new Payment();
                payment.setGOLD("+" + rechare);
                payment.setUSERID(o.getUSER_ID());
                payment.setDOLLID(null);
                payment.setCOST_TYPE("5");
                payment.setREMARK("充值" + rechare);
                paymentService.reg(payment);
                //奖励记录
                Payment payment1 = new Payment();
                payment1.setGOLD("+" + award);
                payment1.setUSERID(o.getUSER_ID());
                payment1.setDOLLID(null);
                payment1.setCOST_TYPE("9");
                payment1.setREMARK("奖励" + award);
                paymentService.reg(payment1);

                //当前订单的用户昵称
                o.setUserNickName(appUser.getNICKNAME());
                o.setREGGOLD(String.valueOf(gold));
                o.setORDER_NO(order_no);
                o.setSTATUS("1");
                orderService.doRegCallbackUpdateOrder(o);
            } else {
                o.setSTATUS("-1");//支付失败
                orderService.update(o);
            }
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "SYSTEM ERROR";
        }
    }


    /**
     * 微玩8游戏平台 支付回调接口
     * <p>
     * username	string	用户名
     * productname	string	商品名称
     * amount	double	金额
     * roleid	string	角色id
     * serverid	string	开服id
     * appid	int	游戏id
     * token	string	签名
     * remarks	string	CP方的扩展参数
     *
     * @return
     */

    @RequestMapping(value = "/w8OrderCallBack", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String w8OrderCallBack(HttpServletRequest request) {
        String orderid = request.getParameter("orderid");
        String username = request.getParameter("username");
        String productname = request.getParameter("productname");
        double amount = new Double(request.getParameter("amount"));
        String roleid = request.getParameter("roleid");
        String serverid = request.getParameter("serverid");
        String appid = request.getParameter("appid");
        String paytime = request.getParameter("paytime");
        String token = request.getParameter("token");
        String remarks = request.getParameter("remarks");
        try {

            String cid = PropertiesUtils.getCurrProperty("api.app.w8sdk.cid");
            //step1 签名验证

            if (token == null || "".equals(token)) {
                return "SIGN IS NULL";
            }
            String md5param = "orderid=" + orderid + "&username=" + username + "&productname=" + URLEncoder.encode(productname, "utf-8") +
                    "&amount=" + amount + "&roleid=" + roleid + "&serverid=" + serverid + "&appid=" + appid + "&paytime=" + paytime +
                    "&remarks=" + remarks + "&appkey=" + cid;
            logger.info("md5param-->" + md5param);
            String md5token = TokenVerify.md5(md5param);

            if (!token.toLowerCase().equals(md5token.toLowerCase())) {
                return "SIGN IS ERROR";
            }

            //step2 订单查询
            Order o = orderService.getOrderById(remarks);

            if (o == null) {
                return "order is null";
            }
            if (o.getSTATUS().equals("1")) {
                return "SUCCESS";
            }


            //充值的金币数量
            int gold = Integer.valueOf(o.getREGGOLD());
            String award = "";
            String rechare = "";
            switch (gold) {
                case 65:
                    rechare = "60";
                    award = "5";
                    break;
                case 335:
                    rechare = "300";
                    award = "35";
                    break;
                case 800:
                    rechare = "680";
                    award = "120";
                    break;
                case 1600:
                    rechare = "1280";
                    award = "320";
                    break;
                case 4375:
                    rechare = "3280";
                    award = "1095";
                    break;
                case 9260:
                    rechare = "6480";
                    award = "2780";
                    break;
                default:
            }

            //step4 更新账户金币余额
            AppUser appUser = appuserService.getUserByID(o.getUSER_ID());
            int a = Integer.valueOf(appUser.getBALANCE()) + gold;
            appUser.setBALANCE(String.valueOf(a));
            appuserService.updateAppUserBalanceById(appUser);

            //step5 更新收支表
            Payment payment = new Payment();
            payment.setGOLD("+" + rechare);
            payment.setUSERID(o.getUSER_ID());
            payment.setDOLLID(null);
            payment.setCOST_TYPE("5");
            payment.setREMARK("充值" + rechare);
            paymentService.reg(payment);

            //step6 更新奖励明细
            Payment payment1 = new Payment();
            payment1.setGOLD("+" + award);
            payment1.setUSERID(o.getUSER_ID());
            payment1.setDOLLID(null);
            payment1.setCOST_TYPE("9");
            payment1.setREMARK("奖励" + award);
            paymentService.reg(payment1);

            //step7 更新订单
            //当前订单的用户昵称
            o.setUserNickName(appUser.getNICKNAME());
            o.setORDER_NO(orderid);
            o.setREGGOLD(String.valueOf(gold));
            o.setSTATUS("1");
            orderService.doRegCallbackUpdateOrder(o);

            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "SYSTEM ERROR";
        }
    }

    /**
     * 支付宝下单
     *
     *    //周卡
     *   String wc = "wc";
     *   //月卡
     *  String mc = "mc";
     *  //首充用户
     *  String fc = "fc";
     *  //正常充值
     *  String nm = "nm";
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
        model.setSubject("第一抓娃娃支付");
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
                UserPoints userPoints =  userpointsService.getUserPointsFinish(userId);
                PointsMall pointsMall =  pointsmallService.getInfoById(Const.pointsMallType.points_type06.getValue());

                    String tag =  userPoints.getFirstPay();
                    if (tag.equals("0")){
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
                        Integer goldValue = 0;
                        Integer sum = 0;
                        Integer ob = Integer.valueOf(appUser.getBALANCE());
                        Integer nb_2 = 0;
                        List<PointsReward> list = pointsrewardService.getPointsReward();
                        String n_rtag = userpointsService.doGoldReward(r_tag, goldValue, sum, ob, list, now_points, nb_2, appUser);
                        userPoints.setPointsReward_Tag(n_rtag);
                        userpointsService.updateUserPoints(userPoints);

                    }

                //当前订单的用户昵称
                o.setUserNickName(appUser.getNICKNAME());
                o.setREGGOLD(String.valueOf(gold));
                o.setORDER_NO(params.get("trade_no"));//支付宝交易凭证号
                o.setSTATUS("1");
                orderService.doRegCallbackUpdateOrder(o);

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
            String tag =  appUser.getFIRST_CHARGE();
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
            String regGold = "" ;
            String payOutType = "";
            Paycard paycard =  paycardService.getPayCardById(pid);

            String amt = paycard.getAMOUNT();
            if (pid.equals("8")){
                regGold = paycard.getRECHARE();
                payOutType = wc;

            }
            if (pid.equals("9")){
                regGold = paycard.getRECHARE();
                payOutType = mc;

            }

            if (tag.equals("0")){
                regGold = paycard.getFIRSTAWARD_GOLD();
                payOutType = fc ;
            }else if(tag.equals("1") ) {
                regGold =paycard.getGOLD();
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
    public JSONObject getRecUrl(@RequestParam("userId") String userId){
        try{
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return null;
            }
            String tag =  appUser.getFIRST_CHARGE();
            if (tag.equals("1")){
                return RespStatus.successs().element("url",nurl+userId);

            }else {
                return RespStatus.successs().element("url",furl+userId);
            }

        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /********************************************************************现在支付******************************************************************************/

    /**
     * @param req
     * @param userId  用户ID
     * @param ant     金额（元）
     * @param ctype
     * @param channel
     * @param payType 默认R
     * @return
     */
    @RequestMapping(value = "/getTradeOrderxdpay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getTradeOrderxdpay(
            HttpServletRequest req,
            @RequestParam("userId") String userId,
            @RequestParam("amount") String ant,
            @RequestParam(value = "ctype", required = false) String ctype,
            @RequestParam(value = "channel", required = false) String channel,
            @RequestParam(value = "payType", required = false) String payType
    ) {
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return null;
            }
            String datetime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            if (ant == null || ant.equals("")) {
                return null;
            }
            boolean a = RedisUtil.getRu().exists("tradeOrder");
            String newOrder = ""; //订单号
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
            order.setUSER_ID(userId);
            order.setREC_ID(newOrder);
            order.setREGAMOUNT(NumberUtils.RMBYuanToCent(ant)); //元转分
            order.setORDER_ID(newOrder);
            order.setREGGOLD(String.valueOf(Integer.valueOf(ant) * 10)); //充值的金币数量
            order.setCHANNEL(channel);
            order.setCTYPE(ctype);
            order.setPAY_TYPE(payType);
            order.setPRO_USER_ID(appUser.getPRO_USER_ID());
            orderService.regmount(order);

            Map<String, Object> map = new HashMap<>();
            map.put("funcode", "WP001");//
            map.put("version", "1.0.0");//
            map.put("appId", PropertiesUtils.getCurrProperty("nowpay.appId"));//
            map.put("mhtOrderNo", newOrder);//
            map.put("mhtOrderName", "娃娃币");//
            map.put("mhtOrderType", "01");//
            map.put("mhtCurrencyType", "156");//
            map.put("mhtOrderAmt", Integer.valueOf(NumberUtils.RMBYuanToCent(ant)));//
            map.put("mhtOrderStartTime", DateUtil.getSdfTimes());//
            map.put("frontNotifyUrl", "http://sds");
            map.put("notifyUrl", "http://111.231.139.61:18081/pooh-web/app/pay/xdpayCallBack");//
            map.put("mhtCharset", "UTF-8");//
            map.put("deviceType", "0601");//
            map.put("payChannelType", "13");//
            map.put("consumerId", userId);//.......
            map.put("consumerName", appUser.getUSERNAME());//........
            map.put("mhtSignType", "MD5");//
            map.put("outputType", "2");//
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
            System.out.println("发送的参数:" + s + "------------");
            String Code = NowPayUtil.doPost(s);
            logger.info("Code---------------" + Code);
            Map<String, Object> map_1 = new HashMap<>();
            map_1.put("Order", getOrderInfo(order.getORDER_ID()));
            return RespStatus.successs().element("data", map_1).element("nowpayData", map);

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
                try {
                    Order o = orderService.getOrderById(dataMap.get("mhtOrderNo"));
                    if (o.getSTATUS().equals("1")) {
                        return "success=Y";
                    }
                    //通过金币卡ID来查询
                    String pid = o.getADD_INFO();
                    Paycard paycard = paycardService.getPayCardById(pid);
                    int gold = Integer.valueOf(paycard.getGOLD());
                    String award = paycard.getAWARD();
                    String rechare = paycard.getRECHARE();

                    AppUser appUser = appuserService.getUserByID(o.getUSER_ID());
                    int a = Integer.valueOf(appUser.getBALANCE()) + gold;
                    appUser.setBALANCE(String.valueOf(a));
                    appuserService.updateAppUserBalanceById(appUser);
                    //更新收支表
                    Payment payment = new Payment();
                    payment.setGOLD("+" + rechare);
                    payment.setUSERID(o.getUSER_ID());
                    payment.setDOLLID(null);
                    payment.setCOST_TYPE("5");
                    payment.setREMARK("充值" + rechare);
                    paymentService.reg(payment);
                    //奖励记录
                    Payment payment1 = new Payment();
                    payment1.setGOLD("+" + award);
                    payment1.setUSERID(o.getUSER_ID());
                    payment1.setDOLLID(null);
                    payment1.setCOST_TYPE("9");
                    payment1.setREMARK("奖励" + award);
                    paymentService.reg(payment1);

                    //当前订单的用户昵称
                    o.setUserNickName(appUser.getNICKNAME());
                    o.setREGGOLD(String.valueOf(gold));
                    o.setORDER_NO(dataMap.get("nowPayOrderNo"));//现在支付流水号
                    o.setSTATUS("1");
                    orderService.doRegCallbackUpdateOrder(o);
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

    public static void main(String[] args) {

        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID,
                AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl("https://xxx.xxx.xxx/open-pay/open-pay/aggregate/pay/QRPay");
        // 在公共参数中设置回跳和通知地址
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
        String out_trade_no = "1234560000000000";
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\"" + out_trade_no + "\"," +
                " \"total_amount\":\"" + 12 + "\"," +
                " \"subject\":\"娃娃币\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"" +
                " }");//填充业务参数
        // 调用SDK生成表单

        try {
            String form = alipayClient.pageExecute(alipayRequest).getBody();
            System.out.println(form);
    }catch (AlipayApiException e){
        e.printStackTrace();}
    }


}
