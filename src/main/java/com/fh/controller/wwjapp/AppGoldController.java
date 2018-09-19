package com.fh.controller.wwjapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.entity.system.AppUser;
import com.fh.entity.system.GoldDetail;
import com.fh.entity.system.GoldSendGoods;
import com.fh.entity.system.PointsDetail;
import com.fh.entity.system.PointsSendGoods;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.golddetail.GoldDetailManager;
import com.fh.service.system.goldgoods.GoldGoodsManager;
import com.fh.service.system.goldsendgoods.GoldSendGoodsManager;
import com.fh.service.system.pointsdetail.PointsDetailManager;
import com.fh.service.system.pointsgoods.PointsGoodsManager;
import com.fh.service.system.pointssendgoods.PointsSendGoodsManager;
import com.fh.util.Const;
import com.fh.util.PageData;
import com.fh.util.PropertiesUtils;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RespStatus;

import net.sf.json.JSONObject;

/**
 * 金币商城
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/app/goldMall")
public class AppGoldController {
	
	@Resource(name = "golddetailService")
    private GoldDetailManager golddetailService;
	
	@Resource(name = "goldsendgoodsService")
    private GoldSendGoodsManager goldsendservice;
	
	
	@Resource(name = "appuserService")
    private AppuserManager appuserService;

	@Resource(name = "goldgoodsService")
    private GoldGoodsManager goldgoodsService;
	
	
	/**
     * 金币列表接口
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getGoldDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getGoldDetail(@RequestParam("userId") String userId) {
        try {
            List<GoldDetail> list = golddetailService.getGoldDetail(userId);
            Map<String, Object> map = new HashMap<>();
            map.put("goldDetail", list);
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
     * 获取商城首页信息
     *
     * @return
     */
    @RequestMapping(value = "/getGoldMallDetail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getGoldMallDetail() {
        try {
            List<PageData> pageData = goldgoodsService.listAll(new PageData());
            Map<String, Object> map = new HashMap<>();
            map.put("goldGoodsList", pageData);
            return RespStatus.successs().element("data", map);

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }
    
    
    /**
     * 查询金币兑换礼品列表
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getGoldGoodsDetail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getGoldGoodsDetail(@RequestParam("userId") String userId) {
        try {
            AppUser appUser = appuserService.getUserByID(userId);
            if (appUser == null) {
                return RespStatus.fail("用户不存在");
            }
            List<GoldSendGoods> list = goldsendservice.getGoldGoodsForUser(userId);
            Map<String, Object> map = new HashMap<>();
            map.put("GoldSendGoodsList", list);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }
    
    /**
     * 金币兑换礼品接口
     *
     * @param userId           用户ID
     * @param gold           消费金币
     * @param goodsName        商品名称
     * @param consignee        收货人名字
     * @param consigneeAddress 地址
     * @param consigneePhone   电话
     * @return
     */
    @RequestMapping(value = "/exchangeGoldGoods", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject exchangeGoldGoods(@RequestParam("userId") String userId,
                                          @RequestParam("gold") Integer gold,
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

            String user_gold = appUser.getBALANCE();
            if (Integer.parseInt(user_gold) < gold) {
                return RespStatus.successs().element("data", "金币不够");
            }
            Integer newp = Integer.parseInt(user_gold) - gold;
            appUser.setBALANCE(newp+"");
            appuserService.updateAppUserBalanceById(appUser);

            GoldSendGoods goldSendGoods = new GoldSendGoods();
            goldSendGoods.setUserId(userId);
            goldSendGoods.setConsignee(consignee);
            goldSendGoods.setConsigneeAddress(consigneeAddress);
            goldSendGoods.setConsigneePhone(consigneePhone);
            goldSendGoods.setGoodsName(goodsName);
            goldSendGoods.setGoodsNum(goodsNum);
            goldsendservice.regGoldSendGoods(goldSendGoods);

            //增加积分消费记录
            GoldDetail goldDetail = new GoldDetail();
            goldDetail.setUserId(userId);
            /*goldDetail.setChannel(Const.pointsMallType.points_type07.getName());*/
            goldDetail.setType("-");
            goldDetail.setGolddetail_id(MyUUID.getUUID32());
            goldDetail.setGoldsvalue(gold);
            golddetailService.regGoldDetail(goldDetail);
            return RespStatus.successs().element("data", "兑换成功");

        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }
    
}
