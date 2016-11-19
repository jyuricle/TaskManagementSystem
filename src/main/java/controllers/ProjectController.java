package controllers;

import dao.ManagerDAO;
import dao.ProjectDAO;
import entities.*;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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

    @ManagedProperty("#{subtaskController}")
    private SubtaskController subtaskController;

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

    public void add() throws IOException {
        setPriority();
        project.setManager(manager);
        projectDAO.insert(project);
        logger.info("New project " + project.getName() + " was added!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
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

    public void initProject() {
        project = projectDAO.get(project.getId());
    }

    public void delete() throws IOException {
        initProject();
        if (project != null) {
            for (Subtask task : project.getSubtasks()) {
                subtaskController.delete(task);
            }
            project.setManager(null);
            project.setSubtasks(null);
            projectDAO.update(project);
            projectDAO.delete(project);
            logger.info("Project " + project.getName() + " was deleted!");
        }
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    public void update() throws IOException {
        setPriority();
        project.setManager(manager);
        projectDAO.update(project);
        logger.info("Project " + project.getName() + " was updated!");
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

    private void setPriority() {
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

    public SubtaskController getSubtaskController() {
        return subtaskController;
    }

    public void setSubtaskController(SubtaskController subtaskController) {
        this.subtaskController = subtaskController;
    }
}
