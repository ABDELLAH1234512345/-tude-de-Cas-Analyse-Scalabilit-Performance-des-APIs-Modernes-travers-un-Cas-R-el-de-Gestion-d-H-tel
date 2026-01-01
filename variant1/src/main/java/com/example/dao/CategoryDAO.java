package com.example.dao;

import com.example.config.HibernateUtil;
import com.example.model.Category;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for Category entity using native Hibernate API.
 * <p>
 * This class provides methods to perform CRUD operations on categories.
 * It manually manages Hibernate sessions and transactions, which is typical
 * in a JAX-RS (Jersey) + Hibernate implementation without Spring Data.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
public class CategoryDAO {

    /**
     * Persists a new category in the database.
     * 
     * @param category the category to save
     * @return the persisted category with generated ID
     */
    public Category save(Category category) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(category);
            transaction.commit();
            return category;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    /**
     * Saves a list of categories using batch processing for performance.
     * <p>
     * Sessions are flushed and cleared every 20 entities to optimize memory usage
     * during large imports.
     * </p>
     * 
     * @param categories the list of categories to save
     */
    public void saveAll(List<Category> categories) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int count = 0;
            for (Category category : categories) {
                session.persist(category);
                if (++count % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    /**
     * Finds a category by its primary key.
     * 
     * @param id the unique identifier
     * @return an Optional containing the category if found, empty otherwise
     */
    public Optional<Category> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Category category = session.get(Category.class, id);
            return Optional.ofNullable(category);
        }
    }

    /**
     * Retrieves a page of categories.
     * 
     * @param page the page number (0-indexed)
     * @param size the number of categories per page
     * @return a list of categories for the requested page
     */
    public List<Category> findAll(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Category> query = session.createQuery("FROM Category", Category.class);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.list();
        }
    }

    /**
     * Counts the total number of categories in the database.
     * 
     * @return the total category count
     */
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(c) FROM Category c", Long.class);
            return query.uniqueResult();
        }
    }

    /**
     * Updates an existing category in the database.
     * 
     * @param category the category with updated data
     * @return the updated and merged category instance
     */
    public Category update(Category category) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Category updated = session.merge(category);
            transaction.commit();
            return updated;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    /**
     * Deletes a category by its ID.
     * 
     * @param id the unique identifier of the category to delete
     */
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Category category = session.get(Category.class, id);
            if (category != null) {
                session.remove(category);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    /**
     * Checks if a category exists for a given ID.
     * 
     * @param id the unique identifier
     * @return true if it exists, false otherwise
     */
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
}
