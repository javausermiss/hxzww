package com.fh.service.system.userrecredpinfo.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.AppUser;
import com.fh.entity.system.UserRecRedPInfo;
import com.fh.service.system.appuser.AppuserManager;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.util.wwjUtil.RedisUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.userrecredpinfo.UserRecRedPinfoManager;

/** 
 * 说明： 用户领取红包记录
 * 创建人：FH Q313596790
 * 创建时间：2018-12-05
 * @version
 */
@Service("userrecredpinfoService")
@Log4j
public class UserRecRedPinfoService implements UserRecRedPinfoManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Resource(name = "appuserService")
	private AppuserManager appuserService;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("UserRecRedPinfoMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("UserRecRedPinfoMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("UserRecRedPinfoMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("UserRecRedPinfoMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("UserRecRedPinfoMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserRecRedPinfoMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("UserRecRedPinfoMapper.deleteAll", ArrayDATA_IDS);
	}


	@Override
	public int reg(UserRecRedPInfo ui) throws Exception {
		return (int)dao.save("UserRecRedPinfoMapper.reg",ui);
	}

	@Override
	public UserRecRedPInfo find(UserRecRedPInfo userRecRedPInfo) throws Exception {
		return (UserRecRedPInfo)dao.findForObject("UserRecRedPinfoMapper.find",userRecRedPInfo);
	}

	@Override
	public synchronized void doGetRedPackage(String num, String userId, String redUserId, String redId) throws Exception {

			int rn =  Integer.valueOf(num);
			if (rn == 1){
				RedisUtil.getRu().del("red:"+redUserId+":"+redId);
			}else {
				RedisUtil.getRu().set("red:"+redUserId+":"+redId,String.valueOf(rn - 1));
			}
			int redGold =  Integer.valueOf(RedisUtil.getRu().get("red:"+redUserId+":"+redId+":"+rn));
			RedisUtil.getRu().del("red:"+redUserId+":"+redId+":"+rn);
			AppUser appUser =  appuserService.getUserByID(userId);

			log.info("用户获取到的红包是:"+"red:"+redUserId+":"+redId+":"+rn);
			appUser.setBALANCE(String.valueOf(Integer.valueOf(appUser.getBALANCE()) + redGold));
			appuserService.updateAppUserBalanceById(appUser);
			//增加用户的领取红包记录
			UserRecRedPInfo userRecRedPInfo = new UserRecRedPInfo();
			userRecRedPInfo.setREDPACKAGE_ID(redId);
			userRecRedPInfo.setUSERRECREDPINFO_ID(MyUUID.getUUID32());
			userRecRedPInfo.setREDUSERID(redUserId);
			userRecRedPInfo.setUSERID(userId);
			userRecRedPInfo.setGOLD(String.valueOf(redGold));
			this.reg(userRecRedPInfo);
		try {
			Thread.sleep(50);
		} catch (InterruptedException i) {
			i.printStackTrace();
			log.info(i.getMessage());
		}

	}
}

