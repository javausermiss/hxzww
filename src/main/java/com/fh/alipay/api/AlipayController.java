package com.fh.alipay.api;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.Order;
import com.fh.entity.system.Paycard;
import com.fh.entity.system.Payment;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.ordertest.OrderManager;
import com.fh.service.system.paycard.PaycardManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RedisUtil;
import com.fh.util.wwjUtil.RespStatus;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RequestMapping("/app/pay")
@Controller
public class AlipayController {

    public static AlipayTradeAppPayResponse response;

    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "paymentService")
    private PaymentManager paymentService;

    @Resource(name = "orderService")
    private OrderManager orderService;

    @Resource(name = "paycardService")
    private PaycardManager paycardService;

    /**
     * 下单
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
        Integer amount;
        String amount_total = "";
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
            amount = Integer.valueOf(paycard.getAMOUNT());//金额
            boolean a = RedisUtil.getRu().exists("tradeOrder");
            if (a) {
                String tradeOrder = RedisUtil.getRu().get("tradeOrder");
                String x = tradeOrder.substring(0, 8);//取前八位进行判断
                if (datetime.substring(0, 8).equals(x)) {
                    String six = tradeOrder.substring(tradeOrder.length() - 6, tradeOrder.length());
                    String newsix = String.format("%06d", (Integer.valueOf(six) + 1));
                    newOrder = datetime + newsix;//新的订单编号
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

                } else {
                    newOrder = datetime + "000001";//新的订单编号
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

                }
            } else {
                newOrder = datetime + "000001";//新的订单编号
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
        model.setBody("抓娃娃金币测试");
        model.setSubject("App支付测试");
        model.setOutTradeNo(newOrder);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(String.valueOf(amount_total));
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(AlipayConfig.notify_url);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            e.printStackTrace();

        }
        return RespStatus.successs().element("data", JSONObject.fromObject(response.getBody()));

    }

    @RequestMapping(value = "/AlipayCallBack", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String AlipayCallBack(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        String out_trade_no = "";
        double amount = 0.00;
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
            if (flag) {
                if ("TRADE_SUCCESS".equals(params.get("trade_status"))) {

                    try {

                        //商户订单号
                        out_trade_no = params.get("out_trade_no");
                        Order o = orderService.getOrderById(out_trade_no);
                        if (o == null) {
                            return "failure";
                        }

                        //实际收到的金额
                        amount = Double.parseDouble(params.get("receipt_amount"));
                        double tb_amount = Double.parseDouble(o.getREGAMOUNT());
                        if (amount - tb_amount != 0) {
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
            } else {
                return "failure";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return "failure";
        }

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
        return "success";

    }

}
