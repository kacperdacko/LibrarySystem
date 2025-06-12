package model;

import java.io.Serializable;

public class Reader extends User implements Serializable {
    public Reader(String name, String id) {
        super(name, id);
    }

    @Override
    public void showRole() {
        System.out.println("Jestem czytelnikiem.");
    }
}
