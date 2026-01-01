package com.example.dao;

import com.example.config.HibernateUtil;
import com.example.model.Item;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for Item entity using native Hibernate API.
 * <p>
 * This class provides methods to perform CRUD operations on items and filter
 * them
 * by category. It optimizes performance by using JOIN FETCH to avoid N+1 query
 * problems and batch processing for mass insertions.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
public class ItemDAO {

    /**
     * Persists a new item in the database.
     * 
     * @param item the item to save
     * @return the persisted item with generated ID
     */
    public Item save(Item item) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(item);
            transaction.commit();
            return item;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    /**
     * Saves a list of items using batch processing for performance.
     * <p>
     * Sessions are flushed and cleared every 20 entities to optimize memory usage
     * during large imports (benchmark data generation).
     * </p>
     * 
     * @param items the list of items to save
     */
    public void saveAll(List<Item> items) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int count = 0;
            for (Item item : items) {
                session.persist(item);
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
     * Finds an item by its primary key.
     * 
     * @param id the unique identifier
     * @return an Optional containing the item if found, empty otherwise
     */
    public Optional<Item> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Item item = session.get(Item.class, id);
            return Optional.ofNullable(item);
        }
    }

    /**
     * Retrieves a page of items.
     * 
     * @param page the page number (0-indexed)
     * @param size the number of items per page
     * @return a list of items for the requested page
     */
    public List<Item> findAll(int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Item> query = session.createQuery("FROM Item", Item.class);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.list();
        }
    }

    /**
     * Retrieves items filtered by category ID with JOIN FETCH to prevent N+1
     * issues.
     * 
     * @param categoryId the category ID to filter by
     * @param page       the page number
     * @param size       the page size
     * @return a list of items belonging to the category
     */
    public List<Item> findByCategoryId(Long categoryId, int page, int size) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Item> query = session.createQuery(
                    "SELECT i FROM Item i JOIN FETCH i.category WHERE i.category.id = :categoryId",
                    Item.class);
            query.setParameter("categoryId", categoryId);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            return query.list();
        }
    }

    /**
     * Counts the total number of items in the database.
     * 
     * @return the total item count
     */
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(i) FROM Item i", Long.class);
            return query.uniqueResult();
        }
    }

    /**
     * Counts the number of items belonging to a specific category.
     * 
     * @param categoryId the category unique identifier
     * @return the count of items in this category
     */
    public long countByCategoryId(Long categoryId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(i) FROM Item i WHERE i.category.id = :categoryId",
                    Long.class);
            query.setParameter("categoryId", categoryId);
            return query.uniqueResult();
        }
    }

    /**
     * Updates an existing item in the database.
     * 
     * @param item the item with updated data
     * @return the updated and merged item instance
     */
    public Item update(Item item) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Item updated = session.merge(item);
            transaction.commit();
            return updated;
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    /**
     * Deletes an item by its ID.
     * 
     * @param id the unique identifier of the item to delete
     */
    public void delete(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Item item = session.get(Item.class, id);
            if (item != null) {
                session.remove(item);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            throw e;
        }
    }

    /**
     * Checks if an item exists for a given ID.
     * 
     * @param id the unique identifier
     * @return true if it exists, false otherwise
     */
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
}
