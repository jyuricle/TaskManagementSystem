package dao;

import java.util.List;

/**
 * Defines operations with database
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */
public interface DAO<T, K> {

    /**
     * Returns class type of defined entity
     * @return class type of entity
     */
    Class<T> getEntityClass();

    /**
     * Inserts object into database
     * @param obj object to insert
     */
    void insert(T obj);

    /**
     * Gets all objects of concrete entity from database
     * @return all objects from database
     */
    List<T> getAll();

    /**
     * Updates object in database
     * @param obj object to update
     */
    void update(T obj);

    /**
     * Finds object in database by primary key
     * @param key primary key
     * @return found object
     */
    T get(K key);

    /**
     * Removes object from database
     * @param obj object to remove
     */
    void delete(T obj);

}
