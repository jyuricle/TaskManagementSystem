package controllers;

import dao.ProjectDAO;
import dao.SubordinateDAO;
import dao.SubtaskDAO;
import entities.Project;
import entities.Status;
import entities.Subordinate;
import entities.Subtask;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.jms.*;
import java.util.List;

/**
 * Provides subordinate with necessary information and
 * implements some actions with subtasks
 *
 * @author Yurii Krat
 * @version 1.0, 15.11.16
 */

@ManagedBean
@ViewScoped
public class SubordinateInfoController {

    /**
     * Logger instance
     */
    private final static Logger logger = Logger.getLogger(SubordinateInfoController.class);

    /**
     * Subordinate's dao for interaction with database
     */
    @EJB
    private SubordinateDAO subordinateDAO;

    /**
     * Subtask's dao for interaction with database
     */
    @EJB
    private SubtaskDAO subtaskDAO;

    /**
     * Project's dao for interaction with database
     */
    @EJB
    private ProjectDAO projectDAO;

    /**
     * List of all projects
     */
    private List<Project> projectList;

    /**
     * Project's instance
     */
    private Project project;

    /**
     * Subordinate's instance
     */
    private Subordinate subordinate;

    /**
     * Connection factory instance
     */
    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    /**
     * Topic instance for receiving messages
     */
    @Resource(mappedName="java:jboss/exported/jms/topic/test")
    private Topic destination;

    /**
     * Initialization of fields after creating of bean
     */
    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String name = externalContext.getRemoteUser();
        subordinate = subordinateDAO.getSubordinateByUserName(name);
        project = new Project();
        projectList = projectDAO.getAll();
    }

    /**
     * Initializes selected project
     */
    public void initProject() {
        project = projectDAO.get(project.getId());
    }

    /**
     * Changes subtask's status to "REVIEW"
     * @param task subtask which status changes
     */
    public void updateStatus(Subtask task) {
        task.setStatus(Status.REVIEW);
        subtaskDAO.update(task);
        logger.info("Subtask " + task.getName() + " was updated!");
    }

    /**
     * Defines if subtask is completed
     * @param task subtask
     * @return true if subtask is completed, otherwise false
     */
    public String completed(Subtask task) {
        if (task.getStatus() == Status.DONE) {
            return "glyphicon glyphicon-ok";
        }
        return "glyphicon glyphicon-remove";
    }

    /**
     * Receives message about updating project's information
     * @return
     */
    public String showMessage() {
        String message = null;
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            long id = (long)(Math.random() * 1e8);
            MessageConsumer consumer = session.createSharedDurableConsumer(destination, "" + id);
            connection.start();
            TextMessage msg = (TextMessage)consumer.receiveNoWait();
            if (msg != null) {
                message = msg.getText();
                logger.info("Message received: " + message);
            }
        } catch (JMSException ex) {
            logger.error("Error occurred: " + ex);
        }
        return message;
    }

    public Subordinate getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(Subordinate subordinate) {
        this.subordinate = subordinate;
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
}
