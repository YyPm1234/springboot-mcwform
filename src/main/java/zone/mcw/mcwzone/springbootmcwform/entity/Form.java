package zone.mcw.mcwzone.springbootmcwform.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Form {
	private Integer formId;

	private Integer moduleId;

	private Integer userId;

	private String formTitle;

	private String createTime;

	private Integer state;

	public Form(Integer formId) {
		this.formId = formId;
	}

	public Form() {
	}
}