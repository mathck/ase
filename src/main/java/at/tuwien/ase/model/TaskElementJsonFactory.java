package at.tuwien.ase.model;

import at.tuwien.ase.model.javax.TaskElement;

/**
 * Created by DanielHofer on 22.12.2015.
 */
public class TaskElementJsonFactory {

        public static TaskElementJson getTaskElement(TaskElement javaxTaskElement){

            TaskElementJson taskElementJson = new TaskElementJson();

            if (javaxTaskElement.getId() != null){
                taskElementJson.setItemId(javaxTaskElement.getId().intValue());
            }
            if (javaxTaskElement.getStatus() != null){
                taskElementJson.setStatus(javaxTaskElement.getStatus());
            }
            if (javaxTaskElement.getValue() != null){
                taskElementJson.setValue(javaxTaskElement.getValue());
            }
            if (javaxTaskElement.getLink() != null){
                taskElementJson.setLink(javaxTaskElement.getLink());
            }
            if (javaxTaskElement.getType() != null){
                taskElementJson.setItemType(javaxTaskElement.getType().value());
            }

            if (javaxTaskElement.getSolution() != null){
                taskElementJson.setSolution(javaxTaskElement.getSolution());
            }


            return taskElementJson;
        }


}
