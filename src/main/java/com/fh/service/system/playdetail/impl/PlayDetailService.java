package com.fh.service.system.playdetail.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.PlayDetail;
import com.fh.service.system.playdetail.PlayDetailManage;
import com.fh.service.system.sendgoods.SendGoodsManager;
import com.fh.util.PageData;

@Service("playDetailService")
public class PlayDetailService implements PlayDetailManage {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource(name = "sendGoodsService")
    private SendGoodsManager sendgoodsService;


    /**
     * 新增
     *
     * @param pd
     * @throws Exception
     */
    public void save(PageData pd) throws Exception {
        dao.save("PlayDetailMapper.save", pd);
    }

    /**
     * 修改
     *
     * @param pd
     * @throws Exception
     */
    public void edit(PageData pd) throws Exception {
        dao.update("PlayDetailMapper.edit", pd);
    }

    /**
     * 列表
     *
     * @param page
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PageData> list(Page page) throws Exception {
        return (List<PageData>) dao.findForList("PlayDetailMapper.datalistPage", page);
    }

    /**
     * 列表(全部)
     *
     * @param pd
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PageData> listAll(PageData pd) throws Exception {
        return (List<PageData>) dao.findForList("PlayDetailMapper.listAll", pd);
    }

    /**
     * 通过id获取数据
     *
     * @param pd
     * @throws Exception
     */
    public PageData findById(PageData pd) throws Exception {
        return (PageData) dao.findForObject("PlayDetailMapper.findById", pd);
    }


    @Override
    public int reg(PlayDetail playDetail) throws Exception {
        return (int) dao.save("PlayDetailMapper.reg", playDetail);
    }

    @Override
    public PlayDetail getPlayDetailByGuessID(PlayDetail playDetail) throws Exception {
        return (PlayDetail) dao.findForObject("PlayDetailMapper.getPlayDetailByGuessID", playDetail);
    }

    @Override
    public PlayDetail getPlayDetailLast(PlayDetail playDetail) throws Exception {
        return (PlayDetail) dao.findForObject("PlayDetailMapper.getPlayDetailLast", playDetail);
    }

    @Override
    public PlayDetail getPlayIdForPeople(String dollId) throws Exception {
        return (PlayDetail) dao.findForObject("PlayDetailMapper.getPlayIdForPeople", dollId);
    }

    @Override
    public PlayDetail getPlayDetailByRoomId(String roomId) throws Exception {
        return (PlayDetail) dao.findForObject("PlayDetailMapper.getPlayDetailByRoomId", roomId);
    }

    @Override
    public int updatePlayDetailStopFlag(PlayDetail playDetail) throws Exception {
        return (int) dao.update("PlayDetailMapper.updatePlayDetailStopFlag", playDetail);
    }

    @Override
    public int updatePlayDetailState(PlayDetail playDetail) throws Exception {
        return (int) dao.update("PlayDetailMapper.updatePlayDetailState", playDetail);
    }

    @Override
    public PlayDetail getPlayDetailForCamera(PlayDetail playDetail) throws Exception {
        return (PlayDetail) dao.findForObject("PlayDetailMapper.getPlayDetailForCamera", playDetail);
    }

    @Override
    public int updatePlayDetailForCamera(PlayDetail playDetail) throws Exception {
        return (int) dao.update("PlayDetailMapper.updatePlayDetailForCamera", playDetail);
    }

    @Override
    public List<PlayDetail> getPlaylist(String userId) throws Exception {
        return (List<PlayDetail>) dao.findForList("PlayDetailMapper.getPlaylist", userId);
    }

    @Override
    public List<PlayDetail> getDollCount(String userId) throws Exception {
        return (List<PlayDetail>) dao.findForList("PlayDetailMapper.getDollCount", userId);
    }

    @Override
    public List<PlayDetail> getPlayRecordPM() throws Exception {
        return (List<PlayDetail>) dao.findForList("PlayDetailMapper.getPlayRecordPM", null);
    }

    @Override
    public PlayDetail getPlayDetailByID(Integer id) throws Exception {
        return (PlayDetail) dao.findForObject("PlayDetailMapper.getPlayDetailById", id);
    }

    @Override
    public int updatePostState(PlayDetail playDetail) throws Exception {
        return (int) dao.update("PlayDetailMapper.updatePostState", playDetail);
    }

    @Override
    public int updatePostStateForCon(PlayDetail playDetail) throws Exception {
        return (int) dao.update("PlayDetailMapper.updatePostStateForCon", playDetail);
    }

    @Override
    public void doSendPost(PageData pageData, String playId, String sid) throws Exception {

        if (playId == null || playId.equals("")) {
            sendgoodsService.edit(pageData);
        } else {
            String[] pd1 = playId.split("\\,");
            for (int i = 0; i < pd1.length; i++) {
                PlayDetail playDetail = this.getPlayDetailByID(Integer.parseInt(pd1[i]));
                if (playDetail.getPOST_STATE().equals("1")) {
                    playDetail.setPOST_STATE("3");
                    playDetail.setSEND_ORDER_ID(sid);
                    this.updatePostStateForSendGood(playDetail);
                }
            }
            sendgoodsService.edit(pageData);

        }

    }

    @Override
    public int updatePostStateForSendGood(PlayDetail playDetail) throws Exception {
        return (int) dao.update("PlayDetailMapper.updatePostStateForSendGood", playDetail);
    }

    @Override
    public List<PlayDetail> getConversionToy() throws Exception {
        return (List<PlayDetail>) dao.update("PlayDetailMapper.getConversionToy", null);
    }
    
	/**获取娃娃机的游戏概率
	 * @param pd
	 * @throws Exception
	 */
	public PageData getDollGameProbability()throws Exception{
		return (PageData) dao.findForObject("PlayDetailMapper.getDollGameProbability", null);
	}

    @Override
    public List<PageData> getGameList(String dollId)throws Exception {
        return (List<PageData>) dao.findForList("PlayDetailMapper.getGameList", dollId);
    }
}
