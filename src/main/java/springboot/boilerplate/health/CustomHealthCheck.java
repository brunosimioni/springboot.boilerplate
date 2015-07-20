package springboot.boilerplate.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * This is a pretty simple health check indicator. Customize as your needs.
 * Actuator will provide /health endpoint, aggregating this indicator
 * @author bruno
 *
 */
@Component
public class CustomHealthCheck implements HealthIndicator {

    public Health health() {
        int errorCode = check();
        if (errorCode != 1) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }
    
    private int check() {
    	// perform some specific health check
    	return 0;
    }

}