package de.lygie.batch;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class DefaultResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String defaultMessage() {
        return "REST-Service ist erreichbar!";
    }
}