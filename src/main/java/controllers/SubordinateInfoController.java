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
 * @author Yurii Krat
 * @version 1.0, 15.11.16
 */

@ManagedBean
@ViewScoped
public class SubordinateInfoController {

    private final static Logger logger = Logger.getLogger(SubordinateInfoController.class);

    @EJB
    private SubordinateDAO subordinateDAO;
    @EJB
    private SubtaskDAO subtaskDAO;

    @EJB
    private ProjectDAO projectDAO;

    private List<Project> projectList;
    private Project project;

    private Subordinate subordinate;

    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName="java:jboss/exported/jms/topic/test")
    private Topic destination;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String name = externalContext.getRemoteUser();
        subordinate = subordinateDAO.getSubordinateByUserName(name);
        project = new Project();
        projectList = projectDAO.getAll();
    }

    public void initProject() {
        project = projectDAO.get(project.getId());
    }

    public void updateStatus(Subtask task) {
        task.setStatus(Status.REVIEW);
        subtaskDAO.update(task);
        logger.info("Subtask " + task.getName() + " was updated!");
    }

    public String completed(Subtask task) {
        if (task.getStatus() == Status.DONE) {
            return "glyphicon glyphicon-ok";
        }
        return "glyphicon glyphicon-remove";
    }

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
