package dao;

import entities.Manager;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.ArrayList;

/**
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Stateless
public class ManagerDAO extends DAOImpl<Manager, Integer> {

    @Override
    public Class<Manager> getEntityClass() {
        return Manager.class;
    }

    public Manager getManagerByUserName(String name) {
        Query query = entityManager.createQuery("SELECT t from " +
                getEntityClass().getSimpleName() + " t where t.user.userName = ?1");
        query.setParameter(1, name);
        return (Manager)query.getSingleResult();
    }
}
