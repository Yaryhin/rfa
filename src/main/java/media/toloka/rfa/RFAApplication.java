package media.toloka.rfa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RFAApplication {
	public static void main(String[] args) {
		SpringApplication.run(RFAApplication.class, args);
	}
	// https://www.marcobehler.com/guides/spring-security
}
