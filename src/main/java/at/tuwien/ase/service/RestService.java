package at.tuwien.ase.service;

import at.tuwien.ase.domain.task.ParentTask;

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
    public ParentTask produceJSON(@PathParam("title") String title ) {

        ParentTask st = new ParentTask(1, "title of task is '"+title+"'", "description of '"+title+"'");

        return st;
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consumeJSON( ParentTask student ) {

        String output = student.toString();

        return Response.status(200).entity(output).build();
    }

}