package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.model.accounting.AccountingEntry;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.commercial.SalePrice;
import be.valuya.comptoir.model.search.SaleSearch;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.AccountService;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.util.LoggedUser;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.SaleColumn;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.accounting.FromWsAccountingEntryConverter;
import be.valuya.comptoir.ws.convert.accounting.ToWsAccountingEntryConverter;
import be.valuya.comptoir.ws.convert.commercial.FromWsSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsSaleConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsSalePriceConverter;
import be.valuya.comptoir.ws.convert.search.FromWsSaleSearchConverter;
import be.valuya.comptoir.ws.rest.api.SaleResourceApi;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntry;
import be.valuya.comptoir.ws.rest.api.domain.accounting.WsAccountingEntryRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalePrice;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSalesSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsSaleSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.SaleStateChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.sse.Sse;
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
    private ToWsSalePriceConverter toWsSalePriceConverter;
    @Inject
    private FromWsAccountingEntryConverter fromWsAccountingEntryConverter;
    @Inject
    private ToWsAccountingEntryConverter toWsAccountingEntryConverter;
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

    public WsSalePrice findSalesTotalPayed(WsSaleSearch wsSaleSearch) {
        SaleSearch saleSearch = fromWsSaleSearchConverter.convert(wsSaleSearch);
        accessChecker.checkOwnCompany(saleSearch);

        SalePrice salesTotalPayed = saleService.getSalesTotalPayed(saleSearch);
        WsSalePrice wsSalePrice = toWsSalePriceConverter.convert(salesTotalPayed);
        return wsSalePrice;
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

}
