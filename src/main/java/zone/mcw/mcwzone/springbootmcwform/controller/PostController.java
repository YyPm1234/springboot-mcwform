package zone.mcw.mcwzone.springbootmcwform.controller;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 已被废除，通过FormDto替换
 *
 * @author by W4i
 * @date 2020/10/11 15:32
 */
@Controller
@RequestMapping("post")
public class PostController {

	/**
	 * 通过formId获取帖子的所有内容
	 *
	 * @param formId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getPost")
	public Result getPost(Integer formId) {
		return null;
	}

}
