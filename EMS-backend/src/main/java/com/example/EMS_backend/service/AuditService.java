package com.example.EMS_backend.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to manage audit context for database operations.
 * Sets the current user ID in PostgreSQL session variables for audit logging.
 */
@Service
@Transactional
public class AuditService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Sets the current user ID for audit logging.
     * Call this method before performing any database operations that should be audited.
     * 
     * @param userId The ID of the current user performing the operation
     */
    public void setCurrentUser(Long userId) {
        if (userId != null) {
            entityManager.createNativeQuery("SET app.current_user_id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
        }
    }

    /**
     * Clears the current user ID from the session.
     * Call this method after completing database operations.
     */
    public void clearCurrentUser() {
        entityManager.createNativeQuery("RESET app.current_user_id").executeUpdate();
    }

    /**
     * Executes a database operation with audit context.
     * This is a convenient method that handles setting and clearing the audit context.
     * 
     * @param userId The user ID to set for auditing
     * @param operation The database operation to perform
     * @return The result of the operation
     */
    public <T> T executeWithAudit(Long userId, DatabaseOperation<T> operation) {
        try {
            setCurrentUser(userId);
            return operation.execute();
        } finally {
            clearCurrentUser();
        }
    }

    /**
     * Functional interface for database operations that need audit context.
     */
    @FunctionalInterface
    public interface DatabaseOperation<T> {
        T execute();
    }
}
