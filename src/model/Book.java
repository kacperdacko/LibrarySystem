package model;
import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String isbn;
    private boolean isAvailable = true;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    // Przeciążenie konstruktora
    public Book(String isbn) {
        this("Nieznany tytuł", "Nieznany autor", isbn);
    }

    // Gettery i settery
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return title + " - " + author + " (ISBN: " + isbn + ")";
    }
}