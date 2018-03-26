package com.fh.service.system.afterVoting.impl;

import com.fh.dao.DaoSupport;
import com.fh.entity.system.AfterVoting;
import com.fh.service.system.afterVoting.AfterVotingManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("afterVotingService")
public class AfterVotingService implements AfterVotingManager {

    @Resource(name = "daoSupport")
    private DaoSupport dao;


    @Override
    public int regAfterVoting(AfterVoting afterVoting)throws Exception{
        return (int)dao.save("AccountLogMapper.regAfterVoting",afterVoting);
    }

    @Override
    public List<AfterVoting> getListAfterVoting(String dollId)throws Exception {
        return (List<AfterVoting>) dao.save("AccountLogMapper.regAfterVoting",dollId);
    }


    @Override
    public AfterVoting getAfterVoting(AfterVoting afterVoting)throws Exception {

        return (AfterVoting) dao.save("AccountLogMapper.regAfterVoting",afterVoting);
    }

    @Override
    public int updateAfterVoting_Num(AfterVoting afterVoting)throws Exception{
        return (int)dao.save("AccountLogMapper.regAfterVoting",afterVoting);
    }
}
