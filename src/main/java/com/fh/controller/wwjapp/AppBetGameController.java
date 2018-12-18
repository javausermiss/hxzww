package com.fh.controller.wwjapp;

import com.fh.entity.system.*;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.betgame.BetGameManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.playback.PlayBackManage;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.pond.PondManager;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.RespStatus;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 竞猜接口
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/app/betgame")
public class AppBetGameController {

    @Resource(name = "betGameService")
    private BetGameManager betGameService;
    @Resource(name = "appuserService")
    private AppuserManager appuserService;
    @Resource(name = "dollService")
    private DollManager dollService;
    @Resource(name = "playBackService")
    private PlayBackManage playBackService;
    @Resource(name = "pondService")
    private PondManager pondService;
    @Resource(name = "playDetailService")
    private PlayDetailManage playDetailService;
    @Resource(name = "paymentService")
    private PaymentManager paymentService;


    /**
     * 奖池信息
     *
     * @param id
     * @return
     */

    public JSONObject getPondInfo(Integer id) {
        try {
            Pond pond = pondService.getPondById(id);
            return JSONObject.fromObject(pond);
        } catch (Exception e) {
            return null;
        }

    }

    public JSONObject getAppUserInfo(String id) {
        try {
            AppUser appUser = appuserService.getUserByID(id);
            return JSONObject.fromObject(appUser);
        } catch (Exception e) {
            return null;
        }

    }

    public JSONObject getPlayBackInfo(Integer id) {
        try {
            PlayBack playBack = playBackService.getPlayBackById(id);
            return JSONObject.fromObject(playBack);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 游戏记录信息
     *
     * @param dollID
     * @return
     */
    public JSONObject getPlayDetailInfo(String dollID) {
        try {
            PlayDetail p = playDetailService.getPlayIdForPeople(dollID);
            return JSONObject.fromObject(p);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 给围观群众分发场次ID
     *
     * @return
     */
    @RequestMapping(value = "/getPlayId", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPlayId(@RequestParam("dollId") String dollId) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("playDetail", getPlayDetailInfo(dollId));
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }


    }

    /**
     * 2017/12/12
     * 玩家点击竞猜，然后围观群众进行投注
     *
     * @param userId   参与竞猜用户ID
     * @param dollId   娃娃机房间ID
     * @param wager    单次竞猜金额（房间金额*倍数）
     * @param guessId  场次ID 如果属于无用户玩游戏，则此值传字符串null
     * @param guessKey 竞猜数字
     * @param multiple 竞猜倍数
     * @param afterVoting 追投期数
     * @param flag 是否开始游戏标签，在游戏中true 不在游戏中false
     * @return
     */
    @RequestMapping(value = "/bets", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject bets(
            @RequestParam("userId") String userId,
            @RequestParam("dollId") String dollId,
            @RequestParam("wager") int wager,
            @RequestParam(value = "guessId",required = false,defaultValue = "null") String guessId,
            @RequestParam("guessKey") String guessKey,
            @RequestParam(value = "multiple",required = false,defaultValue = "1") Integer multiple,
            @RequestParam(value = "afterVoting",required = false,defaultValue = "0") Integer afterVoting,
            @RequestParam("flag") String flag
            )
    {

        try {
            String[] guessKeys  = guessKey.split(",");
            int keysNum = guessKeys.length;
          return  betGameService.doBet(userId,dollId,wager,guessId,guessKey,multiple,afterVoting,flag,keysNum);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }


    }

    @RequestMapping(value = "/betList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject betList(@RequestParam("dollId") String dollId) {
        try {
            List<Pond> list =  pondService.getGuessList(dollId);
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("pondList",list);
            return RespStatus.successs().element("data",map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }
    }


    /**
     * 获取下注人数对比的接口
     *
     * @param guessid
     * @param dollId
     * @return
     */
    @RequestMapping(value = "/getPond", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPond(@RequestParam("playId") String guessid,
                              @RequestParam("dollId") String dollId
    ) {

        try {
            Pond pond = new Pond();
            pond.setDOLL_ID(dollId);
            pond.setGUESS_ID(guessid);
            Pond pond1 = pondService.getPondByPlayId(pond);
            if (pond1 == null) {
                return RespStatus.fail("该奖池不存在");
            }
            Map<String, Object> map = new HashMap<>();
            map.put("pond", getPondInfo(pond1.getPOND_ID()));
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }
    
    
    /**
     * 根据用户Id获取当前用户的竞猜最新10条记录
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getGuessDetailTop10", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getPond( @RequestParam("userId") String userId) {

        try {
        	List<PageData> dataList=betGameService.getGuessDetailTop10ByUserId(userId);
        	Map<String, Object> map = new HashMap<>();
        	map.put("dataList", dataList);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail();
        }

    }

    /**
     * 查询用户收支明细
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getPaymenlist", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody

    public JSONObject getPaymenlist(@RequestParam ("userId") String userId){

        try {
           List<Payment> paymentList =  paymentService.getPaymenlist(userId);
           Map<String,Object> map = new LinkedHashMap<>();
           map.put("paymentList",paymentList);
           map.put("appUser",getAppUserInfo(userId));
            return RespStatus.successs().element("data",map);
        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }



    }

    /**
     * 获取最近的10条竞猜记录(不包含无人竞猜的记录)
     * @return
     */
    @RequestMapping(value = "/getGuesserlast10", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody

    public JSONObject getGuesserlast10(){
        try {
            List<Pond> list = pondService.getGuesserlast10();
            Map<String,Object> map = new HashMap<>();
            map.put("pondList",list);
            return RespStatus.successs().element("data",map);

        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }

    }

    public static void main(String[] a){
        String guessKey = "4,5";
        String[] guessKeys  = guessKey.split(",");
        List<String> list = Arrays.asList(guessKeys);
        int keysNum = guessKeys.length;
        if (list.contains("5")){
            System.out.println("ZHONGJIANG");
        }
        System.out.println(Arrays.toString(guessKeys));
        System.out.println(keysNum);
    }










    }
