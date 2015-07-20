package springboot.boilerplate;

import java.io.IOException;
import java.security.Principal;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.ContainerRequest;

/**
 * This is just a boilerplate. Segregate all those classes into different classes
 * to get this as much decoupled as possible.
 * 
 * @author bruno
 *
 */
@Provider
@PreMatching
public class SpringBootBoilerplateAuthorizationFilter implements ContainerRequestFilter {

	/**
	 * If the user is not found, reply with "WWW-Authenticate" response header, granting
	 * browsers to prompt logon form to user.
	 */
    public void filter(final ContainerRequestContext request) throws IOException {
        final APIUser user = authenticate(request);
     
        if (user != null)
        	request.setSecurityContext(new Authorizer(user));
        else
        	request.abortWith(
    			Response.status(Status.UNAUTHORIZED)
    				.header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"realm\"")
					.entity("Requires login.").build()
			);
    }

    /**
     * This authenticates data against the Basic Authentication method.
     * @param request
     * @return
     */
    private APIUser authenticate(final ContainerRequestContext request) {
        String authentication = request.getHeaderString(ContainerRequest.AUTHORIZATION);

        if (authentication != null) {
            authentication = authentication.replaceFirst("[Bb][Aa][Ss][Ii][Cc] ", "");
            final String[] values = Base64.decodeAsString(authentication).split(":");

            if (values != null && values.length == 2) {
                final String username = values[0];
                final String password = values[1];

                return getUser(username, password);
            }
        }

        return null;
    }
    
    public APIUser getUser(final String pUsername, final String pPassword) {
    	// Do some hard work here and search your user
    	return new APIUser("admin", "admin", "admin");
    }

	/**
	 * This is the Authorizer from SecurityContext, where you ensure roles 
	 * and a valid user. It's connectect to @AllowedRoles, @PermitAll and @DenyAll
	 * @author bruno
	 *
	 */
	public static class Authorizer implements SecurityContext {

	    private Principal principal = null;
	    private APIUser user = null;

	    public Authorizer(final APIUser user) {
	        this.user = user;
	        principal = new Principal() {

	            public String getName() {
	                return user != null ? user.getUsername() : null;
	            }
	        };
	    }

	    public Principal getUserPrincipal() {
	        return this.principal;
	    }

	    public boolean isUserInRole(final String role) {
	        return role.equals(this.user != null ? this.user.getRole() : "");
	    }

	    public boolean isSecure() {
	        return true;
	    }

	    public String getAuthenticationScheme() {
	        return SecurityContext.BASIC_AUTH;
	    }
	}
	
	
	/**
	 * This is the API user.
	 * @author bruno
	 *
	 */
	public static class APIUser {

	    private String username;
	    private String role;
	    private String password;

	    public APIUser(final String username, final String role, final String password) {
	        this.username = username;
	        this.role = role;
	        this.password = password;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(final String username) {
	        this.username = username;
	    }

	    public String getRole() {
	        return role;
	    }

	    public void setRole(final String role) {
	        this.role = role;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(final String password) {
	        this.password = password;
	    }

	    @Override
	    public String toString() {
	        return "User [username=" + username + ", role=" + role + "]";
	    }

	    @Override
	    public int hashCode() {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + (password == null ? 0 : password.hashCode());
	        result = prime * result + (role == null ? 0 : role.hashCode());
	        result = prime * result + (username == null ? 0 : username.hashCode());
	        return result;
	    }
	}
}