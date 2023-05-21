package application;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainController {
    private LibraryManager libraryManager = new LibraryManager();

    public void showBook(ActionEvent AE) throws ClassNotFoundException, SQLException, IOException {
        // LibraryManager.SearchBook();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxmlfile_collection/ShowBookList.fxml"));
        Parent showBookParent = fxmlLoader.load();
        Scene showBookScene = new Scene(showBookParent);
//        // Get the stage information
//        Stage window = (Stage)((Node)AE.getSource()).getScene().getWindow();
//        window.setScene(showBookScene);
//        window.show();

        // Create new stage (window)
        Stage window = new Stage();
        window.setScene(showBookScene);
        window.show();

    }
    public void showBook_asTable(){

    }
    public void deleteBook(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        LibraryManager.DeleteBook();
    }

    public void searchFromGoogle(ActionEvent actionEvent) throws ClassNotFoundException, SQLException, IOException {
        //  LibraryManager.getMultipleBooks();
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void showBookasTable(ActionEvent actionEvent) throws IOException {
        System.out.println("hi");
        // LibraryManager.SearchBook();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxmlfile_collection/ShowBookasTable.fxml"));
        Parent showBookParent = fxmlLoader.load();
        Scene showBookScene = new Scene(showBookParent);
//        // Get the stage information
//        Stage window = (Stage)((Node)AE.getSource()).getScene().getWindow();
//        window.setScene(showBookScene);
//        window.show();

        // Create new stage (window)
        Stage window = new Stage();
        window.setScene(showBookScene);
        window.show();
    }
}
