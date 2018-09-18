package com.fh.service.system.sendgoods.impl;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Resource;

import com.fh.entity.system.*;
import com.fh.service.system.sendcost.SendCostManager;
import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.betgame.BetGameManager;
import com.fh.service.system.conversion.ConversionManager;
import com.fh.service.system.doll.DollManager;
import com.fh.service.system.doll.DollToyManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.service.system.playback.PlayBackManage;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.sendgoods.SendGoodsManager;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.RespStatus;
import com.fh.vo.system.DollToyVo;

import net.sf.json.JSONObject;

@Service("sendGoodsService")
public class SendGoodsService implements SendGoodsManager {


    @Resource(name = "appuserService")
    private AppuserManager appuserService;

    @Resource(name = "playBackService")
    private PlayBackManage playBackService;

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

    @Resource(name = "sendcostService")
    private SendCostManager sendcostService;

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    public JSONObject getAppUserInfo(String id) {
        try {
            AppUser appUser = appuserService.getUserByID(id);
            return JSONObject.fromObject(appUser);
        } catch (Exception e) {
            return null;
        }

    }


    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    public void save(PageData pd) throws Exception {
        dao.save("SendGoodsMapper.save", pd);
    }

    /**
     * 删除
     *
     * @param pd
     * @throws Exception
     */
    public void delete(PageData pd) throws Exception {
        dao.delete("SendGoodsMapper.delete", pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    public void edit(PageData pd) throws Exception {
        dao.update("SendGoodsMapper.edit", pd);
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("SendGoodsMapper.datalistPage", page);
    }

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PageData> listAll(Page pd) throws Exception {
        return (List<PageData>) dao.findForList("SendGoodsMapper.listAll", pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("SendGoodsMapper.findById", pd);
    }

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
        dao.delete("SendGoodsMapper.deleteAll", ArrayDATA_IDS);
    }

    @Override
    public int regSendGoods(SendGoods sendGoods) throws Exception {
        return (int) dao.save("SendGoodsMapper.regSendGoods", sendGoods);
    }

    /**
     * 申请发货
     *
     * @return
     * @throws Exception
     */
    public JSONObject doSendGoods(String playId, String number, String consignee, String remark, String userId, String mode, String costNum) throws Exception {
        //增加发货记录
        SendGoods sendGoods = new SendGoods();
        //免邮

        String[] pd = playId.split("\\,");
        if (pd.length < 3 && Integer.valueOf(number)>=3){
            return RespStatus.fail("数据不合法");
        }
        if (mode.equals("0")) {
            sendGoods.setMODE_DESPATCH("0");
        }
        //货到付款
        if (mode.equals("2")) {
            sendGoods.setMODE_DESPATCH("2");
        }
        //金币抵扣
        if (mode.equals("1")) {
            //满2包邮 ，一个需要付运费
            AppUser appUser = appuserService.getUserByID(userId);
            String balance = appUser.getBALANCE();
            Integer c;
            if (costNum == null) {
                //兼容以前APP版本 ，默认扣除80运费
                c = 80;

            } else {
                //获取相应省份运费
                SendCost sendCost = sendcostService.getSendCostByCostNum(Integer.valueOf(costNum));
                c = sendCost.getCOST();
            }

            if (Integer.valueOf(balance) < c) {
                return RespStatus.fail("余额不足，请充值");
            }
            int newbalance = Integer.valueOf(appUser.getBALANCE()) - c;
            appUser.setBALANCE(String.valueOf(newbalance));
            appuserService.updateAppUserBalanceById(appUser);
            Payment payment = new Payment();
            payment.setGOLD("-" + String.valueOf(c));
            payment.setUSERID(userId);
            payment.setDOLLID(null);
            payment.setCOST_TYPE("6");
            payment.setREMARK("支付运费");
            paymentService.reg(payment);
            sendGoods.setMODE_DESPATCH("1");

        }

        List<String> list = new LinkedList<>();
        List<PlayDetail> playDetails = new LinkedList<>();
        //获取需要发货的娃娃编号
        for (int i = 0; i < pd.length; i++) {
            PlayDetail playDetail = playDetailService.getPlayDetailByID(Integer.parseInt(pd[i]));
            playDetails.add(playDetail);
            String dollId = playDetail.getDOLLID();
            list.add(dollId);
            if (playDetail.getPOST_STATE().equals("0")) {
                playDetail.setPOST_STATE("1");
                playDetail.setSENDGOODS(consignee + "," + remark);
                playDetailService.updatePostState(playDetail);
            } else {
                return RespStatus.fail("已经发货或者兑换");
            }
        }
        List<String> list1 = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            String toyId = dollService.getDollByID(list.get(i)).getTOY_ID();
            DollToyVo dollToyVo = dolltoyService.getDollToyByToyId(toyId);
            String toyName = dollToyVo.getToy_name();
            list1.add(toyName);
        }
        List<String> list2 = new LinkedList<>();
        for (int i = 0; i < list1.size(); i++) {
            int n = Collections.frequency(list1, list1.get(i));
            String toyName = list1.get(i);
            list2.add(toyName + "数量为：" + n);
        }
        List<String> newList = new LinkedList(new TreeSet(list2));
        String[] s = consignee.split("\\,");
        String name = s[0];
        String phone = s[1];
        String address = s[2];
        sendGoods.setCNEE_NAME(name);
        sendGoods.setCNEE_ADDRESS(address);
        sendGoods.setCNEE_PHONE(phone);
        sendGoods.setPLAYBACK_ID(null);//抓取的娃娃编号
        sendGoods.setGOODS_NUM(number);
        sendGoods.setREMARK(remark);//留言
        sendGoods.setUSER_ID(userId);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < newList.size(); i++) {
            String ss = newList.get(i).toString();
            String ss1 = ss.substring(ss.length() - 1, ss.length());
            int num = Integer.valueOf(ss1);
            String toyname = ss.substring(0, ss.length() - 5);
            DollToyVo dollToyVo = dolltoyService.getDollToyByToyName(toyname);
            int toyNum = dollToyVo.getToy_num();
            dollToyVo.setToy_num(toyNum - num);
            dolltoyService.updateToyNum(dollToyVo);
            String d = newList.get(i);
            sb.append(d + "，");
        }
        sendGoods.setPOST_REMARK(sb.toString());
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        sendGoods.setCREATE_TIME(time);
        sendGoods.setTOY_NUM(playId);
        int sg = this.regSendGoods(sendGoods);
        if (sg == 0) {
            return RespStatus.fail("增加记录失败");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("playback", playDetails);
        return RespStatus.successs().element("data", map);
    }

    /**
     * 兑换金币
     *
     * @param id
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject doConversionGold(String id, String userId) throws Exception {


        String[] pd = id.split("\\,");//获取需要兑换的抓中娃娃编号
        for (int i = 0; i < pd.length; i++) {
            String pid = pd[i];
            PlayDetail playBack = playDetailService.getPlayDetailByID(Integer.parseInt(pid));
            if (playBack.getPOST_STATE().equals("0")) {
                AppUser appUser = appuserService.getUserByID(userId);
                int balance = Integer.parseInt(appUser.getBALANCE());
                String c = playBack.getCONVERSIONGOLD();
                int m = Integer.valueOf(c);
                String newBalance = String.valueOf(balance + m);
                appUser.setBALANCE(newBalance);
                appuserService.updateAppUserBalanceById(appUser);
                playBack.setPOST_STATE("2");//兑换
                playDetailService.updatePostStateForCon(playBack);
            } else {
                return RespStatus.fail("该娃娃已经被兑换过或者寄出");
            }
            Conversion conversion = new Conversion();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(date);
            conversion.setCREATETIME(time);
            String dollname ;//
            if (playBack.getTOY_ID() != null && !playBack.getTOY_ID().equals("")) {
                DollToyVo dollToyVo =  dolltoyService.getDollToyByToyId(String.valueOf(playBack.getTOY_ID()));
                dollname = dollToyVo.getToy_name();
            } else {
                dollname = dollService.getDollByID(playDetailService.getPlayDetailByID(Integer.valueOf(playBack.getID())).getDOLLID()).getDOLL_NAME();
            }
            conversion.setDOLLNAME(dollname);
            conversion.setNUMBER("1");
            conversion.setUSERID(userId);
            conversion.setPLAYID(pid);
            conversion.setCONMONEY(String.valueOf(playBack.getCONVERSIONGOLD()));
            conversionService.reg(conversion);
            Payment payment = new Payment();
            payment.setGOLD("+" + String.valueOf(playBack.getCONVERSIONGOLD()));
            payment.setUSERID(userId);
            payment.setDOLLID(playBack.getDOLLID());
            payment.setCOST_TYPE("7");
            payment.setREMARK("兑换" + dollname);
            paymentService.reg(payment);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("appUser", getAppUserInfo(userId));
        return RespStatus.successs().element("data", map);
    }

    @Override
    public List<SendGoods> getLogisticsByUserId(String userId) throws Exception {
        return (List<SendGoods>) dao.findForList("SendGoodsMapper.getLogisticsByUserId", userId);
    }

    @Override
    public SendGoods getSendById(String id) throws Exception {
        return (SendGoods) dao.findForObject("SendGoodsMapper.getSendById", id);
    }

    /**
     * 定时兑换娃娃（年后上线）
     * @param playDetail
     * @throws Exception
     */
    @Override
    public void doConversionGoldAuto(PlayDetail playDetail) throws Exception {
        try {
            playDetail.setPOST_STATE("2");
            playDetailService.updatePostState(playDetail);
            //用户更新余额
            AppUser appUser = appuserService.getUserByID(playDetail.getUSERID());
            int balance = Integer.parseInt(appUser.getBALANCE());
            String c = playDetail.getCONVERSIONGOLD();
            int m = Integer.valueOf(c);
            String newBalance = String.valueOf(balance + m);
            appUser.setBALANCE(newBalance);
            appuserService.updateAppUserBalanceById(appUser);
            //添加兑换记录
            Conversion conversion = new Conversion();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(date);
            conversion.setCREATETIME(time);
            String dollname ;//
            if (playDetail.getTOY_ID() != null && !playDetail.getTOY_ID().equals("")) {
                DollToyVo dollToyVo =  dolltoyService.getDollToyByToyId(String.valueOf(playDetail.getTOY_ID()));
                dollname = dollToyVo.getToy_name();
            } else {
                dollname = dollService.getDollByID(playDetailService.getPlayDetailByID(Integer.valueOf(playDetail.getID())).getDOLLID()).getDOLL_NAME();
            }

            conversion.setDOLLNAME(dollname);
            conversion.setNUMBER("1");
            conversion.setUSERID(playDetail.getUSERID());
            conversion.setPLAYID(playDetail.getID());
            conversion.setCONMONEY(String.valueOf(playDetail.getCONVERSIONGOLD()));
            conversionService.reg(conversion);
            //添加明细记录
            Payment payment = new Payment();
            payment.setGOLD("+" + String.valueOf(playDetail.getCONVERSIONGOLD()));
            payment.setUSERID(playDetail.getUSERID());
            payment.setDOLLID(playDetail.getDOLLID());
            payment.setCOST_TYPE("7");
            payment.setREMARK("超时兑换" + dollname);
            paymentService.reg(payment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * 冻结用户，更新改用户的所有订单为异常订单
     * @param
     * @throws Exception
     */
    public void dofreezeUserOrder(String userId)throws Exception{
    	 dao.update("SendGoodsMapper.dofreezeUserOrder", userId);
    }


    public static void main(String[] a){
        String[] num = "asdasd,asdads,asdasd".split("\\,");
        System.out.println(num.length);
    }
}
