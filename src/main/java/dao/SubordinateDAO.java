package dao;

import entities.Subordinate;

import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Stateless
public class SubordinateDAO extends DAOImpl<Subordinate, Integer> {

    @Override
    public Class<Subordinate> getEntityClass() {
        return Subordinate.class;
    }

    public Subordinate getSubordinateByUserName(String name) {
        Query query = entityManager.createQuery("SELECT t from " +
                getEntityClass().getSimpleName() + " t where t.user.userName = ?1");
        query.setParameter(1, name);
        return (Subordinate) query.getSingleResult();
    }
}
