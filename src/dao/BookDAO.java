package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Book;
import util.JPAUtil;
import java.util.List;

public class BookDAO {
    
    public List<Integer> getAllBooksIds() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                "SELECT b.bookId FROM Book b", Integer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Book> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b", Book.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Book findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Book.class, id);
        } finally {
            em.close();
        }
    }
}