package com.fh.controller.wwjapp;

import com.fh.entity.system.AppUser;
import com.fh.entity.system.RedPackage;
import com.fh.entity.system.UserRecRedPInfo;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.redpackage.RedPackageManager;
import com.fh.service.system.userrecredpinfo.UserRecRedPinfoManager;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RedPacketUtil;
import com.fh.util.wwjUtil.RedisUtil;
import com.fh.util.wwjUtil.RespStatus;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.log4j.Log4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/app/redPackage")
@Log4j
public class AppRedPackage{

    @Resource(name="redpackageService")
    private RedPackageManager redpackageService;

    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name="userrecredpinfoService")
    private UserRecRedPinfoManager userrecredpinfoService;

    /**
     * 用户发红包
     * @param userId
     * @param gold
     * @param num
     * @return
     */
    @RequestMapping(value = "/shareRedPackage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject shareRedPackage(@RequestParam("userId") String userId,
                                      @RequestParam("gold") Integer gold,
                                      @RequestParam("num") Integer num
    ){
        try {
            AppUser appUser =  appuserService.getUserByID(userId);
            String gender =  appUser.getGENDER();
            if (gender == null || gender.equals("")){
                return RespStatus.fail("该用户未设置性别");
            }
            if (gender.equals("男")){
                gender = "0";
            }else if (gender.equals("女")){
                gender = "1";
            }
            int ub =  Integer.valueOf(appUser.getBALANCE());
            if (ub < gold || num > gold){
                return RespStatus.fail("数据不合法");
            }
            RedPackage redPackage = new RedPackage();
            String id = MyUUID.getUUID32();
            redPackage.setREDPACKAGE_ID(id);
            redPackage.setREDGOLD(gold);
            redPackage.setREDNUM(num);
            redPackage.setTAG(gender);
            redPackage.setUSERID(userId);
            redpackageService.inset(redPackage);

            RedPacketUtil rp = new RedPacketUtil();
            List<Integer> list =  rp.splitRedPackets(gold,num);
            String sb = "red:"+ userId + ":" + id ;
            RedisUtil.getRu().set(sb,String.valueOf(list.size()));
            for (int i = 0; i <list.size() ; i++) {
                RedisUtil.getRu().set("red:"+userId+":"+id+":"+(i+1),String.valueOf(list.get(i)));
            }
            return RespStatus.successs();
        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }
    }
    @RequestMapping(value = "/getRedPackage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject showRedPackage(@RequestParam("userId") String userId){
        try {
            AppUser appUser  = appuserService.getUserByID(userId);
            String gender = appUser.getGENDER();
            if (gender.equals("男")){
                gender = "0";
            }else if (gender.equals("女")){
                gender = "1";
            }
            List<RedPackage> list = redpackageService.getRedPackageByGender(gender);







            return RespStatus.successs();
        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }

    }

    /**
     * 用户领取红包
     * @param userId
     * @param redUserId
     * @param redId
     * @return
     */
    @RequestMapping(value = "/getRedPackage1", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject  getRedPackage(@RequestParam("userId")String userId,
                                    @RequestParam("redUserId") String redUserId,
                                    @RequestParam("redId") String redId
                                    ) {
        try {
            AppUser redUser = appuserService.getUserByID(redUserId);
            String redGender = redUser.getGENDER();

            AppUser appUser = appuserService.getUserByID(userId);
            String gender =  appUser.getGENDER();

            //判断性别是否一致
            if (gender.equals(redGender)){
                return RespStatus.fail("性别相同，领取失败");
            }

            String num = RedisUtil.getRu().get("red:"+redUserId+":"+redId);
            if (num == null){
                return RespStatus.fail("该红包已经被抢空");
            }
            UserRecRedPInfo u = new UserRecRedPInfo();
            u.setREDPACKAGE_ID(redId);
            u.setUSERID(userId);
            u.setREDUSERID(redUserId);

            UserRecRedPInfo uf =  userrecredpinfoService.find(u);
            if (uf != null){
                return RespStatus.fail("用户不能重复领取该红包");
            }
            userrecredpinfoService.doGetRedPackage(num,userId,redUserId,redId);

            return RespStatus.successs();

        }catch (Exception e){
            e.printStackTrace();
            return RespStatus.fail();
        }


    }



}
