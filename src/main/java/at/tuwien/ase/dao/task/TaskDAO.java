package at.tuwien.ase.dao.task;

import at.tuwien.ase.model.task.Task;

/**
 * Created by DanielHofer on 09.11.2015.
 */

public interface TaskDAO
{
    /**
     * used to insert new Task objects to db
     *
     * @param task
     * @return task id from db
     */
    public int insertTask(Task task);

    /**
     * used to find task by id
     *
     * @param taskId
     * @return task object
     */
    public Task findByTaskId(int taskId);

}
