package com.fh.service.system.pointsdetail;

import java.util.List;
import com.fh.entity.Page;
import com.fh.entity.system.PointsDetail;
import com.fh.util.PageData;

/** 
 * 说明： 积分记录模块接口
 * 创建人：FH Q313596790
 * 创建时间：2018-08-08
 * @version
 */
public interface PointsDetailManager{

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


	public int regPointsDetail(PointsDetail pointsDetail)throws Exception;


	public List<PointsDetail> getPointsDetail(String userId)throws Exception;
	
}
