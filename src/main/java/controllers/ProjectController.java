package controllers;

import dao.ManagerDAO;
import dao.ProjectDAO;
import entities.*;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.jms.*;
import java.io.IOException;
import java.util.List;

/**
 * Implements actions with projects
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@ManagedBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProjectController {

    /**
     * Logger instance
     */
    private static final Logger logger = Logger.getLogger(ProjectController.class);

    /**
     * Project's dao for interaction with database
     */
    @EJB
    private ProjectDAO projectDAO;

    /**
     * Manager's dao for interaction with database
     */
    @EJB
    private ManagerDAO managerDAO;

    /**
     * Subtask's controller
     */
    @ManagedProperty("#{subtaskController}")
    private SubtaskController subtaskController;

    /**
     * List of all projects
     */
    private List<Project> projectList;

    /**
     * Project instance
     */
    private Project project;

    /**
     * External context instance
     */
    private ExternalContext externalContext;

    /**
     * Priority instance
     */
    private Integer priority;

    /**
     * Manager's instance
     */
    private Manager manager;

    /**
     * Connection factory instance for message service
     */
    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    /**
     * Destination instance for sending message
     */
    @Resource(mappedName="java:jboss/exported/jms/topic/test")
    private Destination destination;

    /**
     * Initialization of fields after creating of bean
     */
    @PostConstruct
    public void init() {
        projectList = projectDAO.getAll();
        project = new Project();
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
        manager = managerDAO.getManagerByUserName(externalContext.getRemoteUser());
    }

    /**
     * Adds new project to the system
     *
     * @throws IOException
     */
    public void add() throws IOException {
        setPriority();
        project.setManager(manager);
        projectDAO.insert(project);
        logger.info("New project " + project.getName() + " was added!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    /**
     * Calculates progress of doing project's
     * @return
     */
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

    /**
     * Initializes selected project
     */
    public void initProject() {
        project = projectDAO.get(project.getId());
    }

    /**
     * Removes project and all it's subtasks from system
     *
     * @throws IOException
     */
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

    /**
     * Updates project's information and sends
     * message about updating to subordinates
     *
     * @throws IOException
     */
    public void update() throws IOException {
        setPriority();
        project.setManager(manager);
        projectDAO.update(project);
        logger.info("Project " + project.getName() + " was updated!");
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            message.setText("Project " + project.getName() + " information was updated");
            producer.send(message);
            logger.info("message sent");
            session.close();
            connection.close();

        } catch (JMSException ex) {
            logger.error("Sending message error: " +  ex);
        }
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    /**
     * Changes status of subtask to "DONE"
     * @param task subtask which status changes
     *
     * @throws IOException
     */
    public void submitStatus(Subtask task) throws IOException {
        task.setStatus(Status.DONE);
        subtaskController.update(task);
        logger.info("Subtask status was changed to \"DONE\"");
    }

    /**
     * Changes status of subtask to "OPEN"
     * @param task subtask which status changes
     *
     * @throws IOException
     */
    public void declineStatus(Subtask task) throws IOException {
        task.setStatus(Status.OPEN);
        subtaskController.update(task);
        logger.info("Subtask status was changed to \"OPEN\"");
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
