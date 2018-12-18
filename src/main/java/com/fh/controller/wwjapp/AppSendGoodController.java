package com.fh.controller.wwjapp;

import com.fh.entity.system.*;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
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
            @RequestParam(value = "id",required = false,defaultValue = "null") String playId,//抓取编号(用户抓取记录ID，逗号拼接)例：5411,2223,5623
            @RequestParam(value = "number",required = false,defaultValue = "null") String number,//娃娃数量
            @RequestParam(value = "consignee") String consignee,//例：名字,地址,手机号码(逗号拼接)
            @RequestParam(value = "remark",required = false,defaultValue = "null") String remark,//用户留言
            @RequestParam(value = "userId") String userId, //用户ID
            @RequestParam(value = "mode") String mode,//模式 0：免邮  1：金币抵扣
            @RequestParam(value = "costNum",required = false,defaultValue = "0") String costNum,//省份编号
            @RequestParam(value = "level" ,required = false,defaultValue = "0") String level//大礼包发货表1 16代表16级礼包 18代表18级礼包

    ) {
        try {
            return sendGoodsService.doSendGoods(playId, number, consignee, remark, userId, mode,costNum,level);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /**
     *
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
@RequestMapping(value = "/success",method = RequestMethod.GET)
    public ModelAndView test(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("test1");
        return modelAndView;
    }


    public static void main(String[] a) {






    }


}
