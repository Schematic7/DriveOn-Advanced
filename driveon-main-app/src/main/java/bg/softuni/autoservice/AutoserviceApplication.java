package bg.softuni.autoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AutoserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoserviceApplication.class, args);
	}

}
