package com.fh.controller.wwjapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.entity.system.AppUser;
import com.fh.entity.system.Conversion;
import com.fh.entity.system.PlayBack;
import com.fh.entity.system.PlayDetail;
import com.fh.entity.system.SendGoods;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.betgame.BetGameManager;
import com.fh.service.system.conversion.ConversionManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.doll.DollToyManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.playback.PlayBackManage;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.sendgoods.SendGoodsManager;
import com.fh.util.DateUtil;
import com.fh.util.wwjUtil.RespStatus;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RequestMapping("/app/goods")
@Controller
public class AppSendGoodController {
    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "playBackService")
    private PlayBackManage playBackService;
    @Resource(name = "sendGoodsService")
    private SendGoodsManager sendGoodsService;
    @Resource(name = "conversionService")
    private ConversionManager conversionService;
    @Resource(name = "betGameService")
    private BetGameManager betGameService;
    @Resource(name = "playDetailService")
    private PlayDetailManage playDetailService;
    @Resource(name = "dollService")
    private DollManager dollService;
    @Resource(name = "paymentService")
    private PaymentManager paymentService;
    @Resource(name = "dolltoyService")
    private DollToyManager dolltoyService;


    public JSONObject getPlayBackInfo(String playId) {
        try {
            PlayBack playBack = playBackService.getPlayBackSGById(Integer.parseInt(playId));
            return JSONObject.fromObject(playBack);
        } catch (Exception e) {
            return null;
        }

    }

    public JSONObject getPlayDetailInfo(Integer playId) {
        try {
            PlayDetail playBack = playDetailService.getPlayDetailByID(playId);
            return JSONObject.fromObject(playBack);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 个人信息
     *
     * @param id
     * @return
     */

    public JSONObject getAppUserInfo(String id) {
        try {
            AppUser appUser = appuserService.getUserByID(id);
            return JSONObject.fromObject(appUser);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 更改收货人信息接口
     *
     * @return
     */
    @RequestMapping(value = "/cnsignee", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject cnsignee(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("userId") String id

    ) {
        try {
            AppUser appUser = appuserService.getUserByID(id);
            appUser.setCNEE_NAME(name);
            appUser.setCNEE_ADDRESS(address);
            appUser.setCNEE_PHONE(phone);
            int n = appuserService.updateAppUserCnee(appUser);
            if (n == 0) {
                return RespStatus.fail("更新失败");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("appUser", getAppUserInfo(id));
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }


    }

    /**
     * 发货接口
     *
     * @param playId
     * @param number
     * @param consignee
     * @param remark
     * @param userId
     * @return
     */
    @RequestMapping(value = "/sendGoods", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject sendGoods1(
            @RequestParam("id") String playId,//抓取编号(用户抓取记录ID，逗号拼接)例：5411,2223,5623
            @RequestParam("number") String number,//娃娃数量
            @RequestParam("consignee") String consignee,//例：名字,地址,手机号码(逗号拼接)
            @RequestParam("remark") String remark,//用户留言
            @RequestParam("userId") String userId, //用户ID
            @RequestParam(value = "mode") String mode,//模式 0：免邮  1：金币抵扣
            @RequestParam(value = "costNum",required = false) String costNum//省份编号
    ) {
        try {
            return sendGoodsService.doSendGoods(playId, number, consignee, remark, userId, mode,costNum);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /**
     * 兑换娃娃币
     *
     * @param id
     * @param dollId
     * @param number
     * @param userId
     * @return
     */
    @RequestMapping(value = "/conversionGoods", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject conversionGoods(
            @RequestParam("id") String id,
            @RequestParam("dollId") String dollId,
            @RequestParam("number") String number,
            @RequestParam("userId") String userId
    ) {

        try {
            return sendGoodsService.doConversionGold(id, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }

    /**
     * 获取兑换列表
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getConList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getConList(
            @RequestParam("userId") String userId
    ) {
        try {
            List<Conversion> conversionList = conversionService.getList(new Conversion(userId));
            Map<String, Object> map = new HashMap<>();
            map.put("conversionList", conversionList);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /**
     * 查询用户订单信息
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getLogistics", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getLogistics(@RequestParam("userId") String userId) {
        try {
            List<SendGoods> sendGoods = sendGoodsService.getLogisticsByUserId(userId);
            for (int i = 0; i < sendGoods.size(); i++) {
                SendGoods sendGoods1  = sendGoods.get(i);
                String id =  String.valueOf(sendGoods1.getID()) ;
                String sid =  DateUtil.getNumOrder(id,sendGoods1.getCREATE_TIME());
                sendGoods1.setSEND_NUM_ID(sid);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("logistics",sendGoods);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }


    public static void main(String[] a) {

        String catch_time = DateUtil.getTimeSSS();
        log.info("下抓时间----------------->"+catch_time);
        String reword = catch_time.substring(catch_time.length()-1,catch_time.length());
        log.info(reword);


    }


}
