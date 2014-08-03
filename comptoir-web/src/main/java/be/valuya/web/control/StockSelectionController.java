package be.valuya.web.control;

import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.StockService;
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
public class StockSelectionController implements Serializable {

    @EJB
    private transient StockService stockService;
    @Inject
    private transient LoginController loginController;
    //
    private List<Stock> stocks;

    @PostConstruct
    public void init() {
        Employee loggedEmployee = loginController.getLoggedEmployee();
        Company company = loggedEmployee.getCompany();
        stocks = stockService.findStocks(company);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

}
