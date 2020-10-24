package zone.mcw.mcwzone.springbootmcwform.service;


import org.springframework.web.multipart.MultipartFile;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.entity.User;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author W4i create 2020/9/16 14:45
 */
public interface IUserService {
	int deleteByPrimaryKey(Integer userId);

	int insertByEntity(User record);

	List<User> selectByEntity(User user);

	List<User> selectByEntityOr(User user);

	int updateByPrimaryKey(User record);

	int updateEntityByEntity(User newRecord, User oldRecord);

	Result login(User user, HttpSession httpSession) throws Exception;

	Result resign(User user, String verification,String to);

	Result userCheck(User user);

	Result pwdReset(String token, String newPwd);

	Result iconUpload(MultipartFile file);

	Result userPwdChange(String newPwd, String oldPwd);

	Result doQuit();
}
