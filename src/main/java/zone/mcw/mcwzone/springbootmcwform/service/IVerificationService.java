package zone.mcw.mcwzone.springbootmcwform.service;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;

/**
 * @author W4i create 2020/9/29 15:14
 */
public interface IVerificationService {
	Result sendVerification(String to, String type);

	boolean checkVerification(String to, String verification, String type);

}
