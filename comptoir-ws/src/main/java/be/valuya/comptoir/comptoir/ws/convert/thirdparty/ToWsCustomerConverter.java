package be.valuya.comptoir.comptoir.ws.convert.thirdparty;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomer;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.comptoir.ws.convert.company.ToWsCompanyConverter;
import be.valuya.comptoir.model.company.Company;
import be.valuya.comptoir.model.thirdparty.Customer;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class ToWsCustomerConverter {

    @Inject
    private ToWsCompanyConverter toWsCompanyConverter;

    public WsCustomer convert(Customer customer) {
        Long id = customer.getId();
        String firstName = customer.getFirstName();
        String lastName = customer.getLastName();
        String adress1 = customer.getAdress1();
        String adress2 = customer.getAdress2();
        String zip = customer.getZip();
        String city = customer.getCity();
        String email = customer.getEmail();
        String notes = customer.getNotes();
        String phone1 = customer.getPhone1();
        String phone2 = customer.getPhone2();

        Company company = customer.getCompany();
        WsCompanyRef companyRef = toWsCompanyConverter.reference(company);

        WsCustomer wsCustomer = new WsCustomer();
        wsCustomer.setId(id);
        wsCustomer.setCompanyRef(companyRef);
        wsCustomer.setFirstName(firstName);
        wsCustomer.setLastName(lastName);
        wsCustomer.setEmail(email);
        wsCustomer.setAdress1(adress1);
        wsCustomer.setAdress2(adress2);
        wsCustomer.setZip(zip);
        wsCustomer.setCity(city);
        wsCustomer.setNotes(notes);
        wsCustomer.setPhone1(phone1);
        wsCustomer.setPhone2(phone2);

        return wsCustomer;
    }

    public WsCustomerRef reference(Customer customer) {
        Long id = customer.getId();
        WsCustomerRef wsCustomerRef = new WsCustomerRef(id);
        return wsCustomerRef;
    }

}
