package zone.mcw.mcwzone.springbootmcwform.service.impl;

import zone.mcw.mcwzone.springbootmcwform.dao.FormMapper;
import zone.mcw.mcwzone.springbootmcwform.dto.FormDto;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Form;
import zone.mcw.mcwzone.springbootmcwform.entity.Modular;
import zone.mcw.mcwzone.springbootmcwform.entity.Reply;
import zone.mcw.mcwzone.springbootmcwform.service.IFormService;
import zone.mcw.mcwzone.springbootmcwform.service.IModularService;
import zone.mcw.mcwzone.springbootmcwform.service.IReplyService;
import zone.mcw.mcwzone.springbootmcwform.utils.GetValue;
import zone.mcw.mcwzone.springbootmcwform.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对form表进行增删改查
 * 目前不允许对form中的title列进行update，只允许管理员改变state
 * search方法只允许管理员查询state=0的数据
 *
 * @author by W4i
 * @date 2020/10/1 14:33
 */
@Service
public class FormServiceImpl implements IFormService {
	private Logger logger = LoggerFactory.getLogger(FormServiceImpl.class);

	@Autowired FormMapper formMapper;
	@Autowired IModularService iModularService;
	@Autowired IReplyService iReplyService;
	GetValue getValue = new GetValue();
	Time time = new Time();

	@Override public int deleteByPrimaryKey(Integer formId) {
		return formMapper.deleteByPrimaryKey(formId);
	}

	@Override public int insertByEntity(Form record) {
		return formMapper.insertByEntity(record);
	}

	@Override public int updateByPrimaryKey(Form record) {
		return formMapper.updateByPrimaryKey(record);
	}

	@Override public int updateEntityByEntity(Form newRecord, Form oldRecord) {
		return formMapper.updateEntityByEntity(newRecord, oldRecord);
	}

	@Override public List<Form> selectByEntity(Form record) {
		return formMapper.selectByEntity(record);
	}

	@Override public List<Form> selectByEntityOr(Form record) {
		return formMapper.selectByEntityOr(record);
	}

	@Override
	public List<FormDto> selectByEntityLike(String title, Integer state, String startTime, String endTime, int page, int pageSize, Integer formId) {
		return formMapper.selectByEntityLike(title, state, startTime, endTime, (page - 1) * pageSize, pageSize, formId);
	}

	@Override public Result doAddForm(Form form, String content) {
		Result result = new Result();
		//modular不存在
		if (iModularService.selectByEntity(new Modular(form.getModuleId())).size() < 1) {
			result.setCode(-1);
			result.setMsg("板块不存在");
			return result;
		}
		try {
			//新增标题
			int userId = getValue.getJwt().getUserId();
			form.setCreateTime(time.getTime());
			form.setState(1);
			form.setUserId(userId);
			int formId = insertByEntity(form);
			//自动回复一楼
			Reply reply = new Reply();
			reply.setCreateTime(time.getTime());
			reply.setFloor(1);
			reply.setFormId(formId);
			reply.setUserId(userId);
			reply.setContent(content);
			reply.setState(1);
			reply.setParentId(0);
			iReplyService.insertByEntity(reply);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg(e.toString());
			logger.error(e.toString());
			result.setCode(-2);
		}
		return result;
	}

	@Override public Result adminDeleteForm(Integer formId) {
		Result result = new Result();
		if (deleteByPrimaryKey(formId) > 0 && iReplyService.deleteByFormId(formId) > 0) {
			result.setSuccess(true);
		}
		return result;
	}

	@Override public Result adminChangeState(Form form) {
		if (form.getState() == null) {
			form.setState(1);
		}
		Result result = new Result();
		Form form1 = new Form();
		form1.setState(form.getState());
		form.setState(null);
		if (updateEntityByEntity(form1, form) > 0) {
			result.setSuccess(true);
		}
		return result;
	}

	@Override public Result searchForm(String title, Integer state, String startTime, String endTime, int page, int pageSize, Integer formId) {
		List<Map<String, Object>> formList = new ArrayList<>();
		Result result1 = new Result();
		try {
			Result result = searchTitle(title, state, startTime, endTime, page, pageSize, formId);
			List<FormDto> titleList = (List<FormDto>) result.getData();
			for (FormDto form : titleList) {
				Map<String, Object> map = new HashMap<>();
				map.put("title", form);
				List<Reply> replyList = (List<Reply>) iReplyService.searchReply(form.getFormId(), null, null, null, null, 1, 1).getData();
				map.put("content", replyList.get(0));
				formList.add(map);
			}
			result1.setSuccess(true);
			result1.setData(formList);
		} catch (Exception e) {
			logger.error(e.toString());
			result1.setMsg(e.toString());
		}
		return result1;
	}

	@Override public Result searchTitle(String title, Integer state, String startTime, String endTime, int page, int pageSize, Integer formId) {
		//非管理员只能搜索正常状态的帖子
		if (getValue.getJwt().getAdmin() != 1) {
			state = 1;
		}
		//管理员查询全部帖子可以乱输state
		if (state != null) {
			if (state != 0 && state != 1) {
				state = null;
			}
		}
		if (StringUtils.hasText(title)) {
			title = "%" + title + "%";
		}
		Result result = new Result();
		try {
			List<FormDto> formList = selectByEntityLike(title, state, startTime, endTime, page, pageSize, formId);
			result.setSuccess(true);
			result.setData(formList);
		} catch (Exception e) {
			logger.error(e.toString());
			result.setMsg(e.toString());
		}
		return result;
	}
}
