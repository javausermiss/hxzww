package com.fh.controller.wwjapp;

import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.userpoints.UserPointsManager;
import com.fh.util.PageData;
import com.fh.util.express.Response;
import com.fh.util.wwjUtil.RespStatus;
import net.sf.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/app/level")
public class AppUserLevelController {


    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "userpointsService")
    private UserPointsManager userpointsService;


    /**
     * 每月首日凌晨6点 刷新用户的的等级礼包
     */
    @Scheduled(cron = "0 06 00 1 * ?")
    public void flushAppUserLevelGift() {

        try {
            appuserService.updateAppUserListForLevelGift();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/getUserLevel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getUserLevel(@RequestParam("userId") String userId) {
        try {
            //计算用户等级
            PageData pageData_ = new PageData();
            pageData_.put("userId", userId);
            PageData pageData1_ = userpointsService.getCostGoldSumAll(pageData_);
            String gm_ = "0";
            if (pageData1_ != null) {
                double aa = (double) pageData1_.get("godsum");
                gm_ = new DecimalFormat("0").format(aa).substring(1);
            }

            double costSumGold = Double.valueOf(gm_);
            int level = 0;
            double levelUp = 0;
            double levelGold = 0;
            double levelB = 0;
            double difference = 0;
            String percentage = "";
            DecimalFormat df = new DecimalFormat("0.0000");
            if (costSumGold >= 100) {
                for (int i = 1; i < 100; i++) {
                    levelGold = 200 * (Math.pow(1.5, i) - 1);
                    if (levelGold > costSumGold) {
                        level = i - 1;
                        levelUp = 100.0 * (Math.pow(1.5, i - 1));
                        levelB = 200 * (Math.pow(1.5, i - 1) - 1);
                        percentage = df.format((costSumGold - levelB) / levelUp);
                        difference = levelGold - costSumGold;
                        break;
                    } else if (levelGold == costSumGold) {
                        levelGold = 200 * (Math.pow(1.5, i + 1) - 1);
                        level = i;
                        percentage = "0.0000";
                        difference = levelGold - costSumGold;
                        break;
                    }

                }
            } else {
                difference = Math.round(100 - costSumGold);
                percentage = df.format(costSumGold / 100.0);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("level", level);
            map.put("residualValue", difference);
            map.put("percentage", percentage);

            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.fail(e.getMessage());
        }
    }

}
