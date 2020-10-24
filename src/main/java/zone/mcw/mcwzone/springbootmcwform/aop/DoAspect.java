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
 * do*方法拦截
 *
 * @author W4i create 2020/9/21 14:50
 */
@Aspect
@Component
public class DoAspect {
	private Logger logger = LoggerFactory.getLogger(DoAspect.class);

	@Autowired ILogService iLogService;
	@Resource(name = "redisTemplate10")
	private StringRedisTemplate db10;

	Time time = new Time();
	GetValue getValue = new GetValue();
	RedisFunction redisFunction = new RedisFunction();
	EncryptionUtils encryptionUtils = new EncryptionUtils();

	//切入点：do*
	@Pointcut("execution( public * zone.mcw.mcwzone.springbootmcwform.controller.*.*do*(..))")
	public void doMethod() {
	}

	@Around("doMethod()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

		Result result = new Result();
		result.setSuccess(false);
		Args args = getValue.getArgs(joinPoint);
		//本地判断是否存在token
		Jwt jwt = args.getJWT();
		if (jwt.getUserId() == 0) {
			result.setMsg("用户未登录");
			result.setCode(-1);
			logger.error(args.getIP() + " : 用户未登录，试图访问方法 : " + args.getCLASS_METHOD() + "(" + args.getHTTP_METHOD() + ") , ARGS : " + args.getARGS());
			iLogService.insertByEntity(new Log(
					jwt.getUserId(), args.getIP(), time.getTime(), args.getCLASS_METHOD(), 0,
					encryptionUtils.encode(args.toString()), "ERROR"));
			return result;
		}
		//在redis中判断token是否有效
		String token = getValue.getToken();
		String redisToken = redisFunction.getTokenByKey(redisFunction.UserId(jwt.getUserId()), db10);
		if (!StringUtils.hasText(redisToken) || !redisToken.equals(token)) {
			result.setMsg("token失效,需重新登录");
			result.setCode(-2);
			iLogService.insertByEntity(new Log(
					jwt.getUserId(), args.getIP(), time.getTime(), args.getCLASS_METHOD(),
					0, encryptionUtils.encode(args.toString()), "ERROR"));
			logger.error(
					args.getIP() + " : token超时，试图访问方法 : " + args.getCLASS_METHOD() + "(" + args.getHTTP_METHOD() + ") , ARGS : " + args.getARGS());
			return result;
		}
		//通过
		iLogService.insertByEntity(new Log(
				jwt.getUserId(), "", time.getTime(), args.getCLASS_METHOD(),
				1, encryptionUtils.encode(args.toString()), "INFO"));
		logger.info("userId : " + jwt.getUserId() + " : " + args.getCLASS_METHOD() + "(" + args.getHTTP_METHOD() + ")");
		return joinPoint.proceed();
	}
}
