package application;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Util.DbUtil;
import bookDAO.GoogleBooksDao;
import bookDTO.BookDto;
import bookPrintout.BookPrinter;
import urlconnection.ConnUtil;


public class LibraryManager {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("システム開始");
        while (true) {
            try {
            	 new LibraryManager();
				LibraryManager.start();start();
            } catch (Exception e) {
                System.out.println("どこかでエラー");
                e.printStackTrace();
            }
        }
    }
    public static  boolean isISBNValid(String isbn) {
        if (isbn.length() != 13) {
        	String flg = "ISBNのコードは数字の１３桁です";
        	System.out.println(flg);
            return false;
        }
        if (!isbn.startsWith("978") && !isbn.startsWith("979")) {
        	
        	String flg = "ISBNコードではありません、数字であるか確認してください";
        	System.out.println(flg);
            return false;
        }
        return true;
    }
    
    public static void start() throws ClassNotFoundException, SQLException, IOException {
        System.out.println();
        System.out.println("1:本の一覧　2:追加　3:本の削除　4:Googleから本を探す　0:終了");
        String ope = sc.nextLine();
        switch (ope) {
            case "1":
                showBooks();
                break;
            case "2":
                SearchBook();
                break;
            case "3":
            	DeleteBook();
                break;
            case "4":
            	getMultipleBooks();
            	break;

            case "0":
                System.out.println("システムを終了します");
                System.exit(0);
            default:
                System.out.println("指定の番号を入力してください");
                break;
        }
    }

    public static void showBooks() throws SQLException {
        Connection con = null;
        GoogleBooksDao booksDao = null;
        System.out.println("1:１個づつ　2:テーブル型　0:終了");
        String ope = sc.nextLine();

        try {
             con = DbUtil.getConnection(); //Establish connection
             booksDao = new GoogleBooksDao(con); //Pass connection to Dao

            List<BookDto> books = booksDao.selectAll(); //Call selectAll method from DAO
         //   
            switch (ope) {
            case "1":
            	BookPrinter.printBookData(books);
                break;
            case "2":
            	BookPrinter.printBookData_intableform(books);
                break;
            case "0":
                System.out.println("システムを終了します");
                System.exit(0);
            default:
                System.out.println("指定の番号を入力してください");
                break;
        }
            
            

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            DbUtil.closeConnection(con); // Close connection

        }
    }
    public static void SearchBook() throws SQLException, ClassNotFoundException {
        System.out.println("ISBNコードを入力してください");
        Connection con = DbUtil.getConnection();
       
        //API link from google
        String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
        
        //example
        //https://www.googleapis.com/books/v1/volumes?q=isbn:9784844336778

        try {
            String query = sc.nextLine();
            if (!isISBNValid(query)) {
                return;
            }
            
            // Send GET request and retrieve response
            String jsonResponse = ConnUtil.sendGetRequest(apiUrl + query);
            GoogleBooksDao booksDao = new GoogleBooksDao(con);
            // Parse JSON and retrieve book data
            List<BookDto> books = booksDao.parseJsonResponse(jsonResponse);
            // Print book data
            if (!books.isEmpty()) {
                BookDto book = books.get(0); // Get the first book
                System.out.println("ISBNコード		: " + book.getIsbn());
                System.out.println("タイトル		: " + book.getTitle());
                System.out.println("作家名			: " + book.getAuthors());
                System.out.println("出版日			: " + book.getPublishedDate());
                System.out.println("概要			: " + book.getDescription());
                System.out.println();
                
                
                //since book.getisbn is string, need to parse this into long, cannot int coz too long
                long isbnLong = Long.parseLong(book.getIsbn());
                List<BookDto> existingBooks = booksDao.selectbyISBN(isbnLong);
                
                if (!existingBooks.isEmpty()) {
                    System.out.println("もうすでに保存済みです");
                } else {
                    System.out.println("こちらの本を登録しますか (Y/N)");
                    String answer = sc.nextLine();
                    if (answer.equalsIgnoreCase("y")) {
                        // Save book to database
                        booksDao.saveBookToDatabase(book);
                    }else {
                    	System.out.println("キャンセルしました");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            DbUtil.closeConnection(con); // Close connection
        }
    }

    public static void DeleteBook() throws ClassNotFoundException, SQLException {
    	 Connection con = null;
        try {
        	con = DbUtil.getConnection();

        	 System.out.println("ISBNコードを入力してください");
            String isbncode = sc.nextLine();
            if (!isISBNValid(isbncode)) {
                return;
            }
            
            GoogleBooksDao booksDao = new GoogleBooksDao(con);

            // String to Long
            long isbnLong = Long.parseLong(isbncode);

            // Check if the book exists in the database before deletion
            List<BookDto> existingBooks = booksDao.selectbyISBN(isbnLong);
            
            if (existingBooks.isEmpty()) {
                System.out.println("データベースには保存されておりません");
            } else {
                System.out.println("こちらの本を「削除」でよろしいですか(Y/N)");
                String answer = sc.nextLine();
                if (answer.equalsIgnoreCase("y")) {
                    booksDao.Delete_bookDAO(isbnLong);
                } else {
                    System.out.println("キャンセルしました");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            DbUtil.closeConnection(con); // Close connection

        }
    }
    public static void getMultipleBooks() throws ClassNotFoundException, SQLException, IOException {
    	// System.out.println("探したい本の名前を入力してください");
    	 Connection con = DbUtil.getConnection();
    	 
        
         //API link from google
    	 //since this is looking for multiple data
    	 //maxResults=" + maxResults better add this one
         String apiUrl = "https://www.googleapis.com/books/v1/volumes?q=:";
         
         
    
         //example
         //https://www.googleapis.com/books/v1/volumes?q=isbn:9784844336778
        try {
        
        	 String query = "";
        	 //caring space
        	 do {
        		    System.out.println("探したい本の名前を入力してください");
        		    query = sc.nextLine();
        		} while (query.isEmpty());
        	 
        	 String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
             
             
             //for now, i have setup max result as 10 books
             String maxresult ="&maxResults=10";
             // Send GET request and retrieve response
             String jsonResponse = ConnUtil.sendGetRequest(apiUrl + encodedQuery+ maxresult);
             GoogleBooksDao booksDao = new GoogleBooksDao(con);
             // Parse JSON and retrieve book data
             List<BookDto> books = booksDao.parseJsonResponse(jsonResponse);
             // Print book data
             if(!books.isEmpty()) {
            	// System.out.println("hi, total data =" + books.size());
            	BookPrinter.printBookData_description(books);
            	 
             }else {
            	 System.out.println("nope");
             }
        }
        finally {
            DbUtil.closeConnection(con); // Close connection

        }
        
    }
    
    
    //created to use for GUI 
    
    public static List<BookDto> showBooksinListForm() throws SQLException {
        Connection con = null;
        GoogleBooksDao booksDao = null;
        List<BookDto> books = new ArrayList<>();
        try {
             con = DbUtil.getConnection(); //Establish connection
             booksDao = new GoogleBooksDao(con); //Pass connection to Dao
             books = booksDao.selectAll(); //Call selectAll method from DAOd
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            DbUtil.closeConnection(con); // Close connection
        }
        
        return books;
    }
    
    public static List<BookDto> showBooksinTableForm() throws SQLException {
        Connection con = null;
        GoogleBooksDao booksDao = null;
        List<BookDto> books = new ArrayList<>();
        try {
            con = DbUtil.getConnection(); //Establish connection
            booksDao = new GoogleBooksDao(con); //Pass connection to Dao
            books = booksDao.selectAll(); //Call selectAll method from DAOd
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            DbUtil.closeConnection(con); // Close connection
        }

        return books;
    }

    





}
