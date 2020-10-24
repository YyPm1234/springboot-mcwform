package zone.mcw.mcwzone.springbootmcwform.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志表
 */
@Getter
@Setter
@ToString
public class Log {
	private Integer logId;
	private Integer userId;
	private String ip;
	private String time;
	private String classMethod;
	private Integer success;
	private String args;
	private String level;

	public Log(Integer userId, String ip, String time, String classMethod, Integer success, String args, String level) {
		this.userId = userId;
		this.ip = ip;
		this.time = time;
		this.classMethod = classMethod;
		this.success = success;
		this.args = args;
		this.level = level;
	}

	public Log(Integer logId) {
		this.logId = logId;
	}

	public Log() {
	}
}