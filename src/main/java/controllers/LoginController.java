package controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Defines information about user who logged in
 *
 * @author Yurii Krat
 * @version 1.0, 12.11.16
 */
@ManagedBean
public class LoginController {

    public LoginController() {

    }

    /**
     * Defines role of user who logged in
     * @return user's role
     */
    public String userRole() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        if(externalContext.isUserInRole("Admin")) {
            return "admin";
        } else if (externalContext.isUserInRole("Manager")) {
            return "manager";
        } else {
            return "subordinate";
        }
    }
}
