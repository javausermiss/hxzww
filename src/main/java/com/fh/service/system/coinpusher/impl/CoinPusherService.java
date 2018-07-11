package com.fh.service.system.coinpusher.impl;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.CoinPusher;
import com.fh.service.system.coinpusher.CoinPusherManager;
import com.fh.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/** 
 * 说明： 推币机游戏记录
 * 创建人：FH Q313596790
 * 创建时间：2018-05-30
 * @version
 */
@Service("coinpusherService")
public class CoinPusherService implements CoinPusherManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void save(PageData pd)throws Exception{
		dao.save("CoinPusherMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delete(PageData pd)throws Exception{
		dao.delete("CoinPusherMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void edit(PageData pd)throws Exception{
		dao.update("CoinPusherMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CoinPusherMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CoinPusherMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CoinPusherMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CoinPusherMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public int reg(CoinPusher coinPusher)throws Exception {
		return (int)dao.save("CoinPusherMapper.reg",coinPusher);
	}

	@Override
	public CoinPusher getLatestRecord(CoinPusher coinPusher) throws Exception{
		return (CoinPusher) dao.findForObject("CoinPusherMapper.getLatestRecord",coinPusher);
	}

	@Override
	public int updateOutCoin(CoinPusher coinPusher) throws Exception{
		return (int)dao.update("CoinPusherMapper.updateOutCoin",coinPusher);
	}

	@Override
	public CoinPusher getLatestRecordForId(String roomId)throws Exception {
		return (CoinPusher) dao.findForObject("CoinPusherMapper.getLatestRecordForId",roomId);
	}

	@Override
	public List<CoinPusher> getCoinPusherRecondList(String userId)throws Exception {
		return (List<CoinPusher>) dao.findForList("CoinPusherMapper.getCoinPusherRecondList",userId);
	}

	@Override
	public CoinPusher getSumCoinOneDay(CoinPusher coinPusher)throws Exception {
		return (CoinPusher)dao.findForObject("CoinPusherMapper.getSumCoinOneDay",coinPusher);
	}
}

