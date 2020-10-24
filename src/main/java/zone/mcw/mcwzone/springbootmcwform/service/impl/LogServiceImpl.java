package zone.mcw.mcwzone.springbootmcwform.service.impl;

import zone.mcw.mcwzone.springbootmcwform.dao.LogMapper;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Log;
import zone.mcw.mcwzone.springbootmcwform.service.ILogService;
import zone.mcw.mcwzone.springbootmcwform.utils.EncryptionUtils;
import zone.mcw.mcwzone.springbootmcwform.utils.GetValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 日志service
 *
 * @author W4i create 2020/9/24 16:56
 */
@Service
public class LogServiceImpl implements ILogService {
	@Autowired LogMapper logMapper;
	EncryptionUtils encryptionUtils = new EncryptionUtils();
	GetValue getValue = new GetValue();

	@Override
	public int deleteByPrimaryKey(Integer logId) {
		return logMapper.deleteByPrimaryKey(logId);
	}

	@Override
	public int insertByEntity(Log record) {
		return logMapper.insertByEntity(record);
	}

	@Override
	public List<Log> selectByEntity(Log record) {
		return logMapper.selectByEntity(record);
	}

	@Override
	public List<Log> selectByEntityOr(Log record) {
		return logMapper.selectByEntityOr(record);
	}

	@Override
	public int updateByPrimaryKey(Log record) {
		return logMapper.updateByPrimaryKey(record);
	}

	@Override
	public int updateEntityByEntity(Log newRecord, Log oldRecord) {
		return updateEntityByEntity(newRecord, oldRecord);
	}

	@Override public List<Log> selectByPage(int startPage, int pageSize, String order, String desc) {
		return logMapper.selectByPage(startPage, pageSize, order, desc);
	}

	@Override public Result logGetAll(int page, int pageSize, String order, String desc) {
		Result result = new Result();
		//判断order是否合法
		List<String> list = getValue.getEntity(new Log());
		boolean isOrder = false;
		for (String s : list) {
			if (s.equals(getValue.toHump(order))) {
				isOrder = true;
			}
		}
		if (!StringUtils.hasText(order) || !isOrder) {
			order = "log_id";
		}
		//分页查询
		int start = (page - 1) * pageSize;
		List<Log> logList = selectByPage(start, pageSize, order, desc);
		for (Log log1 : logList) {
			try {
				log1.setArgs(encryptionUtils.decode(log1.getArgs()));
			} catch (Exception e) {
			}
		}
		result.setSuccess(true);
		result.setData(logList);
		return result;
	}

}
