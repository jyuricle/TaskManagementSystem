package controllers;

import dao.ManagerDAO;
import dao.ProjectDAO;
import entities.Manager;
import entities.Priority;
import entities.Project;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

/**
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@ManagedBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProjectController {

    private static final Logger logger = Logger.getLogger(ProjectController.class);

    @EJB
    private ProjectDAO projectDAO;

    @EJB
    private ManagerDAO managerDAO;

    private List<Project> projectList;

    private Project project;

    private ExternalContext externalContext;

    private Integer priority;

    private Manager manager;

    @PostConstruct
    public void init() {
        projectList = projectDAO.getAll();
        project = new Project();
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        manager = managerDAO.getManagerByUserName(externalContext.getRemoteUser());
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void add() throws IOException {
        switch (priority) {
            case 1:
                project.setPriority(Priority.LOW);
                break;
            case 2:
                project.setPriority(Priority.MEDIUM);
                break;
            case 3:
                project.setPriority(Priority.HIGH);
                break;
        }
        project.setManager(manager);
        projectDAO.insert(project);
        logger.info("New project " + project.getName() + " was added!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }


    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
