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
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

/**
 * Implements actions with subtasks
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@ManagedBean
@TransactionManagement(TransactionManagementType.CONTAINER)
@ViewScoped
public class SubtaskController {

    /**
     * Logger instance
     */
    private static final Logger logger = Logger.getLogger(SubtaskController.class);

    /**
     * Subtask's dao for interaction with database
     */
    @EJB
    private SubtaskDAO subtaskDAO;

    /**
     * List of all subtask in system
     */
    private List<Subtask> subtaskList;

    /**
     * Subtask's instance
     */
    private Subtask subtask;

    /**
     * Priority's instance
     */
    private Integer priority;

    /**
     * Subordinate's instance
     */
    private Subordinate subordinate;

    /**
     * External context instance
     */
    private ExternalContext externalContext;

    /**
     * Subordinate's dao for interaction with database
     */
    @EJB
    private SubordinateDAO subordinateDAO;

    /**
     * Manager's dao for interaction with database
     */
    @EJB
    private ManagerDAO managerDAO;

    /**
     * Project's dao for interaction with database
     */
    @EJB
    private ProjectDAO projectDAO;

    /**
     * Manager's instance
     */
    private Manager manager;

    /**
     * Project's instance
     */
    private Project project;

    /**
     * Initialization of fields after creating of bean
     */
    @PostConstruct
    public void init() {
        subtaskList = subtaskDAO.getAll();
        subtask = new Subtask();
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        manager = managerDAO.getManagerByUserName(externalContext.getRemoteUser());
        subordinate = new Subordinate();
        project = new Project();
    }

    /**
     * Adds new subtask to the project
     *
     * @throws IOException
     */
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
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    /**
     * Initializes project's subtasks
     */
    public void initSubtask() {
        subtask = subtaskDAO.get(subtask.getId());
    }

    /**
     * Updates subtask
     * @throws IOException
     */
    public void update() throws IOException {
        setPriority();
        subtask.setProject(project);
        subtaskDAO.update(subtask);
        logger.info("Subtask " + subtask.getName() + " was updated!");
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    /**
     * Updates subtask
     *
     * @param task subtask
     * @throws IOException
     */
    public void update(Subtask task) throws IOException {
        subtaskDAO.update(task);
    }

    /**
     * Removes subtask from the system
     * @throws IOException
     */
    public void delete() throws IOException {
        initSubtask();
        if (subtask != null) {
            delete(subtask);
        }
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    /**
     * Removes subtask from the system
     *
     * @param task subtask to remove
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void delete(Subtask task) {
        task.setSubordinate(null);
        task.setProject(null);
        subtaskDAO.update(task);
        subtaskDAO.delete(task);
        logger.info("Subtask " + task.getName() + " was deleted!");
    }

    /**
     * Initializes projects of defined manager
     */
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
