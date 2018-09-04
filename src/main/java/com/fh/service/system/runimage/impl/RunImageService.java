package com.fh.service.system.runimage.impl;

import java.util.List;
import javax.annotation.Resource;

import com.fh.entity.system.RunImage;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.system.runimage.RunImageManager;

/** 
 * 说明： 首页滚动图
 * 创建人：FH Q313596790
 * 创建时间：2018-01-09
 * @version
 */
@Service("runimageService")
public class RunImageService implements RunImageManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("RunImageMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("RunImageMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("RunImageMapper.edit", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("RunImageMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("RunImageMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("RunImageMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("RunImageMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public RunImage getRunImageById(String id) throws Exception {
		return (RunImage)dao.findForObject("RunImageMapper.getRunImageById",id);
	}

	@Override
	public List<RunImage> getRunImageList() throws Exception {
		return (List<RunImage>)dao.findForList("RunImageMapper.getRunImageList",null);
	}

	@Override
	public List<RunImage> getRunImageListNew(RunImage runImage)throws Exception {
		return (List<RunImage>)dao.findForList("RunImageMapper.getRunImageListNew",runImage);
	}
}

