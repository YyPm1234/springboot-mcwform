package zone.mcw.mcwzone.springbootmcwform.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 调用方法时的入参
 *
 * @author W4i create 2020/9/23 8:37
 */
@Setter
@Getter

public class Args {
	private String URL;
	private String CLASS_METHOD;
	private String HTTP_METHOD;
	private String ARGS;
	private Jwt JWT;
	private String IP;

	public Args(String URL, String CLASS_METHOD, String HTTP_METHOD, String ARGS, Jwt JWT, String IP) {
		this.URL = URL;
		this.CLASS_METHOD = CLASS_METHOD;
		this.HTTP_METHOD = HTTP_METHOD;
		this.ARGS = ARGS;
		this.JWT = JWT;
		this.IP = IP;
	}

	@Override public String toString() {
		return JSONObject.toJSONString(this);
	}
}
