package zone.mcw.mcwzone.springbootmcwform.service.impl;

import zone.mcw.mcwzone.springbootmcwform.dao.ReplyMapper;
import zone.mcw.mcwzone.springbootmcwform.dto.ReplyDto;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Form;
import zone.mcw.mcwzone.springbootmcwform.entity.Reply;
import zone.mcw.mcwzone.springbootmcwform.service.IFileService;
import zone.mcw.mcwzone.springbootmcwform.service.IFormService;
import zone.mcw.mcwzone.springbootmcwform.service.IReplyService;
import zone.mcw.mcwzone.springbootmcwform.utils.GetValue;
import zone.mcw.mcwzone.springbootmcwform.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author by W4i
 * @date 2020/10/1 14:36
 */
@Service
public class ReplyServiceImpl implements IReplyService {
	private Logger logger = LoggerFactory.getLogger(ReplyServiceImpl.class);
	@Autowired ReplyMapper replyMapper;
	@Autowired IFormService iFormService;
	@Autowired IFileService iFileService;

	Time time = new Time();
	GetValue getValue = new GetValue();

	@Override public int deleteByPrimaryKey(Integer replyId) {
		return replyMapper.deleteByPrimaryKey(replyId);
	}

	@Override public int insertByEntity(Reply record) {
		return replyMapper.insertByEntity(record);
	}

	@Override public int updateByPrimaryKey(Reply record) {
		return replyMapper.updateByPrimaryKey(record);
	}

	@Override public int updateEntityByEntity(Reply newRecord, Reply oldRecord) {
		return replyMapper.updateEntityByEntity(newRecord, oldRecord);
	}

	@Override public List<Reply> selectByEntity(Reply record) {
		return replyMapper.selectByEntity(record);
	}

	@Override public List<Reply> selectByEntityOr(Reply record) {
		return replyMapper.selectByEntityOr(record);
	}

	@Override
	public List<ReplyDto> selectByEntityLike(Integer formId, String content, Integer state, String startTime, String endTime, int page, int pageSize) {
		return replyMapper.selectByEntityLike(formId, content, state, startTime, endTime, (page - 1) * pageSize, pageSize);
	}

	@Override public Result doReply(Reply reply) {
		Result result = new Result();
		try {
			//判断form是否存在
			if (iFormService.selectByEntity(new Form(reply.getFormId())).size() < 1) {
				result.setMsg("帖子不存在");
				result.setCode(-1);
				return result;
			}
			reply.setCreateTime(time.getTime());
			reply.setState(1);
			reply.setUserId(getValue.getJwt().getUserId());
			Reply reply1 = new Reply();
			reply1.setFormId(reply.getFormId());
			reply.setFloor(selectByEntity(reply1).size() + 1);
			insertByEntity(reply);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg(e.toString());
			logger.error(e.toString());
			result.setCode(-2);
		}
		return result;
	}

	@Override public Result adminDeleteReply(Integer replyId) {
		Result result = new Result();
		if (deleteByPrimaryKey(replyId) > 0) {
			result.setSuccess(true);
		}
		return result;
	}

	@Override public Result adminChangeState(int replyId, Integer state) {
		Reply newReply = new Reply();
		Reply oldReply = new Reply();
		newReply.setState(state);
		oldReply.setReplyId(replyId);
		Result result = new Result();
		if (updateEntityByEntity(newReply, oldReply) > 0) {
			result.setSuccess(true);
		}
		return result;
	}

	@Override public Result searchReply(Integer formId, String content, Integer state, String startTime, String endTime, int page, int pageSize) {
		if (getValue.getJwt().getAdmin() != 1) {
			state = 1;
		}
		//管理员查询全部帖子可以乱输state
		if (state != null) {
			if (state != 0 && state != 1) {
				state = null;
			}
		}
		if (StringUtils.hasText(content)) {
			content = "%" + content + "%";
		}
		Result result = new Result();
		try {
			List<ReplyDto> replyList = selectByEntityLike(formId, content, state, startTime, endTime, page, pageSize);
			result.setData(replyList);
			result.setSuccess(true);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return result;
	}

	@Override public Result doUpdatePic(MultipartFile file) {
		Result result=iFileService.fileUpload(file,"PIC");
		if (!result.isSuccess()){
			return result;
		}else {
			String fileName= (String) result.getData();
			Result result1=new Result();
			result1.setSuccess(true);
			result1.setData(fileName);
			return result1;
		}
	}

	@Override public int deleteByFormId(Integer formId) {
		return replyMapper.deleteByFormId(formId);
	}
}
