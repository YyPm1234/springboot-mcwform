package zone.mcw.mcwzone.springbootmcwform.controller;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.User;
import zone.mcw.mcwzone.springbootmcwform.service.IUserService;
import zone.mcw.mcwzone.springbootmcwform.service.IVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

/**
 * 用户controller
 *
 * @author W4i create 2020/9/16 14:26
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired IUserService iUserService;
	@Autowired IVerificationService iVerificationService;

	/**
	 * 注册
	 *
	 * @param user         用户信息，目前需要：username，password，mail
	 * @param verification 验证码，先通过邮箱调取getVerification方法获取验证码，再调用注册
	 * @return
	 */
	@PostMapping("/resign")
	@ResponseBody
	public Result resign(User user, String verification,String to) {
		return iUserService.resign(user, verification,to);
	}

	/**
	 * 登录
	 *
	 * @param user        用户信息，username&password，username可以写用户名、手机号、邮箱，service有判断
	 * @param httpSession session用户储存用户，后期改为Spring Security或者shiro
	 * @return
	 */
	@PostMapping("/login")
	@ResponseBody
	public Result login(User user, HttpSession httpSession) throws Exception {
		Result result = new Result();
		if (null == user || null == user.getUsername() || null == user.getPassword()) {
			result.setSuccess(false);
			result.setData(null);
			result.setMsg("信息填完整");
			return result;
		}
		return iUserService.login(user, httpSession);
	}

	/**
	 * 获取验证码
	 *
	 * @param to   邮箱或者手机号
	 * @param type 验证码类型：type=RESIGN 注册，FORGET 找回，ADD 加绑
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getVerification")
	public Result getVerification(String to, String type) {
		return iVerificationService.sendVerification(to, type);
	}

	/**
	 * 忘记密码获取临时token
	 *
	 * @param mailPhone 手机号或者邮箱，service判断
	 * @return
	 */
	@ResponseBody
	@PostMapping("/getForgetToken")
	public Result getForgetToken(String mailPhone) throws Exception {
		return iVerificationService.sendVerification(mailPhone, "FORGET");
	}

	/**
	 * 通过临时token重置密码
	 *
	 * @param token
	 * @param newPwd
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/pwdReset")
	public Result pwdReset(String token, String newPwd) {
		return iUserService.pwdReset(token, newPwd);
	}

	/**
	 * 用户头像上传
	 *
	 * @param file
	 * @return
	 */
	@ResponseBody
	@PostMapping("doUploadIcon")
	public Result doUploadIcon(MultipartFile file) {
		return iUserService.iconUpload(file);
	}

	/**
	 * 修改密码
	 *
	 * @param newPwd
	 * @param oldPwd
	 * @return
	 */
	@ResponseBody
	@PostMapping("doPwdChange")
	public Result doPwdChange(String newPwd, String oldPwd) {
		return iUserService.userPwdChange(newPwd, oldPwd);
	}

	/**
	 * 退出登录（删除token）
	 *
	 * @return
	 */
	@ResponseBody
	@PostMapping("doQuit")
	public Result doQuit() {
		return iUserService.doQuit();
	}
}
