package at.tuwien.ase.services;

import at.tuwien.ase.model.task.Issue;
import at.tuwien.ase.model.task.Task;

/**
 * Created by DanielHofer on 16.16.11.2015.
 */
public interface TaskService {


    /**
     * get task by id
     *
     * @param taskId
     * @return created task
     */
    public Task getTask(int taskId);

    /**
     * create new task
     *
     * @param task
     * @return created task
     */
    public Task postTask(Task task);

}




