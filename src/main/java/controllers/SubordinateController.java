package controllers;

import dao.SubordinateDAO;
import entities.Subordinate;
import entities.Subtask;
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
 * Implements subordinate's functions and provides him with
 * necessary information
 *
 * @author Yurii Krat
 * @version 1.0, 09.11.16
 */

@ManagedBean
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SubordinateController {

    /**
     * Logger instance
     */
    private static final Logger logger = Logger.getLogger(SubordinateController.class);

    /**
     * Subordinate's dao for interaction with database
     */
    @EJB
    private SubordinateDAO subordinateDAO;

    /**
     * List of all subordinates
     */
    private List<Subordinate> subordinateList;

    /**
     * Subordinate's instance
     */
    private Subordinate subordinate;

    /**
     * User's instance
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
        subordinateList = subordinateDAO.getAll();
        subordinate = new Subordinate();
        user = new User();
        externalContext = FacesContext.getCurrentInstance().getExternalContext();
    }

    /**
     * Adds new subordinate to the system
     * @throws IOException
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void add() throws IOException {
        user.setRole("Subordinate");
        subordinate.setUser(user);
        subordinateDAO.insert(subordinate);
        logger.info("New subordinate " + subordinate.getFirstName() + " " +
                subordinate.getLastName() + " was added!");
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }

    public List<Subordinate> getSubordinateList() {
        return subordinateList;
    }

    public void setSubordinateList(List<Subordinate> subordinateList) {
        this.subordinateList = subordinateList;
    }

    public Subordinate getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(Subordinate subordinate) {
        this.subordinate = subordinate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
