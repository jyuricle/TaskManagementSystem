package controllers;

import dao.ManagerDAO;
import dao.ProjectDAO;
import dao.SubordinateDAO;
import dao.SubtaskDAO;
import entities.*;
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
public class SubtaskController {

    private static final Logger logger = Logger.getLogger(SubtaskController.class);
    @EJB
    private SubtaskDAO subtaskDAO;
    private List<Subtask> subtaskList;
    private Subtask subtask;
    private Integer priority;
    private Subordinate subordinate;
    private ExternalContext externalContext;
    @EJB
    private SubordinateDAO subordinateDAO;
    @EJB
    private ManagerDAO managerDAO;
    @EJB
    private ProjectDAO projectDAO;
    private Manager manager;
    private Project project;

    @PostConstruct
    public void init() {
        subtaskList = subtaskDAO.getAll();
        subtask = new Subtask();
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        manager = managerDAO.getManagerByUserName(externalContext.getRemoteUser());
        subordinate = new Subordinate();
        project = new Project();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void add() throws IOException {
        initProject();
        subordinate = subordinateDAO.get(subordinate.getId());
        switch (priority) {
            case 1:
                subtask.setPriority(Priority.LOW);
                break;
            case 2:
                subtask.setPriority(Priority.MEDIUM);
                break;
            case 3:
                subtask.setPriority(Priority.HIGH);
                break;
        }
        subtask.setStatus(Status.OPEN);
        subtask.setProject(project);
        subtask.setSubordinate(subordinate);
        subtaskDAO.insert(subtask);
        logger.info("New subtask " + subtask.getName() + " was added!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete() throws IOException {
        subtaskDAO.delete(subtask);
        logger.info("Subtask " + subtask.getName() + " was deleted!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    public void initProject() {
        project = projectDAO.get(project.getId());
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public Subtask getSubtask() {
        return subtask;
    }

    public void setSubtask(Subtask subtask) {
        this.subtask = subtask;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Subordinate getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(Subordinate subordinate) {
        this.subordinate = subordinate;
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
