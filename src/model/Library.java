package model;

import model.exceptions.BookNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.util.Scanner;

public class Library {
    public static List<Book> books = new ArrayList<>();
    private Map<String, User> users = new HashMap<>();
    private List<Loan> loans = new ArrayList<>();

    // Dodaj książkę do biblioteki
    public void addBook(Book book) {
        books.add(book);
    }

    // Dodaj użytkownika (czytelnika lub bibliotekarza)
    public void registerUser(User user) {
        users.put(user.getId(), user);
    }

    // Wyszukaj książkę po ISBN
    public Book findBookByIsbn(String isbn) throws BookNotFoundException {
        for (Book b : books) {
            if (b.getIsbn().equals(isbn)) {
                return b;
            }
        }
        throw new BookNotFoundException("Książka o ISBN " + isbn + " nie została znaleziona.");
    }

    // Wypożycz książkę
    public boolean borrowBookByInput(String isbn, String userId) throws BookNotFoundException {
        User user = users.get(userId);
        if (user == null) {
            System.out.println("❌ Nie znaleziono użytkownika.");
            return false;
        }

        Book book = findBookByIsbn(isbn);

        // Wyszukaj książkę po ISBN
        Book found = null;
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                found = b;
                break;
            }
        }

        if (found == null) {
            System.out.println("❌ Nie znaleziono książki o ISBN: " + isbn);
            return false;
        }




        // Wypożyczenie
        Loan loan = new Loan(book, user);
        loans.add(loan);
        return true;
    }

    public void borrowAndSave(String isbn, String userId, String loansFile) {
        try {
            if (borrowBookByInput(isbn, userId)) {
                saveLoansToCSV(loansFile);
            }
        } catch (BookNotFoundException e) {
            System.out.println("📕 Nie znaleziono książki: " + e.getMessage());
        }
    }

    public boolean returnBookByInput(String isbn, String userId) {
        return loans.removeIf(loan ->
                loan.getBook().getIsbn().equalsIgnoreCase(isbn)
                        && loan.getUser().getId().equalsIgnoreCase(userId)
        );
    }


    public static List<Book> getBooks() {
        return books;
    }

    public static void printAllBooks() {
        System.out.println("\n📚 LISTA WSZYSTKICH KSIĄŻEK:");
        List<Book> books = getBooks();
        if (books.isEmpty()) {
            System.out.println("📭 Brak książek w bibliotece.");
        } else {
            books.forEach(book -> System.out.println("📘 " + book));
        }
    }
    public List<Loan> getActiveLoans() {
        List<Loan> active = new ArrayList<>();
        for (Loan loan : loans) {
            if (!loan.isReturned()) {
                active.add(loan);
            }
        }
        return active;
    }

    public void saveToFile(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(books);
            oos.writeObject(users);
            oos.writeObject(loans);
            System.out.println("✅ Dane zapisane do pliku.");
        } catch (IOException e) {
            System.out.println("❌ Błąd zapisu: " + e.getMessage());
        }
    }

    public void loadFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            books = (List<Book>) ois.readObject();
            users = (Map<String, User>) ois.readObject();
            loans = (List<Loan>) ois.readObject();
            System.out.println("✅ Dane wczytane z pliku.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Błąd odczytu: " + e.getMessage());
        }
    }
    public void loadBooksFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();
                    String isbn = parts[2].trim();
                    addBook(new Book(title, author, isbn));
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
    public Map<String, User> getUsers() {
        return users;
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

                    if (type.equalsIgnoreCase("Reader")) {
                        registerUser(new Reader(name, id));
                    } else if (type.equalsIgnoreCase("Librarian")) {
                        registerUser(new Librarian(name, id));
                    }
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
                        borrowBookByInput(isbn, userId); // próba wypożyczenia książki
                    } catch (BookNotFoundException e) {
                        System.out.println("⚠️ Nie znaleziono książki o ISBN: " + isbn + ". " + e.getMessage());
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

    public static void main(String[] args) {
        // Tworzymy bibliotekę
        Library library = new Library();

        // ... (poprzedni kod bez zmian) ...

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("🔍 WYSZUKIWANIE KSIĄŻEK");
            System.out.print("Szukaj po (1) tytule czy (2) autorze? Wybierz 1 lub 2: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.print("Wpisz szukaną frazę: ");
            String query = scanner.nextLine();

            List<Book> results;
            if (option == 1) {
                results = library.searchBooksByTitle(query);
            } else {
                results = library.searchBooksByAuthor(query);
            }

            if (results.isEmpty()) {
                System.out.println("🔎 Nie znaleziono książek.");
            } else {
                System.out.println("🔎 Wyniki wyszukiwania:");
                for (Book b : results) {
                    System.out.println("📘 " + b);
                }
            }

            System.out.println("\n📚 WYPOSAŻENIE W KSIĄŻKI");
            System.out.print("Podaj ISBN książki do wypożyczenia: ");
            String isbn = scanner.nextLine();
            System.out.print("Podaj ID użytkownika: ");
            String userId = scanner.nextLine();
            
            try {
                library.borrowBookByInput(isbn, userId);
                System.out.println("✅ Książka została wypożyczona pomyślnie.");
            } catch (BookNotFoundException e) {
                System.out.println("❌ Błąd: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Wystąpił nieoczekiwany błąd: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("❌ Wystąpił błąd: " + e.getMessage());
        }

        // Załadowanie/zapisanie danych z plików z obsługą błędów
        try {
            String booksFile = "books.csv";
            String usersFile = "users.csv";
            String loansFile = "loans.csv";

            library.loadBooksFromCSV(booksFile);
            library.loadUsersFromCSV(usersFile);
            library.loadLoansFromCSV(loansFile);

            // Jeśli dane są puste, dodaj przykładowe
            if (library.getBooks().isEmpty() || library.getUsers().isEmpty()) {
                System.out.println("🔧 Dodajemy dane testowe...");
                library.addBook(new Book("Wiedźmin", "Andrzej Sapkowski", "9788375780635"));
                library.addBook(new Book("Java. Podstawy", "Cay Horstmann", "9788328302343"));
                library.registerUser(new Reader("Jan Kowalski", "U001"));
                library.registerUser(new Librarian("Anna Nowak", "L001"));
            }

            // Wyświetl książki (usunieto duplikat wyświetlania)
            System.out.println("\n📚 Książki:");
            for (Book book : library.getBooks()) {
                System.out.println(book);
            }

            // Zapisz dane
            library.saveBooksToCSV(booksFile);
            library.saveUsersToCSV(usersFile);
            library.saveLoansToCSV(loansFile);

        } catch (Exception e) {
            System.out.println("❌ Błąd podczas operacji na plikach: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}