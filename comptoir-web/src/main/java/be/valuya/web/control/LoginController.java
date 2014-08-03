package be.valuya.web.control;

import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.EmployeeService;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
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
    private EmployeeService employeeService;
    //
    private Employee loggedEmployee;

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        String remoteUser = request.getRemoteUser();

        loggedEmployee = employeeService.findEmployeeByLogin(remoteUser);
        if (loggedEmployee == null) {
            throw new IllegalStateException("Inexistent user logged in");
        }
    }

    @Nonnull
    public Employee getLoggedEmployee() {
        return loggedEmployee;
    }

}
