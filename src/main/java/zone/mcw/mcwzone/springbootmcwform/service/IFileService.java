package zone.mcw.mcwzone.springbootmcwform.service;


import org.springframework.web.multipart.MultipartFile;
import zone.mcw.mcwzone.springbootmcwform.dto.Result;

/**
 * @author W4i create 2020/9/22 9:42
 */
public interface IFileService {
	Result fileUpload(MultipartFile file, String type);
}
