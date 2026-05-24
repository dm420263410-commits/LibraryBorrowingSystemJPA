package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Student;
import util.JPAUtil;
import java.util.List;

public class StudentDAO {
    
    public List<Integer> getAllStudentsIds() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Integer> query = em.createQuery(
                "SELECT s.studentId FROM Student s", Integer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Student> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery("SELECT s FROM Student s", Student.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Student findById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Student.class, id);
        } finally {
            em.close();
        }
    }
}