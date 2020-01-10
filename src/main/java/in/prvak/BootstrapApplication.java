package in.prvak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.thasmai","in.prvak"})
public class BootstrapApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootstrapApplication.class, args);
	}

}

/*
 * http://www.technicalkeeda.com/spring-tutorials/spring-4-mongodb-repository-example
 * https://www.mongodb.org/
 * https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/
 * https://www.baeldung.com/spring-data-mongodb-tutorial
 * https://docs.mongodb.com/manual/mongo/
 */