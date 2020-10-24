package zone.mcw.mcwzone.springbootmcwform.controller;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Log;
import zone.mcw.mcwzone.springbootmcwform.service.IAdminService;
import zone.mcw.mcwzone.springbootmcwform.service.IBanService;
import zone.mcw.mcwzone.springbootmcwform.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author by W4i
 * @date 2020/9/29 20:49
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired ILogService iLogService;
	@Autowired IBanService iBanService;

	@PostMapping("/ban")
	@ResponseBody
	public Result ban(int userId, String info, String banTime) {
		return iBanService.banUser(userId, info, banTime);
	}

	@PostMapping("/unban")
	@ResponseBody
	public Result unban(int userId, String info) {
		return iBanService.unbanUser(userId, info);
	}

	@PostMapping("/banSearch")
	@ResponseBody
	public Result banSearch(@RequestParam(defaultValue = "1") int page,
							@RequestParam(defaultValue = "20") int pageSize,
							@RequestParam(defaultValue = "ban_id") String order,
							@RequestParam(defaultValue = "0") String desc) {
		return iBanService.selectByPage(page, pageSize, order, desc);
	}

	@PostMapping("/banCheck")
	@ResponseBody
	public Result banCheck(int userId) {
		return iBanService.banCheckByUserId(userId);
	}

	@PostMapping("/logSearch")
	@ResponseBody
	public Result logSearch(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize,
			@RequestParam(defaultValue = "log_id") String order,
			@RequestParam(defaultValue = "0") String desc) {
		return iLogService.logGetAll(page, pageSize, order, desc);
	}
}
