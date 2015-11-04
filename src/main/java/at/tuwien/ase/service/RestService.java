package at.tuwien.ase.service;

import at.tuwien.ase.domain.Task;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Class containing all REST services
 *
 * @author Daniel Hofer
 */

@Path("/jsonServices")
public class RestService {

    @GET
    @Path("/print/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Task produceJSON(@PathParam("title") String title ) {

        Task st = new Task(1, "title of task is '"+title+"'", "description of '"+title+"'");

        return st;
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consumeJSON( Task student ) {

        String output = student.toString();

        return Response.status(200).entity(output).build();
    }

}