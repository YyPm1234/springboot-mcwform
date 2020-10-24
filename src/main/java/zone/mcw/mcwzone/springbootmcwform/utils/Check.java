package zone.mcw.mcwzone.springbootmcwform.utils;

import com.alibaba.fastjson.JSONObject;
import zone.mcw.mcwzone.springbootmcwform.dto.MsgReturn;

import java.util.HashMap;
import java.util.Map;

/**
 * @author W4i create 2020/9/29 14:23
 */
public class Check {
	/**
	 * 判断传入内容是手机号还是邮箱
	 * 邮箱：1
	 * 手机：0
	 * 四不像：-1
	 *
	 * @param to
	 * @return
	 */
	public int mailPhoneCheck(String to) {
		if (to.indexOf("@") != -1) {
			return 1;
		}
		if (to.length() == 11) {
			try {
				Long.parseLong(to);
				return 0;
			} catch (NumberFormatException e) {
			}
		}
		return -2;
	}
}
