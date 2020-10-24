package zone.mcw.mcwzone.springbootmcwform.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Reply {
	private Integer replyId;

	private Integer formId;

	private Integer parentId;

	private String content;

	private Integer floor;

	private String createTime;

	private Integer userId;

	private Integer state;

	public Reply() {
	}

	public Reply(Integer replyId) {
		this.replyId = replyId;
	}
}