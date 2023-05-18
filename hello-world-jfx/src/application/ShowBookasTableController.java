package application;

import bookDTO.BookDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ShowBookasTableController {
    @FXML
    public Button closeButton;

    public void showBookTable() throws SQLException {
        List<BookDto> books = LibraryManager.showBooksinListForm();
        StringBuilder sb = new StringBuilder();

    }



    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
