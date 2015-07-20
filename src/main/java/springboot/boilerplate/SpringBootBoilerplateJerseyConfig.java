package springboot.boilerplate;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.springframework.context.annotation.Configuration;

/**
 * Makes Jersey connected to Swagger, and put all API endpoints under "/api" path.
 * Jersey 2.19 is override on pom.xml, providing last fixed issues
 * @author bruno
 *
 */
@Configuration
@ApplicationPath("/api")
public class SpringBootBoilerplateJerseyConfig extends ResourceConfig {

	public SpringBootBoilerplateJerseyConfig() {
		register(com.wordnik.swagger.jaxrs.listing.ApiListingResource.class);
		register(com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);
		register(com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON.class);
		register(com.wordnik.swagger.jaxrs.listing.ResourceListingProvider.class);

		// allows @RolesAllowed
		register(RolesAllowedDynamicFeature.class);
		
		// grants Basic Auth to users.
		register(SpringBootBoilerplateAuthorizationFilter.class);

		packages("springboot.boilerplate.v1.resources");
	}
}