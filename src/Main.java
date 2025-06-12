import model.*;
import model.exceptions.BookNotFoundException;

import java.util.List;
import java.util.Scanner;

import static model.Library.printAllBooks;


public class Main {
    private static final String booksFile = "books.csv";
    private static final String usersFile = "users.csv";
    private static final String loansFile = "loans.csv";
    private static final Library library = new Library();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadInitialData();
        showMainMenu();
        saveAllData();
    }

    private static void loadInitialData() {
        library.loadBooksFromCSV(booksFile);
        library.loadUsersFromCSV(usersFile);
        library.loadLoansFromCSV(loansFile);

        if (library.getBooks().isEmpty() || library.getUsers().isEmpty()) {
            System.out.println("🔧 Dodajemy dane testowe...");
            library.addBook(new Book("Wiedźmin", "Andrzej Sapkowski", "9788375780635"));
            library.addBook(new Book("Java. Podstawy", "Cay Horstmann", "9788328302343"));
            library.registerUser(new Reader("Jan Kowalski", "U001"));
            library.registerUser(new Librarian("Anna Nowak", "L001"));
        }
    }

    private static void showMainMenu() {
        while (true) {
            System.out.println("\n📚 MENU GŁÓWNE");
            System.out.println("1. Wyszukaj książkę");
            System.out.println("2. Wypożycz książkę");
            System.out.println("3. Zwróć książkę");
            System.out.println("4. Pokaż wszystkie książki");
            System.out.println("5. Wyjście");
            System.out.print("Wybierz opcję (1-5): ");


            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    showSearchMenu();
                    break;
                case "2":
                    handleBorrowing();
                    break;
                case "3":
                    handleReturn(); // NOWA METODA
                    break;
                case "4":
                    library.printAllBooks();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("❗ Nieprawidłowy wybór.");
            }

        }
    }

    private static void showSearchMenu() {
        System.out.println("\n🔍 WYSZUKIWANIE KSIĄŻEK");
        System.out.print("Szukaj po (1) tytule czy (2) autorze? Wybierz 1 lub 2: ");
        int option = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Wpisz szukaną frazę: ");
        String query = scanner.nextLine();

        List<Book> results = (option == 1)
                ? library.searchBooksByTitle(query)
                : library.searchBooksByAuthor(query);

        if (results.isEmpty()) {
            System.out.println("🔎 Nie znaleziono książek.");
        } else {
            System.out.println("🔎 Wyniki wyszukiwania:");
            results.forEach(book -> System.out.println("📘 " + book));
        }
    }

    private static void handleBorrowing() {
        while (true) {
            System.out.println("\n📚 WYPOŻYCZ KSIĄŻKĘ");
            System.out.print("Podaj ISBN książki (lub wpisz 'exit' aby wrócić do menu): ");
            String isbn = scanner.nextLine().trim();
            if (isbn.equalsIgnoreCase("exit")) break;

            System.out.print("Podaj ID użytkownika: ");
            String userId = scanner.nextLine().trim();

            try {
                boolean success = library.borrowBookByInput(isbn, userId);
                if (success) {
                    library.saveLoansToCSV(loansFile);
                }
            } catch (BookNotFoundException e) {
                System.out.println("❌ Błąd: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Nieoczekiwany błąd: " + e.getMessage());
            }
        }
    }
    private static void handleReturn() {
        System.out.println("\n📤 ZWRACANIE KSIĄŻKI");
        System.out.print("Podaj ISBN książki do zwrotu: ");
        String isbn = scanner.nextLine().trim();

        System.out.print("Podaj ID użytkownika: ");
        String userId = scanner.nextLine().trim();

        boolean success = library.returnBookByInput(isbn, userId);
        if (success) {
            library.saveLoansToCSV(loansFile);
            System.out.println("✅ Książka została zwrócona.");
        } else {
            System.out.println("❌ Nie znaleziono wypożyczenia dla podanych danych.");
        }
    }

    private static void saveAllData() {
        library.saveBooksToCSV(booksFile);
        library.saveUsersToCSV(usersFile);
        library.saveLoansToCSV(loansFile);
        System.out.println("✅ Dane zostały zapisane. Do zobaczenia!");
    }
}
