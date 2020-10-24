package zone.mcw.mcwzone.springbootmcwform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * redis多库配置，现在看来没什么用
 *
 * @author W4i create 2020/9/29 8:59
 */
@Configuration
@EnableAutoConfiguration
public class RedisConfig {
	@Value(value = "${spring.redis.database.db10}")
	private int db10;

	@Value(value = "${spring.redis.database.db11}")
	private int db11;

	@Value(value = "${spring.redis.database.db12}")
	private int db12;

	@Value(value = "${spring.redis.database.db13}")
	private int db13;

	@Value(value = "${spring.redis.database.db14}")
	private int db14;

	@Value(value = "${spring.redis.database.db15}")
	private int db15;

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.port}")
	private int port;

	@Primary
	@Bean(name = "redisTemplate10")
	public StringRedisTemplate getRedisTemplate10() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setDatabase(db10);
		connectionFactory.setHostName(host);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(port);
		connectionFactory.afterPropertiesSet();
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
		return stringRedisTemplate;
	}

	@Bean(name = "redisTemplate11")
	public StringRedisTemplate getRedisTemplate11() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setDatabase(db11);
		connectionFactory.setHostName(host);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(port);
		connectionFactory.afterPropertiesSet();
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
		return stringRedisTemplate;
	}

	@Bean(name = "redisTemplate12")
	public StringRedisTemplate getRedisTemplate12() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setDatabase(db12);
		connectionFactory.setHostName(host);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(port);
		connectionFactory.afterPropertiesSet();
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
		return stringRedisTemplate;
	}

	@Bean(name = "redisTemplate13")
	public StringRedisTemplate getRedisTemplate13() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setDatabase(db13);
		connectionFactory.setHostName(host);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(port);
		connectionFactory.afterPropertiesSet();
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
		return stringRedisTemplate;
	}

	@Bean(name = "redisTemplate14")
	public StringRedisTemplate getRedisTemplate14() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setDatabase(db14);
		connectionFactory.setHostName(host);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(port);
		connectionFactory.afterPropertiesSet();
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
		return stringRedisTemplate;
	}

	@Bean(name = "redisTemplate15")
	public StringRedisTemplate getRedisTemplate15() {
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
		connectionFactory.setDatabase(db15);
		connectionFactory.setHostName(host);
		connectionFactory.setPassword(password);
		connectionFactory.setPort(port);
		connectionFactory.afterPropertiesSet();
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
		return stringRedisTemplate;
	}
}
