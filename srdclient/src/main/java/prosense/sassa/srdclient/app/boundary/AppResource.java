package prosense.sassa.srdclient.app.boundary;


import javax.ejb.Stateless;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import prosense.sassa.srdclient.api.entity.AppException;

@Stateless
@Path("/")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")

public class AppResource {
    @GET
    public void read(@Context UriInfo uriInfo) {
        throw AppException.builder()
                          .methodNotAllowed405()
                          .message("Base uri call not allowed")
                          .build();
    }
}
