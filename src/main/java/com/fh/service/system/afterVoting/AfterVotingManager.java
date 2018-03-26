package com.fh.service.system.afterVoting;

import com.fh.entity.system.AfterVoting;

import java.util.List;

/**
 * 追投service
 */
public interface AfterVotingManager {
    /**
     * 增加记录
     * @param afterVoting
     * @return
     * @throws Exception
     */
    public int regAfterVoting(AfterVoting afterVoting)throws Exception;


    /**
     * 查询出符合条件的追投对象集合
     * @param dollId
     * @return
     * @throws Exception
     */
    public List<AfterVoting> getListAfterVoting(String dollId)throws Exception;


    /**
     * 查询出用户的追投记录
     * @param afterVoting
     * @return
     * @throws Exception
     */
    public AfterVoting getAfterVoting(AfterVoting afterVoting)throws Exception;

    /**
     * 更新追投期数
     * @param afterVoting
     * @return
     * @throws Exception
     */
    public int updateAfterVoting_Num(AfterVoting afterVoting)throws Exception;

}
