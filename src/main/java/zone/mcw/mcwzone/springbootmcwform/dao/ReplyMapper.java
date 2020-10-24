package zone.mcw.mcwzone.springbootmcwform.dao;

import zone.mcw.mcwzone.springbootmcwform.dto.ReplyDto;
import zone.mcw.mcwzone.springbootmcwform.entity.Reply;

import java.util.List;

public interface ReplyMapper {
	/**
	 * 通过主键删除
	 */
	int deleteByPrimaryKey(Integer replyId);

	/**
	 * 通过实体类插入
	 */
	int insertByEntity(Reply record);

	/**
	 * 通过实体类更新，若数据未空则更新未空
	 */
	int updateByPrimaryKey(Reply record);

	/**
	 * 通过实体类更新实体类（无值不更新）
	 */
	int updateEntityByEntity(Reply newRecord, Reply oldRecord);

	/**
	 * 通过实体类查询（条件带and）
	 */
	List<Reply> selectByEntity(Reply record);

	/**
	 * 通过实体类查询（条件带or）
	 */
	List<Reply> selectByEntityOr(Reply record);

	List<ReplyDto> selectByEntityLike(Integer formId, String content, Integer state, String startTime, String endTime, int start, int pageSize);

	int deleteByFormId(Integer formId);
}