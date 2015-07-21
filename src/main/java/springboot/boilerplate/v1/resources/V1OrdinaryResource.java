package springboot.boilerplate.v1.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springboot.boilerplate.v1.services.V1OrdinaryService;

/**
 * This is an ordinary API. All APIs are versioned and contained into /api path.
 * @author bruno
 *
 */
@Path("ordinary")
@Api(value = "ordinary")
public class V1OrdinaryResource {

    @Autowired
    V1OrdinaryService ordinaryService;

    public static class V1OrdinaryResponse {
        public V1OrdinaryResponse() {};
        public String result;
    }

	@GET
	@Path("/admin")
	@RolesAllowed("ROLE_ADMIN")
    @ApiOperation(value = "Ask for waste o CPUPost ")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK", response = V1OrdinaryResponse.class),
        @ApiResponse(code = 401, message = ""),
        @ApiResponse(code = 403, message = ""),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    })
	public V1OrdinaryResponse requestAsAdmin(@ApiParam(required=true) @QueryParam("req") @NotNull String req) throws Exception {
        return request(req);
    }

	@GET
	@PermitAll
	@ApiOperation(value = "Ask for waste o CPUPost ")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = {
		@ApiResponse(code = 200, message = "OK", response = V1OrdinaryResponse.class),
		@ApiResponse(code = 400, message = "Bad Request"),
		@ApiResponse(code = 500, message = "Internal Server Error")
	})
	public V1OrdinaryResponse request(@ApiParam(required=true) @QueryParam("req") @NotNull String req) throws Exception {

        final V1OrdinaryResponse response = new V1OrdinaryResponse();

        if (!ordinaryService.processRequest(req)) {
            throw new RuntimeException("Error processing request");
        }

        response.result = "ok";
        return response;
    }
}
