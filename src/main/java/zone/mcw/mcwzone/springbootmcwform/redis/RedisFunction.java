package zone.mcw.mcwzone.springbootmcwform.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import zone.mcw.mcwzone.springbootmcwform.config.RedisConfig;

import java.util.concurrent.TimeUnit;

/**
 * redis自定义方法
 *
 * @author W4i create 2020/9/28 15:43
 */
public class RedisFunction {
	@Autowired RedisConfig redisConfig;

	/**
	 * 添加
	 *
	 * @param key
	 * @param value
	 * @param time
	 * @param stringRedisTemplate
	 */
	public void productToken(String key, String value, long time, StringRedisTemplate stringRedisTemplate) {
		stringRedisTemplate.opsForValue().set(key, value);
		stringRedisTemplate.expire(key, time, TimeUnit.MINUTES);
	}

	/**
	 * 通过key获取过期时间
	 *
	 * @param key
	 * @param stringRedisTemplate
	 * @return
	 */
	public Long getTTLByKey(String key, StringRedisTemplate stringRedisTemplate) {
		return stringRedisTemplate.getExpire(key);
	}

	/**
	 * 查看
	 *
	 * @param key
	 * @param stringRedisTemplate
	 * @return
	 */
	public String getTokenByKey(String key, StringRedisTemplate stringRedisTemplate) {
		if (stringRedisTemplate.opsForValue().get(key) == null) {
			return null;
		} else {
			return stringRedisTemplate.opsForValue().get(key);
		}
	}

	/**
	 * 删除
	 *
	 * @param stringRedisTemplate
	 * @param key
	 */
	public void deleteTokenByKey(StringRedisTemplate stringRedisTemplate, String... key) {
		for (String s : key) {
			stringRedisTemplate.delete(s);
		}
	}

	//统一key前缀
	public String UserId(int code) {
		return "USER" + code;
	}

	public String ForgetId(String code) {
		return "FORGET" + code;
	}

	public String ResignId(String code) {
		return "RESIGN" + code;
	}

	public String BanId(int code) {
		return "BAN" + code;
	}
}
