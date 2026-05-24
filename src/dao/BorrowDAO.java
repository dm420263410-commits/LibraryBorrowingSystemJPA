package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Borrow;
import models.Book;
import models.Student;
import util.JPAUtil;
import java.time.LocalDate;
import java.util.List;

public class BorrowDAO {
    
    public List<Borrow> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Borrow> query = em.createQuery(
                "SELECT b FROM Borrow b", Borrow.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public boolean insertOne(Borrow borrow) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // جلب الطالب والكتاب من قاعدة البيانات
            Student student = em.find(Student.class, borrow.getStudentId());
            Book book = em.find(Book.class, borrow.getBookId());
            
            if (student == null || book == null) {
                return false;
            }
            
            borrow.setStudent(student);
            borrow.setBook(book);
            borrow.setBorrowDate(LocalDate.now());
            borrow.setStatus(false);
            
            em.persist(borrow);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    
    public boolean updateOne(Borrow borrow) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Borrow existing = em.find(Borrow.class, borrow.getBorrowId());
            
            if (existing == null) {
                return false;
            }
            
            existing.setReturnDate(borrow.getReturnDate());
            existing.setStatus(borrow.getStatus());
            
            em.merge(existing);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    
    public boolean deleteOne(Borrow borrow) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Borrow existing = em.find(Borrow.class, borrow.getBorrowId());
            
            if (existing != null) {
                em.remove(existing);
            }
            
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }
    
    //ex:
    public List<Borrow> findBorrowedBooks() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Borrow> query = em.createQuery(
                "SELECT b FROM Borrow b WHERE b.status = false OR b.returnDate IS NULL", 
                Borrow.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
   
    public List<Borrow> searchByIds(Integer bookId, Integer studentId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT b FROM Borrow b WHERE 1=1";
            
            if (bookId != null && bookId > 0) {
                jpql += " AND b.book.bookId = :bookId";
            }
            if (studentId != null && studentId > 0) {
                jpql += " AND b.student.studentId = :studentId";
            }
            
            TypedQuery<Borrow> query = em.createQuery(jpql, Borrow.class);
            
            if (bookId != null && bookId > 0) {
                query.setParameter("bookId", bookId);
            }
            if (studentId != null && studentId > 0) {
                query.setParameter("studentId", studentId);
            }
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}