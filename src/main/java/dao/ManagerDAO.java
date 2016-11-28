package dao;

import entities.Manager;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.ArrayList;

/**
 * Implements operation with database for manager instance
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Stateless
public class ManagerDAO extends DAOImpl<Manager, Integer> {

    @Override
    public Class<Manager> getEntityClass() {
        return Manager.class;
    }

    /**
     * Gets manager instance by user's name from session
     * @param name user's name
     * @return manager instance
     */
    public Manager getManagerByUserName(String name) {
        Query query = entityManager.createQuery("SELECT t from " +
                getEntityClass().getSimpleName() + " t where t.user.userName = ?1");
        query.setParameter(1, name);
        return (Manager)query.getSingleResult();
    }
}
