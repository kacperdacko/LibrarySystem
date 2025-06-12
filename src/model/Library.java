package model;

import model.exceptions.BookNotFoundException;

import java.io.*;
import java.util.*;

public class Library {
    private static List<Book> books = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();
    private List<Loan> loans = new ArrayList<>();

    // Książki
    public void addBook(Book book) {
        books.add(book);
    }

    public static List<Book> getBooks() {
        return books;
    }

    public static void printAllBooks() {
        System.out.println("\n📚 LISTA WSZYSTKICH KSIĄŻEK:");
        if (books.isEmpty()) {
            System.out.println("📭 Brak książek w bibliotece.");
        } else {
            books.forEach(book -> System.out.println("📘 " + book));
        }
    }

    public Book findBookByIsbn(String isbn) throws BookNotFoundException {
        return books.stream()
                .filter(book -> book.getIsbn().equalsIgnoreCase(isbn))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException("Książka o ISBN " + isbn + " nie została znaleziona."));
    }

    // Użytkownicy
    public void registerUser(User user) {
        users.put(user.getId(), user);
    }

    public Map<String, User> getUsers() {
        return users;
    }

    // Wypożyczenia
    public boolean borrowBookByInput(String isbn, String userId) throws BookNotFoundException {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("❌ Nie znaleziono użytkownika.");
            return false;
        }
        Book book = findBookByIsbn(isbn);
        loans.add(new Loan(book, user));
        return true;
    }


    public boolean returnBookByInput(String isbn, String userId) {
        return loans.removeIf(loan ->
                loan.getBook().getIsbn().equalsIgnoreCase(isbn) &&
                        loan.getUser().getId().equalsIgnoreCase(userId));
    }

 

    public void loadBooksFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    addBook(new Book(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                }
            }
            System.out.println("✅ Wczytano książki z pliku CSV.");
        } catch (IOException e) {
            System.out.println("⚠️ Błąd wczytywania CSV: " + e.getMessage());
        }
    }

    public void saveBooksToCSV(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                bw.write(book.getTitle() + "," + book.getAuthor() + "," + book.getIsbn());
                bw.newLine();
            }
            System.out.println("✅ Zapisano książki do pliku CSV.");
        } catch (IOException e) {
            System.out.println("❌ Błąd zapisu do CSV: " + e.getMessage());
        }
    }

    public void loadUsersFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String type = parts[0].trim();
                    String name = parts[1].trim();
                    String id = parts[2].trim();
                    registerUser("Reader".equalsIgnoreCase(type) ? new Reader(name, id) : new Librarian(name, id));
                }
            }
            System.out.println("✅ Wczytano użytkowników z pliku CSV.");
        } catch (IOException e) {
            System.out.println("⚠️ Błąd wczytywania użytkowników: " + e.getMessage());
        }
    }

    public void saveUsersToCSV(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users.values()) {
                String type = (user instanceof Reader) ? "Reader" : "Librarian";
                bw.write(type + "," + user.getName() + "," + user.getId());
                bw.newLine();
            }
            System.out.println("✅ Zapisano użytkowników do pliku CSV.");
        } catch (IOException e) {
            System.out.println("❌ Błąd zapisu użytkowników: " + e.getMessage());
        }
    }

    public void loadLoansFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String isbn = parts[0].trim();
                    String userId = parts[1].trim();
                    try {
                        borrowBookByInput(isbn, userId);
                    } catch (BookNotFoundException e) {
                        System.out.println("⚠️ Nie znaleziono książki o ISBN: " + isbn);
                    }
                }
            }
            System.out.println("✅ Wczytano wypożyczenia z pliku CSV.");
        } catch (IOException e) {
            System.out.println("⚠️ Błąd wczytywania pliku CSV: " + e.getMessage());
        }
    }

    public void saveLoansToCSV(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Loan loan : loans) {
                bw.write(loan.getBook().getIsbn() + "," + loan.getUser().getId());
                bw.newLine();
            }
            System.out.println("💾 Zapisano wypożyczenia do pliku CSV.");
        } catch (IOException e) {
            System.out.println("❌ Błąd zapisu wypożyczeń: " + e.getMessage());
        }
    }

    // Wyszukiwanie
    public List<Book> searchBooksByTitle(String titlePart) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(titlePart.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }

    public List<Book> searchBooksByAuthor(String authorPart) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(authorPart.toLowerCase())) {
                result.add(book);
            }
        }
        return result;
    }
}
