package zone.mcw.mcwzone.springbootmcwform.service;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Log;

import java.util.List;

/**
 * @author W4i create 2020/9/24 16:56
 */
public interface ILogService {

	/**
	 * 通过主键删除
	 */
	int deleteByPrimaryKey(Integer logId);

	/**
	 * 通过实体类插入
	 */
	int insertByEntity(Log record);

	/**
	 * 通过实体类更新，若数据未空则更新未空
	 */
	int updateByPrimaryKey(Log record);

	/**
	 * 通过实体类更新实体类（无值不更新）
	 */
	int updateEntityByEntity(Log newRecord, Log oldRecord);

	/**
	 * 通过实体类查询（条件带and）
	 */
	List<Log> selectByEntity(Log record);

	/**
	 * 通过实体类查询（条件带or）
	 */
	List<Log> selectByEntityOr(Log record);

	List<Log> selectByPage(int startPage, int pageSize, String order, String desc);

	Result logGetAll(int page, int pageSize, String order, String desc);

}
