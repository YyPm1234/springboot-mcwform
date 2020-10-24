package zone.mcw.mcwzone.springbootmcwform.dao;

import zone.mcw.mcwzone.springbootmcwform.entity.User;

import java.util.List;

public interface UserMapper {

	/**
	 * 通过主键删除
	 */
	int deleteByPrimaryKey(Integer userId);

	/**
	 * 通过实体类插入
	 */
	int insertByEntity(User record);

	/**
	 * 通过实体类更新，若数据未空则更新未空
	 */
	int updateByPrimaryKey(User record);

	/**
	 * 通过实体类更新实体类（无值不更新）
	 */
	int updateEntityByEntity(User newRecord, User oldRecord);

	/**
	 * 通过实体类查询（条件带and）
	 */
	List<User> selectByEntity(User record);

	/**
	 * 通过实体类查询（条件带or）
	 */
	List<User> selectByEntityOr(User record);

}