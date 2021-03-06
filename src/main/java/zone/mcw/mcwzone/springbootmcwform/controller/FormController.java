package zone.mcw.mcwzone.springbootmcwform.controller;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Form;
import zone.mcw.mcwzone.springbootmcwform.service.IFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author W4i create 2020/10/9 14:19
 */
@Controller
@RequestMapping("/form")
public class FormController {
	@Autowired IFormService iFormService;

	/**
	 * 发帖
	 *
	 * @param form
	 * @param content
	 * @return
	 */
	@PostMapping("/doAddForm")
	@ResponseBody
	public Result doAddForm(Form form, String content) {
		return iFormService.doAddForm(form, content);
	}

	/**
	 * 删除帖子
	 *
	 * @param formId
	 * @return
	 */
	@PostMapping("/adminDeleteForm")
	@ResponseBody
	public Result adminDeleteForm(Integer formId) {
		return iFormService.adminDeleteForm(formId);
	}

	/**
	 * 修改帖子状态
	 *
	 * @param form
	 * @return
	 */
	@PostMapping("/adminChangeState")
	@ResponseBody
	public Result adminChangeState(Form form) {
		return iFormService.adminChangeState(form);
	}

	/**
	 * 搜索帖子
	 *
	 * @param title
	 * @param state
	 * @param startTime
	 * @param endTime
	 * @param formId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@PostMapping("/searchForm")
	@ResponseBody
	public Result searchForm(String title, Integer state, String startTime, String endTime, Integer formId,
							 @RequestParam(defaultValue = "1") int page,
							 @RequestParam(defaultValue = "10") int pageSize) {
		return iFormService.searchForm(title, state, startTime, endTime, page, pageSize, formId);
	}

	/**
	 * 搜索标题
	 *
	 * @param title
	 * @param state
	 * @param startTime
	 * @param endTime
	 * @param formId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@PostMapping("/searchTitle")
	@ResponseBody
	public Result searchTitle(String title, Integer state, String startTime, String endTime, Integer formId,
							  @RequestParam(defaultValue = "1") int page,
							  @RequestParam(defaultValue = "10") int pageSize) {
		return iFormService.searchTitle(title, state, startTime, endTime, page, pageSize, formId);
	}
}
