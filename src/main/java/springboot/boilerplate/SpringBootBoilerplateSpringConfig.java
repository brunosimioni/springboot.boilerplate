package springboot.boilerplate;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

/**
 * Externalize both static and dynamic configuration into a file not
 * self-contained. This must provides an nice way to get your software deployed
 * into different environments (prod, QA, staging, etc).
 *
 * Dynamic configuration is provided via Netflix Archaius
 *
 * @author bruno
 *
 */
@Configuration
@PropertySources({ @PropertySource("classpath:default.properties"),
        @PropertySource(value = "file:${external.config}", ignoreResourceNotFound = true) })
@Import(SpringBootBoilerplateCORSConfig.class)
public class SpringBootBoilerplateSpringConfig implements InitializingBean {

    @Autowired
    private Environment env;

    /**
     * Netflix Archaius provides dynamic configuration.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
//        System.setProperty("archaius.configurationSource.additionalUrls",
//                env.getProperty("archaius.config"));
    }
}
