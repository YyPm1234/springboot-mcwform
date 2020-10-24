package zone.mcw.mcwzone.springbootmcwform.dto;


import lombok.Data;

/**
 * 用于阿里云短信服务返回值判断处理
 * @author by W4i
 * @date 2020/10/10 22:12
 */
@Data
public class MsgReturn {
	private String Message;
	private String RequestId;
	private String Code;
}
