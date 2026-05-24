package models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int bookId;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Borrow> borrows = new ArrayList<>();
    
    public Book() {}
    
    public Book(String title) {
        this.title = title;
    }
    
    public Book(int bookId, String title) {
        this.bookId = bookId;
        this.title = title;
    }
    
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public List<Borrow> getBorrows() { return borrows; }
    public void setBorrows(List<Borrow> borrows) { this.borrows = borrows; }
    
    @Override
    public String toString() {
        return "Book{id=" + bookId + ", title='" + title + "'}";
    }
}