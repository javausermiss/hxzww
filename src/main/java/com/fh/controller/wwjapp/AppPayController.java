package com.fh.controller.wwjapp;

import java.net.URLDecoder;
import java.net.URLEncoder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.fh.alipay.AlipayConfig;
import com.fh.util.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.controller.base.BaseController;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.Order;
import com.fh.entity.system.Paycard;
import com.fh.entity.system.Payment;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.ordertest.OrderManager;
import com.fh.service.system.paycard.PaycardManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.playback.PlayBackManage;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.pond.PondManager;
import com.fh.util.PropertiesUtils;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RedisUtil;
import com.fh.util.wwjUtil.RespStatus;
import com.fh.util.wwjUtil.TokenVerify;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/app/pay")
public class AppPayController extends BaseController {

    public static AlipayTradeAppPayResponse response;

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
            @RequestParam(value = "channel", required = false) String channel

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
            int amount = Integer.valueOf(paycard.getAMOUNT());//金额
            boolean a = RedisUtil.getRu().exists("tradeOrder");
            if (a) {
                String tradeOrder = RedisUtil.getRu().get("tradeOrder");
                String x = tradeOrder.substring(0, 8);//取前八位进行判断
                if (datetime.substring(0, 8).equals(x)) {
                    String six = tradeOrder.substring(tradeOrder.length() - 6, tradeOrder.length());
                    String newsix = String.format("%06d", (Integer.valueOf(six) + 1));
                    String newOrder = datetime + newsix;//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                    Order order = new Order();
                    order.setUSER_ID(userId);
                    order.setREC_ID(MyUUID.getUUID32());
                    order.setREGAMOUNT(String.valueOf(amount * 100));//充值金额
                    order.setORDER_ID(newOrder);
                    order.setREGGOLD(glodNum);//充值的金币数量
                    order.setCHANNEL(channel);
                    order.setCTYPE(ctype);
                    orderService.regmount(order);
                    Map<String, Object> map = new HashMap<>();
                    map.put("Order", getOrderInfo(order.getORDER_ID()));
                    return RespStatus.successs().element("data", map);
                } else {
                    String newOrder = datetime + "000001";//新的订单编号
                    RedisUtil.getRu().set("tradeOrder", newOrder);
                    Order order = new Order();
                    order.setUSER_ID(userId);
                    order.setREC_ID(MyUUID.getUUID32());
                    order.setREGAMOUNT(String.valueOf(amount * 100));
                    order.setORDER_ID(newOrder);
                    order.setREGGOLD(glodNum);//充值的金币数量
                    order.setCHANNEL(channel);
                    order.setCTYPE(ctype);
                    orderService.regmount(order);
                    Map<String, Object> map = new HashMap<>();
                    map.put("Order", getOrderInfo(order.getORDER_ID()));
                    return RespStatus.successs().element("data", map);
                }
            } else {
                String newOrder = datetime + "000001";//新的订单编号
                RedisUtil.getRu().set("tradeOrder", newOrder);
                Order order = new Order();
                order.setUSER_ID(userId);
                order.setREC_ID(MyUUID.getUUID32());
                order.setREGAMOUNT(String.valueOf(amount * 100));
                order.setORDER_ID(newOrder);
                order.setREGGOLD(glodNum); //充值的金币数量
                order.setCHANNEL(channel);
                order.setCTYPE(ctype);
                orderService.regmount(order);
                Map<String, Object> map = new HashMap<>();
                map.put("Order", getOrderInfo(order.getORDER_ID()));
                return RespStatus.successs().element("data", map);
            }

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
                o.setREGGOLD(String.valueOf(gold));
                o.setORDER_NO(order_no);
                o.setSTATUS("1");
                orderService.update(o);
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
                o.setREGGOLD(String.valueOf(gold));
                o.setORDER_NO(order_no);
                o.setSTATUS("1");
                orderService.update(o);
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
            o.setORDER_NO(orderid);
            o.setREGGOLD(String.valueOf(gold));
            o.setSTATUS("1");
            orderService.update(o);

            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "SYSTEM ERROR";
        }
    }

    /**
     * 支付宝下单
     *
     * @param userId
     * @param pid
     * @param ctype
     * @param channel
     * @return
     */
    @RequestMapping(value = "/getTradeOrderAlipay", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getTradeOrder(@RequestParam("userId") String userId,
                                    @RequestParam("pid") String pid,
                                    @RequestParam(value = "ctype", required = false) String ctype,
                                    @RequestParam(value = "channel", required = false) String channel
    ) {


        String newOrder = "";
        String amount = "";

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
            amount = NumberUtils.RMBYuanToCent(paycard.getAMOUNT());
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
            order.setREC_ID(MyUUID.getUUID32());
            order.setREGAMOUNT(amount);//充值金额
            order.setORDER_ID(newOrder);
            order.setREGGOLD(glodNum);//充值的金币数量
            order.setCHANNEL(channel);
            order.setCTYPE(ctype);
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
        model.setTotalAmount(NumberUtils.RMBCentToYuan(amount));
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
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
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
                    String am =  NumberUtils.RMBYuanToCent(String.valueOf(amount));
                    String tb_amount = o.getREGAMOUNT();

                    logger.info("实际收到的金额为:" + am+"分，分为单位");
                    logger.info("订单支付的金额为:" + tb_amount+"分，分为单位");
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
                if (o.getSTATUS().equals("1")) {
                    return "success";
                }
                Paycard paycard = paycardService.getGold(String.valueOf(amount));
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
                o.setREGGOLD(String.valueOf(gold));
                o.setORDER_NO(params.get("trade_no"));//支付宝交易凭证号
                o.setSTATUS("1");
                orderService.update(o);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "success";

    }


}
