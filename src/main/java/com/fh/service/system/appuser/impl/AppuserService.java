package com.fh.service.system.appuser.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.AppUser;
import com.fh.entity.system.Payment;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.service.system.payment.PaymentManager;
import com.fh.util.Const;
import com.fh.util.EmojiUtil;
import com.fh.util.NumberUtils;
import com.fh.util.PageData;
import com.fh.util.StrUtil;
import com.fh.util.wwjUtil.MyUUID;


/**类名称：AppuserService
 * @author FH Q313596790
 * 修改时间：2015年11月6日
 */
@Service("appuserService")
public class AppuserService implements AppuserManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	
    @Resource(name = "paymentService")
    private PaymentManager paymentService;
	
	/**列出某角色下的所有会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllAppuserByRorlid(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("AppuserMapper.listAllAppuserByRorlid", pd);
	}
	
	/**会员列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listPdPageUser(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.userlistPage", page);
	}
	
	/**通过用户名获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUsername(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByUsername", pd);
	}
	
	/**通过邮箱获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByEmail(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByEmail", pd);
	}
	
	/**通过编号获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByNumber(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByNumber", pd);
	}
	
	/**保存用户
	 * @param pd
	 * @throws Exception
	 */
	public void saveU(PageData pd)throws Exception{
		dao.save("AppuserMapper.saveU", pd);
	}
	
	/**删除用户
	 * @param pd
	 * @throws Exception
	 */
	public void deleteU(PageData pd)throws Exception{
		dao.delete("AppuserMapper.deleteU", pd);
	}
	
	/**修改用户
	 * @param pd
	 * @throws Exception
	 */
	public void editU(PageData pd)throws Exception{
		dao.update("AppuserMapper.editU", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByUiId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.findByUiId", pd);
	}
	
	/**全部会员
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAllUser(PageData pd)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.listAllUser", pd);
	}
	
	/**批量删除用户
	 * @param USER_IDS
	 * @throws Exception
	 */
	public void deleteAllU(String[] USER_IDS)throws Exception{
		dao.delete("AppuserMapper.deleteAllU", USER_IDS);
	}
	
	/**获取总数
	 * @param value
	 * @throws Exception
	 */
	public PageData getAppUserCount(String value)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.getAppUserCount", value);
	}

	/**
	 * 注册用户
	 * @param phone
	 * @return
	 * @throws Exception
	 */

	public int reg(String phone,String url) throws Exception {
		return (int)dao.save("AppuserMapper.reg",new AppUser(MyUUID.createSessionId(),phone,null,phone,phone,url,"3"));
	}

	public int regAppUser(AppUser appUser) throws Exception {
		return (int)dao.save("AppuserMapper.regAppUser",appUser);
	}
/**
	 * 通过手机号码查询用户信息
	 * @param phone
	 * @return
	 * @throws Exception
	 */

	public AppUser getUserByPhone(String phone) throws Exception {
		return (AppUser) dao.findForObject("AppuserMapper.getUserByPhone",phone);
	}

	/**
	 * 通过ID查询用户信息
	 * @param id
	 * @return
	 * @throws Exception
	 */

	public AppUser getUserByID(String id) throws Exception {
		return (AppUser) dao.findForObject("AppuserMapper.getUserByID",id);
	}

	/**
	 * 更改用户头像
	 * @param appUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateAppUserImage(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserImage",appUser);
	}

	/**
	 * 更改用户昵称
	 * @param appUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateAppUserName(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserName",appUser);
	}

	/**
	 * 根据用户名获取用户信息
	 * @param name
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppUser getAppUserByUserName(String name) throws Exception {
		return (AppUser) dao.findForObject("AppuserMapper.getAppUserByNickName",name);
	}

	/**
	 * 根据昵称获取用户信息
	 * @param nickName
	 * @return
	 * @throws Exception
	 */
	@Override
	public AppUser getAppUserByNickName(String nickName) throws Exception {
		return (AppUser)dao.findForObject("AppuserMapper.getAppUserByNickName",nickName);
	}

	@Override
	public List<AppUser> getAppUserByNickNameList(String nickName) throws Exception {
		return (List<AppUser>)dao.findForObject("AppuserMapper.getAppUserByNickNameList",nickName);
	}

	/**
	 * 获取用户更新的的账户余额
	 * @param appUser
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateAppUserBalanceByPhone(AppUser appUser) throws Exception {
		return (int) dao.update("AppuserMapper.updateAppUserBalanceByPhone",appUser);
	}

	/**
	 * rank榜单
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<AppUser> rankList() throws Exception {
		return (List<AppUser>) dao.findForList("AppuserMapper.rankList",null);
	}

	/**
	 * 更新用户的抓娃娃数量
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateAppUserDollTotalById(AppUser appUser) throws Exception {
		return (int) dao.update("AppuserMapper.updateAppUserDollTotalById",appUser);
	}

	@Override
	public int updateAppUserBalanceById(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserBalanceById",appUser);
	}

	@Override
	public int updateAppUsernickName(AppUser appUser) throws Exception {
		return (int) dao.update("AppuserMapper.updateNickName",appUser);
	}

	@Override
	public int updateAppUserCnee(AppUser appUser) throws Exception {
		return (int) dao.update("AppuserMapper.updateAppUserCnee",appUser);
	}

	@Override
	public int regwx(AppUser appUser) throws Exception {
		appUser.setNICKNAME(EmojiUtil.emojiConverterToAlias(appUser.getNICKNAME())); //修改用户昵称转码
		return (int) dao.save("AppuserMapper.regwx",appUser);
	}

	@Override
	public int updateTencentUser(AppUser appUser) throws Exception {
		appUser.setNICKNAME(EmojiUtil.emojiConverterToAlias(appUser.getNICKNAME())); //修改用户昵称转码
		return (int)dao.update("AppuserMapper.updateTencentUser",appUser);
	}

	@Override
	public AppUser testuser(AppUser appUser) throws Exception {
		return (AppUser)dao.findForObject("AppuserMapper.testuser",appUser);
	}

	@Override
	public List<AppUser> getAppUserList() throws Exception {
		return (List<AppUser>)dao.findForList("AppuserMapper.getAppUserList",null);
	}

	@Override
	public int updateAppUserSB(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserSB",appUser);
	}

	@Override
	public int updateAppUserSign(AppUser appUser) throws Exception {
		return (int)dao.update("AppuserMapper.updateAppUserSign",appUser);
	}
	
	

    /**
     * 修改账户金币
     * @param userId appUser.userId 用户对象主键
     * @param operNum 操作的数量
     * @param operType A:add,加; S:sub,减
     * @param operMenu 操作的枚举
     * @return
     */
    public int updateUserBalance(String userId,int operNum,String operType,Const.PlayMentCostType operMenu) throws Exception{
    	
    	int oper=0;
    	AppUser appUser=this.getUserAppById(userId);
    	//step1 判断用户是否存在
    	if(appUser ==null){
    		return 0;
    	}
    	if(StrUtil.isNullOrEmpty(appUser.getUSER_ID())){
    		return 0;
    	}
    	if(StrUtil.isNullOrEmpty(appUser.getBALANCE())){
    		appUser.setBALANCE("0");
    	}
    	//step2  判断金额
    	if(operNum<=0){
    		return 0;
    	}
    	
    	//step3  判断追加类型
    	String operGlod="0";
    	if(StrUtil.isNullOrEmpty(operType)){
    		return 0;
    	}else{
    		String newBalance=appUser.getBALANCE();
    		if("A".equals(operType)){
    			operGlod="+"+operNum;
    			newBalance=NumberUtils.add(appUser.getBALANCE(), String.valueOf(operNum));
    		}else if("S".equals(operType)){
    			operGlod="-"+operNum;
    			newBalance=NumberUtils.sub(appUser.getBALANCE(), String.valueOf(operNum));
    		}
    		appUser.setBALANCE(newBalance);
    		
    	}
    	
    	
    	//step4 判断操作类型
    	if(operMenu==null){
    		return 0;
    	}
    	
    	
    	//step5 记录交易明细
        Payment payment = new Payment();
        payment.setCOST_TYPE(operMenu.getValue());
        payment.setUSERID(appUser.getUSER_ID());
        payment.setGOLD(operGlod);
        payment.setREMARK(operMenu.getName());
        paymentService.reg(payment);
    	
        
        //修改金额余额
        oper=this.updateAppUserBalanceNew(appUser);
    	
    	return oper;
    }
    
	/**
	 * 修改用户金币
	 * @param appUser
	 * @return
	 * @throws Exception
	 */
	private int updateAppUserBalanceNew(AppUser appUser)throws Exception{
		return (int)dao.update("AppuserMapper.updateAppUserBalanceNew",appUser);
	}
	
	/**
	 * 查询用户信
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private AppUser getUserAppById(String userId)throws Exception{
		return (AppUser)dao.findForObject("AppuserMapper.getUserAppById",userId);
	}

	@Override
	public AppUser getAppUserRanklist(String userId) throws Exception {
		return (AppUser)dao.findForObject("AppuserMapper.getAppUserRanklist",userId);
	}
	
	/**用户游戏统计列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listUserGames(Page page)throws Exception{
		return (List<PageData>) dao.findForList("AppuserMapper.getAppUserGamelistPage", page);
	}
	
	
	/**查看用户的充值总记录**/
	public PageData getAppUesrRechargeToTal(String userId)throws Exception{
		return (PageData)dao.findForObject("AppuserMapper.getAppUesrRechargeToTal",userId);
	}
	
    /**
     * 查询渠道的总注册用户数量
     * @param channelCode
     * @return
     * @throws Exception
     */
    public Integer getSysAppUserCountByChannelCode(String channelCode)throws Exception{
    	return (int)dao.findForObject("AppuserMapper.getSysAppUserCountByChannelCode",channelCode);
    }
}

