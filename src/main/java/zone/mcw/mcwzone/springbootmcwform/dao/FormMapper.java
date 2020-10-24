package zone.mcw.mcwzone.springbootmcwform.dao;

import zone.mcw.mcwzone.springbootmcwform.dto.FormDto;
import zone.mcw.mcwzone.springbootmcwform.entity.Form;

import java.util.List;

public interface FormMapper {
	/**
	 * 通过主键删除
	 */
	int deleteByPrimaryKey(Integer formId);

	/**
	 * 通过实体类插入
	 */
	int insertByEntity(Form record);

	/**
	 * 通过实体类更新，若数据未空则更新未空
	 */
	int updateByPrimaryKey(Form record);

	/**
	 * 通过实体类更新实体类（无值不更新）
	 */
	int updateEntityByEntity(Form newRecord, Form oldRecord);

	/**
	 * 通过实体类查询（条件带and）
	 */
	List<Form> selectByEntity(Form record);

	/**
	 * 通过实体类查询（条件带or）
	 */
	List<Form> selectByEntityOr(Form record);

	List<FormDto> selectByEntityLike(String title, Integer state, String startTime, String endTime, int start, int pageSize, Integer formId);
}