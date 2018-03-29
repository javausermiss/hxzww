package com.fh.service.system.betgame;

import com.fh.entity.Page;
import com.fh.entity.system.GuessDetailL;
import com.fh.util.PageData;
import com.iot.game.pooh.server.rpc.interfaces.bean.RpcCommandResult;
import net.sf.json.JSONObject;

import java.util.List;

public interface BetGameManager {

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    public List<PageData> list(Page page) throws Exception;

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    public List<PageData> listAll(PageData pd) throws Exception;

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    public PageData findById(PageData pd) throws Exception;

    /**
     * 批量删除
     *
     * @param ArrayDATA_IDS
     * @throws Exception
     */
    public void deleteAll(String[] ArrayDATA_IDS) throws Exception;

    /**
     * 增加竞猜记录
     *
     * @param guessDetailL
     * @return
     * @throws Exception
     */
    public int regGuessDetail(GuessDetailL guessDetailL) throws Exception;

    /**
     * 修改竞猜记录
     *
     * @param guessDetailL
     * @return
     * @throws Exception
     */
    public int updateGuessDetail(GuessDetailL guessDetailL) throws Exception;

    /**
     * 查询获奖人数
     *
     * @param playID
     * @return
     * @throws Exception
     */
    public List<GuessDetailL> getWin(Integer playID) throws Exception;

    /**
     * 查询未获奖人数
     *
     * @param guessDetailL
     * @return
     * @throws Exception
     */
    public List<GuessDetailL> getFailer(GuessDetailL guessDetailL) throws Exception;

    /**
     * 查询获奖人数
     *
     * @param guessDetailL
     * @return
     * @throws Exception
     */

    public List<GuessDetailL> getWinner(GuessDetailL guessDetailL) throws Exception;


    /**
     * 获取所有参与竞猜的人
     *
     * @param guessid
     * @return
     * @throws Exception
     */
    public List<GuessDetailL> getAllGuesser(String guessid) throws Exception;

    /**
     * 更新竞猜结果
     *
     * @param guessDetailL
     * @return
     * @throws Exception
     */
    public int updateGuessDetailGuessType(GuessDetailL guessDetailL) throws Exception;

    /**
     * 通过playId userId 查询
     *
     * @param guessDetailL
     * @return
     * @throws Exception
     */
    public GuessDetailL getGuessDetail(GuessDetailL guessDetailL) throws Exception;

    /**
     * 通过 userId 查询 最新10条竞猜记录
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public List<PageData> getGuessDetailTop10ByUserId(String userId) throws Exception;


    public JSONObject doBet(String userId, String dollId, int wager, String guessId, String guessKey,Integer multiple,Integer afterVoting) throws Exception;

    /**
     *娃娃机复位执行结算逻辑（第二种结算逻辑）
     * @param dollId
     * @param  gifinumber
     * @throws Exception
     */
    public RpcCommandResult doFree(String dollId, Integer gifinumber)throws Exception;


    public List<GuessDetailL> getWinByNum(GuessDetailL num)throws Exception;

    public List<GuessDetailL> getFailerByNum(GuessDetailL num)throws Exception;


}