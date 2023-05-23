package application;

import java.sql.SQLException;
import java.util.List;

import bookDTO.BookDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ShowBookasTableController {
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
    private TableColumn<BookDto, String> timestampCol;

    @FXML
    public void initialize() throws SQLException {
        noCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("no"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("isbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("authors"));
        publishedDateCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("publishedDate"));
        timestampCol.setCellValueFactory(new PropertyValueFactory<BookDto, String>("timestamp"));

        showBookTable();

    }

    public void showBookTable() throws SQLException {
        List<BookDto> books = LibraryManager.showBooksinListForm();
        
        for (int i = 0; i < books.size(); i++) {
            books.get(i).setNo(i + 1);
        }
        
        bookTable.getItems().setAll(books);
    }

    @FXML
    public void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
