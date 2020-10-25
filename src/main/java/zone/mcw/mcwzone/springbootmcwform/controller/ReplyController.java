package zone.mcw.mcwzone.springbootmcwform.controller;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Reply;
import zone.mcw.mcwzone.springbootmcwform.service.IReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author W4i create 2020/10/5 14:07
 */
@Controller
@RequestMapping("reply")
public class ReplyController {
	@Autowired IReplyService iReplyService;

	/**
	 * 回帖
	 *
	 * @param reply
	 * @return
	 */
	@ResponseBody
	@PostMapping("doReply")
	public Result doReply(Reply reply) {
		return iReplyService.doReply(reply);
	}

	/**
	 * 删除回复
	 *
	 * @param replyId
	 * @return
	 */
	@ResponseBody
	@PostMapping("adminDeleteReply")
	public Result adminDeleteReply(Integer replyId) {
		return iReplyService.adminDeleteReply(replyId);
	}

	/**
	 * 盖面回复状态
	 *
	 * @param replyId
	 * @param state
	 * @return
	 */
	@ResponseBody
	@PostMapping("adminChangeState")
	public Result adminChangeState(int replyId, Integer state) {
		return iReplyService.adminChangeState(replyId, state);
	}

	/**
	 * 搜索回复
	 *
	 * @param formId
	 * @param content
	 * @param state
	 * @param startTime
	 * @param endTime
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@PostMapping("searchReply")
	public Result searchReply(Integer formId, String content, Integer state, String startTime, String endTime,
							  @RequestParam(defaultValue = "1") int page,
							  @RequestParam(defaultValue = "10") int pageSize) {
		return iReplyService.searchReply(formId, content, state, startTime, endTime, page, pageSize);
	}

	/**
	 * 上传图片/文件
	 *
	 * @param file
	 * @return
	 */
	@ResponseBody
	@PostMapping("doUpdatePic")
	public Result doUpdatePic(MultipartFile file) {
		return iReplyService.doUpdatePic(file);
	}
}
