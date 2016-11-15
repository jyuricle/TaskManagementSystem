package controllers;

import dao.SubordinateDAO;
import dao.SubtaskDAO;
import entities.Status;
import entities.Subordinate;
import entities.Subtask;
import org.apache.log4j.Logger;

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
public class SubordinateInfoController {

    private final static Logger logger = Logger.getLogger(SubordinateInfoController.class);

    @EJB
    private SubordinateDAO subordinateDAO;
    @EJB
    private SubtaskDAO subtaskDAO;
    private Subordinate subordinate;

    @PostConstruct
    public void init() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String name = externalContext.getRemoteUser();
        subordinate = subordinateDAO.getSubordinateByUserName(name);
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

    public Subordinate getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(Subordinate subordinate) {
        this.subordinate = subordinate;
    }
}
