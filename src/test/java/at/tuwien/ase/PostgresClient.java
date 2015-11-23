package at.tuwien.ase;

import at.tuwien.ase.model.task.Task;
import at.tuwien.ase.dao.task.TaskDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by DanielHofer on 09.11.2015.
 */
public class PostgresClient {

    public static void main( String[] args )
    {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("src/main/webapp/WEB-INF/applicationContext.xml");

        //create new task
        TaskDAO taskDAO = (TaskDAO) context.getBean("taskDAO");
        Task task = new Task("taskTitle", "taskDescription");
        task.setId(1);
        taskDAO.insertTask(task);

        //read task from db
        Task task1 = taskDAO.findByID(1);
        System.out.println(task1);
    }
}
