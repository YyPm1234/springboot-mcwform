package zone.mcw.mcwzone.springbootmcwform.controller;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.utils.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 对外公共接口
 *
 * @author W4i create 2020/9/23 9:45
 */
@Controller
@RequestMapping("/common")
public class CommonController {
	private Logger logger = LoggerFactory.getLogger(CommonController.class);

	EncryptionUtils encryptionUtils = new EncryptionUtils();

	/**
	 * 对称加密对外接口
	 *
	 * @param code
	 * @return
	 */
	@ResponseBody
	@PostMapping("/encryption/encode")
	public Result encode(String code) {
		Result result = new Result();
		try {
			code = encryptionUtils.encode(code);
			result.setSuccess(true);
			result.setMsg(code);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.toString());
			logger.error(e.toString());
		}
		return result;
	}

	/**
	 * 对称解密对外接口
	 *
	 * @param code
	 * @return
	 */
	@ResponseBody
	@PostMapping("/encryption/decode")
	public Result decode(String code) {
		Result result = new Result();
		try {
			code = encryptionUtils.decode(code);
			result.setSuccess(true);
			result.setMsg(code);
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.toString());
			logger.error(e.toString());
		}
		return result;
	}
}
