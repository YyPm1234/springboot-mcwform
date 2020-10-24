package zone.mcw.mcwzone.springbootmcwform.dto;


import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * token类型
 *
 * @author W4i create 2020/9/28 14:24
 */
@Getter
@Setter
public class Jwt {
	private int admin;
	private String time;
	private int userId;

	public Jwt(int userId, int admin) {
		this.userId = userId;
		this.admin = admin;
	}

	public Jwt() {
	}

	public Jwt(int admin, String time, int userId) {
		this.admin = admin;
		this.time = time;
		this.userId = userId;
	}

	@Override public String toString() {
		return JSONObject.toJSONString(this);
	}
}
