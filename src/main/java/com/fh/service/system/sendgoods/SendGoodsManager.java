package com.fh.service.system.sendgoods;

import java.util.List;

import com.fh.entity.Page;
import com.fh.entity.system.PlayDetail;
import com.fh.entity.system.SendGoods;
import com.fh.util.PageData;

import net.sf.json.JSONObject;

public interface SendGoodsManager {
    /**新增
     * @param pd
     * @throws Exception
     */
    public void save(PageData pd)throws Exception;

    /**删除
     * @param pd
     * @throws Exception
     */
    public void delete(PageData pd)throws Exception;

    /**修改
     * @param pd
     * @throws Exception
     */
    public void edit(PageData pd)throws Exception;

    /**列表
     * @param page
     * @throws Exception
     */
    public List<PageData> list(Page page)throws Exception;

    /**列表(全部)
     * @param pd
     * @throws Exception
     */
    public List<PageData> listAll(Page  pd)throws Exception;

    /**通过id获取数据
     * @param pd
     * @throws Exception
     */
    public PageData findById(PageData pd)throws Exception;

    /**批量删除
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    public void deleteAll(String[] ArrayDATA_IDS)throws Exception;

    public int regSendGoods (SendGoods sendGoods)throws Exception;

    /**
     * 申请发货
     * @return
     * @throws Exception
     */
    public JSONObject doSendGoods(String playId,String number,String consignee,String remark,String userId,String mode,String costNum,String levels) throws Exception;

    /**
     * 兑换金币
     * @param id
     * @param userId
     * @return
     * @throws Exception
     */
    public JSONObject doConversionGold(String id,String userId)throws Exception;

    /**
     * 查询用户发货订单物流信息
     * @param userId
     * @return
     * @throws Exception
     */
    public List<SendGoods> getLogisticsByUserId (String userId)throws Exception;

    /**
     * 根据ID查询订单信息
     * @param id
     * @return
     * @throws Exception
     */
    public SendGoods getSendById(String id)throws Exception;

    /**
     * 兑换过期未兑换未发货娃娃
     * @param playDetail
     * @throws Exception
     */
    public void doConversionGoldAuto(PlayDetail playDetail)throws Exception;


    /**
     * 冻结用户，更新改用户的所有订单为异常订单
     * @param playDetail
     * @throws Exception
     */
    public void dofreezeUserOrder(String userId)throws Exception;



}
