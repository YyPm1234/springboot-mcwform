package zone.mcw.mcwzone.springbootmcwform.dto;


import lombok.Data;

/**
 * @author W4i create 2020/10/12 14:58
 */
@Data
public class ReplyDto {
	private Integer replyId;

	private Integer formId;

	private Integer parentId;

	private String content;

	private Integer floor;

	private String createTime;

	private Integer userId;

	private Integer state;

	private String username;

	private String icon;
}
