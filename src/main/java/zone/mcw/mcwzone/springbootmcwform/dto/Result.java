package zone.mcw.mcwzone.springbootmcwform.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 返回类型
 *
 * @author W4i create 2020/9/16 14:27
 */
@Getter
@Setter
@ToString
public class Result {
	private boolean isSuccess;
	private String msg;
	private Object data;
	private int code;

	public Result() {
		this.isSuccess = false;
	}
}
