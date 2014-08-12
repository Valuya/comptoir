package be.valuya.comptoir.web.control;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.AccountService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Named
@SessionScoped
public class AccountSelectionController implements Serializable {

    @EJB
    private transient AccountService accountService;
    @Inject
    private transient LoginController loginController;
    //
    private List<Account> accounts;

    @PostConstruct
    public void init() {
        Employee loggedEmployee = loginController.getLoggedEmployee();
        Company company = loggedEmployee.getCompany();
        accounts = accountService.findAccounts(company);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

}
