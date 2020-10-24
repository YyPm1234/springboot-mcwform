package zone.mcw.mcwzone.springbootmcwform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("zone.mcw.mcwzone.springbootmcwform.dao")
public class SpringbootMcwformApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMcwformApplication.class, args);
	}

}
