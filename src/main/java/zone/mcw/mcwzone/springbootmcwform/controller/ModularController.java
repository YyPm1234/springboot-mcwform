package zone.mcw.mcwzone.springbootmcwform.controller;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.Modular;
import zone.mcw.mcwzone.springbootmcwform.service.IModularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author by W4i
 * @date 2020/10/1 15:02
 */
@Controller
@RequestMapping("/modular")
public class ModularController {
	@Autowired IModularService iModularService;

	/**
	 * 添加模块
	 *
	 * @param modularName
	 * @return
	 */
	@ResponseBody
	@PostMapping("/adminAdd")
	public Result adminAdd(String modularName) {
		return iModularService.add(modularName);
	}

	/**
	 * 改变模块状态
	 *
	 * @param modularId
	 * @param state
	 * @return
	 */
	@ResponseBody
	@PostMapping("/adminChangeState")
	public Result adminChangeState(int modularId, int state) {
		return iModularService.changeState(modularId, state);
	}

	/**
	 * 删除模块
	 *
	 * @param modularId
	 * @return
	 */
	@ResponseBody
	@PostMapping("/adminDelete")
	public Result adminDelete(int modularId) {
		Result result = new Result();
		if (iModularService.deleteByPrimaryKey(modularId) > 0) {
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 * 查询模块
	 *
	 * @param modularName
	 * @param state
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getModular")
	public Result getModular(String modularName, Integer state) {
		return iModularService.getModular(modularName, state);
	}
}
