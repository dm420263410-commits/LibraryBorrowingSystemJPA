package controllers;

import dao.BookDAO;
import dao.BorrowDAO;
import dao.StudentDAO;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Borrow;

public class BorrowController implements Initializable {

    @FXML
    private ComboBox<Integer> booksCombobox;
    @FXML
    private ComboBox<Integer> studentsCombobox;
    @FXML
    private DatePicker borrowDate;
    @FXML
    private DatePicker returnDate;
    @FXML
    private CheckBox status;
    @FXML
    private TableView<Borrow> table;
    @FXML
    private TableColumn<Borrow, Integer> borrowIdTC;
    @FXML
    private TableColumn<Borrow, Integer> bookIdTC;
    @FXML
    private TableColumn<Borrow, Integer> studentIdTC;
    @FXML
    private TableColumn<Borrow, LocalDate> borrowDateTC;
    @FXML
    private TableColumn<Borrow, LocalDate> returnDateTC;
    @FXML
    private TableColumn<Borrow, Boolean> statusTC;
    
    BookDAO bookdao = new BookDAO();
    StudentDAO studentdao = new StudentDAO();
    BorrowDAO borrowdao = new BorrowDAO();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borrowIdTC.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        bookIdTC.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        studentIdTC.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        borrowDateTC.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateTC.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusTC.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        List<Integer> ids = bookdao.getAllBooksIds();
        booksCombobox.getItems().addAll(ids);
        
        List<Integer> student_ids = studentdao.getAllStudentsIds();
        studentsCombobox.getItems().addAll(student_ids);
        
        table.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue == null) return;
                booksCombobox.setValue(newValue.getBookId());
                studentsCombobox.setValue(newValue.getStudentId());
                borrowDate.setValue(newValue.getBorrowDate());
                LocalDate rd = newValue.getReturnDate();
                if (rd != null) {
                    returnDate.setValue(rd);
                } else {
                    returnDate.setValue(null);
                }
                status.setSelected(newValue.getStatus());
            }
        );
    }    

    @FXML
    private void viewHandle(ActionEvent event) {
        List<Borrow> borrows = borrowdao.findAll();
        table.getItems().setAll(borrows);
        showInfoAlert("View All", "Total records: " + borrows.size());
    }

    @FXML
    private void borrowHandle(ActionEvent event) {
        if (borrowValidor()) {
            Integer book_id = booksCombobox.getValue();
            Integer student_id = studentsCombobox.getValue();
            LocalDate bd = borrowDate.getValue();
            
            Borrow b = new Borrow();
            b.setStudentId(student_id);
            b.setBookId(book_id);
            b.setBorrowDate(bd);
            
            boolean success = borrowdao.insertOne(b);
            if (success) {
                clear();
                viewHandle(event);
                showInfoAlert("Success", "Borrow Added Successfully");
            } else {
                showWarningAlert("Error", "Failed to Add", "Please check if IDs exist");
            }
        } else {
            showWarningAlert(
                "Invalid input",
                "Missing Data",
                "Please select book id, student id and borrow date"
            );
        }
    }

    @FXML
    private void returnHandle(ActionEvent event) {   
        Borrow b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            showWarningAlert("No Selection", "No Record Selected",
                    "Please select a borrow record from the table");
        } else {
            if (returnDate.getValue() == null || !status.isSelected()) {
                showWarningAlert("Invalid Input", "Missing data", 
                        "Please select both return date and status");
            } else {
                b.setReturnDate(returnDate.getValue());
                b.setStatus(status.isSelected());
                boolean success = borrowdao.updateOne(b);
                if (success) {
                    showInfoAlert("Success", "Book returned successfully");
                    clear();
                    viewHandle(event);
                }
            }
        }
    }

    @FXML
    private void deleteHandle(ActionEvent event) {
        Borrow b = table.getSelectionModel().getSelectedItem();
        if (b == null) {
            showWarningAlert("No Selection", "No Record Selected",
                    "Please select a borrow record from the table");
        } else {
            if (showConfirmationAlert("Delete Confirmation", "Are you sure?",
                    "Do you want to delete this borrow record?")) {
                borrowdao.deleteOne(b);
                viewHandle(event);
                clear();
            }
        }
    }

    //ex
    
    @FXML
    private void borrowedBooksHandle(ActionEvent event) {
        List<Borrow> borrowed = borrowdao.findBorrowedBooks();
        if (borrowed.isEmpty()) {
            showInfoAlert("Borrowed Books", "No borrowed books found!");
        } else {
            table.getItems().setAll(borrowed);
            showInfoAlert("Borrowed Books", "Showing " + borrowed.size() + " borrowed book(s)");
        }
    }
    
    @FXML
    private void searchbyIds(ActionEvent event) {
        Integer bookId = booksCombobox.getValue();
        Integer studentId = studentsCombobox.getValue();
        
        if ((bookId == null || bookId <= 0) && (studentId == null || studentId <= 0)) {
            showWarningAlert("Search Error", "No criteria selected", 
                    "Please select:\n- A book ID\n- OR a student ID\n- OR both from the dropdown menus");
        } else {
            List<Borrow> results = borrowdao.searchByIds(bookId, studentId);
            if (results.isEmpty()) {
                showInfoAlert("Search Results", "No records found matching your criteria");
            } else {
                table.getItems().setAll(results);
                showInfoAlert("Search Results", "Found " + results.size() + " record(s)");
            }
        }
    }
    

    
    private void showWarningAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private boolean borrowValidor() {
        return (booksCombobox.getValue() != null && studentsCombobox.getValue() != null
                && borrowDate.getValue() != null);
    }
    
    private void clear() {
        booksCombobox.setValue(null);
        studentsCombobox.setValue(null);
        borrowDate.setValue(null);
        returnDate.setValue(null);
        status.setSelected(false);
    }
    
    private boolean showConfirmationAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}