package com.fh.controller.wwjapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.fh.entity.system.Doll;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fh.controller.base.BaseController;
import com.fh.entity.Page;
import com.fh.entity.system.ToyType;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.toytype.ToyTypeManager;
import com.fh.util.NumberUtils;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.RedisUtil;
import com.fh.util.wwjUtil.RespStatus;
import com.fh.vo.system.CameraVo;
import com.fh.vo.system.DollVo;

import net.sf.json.JSONObject;

/**
 * APP DOLL ctrl
 *
 * @author JAVA_DEV
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/app/doll")
public class AppDollController extends BaseController {

    @Resource(name = "dollService")
    private DollManager dollService;
    @Resource(name = "toytypeService")
    private ToyTypeManager toytypeService;


    /**
     * 所有娃娃机列表
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/getDollList", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getDollLists(HttpServletRequest req) {
        try {
            List<DollVo> dollList = dollService.getDollVoList();
            /* Random index = new Random();
             List<Integer> indexList = new ArrayList<>();
             List<DollVo> newList = new ArrayList<>();
             for(int i=0,j;i<10;i++){
                 j = index.nextInt(dollList.size());
                 //判断是否重复
                 if(!indexList.contains(j)){
                     //获取元素
                     indexList.add(j);
                     newList.add(dollList.get(j));
                 }else{
                     i--;//如果重复再来一次
                 }
             }*/
             
             //娃娃机网关信息
            if (dollList != null && dollList.size() > 0) {
                for (DollVo dollVo : dollList) {
                    dollVo.setDollState(RedisUtil.getStr("roomInfo:" + dollVo.getDollId()));
                }
            }
            return RespStatus.successs().element("dollList", dollList);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return RespStatus.exception();
        }
    }

    /**
     * 娃娃机分页表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getDollPage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getDollPage(HttpServletRequest request) {
        try {
            //获取当前页
            Page page = new Page();
            int currentPage = NumberUtils.parseInt(request.getParameter("nextPage"), 1);
            page.setCurrentPage(currentPage); //当前页数
            
        	String ctype=request.getParameter("ctype"); //SDK
        	String channel=request.getParameter("channel");//渠道

            int showCount = 8 * 2;
            page.setShowCount(showCount);//

            //获取前端分类
            PageData pd = new PageData();
            String currentType = request.getParameter("currentType");
            pd.put("currentType", currentType);
            pd.put("currentPage", currentPage);
            pd.put("showCount", showCount);
            pd.put("channel", channel);
            
            page.setPd(pd);

            //娃娃机列表分页
            List<DollVo> dollList = dollService.getDollPage(page);
            
            //前端展示集合
            List<DollVo> tmpList =new ArrayList<DollVo>();
            
            //娃娃机网关信息
            if (dollList != null && dollList.size() > 0) {
            	  List<CameraVo> cameras =new ArrayList<CameraVo>(); //网关摄像头集合
            	 
            	  CameraVo cameraVo=null;
                for (DollVo dollVo : dollList) {
                    //获取概率
                    String prob =  RedisUtil.getStr("roomProbability:" + dollVo.getDollId());
                    if (prob != null){
                        dollVo.setProb(prob);
                    }
                    //预计奖金
                    dollVo.setReward(String.valueOf(dollVo.getDollGold()*5));
                    //异常设备不展示 FREE表正常，USING:游戏中
                	cameras=dollVo.getCameras();
                	if(cameras !=null && cameras.size()>=1){
                		boolean cState=true;
                		for(int i=0;i<cameras.size();i++){
                			cameraVo=cameras.get(i);
                			if(!"0".equals(cameraVo.getDeviceState())){ // DEVICE_STATE:0 正常 ，1:不正常
                				logger.info(cameraVo.getCameraId()+",cameraType="+cameraVo.getCameraType()+" deviceState-------->"+cameraVo.getDeviceState());
                				cState=false;
                			}
                		}
                		if(cState){
                			//娃娃机网关正常，并且摄像头状态正常
                			tmpList.add(dollVo);
                		}
                	}
                }
            }
            
            Map<String, Object> map = new HashMap<>();
            map.put("dollList", tmpList);
            //分页信息
            pd.put("currentPage", page.getCurrentPage());
            pd.put("showCount", (page.getShowCount() / 2));
            pd.put("totalPage", (page.getTotalPage()));
            map.put("pd", pd); //搜索的顺序
            return RespStatus.successs().element("data", map);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return RespStatus.exception();
        }
    }

    /**
     * 获取分页列表
     *
     * @return
     */
    @RequestMapping(value = "/getAllToyTypeList", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public JSONObject getAllToyTypeList() {
        try {
            List<ToyType> list = toytypeService.getAllToyType();
            Map<String, Object> map = new HashMap<>();
            map.put("toyTypeList", list);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return RespStatus.exception();
        }

    }


    /**
     * 获取分页列表
     *
     * @return
     */
    @RequestMapping(value = "/getDollToyNum", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
                public JSONObject getDollToyNum(@RequestParam("roomId") String roomId) {
        try {
            Doll doll = dollService.getToyNum(roomId);
            int num =  doll.getTOY_NUM();
            Map<String,Object> map = new HashMap<>();
            map.put("toyNum", num);
            return RespStatus.successs().element("data", map);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
            return RespStatus.exception();
        }

    }






}

