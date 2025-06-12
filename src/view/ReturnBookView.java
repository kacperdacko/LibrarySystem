package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Library;

public class ReturnBookView {

    private final Library library;

    public ReturnBookView(Library library) {
        this.library = library;
    }

    public void show(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        Label isbnLabel = new Label("ISBN książki:");
        TextField isbnField = new TextField();

        Label userIdLabel = new Label("ID użytkownika:");
        TextField userIdField = new TextField();

        Button returnButton = new Button("Zwróć książkę");
        Label messageLabel = new Label();

        returnButton.setOnAction(e -> {
            String isbn = isbnField.getText().trim();
            String userId = userIdField.getText().trim();

            boolean success = library.returnBookByInput(isbn, userId);
            if (success) {
                library.saveLoansToCSV("loans.csv");
                messageLabel.setText("✅ Książka została zwrócona.");
            } else {
                messageLabel.setText("❌ Nie znaleziono wypożyczenia dla podanych danych.");
            }
        });

        root.getChildren().addAll(isbnLabel, isbnField, userIdLabel, userIdField, returnButton, messageLabel);

        stage.setScene(new Scene(root, 400, 250));
        stage.setTitle("📘 Zwróć książkę");
        stage.show();
    }
}
