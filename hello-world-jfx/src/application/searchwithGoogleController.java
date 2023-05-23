package application;
import java.sql.SQLException;
import java.util.List;

import bookDTO.BookDto;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import static application.LibraryManager.getMultipleBooks;

public class searchwithGoogleController {
    @FXML
    private Button closeButton;

    @FXML
    private TableView<BookDto> bookTable;

    @FXML
    private TableColumn<BookDto, String> noCol;

    @FXML
    private TableColumn<BookDto, String> isbnCol;

    @FXML
    private TableColumn<BookDto, String> titleCol;

    @FXML
    private TableColumn<BookDto, String> authorCol;

    @FXML
    private TableColumn<BookDto, String> publishedDateCol;

    @FXML
    private TableColumn<BookDto, String> Description;


    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    //text and button part
    @FXML
    private TextField queryField;

    @FXML
    private void handleSearchButtonAction(ActionEvent event) {
        String query = queryField.getText();
        try {
            List<BookDto> books = getMultipleBooks(query);  // save the returned list into a variable
            showBookTable(books);  // pass it to your showBookTable method
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //table
    @FXML
    public void initialize() {
        noCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("no"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("isbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("authors"));
        publishedDateCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("publishedDate"));
        Description.setCellValueFactory(new PropertyValueFactory<BookDto, String>("description"));


        noCol.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.1));
        isbnCol.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.15));
        titleCol.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.2));
        authorCol.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.2));
        publishedDateCol.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.2));
        Description.prefWidthProperty().bind(bookTable.widthProperty().multiply(0.15));


       // Platform.runLater(() -> queryField.requestFocus());

        // Set the placeholder text
        queryField.setPromptText("Enter book name");
    }
    public void showBookTable(List<BookDto> books) throws SQLException {
        for (int i = 0; i < books.size(); i++) {
            books.get(i).setNo(i + 1);
        }

        bookTable.getItems().setAll(books);
    }

}
