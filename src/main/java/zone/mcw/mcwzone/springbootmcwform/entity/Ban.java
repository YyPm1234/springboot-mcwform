package zone.mcw.mcwzone.springbootmcwform.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ban {
	private Integer banId;

	private Integer userId;

	private Integer adminId;

	private String unbanTime;

	private String info;

	public Ban(Integer banId) {
		this.banId = banId;
	}

	public Ban() {
	}
}