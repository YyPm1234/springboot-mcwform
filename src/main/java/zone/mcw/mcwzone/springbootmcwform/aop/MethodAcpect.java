package zone.mcw.mcwzone.springbootmcwform.aop;


import zone.mcw.mcwzone.springbootmcwform.dto.Args;
import zone.mcw.mcwzone.springbootmcwform.entity.Log;
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
import org.springframework.stereotype.Component;

/**
 * 普通方法拦截
 * 普通方法不做拦截只做日志输出
 *
 * @author W4i create 2020/9/21 14:41
 */
@Aspect
@Component
public class MethodAcpect {
	private Logger logger = LoggerFactory.getLogger(MethodAcpect.class);
	GetValue getValue = new GetValue();
	EncryptionUtils encryptionUtils = new EncryptionUtils();
	Time time = new Time();
	@Autowired ILogService iLogService;

	//切入点：所有方法，并剔除admin*和do*防止重复logger
	@Pointcut("execution( public * zone.mcw.mcwzone.springbootmcwform.controller.*.*(..))"
			+ "&&!execution( public * zone.mcw.mcwzone.springbootmcwform.controller.*.*do*(..))"
			+ "&&!execution( public * zone.mcw.mcwzone.springbootmcwform.controller.AdminController.*(..))"
			+ "&&!execution( public * zone.mcw.mcwzone.springbootmcwform.controller..*..*admin*(..))"
	)
	public void method() {
	}

	//
	@Around("method()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Args args = getValue.getArgs(joinPoint);
		logger.info(args.getIP() + " : " + args.getCLASS_METHOD() + "(" + args.getHTTP_METHOD() + ")");
		iLogService.insertByEntity(new Log(
				getValue.getJwt().getUserId(), args.getIP(), time.getTime(), args.getCLASS_METHOD(),
				null, encryptionUtils.encode(args.toString()), "INFO"));
		return joinPoint.proceed();
	}
}
