package zone.mcw.mcwzone.springbootmcwform.service.impl;

import zone.mcw.mcwzone.springbootmcwform.dto.Result;
import zone.mcw.mcwzone.springbootmcwform.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传service
 *
 * @author W4i create 2020/9/22 9:43
 */
@Service
public class FileServiceImpl implements IFileService {
	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	/**
	 * 文件上传
	 * 服务器环境已经测试通了，就可能不在本地环境再进行特别的测试，如果需要本地测试需要将filePath进行替换,并提前再resources下创建uploadTmp文件夹
	 * String filePath = FileServiceImpl.class.getResource("/").getPath()+"uploadTmp/";
	 *
	 * @param file
	 * @return
	 */
	@Override
	public Result fileUpload(MultipartFile file, String type) {
		Result result = new Result();
		result.setSuccess(false);
		if (file.isEmpty()) {
			result.setCode(-1);
			result.setMsg("上传失败，请选择文件");
			return result;
		}
		String fileName = file.getOriginalFilename();
		String last = fileName.substring(fileName.indexOf("."));
		if (type.equals("PIC")) {
			//检测图片
			if (!last.equals(".png") && !last.equals(".jpg")) {
				result.setCode(-2);
				result.setMsg("不是图片");
				return result;
			}
			//15MB大小限制
			if (file.getSize() > 15728640) {
				result.setCode(-3);
				result.setMsg("太大");
				return result;
			}
		}
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		fileName = uuid + last;
		String filePath = FileServiceImpl.class.getResource("/").getPath()+"static/uploadTmp/";
//		String filePath = "/opt/webapps/mcwForm/upload/";
		File dest = new File(filePath + fileName);
		File path=new File(filePath);
		try {
			if (!path.exists()){
				path.mkdirs();
			}
			file.transferTo(dest);
			logger.info("上传成功 : " + filePath + fileName);
			result.setCode(1);
			result.setData(fileName);
			result.setSuccess(true);
			return result;
		} catch (IOException e) {
			logger.error(e.toString());
			result.setCode(-4);
			result.setMsg("上传失败");
			logger.error(e.toString());
			return result;
		}
	}
}
