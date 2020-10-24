package zone.mcw.mcwzone.springbootmcwform.dto;

import lombok.Data;

/**
 * @author W4i create 2020/10/12 15:15
 */
@Data
public class FormDto {
	private Integer formId;

	private Integer moduleId;

	private String moduleName;

	private Integer userId;

	private String formTitle;

	private String createTime;

	private Integer state;

	private String username;

	private String icon;
}
