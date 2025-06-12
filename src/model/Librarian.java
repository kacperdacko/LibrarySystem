package model;

import java.io.Serializable;

public class Librarian extends User implements Serializable {
    public Librarian(String name, String id) {
        super(name, id);
    }

    @Override
    public void showRole() {
        System.out.println("Jestem bibliotekarzem.");
    }
}