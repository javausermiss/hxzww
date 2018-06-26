package com.fh.controller.wwjapp;

import com.fh.entity.system.Doll;
import com.fh.service.system.doll.DollManager;
import com.fh.util.wwjUtil.*;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * 网关注册效验接口
 *
 * @author wjy
 * 2017/11/07
 */
@Controller
@RequestMapping(value = "/app/dollReg")
public class AppDollRegController {
    @Resource(name = "dollService")
    private DollManager dollService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject dollLogin(@RequestParam("sn") String sn, @RequestParam("code") String code ,
                                @RequestParam(value = "deviceType" ,required = false ,defaultValue = "1") String deviceType) {

        try {
            Doll doll = dollService.getDollBySN(sn);
            String localCode = DollRegUtil.getCode(sn);
            if (!localCode.equals(code)) {
                return RespStatus.fail("fail");
            }
            if (doll == null) {
                Doll doll1 =  new Doll("room_"+MyUUID.getUUID(),sn,deviceType);
                int a = dollService.regDollBySN(doll1);
                if (a != 1) {
                    return RespStatus.fail("fail");
                }
                Doll newDoll = dollService.getDollBySN(sn);
                String dollId = newDoll.getDOLL_ID();
                String sessionID = MyUUID.createSessionId();
                RedisUtil.getRu().set("sessionId:gateway:"+ dollId, sessionID);
                return RespStatus.successs().element("sessionID", sessionID).element("roomID", dollId);
            } else {
                String dollId = doll.getDOLL_ID();
                String sessionID = MyUUID.createSessionId();
                RedisUtil.getRu().set("sessionId:gateway:" + dollId, sessionID);
                return RespStatus.successs().element("sessionID", sessionID).element("roomID", dollId);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return RespStatus.exception();
        }


    }


}
