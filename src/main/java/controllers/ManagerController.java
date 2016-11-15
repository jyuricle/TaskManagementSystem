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
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@ManagedBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ManagerController {

    private static final Logger logger = Logger.getLogger(ManagerController.class);

    @EJB
    private ManagerDAO managerDAO;

    @EJB
    private ProjectDAO projectDAO;

    private List<Manager> managerList;

    private Manager manager;

    private User user;

    private ExternalContext externalContext;

    @PostConstruct
    public void init() {
        managerList = managerDAO.getAll();
        manager = new Manager();
        user = new User();
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void add() throws IOException {
        user.setRole("Manager");
        manager.setUser(user);
        managerDAO.insert(manager);
        logger.info("New manager " + manager.getFirstName() + " " +
                manager.getLastName() + " was added!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete() throws IOException{
        manager = managerDAO.get(manager.getId());
        if(manager != null) {
            managerDAO.delete(manager);
            logger.info("Manager " + manager.getFirstName() + " " +
                    manager.getLastName() + " was deleted!");
        }
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
