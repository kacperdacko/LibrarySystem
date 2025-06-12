package model;

import java.time.LocalDate;
import java.io.Serializable;

public class Loan implements Serializable {
    private Book book;
    private User user;
    private LocalDate date;
    private boolean returned = false;

    public Loan(Book book, User user) {
        this.book = book;
        this.user = user;
        this.date = LocalDate.now();
    }

    public Book getBook() { return book; }
    public User getUser() { return user; }


    @Override
    public String toString() {
        return book.getTitle() + " wypożyczona przez " + user.getName() +
                " dnia " + date + (returned ? " (zwrócona)" : "");
    }
}
