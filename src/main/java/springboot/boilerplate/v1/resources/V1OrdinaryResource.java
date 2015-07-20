package springboot.boilerplate.v1.resources;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;

import springboot.boilerplate.v1.services.V1OrdinaryService;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This is an ordinary API. All APIs are versioned and contained into /api path.
 * @author bruno
 *
 */
@Path("ordinary")
@Api(value = "ordinary", description = "Asks for an ordinary waste of CPU")
public class V1OrdinaryResource {

	@Autowired
	V1OrdinaryService ordinaryService;
	
	public static class V1OrdinaryResponse {
		public V1OrdinaryResponse() {};
		public String result;
	}
	
	@GET
//	@RolesAllowed("SOME_ROLE")
	@PermitAll
	@ApiOperation(value = "GET", notes = "Ask for waste o CPUPost ", response = V1OrdinaryResponse.class)
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 500, message = "Internal Server Error") 
	})
	public V1OrdinaryResponse request(@QueryParam("req") String req) throws Exception {
		
		V1OrdinaryResponse response = new V1OrdinaryResponse();
		
		if (!ordinaryService.processRequest(req)) {
			throw new RuntimeException("Error processing request");
		}

		response.result = "ok";
		return response;
	}
}
