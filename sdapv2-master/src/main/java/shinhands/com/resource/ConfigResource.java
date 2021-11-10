package shinhands.com.resource;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// import com.redhat.labs.lodestar.service.ConfigService;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
@Path("/config")
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigResource.class);

    @Inject
    JsonWebToken jwt;

    // @Inject
    // ConfigService configService;

    // @GET
    // @SecurityRequirement(name = "jwt", scopes = {})
    // @APIResponses(value = { @APIResponse(responseCode = "401", description = "Missing or Invalid JWT"),
    // @APIResponse(responseCode = "200", description = "Configuration file data has been returned.") })
    // @Operation(summary = "Returns configuration file data from git.")
    // public Response fetchConfigData(@HeaderParam(value = "Accept-version") String apiVersion) {
    // LOGGER.debug("Config version requested {}", apiVersion);
    // return configService.getConfigData(apiVersion);
    // }

}
