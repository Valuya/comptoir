package be.valuya.web.control;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemPrice;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.SaleService;
import be.valuya.web.view.Views;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
public class SaleController implements Serializable {

    @EJB
    private transient SaleService saleService;
    @Inject
    private transient LoginController loginController;
    //
    private Sale sale;
    private List<ItemSale> itemSales;
    private ItemSale itemSale;
    private BigDecimal payedAmount;
    private BigDecimal giveBackAmount;
    private Account selectedAccount;

    public String actionNew() {
        Employee loggedEmployee = loginController.getLoggedEmployee();
        Company company = loggedEmployee.getCompany();

        sale = new Sale();
        itemSales = new ArrayList<>();
        sale.setCompany(company);

        resetItemSale();

        return Views.SALE_DETAILS;
    }

    public String actionDetails(Sale sale) {
        this.sale = sale;
        itemSales = saleService.findSaleItems(sale);

        return Views.SALE_DETAILS;
    }

    public void actionAddItem() {
        Item item = itemSale.getItem();
        
        ZonedDateTime dateTime = ZonedDateTime.now();
        ItemPrice price = item.getCurrentPrice();
        
        itemSale.setPrice(price);
        itemSale.setDateTime(dateTime);
        
        itemSales.add(itemSale);
        
        sale = saleService.calcSale(sale, itemSales);
        
        resetItemSale();
    }
    
    public void actionCloseSale() {
        sale = saleService.closeSale(sale, itemSales, selectedAccount);
    }
    
    public void handlePayedChange() {
        giveBackAmount = saleService.calcPayBackAmount(sale, itemSales, payedAmount);
    }

    public Sale getSale() {
        return sale;
    }

    public List<ItemSale> getItemSales() {
        return itemSales;
    }

    public ItemSale getItemSale() {
        return itemSale;
    }

    public BigDecimal getPayedAmount() {
        return payedAmount;
    }

    public BigDecimal getGiveBackAmount() {
        return giveBackAmount;
    }

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    private void resetItemSale() {
        itemSale = new ItemSale();
        itemSale.setSale(sale);
    }

}
