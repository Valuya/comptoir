package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePriceDetails;
import be.valuya.comptoir.model.event.SaleUpdateEvent;
import be.valuya.comptoir.model.search.SaleSearch;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.service.AccountingUtils;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.util.LoggedUser;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.SaleColumn;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.accounting.FromWsAccountingEntryConverter;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountingEntryConverter;
import be.valuya.comptoir.ws.convert.commercial.FromWsSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsSalePriceDetailsConverter;
import be.valuya.comptoir.ws.convert.search.FromWsSaleSearchConverter;
import be.valuya.comptoir.ws.rest.api.SaleResourceApi;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntryRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalePriceDetails;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalesSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsSaleSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.service.ComptoirEventService;
import be.valuya.comptoir.ws.rest.service.EmployeeSaleEventSubscription;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.SaleStateChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class SaleResource implements SaleResourceApi {

    @EJB
    private SaleService saleService;
    @EJB
    private AccountService accountService;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;
    @Inject
    private FromWsSaleSearchConverter fromWsSaleSearchConverter;
    @Inject
    private ToWsSaleConverter toWsSaleConverter;
    @Inject
    private FromWsAccountingEntryConverter fromWsAccountingEntryConverter;
    @Inject
    private ToWsAccountingEntryConverter toWsAccountingEntryConverter;
    @Inject
    private ToWsSalePriceDetailsConverter toWsSalePriceDetailsConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private SaleStateChecker saleStateChecker;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Context
    private Sse sse;
    @Inject
    private EmployeeAccessChecker accessChecker;
    @Inject
    private ComptoirEventService eventService;
    @Inject
    @LoggedUser
    private Employee loggedEmployee;
    @Inject
    private Logger logger;

    public WsSaleRef createSale(WsSale wsSale) {
        Sale sale = fromWsSaleConverter.convert(wsSale);
        accessChecker.checkOwnCompany(sale);
        Sale savedSale = saleService.saveSale(sale);

        WsSaleRef saleRef = toWsSaleConverter.reference(savedSale);

        return saleRef;
    }

    public WsSaleRef updateSale(long id, WsSale wsSale) {
        idChecker.checkId(id, wsSale);

        Sale existingSale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(existingSale);
        Sale updatedSale = fromWsSaleConverter.patch(existingSale, wsSale);

        Sale savedSale = saleService.saveSale(updatedSale);

        WsSaleRef saleRef = toWsSaleConverter.reference(savedSale);

        return saleRef;
    }

    public WsSale getSale(long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        WsSale wsSale = toWsSaleConverter.convert(sale);

        return wsSale;
    }

    public WsSalesSearchResult findSales(PaginationParams paginationParams, WsSaleSearch wsSaleSearch) {
        Pagination<Sale, SaleColumn> pagination = restPaginationUtil.extractPagination(uriInfo, SaleColumn::valueOf);
        SaleSearch saleSearch = fromWsSaleSearchConverter.convert(wsSaleSearch);
        accessChecker.checkOwnCompany(saleSearch);

        List<Sale> sales = saleService.findSales(saleSearch, pagination);

        List<WsSaleRef> wsSales = sales.stream()
                .map(toWsSaleConverter::reference)
                .collect(Collectors.toList());

        return restPaginationUtil.setResults(new WsSalesSearchResult(), wsSales, pagination);
    }

    public BigDecimal findSalesTotalPayed(WsSaleSearch wsSaleSearch) {
        SaleSearch saleSearch = fromWsSaleSearchConverter.convert(wsSaleSearch);
        accessChecker.checkOwnCompany(saleSearch);

        BigDecimal salesTotalPayed = saleService.getSalesTotalPayed(saleSearch);
        return salesTotalPayed;
    }

    public void deleteSale(long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        saleService.cancelOpenSale(sale);
    }

    public WsSaleRef closeSale(long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        saleStateChecker.checkState(sale, false); // TODO: replace with bean validation

        sale = saleService.closeSale(sale);

        WsSaleRef saleRef = toWsSaleConverter.reference(sale);

        return saleRef;
    }

    public WsSaleRef openSale(long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        saleStateChecker.checkState(sale, true); // TODO: replace with bean validation

        sale = saleService.reopenSale(sale);

        WsSaleRef saleRef = toWsSaleConverter.reference(sale);

        return saleRef;
    }

    public String getSaleTotalPayed(long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        return saleService.getSaleTotalPayed(sale).toPlainString();
    }

    @Override
    public WsAccountingEntryRef addSalePayment(long id, WsAccountingEntry paymentEntry) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);

        AccountingEntry accountingEntry = fromWsAccountingEntryConverter.convert(paymentEntry);
        AccountingEntry addedEntry = saleService.addPayment(sale, accountingEntry);

        WsAccountingEntryRef accountingEntryRef = toWsAccountingEntryConverter.reference(addedEntry);
        return accountingEntryRef;
    }

    @Override
    public void deleteSalePayment(long id, long entryId) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);
        AccountingEntry accountingEntry = accountService.findAccountingEntryById(entryId);

        saleService.deletePayment(sale, accountingEntry);
    }

    @Override
    public WsSalePriceDetails getSalePrice(long id) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);

        List<ItemVariantSale> itemVariantSales = saleService.findItemSales(sale);
        SalePriceDetails salePriceDetails = AccountingUtils.calcSalePriceDetails(sale, itemVariantSales);

        WsSalePriceDetails wsSalePriceDetails = toWsSalePriceDetailsConverter.convert(salePriceDetails, sale);
        return wsSalePriceDetails;
    }

    @Override
    public WsSalePriceDetails setSaleDiscountRatio(long id, @NotNull BigDecimal discountRatio) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);

        sale.setDiscountRatio(discountRatio);
        Sale updatedSale = saleService.saveSale(sale);

        List<ItemVariantSale> itemVariantSales = saleService.findItemSales(updatedSale);
        SalePriceDetails salePriceDetails = AccountingUtils.calcSalePriceDetails(updatedSale, itemVariantSales);

        WsSalePriceDetails wsSalePriceDetails = toWsSalePriceDetailsConverter.convert(salePriceDetails, updatedSale);
        return wsSalePriceDetails;
    }

    @Override
    public WsSalePriceDetails setSaleDiscountAmount(long id, @NotNull BigDecimal discountAmount) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);

        List<ItemVariantSale> itemVariantSales = saleService.findItemSales(sale);
        BigDecimal discountRate = AccountingUtils.calcDiscountRateFromDiscountAmount(sale, itemVariantSales, discountAmount);
        sale.setDiscountRatio(discountRate);
        Sale updatedSale = saleService.saveSale(sale);

        SalePriceDetails salePriceDetails = AccountingUtils.calcSalePriceDetails(updatedSale, itemVariantSales);

        WsSalePriceDetails wsSalePriceDetails = toWsSalePriceDetailsConverter.convert(salePriceDetails, updatedSale);
        return wsSalePriceDetails;
    }

    @Override
    public WsSalePriceDetails setSaleTotalVatInclusive(long id, @NotNull BigDecimal totalVatInclusive) {
        Sale sale = saleService.findSaleById(id);
        accessChecker.checkOwnCompany(sale);

        List<ItemVariantSale> itemVariantSales = saleService.findItemSales(sale);
        BigDecimal discountRate = AccountingUtils.calcDiscountRateFromTotalVatInclusive(sale, itemVariantSales, totalVatInclusive);
        sale.setDiscountRatio(discountRate);
        Sale updatedSale = saleService.saveSale(sale);

        SalePriceDetails salePriceDetails = AccountingUtils.calcSalePriceDetails(updatedSale, itemVariantSales);

        WsSalePriceDetails wsSalePriceDetails = toWsSalePriceDetailsConverter.convert(salePriceDetails, updatedSale);
        return wsSalePriceDetails;
    }

    public void registerToSaleEvents(long id, @Context SseEventSink eventSink) {
        WsSaleRef wsSaleRef = new WsSaleRef(id);
        EmployeeSaleEventSubscription subscription = eventService.subscribeToSale(loggedEmployee, wsSaleRef, eventSink);

        eventService.sendSaleUpdate(sse, subscription);
        eventService.sendSaleItemsUpdate(sse, subscription);
        eventService.sendSaleAccountingEntriesUpdate(sse, subscription);
    }

    public void notifySaleUpdated(@Observes SaleUpdateEvent updateEvent) {
        Sale sale = updateEvent.getSale();
        WsSaleRef saleRef = new WsSaleRef(sale.getId());

        eventService.getSaleSubscriptions(saleRef)
                .forEach(this::notifySaleUpdated);
    }

    private void notifySaleUpdated(EmployeeSaleEventSubscription employeeSaleEventSubscription) {
        WsSaleRef saleRef = employeeSaleEventSubscription.getSaleRef();
        Employee employee = employeeSaleEventSubscription.getEmployee();

        eventService.sendSaleUpdate(sse, employeeSaleEventSubscription);
        eventService.sendSaleItemsUpdate(sse, employeeSaleEventSubscription);
        eventService.sendSaleAccountingEntriesUpdate(sse, employeeSaleEventSubscription);

        logger.fine("Notified sale " + saleRef + " update to " + employee);
    }

}
