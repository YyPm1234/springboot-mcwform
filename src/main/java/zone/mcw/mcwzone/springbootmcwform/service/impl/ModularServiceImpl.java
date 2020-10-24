package zone.mcw.mcwzone.springbootmcwform.service.impl;

import zone.mcw.mcwzone.springbootmcwform.dao.ModularMapper;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Modular;
import zone.mcw.mcwzone.springbootmcwform.service.IModularService;
import zone.mcw.mcwzone.springbootmcwform.utils.GetValue;
import zone.mcw.mcwzone.springbootmcwform.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author by W4i
 * @date 2020/10/1 14:35
 */
@Service
public class ModularServiceImpl implements IModularService {
	private Logger logger = LoggerFactory.getLogger(ModularServiceImpl.class);

	@Autowired ModularMapper modularMapper;
	Time time = new Time();
	GetValue getValue = new GetValue();

	@Override public int deleteByPrimaryKey(Integer modularId) {
		return modularMapper.deleteByPrimaryKey(modularId);
	}

	@Override public int insertByEntity(Modular record) {
		return modularMapper.insertByEntity(record);
	}

	@Override public int updateByPrimaryKey(Modular record) {
		return modularMapper.updateByPrimaryKey(record);
	}

	@Override public int updateEntityByEntity(Modular newRecord, Modular oldRecord) {
		return modularMapper.updateEntityByEntity(newRecord, oldRecord);
	}

	@Override public List<Modular> selectByEntity(Modular record) {
		return modularMapper.selectByEntity(record);
	}

	@Override public List<Modular> selectByEntityOr(Modular record) {
		return modularMapper.selectByEntityOr(record);
	}

	@Override
	public Result add(String modularName) {
		Result result = new Result();
		Modular modular = new Modular();
		modular.setModularName(modularName);
		if (modularMapper.selectByEntity(modular).size() != 0) {
			result.setMsg("分区已存在");
			return result;
		}
		try {
			modular.setCreateTime(time.getTime());
			modular.setState(1);
			insertByEntity(modular);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg(e.toString());
			logger.error(e.toString());

		}
		return result;
	}

	@Override
	public Result changeState(int modularId, int state) {
		Result result = new Result();
		if (state != 0 && state != 1) {
			result.setMsg("参数错误");
			return result;
		}
		Modular modular = new Modular();
		modular.setState(state);
		int a = updateEntityByEntity(modular, new Modular(modularId));
		if (a > 0) {
			result.setSuccess(true);
		}
		return result;
	}

	@Override public Result getModular(String modularName, Integer state) {
		if (getValue.getJwt().getAdmin() != 1) {
			state = 1;
		}
		//管理员查询全部帖子可以乱输state
		if (state != null) {
			if (state != 0 && state != 1) {
				state = null;
			}
		}
		Result result = new Result();
		Modular modular = new Modular();
		if (!StringUtils.hasText(modularName)) {
			modularName = null;
		}
		modular.setModularName(modularName);
		modular.setState(state);
		try {
			result.setData(selectByEntityOr(modular));
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		return result;
	}
}
