package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "libraryPU";
    private static EntityManagerFactory factory;
    
    private JPAUtil() {}
    
    public static EntityManagerFactory getEntityManagerFactory() {
        if (factory == null || !factory.isOpen()) {
            try {
                factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                System.out.println("EntityManagerFactory created successfully!");
            } catch (Exception e) {
                System.err.println("Error creating EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return factory;
    }
    
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
    
    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
            System.out.println("EntityManagerFactory closed.");
        }
    }
}