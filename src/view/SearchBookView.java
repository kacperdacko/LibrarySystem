package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Book;
import model.Library;

import java.util.List;

public class SearchBookView {

    private final Library library;

    public SearchBookView(Library library) {
        this.library = library;
    }

    public void show(Stage stage) {
        Label label = new Label("🔍 Wpisz tytuł lub autora:");
        TextField searchField = new TextField();
        Button searchButton = new Button("Szukaj");

        TableView<Book> tableView = new TableView<>();
        ObservableList<Book> results = FXCollections.observableArrayList();

        TableColumn<Book, String> titleCol = new TableColumn<>("Tytuł");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Autor");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        tableView.getColumns().addAll(titleCol, authorCol, isbnCol);
        tableView.setItems(results);

        searchButton.setOnAction(e -> {
            String query = searchField.getText().trim().toLowerCase();
            List<Book> found = library.searchBooksByTitle(query);
            if (found.isEmpty()) {
                found = library.searchBooksByAuthor(query);
            }
            results.setAll(found);
        });

        VBox layout = new VBox(10, label, searchField, searchButton, tableView);
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 600, 400);
        stage.setTitle("🔍 Wyszukiwanie książek");
        stage.setScene(scene);
        stage.show();
    }
}
