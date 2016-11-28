package controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Implements logout
 *
 * @author Yurii Krat
 * @version 1.0, 10.11.16
 */

@ManagedBean
public class LogoutController {

    public LogoutController() {

    }

    /**
     * Invalidates user's session
     *
     * @throws IOException
     */
    public void logout() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.invalidateSession();
        externalContext.redirect("/TasksManagement_war_exploded/index.xhtml");
    }
}
