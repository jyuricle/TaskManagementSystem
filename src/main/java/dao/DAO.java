package dao;

import java.util.List;

/**
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */
public interface DAO<T, K> {

    Class<T> getEntityClass();
    void insert(T obj);
    List<T> getAll();
    void update(T obj);
    T get(K key);
    void delete(T obj);

}
