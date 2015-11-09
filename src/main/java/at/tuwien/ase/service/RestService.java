package at.tuwien.ase.service;

import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.domain.task.Task;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * REST services
 *
 * Created by DanielHofer on 04.11.2015.
 */

@Path("/")
public class RestService {


    @GET
    @Path("/task/{id}/{title}/{description}")
    @Produces(MediaType.APPLICATION_JSON)
    public Task produceJSON(@PathParam("id") int id, @PathParam("title") String title, @PathParam("description") String description) {

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        TaskDAO taskDAO = (TaskDAO) context.getBean("taskDAO");

        //create new task
        Task task = new Task(id, title, description);

        //db insert
        taskDAO.insertTask(task);

        return task;
    }

    @GET
    @Path("/task/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Task produceJSON(@PathParam("id") int id) {

        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        TaskDAO taskDAO = (TaskDAO) context.getBean("taskDAO");

        //find task by id
        Task task = taskDAO.findByTaskId(id);

        return task;
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response consumeJSON(Task task) {

        String output = task.toString();

        return Response.status(200).entity(output).build();
    }

}