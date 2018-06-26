package com.fh.service.system.doll.impl;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.entity.system.Doll;
import com.fh.service.system.doll.DollManager;
import com.fh.util.PageData;
import com.fh.util.wwjUtil.MyUUID;
import com.fh.vo.system.DollVo;

/** 
 * 说明： 娃娃机处理类
 * 创建人：FH Q313596790
 * 创建时间：2017-10-27
 * @version
 */
@Service("dollService")
public class DollService implements DollManager {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void save(PageData pd)throws Exception{
		dao.save("DollMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void delete(PageData pd)throws Exception{
		dao.delete("DollMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void edit(PageData pd)throws Exception{
		dao.update("DollMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("DollMapper.dolllistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("DollMapper.listAllPage", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("DollMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	@Override
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("DollMapper.deleteAll", ArrayDATA_IDS);
	}
	
	/**批量发布
	 * @param ArrayRELEASE_STA
	 * @throws Exception
	 */
	@Override
	public void releaseAll(String[] ArrayRELEASE_STA)throws Exception{
		dao.delete("DollMapper.releaseAll", ArrayRELEASE_STA);
	}
	
	/**
	 * 通过SN号来查询是否存在娃娃机
	 * @param SN
	 * @return
	 * @throws Exception
	 */
	@Override
	public Doll getDollBySN(String SN) throws Exception {
		return (Doll)dao.findForObject("DollMapper.getDollBySN",SN);
	}

	/**
	 * 根据状态码获取娃娃机信息
	 * @param state
	 * @return
	 * @throws Exception
	 */
	@Override
	public Doll getDollByState(String state) throws Exception {
		return (Doll) dao.findForObject("DollMapper.getDollByState",state);
	}

	/**
	 * 根据id获取娃娃机房间信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public Doll getDollByID(String id) throws Exception {
		return (Doll) dao.findForObject("DollMapper.getDollByID",id);
	}

	/**
	 * 获取所有娃娃机的信息
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Doll> getAllDoll() throws Exception {
		return (List<Doll> ) dao.findForList("DollMapper.getAllDoll",null);
	}

	/**
	 * 通过SN注册娃娃机
	 * @param doll
	 * @return
	 * @throws Exception
	 */
	@Override
	public int regDollBySN(Doll doll) throws Exception {
		return (int)dao.save("DollMapper.regDollBySN",doll);
	}

	/**
	 * 通过网关表获取娃娃机在线的信息
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Doll> getDollBySessionOnLine() throws Exception {
		return (List<Doll>) dao.findForList("DollMapper.getDollBySessionOnLine",null);
	}

	@Override
	public Doll getDollByDollName(String dollName) throws Exception {
		return (Doll)dao.findForObject("DollMapper.getDollByDollName",dollName);
	}

	
	/** 娃娃机列表 （包含摄像信息）
	 * getDollList
	 * @return DollVo
	 * @throws Exception
	 */
	@Override
	public List<DollVo>  getDollVoList()throws Exception{
		return (List<DollVo>) dao.findForList("DollMapper.getDollVoList",null);
	}
	
	/** 娃娃机列表  分页（包含摄像信息）
	 * getDollList
	 * @return DollVo
	 * @throws Exception
	 */
	@Override
	public List<DollVo>  getDollPage(Page page)throws Exception{
		return (List<DollVo>) dao.findForList("DollMapper.getDolllistPage",page);
	}
	
	
	/** 娃娃机游戏统计
	 * @param page
	 * @throws Exception
	 */
	@Override
	public List<PageData> getDollCountlist(Page page)throws Exception{
		return (List<PageData>)dao.findForList("DollMapper.dollCountlistPage", page);
	}

	@Override
	public List<DollVo> getDollVoListByTag(String tag) throws Exception {
		return  (List<DollVo>) dao.findForList("DollMapper.getDollVoListByTag",tag);
	}

	@Override
	public List<DollVo> getDollTypeList(Integer typeid) throws Exception {
		return (List<DollVo>) dao.findForList("DollMapper.getDollTypeList",typeid);
	}
	
	/**
	 * 获取所有已经发布娃娃机列表
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<PageData> getAllDollListByRelease()throws Exception{
		return (List<PageData>) dao.findForList("DollMapper.getAllDollListByRelease",null);
	}
	
	/**
	 * 更新网关状态
	 * @param pd
	 * @throws Exception
	 */
	@Override
	public void updateDollStateByDollId(PageData pd)throws Exception{
		dao.update("DollMapper.updateDollStateByDollId", pd);
	}
}

