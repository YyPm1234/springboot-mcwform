package zone.mcw.mcwzone.springbootmcwform.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户信息表
 */
@Getter
@Setter
@ToString
public class User {
	private Integer userId;

	private String username;

	private String password;

	private String createTime;

	private String lastLoginTime;

	private Integer admin;

	private String mail;

	private String phone;

	private String icon;

	public User() {
	}

	public User(Integer userId) {
		this.userId = userId;
	}
}