package controllers;

import dao.ManagerDAO;
import dao.ProjectDAO;
import entities.Manager;
import entities.Project;
import entities.Status;
import entities.Subtask;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * @author Yurii Krat
 * @version 1.0, 15.11.16
 */

@ManagedBean
public class ManagerInfoController {

    @EJB
    private ManagerDAO managerDAO;
    @EJB
    private ProjectDAO projectDAO;
    private Manager manager;
    private Project project;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String name = externalContext.getRemoteUser();
        project = new Project();
        manager = managerDAO.getManagerByUserName(name);
    }

    public void initProject() {
        project = projectDAO.get(project.getId());
    }

    public int progress() {
        if (project.getSubtasks() != null) {
            int size = project.getSubtasks().size();
            if (size != 0) {
                int finished = 0;
                for (Subtask task : project.getSubtasks()) {
                    if (task.getStatus() == Status.DONE) {
                        finished++;
                    }
                }
                return (int) (((double) finished / size) * 100);
            }
        }
        return 0;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
