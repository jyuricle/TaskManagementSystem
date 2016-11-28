package dao;

import entities.Subordinate;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * Implements operation with database for subordinate instance
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Stateless
public class SubordinateDAO extends DAOImpl<Subordinate, Integer> {

    @Override
    public Class<Subordinate> getEntityClass() {
        return Subordinate.class;
    }

    /**
     * Gets subordinate instance by user's name from session
     * @param name user's name
     * @return subordinate instance
     */
    public Subordinate getSubordinateByUserName(String name) {
        Query query = entityManager.createQuery("SELECT t from " +
                getEntityClass().getSimpleName() + " t where t.user.userName = ?1");
        query.setParameter(1, name);
        return (Subordinate) query.getSingleResult();
    }
}
