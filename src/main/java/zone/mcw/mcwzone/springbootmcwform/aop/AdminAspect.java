package zone.mcw.mcwzone.springbootmcwform.aop;

import zone.mcw.mcwzone.springbootmcwform.dto.Jwt;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.dto.Args;
import zone.mcw.mcwzone.springbootmcwform.entity.Log;
import zone.mcw.mcwzone.springbootmcwform.redis.RedisFunction;
import zone.mcw.mcwzone.springbootmcwform.service.ILogService;
import zone.mcw.mcwzone.springbootmcwform.utils.EncryptionUtils;
import zone.mcw.mcwzone.springbootmcwform.utils.GetValue;
import zone.mcw.mcwzone.springbootmcwform.utils.Time;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * admin方法拦截
 *
 * @author W4i create 2020/9/21 16:53
 */
@Aspect
@Component
public class AdminAspect {
	private Logger logger = LoggerFactory.getLogger(AdminAspect.class);
	@Autowired ILogService iLogService;
	@Resource(name = "redisTemplate10")
	private StringRedisTemplate db10;

	RedisFunction redisFunction = new RedisFunction();
	GetValue getValue = new GetValue();
	Time time = new Time();
	EncryptionUtils encryptionUtils = new EncryptionUtils();

	//切入点AdminController.*
	@Pointcut("execution( public * zone.mcw.mcwzone.springbootmcwform.controller.*.*admin*(..))"
			+ "|| execution( public * zone.mcw.mcwzone.springbootmcwform.controller.AdminController.*(..))")
	public void adminMethod() {
	}

	@Around("adminMethod()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Args args = getValue.getArgs(joinPoint);
		Jwt jwt = args.getJWT();
		Result result = new Result();
		String tokken = getValue.getToken();
		//本地token解析
		if (jwt.getUserId() == 0 || jwt.getAdmin() != 1) {
			result.setMsg("你不是管理员");
			result.setSuccess(false);
			iLogService
					.insertByEntity(
							new Log(jwt.getUserId(), args.getIP(), time.getTime(), args.getCLASS_METHOD(), 0, encryptionUtils.encode(args.toString()),
									"ERROR"));
			logger.error(
					args.getIP() + " : 用户不是管理员，试图访问方法 : " + args.getCLASS_METHOD() + "(" + args.getHTTP_METHOD() + ") , ARGS : " + args
							.getARGS());
			return result;
		}
		//在线验证
		if (jwt.getUserId() != 0) {
			String token = redisFunction.getTokenByKey(redisFunction.UserId(jwt.getUserId()), db10);
			if (!StringUtils.hasText(token) || !token.equals(tokken)) {
				result.setMsg("请先登录");
				result.setSuccess(false);
				iLogService
						.insertByEntity(new Log(jwt.getUserId(), args.getIP(), time.getTime(), args.getCLASS_METHOD(), 0,
								encryptionUtils.encode(args.toString()), "ERROR"));
				logger.error(
						args.getIP() + " : 用户不是管理员，试图访问方法 : " + args.getCLASS_METHOD() + "(" + args.getHTTP_METHOD() + ") , ARGS : " + args
								.getARGS());
				return result;
			}
		}
		//成功
		iLogService
				.insertByEntity(
						new Log(jwt.getUserId(), "", time.getTime(), args.getCLASS_METHOD(), 1, encryptionUtils.encode(args.toString()), "INFO"));
		logger.info("adminId : " + jwt.getUserId() + " , CLASS_METHOD : " + args.getCLASS_METHOD() + "(" + args.getHTTP_METHOD() + ")");
		return joinPoint.proceed();

	}
}
