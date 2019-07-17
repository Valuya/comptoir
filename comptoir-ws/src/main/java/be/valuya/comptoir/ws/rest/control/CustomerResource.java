package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.search.WsCustomerSearch;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomer;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.search.CustomerLoyaltyAccountingEntrySearch;
import be.valuya.comptoir.model.search.CustomerSearch;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomerSearchResult;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.CustomerService;
import be.valuya.comptoir.util.pagination.CustomerColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.search.FromWsCustomerSearchConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsCustomerConverter;
import be.valuya.comptoir.ws.convert.thirdparty.ToWsCustomerConverter;
import be.valuya.comptoir.ws.rest.api.CustomerResourceApi;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class CustomerResource implements CustomerResourceApi {

    @EJB
    private CustomerService customerService;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Inject
    private FromWsCustomerConverter fromWsCustomerConverter;
    @Inject
    private ToWsCustomerConverter toWsCustomerConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private FromWsCustomerSearchConverter fromWsCustomerSearchConverter;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Inject
    private EmployeeAccessChecker accessChecker;

    public WsCustomerRef createCustomer(WsCustomer wsCustomer) {
        Customer customer = fromWsCustomerConverter.convert(wsCustomer);
        accessChecker.checkOwnCompany(customer);
        Customer savedCustomer = customerService.saveCustomer(customer);

        WsCustomerRef customerRef = toWsCustomerConverter.reference(savedCustomer);

        return customerRef;
    }

    public WsCustomerRef updateCustomer(long id, WsCustomer wsCustomer) {
        idChecker.checkId(id, wsCustomer);

        Customer existingCustomer = customerService.findCustomerById(id);
        accessChecker.checkOwnCompany(existingCustomer);
        Customer customer = fromWsCustomerConverter.patch(existingCustomer, wsCustomer);

        Customer savedCustomer = customerService.saveCustomer(customer);
        WsCustomerRef customerRef = toWsCustomerConverter.reference(savedCustomer);

        return customerRef;
    }

    public WsCustomer getCustomer(long id) {
        Customer customer = customerService.findCustomerById(id);
        accessChecker.checkOwnCompany(customer);

        WsCustomer wsCustomer = toWsCustomerConverter.convert(customer);

        return wsCustomer;
    }

    public BigDecimal getLoyaltyBalance(long id) {
        Customer customer = customerService.findCustomerById(id);
        accessChecker.checkOwnCompany(customer);
        Company company = customer.getCompany();

        CustomerLoyaltyAccountingEntrySearch accountingEntrySearch = new CustomerLoyaltyAccountingEntrySearch();
        accountingEntrySearch.setCustomer(customer);
        accountingEntrySearch.setCompany(company);
        BigDecimal customerLoyaltyAccountBalance = customerService.getCustomerLoyaltyAccountBalance(accountingEntrySearch);
        if (customerLoyaltyAccountBalance == null) {
            return BigDecimal.ZERO;
        }
        return customerLoyaltyAccountBalance;
    }

    public WsCustomerSearchResult  findCustomers(WsCustomerSearch wsCustomerSearch) {
        Pagination<Customer, CustomerColumn> pagination = restPaginationUtil.extractPagination(uriInfo, CustomerColumn::valueOf);

        CustomerSearch customerSearch = fromWsCustomerSearchConverter.convert(wsCustomerSearch);
        accessChecker.checkOwnCompany(customerSearch);
        List<Customer> customers = customerService.findCustomers(customerSearch, pagination);

        List<WsCustomerRef> wsCustomers = customers.stream()
                .map(toWsCustomerConverter::reference)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);
        return restPaginationUtil.setResults(new WsCustomerSearchResult(), wsCustomers, pagination);
    }


}
