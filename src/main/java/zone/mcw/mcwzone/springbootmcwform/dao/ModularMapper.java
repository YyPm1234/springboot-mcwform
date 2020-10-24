package zone.mcw.mcwzone.springbootmcwform.dao;

import zone.mcw.mcwzone.springbootmcwform.entity.Modular;

import java.util.List;

public interface ModularMapper {
	/**
	 * 通过主键删除
	 */
	int deleteByPrimaryKey(Integer modularId);

	/**
	 * 通过实体类插入
	 */
	int insertByEntity(Modular record);

	/**
	 * 通过实体类更新，若数据未空则更新未空
	 */
	int updateByPrimaryKey(Modular record);

	/**
	 * 通过实体类更新实体类（无值不更新）
	 */
	int updateEntityByEntity(Modular newRecord, Modular oldRecord);

	/**
	 * 通过实体类查询（条件带and）
	 */
	List<Modular> selectByEntity(Modular record);

	/**
	 * 通过实体类查询（条件带or）
	 */
	List<Modular> selectByEntityOr(Modular record);

}