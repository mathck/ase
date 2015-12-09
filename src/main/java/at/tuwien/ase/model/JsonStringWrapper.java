package at.tuwien.ase.model;

/**
 * Created by DanielHofer on 09.12.2015.
 */
public class JsonStringWrapper {

    private String item;

    public JsonStringWrapper(){

    }

    public JsonStringWrapper(String item){
        this.item=item;
    }

    public JsonStringWrapper(int item){
        this.item=String.valueOf(item);
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
