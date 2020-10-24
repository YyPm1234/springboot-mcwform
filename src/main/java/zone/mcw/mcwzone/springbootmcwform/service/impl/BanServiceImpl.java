package zone.mcw.mcwzone.springbootmcwform.service.impl;

import zone.mcw.mcwzone.springbootmcwform.dao.BanMapper;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Ban;
import zone.mcw.mcwzone.springbootmcwform.redis.RedisFunction;
import zone.mcw.mcwzone.springbootmcwform.service.IBanService;
import zone.mcw.mcwzone.springbootmcwform.utils.EncryptionUtils;
import zone.mcw.mcwzone.springbootmcwform.utils.GetValue;
import zone.mcw.mcwzone.springbootmcwform.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author W4i create 2020/10/9 11:27
 */
@Service
public class BanServiceImpl implements IBanService {
	private Logger logger = LoggerFactory.getLogger(BanServiceImpl.class);
	@Autowired BanMapper banMapper;
	@Resource(name = "redisTemplate10")
	private StringRedisTemplate db10;
	RedisFunction redisFunction = new RedisFunction();
	GetValue getValue = new GetValue();
	Time time = new Time();

	@Override public int deleteByPrimaryKey(Integer banId) {
		return banMapper.deleteByPrimaryKey(banId);
	}

	@Override public int insertByEntity(Ban record) {
		return banMapper.insertByEntity(record);
	}

	@Override public int updateByPrimaryKey(Ban record) {
		return banMapper.updateByPrimaryKey(record);
	}

	@Override public int updateEntityByEntity(Ban newRecord, Ban oldRecord) {
		return banMapper.updateEntityByEntity(newRecord, oldRecord);
	}

	@Override public List<Ban> selectByEntity(Ban record) {
		return banMapper.selectByEntity(record);
	}

	@Override public List<Ban> selectByEntityOr(Ban record) {
		return banMapper.selectByEntityOr(record);
	}

	@Override public Ban selectByUserIdOrderById(Integer userId) {
		return banMapper.selectByUserIdOrderById(userId);
	}

	@Override public Result selectByPage(int page, int pageSize, String order, String desc) {
		Result result = new Result();
		List<String> list = getValue.getEntity(new Ban());
		boolean isOrder = false;
		for (String s : list) {
			if (s.equals(getValue.toHump(order))) {
				isOrder = true;
			}
		}
		if (!isOrder) {
			order = "ban_id";
		}
		try {
			result.setData(banMapper.selectByPage((page - 1) * pageSize, pageSize, order, desc));
			result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg(e.toString());
			logger.error(e.toString());
		}
		return result;
	}

	@Override public Result banCheckByUserId(int userId) {
		Result result = new Result();
		String code = redisFunction.BanId(userId);
		String token = redisFunction.getTokenByKey(code, db10);
		if (StringUtils.hasText(token)) {
			result.setSuccess(true);
			result.setData(token);
		}
		return result;
	}

	@Override
	public Result banUser(int userId, String info, String banTime) {
		String key = redisFunction.BanId(userId);
		Result result = new Result();
		if (time.timeCompareWithNow(banTime)) {
			result.setMsg("时间不对");
			return result;
		}
		String token = redisFunction.getTokenByKey(key, db10);
		if (StringUtils.hasText(token)) {
			result.setMsg("用户已被封禁，不要重复操作");
			return result;
		}
		try {
			redisFunction.productToken(key, banTime, time.timeSubtraction(banTime, time.getTime()), db10);
			redisFunction.deleteTokenByKey(db10, redisFunction.UserId(userId));
			result.setSuccess(true);
			Ban ban = new Ban();
			//写入ban表做记录
			ban.setUserId(userId);
			ban.setAdminId(getValue.getJwt().getUserId());
			ban.setInfo(info);
			ban.setUnbanTime(banTime);
			insertByEntity(ban);
		} catch (Exception e) {
			result.setMsg(e.toString());
			logger.error(e.toString());
		}
		return result;
	}

	@Override
	public Result unbanUser(int userId, String info) {
		String key = redisFunction.BanId(userId);
		String token = redisFunction.getTokenByKey(key, db10);
		Result result = new Result();
		if (StringUtils.hasText(token)) {
			try {
				redisFunction.deleteTokenByKey(db10, key);
				result.setSuccess(true);
			} catch (Exception e) {
				result.setMsg(e.toString());
				logger.error(e.toString());
			}
		} else {
			result.setMsg("用户未被封禁");
		}
		return result;
	}
}
