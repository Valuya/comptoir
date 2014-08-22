package be.valuya.comptoir.web.control;

import be.valuya.comptoir.model.accounting.Account;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemSale;
import be.valuya.comptoir.model.commercial.Payment;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.factory.PriceFactory;
import be.valuya.comptoir.model.search.ItemSearch;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.ItemColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.web.view.Views;
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
    @EJB
    private transient StockService stockService;
    @Inject
    private transient LoginController loginController;
    @Inject
    private transient PriceFactory priceFactory;
    //
    private Sale sale;
    private List<ItemSale> itemSales;
    private ItemSale itemSale;
    private BigDecimal payedAmount;
    private BigDecimal giveBackAmount;
    private List<Payment> payments;
    private Account paymentAccount;
    private boolean closeSale;
    private ItemSearch itemSearch;
    private List<Item> foundItems;

    public String actionNew() {
        Employee loggedEmployee = loginController.getLoggedEmployee();
        Company company = loggedEmployee.getCompany();

        sale = new Sale();
        itemSales = new ArrayList<>();
        sale.setCompany(company);

        itemSearch = new ItemSearch();
        itemSearch.setCompany(company);

        resetItemSale();
        resetPayements();

        return Views.SALE_DETAILS;
    }

    public String actionDetails(Sale sale) {
        this.sale = sale;
        itemSales = saleService.findSaleItems(sale);

        return Views.SALE_DETAILS;
    }

    public void actionAddCurrentItemSale() {
        Item item = itemSale.getItem();

        ZonedDateTime dateTime = ZonedDateTime.now();
        Price price = item.getCurrentPrice();

        itemSale.setItem(item);
        itemSale.setPrice(price);
        itemSale.setDateTime(dateTime);

        itemSales.add(itemSale);

        sale = saleService.calcSale(sale, itemSales);

        resetItemSale();
    }

    public void actionPay() {
        sale = saleService.pay(sale, itemSales, payments, closeSale);
    }

    public void handlePayedChange() {
        giveBackAmount = saleService.calcPayBackAmount(sale, itemSales, payedAmount);
    }

    public void handleSearchChanged() {
        Pagination<Item, ItemColumn> pagination = new Pagination<>();
        pagination.setMaxResults(10);
        foundItems = stockService.findItems(itemSearch, pagination);
        if (foundItems.size() == 1) {
            Item item = foundItems.get(0);
            itemSale.setItem(item);
            handleRowSelection();
        }
    }

    public void handleRowSelection() {
        Item item = itemSale.getItem();
        if (item != null) {
            Price currentPrice = item.getCurrentPrice();

            if (currentPrice != null) {
                BigDecimal vatExclusive = currentPrice.getVatExclusive();
                BigDecimal vatRate = currentPrice.getVatRate();

                Price newPrice = itemSale.getPrice();
                newPrice.setVatExclusive(vatExclusive);
                newPrice.setVatRate(vatRate);
            }
        }
    }

    private void resetItemSale() {
        Employee loggedEmployee = loginController.getLoggedEmployee();
        Company company = loggedEmployee.getCompany();
        Price newPrice = priceFactory.createPrice(company);
        BigDecimal quantity = BigDecimal.ONE;

        itemSale = new ItemSale();
        itemSale.setPrice(newPrice);
        itemSale.setSale(sale);
        itemSale.setQuantity(quantity);
    }

    private void resetPayements() {
        payments = new ArrayList<>();
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

    public Account getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(Account paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public boolean isCloseSale() {
        return closeSale;
    }

    public void setCloseSale(boolean closeSale) {
        this.closeSale = closeSale;
    }

    public ItemSearch getItemSearch() {
        return itemSearch;
    }

    public List<Item> getFoundItems() {
        return foundItems;
    }

}
