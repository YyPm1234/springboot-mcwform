package zone.mcw.mcwzone.springbootmcwform.service;

import zone.mcw.mcwzone.springbootmcwform.dto.FormDto;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Form;

import java.util.List;

/**
 * @author W4i create 2020/9/30 16:12
 */
public interface IFormService {
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

	List<FormDto> selectByEntityLike(String title, Integer state, String startTime, String EndTime, int page, int pageSize, Integer formId);

	Result doAddForm(Form form, String content);

	Result adminDeleteForm(Integer formId);

	Result adminChangeState(Form form);

	Result searchForm(String title, Integer state, String startTime, String endTime,int page,int pageSize, Integer formId);

	Result searchTitle(String title, Integer state, String startTime, String endTime,int page,int pageSize,Integer formId);

}
