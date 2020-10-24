package zone.mcw.mcwzone.springbootmcwform.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import zone.mcw.mcwzone.springbootmcwform.dto.MsgReturn;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author W4i create 2020/9/30 14:27
 */
public class MsgSend {
	private Logger logger= LoggerFactory.getLogger(MsgSend.class);

	public Result sendMail(JavaMailSender mailSender,String mail, String body) {
		Result result = new Result();
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("mcw.zone@foxmail.com"); // 邮件发送者
			message.setTo(mail); // 邮件接受者
			message.setSubject("mcw.zone"); // 主题
			message.setText(body);//正文
			mailSender.send(message);//发送
			result.setMsg("发送成功");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setMsg("发送失败");
			result.setSuccess(false);
			logger.error(e.toString());
		}
		return result;
	}

	public Result sendPhone(String to, String body,String TemplateCode) {
		Result result=new Result();
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "accessKeyId", "secret");
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain("dysmsapi.aliyuncs.com");
		request.setSysVersion("2017-05-25");
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("PhoneNumbers", to);
		request.putQueryParameter("SignName", "mcw论坛");
		request.putQueryParameter("TemplateCode", TemplateCode);
		request.putQueryParameter("TemplateParam", "{\"code\":\""+body+"\"}");
		try {
			CommonResponse response = client.getCommonResponse(request);
			MsgReturn msgReturn= JSONObject.parseObject(response.getData(), MsgReturn.class);
			if (!msgReturn.getCode().equals("OK")){
				result.setCode(-2);
				result.setMsg("发送失败");
				logger.error(msgReturn.getMessage());
			}
			result.setData(response.getData());
			result.setSuccess(true);
		} catch (ClientException e) {
			result.setMsg("发送失败");
			result.setCode(-1);
			logger.error(e.toString());
		}
		return result;
	}
}
