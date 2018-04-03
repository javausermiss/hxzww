package com.fh.controller.wwjapp;

import com.fh.entity.system.AppUser;
import com.fh.entity.system.PlayBack;
import com.fh.entity.system.PlayDetail;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.betgame.BetGameManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.playback.PlayBackManage;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.pond.PondManager;
import com.fh.util.PageData;
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
 * 视频回放记录存储&游戏记录&竞猜结算
 */
@Controller
@RequestMapping(value = "/api/play")
public class ApiPlayBackController {

    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "dollService")
    private DollManager dollService;
    @Resource(name = "playBackService")
    private PlayBackManage playBackService;
    @Resource(name = "pondService")
    private PondManager pondService;
    @Resource(name = "betGameService")
    private BetGameManager betGameService;
    @Resource(name = "playDetailService")
    private PlayDetailManage playDetailService;

    /**
     * 增加视频记录
     *
     * @param time
     * @param userId
     * @param guessId
     * @param dollId
     * @return
     */
    @RequestMapping(value = "/regPlayBack", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject regPlayBack(
            @RequestParam("time") String time,//时间
            @RequestParam("userId") String userId,//用户ID
            @RequestParam("guessId") String guessId,//场次ID
            @RequestParam("dollId") String dollId,//房间ID
            @RequestParam("state") String state //视频状态
    ) {
        try {
            PlayDetail p = new PlayDetail();
            p.setDOLLID(dollId);
            p.setUSERID(userId);
            p.setGUESS_ID(guessId);
            PlayDetail playDetail = playDetailService.getPlayDetailForCamera(p);
            playDetail.setCAMERA_DATE(time);
            playDetailService.updatePlayDetailForCamera(playDetail);
            return RespStatus.successs();
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }


    /**
     * 获取抓中的娃娃记录
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getPlayRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPlayRecord(
            @RequestParam("userId") String userId
    ) {
        try {
            AppUser appUser = appuserService.getAppUserRanklist(userId);
            if (appUser==null){
                return null;
            }
            List<PlayDetail> p = playDetailService.getDollCount(userId);
            List<PlayDetail> n = playDetailService.getPlaylist(userId);
            Map<String, Object> map = new HashMap<>();
            map.put("playback", n);
            map.put("dollCount", p);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }


    /**
     * 获取抓娃娃记录
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "/getAllPlayRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getAllPlayRecord(
            @RequestParam("nickName") String username
    ) {
        try {
            List<PlayBack> n = playBackService.getAllPlayRecord(username);
            Map<String, Object> map = new HashMap<>();
            map.put("playback", n);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }

    /**
     * 查询最新的10人抓取成功记录
     *
     * @return
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserList() {
        try {
            List<PlayDetail> list = playDetailService.getPlayRecordPM();
            Map<String, Object> map = new HashMap<>();
            map.put("playback", list);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }

    /**
     * 查询该房间最近10条游戏记录
     * @param roomId
     * @return
     */

    @RequestMapping(value = "/getGamelist",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getGamelist(@RequestParam("roomId") String roomId){
        try {
             List<PageData> list = playDetailService.getGameList(roomId);
             Map<String,Object> map =  new HashMap<>();
             map.put("gameList",list);
             return  RespStatus.successs().element("data",map);
        }catch (Exception e){
            e.printStackTrace();
            return  RespStatus.fail();
        }
    }




    public static void main(String[] strings){
        String sendGoods = "wqe,sda, ";
        String[] s = sendGoods.split("\\,");
        String name = s[0];
        String phone = s[1];
        String address = s[2];
        System.out.println(name);
        System.out.println(phone);
        System.out.println("空格" + address);
        int a = 38, b = 7;
        double b1 = (a * 1.0 / b);
        Math.round(38 * 1.0 / 7);
        int n = (int) Math.floor(29 / 1);
        System.out.println(n);

    }

}
