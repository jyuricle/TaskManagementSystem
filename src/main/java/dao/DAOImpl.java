package dao;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Describes operations with database
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */
public abstract class DAOImpl<T, K extends Serializable> implements DAO<T, K> {

    /**
     * Entity manager instance
     */
    @PersistenceContext(unitName = "DB-unit")
    protected EntityManager entityManager;

    /**
     * Logger instance
     */
    private final static Logger logger = Logger.getLogger(DAOImpl.class);

    @Override
    public void insert(T obj) {
        entityManager.persist(obj);
    }

    @Override
    public List<T> getAll() {
        List<T> tList = null;
        try {
            tList = entityManager.createQuery("Select t from " +
                    getEntityClass().getSimpleName() + " t", getEntityClass()).getResultList();
        }
        catch (IllegalArgumentException iae) {
            logger.error("Error occurred while getting list of objects! " + iae.getMessage());
        }
        return tList;
    }

    @Override
    public void update(T obj) {
        entityManager.merge(obj);
    }

    @Override
    public T get(K key) {
        T obj = null;
        try {
            obj = entityManager.find(getEntityClass(), key);
        }
        catch (IllegalArgumentException iae) {
            logger.error("Error occurred while getting object by primary key! " + iae.getMessage());
        }
        return obj;
    }

    @Override
    public void delete(T obj) {
        entityManager.remove(entityManager.contains(obj) ? obj : entityManager.merge(obj));
    }

}
