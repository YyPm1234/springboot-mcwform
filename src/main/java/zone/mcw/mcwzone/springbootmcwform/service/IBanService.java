package zone.mcw.mcwzone.springbootmcwform.service;

import org.springframework.web.bind.annotation.RequestParam;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Ban;

import java.util.List;

/**
 * @author W4i create 2020/10/9 11:27
 */
public interface IBanService {
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

	Ban selectByUserIdOrderById(Integer userId);

	Result selectByPage(int page, int pageSize, String order, String desc);

	Result banCheckByUserId(int userId);

	Result banUser(int userId, String info, String banTime);

	Result unbanUser(int userId, String info);
}
