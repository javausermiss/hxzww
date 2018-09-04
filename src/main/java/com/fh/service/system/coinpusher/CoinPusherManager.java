package com.fh.service.system.coinpusher;

import com.fh.entity.Page;
import com.fh.entity.system.CoinPusher;
import com.fh.util.PageData;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcCommandResult;

import java.util.List;

/** 
 * 说明： 推币机游戏记录接口
 * 创建人：FH Q313596790
 * 创建时间：2018-05-30
 * @version
 */
public interface CoinPusherManager {

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
	public List<PageData> listAll(PageData pd)throws Exception;
	
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

	/**
	 * 增加游戏记录
	 * @param coinPusher
	 * @return
	 * @throws Exception
	 */
	public int reg(CoinPusher coinPusher)throws Exception;

	/**
	 * 查询最新的一期游戏记录
	 * @param coinPusher
	 * @return
	 * @throws Exception
	 */
	public CoinPusher getLatestRecord(CoinPusher coinPusher)throws Exception;

	/**
	 * 修改出币数
	 * @param coinPusher
	 * @return
	 * @throws Exception
	 */
	public int updateOutCoin(CoinPusher coinPusher)throws Exception;

	/**
	 * 查询最新的一条已完成的记录
	 * @param roomId
	 * @return
	 * @throws Exception
	 */
	public CoinPusher getLatestRecordForId(String roomId)throws Exception;

	/**
	 * 查询出该用户的投币记录
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public List<CoinPusher> getCoinPusherRecondList(String userId)throws Exception;

	/**
	 * 查询该用户当日已经获得硬币数
	 * @param coinPusher
	 * @return
	 * @throws Exception
	 */
	public CoinPusher getSumCoinOneDay(CoinPusher coinPusher) throws Exception;


	/**
	 * RPC推币机开始游戏
	 * @param roomId
	 * @param userId
	 * @param bat
	 * @return
	 * @throws Exception
	 */

	public RpcCommandResult doPusherGame(String roomId, String userId, Integer bat)throws Exception;

}

