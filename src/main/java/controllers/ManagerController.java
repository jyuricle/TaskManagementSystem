package controllers;

import dao.ManagerDAO;
import dao.ProjectDAO;
import entities.Manager;
import entities.User;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

/**
 * Implements manager's functions and provides him with necessary information
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */
@ManagedBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ManagerController {

    /**
     * Logger instance
     */
    private static final Logger logger = Logger.getLogger(ManagerController.class);

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
     * List of all managers in system
     */
    private List<Manager> managerList;

    /**
     * Manager instance
     */
    private Manager manager;

    /**
     * User instance
     */
    private User user;

    /**
     * External context instance
     */
    private ExternalContext externalContext;

    /**
     * Initialization of fields after creating of bean
     */
    @PostConstruct
    public void init() {
        managerList = managerDAO.getAll();
        manager = new Manager();
        user = new User();
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
    }

    /**
     * Adds new manager to system
     *
     * @throws IOException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void add() throws IOException {
        user.setRole("Manager");
        manager.setUser(user);
        managerDAO.insert(manager);
        logger.info("New manager " + manager.getFirstName() + " " +
                manager.getLastName() + " was added!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    public List<Manager> getManagerList() {
        return managerList;
    }

    public void setManagerList(List<Manager> managerList) {
        this.managerList = managerList;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
