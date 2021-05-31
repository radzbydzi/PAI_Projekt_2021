package pl.pai.pai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class PaiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaiApplication.class, args);
	}

}
