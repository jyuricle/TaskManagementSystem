package dao;

import entities.Project;

import javax.ejb.Stateless;

/**
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@Stateless
public class ProjectDAO extends DAOImpl<Project, Integer> {

    @Override
    public Class<Project> getEntityClass() {
        return Project.class;
    }
}