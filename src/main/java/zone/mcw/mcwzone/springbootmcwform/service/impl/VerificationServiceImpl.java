package zone.mcw.mcwzone.springbootmcwform.service.impl;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.User;
import zone.mcw.mcwzone.springbootmcwform.redis.RedisFunction;
import zone.mcw.mcwzone.springbootmcwform.service.IUserService;
import zone.mcw.mcwzone.springbootmcwform.service.IVerificationService;
import zone.mcw.mcwzone.springbootmcwform.utils.Check;
import zone.mcw.mcwzone.springbootmcwform.utils.EncryptionUtils;
import zone.mcw.mcwzone.springbootmcwform.utils.MsgSend;
import zone.mcw.mcwzone.springbootmcwform.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author W4i create 2020/9/17 15:53
 * 验证码类的service
 */
@Service
public class VerificationServiceImpl implements IVerificationService {
	private Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

	@Autowired JavaMailSender mailSender;
	@Autowired IUserService iUserService;
	@Resource(description = "redisTemplate10")
	private StringRedisTemplate redisTemplate10;

	MsgSend msgSend = new MsgSend();
	Check check = new Check();
	Time time = new Time();
	EncryptionUtils encryptionUtils = new EncryptionUtils();
	RedisFunction redisFunction = new RedisFunction();

	//定义静态字段
	private static final String resignBody1 = "欢迎注册，你的验证码是：";
	private static final String resignBody2 = ",验证码10分钟内有效";
	private static final String resignTitle = "mcw.zone";
	private static final String forget1 = "你的重置密码地址为：";
	private static final String forget2 = "\n , 5分钟内有效";

	/**
	 * 发送验证码
	 * type:RESIGN,FORGET...
	 *
	 * @param to
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@Override
	public Result sendVerification(String to, String type) {
		User user = new User();
		int re = check.mailPhoneCheck(to);
		Result result = new Result();
		if (re == 0) {
			user.setPhone(to);
		}
		if (re == 1) {
			user.setMail(to);
		}
		if (type.equals("RESIGN")) {
			Result check = iUserService.userCheck(user);
			if (check.isSuccess() == false) {
				return check;
			}
			Result re1 = iUserService.userCheck(user);
			if (!re1.isSuccess()) {
				return re1;
			}
			String key = redisFunction.ResignId(to);
			String token = redisFunction.getTokenByKey(key, redisTemplate10);
			if (StringUtils.hasText(token)) {
				System.out.println(redisFunction.getTTLByKey(key, redisTemplate10));
				if (redisFunction.getTTLByKey(key, redisTemplate10) > 540) {
					result.setMsg("1分钟内不要重试");
					result.setSuccess(false);
					return result;
				}
				redisFunction.deleteTokenByKey(redisTemplate10, key);
			}
			String vCode = encryptionUtils.getCode();
			//组装句子
			String body = resignBody1 + vCode + resignBody2;
			redisFunction.productToken(redisFunction.ResignId(to), vCode, 10, redisTemplate10);
			if (re == 0) {
				return msgSend.sendPhone(to, vCode, "SMS_204285747");
			}
			if (re == 1) {
				return msgSend.sendMail(mailSender, to, body);
			}
		}

		if (type.equals("FORGET")) {
			List<User> userList = iUserService.selectByEntity(user);
			if (userList.size() == 0) {
				result.setSuccess(false);
				result.setMsg("用户不存在");
				return result;
			}
			User user1 = userList.get(0);
			String code = to + "#" + user1.getUserId() + "*" + time.getTime();
			try {
				code = encryptionUtils.encode(code);
				String token = "xxx/xxx/xxx?token=" + code;
				//组装句子
				String body = forget1 + token + forget2;
				String key = redisFunction.getTokenByKey(redisFunction.ForgetId(to), redisTemplate10);
				if (StringUtils.hasText(key)) {
					if (redisFunction.getTTLByKey(key, redisTemplate10) > 240) {
						result.setMsg("1分钟内不要重试");
						result.setSuccess(false);
						return result;
					}
					redisFunction.deleteTokenByKey(redisTemplate10, key);
				}
				redisFunction.productToken(redisFunction.ForgetId(to), code, 5, redisTemplate10);
				if (re == 0) {
					result.setMsg("暂不支持手机找回密码");
					return result;
					//todo 网站没搭起来阿里云不给普通短信接口
//					return msgSend.sendPhone(to, token, "SMS_204285767");
				}
				if (re == 1) {
					return msgSend.sendMail(mailSender, to, body);
				}
			} catch (Exception e) {
				logger.error(e.toString());
				result.setMsg("出错");
				return result;
			}
		}
		result.setMsg("输入有误");
		return result;
	}

	/**
	 * 判断验证码是否正确
	 *
	 * @param to
	 * @param verification
	 * @param type
	 * @return
	 */
	@Override public boolean checkVerification(String to, String verification, String type) {
		String key = type.toUpperCase() + to;
		//获取redis中的token
		String v = redisFunction.getTokenByKey(key, redisTemplate10);
		if (StringUtils.hasText(v) && v.equals(verification)) {
			return true;
		}
		return false;
	}
}
