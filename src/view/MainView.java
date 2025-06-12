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


        int width = 300;
        int height = 60;

        btnShowAll.setPrefSize(width, height);
        btnBorrow.setPrefSize(width, height);
        btnReturn.setPrefSize(width, height);
        btnSearch.setPrefSize(width, height);

        String buttonStyle = "-fx-font-size: 18px;";
        btnShowAll.setStyle(buttonStyle);
        btnBorrow.setStyle(buttonStyle);
        btnReturn.setStyle(buttonStyle);
        btnSearch.setStyle(buttonStyle);

        // Akcje
        btnShowAll.setOnAction(e -> new BookListView(library).show(new Stage()));
        btnBorrow.setOnAction(e -> new BorrowBookView(library).show(new Stage()));
        btnReturn.setOnAction(e -> new ReturnBookView(library).show(new Stage()));
        btnSearch.setOnAction(e -> new SearchBookView(library).show(new Stage()));

        // Layout
        VBox root = new VBox(40, btnShowAll, btnBorrow, btnReturn, btnSearch);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(root, 800, 700);
        primaryStage.setTitle("📚 System Biblioteczny");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
