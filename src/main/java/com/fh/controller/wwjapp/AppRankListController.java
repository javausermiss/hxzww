package com.fh.controller.wwjapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fh.util.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fh.entity.system.AppUser;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.util.wwjUtil.RespStatus;

import net.sf.json.JSONObject;

/**
 * 抓娃娃排行榜
 */
@Controller
@RequestMapping(value = "/app/rank")
public class AppRankListController {

    @Resource(name = "appuserService")
    private AppuserManager appuserService;

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
     * 排行榜前10名
     * @return
     */
    @RequestMapping(value = "/rankList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject rankList() {
        try {
            List<AppUser> list = appuserService.rankList();
            Map<String, Object> map = new HashMap<>();
            map.put("appUser", list);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            return RespStatus.fail();
        }

    }

    /**
     * 排行榜个人所在名次
     * @param userId
     * @return
     */
    @RequestMapping(value = "/rankSelfList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject rankSelfList(@RequestParam ("userId") String userId) {
        try {
            AppUser appUser = appuserService.getAppUserRanklist(userId);
            if (appUser==null){
                return null;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("appUser",appUser);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            return RespStatus.fail();
        }

    }

    /**
     * 排行榜及个人所在名次
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/rankAndSelfList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject rankAndSelfList(@RequestParam("userId") String userId) {
        try {
            List<AppUser> list = appuserService.rankList();
            AppUser appUser = appuserService.getAppUserRanklist(userId);
            if (appUser == null) {
                return null;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("appUser", appUser);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            return RespStatus.fail();
        }

    }


    /**
     * 当日排行榜及个人所在名次
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/rankAndSelfListToday", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject rankAndSelfListToday(@RequestParam("userId") String userId) {
        try {
            List<AppUser> list = appuserService.rankListToday();
            AppUser appUser = appuserService.getAppUserRanklistToday(userId);
            if (appUser == null) {
                return null;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("appUser", appUser);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            return RespStatus.fail();
        }

    }



    /**
     * 竞猜排行榜及个人所在名次
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/rankBetList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject rankBetList(@RequestParam("userId") String userId) {
        try {
            List<PageData> list = appuserService.rankBetList();
            PageData appUser = appuserService.getAppUserBetRanklist(userId);

            if (appUser == null) {
                return null;
            }
            double c = (double) appUser.get("RANK");
            int i = (new Double(c)).intValue();
            appUser.put("RANK", String.valueOf(i));
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("appUser", appUser);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            return RespStatus.fail();
        }

    }

    /**
     * 竞猜排行榜及个人所在名次
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/rankBetListToday", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject rankBetListToday(@RequestParam("userId") String userId) {
        try {
            List<PageData> list = appuserService.rankBetListToday();
            PageData appUser = appuserService.getAppUserBetRanklistToday(userId);

            if (appUser == null) {
                return null;
            }
            double c = (double) appUser.get("RANK");
            int i = (new Double(c)).intValue();
            appUser.put("RANK", String.valueOf(i));
            Map<String, Object> map = new HashMap<>();
            map.put("list", list);
            map.put("appUser", appUser);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            return RespStatus.fail();
        }

    }



}
