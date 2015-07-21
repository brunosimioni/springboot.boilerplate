package springboot.boilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootBoilerplateApplication {

	/**
	 * Here's the application's entry point.
	 * 
	 * Swagger json: http://localhost:8080/api/swagger.json
	 * Swagger yaml: http://localhost:8080/api/swagger.yaml
	 * Swagger UI: http://localhost:8080/swagger/index.html
	 * Metrics: http://localhost:8080/metrics
	 * Health: http://localhost:8080/health
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        SpringApplication.run(SpringBootBoilerplateApplication.class, args);
    }
}
