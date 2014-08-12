package be.valuya.comptoir.web.control;

import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.EmployeeService;
import java.io.Serializable;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Named
@SessionScoped
public class LoginController implements Serializable {

    @EJB
    private transient EmployeeService employeeService;
    //
    private Employee loggedEmployee;
    private boolean eidLogin;
    private Locale userLocale = Locale.ENGLISH;
    private Locale editLocale;

    /**
     * Should be called by main page (preRenderView), which can't be loaded without login.
     */
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String remoteUser = request.getRemoteUser();

        loggedEmployee = employeeService.findEmployeeByLogin(remoteUser);
        if (loggedEmployee == null) {
            throw new IllegalStateException("Inexistent user logged in");
        }

        userLocale = loggedEmployee.getLocale();
        editLocale = loggedEmployee.getLocale();
    }

    @Nonnull
    public Employee getLoggedEmployee() {
        return loggedEmployee;
    }

    public boolean isEidLogin() {
        return eidLogin;
    }

    public Locale getUserLocale() {
        return userLocale;
    }

    public void setUserLocale(Locale userLocale) {
        this.userLocale = userLocale;
    }

    public Locale getEditLocale() {
        return editLocale;
    }

    public void setEditLocale(Locale editLocale) {
        this.editLocale = editLocale;
    }

}
