package zone.mcw.mcwzone.springbootmcwform.service.impl;

import zone.mcw.mcwzone.springbootmcwform.dto.Jwt;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.dao.UserMapper;
import zone.mcw.mcwzone.springbootmcwform.entity.Ban;
import zone.mcw.mcwzone.springbootmcwform.entity.User;
import zone.mcw.mcwzone.springbootmcwform.redis.RedisFunction;
import zone.mcw.mcwzone.springbootmcwform.service.IBanService;
import zone.mcw.mcwzone.springbootmcwform.service.IFileService;
import zone.mcw.mcwzone.springbootmcwform.service.IUserService;
import zone.mcw.mcwzone.springbootmcwform.service.IVerificationService;
import zone.mcw.mcwzone.springbootmcwform.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户类service
 *
 * @author W4i create 2020/9/16 14:46
 */
@Service
public class UserServiceImpl implements IUserService {
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired IBanService iBanService;
	@Autowired IVerificationService iVerificationService;
	@Autowired UserMapper userMapper;
	@Autowired IFileService iFileService;

	@Resource(name = "redisTemplate10")
	private StringRedisTemplate redisTemplate10;

	RedisFunction redisFunction = new RedisFunction();
	GetValue getValue = new GetValue();
	EncryptionUtils encryptionUtils = new EncryptionUtils();
	Time time = new Time();
	Check check = new Check();

	@Override
	public int deleteByPrimaryKey(Integer userId) {
		return userMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insertByEntity(User record) {
		return userMapper.insertByEntity(record);
	}

	@Override
	public List<User> selectByEntity(User user) {
		return userMapper.selectByEntity(user);

	}

	@Override
	public List<User> selectByEntityOr(User user) {
		return userMapper.selectByEntityOr(user);

	}

	@Override
	public int updateByPrimaryKey(User record) {
		return userMapper.updateByPrimaryKey(record);
	}

	@Override public int updateEntityByEntity(User newRecord, User oldRecord) {
		return userMapper.updateEntityByEntity(newRecord, oldRecord);
	}

	/**
	 * 登录
	 *
	 * @param user
	 * @param httpSession
	 * @return
	 */
	@Override
	public Result login(User user, HttpSession httpSession) throws Exception {
		Result result = new Result();
		if (!StringUtils.hasText(user.getPassword()) && !StringUtils.hasText(user.getUsername())) {
			result.setMsg("请输入正确的信息");
			return result;
		}
		user.setPassword(encryptionUtils.getMD5String(user.getPassword()));
		//判断是否为手机号或者邮箱
		if (user.getUsername().length() == 11 || user.getUsername().indexOf("@") != -1) {
			//登陆时输入的是手机号
			if (user.getUsername().length() == 11) {
				user.setPhone(user.getUsername());
			}
			//登陆时输入的是邮箱
			if (user.getUsername().indexOf("@") != -1) {
				user.setMail(user.getUsername());
			}
			user.setUsername(null);
		}
		//查询user表
		List<User> userList = selectByEntity(user);
		if (userList.size() > 0) {
			//从redis获取ban信息
			String unbanTime = redisFunction.getTokenByKey(redisFunction.BanId(userList.get(0).getUserId()), redisTemplate10);
			if (StringUtils.hasText(unbanTime)) {
				result.setSuccess(false);
				Ban ban = iBanService.selectByUserIdOrderById(userList.get(0).getUserId());
				result.setMsg("你将在" + unbanTime + "解封 , 封禁原因 : " + ban.getInfo() + " 详情联系管理员。");
				return result;
			}
			User user1 = userList.get(0);
			//未被封禁，正常执行登录逻辑
			user1.setLastLoginTime(time.getTime());
			updateEntityByEntity(user1, new User(user1.getUserId()));
			Jwt jwt = new Jwt(user1.getUserId(), user1.getAdmin());
			user1.setPassword(null);
			jwt.setTime(time.getTime());
			String token = encryptionUtils.encode(jwt.toString());
			Map<String,Object> map=new HashMap();
			map.put("token",token);
			map.put("user",user1);
			result.setData(map);
			result.setMsg("登陆成功");
			result.setSuccess(true);
			//生成token存放到redis
			if (user1.getAdmin() == 1) {
				//管理员用户6小时重置
				redisFunction.productToken(redisFunction.UserId(user1.getUserId()), token, 360, redisTemplate10);
			} else {
				//用户token在72小时后超时重置
				redisFunction.productToken(redisFunction.UserId(user1.getUserId()), token, 4320, redisTemplate10);
			}
		} else {
			result.setSuccess(false);
			result.setMsg("用户名或密码错误");
		}
		return result;
	}

	/**
	 * 注册
	 *
	 * @param user
	 * @param verification
	 * @return
	 */
	@Override
	public Result resign(User user, String verification, String to) {
		verification=verification.toUpperCase();
		Result result = new Result();
		//判断信息是否漏填
		if (!StringUtils.hasText(to)) {
			result.setMsg("请填写必要的信息");
			return result;
		}
		int a = check.mailPhoneCheck(to);
		switch (a) {
		case 1:
			user.setMail(to);
			break;
		case 0:
			user.setPhone(to);
			break;
		default:
			result.setMsg("请填写正确的信息");
			return result;
		}
		//判断用户名是否合法
		if (user.getUsername().indexOf("@") != -1) {
			result.setSuccess(false);
			result.setMsg("用户名中不允许出现@");
		}
		if (user.getUsername().length() > 10) {
			result.setSuccess(false);
			result.setMsg("用户名长度不能大于10");
		}
		//判断验证码
		if (StringUtils.hasText(user.getMail())) {
			//检查验证码是否正确
			if (!iVerificationService.checkVerification(user.getMail(), verification, "RESIGN")) {
				result.setMsg("验证码错误");
				return result;
			}
		}
		if (StringUtils.hasText(user.getPhone())) {
			//检查验证码是否正确
			if (!iVerificationService.checkVerification(user.getPhone(), verification, "RESIGN")) {
				result.setMsg("验证码错误");
				return result;
			}
		}
		try {
			//写入数据
			user.setPassword(encryptionUtils.getMD5String(user.getPassword()));
			user.setCreateTime(time.getTime());
			insertByEntity(user);
			user.setPassword("***************************");
			result.setData(user);
			result.setMsg("注册成功");
		} catch (Exception e) {
			result.setMsg("注册失败:" + e.toString());
			logger.error(e.toString());
		}
		return result;
	}

	/**
	 * 判断用户名、邮箱、手机号是否可用
	 *
	 * @param user
	 * @return
	 */
	@Override
	public Result userCheck(User user) {
		Result result = new Result();
		//获取user
		List<User> userList = selectByEntityOr(user);
		//如果返回值不为空，说明表中已存在任意字段
		if (userList.size() > 0) {
			User check = userList.get(0);
			result.setSuccess(false);
			//判断存在的的字段是哪些
			if (check.getMail() != null) {
				result.setMsg("邮箱已存在");
				return result;
			}
			if (check.getPhone() != null) {
				result.setMsg("手机号已存在");
				return result;
			}
			if (check.getUsername() != null) {
				result.setMsg("用户名已存在");
				return result;
			}
		}
		result.setSuccess(true);
		result.setMsg("可用");
		return result;
	}

	/**
	 * 通过临时token重置密码
	 *
	 * @param token
	 * @param newPwd
	 * @return
	 */
	@Override
	public Result pwdReset(String token, String newPwd) {
		Result result = new Result();
		try {
			//解密token获取邮箱和用户id(其实有一个就够了，但是懒得坐邮箱还是手机的判断)
			String tokenD = encryptionUtils.decode(token);
			String to = tokenD.substring(0, tokenD.indexOf("#"));
			String userId = tokenD.substring(tokenD.indexOf("#") + 1, tokenD.indexOf("*"));
			if (iVerificationService.checkVerification(to, token, "FORGET")) {
				//newUser插入新密码
				User newUser = new User();
				newUser.setPassword(encryptionUtils.getMD5String(newPwd));
				//oldUser插入userId
				User oldUser = new User(Integer.parseInt(userId));
				if (updateEntityByEntity(newUser, oldUser) > 0) {
					//更新密码成功，删除redis中的FORGET和USER
					redisFunction.deleteTokenByKey(redisTemplate10, redisFunction.ForgetId(to));
					redisFunction.deleteTokenByKey(redisTemplate10, redisFunction.UserId(oldUser.getUserId()));
					result.setSuccess(true);
				} else {
					result.setSuccess(false);
					result.setMsg("?????");
				}
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		result.setMsg("错误");
		result.setSuccess(false);
		return result;
	}

	/**
	 * 用户头像上传
	 *
	 * @param file
	 * @return
	 */
	@Override
	public Result iconUpload(MultipartFile file) {
		//获取文件，type=pic上传
		Result result = iFileService.fileUpload(file, "PIC");
		if (!result.isSuccess()) {
			return result;
		}
		//将头像名称更新到用户表
		String fileName = (String) result.getData();
		User user = new User();
		user.setIcon(fileName);
		updateEntityByEntity(user, new User(getValue.getJwt().getUserId()));
		return result;
	}

	/**
	 * 修改密码
	 *
	 * @param newPwd
	 * @param oldPwd
	 * @return
	 */
	@Override
	public Result userPwdChange(String newPwd, String oldPwd) {
		//oldUser：旧密码、用户id，newUser：新密码
		Result result = new Result();
		User newUser = new User();
		User oldUser = new User();
		newUser.setPassword(encryptionUtils.getMD5String(newPwd));
		oldUser.setPassword(encryptionUtils.getMD5String(oldPwd));
		int userId = getValue.getJwt().getUserId();
		oldUser.setUserId(userId);
		if (userMapper.updateEntityByEntity(newUser, oldUser) > 0) {
			result.setSuccess(true);
			//更新后删除redis中的USER防止复用
			return doQuit();
		} else {
			result.setSuccess(false);
			return result;
		}
	}

	/**
	 * 退出登录（删除redis中的USER）
	 *
	 * @return
	 */
	@Override
	public Result doQuit() {
		Result result = new Result();
		try {
			redisFunction.deleteTokenByKey(redisTemplate10, redisFunction.UserId(getValue.getJwt().getUserId()));
			result.setSuccess(true);
			return result;
		} catch (Exception e) {
			result.setMsg(e.toString());
			logger.error(e.toString());
			return result;
		}
	}

}
