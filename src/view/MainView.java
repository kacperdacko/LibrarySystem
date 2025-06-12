package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Library;

public class MainView {

    private final Library library;

    public MainView(Library library) {
        this.library = library;
    }

    public void show(Stage primaryStage) {
        // Przyciski
        Button btnShowAll = new Button("📖 Pokaż wszystkie książki");
        Button btnBorrow = new Button("📕 Wypożycz książkę");
        Button btnReturn = new Button("📘 Zwróć książkę");
        Button btnSearch = new Button("🔍 Wyszukaj książkę");

        // Akcje
        btnShowAll.setOnAction(e -> new BookListView(library).show(new Stage()));
        btnBorrow.setOnAction(e -> new BorrowBookView(library).show(new Stage()));
        btnReturn.setOnAction(e -> new ReturnBookView(library).show(new Stage()));
        btnSearch.setOnAction(e -> new SearchBookView(library).show(new Stage()));

        // Layout
        VBox root = new VBox(15, btnShowAll, btnBorrow, btnReturn, btnSearch);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");

        // Scena i okno
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("📚 System Biblioteczny");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
