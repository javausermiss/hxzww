package com.fh.service.system.appuser;

import java.util.List;

import com.fh.entity.Page;
import com.fh.entity.system.AppUser;
import com.fh.util.Const;
import com.fh.util.PageData;


/**
 * 会员接口类
 *
 * @author fh313596790qq(青苔)
 * 修改时间：2015.11.2
 */
public interface AppuserManager {

    /**
     * 列出某角色下的所有会员
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> listAllAppuserByRorlid(PageData pd) throws Exception;

    /**
     * 会员列表
     *
     * @param page
     * @return
     * @throws Exception
     */
    public List<PageData> listPdPageUser(Page page) throws Exception;

    /**
     * 通过用户名获取数据
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public PageData findByUsername(PageData pd) throws Exception;

    /**
     * 通过邮箱获取数据
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public PageData findByEmail(PageData pd) throws Exception;

    /**
     * 通过编号获取数据
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public PageData findByNumber(PageData pd) throws Exception;

    /**
     * 保存用户
     *
     * @param pd
     * @throws Exception
     */
    public void saveU(PageData pd) throws Exception;

    /**
     * 删除用户
     *
     * @param pd
     * @throws Exception
     */
    public void deleteU(PageData pd) throws Exception;

    /**
     * 修改用户
     *
     * @param pd
     * @throws Exception
     */
    public void editU(PageData pd) throws Exception;

    /**
     * 通过id获取数据
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public PageData findByUiId(PageData pd) throws Exception;

    /**
     * 全部会员
     *
     * @param pd
     * @return
     * @throws Exception
     */
    public List<PageData> listAllUser(PageData pd) throws Exception;

    /**
     * 批量删除用户
     *
     * @param USER_IDS
     * @throws Exception
     */
    public void deleteAllU(String[] USER_IDS) throws Exception;

    /**
     * 获取总数
     *
     * @param value
     * @throws Exception
     */
    public PageData getAppUserCount(String value) throws Exception;

    /**
     * 通过手机号码注册用户信息
     *
     * @param phone
     * @return
     * @throws Exception
     */
    public int reg(String phone,String url) throws Exception;

    /**
     * 通过手机号码查询用户信息
     *
     * @param phone
     * @return
     * @throws Exception
     */
    public AppUser getUserByPhone(String phone) throws Exception;

    /**
     * 通过ID获取用户信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    public AppUser getUserByID(String id) throws Exception;

    /**
     * 更改用户头像
     *
     * @param appUser
     * @return
     * @throws Exception
     */
    public int updateAppUserImage(AppUser appUser) throws Exception;

    /**
     * 更改用户昵称
     *
     * @param appUser
     * @return
     * @throws Exception
     */
    public int updateAppUserName(AppUser appUser) throws Exception;

    /**
     * 通过用户名查询用户信息
     *
     * @param name
     * @return
     * @throws Exception
     */
    public AppUser getAppUserByUserName(String name) throws Exception;


    /**
     * 通过昵称来查询用户信息
     *
     * @param nickName
     * @return
     * @throws Exception
     */
    public AppUser getAppUserByNickName(String nickName) throws Exception;


    public List<AppUser> getAppUserByNickNameList(String nickName) throws Exception;

    /**
     * 获取用户更新的的账户余额
     *
     * @param appUser
     * @return
     * @throws Exception
     */
    public int updateAppUserBalanceByPhone(AppUser appUser) throws Exception;

    /**
     * rank榜单
     *
     * @return
     * @throws Exception
     */
    public List<AppUser> rankList() throws Exception;

    /**
     * 更新用户的抓娃娃数量
     *
     * @return
     * @throws Exception
     */
    public int updateAppUserDollTotalById(AppUser appUser) throws Exception;

    /**
     * 根据ID修改余额
     *
     * @param appUser
     * @return
     * @throws Exception
     */
    public int updateAppUserBalanceById(AppUser appUser) throws Exception;

    /**
     * 更改用户昵称
     *
     * @param appUser
     * @return
     * @throws Exception
     */
    public int updateAppUsernickName(AppUser appUser) throws Exception;

    public int updateAppUserCnee(AppUser appUser) throws Exception;

    /***********************************************************************************************************/
    /**
     * 注册腾讯用户
     * @param appUser
     * @return
     * @throws Exception
     */
    public int regwx(AppUser appUser) throws Exception;

    /**
     * 二次登录修改用户
     * @param appUser
     * @return
     * @throws Exception
     */
    public int updateTencentUser(AppUser appUser)throws Exception;



    public AppUser testuser(AppUser appUser) throws Exception;

    /**
     * 查询所有的用户
     * @return
     * @throws Exception
     */
    public List<AppUser> getAppUserList()throws Exception;

    /**
     * 修改余额和标签
     * @param appUser
     * @return
     * @throws Exception
     */
    public int  updateAppUserSB(AppUser appUser)throws Exception;


    /**
     * 修改用户的签到标签
     * @param appUser
     * @return
     * @throws Exception
     */
    public int  updateAppUserSign(AppUser appUser)throws Exception;

    /**
     * 修改账户金币
     * @param userId appUser.userId 用户对象主键
     * @param operNum 操作的数量
     * @param operType A:add,加; S:sub,减
     * @param operMenu 操作的枚举
     * @return
     */
    public int updateUserBalance(String userId,int operNum,String operType,Const.PlayMentCostType operMenu) throws Exception;

    /**
     * 查询用户个人所在排名
     * @param userId
     * @return
     * @throws Exception
     */
    public AppUser getAppUserRanklist (String userId)throws Exception;
    
	
	
	/**用户游戏统计列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listUserGames(Page page)throws Exception;
	
	
	/**
	 * 查看用户的充值总记录
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public PageData getAppUesrRechargeToTal(String userId)throws Exception;
	

    /**
     * 查询渠道的总注册用户数量
     * @param channelCode
     * @return
     * @throws Exception
     */
    public Integer getSysAppUserCountByChannelCode(String channelCode)throws Exception;
}

