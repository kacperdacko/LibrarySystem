package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Book;
import model.Library;

public class BookListView {

    private final Library library;

    public BookListView(Library library) {
        this.library = library;
    }

    public void show(Stage primaryStage) {
        primaryStage.setTitle("📚 Lista książek");

        TableView<Book> table = new TableView<>();

        // Kolumna: Tytuł
        TableColumn<Book, String> titleCol = new TableColumn<>("Tytuł");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));

        // Kolumna: Autor
        TableColumn<Book, String> authorCol = new TableColumn<>("Autor");
        authorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAuthor()));

        // Kolumna: ISBN
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIsbn()));

        table.getColumns().addAll(titleCol, authorCol, isbnCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList<Book> books = FXCollections.observableArrayList(library.getBooks());
        table.setItems(books);

        Button backButton = new Button("⬅ Powrót do menu");
        backButton.setOnAction(e -> {
            MainView mainView = new MainView(library);
            mainView.show(primaryStage);
        });

        VBox vbox = new VBox(10, table, backButton);
        vbox.setPadding(new Insets(10));

        BorderPane root = new BorderPane(vbox);
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
