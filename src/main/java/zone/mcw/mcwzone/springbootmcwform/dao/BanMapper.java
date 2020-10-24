package zone.mcw.mcwzone.springbootmcwform.dao;

import zone.mcw.mcwzone.springbootmcwform.entity.Ban;

import java.util.List;

public interface BanMapper {
	/**
	 * 通过主键删除
	 */
	int deleteByPrimaryKey(Integer banId);

	/**
	 * 通过实体类插入
	 */
	int insertByEntity(Ban record);

	/**
	 * 通过实体类更新，若数据未空则更新未空
	 */
	int updateByPrimaryKey(Ban record);

	/**
	 * 通过实体类更新实体类（无值不更新）
	 */
	int updateEntityByEntity(Ban newRecord, Ban oldRecord);

	/**
	 * 通过实体类查询（条件带and）
	 */
	List<Ban> selectByEntity(Ban record);

	/**
	 * 通过实体类查询（条件带or）
	 */
	List<Ban> selectByEntityOr(Ban record);

	Ban selectByUserIdOrderById(Integer banId);

	/**
	 * 分页查询
	 *
	 * @param startPage
	 * @param pageSize
	 * @param order
	 * @param desc
	 * @return
	 */
	List<Ban> selectByPage(int startPage, int pageSize, String order, String desc);

}