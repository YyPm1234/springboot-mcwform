package zone.mcw.mcwzone.springbootmcwform.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Modular {
	private Integer modularId;

	private String modularName;

	private String createTime;

	private Integer state;

	public Modular(Integer modularId) {
		this.modularId = modularId;
	}

	public Modular() {
	}
}