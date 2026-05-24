package models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrow")
public class Borrow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_id")
    private int borrowId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;
    
    @Column(name = "return_date")
    private LocalDate returnDate;
    
    @Column(name = "status")
    private boolean status = false;
    
    public Borrow() {}
    
    public Borrow(Student student, Book book, LocalDate borrowDate) {
        this.student = student;
        this.book = book;
        this.borrowDate = borrowDate;
        this.status = false;
    }
    
    public Borrow(int borrowId, Student student, Book book, 
                  LocalDate borrowDate, LocalDate returnDate, boolean status) {
        this.borrowId = borrowId;
        this.student = student;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }
    
    public int getBorrowId() { return borrowId; }
    public void setBorrowId(int borrowId) { this.borrowId = borrowId; }
    
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    
    // دالة مساعدة للحصول على ID الطالب (للواجهة)
    public int getStudentId() { 
        return student != null ? student.getStudentId() : 0; 
    }
    
    public void setStudentId(int studentId) {
        // هذه الدالة للتوافق مع الواجهة
        // سنستخدمها في الـ Controller
        if (this.student == null) {
            this.student = new Student();
        }
        this.student.setStudentId(studentId);
    }
    
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    
    // دالة مساعدة للحصول على ID الكتاب (للواجهة)
    public int getBookId() { 
        return book != null ? book.getBookId() : 0; 
    }
    
    public void setBookId(int bookId) {
        // هذه الدالة للتوافق مع الواجهة
        if (this.book == null) {
            this.book = new Book();
        }
        this.book.setBookId(bookId);
    }
    
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    
    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    
    @Override
    public String toString() {
        return "Borrow{id=" + borrowId + ", studentId=" + getStudentId() + 
               ", bookId=" + getBookId() + ", status=" + status + "}";
    }
}