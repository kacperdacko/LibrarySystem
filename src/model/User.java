package model;
import java.io.Serializable;

public abstract class User implements Serializable {
    protected String name;
    protected String id;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public abstract void showRole();

    public String getName() { return name; }
    public String getId() { return id; }
}
