package dao;

import entities.Subtask;

import javax.ejb.Stateless;

/**
 * Implements operation with database for subtask instance
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Stateless
public class SubtaskDAO extends DAOImpl<Subtask, Integer> {

    @Override
    public Class<Subtask> getEntityClass() {
        return Subtask.class;
    }
}
