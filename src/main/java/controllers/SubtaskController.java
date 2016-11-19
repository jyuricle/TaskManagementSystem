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
        setPriority();
        subtask.setStatus(Status.OPEN);
        subtask.setProject(project);
        subtask.setSubordinate(subordinate);
        subtaskDAO.insert(subtask);
        logger.info("New subtask " + subtask.getName() + " was added!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    public void initSubtask() {
        subtask = subtaskDAO.get(subtask.getId());
    }

    public void update() throws IOException {
        setPriority();
        subtask.setProject(project);
        subtaskDAO.update(subtask);
        logger.info("Subtask " + subtask.getName() + " was updated!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    public void update(Subtask task) throws IOException {
        logger.info("SubIIIIIIIIIIIIIIIIIIIIIIIIItask " + task.getName() + " was updated!");
        subtaskDAO.update(task);
    }

    public void delete() throws IOException {
        initSubtask();
        if (subtask != null) {
            logger.error("SSSSSSSSSSSSSSS " + subtask.getName() + subtask.getId());
            subtask.setSubordinate(null);
            subtask.setProject(null);
            subtaskDAO.update(subtask);
            subtaskDAO.delete(subtask);
            logger.info("Subtask " + subtask.getName() + " was deleted!");
        }
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void delete(Subtask task) {
        task.setSubordinate(null);
        task.setProject(null);
        subtaskDAO.update(task);
        subtaskDAO.delete(task);
        logger.info("Subtask " + task.getName() + " was deleted!");
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

    private void setPriority() {
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
    }

}
