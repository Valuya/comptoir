package be.valuya.comptoir.ws.convert.thirdparty;

import be.valuya.comptoir.ws.rest.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomer;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.service.CustomerService;
import be.valuya.comptoir.ws.convert.company.FromWsCompanyConverter;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Optional;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsCustomerConverter {

    @EJB
    private CustomerService customerService;
    @Inject
    private FromWsCompanyConverter fromWsCompanyConverter;

    public Customer convert(WsCustomer wsCustomer) {
        if (wsCustomer == null) {
            return null;
        }
        Customer customer = new Customer();

        customer = patch(customer, wsCustomer);

        return customer;
    }

    public Customer patch(Customer customer, WsCustomer wsCustomer) {
        Long id = wsCustomer.getId();
        String firstName = wsCustomer.getFirstName();
        String lastName = wsCustomer.getLastName();
        String adress1 = wsCustomer.getAdress1();
        String adress2 = wsCustomer.getAdress2();
        String zip = wsCustomer.getZip();
        String city = wsCustomer.getCity();
        String email = wsCustomer.getEmail();
        String notes = wsCustomer.getNotes();
        String phone1 = wsCustomer.getPhone1();
        String phone2 = wsCustomer.getPhone2();
        BigDecimal discountRate = wsCustomer.getDiscountRate();
        boolean discountCumulable = Optional.ofNullable(wsCustomer.getDiscountCumulable())
                .orElse(false);

        WsCompanyRef companyRef = wsCustomer.getCompanyRef();
        Company company = fromWsCompanyConverter.find(companyRef);

        customer.setId(id);
        customer.setCompany(company);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setAdress1(adress1);
        customer.setAdress2(adress2);
        customer.setZip(zip);
        customer.setCity(city);
        customer.setNotes(notes);
        customer.setPhone1(phone1);
        customer.setPhone2(phone2);
        customer.setDiscountRate(discountRate);
        customer.setDiscountCumulable(discountCumulable);
        return customer;
    }

    public Customer find(WsCustomerRef customerRef) {
        if (customerRef == null) {
            return null;
        }
        Long id = customerRef.getId();
        return customerService.findCustomerById(id);
    }

}
