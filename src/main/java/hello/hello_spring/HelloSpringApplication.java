package hello.hello_spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.oconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloSpringApplication.class, args);
	}

	//실행시키고 접속 to where? -> http://localhost:8080/

}
