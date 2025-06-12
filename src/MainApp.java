import javafx.application.Application;
import javafx.stage.Stage;
import model.Library;
import view.MainView;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Library library = new Library();

        // Wczytywanie danych z CSV
        library.loadBooksFromCSV("books.csv");
        library.loadUsersFromCSV("users.csv");
        library.loadLoansFromCSV("loans.csv");

        MainView view = new MainView(library);
        view.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}