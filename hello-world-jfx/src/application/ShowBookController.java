package application;

import java.sql.SQLException;
import java.util.List;

import bookDTO.BookDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ShowBookController {

    @FXML
    private TextArea bookListTextArea;
    @FXML
    public Button closeButton;

    @FXML
    public void showBookList() throws SQLException {
        List<BookDto> books = LibraryManager.showBooksinListForm();
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (BookDto book : books) {
            sb.append(i).append("\n");
            sb.append("ISBNコード: ").append(book.getIsbn()).append("\n");
            sb.append("タイトル: ").append(book.getTitle()).append("\n");
            sb.append("作家名: ").append(book.getAuthors()).append("\n");
            sb.append("出版日: ").append(book.getPublishedDate()).append("\n");
            sb.append("\n");
            i++;
        }

        bookListTextArea.setText(sb.toString());

        if (bookListTextArea.getText().isEmpty()) {
            System.out.println("bookListTextArea is empty.");
        } else {
            System.out.println("bookListTextArea is not empty.");
        }
    }
    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
