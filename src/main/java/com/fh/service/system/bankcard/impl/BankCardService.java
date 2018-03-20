package com.fh.service.system.bankcard.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.BankCard;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.util.StringUtils;
import com.fh.service.system.bankcard.BankCardManager;

/** 
 * 说明： 银行卡信息
 * 创建人：FH Q313596790
 * 创建时间：2018-03-13
 * @version
 */
@Service("bankcardService")
public class BankCardService implements BankCardManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("BankCardMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("BankCardMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("BankCardMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("BankCardMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("BankCardMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("BankCardMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("BankCardMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public int regBankInf(BankCard bankCard) throws Exception {
		return (int)dao.save("BankCardMapper.regBankInf",bankCard);
	}

	@Override
	public BankCard getBankInfByUserId(String userId) throws Exception {
		return (BankCard)dao.findForObject("BankCardMapper.getBankInfByUserId",userId);
	}

	@Override
	public int updateBankInfByUserId(BankCard bankCard) throws Exception {
		return (int)dao.update("BankCardMapper.updateBankInfByUserId",bankCard);
	}
	
	
	/**
	 * APP通过用户ID修改用户的银行卡信息
	 * @param bankCard
	 * @return
	 * @throws Exception
	 */
	public BankCard getBankInfForAppByUserId(String userId)throws Exception{
		BankCard bankCard=(BankCard)dao.findForObject("BankCardMapper.getBankInfByUserId",userId);
		if(bankCard !=null && StringUtils.isNotEmpty(bankCard.getBANK_CARD_NO())){
			bankCard.setBANK_CARD_NO(bankCard.getBANK_CARD_NO().replaceAll("(\\d{4})\\d{8}(\\w{4})","$1*****$2"));
		}
		
		if(bankCard !=null && StringUtils.isNotEmpty(bankCard.getID_NUMBER())){
			bankCard.setID_NUMBER(bankCard.getID_NUMBER().replaceAll("(\\d{4})\\d{10}(\\w{4})","$1*****$2"));
		}
		return bankCard;
	}
}

