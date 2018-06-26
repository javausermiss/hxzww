package com.fh.controller.wwjapp;

import com.fh.entity.system.CoinPusher;
import com.fh.service.system.coinpusher.CoinPusherManager;
import com.fh.util.wwjUtil.RespStatus;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推币机控制类
 * @author wjy
 * @date 2018/06/15
 */
@Controller
@RequestMapping("/app/coinPusher")
public class AppCoinPusherController {

    @Resource(name = "coinpusherService")
    private CoinPusherManager coinpusherService;

    /**
     * 查询用户的投币记录，出币记录
     * @return
     */
    @RequestMapping(value = "/getCoinPusherRecondList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getCoinPusherRecondList(@RequestParam("userId") String userId){
        try {
            List<CoinPusher> list = coinpusherService.getCoinPusherRecondList(userId);
            if (list==null){
                return null;
            }
            Map<String,Object> map = new HashMap<>();
            map.put("coinPusher",list);
            return RespStatus.successs().element("data",map);
        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }


    }




}
