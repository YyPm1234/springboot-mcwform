package zone.mcw.mcwzone.springbootmcwform.utils;

import com.alibaba.fastjson.JSONObject;
import zone.mcw.mcwzone.springbootmcwform.dto.Args;
import zone.mcw.mcwzone.springbootmcwform.dto.Jwt;
import zone.mcw.mcwzone.springbootmcwform.entity.Log;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 获取值
 *
 * @author W4i create 2020/9/22 10:00
 */
public class GetValue {
	EncryptionUtils encryptionUtils = new EncryptionUtils();

	/**
	 * 获取token并解析
	 *
	 * @return
	 */
	public Jwt getJwt() {
		String token = getToken();
		try {
			token = encryptionUtils.decode(token);
			Jwt jwt = JSONObject.parseObject(token, Jwt.class);
			return jwt;
		} catch (Exception e) {
			return new Jwt(0, 0);
		}
	}

	public String getToken() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		return request.getHeader("Json-Web-Token");
	}

	/**
	 * 获取调用方法时的URL，HTTP_METHOD等参数信息
	 *
	 * @param joinPoint
	 * @return
	 */
	public Args getArgs(ProceedingJoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String ARGS = Arrays.toString(joinPoint.getArgs());
		String CLASS_METHOD = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
		CLASS_METHOD = CLASS_METHOD.substring(CLASS_METHOD.indexOf("controller") + "controller".length() + 1);
		String URL = request.getRequestURL().toString();
		String HTTP_METHOD = request.getMethod();
		Jwt jwt = getJwt();
		String IP = request.getRemoteAddr();
		Args args = new Args(URL, CLASS_METHOD, HTTP_METHOD, ARGS, jwt, IP);
		return args;
	}

	public List<String> getEntity(Object object) {
		List<String> list = new ArrayList<>();
		for (Field field : object.getClass().getDeclaredFields()) {
			JsonProperty annotation = field.getAnnotation(JsonProperty.class);
			if (null == annotation) {
				list.add(field.getName());
				continue;
			}
		}
		return list;
	}

	public String toHump(String oldStr) {
		String hump = "";
		while (true) {
			int split = oldStr.indexOf("_");
			if (split != -1) {
				oldStr = oldStr.substring(0, split) + oldStr.substring(split + 1, split + 2).toUpperCase() + oldStr.substring(split + 2);
			} else {
				hump = oldStr;
				break;
			}
		}
		return hump;
	}

}
