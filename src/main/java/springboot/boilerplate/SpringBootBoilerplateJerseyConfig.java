package springboot.boilerplate;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.jaxrs.config.BeanConfig;

/**
 * Makes Jersey connected to Swagger, and put all API endpoints under "/api"
 * path. Jersey 2.19 is override on pom.xml, providing last fixed issues
 *
 * @author bruno
 *
 */
@Configuration
@ApplicationPath("/api")
public class SpringBootBoilerplateJerseyConfig extends ResourceConfig {

    private static final String RESOURCE_PACKAGE = "springboot.boilerplate.v1.resources";

    public SpringBootBoilerplateJerseyConfig() {

        // Swagger
        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        // allows @RolesAllowed
        register(RolesAllowedDynamicFeature.class);

        packages(RESOURCE_PACKAGE);
    }

    @Bean
    public BeanConfig swaggerConfig() {
        final BeanConfig swaggerConfig = new BeanConfig();
        swaggerConfig.setBasePath("/api");
        swaggerConfig.setVersion("1.0.0");
        swaggerConfig.setTitle("Boiler Plate API");
        swaggerConfig.setResourcePackage(RESOURCE_PACKAGE);
        swaggerConfig.setScan(true);
        return swaggerConfig;
    }
}
