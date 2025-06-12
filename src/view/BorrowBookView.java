package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Library;
import model.exceptions.BookNotFoundException;

public class BorrowBookView {

    private final Library library;

    public BorrowBookView(Library library) {
        this.library = library;
    }

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label isbnLabel = new Label("ISBN książki:");
        TextField isbnField = new TextField();

        Label userIdLabel = new Label("ID użytkownika:");
        TextField userIdField = new TextField();

        Button borrowButton = new Button("Wypożycz");
        Label messageLabel = new Label();

        borrowButton.setOnAction(e -> {
            String isbn = isbnField.getText().trim();
            String userId = userIdField.getText().trim();

            try {
                boolean success = library.borrowBookByInput(isbn, userId);
                if (success) {
                    messageLabel.setText("✅ Książka została wypożyczona.");
                    library.saveLoansToCSV("loans.csv");
                } else {
                    messageLabel.setText("❌ Nie udało się wypożyczyć książki.");
                }
            } catch (BookNotFoundException ex) {
                messageLabel.setText("❌ Błąd: " + ex.getMessage());
            } catch (Exception ex) {
                messageLabel.setText("❌ Nieoczekiwany błąd: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(isbnLabel, isbnField, userIdLabel, userIdField, borrowButton, messageLabel);
        stage.setScene(new Scene(root, 400, 250));
        stage.setTitle("📕 Wypożycz książkę");
        stage.show();
    }
}
