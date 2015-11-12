package at.tuwien.ase.controller;

import at.tuwien.ase.dao.task.TaskDAO;
import at.tuwien.ase.model.task.Task;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

/**
 * Created by DanielHofer on 12.11.2015.
 */

@RestController
public class TaskController {

    @RequestMapping(value = "/task/{id}/{title}/{description}", method = RequestMethod.GET)
    public @ResponseBody Task test(@PathVariable("id") int id, @PathVariable("title") String title, @PathVariable("description") String description) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        TaskDAO taskDAO = (TaskDAO) context.getBean("taskDAO");

        //create new task
        Task task = new Task(id, title, description);

        //db insert
        taskDAO.insertTask(task);

        return task;
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public @ResponseBody Task task(@PathVariable("id") int id) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        TaskDAO taskDAO = (TaskDAO) context.getBean("taskDAO");

        //find task by id
        Task task = taskDAO.findByTaskId(id);

        return task;
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody Task task(@RequestBody Task task) {

        Task t = task;

        return t;
    }

}
