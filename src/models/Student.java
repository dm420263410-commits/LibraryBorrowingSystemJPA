package models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private int studentId;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Borrow> borrows = new ArrayList<>();
    
    public Student() {}
    
    public Student(int studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<Borrow> getBorrows() { return borrows; }
    public void setBorrows(List<Borrow> borrows) { this.borrows = borrows; }
    
    @Override
    public String toString() {
        return "Student{id=" + studentId + ", name='" + name + "'}";
    }
}